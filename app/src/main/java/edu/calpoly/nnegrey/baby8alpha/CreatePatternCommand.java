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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.triggertrap.seekarc.SeekArc;

/**
 * Created by noahnegrey on 5/11/16.
 */
public class CreatePatternCommand extends AppCompatActivity {

    protected SeekArc seekArcDirection;

    protected RadioGroup radioGroup;
    protected RadioButton radioButtonSpeed1;
    protected RadioButton radioButtonSpeed2;
    protected RadioButton radioButtonSpeed3;
    protected RadioButton radioButtonSpeed4;
    protected RadioButton radioButtonSpeed5;
    protected EditText editTextDuration;
    protected SeekArc seekArcHead;
    protected Button buttonSave;

    protected Menu m_vwMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pattern_command);

        seekArcDirection = (SeekArc) findViewById(R.id.createPatternCommandSeekArcDirection);

        radioGroup = (RadioGroup) findViewById(R.id.createPatternCommandRadioGroup);
        radioButtonSpeed1 = (RadioButton) findViewById(R.id.createPatternCommandRadioButtonSpeed1);
        radioButtonSpeed2 = (RadioButton) findViewById(R.id.createPatternCommandRadioButtonSpeed2);
        radioButtonSpeed3 = (RadioButton) findViewById(R.id.createPatternCommandRadioButtonSpeed3);
        radioButtonSpeed4 = (RadioButton) findViewById(R.id.createPatternCommandRadioButtonSpeed4);
        radioButtonSpeed5 = (RadioButton) findViewById(R.id.createPatternCommandRadioButtonSpeed5);

        editTextDuration = (EditText) findViewById(R.id.createPatternCommandEditTextDuration);

        seekArcHead = (SeekArc) findViewById(R.id.createPatternCommandSeekArcHead);

        buttonSave = (Button) findViewById(R.id.createPatternCommandSaveButton);

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
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("INDEX", getIntent().getIntExtra("INDEX", -1));
                intent.putExtra("DIRECTION", getDirection());
                intent.putExtra("VELOCITY", getSelectedSpeed());
                intent.putExtra("DURATION", getDuration());
                intent.putExtra("HEAD_DEGREE", getHeadDegree());
                setResult(Activity.RESULT_OK, intent);

                finish();//finishing activity
            }
        });

        Intent i = getIntent();
        if (i.getIntExtra("INDEX", -1) != -1) {
            seekArcDirection.setProgress(i.getIntExtra("DIRECTION", 0));
            setSelectedSpeed(i.getIntExtra("VELOCITY", 0));
            editTextDuration.setText(String.valueOf(i.getIntExtra("DURATION", 0)));
            seekArcHead.setProgress(i.getIntExtra("HEAD_DEGREE", 0));
        }
    }

    private int getDirection() {
        return seekArcDirection.getProgress();
    }

    private int getSelectedSpeed() {
        if (radioButtonSpeed1.isChecked()) {
            return 1;
        }
        else if (radioButtonSpeed2.isChecked()) {
            return 2;
        }
        else if (radioButtonSpeed3.isChecked()) {
            return 3;
        }
        else if (radioButtonSpeed4.isChecked()) {
            return 4;
        }
        else if (radioButtonSpeed5.isChecked()) {
            return 5;
        }
        return 0;
    }

    private void setSelectedSpeed(int s) {
        if (s == 1) {
            radioButtonSpeed1.setChecked(true);
        }
        else if (s == 2) {
            radioButtonSpeed2.setChecked(true);
        }
        else if (s == 3) {
            radioButtonSpeed3.setChecked(true);
        }
        else if (s == 4) {
            radioButtonSpeed4.setChecked(true);
        }
        else if (s == 5) {
            radioButtonSpeed5.setChecked(true);
        }
    }

    private int getDuration() {
        String duration = editTextDuration.getText().toString();
        if (duration != null && !duration.equals("")) {
            return Integer.valueOf(duration);
        }

        return 0;
    }

    private int getHeadDegree() {
        return seekArcHead.getProgress();
    }

    protected void startRemoteActivity() {
        Intent i = new Intent(this, Remote.class);
        startActivity(i);
    }
}
