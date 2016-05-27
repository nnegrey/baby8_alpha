package edu.calpoly.nnegrey.baby8alpha;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * Created by noahnegrey on 5/11/16.
 */
public class CreatePattern extends AppCompatActivity {
    private int REQUEST_CODE_COMMAND = 1;
    private int REQUEST_CODE_EFFECT = 2;

    protected Button buttonSave;
    protected EditText editTextPatternName;
    protected RecyclerView commandLayout;
    protected FloatingActionButton fab_command;
    protected FloatingActionButton fab_effect;

    private String patternName;
    private ArrayList<Command> commands;

    protected CommandListAdapter commandAdapter;

    protected int position;
    protected Menu m_vwMenu;

    private boolean loadIntent = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pattern);

        buttonSave = (Button) findViewById(R.id.createPatternSavePattern);
        editTextPatternName = (EditText) findViewById(R.id.createPatternEditText);

        if (savedInstanceState == null) {
            commands = new ArrayList<>();
            loadIntent = true;
        }
        else {
            commands = savedInstanceState.getParcelableArrayList("COMMANDS");
        }

        commandAdapter = new CommandListAdapter(commands);
        commandLayout = (RecyclerView) findViewById(R.id.createPatternViewGroup);
        fab_command = (FloatingActionButton) findViewById(R.id.createPatternFloatingActionButtonCommand);
        fab_command.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCreatePatternCommand();
            }
        });
        fab_effect = (FloatingActionButton) findViewById(R.id.createPatternFloatingActionButtonEffect);
        fab_effect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCreatePatternEffect();
            }
        });
        commandLayout.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        commandLayout.setAdapter(commandAdapter);

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
                commandAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int index = viewHolder.getAdapterPosition();
                position = index;
                if (direction == ItemTouchHelper.LEFT) {
                    editCommand();
                } else if (direction == ItemTouchHelper.RIGHT) {
                    deleteCommand();
                    commandAdapter.notifyItemRemoved(index);
                }

            }

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(commandLayout);
        initLayout();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("COMMANDS", commands);
        super.onSaveInstanceState(outState);
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

    protected void initLayout() {
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTextPatternName.getWindowToken(), 0);

                patternName = editTextPatternName.getText().toString();
                if (!patternName.isEmpty()) {
                    editTextPatternName.setText("");
                    Intent intent = new Intent();
                    intent.putExtra("INDEX", getIntent().getIntExtra("INDEX", -1));
                    intent.putExtra("PATTERN_NAME", patternName);
                    intent.putParcelableArrayListExtra("COMMANDS", commands);
                    setResult(Activity.RESULT_OK, intent);
                    finish();//finishing activity
                }
            }
        });

        editTextPatternName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTextPatternName.getWindowToken(), 0);

                if (keyCode == KeyEvent.KEYCODE_ENTER ||
                        keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                    patternName = editTextPatternName.getText().toString();
                    if (!patternName.isEmpty()) {
                        editTextPatternName.setText("");
                        Intent intent = new Intent();
                        intent.putExtra("INDEX", getIntent().getIntExtra("INDEX", -1));
                        intent.putExtra("PATTERN_NAME", patternName);
                        intent.putParcelableArrayListExtra("COMMANDS", commands);
                        setResult(Activity.RESULT_OK, intent);
                        finish();//finishing activity
                    }
                    return true;
                }
                return false;
            }
        });

        Intent i = getIntent();
        if (i.getIntExtra("INDEX", -1) != -1 && loadIntent) {
            Toast.makeText(this, "ADDING AGAIN", Toast.LENGTH_SHORT).show();
            editTextPatternName.setText(i.getStringExtra("PATTERN_NAME"));
            ArrayList<Command> cs = i.getParcelableArrayListExtra("COMMANDS");
            for (Command c : cs) {
                if (c.getEffect() != null && c.getEffect().isEmpty()) {
                    addCommandFromDatabase(c.getDirection(), c.getVelocity(), c.getDuration(), c.getHeadDegree(), c.getId(), c.getPatternId());
                }
                else {
                    addCommandFromDataBase(c.getEffect(), c.getId(), c.getPatternId());
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == REQUEST_CODE_COMMAND) {
            if (resultCode == Activity.RESULT_OK){
                int index = data.getIntExtra("INDEX", -1);
                int direction = data.getIntExtra("DIRECTION", 0);
                int velocity = data.getIntExtra("VELOCITY", 0);
                int duration = data.getIntExtra("DURATION", 0);
                int head_degree = data.getIntExtra("HEAD_DEGREE", 0);
                if (index == -1) {
                    addCommand(direction, velocity, duration, head_degree);
                }
                else {
                    commands.get(index).setDirection(direction);
                    commands.get(index).setVelocity(velocity);
                    commands.get(index).setDuration(duration);
                    commands.get(index).setHeadDegree(head_degree);
                    commandAdapter.notifyDataSetChanged();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
            }
        }
        else if (requestCode == REQUEST_CODE_EFFECT) {
            if (resultCode == Activity.RESULT_OK){
                int index = data.getIntExtra("INDEX", -1);
                String effect = data.getStringExtra("EFFECT");
                if (index == -1) {
                    addCommand(effect);
                }
                else {
                    commands.get(index).setEffect(effect);
                    commandAdapter.notifyDataSetChanged();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }

    protected void startCreatePatternCommand() {
        Intent i = new Intent(this, CreatePatternCommand.class);
        startActivityForResult(i, REQUEST_CODE_COMMAND);
    }

    protected void startCreatePatternEffect() {
        Intent i = new Intent(this, CreatePatternEffect.class);
        startActivityForResult(i, REQUEST_CODE_EFFECT);
    }

    protected void addCommand(int direction, int velocity, int duration, int head_degree) {
        Command command = new Command(direction, velocity, duration, head_degree);

        if (!commands.contains(command)) {
            this.commands.add(command);
            commandAdapter.notifyDataSetChanged();
        }
    }

    protected void addCommandFromDatabase(int direction, int velocity, int duration, int head_degree, long id, long patternId) {
        Command command = new Command(direction, velocity, duration, head_degree, id, patternId);

        if (!commands.contains(command)) {
            this.commands.add(command);
            commandAdapter.notifyDataSetChanged();
        }
    }

    protected void addCommand(String effect) {
        Command command = new Command(effect);

        if (!commands.contains(command)) {
            this.commands.add(command);
            commandAdapter.notifyDataSetChanged();
        }
    }

    protected void addCommandFromDataBase(String effect, long id, long patternId) {
        Command command = new Command(effect, id, patternId);

        if (!commands.contains(command)) {
            this.commands.add(command);
            commandAdapter.notifyDataSetChanged();
        }
    }

    protected void deleteCommand() {
        if (commands.get(position).getId() != -1) {
            CommandDataSource commandDataSource = new CommandDataSource(this);
            commandDataSource.open();
            commandDataSource.deleteCommand(commands.get(position));
            commandDataSource.close();
        }

        commands.remove(position);

        commandAdapter.notifyDataSetChanged();
    }

    protected void editCommand() {
        Command c = commands.get(position);
        if (c.getType() == 0) {
            Intent i = new Intent(this, CreatePatternCommand.class);
            i.putExtra("INDEX", position);
            i.putExtra("DIRECTION", c.getDirection());
            i.putExtra("VELOCITY", c.getVelocity());
            i.putExtra("DURATION", c.getDuration());
            i.putExtra("HEAD_DEGREE", c.getHeadDegree());
            startActivityForResult(i, REQUEST_CODE_COMMAND);
        }
        else {
            Intent i = new Intent(this, CreatePatternEffect.class);
            i.putExtra("INDEX", position);
            startActivityForResult(i, REQUEST_CODE_EFFECT);
        }
    }

    protected void startRemoteActivity() {
        Intent i = new Intent(this, Remote.class);
        startActivity(i);
    }
}
