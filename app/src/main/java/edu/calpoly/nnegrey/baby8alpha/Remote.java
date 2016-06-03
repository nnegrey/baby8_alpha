package edu.calpoly.nnegrey.baby8alpha;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.concurrent.ExecutionException;

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
    private int width;

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
        // Landscape: 1794
        // Portrait: 1080
        width = getResources().getDisplayMetrics().widthPixels;
        js.setStickSize(width / 6, width / 6);
        js.setLayoutSize((int) (width / 1.4),(int) (width / 1.4));
        js.setLayoutAlpha(150);
        js.setOffset(90);
        js.setMinimumDistance(50);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            headDegree = (TextView) findViewById(R.id.textViewHeadDegree);
            js.setStickSize(width / 12, width / 12);
            js.setLayoutSize((int) (width / 3.6), (int) (width / 3.6));
            js.setLayoutAlpha(150);
            js.setOffset(90);
            js.setMinimumDistance(50);

            js2 = new Joystick(getApplicationContext(), layout_joystick2, R.drawable.image_button);
            js2.setStickSize(width / 12, width / 12);
            js2.setLayoutSize((int) (width / 2.2), (int) (width / 3.6));
            js2.setLayoutAlpha(150);
            js2.setOffset(90);
            js2.setMinimumDistance(50);

            initJoyStickHead();
        }


        layout_joystick.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                js.drawStick(arg1);
                if(arg1.getAction() == MotionEvent.ACTION_DOWN
                        || arg1.getAction() == MotionEvent.ACTION_MOVE) {
                    double distance = js.getDistance();

                    // Joystick.STICKUP
                    int range;
                    if (width < 1500) {
                        range = (int) (width / 16.8);
                    }
                    else {
                        range = (int) (width / 43.2);
                    }

                    if (distance <= range) {
                        velocity.setText("0");
                        try {
                            write("0");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else if (range < distance && distance <= range * 2) {
                        velocity.setText("1");
                        try {
                            write("1");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else if (range * 2 < distance && distance <= range * 3) {
                        velocity.setText("2");
                        try {
                            write("2");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else if (range * 3 < distance && distance <= range * 4) {
                        velocity.setText("3");
                        try {
                            write("3");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else if (range * 5 < distance && distance <= range * 6) {
                        velocity.setText("4");
                        try {
                            write("4");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else if (range * 6 < distance) {
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
        connectBluetooth();
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
            case R.id.menu_connect:
                connectBluetooth();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initLayoutPortrait() {
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
    }

    private void connectBluetooth() {
        AsyncSetupBluetoothTask asyncSetupBluetoothTask = new AsyncSetupBluetoothTask();
        asyncSetupBluetoothTask.execute();
    }

    private void checkStatus() {
        if (isConnected) {
            Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT).show();
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
            String message;
            while (s.length() >= 78) {
                message = "<" + s.substring(0, 77) + ">";
                outputStream.write(message.getBytes());
                s = s.substring(77, s.length());
            }

            s = "<" + s + ">";
            outputStream.write(s.getBytes());

            // Signify EOF to board.
            s = "<!>";
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
                if(arg1.getAction() == MotionEvent.ACTION_DOWN
                        || arg1.getAction() == MotionEvent.ACTION_MOVE) {
                    if (width / 9 < arg1.getY() && arg1.getY() < width / 6.5) {
                        js2.drawStick(arg1);
                    }
                    int direction = js2.getX() / 2;

                    if (direction > 180) {
                        direction = 180;
                    }
                    else if (direction < -180) {
                        direction = -180;
                    }

                    headDegree.setText(String.valueOf(direction));
                    try {
                        write(String.valueOf(direction));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    js2.drawStick(arg1);
                    try {
                        write(String.valueOf(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });
    }

    public class AsyncSetupBluetoothTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter != null) {
                pairedDevices = bluetoothAdapter.getBondedDevices();
                for (BluetoothDevice bt : pairedDevices) {
                    if (bt.getName().equals("HC05_SLV")) {
                        bb_8 = bt;
                    }
                }
            }

            try {
                ParcelUuid[] uuids = bb_8.getUuids();
                BluetoothSocket socket = bb_8.createRfcommSocketToServiceRecord(uuids[0].getUuid());
                socket.connect();
                outputStream = socket.getOutputStream();
                isConnected = true;
            } catch (Exception e) {
                return false;
            }
            if (isConnected) {
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            checkStatus();
        }
    }
}
