package cronzy.com.cronzypicker.constants;

import android.graphics.Color;

import java.util.UUID;


public interface Constants {





    float WIDTH_SHADER_BORDER = 0.02266f;
    float RAD_OUTER_ROTARY_CIRCLE = 0.037f;
    float RAD_INNER_ROTARY_CIRCLE = 0.037f;
    float RAD_OUTER_SHADER_CIRCLE = 0.421f;
    float RAD_INNER_MAIN_CIRCLE = 0.14136f;
    int colors[] = new int[]{Color.LTGRAY, Color.BLUE, Color.CYAN, Color.YELLOW, Color.RED, Color.DKGRAY, Color.BLACK, Color.GREEN};


    int SET_COLOR = 0;
    int SET_INNER = 1;

    // SPP UUID сервиса
    UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    //Статус для Handler
    //int RECIEVE_MESSAGE = 1;
    int RECIEVE_MESSAGE = 0;
    int REQUEST_ENABLE_BT = 1;

    // MAC-адрес Bluetooth модуля
    String addressDeffault = "20:16:07:05:94:01";

    //Shared Prederences
    String APP_PREFERENCES = "appsettings";
    String APP_PREFERENCES_NAME = "bluetoothadress";
    String APP_DEFAULT_NAME_VALUE="default";


    String TAG = "MyLog";
    String TAG_BT = "MyLogBT";
    String TAG_Handle = "MyLogHandle";
    String SAVED_NEW_RGB = "RGB";
    String PREF_NAME = "prefs";



}
