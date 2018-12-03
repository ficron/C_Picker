package cronzy.com.cronzypicker.activities;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;


import cronzy.com.cronzypicker.R;
import cronzy.com.cronzypicker.bluetooth.BluetoothEstablisher;
import cronzy.com.cronzypicker.constants.ProjectConstants;
import cronzy.com.cronzypicker.utils.MemoryPallete;
import cronzy.com.cronzypicker.utils.ArchieveColors;
import cronzy.com.cronzypicker.views.ColorCirclePicker;


public class MainActivity extends AppCompatActivity
        implements  SeekBar.OnSeekBarChangeListener {

    private MemoryPallete mp;
    private SeekBar sbSaturation;
    private SeekBar sbValue;
    private ArchieveColors archieveColors;
    private ColorCirclePicker mColorCirclePickerView;
    private BluetoothEstablisher bluetoothEstablisher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.main);

        initColorPickerView();
        initBluetoothEstablisher();
        initPallete();
        initButtons();
        initSeakBars();
        initStartColor();
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

    private void initBluetoothEstablisher() {
        SharedPreferences mSettings = getSharedPreferences(ProjectConstants.APP_PREFERENCES, Context.MODE_PRIVATE);
        String bluetoothAdress = getIntent().getExtras().getString(ProjectConstants.APP_DEFAULT_NAME_VALUE);
        Log.d(ProjectConstants.TAG_BT,"bluetoothAdress: "+bluetoothAdress);
        bluetoothEstablisher = new BluetoothEstablisher(this,mColorCirclePickerView,bluetoothAdress);
    }

    private void initPallete() {
        archieveColors = new ArchieveColors();
        mp =new MemoryPallete(this,mColorCirclePickerView);
        mp.init();
    }

    private void initButtons() {
        Button buttonSendColor = (Button) findViewById(R.id.buttonSendColor);
        buttonSendColor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bluetoothEstablisher.sendData(mColorCirclePickerView.getRGB());
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
                bluetoothEstablisher.createConnectionThred();
            }
        });
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


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mp.setMinDemension();
    }


    @Override
    public void onResume() {
        super.onResume();
        bluetoothEstablisher.createConnectionThred();
    }


    @Override
    public void onPause() {

        super.onPause();
        bluetoothEstablisher.closeBluetoothSocket();
        bluetoothEstablisher.closeConnectionThread();
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