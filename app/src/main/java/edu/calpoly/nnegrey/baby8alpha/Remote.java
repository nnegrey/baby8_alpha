package edu.calpoly.nnegrey.baby8alpha;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

public final class Remote extends AppCompatActivity {
    private RelativeLayout layout_joystick;
    private RelativeLayout layout_joystick2;
    private Joystick js;
    private Joystick js2;
    private TextView velocity;
    private TextView headDegree;
    private Button buttonLeft;
    private Button buttonRight;
    private Button buttonSound;
    private Button buttonEffect;

    private BluetoothAdapter bluetoothAdapter;
    private Set<BluetoothDevice> pairedDevices;
    public static BluetoothDevice bb_8;
    public static OutputStream outputStream = null;
    public static boolean isConnected = false;

    protected Menu m_vwMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote);

        velocity = (TextView) findViewById(R.id.textViewVelocity);
        layout_joystick = (RelativeLayout)findViewById(R.id.layout_joystick);
        layout_joystick2 = (RelativeLayout)findViewById(R.id.layout_joystick2);

        buttonLeft = (Button) findViewById(R.id.buttonTurnLeft);
        buttonRight = (Button) findViewById(R.id.buttonTurnRight);
        buttonEffect = (Button) findViewById(R.id.buttonLighter);
        buttonSound = (Button) findViewById(R.id.buttonSound);

        js = new Joystick(getApplicationContext(), layout_joystick, R.drawable.image_button);
        js.setStickSize(250, 250);
        js.setLayoutSize(800, 800);
        js.setLayoutAlpha(150);
//        js.setStickAlpha(100);
        js.setOffset(90);
        js.setMinimumDistance(50);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            headDegree = (TextView) findViewById(R.id.textViewHeadDegree);
            js.setStickSize(150, 150);
            js.setLayoutSize(500, 500);
            js.setLayoutAlpha(150);
//        js.setStickAlpha(100);
            js.setOffset(90);
            js.setMinimumDistance(50);

            js2 = new Joystick(getApplicationContext(), layout_joystick2, R.drawable.image_button);
            js2.setStickSize(125, 125);
            js2.setLayoutSize(800, 50);
            js2.setLayoutAlpha(150);
//        js.setStickAlpha(100);
            js2.setOffset(90);
            js2.setMinimumDistance(50);

            initJoyStickHead();
        }


        layout_joystick.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                js.drawStick(arg1);
                if(arg1.getAction() == MotionEvent.ACTION_DOWN
                        || arg1.getAction() == MotionEvent.ACTION_MOVE) {
                    String x = String.valueOf(js.getX());
                    String y = String.valueOf(js.getY());
                    String angle = String.valueOf(js.getAngle());
                    double distance =  js.getDistance();

                    int direction = js.get8Direction();
                    // Joystick.STICKUP
                    if (0 < distance && distance <= 100) {
                        velocity.setText("1");
                        try {
                            write("1");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else if (100 < distance && distance <= 200) {
                        velocity.setText("2");
                        try {
                            write("2");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else if (200 < distance && distance <= 300) {
                        velocity.setText("3");
                        try {
                            write("3");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else if (300 < distance && distance <= 400) {
                        velocity.setText("4");
                        try {
                            write("4");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else if (400 < distance) {
                        velocity.setText("5");
                        try {
                            write("5");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                } else if(arg1.getAction() == MotionEvent.ACTION_UP) {
                    velocity.setText("0");
                    try {
                        write("0");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            initLayoutPortrait();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        m_vwMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_pattern:
                startPatternListActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initLayoutPortrait() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        pairedDevices = bluetoothAdapter.getBondedDevices();
        for(BluetoothDevice bt : pairedDevices) {
            if (bt.getName().equals("HC05_SLV")) {
                Toast.makeText(this,"FOUND",Toast.LENGTH_SHORT).show();
                bb_8 = bt;
            }
        }

        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    write("Head Left: 10");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    write("Head Right: 10");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        buttonEffect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    write("Effect: Lighter");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        buttonSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    write("Effect: Sound");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        ParcelUuid[] uuids = bb_8.getUuids();
        BluetoothSocket socket = null;
        try {
            socket = bb_8.createRfcommSocketToServiceRecord(uuids[0].getUuid());
            socket.connect();
            outputStream = socket.getOutputStream();
            isConnected = true;
            write("Test");
        } catch (IOException e) {
            Toast.makeText(this,"Disconnected", Toast.LENGTH_SHORT).show();
        }
        if (isConnected) {
            Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        }
    }

    protected void startPatternListActivity() {
        Intent i = new Intent(this, PatternList.class);
        startActivity(i);
    }

    public static void write(String s) throws IOException {
        if (outputStream == null) {
//            Toast.makeText(Remote.this, "Disconnected", Toast.LENGTH_LONG).show();
        }
        else {
            s.replace("<", "");
            s.replace(">", "");
            s = "<" + s + ">";
            // TODO Configure multiple sends for smaller byte sizes.
            // Break apart message by <>
            outputStream.write(s.getBytes());
        }
    }

    private void initJoyStickHead() {
        buttonEffect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    write("Effect: Lighter");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        buttonSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    write("Effect: Sound");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        layout_joystick2.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                js2.drawStick(arg1);
                if(arg1.getAction() == MotionEvent.ACTION_DOWN
                        || arg1.getAction() == MotionEvent.ACTION_MOVE) {
                    int direction = js2.getX();
                    headDegree.setText(String.valueOf(direction));
                    try {
                        write(String.valueOf(direction));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });
    }
}
