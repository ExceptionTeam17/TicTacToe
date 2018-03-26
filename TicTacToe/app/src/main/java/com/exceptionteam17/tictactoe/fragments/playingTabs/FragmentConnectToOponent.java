package com.exceptionteam17.tictactoe.fragments.playingTabs;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.exceptionteam17.tictactoe.R;
import com.exceptionteam17.tictactoe.model.utils.Preferences;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;

import es.dmoral.toasty.Toasty;

import static java.nio.charset.StandardCharsets.UTF_8;

public class FragmentConnectToOponent extends Fragment {

    private View view;
    private static final int REQUEST_PERMISSION = 9999;
    private static String[] PERMISSIONS= {
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE
    };
    private ConnectionsClient connectionsClient;
    private Button findOpponentButton;
    private Button disconnectButton;
    private String playerName;
    private String opponentEndpointId;
    private String opponentName;
    private TextView satatus;
    private final Strategy STRATEGY = Strategy.P2P_STAR;
    private DiscoveryOptions discoveryOptions;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_discover, container, false);

        initialize();

        verifyPermissions(((Activity)view.getContext()));

        return view;
    }

    private void initialize(){
        connectionsClient = Nearby.getConnectionsClient(view.getContext());
        playerName = Preferences.getStringFromPreferences(view.getContext(),"user");
        findOpponentButton = view.findViewById(R.id.discover_find_opponent_btn);
        findOpponentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findOpponent();
            }
        });
        disconnectButton = view.findViewById(R.id.discover_disconnect_btn);
        disconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disconnect();
            }
        });
        satatus = view.findViewById(R.id.discover_text_status);
        discoveryOptions = new DiscoveryOptions.Builder().setStrategy(STRATEGY).build();

    }

    public void findOpponent() {
        startAdvertising();
        startDiscovery();
        satatus.setText("Searching");
        //findOpponentButton.setEnabled(false);
    }

    public void disconnect() {
        connectionsClient.disconnectFromEndpoint(opponentEndpointId);
        satatus.setText("disconected");
        //resetGame();
    }

    public void verifyPermissions(Activity activity) {
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

    @CallSuper
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode != REQUEST_PERMISSION) {
            return;
        }

        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                Toasty.warning(view.getContext().getApplicationContext(), "Must give permission to play", Toast.LENGTH_SHORT, true).show();
                verifyPermissions(((Activity)view.getContext()));
                return;
            }
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.home_main, fragment);
        ft.commit();
    }


    ///////////////////////////////////


    // Callbacks for receiving payloads
    private final PayloadCallback payloadCallback =
            new PayloadCallback() {
                @Override
                public void onPayloadReceived(String endpointId, Payload payload) {
                    String opponentMove = new String(payload.asBytes(), UTF_8);
                }

                @Override
                public void onPayloadTransferUpdate(String endpointId, PayloadTransferUpdate update) {
//                    if (update.getStatus() == PayloadTransferUpdate.Status.SUCCESS && myChoice != null && opponentChoice != null) {
//                        finishRound();
//                    }
                }
            };

    // Callbacks for finding other devices
    private final EndpointDiscoveryCallback endpointDiscoveryCallback =
            new EndpointDiscoveryCallback() {
                @Override
                public void onEndpointFound(String endpointId, DiscoveredEndpointInfo info) {
                    Log.i("bla bla", "onEndpointFound: endpoint found, connecting");
                    connectionsClient.requestConnection(playerName, endpointId, connectionLifecycleCallback);
                }

                @Override
                public void onEndpointLost(String endpointId) {}
            };

    // Callbacks for connections to other devices
    private final ConnectionLifecycleCallback connectionLifecycleCallback =
            new ConnectionLifecycleCallback() {
                @Override
                public void onConnectionInitiated(String endpointId, ConnectionInfo connectionInfo) {
                    Log.i("bla bla", "onConnectionInitiated: accepting connection");
                    connectionsClient.acceptConnection(endpointId, payloadCallback);
                    opponentName = connectionInfo.getEndpointName();
                }

                @Override
                public void onConnectionResult(String endpointId, ConnectionResolution result) {
                    if (result.getStatus().isSuccess()) {
                        Log.i("bla bla", "onConnectionResult: connection successful");

                        connectionsClient.stopDiscovery();
                        connectionsClient.stopAdvertising();

                        opponentEndpointId = endpointId;
                        //setOpponentName(opponentName);
                        satatus.setText("connected to: " + opponentName);
                        //setButtonState(true);
                    } else {
                        Log.i("bla bla", "onConnectionResult: connection failed");
                    }
                }

                @Override
                public void onDisconnected(String endpointId) {
                    Log.i("bla bla", "onDisconnected: disconnected from the opponent");
                    //resetGame();
                }
            };

    /** Starts looking for other players using Nearby Connections. */
    private void startDiscovery() {
        // Note: Discovery may fail. To keep this demo simple, we don't handle failures.
        connectionsClient.startDiscovery(
                "com.exceptionteam17.tictactoe", endpointDiscoveryCallback, discoveryOptions);
    }

    /** Broadcasts our presence using Nearby Connections so other players can find us. */
    private void startAdvertising() {
        // Note: Advertising may fail. To keep this demo simple, we don't handle failures.
        connectionsClient.startAdvertising(
                playerName, "com.exceptionteam17.tictactoe", connectionLifecycleCallback, new AdvertisingOptions.Builder().setStrategy(STRATEGY).build());
    }

    @Override
    public void onStop() {
        connectionsClient.stopAllEndpoints();
        super.onStop();
    }

    /** Sends the user's selection of rock, paper, or scissors to the opponent. */
//    private void sendGameChoice(GameChoice choice) {
//        myChoice = choice;
//        connectionsClient.sendPayload(
//                opponentEndpointId, Payload.fromBytes(choice.name().getBytes(UTF_8)));
//
//        setStatusText(getString(R.string.game_choice, choice.name()));
//        // No changing your mind!
//        setGameChoicesEnabled(false);
//    }
}
