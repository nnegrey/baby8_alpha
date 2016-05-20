package edu.calpoly.nnegrey.baby8alpha;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Created by noahnegrey on 5/14/16.
 */
public class CreatePatternEffect extends AppCompatActivity {
    protected Button btnLighter;
    protected Button btnSound;
    protected Menu m_vwMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pattern_effect);

        btnLighter = (Button) findViewById(R.id.buttonCreateEffectLighter);
        btnSound = (Button) findViewById(R.id.buttonCreateEffectSound);

        initLayout();
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
        btnLighter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("INDEX", getIntent().getIntExtra("INDEX", -1));
                intent.putExtra("EFFECT","Lighter");
                setResult(Activity.RESULT_OK, intent);
                finish();//finishing activity
            }
        });

        btnSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("INDEX", getIntent().getIntExtra("INDEX", -1));
                intent.putExtra("EFFECT","Sound");
                setResult(Activity.RESULT_OK, intent);
                finish();//finishing activity
            }
        });
    }

    protected void startRemoteActivity() {
        Intent i = new Intent(this, Remote.class);
        startActivity(i);
    }
}
