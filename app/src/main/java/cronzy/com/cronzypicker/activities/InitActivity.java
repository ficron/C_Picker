package cronzy.com.cronzypicker.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cronzy.com.cronzypicker.R;
import cronzy.com.cronzypicker.constants.Constants;
import cronzy.com.cronzypicker.constants.ProjectConstants;


public class InitActivity extends Activity  {
    private Button searchDevesis;
    private BluetoothAdapter BA;
    private Set<BluetoothDevice> pairedDevices;
    private ListView lv;
    private SharedPreferences mSettings;

    private String bluetoothAdress = ProjectConstants.APP_DEFAULT_NAME_VALUE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.init_bluetooth);

        initSP();
        //redirect();
        initViews();

    }

    private void initSP() {
        mSettings = getSharedPreferences(ProjectConstants.APP_PREFERENCES, Context.MODE_PRIVATE);
    }



    private void redirect() {
        bluetoothAdress = getBluetoothAdress();

        if (!bluetoothAdress.equals(ProjectConstants.APP_DEFAULT_NAME_VALUE)) {
            Intent intent = new Intent(InitActivity.this, MainActivity.class);
            intent.putExtra(ProjectConstants.APP_DEFAULT_NAME_VALUE, bluetoothAdress);
            startActivity(intent);
        }
    }

    private void initViews() {
        searchDevesis = (Button) findViewById(R.id.btnSearchDevesis);
        BA = BluetoothAdapter.getDefaultAdapter();
        lv = (ListView) findViewById(R.id.listOfPairedDevises);
    }


    private String getBluetoothAdress() {
        if (mSettings.contains(APP_PREFERENCES_NAME)) {
            return mSettings.getString(APP_PREFERENCES_NAME, APP_DEFAULT_NAME_VALUE);
        }
        return APP_DEFAULT_NAME_VALUE;
    }


    private void editSPBluetoothAdress(String bluetothAdress) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_NAME, bluetothAdress);
        editor.apply();
        Toast.makeText(getApplicationContext(), "Address add to sp: " + bluetothAdress, Toast.LENGTH_SHORT).show();
    }


    public void list(View v) {
        pairedDevices = BA.getBondedDevices();

        ArrayList list = new ArrayList();
        final Map<String, BluetoothDevice> bdLMap = new HashMap<>();

        for (BluetoothDevice bt : pairedDevices) {
            list.add(bt.getName());
            bdLMap.put(bt.getName(), bt);
        }

        Toast.makeText(getApplicationContext(), "Showing Paired Devices", Toast.LENGTH_SHORT).show();
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int position, long id) {
                String name = ((TextView) view).getText().toString();
                //Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();
                String selectedAdress = bdLMap.get(name).getAddress();
                //Toast.makeText(getApplicationContext(), "getAddress(): " + selectedAdress, Toast.LENGTH_SHORT).show();
                editSPBluetoothAdress(selectedAdress);
                redirect();
            }
        });
    }










}
