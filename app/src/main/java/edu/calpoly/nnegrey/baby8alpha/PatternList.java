package edu.calpoly.nnegrey.baby8alpha;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PatternList extends AppCompatActivity {

    protected ArrayList<Pattern> patterns;
    protected PatternListAdapter patternListAdapter;
    protected RecyclerView patternLayout;

    /** Menu used for filtering Jokes. */
    protected Menu m_vwMenu;
    private ActionMode m_actionMode;
    private ActionMode.Callback m_callBack;

    /** Background Color values used for alternating between light and dark rows
     *  of Jokes. Add a third for text color if necessary. */
    protected int m_nDarkColor;
    protected int m_nLightColor;
    protected int m_nTextColor;

    private int position;

    protected FloatingActionButton fab;
    private int REQUEST_CODE_CREATE = 1;

    private PatternDataSource patternDataSource;
    private CommandDataSource commandDataSource;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patterns);
        // TODO
        Resources resource = this.getResources();
        m_nDarkColor = resource.getColor(R.color.dark);
        m_nLightColor = resource.getColor(R.color.light);
        m_nTextColor = resource.getColor(R.color.text);
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

//        if (savedInstanceState != null) {
//            patterns = savedInstanceState.getParcelableArrayList("PATTERNS");
//        }

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
                for (Pattern p : patterns) {
                    patternDataSource.updateOrder(p);
                }
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int index = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.LEFT) {
                    editPattern();
                    patternListAdapter.notifyDataSetChanged();
                } else if (direction == ItemTouchHelper.RIGHT) {
                    position = index;
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

        initLayout();

        patternDataSource = new PatternDataSource(this);
        patternDataSource.open();
        commandDataSource = new CommandDataSource(this);
        commandDataSource.open();

        List<Pattern> ps = patternDataSource.getAllPatterns();
        for (long i = 0; i < ps.size(); i++) {
            Pattern pattern;
            for (int k = 0; k < ps.size(); k++) {
                if (i == ps.get(k).getDisplayOrder()) {
                    pattern = ps.get(k);
                    List<Command> c = commandDataSource.getAllCommands(pattern.getId());
                    addPatternFromDatabase(new Pattern(pattern.getPatternName(), (ArrayList) c, pattern.getId()));
                }
            }
        }

//        for (Pattern pattern : patternDataSource.getAllPatterns()) {
//            List<Command> c = commandDataSource.getAllCommands(pattern.getId());
//            addPatternFromDatabase(new Pattern(pattern.getPatternName(), (ArrayList) c, pattern.getId()));
//        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        outState.putParcelableArrayList("PATTERNS", patterns);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.mainmenu, menu);
//        m_vwMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.menu_filter:
//                return true;
//            case R.id.submenu_like:
//                setFilteredJokeList(Joke.LIKE);
//                m_filter = Joke.LIKE;
//                return true;
//            case R.id.submenu_dislike:
//                setFilteredJokeList(Joke.DISLIKE);
//                m_filter = Joke.DISLIKE;
//                return true;
//            case R.id.submenu_unrated:
//                setFilteredJokeList(Joke.UNRATED);
//                m_filter = Joke.UNRATED;
//                return true;
//            case R.id.submenu_show_all:
//                setFilteredJokeList(-1);
//                m_filter = -1;
//                return true;
//            case R.id.submenu_download_all:
//                Toast.makeText(PatternList.this, "Downloading Jokes", Toast.LENGTH_SHORT).show();
//                AsyncGetJokeTask agjt = new AsyncGetJokeTask();
//                agjt.execute();
//                setFilteredJokeList(-1);
//                m_filter = -1;
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Method is used to encapsulate the code that initializes and sets the
     * Layout for this Activity.
     */
    protected void initLayout() {
        m_callBack = new ActionMode.Callback() {

            // Called when the action mode is created; startActionMode() was called
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Inflate a menu resource providing context menu items
//                MenuInflater inflater = mode.getMenuInflater();
//                inflater.inflate(R.menu.actionmenu, menu);
                return true;
            }

            // Called each time the action mode is shown. Always called after onCreateActionMode, but
            // may be called multiple times if the mode is invalidated.
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false; // Return false if nothing is done
            }

            // Called when the user selects a contextual menu item
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
//                    case R.id.menu_remove:
//                        deleteJoke();
//                        mode.finish(); // Action picked, so close the CAB
//                        return true;
                    default:
                        return false;
                }
            }

            // Called when the user exits the action mode
            @Override
            public void onDestroyActionMode(ActionMode mode) {
                m_actionMode = null;
            }
        };
    }

    protected void addPatternFromDatabase(Pattern pattern) {
        if (!patterns.contains(pattern)) {
            patterns.add(pattern);
            patternListAdapter.notifyDataSetChanged();
        }
    }


    protected void addPattern(String patternName, long order, ArrayList<Command> commands) {
//        Pattern p = new Pattern(patternName, commands);
        ArrayList<Command> newCommands = new ArrayList<>();
        Pattern pattern = patternDataSource.createPattern(patternName, order);
        pattern.setDisplayOrder(patterns.size());
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
    }

    protected void deletePattern() {
        for (Command c : patterns.get(position).getCommands()) {
            commandDataSource.deleteCommand(c);
        }

        for (int i = position; i < patterns.size(); i++) {
            patterns.get(i).setDisplayOrder(patterns.get(i).getDisplayOrder() - 1);
        }

        patternDataSource.deletePattern(patterns.get(position));
        patterns.remove(position);
        for (Pattern p : patterns) {
            patternDataSource.updateOrder(p);
        }
        patternListAdapter.notifyDataSetChanged();
    }

    public class AsyncGetJokeTask extends AsyncTask<String, Void, Boolean> {
        public ArrayList<String> jokes;
        @Override
        protected Boolean doInBackground(String... params) {
            jokes = new ArrayList<>();
            try {
                URL url = new URL("http://simexusa.com/aac/getAllJokes.php");
                URLConnection connection = url.openConnection();
                connection.connect();
                InputStream input = new BufferedInputStream(url.openStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                String joke;
                while ((joke = reader.readLine()) != null) {
                    if (!jokes.contains(joke) && joke != "") {
                        jokes.add(joke);
                    }
                }
                reader.close();
                input.close();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            Toast.makeText(PatternList.this, "Finished Downloading Jokes", Toast.LENGTH_SHORT).show();
            for (String joke : jokes) {
//                addJoke(joke);
            }
        }
    }

    public class AsyncPostJokeTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
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

    public void test() {
        if (Remote.isConnected) {

        }
    }
}