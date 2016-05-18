package edu.calpoly.nnegrey.baby8alpha;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by noahnegrey on 5/14/16.
 */
public class CreatePatternEffect extends AppCompatActivity {
    protected Button btnLighter;
    protected Button btnSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pattern_effect);

        btnLighter = (Button) findViewById(R.id.buttonCreateEffectLighter);
        btnSound = (Button) findViewById(R.id.buttonCreateEffectSound);

        initLayout();
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
}
