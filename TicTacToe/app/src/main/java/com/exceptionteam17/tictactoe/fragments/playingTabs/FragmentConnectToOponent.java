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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.exceptionteam17.tictactoe.R;
import com.exceptionteam17.tictactoe.fragments.Fragment_Home;
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
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

import static java.nio.charset.StandardCharsets.UTF_8;

public class FragmentConnectToOponent extends Fragment implements View.OnClickListener {

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
    private Button openGameButton;
    private Button disconnectButton;
    private String playerName;
    private String opponentEndpointId;
    private String opponentName;
    private TextView satatus, textGameplay;
    private final Strategy STRATEGY = Strategy.P2P_STAR;
    private DiscoveryOptions discoveryOptions;
    private AdvertisingOptions advertisingOptions;
    private PrettyDialog prettyDialog;
    ///////////////////////////

    private final static String WINNER_PHONE = "SYSTEM_PHONE";
    private final static String WINNER_PLAYER = "SYSTEM_PLAYER";
    private final static String GAME_OVER = "SYSTEM_GAME_OVER";
    private final static String NO_WINNER = "SYSTEM_NO_WINNER";
    private boolean isPlayerTurn, isCreator;
    private ImageView box1, box2, box3, box4, box5, box6, box7, box8, box9;
    private ImageView[][] field;
    private Boolean [][] board;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_discover, container, false);
        if(getActivity() != null && getActivity().getWindow() != null) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        verifyPermissions(((Activity)view.getContext()));

        initialize();

        setImagesClickListeners();

        return view;
    }

    private void initialize(){
        isCreator = false;
        connectionsClient = Nearby.getConnectionsClient(view.getContext());
        playerName = Preferences.getStringFromPreferences(view.getContext(),"user");
        findOpponentButton = view.findViewById(R.id.discover_find_opponent_btn);
        findOpponentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCreator = false;
                isPlayerTurn = false;
                findOpponent();
            }
        });
        openGameButton = view.findViewById(R.id.start_game_btn);
        openGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCreator = true;
                isPlayerTurn = true;
                openGame();
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
        advertisingOptions = new AdvertisingOptions.Builder().setStrategy(STRATEGY).build();
