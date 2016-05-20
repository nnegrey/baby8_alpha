package edu.calpoly.nnegrey.baby8alpha;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class PatternList extends AppCompatActivity {

    protected ArrayList<Pattern> patterns;
    protected PatternListAdapter patternListAdapter;
    protected RecyclerView patternLayout;
    protected Menu m_vwMenu;
    private int position;
    protected FloatingActionButton fab;
    private int REQUEST_CODE_CREATE = 1;
    private PatternDataSource patternDataSource;
    private CommandDataSource commandDataSource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patterns);
        fab = (FloatingActionButton) findViewById(R.id.patternsFloatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCreatePattern();
            }
        });
        patterns = new ArrayList<>();
        patternListAdapter = new PatternListAdapter(patterns);
        patternLayout = (RecyclerView) findViewById(R.id.patternsViewGroup);
        patternLayout.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        patternLayout.setAdapter(patternListAdapter);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT |
                ItemTouchHelper.UP | ItemTouchHelper.DOWN) {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                patternListAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                for (int i = patterns.size() - 1; i >= 0; i--) {
                    patternDataSource.updatePatternOrder(patterns.get(i));
                }
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int index = viewHolder.getAdapterPosition();
                position = index;
                if (direction == ItemTouchHelper.LEFT) {
                    editPattern();
                    patternListAdapter.notifyDataSetChanged();
                } else if (direction == ItemTouchHelper.RIGHT) {
                    deletePattern();
                    patternListAdapter.notifyItemRemoved(index);
                }
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(patternLayout);

        patternDataSource = new PatternDataSource(this);
        patternDataSource.open();
        commandDataSource = new CommandDataSource(this);
        commandDataSource.open();

        List<Pattern> ps = patternDataSource.getAllPatterns();
        int counter = 0;
        for (int i = 0; i < ps.size(); i++) {
            Pattern pattern = ps.get(i);
            List<Command> cs = commandDataSource.getAllCommands(pattern.getId());
            counter++;
            addPatternFromDatabase(new Pattern(pattern.getPatternName(), (ArrayList) cs, pattern.getId()));
        }
        if (counter != ps.size()) {
            Toast.makeText(this, "INCORRECT NUMBER OF PATTERNS Got: " + counter + " DB: " + ps.size(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        m_vwMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_pattern:
//                finish();
                startRemoteActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void addPatternFromDatabase(Pattern pattern) {
        if (!patterns.contains(pattern)) {
            patterns.add(pattern);
            patternListAdapter.notifyDataSetChanged();
        }
    }


    protected void addPattern(String patternName, long order, ArrayList<Command> commands) {
        ArrayList<Command> newCommands = new ArrayList<>();
        Pattern pattern = patternDataSource.createPattern(patternName, order);
        long id = pattern.getId();

        for (Command c : commands) {
            newCommands.add(commandDataSource.createCommand(id, c.getType(), c.getDirection(),
                            c.getVelocity(), c.getDuration(), c.getHeadDegree(), c.getEffect()));
        }
        pattern.setCommands(newCommands);

        if (!patterns.contains(pattern)) {
            patterns.add(pattern);
            patternListAdapter.notifyDataSetChanged();
        }
        else {
            // Delete the commands we were just about to add.
            // TODO Revert this to skip even doing this.
            for (Command c : newCommands) {
                commandDataSource.deleteCommand(c);
            }
        }
    }

    protected void deletePattern() {
        for (Command c : patterns.get(position).getCommands()) {
            commandDataSource.deleteCommand(c);
        }
        patternDataSource.deletePattern(patterns.get(position));
        patterns.remove(position);
        patternListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CREATE) {
            if (resultCode == Activity.RESULT_OK){
                int index = data.getIntExtra("INDEX", -1);
                if (index == -1) {
                    addPattern(data.getStringExtra("PATTERN_NAME"), patterns.size(), data.<Command>getParcelableArrayListExtra("COMMANDS"));
                }
                else {
                    patterns.get(index).setPatternName(data.getStringExtra("PATTERN_NAME"));
                    patterns.get(index).setCommands(data.<Command>getParcelableArrayListExtra("COMMANDS"));
                    patternListAdapter.notifyDataSetChanged();

                    ArrayList<Command> newCommands = new ArrayList<>();
                    for (Command c : patterns.get(index).getCommands()) {
                        if (c.getId() == -1) {
                            Command newCommand = commandDataSource.createCommand(patterns.get(index).getId(), c.getType(), c.getDirection(), c.getVelocity(), c.getDuration(), c.getHeadDegree(), c.getEffect());
                            newCommands.add(newCommand);
                        }
                        else {
                            commandDataSource.updateCommand(c);
                            newCommands.add(c);
                        }
                    }
                    patterns.get(index).setCommands(newCommands);
                    patternDataSource.updatePatternValues(patterns.get(index));
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }

    protected void startCreatePattern() {
        Intent i = new Intent(this, CreatePattern.class);
        startActivityForResult(i, REQUEST_CODE_CREATE);
    }

    protected void editPattern() {
        Pattern p = patterns.get(position);

        Intent i = new Intent(this, CreatePattern.class);
        i.putExtra("INDEX", position);
        i.putExtra("PATTERN_NAME", p.getPatternName());
        i.putParcelableArrayListExtra("COMMANDS", p.getCommands());
        startActivityForResult(i, REQUEST_CODE_CREATE);
    }

    protected void startRemoteActivity() {
        Intent i = new Intent(this, Remote.class);
        startActivity(i);
    }
}