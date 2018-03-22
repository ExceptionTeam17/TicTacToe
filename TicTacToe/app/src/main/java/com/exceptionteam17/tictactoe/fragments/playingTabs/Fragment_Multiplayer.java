package com.exceptionteam17.tictactoe.fragments.playingTabs;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.exceptionteam17.tictactoe.R;
import com.exceptionteam17.tictactoe.activities.MainActivity;
import com.exceptionteam17.tictactoe.model.database.DatabaseHelper;
import com.exceptionteam17.tictactoe.model.utils.Preferences;
import com.exceptionteam17.tictactoe.model.utils.Utils;

import java.util.ArrayList;
import java.util.Set;

public class Fragment_Multiplayer extends Fragment{

    private View view;
    private String username;
    private TextView win, draw, lost, usernameView;
    private DatabaseHelper db;
    private static final int REQUEST_PERMISSION = 1;
    private Button startMulti2Dev, startMulti1Dev;
    private BluetoothAdapter bluetoothAdapter;
    private static ArrayList<BluetoothDevice> devices;
    private static String[] PERMISSIONS= {
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_COARSE_LOCATION
            //Manifest.permission.BLUETOOTH_PRIVILEGED api 19+
    };

    public static void addDevice(BluetoothDevice device) {
        if (device != null) {
            devices.add(device);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_multiplayer, container, false);
        initialize();
        verifyPermissions(((Activity)view.getContext()));
        startGame();
        return view;
    }

    private void initialize() {
        db = DatabaseHelper.getInstance(view.getContext());
        startMulti2Dev = view.findViewById(R.id.start_gameplay_multi);
        startMulti1Dev = view.findViewById(R.id.start_gameplay_multi_same);
        username = Preferences.getStringFromPreferences(view.getContext(),"user");
        usernameView = view.findViewById(R.id.toolbar_username);
        win = view.findViewById(R.id.toolbar_win);
        lost = view.findViewById(R.id.toolbar_lost);
        draw = view.findViewById(R.id.toolbar_draw);
        usernameView.setText(username);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        devices = new ArrayList<>();

        setMulti();
    }

    public void setMulti(){
        win.setText("Wins: " + db.getUserWinsMulti(username));
        draw.setText("Draws: " + db.getUserDrawsMulti(username));
        lost.setText("Loses: " + db.getUserLosesMulti(username));
    }

    public static void verifyPermissions(Activity activity) {
        int btAdmin = ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_ADMIN);
        int bt = ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH);
        int coarse = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
        int wifi1 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_WIFI_STATE);
        int wifi2 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CHANGE_WIFI_STATE);

        if (bt != PackageManager.PERMISSION_GRANTED || btAdmin != PackageManager.PERMISSION_GRANTED ||
                coarse != PackageManager.PERMISSION_GRANTED || wifi1 != PackageManager.PERMISSION_GRANTED ||
                wifi2 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS,
                    REQUEST_PERMISSION
            );
        }
    }

    private void startGame() {
        startMulti2Dev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startStopBluetooth(); //turn on bluetooth
                enableBluetoothDiscoverable(); //set discoverable bluetooth
                discoverDevices(); //start discovering devices
            }
        });

        startMulti1Dev.setOnClickListener(new View.OnClickListener() {
            Dialog dialog = new Dialog(getContext(), R.style.Theme_Dialog);
            @Override
            public void onClick(View v) {
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_second_username);
                ((Button)dialog.findViewById(R.id.btn_2user_cancel)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                ((Button)dialog.findViewById(R.id.btn_2user_ok)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String secondUser = ((EditText)dialog.findViewById(R.id.second_username_edit)).getText().toString();
                        if(secondUser.isEmpty()){
                            ((EditText) dialog.findViewById(R.id.second_username_edit)).setError("Enter valid username");
                            return;
                        }
                        final FragmentGameplayMulti1Dev fr = new FragmentGameplayMulti1Dev();
                        Bundle bundle = new Bundle();
                        bundle.putString("secondPlayer", secondUser);
                        fr.setArguments(bundle);
                        dialog.dismiss();
                        Utils.hideKeyboard(getActivity());
                        loadFragment(fr);
                    }
                });
                dialog.show();

            }
        });
    }

    private void startStopBluetooth(){
        if (bluetoothAdapter == null) {
            Toast.makeText(view.getContext(), "NO BLUETOOTH", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(turnOn);
        }
    }

    private void enableBluetoothDiscoverable() {
        Intent enableDiscoverable = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        enableDiscoverable.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 30);
        startActivity(enableDiscoverable);
    }

    private void discoverDevices() {
        if (!bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.startDiscovery();
            Log.e("Tuka", "sum");
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.home_main, fragment);
        ft.commit();
    }
}