//        go.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                connectionsClient.sendPayload(
//                        opponentEndpointId, Payload.fromBytes((" " + (new Random().nextInt())).getBytes(UTF_8)));
//            }
//        });

        ////////////////////////////////////////////////////////
        isPlayerTurn = true;
        box1 = view.findViewById(R.id.single_box1);
        box2 = view.findViewById(R.id.single_box2);
        box3 = view.findViewById(R.id.single_box3);
        box4 = view.findViewById(R.id.single_box4);
        box5 = view.findViewById(R.id.single_box5);
        box6 = view.findViewById(R.id.single_box6);
        box7 = view.findViewById(R.id.single_box7);
        box8 = view.findViewById(R.id.single_box8);
        box9 = view.findViewById(R.id.single_box9);
        field = new ImageView[][]{
                {box1, box2, box3},
                {box4, box5, box6},
                {box7, box8, box9}
        };
        board = new Boolean[][]{
                {null, null, null},
                {null, null, null},
                {null, null, null}
        };
        disableBoard();
        disconnectButton.setVisibility(View.GONE);
        findOpponentButton.setVisibility(View.VISIBLE);
        openGameButton.setVisibility(View.VISIBLE);
        textGameplay = view.findViewById(R.id.text_gameplay);
        textGameplay.setText("");
    }

    public void findOpponent() {
//        startAdvertising();
        startDiscovery();
        satatus.setText("Searching");
        disconnectButton.setVisibility(View.VISIBLE);
        findOpponentButton.setVisibility(View.GONE);
        openGameButton.setVisibility(View.GONE);
    }

    public void openGame(){
        startAdvertising();
        satatus.setText("Waiting for opponent");
    }

    public void disconnect() {
        if(connectionsClient != null && opponentEndpointId != null) {
            connectionsClient.disconnectFromEndpoint(opponentEndpointId);
            opponentEndpointId = null;
        }
        satatus.setText("disconnected");
        disconnectButton.setVisibility(View.GONE);
        findOpponentButton.setVisibility(View.VISIBLE);
        openGameButton.setVisibility(View.VISIBLE);
        textGameplay.setText("");
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
                    if(opponentMove.equalsIgnoreCase("NEW")){
                        resetGame();
                        //isPlayerTurn = false;
                        textGameplay.setText(opponentName);
                        if(prettyDialog != null && prettyDialog.isShowing()){
                            prettyDialog.cancel();
                        }
                    } else {
                        int x = Integer.parseInt(opponentMove.substring(0, 1));
                        int y = Integer.parseInt(opponentMove.substring(1, 2));
                        isPlayerTurn = false;
                        changePicture(x, y);
                        checkForEndGame();
                        isPlayerTurn = true;
                        enableBoard();
                        textGameplay.setText(playerName + " turn");
                    }
                }

                @Override
                public void onPayloadTransferUpdate(String endpointId, PayloadTransferUpdate update) {
                    Log.e("blaalalala", "kakvo da pravq tuk?!" + update);
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
                    satatus.setText("Connecting");
                }

                @Override
                public void onEndpointLost(String endpointId) {
                    satatus.setText("searching");
                    disconnect();
                    findOpponent();
                }
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
                        enableBoard();
                        connectionsClient.stopDiscovery();
                        connectionsClient.stopAdvertising();

                        opponentEndpointId = endpointId;

                        satatus.setText("connected to: " + opponentName);
                        disconnectButton.setVisibility(View.VISIBLE);
                        findOpponentButton.setVisibility(View.GONE);
                        openGameButton.setVisibility(View.GONE);
                        textGameplay.setVisibility(View.VISIBLE);
                        //setButtonState(true);
                    } else {
                        Log.i("bla bla", "onConnectionResult: connection failed");
                        satatus.setText("searching");
//                        disconnect(); //TODO add boolean
//                        findOpponent();
                    }
                }

                @Override
                public void onDisconnected(String endpointId) {
                    Log.i("bla bla", "onDisconnected: disconnected from the opponent");
                    Toasty.error(view.getContext().getApplicationContext(), "DISCONNECTED", Toast.LENGTH_SHORT, true).show();
                    satatus.setText("DISCONNECTED");
//                    disableBoard();
//                    resetGame();
//                    disconnectButton.setVisibility(View.GONE);
//                    findOpponentButton.setVisibility(View.VISIBLE);
//                    textGameplay.setVisibility(View.GONE);
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
                playerName, "com.exceptionteam17.tictactoe", connectionLifecycleCallback, advertisingOptions);
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

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private void setImagesClickListeners() {
        box1.setOnClickListener(this);
        box2.setOnClickListener(this);
        box3.setOnClickListener(this);
        box4.setOnClickListener(this);
        box5.setOnClickListener(this);
        box6.setOnClickListener(this);
        box7.setOnClickListener(this);
        box8.setOnClickListener(this);
        box9.setOnClickListener(this);
    }

    private void disableBoard(){
        box1.setEnabled(false);
        box2.setEnabled(false);
        box3.setEnabled(false);
        box4.setEnabled(false);
        box5.setEnabled(false);
        box6.setEnabled(false);
        box7.setEnabled(false);
        box8.setEnabled(false);
        box9.setEnabled(false);
    }

    private void enableBoard(){
        box1.setEnabled(true);
        box2.setEnabled(true);
        box3.setEnabled(true);
        box4.setEnabled(true);
        box5.setEnabled(true);
        box6.setEnabled(true);
        box7.setEnabled(true);
        box8.setEnabled(true);
        box9.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.single_box1:
                if(changePicture(0,0)){
                    connectionsClient.sendPayload(
                            opponentEndpointId, Payload.fromBytes("00".getBytes(UTF_8)));
                    disableBoard();
                    textGameplay.setText(opponentName + " turn");
                }
                checkForEndGame();
                break;
            case R.id.single_box2:
                if(changePicture(0,1)){
                    connectionsClient.sendPayload(
                        opponentEndpointId, Payload.fromBytes("01".getBytes(UTF_8)));
                    disableBoard();
                    textGameplay.setText(opponentName + " turn");
                }
                checkForEndGame();
                break;
            case R.id.single_box3:
                if( changePicture(0,2)){
                    connectionsClient.sendPayload(
                        opponentEndpointId, Payload.fromBytes("02".getBytes(UTF_8)));
                    disableBoard();
                    textGameplay.setText(opponentName + " turn");
                }
                checkForEndGame();
                break;
            case R.id.single_box4:
                if(changePicture(1,0)){
                    connectionsClient.sendPayload(
                        opponentEndpointId, Payload.fromBytes("10".getBytes(UTF_8)));
                    disableBoard();
                    textGameplay.setText(opponentName + " turn");
                }
                checkForEndGame();
                break;
            case R.id.single_box5:
                if(changePicture(1,1)){
                    connectionsClient.sendPayload(
                        opponentEndpointId, Payload.fromBytes("11".getBytes(UTF_8)));
                    disableBoard();
                    textGameplay.setText(opponentName + " turn");
                }
                checkForEndGame();
                break;
            case R.id.single_box6:
                if(changePicture(1,2)){
                    connectionsClient.sendPayload(
                        opponentEndpointId, Payload.fromBytes("12".getBytes(UTF_8)));
                    disableBoard();
                    textGameplay.setText(opponentName + " turn");
                }
                checkForEndGame();
                break;
            case R.id.single_box7:
                if(changePicture(2,0)){
                    connectionsClient.sendPayload(
                        opponentEndpointId, Payload.fromBytes("20".getBytes(UTF_8)));
                    disableBoard();
                    textGameplay.setText(opponentName + " turn");
                }
                checkForEndGame();
                break;
            case R.id.single_box8:
                if(changePicture(2,1)){
                    connectionsClient.sendPayload(
                        opponentEndpointId, Payload.fromBytes("21".getBytes(UTF_8)));
                    disableBoard();
                    textGameplay.setText(opponentName + " turn");
                }
                checkForEndGame();
                break;
            case R.id.single_box9:
                if( changePicture(2,2)){
                    connectionsClient.sendPayload(
                        opponentEndpointId, Payload.fromBytes("22".getBytes(UTF_8)));
                    disableBoard();
                    textGameplay.setText(opponentName + " turn");
                }
                checkForEndGame();
                break;
        }
    }

    private boolean changePicture(int x, int y){
        if(isPlayerTurn) {
            if(board[x][y] == null) {
                field[x][y].setImageResource(R.drawable.x);
                isPlayerTurn = false;
                board[x][y] = true;
                return true;
            }
        }  else {
            if(board[x][y] == null) {
                field[x][y].setImageResource(R.drawable.o);
                isPlayerTurn = true;
                board[x][y] = false;
                return true;
            }
        }
        return false;
    }

    private String checkForVictory() {
        if ((board[0][0] != null && board[0][1] != null && board[0][2] != null) || (board[0][0] != null&& board[0][1] != null&& board[0][2]!= null)) {
            if (board[0][0] && board[0][1] && board[0][2]) {
                return WINNER_PLAYER;
            } else if (!board[0][0] && !board[0][1] && !board[0][2]) {
                return WINNER_PHONE;
            }
        }
        if ((board[1][0] != null && board[1][1] != null && board[1][2] != null) || (board[1][0] != null && board[1][1] != null && board[1][2]!= null)) {
            if (board[1][0] && board[1][1] && board[1][2]) {
                return WINNER_PLAYER;
            } else if (!board[1][0] && !board[1][1] && !board[1][2]){
                return WINNER_PHONE;
            }
        }
        if ((board[2][0] != null && board[2][1] != null && board[2][2] != null) || (board[2][0] != null && board[2][1] != null&& board[2][2]!= null)) {
            if (board[2][0] && board[2][1] && board[2][2]) {
                return WINNER_PLAYER;
            } else if(!board[2][0] && !board[2][1] && !board[2][2]) {
                return WINNER_PHONE;
            }
        }
        if ((board[0][0] != null && board[1][0] != null && board[2][0] != null) || (board[0][0] != null&& board[1][0] != null&& board[2][0]!= null)) {
            if (board[0][0] && board[1][0] && board[2][0]) {
                return WINNER_PLAYER;
            } else if(!board[0][0] && !board[1][0] && !board[2][0]) {
                return WINNER_PHONE;
            }
        }
        if ((board[0][1] != null && board[1][1] != null && board[2][1] != null) || (board[0][1] != null&& board[1][1] != null&& board[2][1]!= null)) {
            if (board[0][1] && board[1][1] && board[2][1]) {
                return WINNER_PLAYER;
            } else if(!board[0][1] && !board[1][1] && !board[2][1]) {
                return WINNER_PHONE;
            }
        }
        if ((board[0][2] != null && board[1][2] != null && board[2][2] != null) || (board[0][2] != null&& board[1][2] != null&& board[2][2]!= null)) {
            if (board[0][2] && board[1][2] && board[2][2]) {
                return WINNER_PLAYER;
            } else if(!board[0][2] && !board[1][2] && !board[2][2]) {
                return WINNER_PHONE;
            }
        }
        if ((board[0][0] != null && board[1][1] != null && board[2][2] != null) || (board[0][0] != null && board[1][1] != null&& board[2][2]!= null)) {
            if (board[0][0] && board[1][1] && board[2][2]) {
                return WINNER_PLAYER;
            } else if(!board[0][0] && !board[1][1] && !board[2][2]) {
                return WINNER_PHONE;
            }
        }
        if ((board[2][0] != null && board[1][1] != null && board[0][2] != null) || (board[2][0] != null && board[1][1] != null && board[0][2]!= null)) {
            if (board[2][0] && board[1][1] && board[0][2]) {
                return WINNER_PLAYER;
            } else if (!board[2][0] && !board[1][1] && !board[0][2]) {
                return WINNER_PHONE;
            }
        }

        boolean flag = true;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == null) {
                    flag = false;
                }
            }
        }
        if (flag) {
            return GAME_OVER;
        }
        return NO_WINNER;
    }

    private void checkForEndGame() {
        switch (checkForVictory()) {
            case GAME_OVER:
                showAlert("DRAW", "New game?", R.mipmap.ic_popup_round, "PLAY", "NO");
                break;
            case NO_WINNER:
                break;
            case WINNER_PHONE:
                showAlert("YOU LOST", "New game?", R.mipmap.ic_popup_round, "PLAY", "NO");
                break;
            case WINNER_PLAYER:
                showAlert("YOU WON!!!", "New game?", R.mipmap.ic_popup_round, "PLAY", "NO");
                break;
        }
    }

    private void showAlert(String title, String msg, int icon, String posBtnText, String negativeBtnText){
        prettyDialog = new PrettyDialog(view.getContext());
        prettyDialog.setCanceledOnTouchOutside(false);
        prettyDialog
                .setTitle(title)
                .setMessage(msg)
                .setIcon(icon)
                .addButton(
                        posBtnText,     // button text
                        R.color.pdlg_color_white,  // button text color
                        R.color.pdlg_color_green,  // button background color //TODO change color
                        new PrettyDialogCallback() {  // button OnClick listener
                            @Override
                            public void onClick() {
                                if(isCreator) {
                                    isPlayerTurn = true;
                                    connectionsClient.sendPayload(
                                            opponentEndpointId, Payload.fromBytes("NEW".getBytes(UTF_8)));
                                    resetGame();
                                    enableBoard();
                                    prettyDialog.cancel();
                                }
                            }
                        }
                )
                .addButton(
                        negativeBtnText,     // button text
                        R.color.pdlg_color_white,  // button text color
                        R.color.pdlg_color_red,  // button background color
                        new PrettyDialogCallback() {  // button OnClick listener
                            @Override
                            public void onClick() {
                                disconnect();
                                try {
                                    FragmentConnectToOponent.this.finalize();
                                } catch (Throwable throwable) {
                                    throwable.printStackTrace();
                                }
                                loadFragment(new Fragment_Home());
                                prettyDialog.cancel();
                            }
                        }
                )
                .show();
    }

    private void resetGame(){
        field = new ImageView[][]{
                {box1, box2, box3},
                {box4, box5, box6},
                {box7, box8, box9}
        };
        board = new Boolean[][]{
                {null, null, null},
                {null, null, null},
                {null, null, null}
        };

        box1.setImageResource(R.drawable.transparent);
        box2.setImageResource(R.drawable.transparent);
        box3.setImageResource(R.drawable.transparent);
        box4.setImageResource(R.drawable.transparent);
        box5.setImageResource(R.drawable.transparent);
        box6.setImageResource(R.drawable.transparent);
        box7.setImageResource(R.drawable.transparent);
        box8.setImageResource(R.drawable.transparent);
        box9.setImageResource(R.drawable.transparent);
    }
}
