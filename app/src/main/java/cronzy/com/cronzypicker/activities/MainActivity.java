package cronzy.com.cronzypicker.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;

import cronzy.com.cronzypicker.R;
import cronzy.com.cronzypicker.constants.Constants;
import cronzy.com.cronzypicker.main.MemoryPallete;
import cronzy.com.cronzypicker.main.MyBluetoothService;
import cronzy.com.cronzypicker.main.MyHandler;
import cronzy.com.cronzypicker.utils.ArchieveColors;
import cronzy.com.cronzypicker.views.ColorCirclePicker;


public class MainActivity extends AppCompatActivity
        implements Constants, SeekBar.OnSeekBarChangeListener {

    private BluetoothAdapter btAdapter;
    private BluetoothSocket btSocket;
    private Handler h;

    private MyBluetoothService.ConnectedThread mConnectedThread;
    private MyBluetoothService bluetoothService;
    private MemoryPallete mp;

    private SeekBar sbSaturation;
    private SeekBar sbValue;
    private ArchieveColors archieveColors;
    private ColorCirclePicker mColorCirclePickerView;

    private String bluetoothAdress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.main);

        initSP();
        initColorPickerView();

        initPallete();

        initButtons();
        initSeakBars();
        initStartColor();
        initHandler();

    }


    private void initHandler() {
        h = new MyHandler(mColorCirclePickerView);
        bluetoothService = new MyBluetoothService(h);
    }

    private void initPallete() {
        archieveColors = new ArchieveColors();
        mp =new MemoryPallete(this,mColorCirclePickerView);
        mp.init();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mp.setMinDemension();
    }


    private void initSeakBars() {
        sbSaturation = (SeekBar) findViewById(R.id.sbSaturation);
        sbSaturation.setOnSeekBarChangeListener(this);
        sbValue = (SeekBar) findViewById(R.id.sbValue);
        sbValue.setOnSeekBarChangeListener(this);
        sbValue.setRotation(180);
    }

    private void initStartColor() {
        mColorCirclePickerView.setUsedColor("000000000");
        changeProgressBackground();
    }


    private void initButtons() {
        Button buttonSendColor = (Button) findViewById(R.id.buttonSendColor);
        buttonSendColor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String mRgb = mColorCirclePickerView.getRGB();

                if (mConnectedThread != null) {
                    mConnectedThread.write(mRgb + "\n");
                    Toast.makeText(getBaseContext(), "Sent:" + mRgb, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), "Connection isn't established",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button buttonSaveColor = (Button) findViewById(R.id.buttonSaveColor);
        buttonSaveColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                archieveColors.addColorToArchieve(mColorCirclePickerView.getRGBHEx());
                mp.updateViewColors(archieveColors);
            }
        });


        ImageButton btnLogoBT = (ImageButton) findViewById(R.id.logoButtonBT);
        btnLogoBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btAdapter = BluetoothAdapter.getDefaultAdapter();
                checkBTState();
                runnerForBL();
            }
        });
    }


    private void initColorPickerView() {
        mColorCirclePickerView = (ColorCirclePicker) findViewById(R.id.colorPickerView);
        mColorCirclePickerView.setOnColorChangeListener(
                new ColorCirclePicker.OnColorChangeListener() {
                    @Override
                    public void onDismiss(int val, float alpha) {
                    }

                    @Override
                    public void onColorChanged(int val, float alpha) {
                        changeProgressBackground();
                    }
                });
    }


    private void initSP() {
        SharedPreferences mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        bluetoothAdress = getIntent().getExtras().getString(APP_PREFERENCES_NAME);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (btAdapter != null) {
            runnerForBL();
        }
    }

    private void runnerForBL(){
        if (btSocket!=null){
            try {
                btSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            btSocket = null;
        }
        btSocket = bluetoothService.connectionBT(btAdapter, bluetoothAdress);

        Log.d(TAG_BT, "onResume btSocket " + btSocket.isConnected());

        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        mConnectedThread = bluetoothService.getConnectedThread(btSocket);
        mConnectedThread.start();

    }



    @Override
    public void onPause() {
        Log.d(TAG,"onPause()");
        super.onPause();
        try {
            btSocket.close();
            btSocket = null;
            mConnectedThread.cancel();
            mConnectedThread = null;

        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d(TAG_BT, "...In onPause()...");
    }

    private void checkBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on
        // Emulator doesn't support Bluetooth and will return null
        if (btAdapter == null) {
            errorExit("Fatal Error", "Bluetooth не поддерживается");
        } else {
            if (btAdapter.isEnabled()) {
                // Log.d(TAG_BT, "...Bluetooth включен...");
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(btAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    private void errorExit(String title, String message) {
        Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
        finish();
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        float mProgress;
        if (seekBar.getId() == sbSaturation.getId()) {
            mProgress = (float) progress / 100;
            mColorCirclePickerView.setSaturation(mProgress);
        } else if (seekBar.getId() == sbValue.getId()) {
            int reverceProgress = Math.abs(progress);
            mProgress = (float) reverceProgress / 100;
            mColorCirclePickerView.setValue(mProgress);
        }
        changeProgressBackground();
    }


    private void changeProgressBackground() {
        int mColor = mColorCirclePickerView.getColor();
        int mSaturation = mColorCirclePickerView.getSaturation();
        int mValue = mColorCirclePickerView.getValue();

        sbSaturation.getProgressDrawable().setColorFilter(mColor, PorterDuff.Mode.MULTIPLY);
        sbSaturation.getThumb().setColorFilter(mColor, PorterDuff.Mode.SRC_IN);
        sbSaturation.setProgress(mSaturation);

        sbValue.getProgressDrawable().setColorFilter(mColor, PorterDuff.Mode.MULTIPLY);
        sbValue.getThumb().setColorFilter(mColor, PorterDuff.Mode.SRC_IN);
        sbValue.setProgress(mValue);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

}