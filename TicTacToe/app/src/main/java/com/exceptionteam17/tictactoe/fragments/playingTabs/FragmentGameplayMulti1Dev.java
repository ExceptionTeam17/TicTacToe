package com.exceptionteam17.tictactoe.fragments.playingTabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.exceptionteam17.tictactoe.R;
import com.exceptionteam17.tictactoe.fragments.Fragment_Home;
import com.exceptionteam17.tictactoe.model.utils.Preferences;
import com.exceptionteam17.tictactoe.model.utils.Utils;

import java.util.Random;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public final class FragmentGameplayMulti1Dev extends Fragment implements View.OnClickListener{

    private final static String WINNER_PHONE = "SYSTEM_PHONE";
    private final static String WINNER_PLAYER = "SYSTEM_PLAYER";
    private final static String GAME_OVER = "SYSTEM_GAME_OVER";
    private final static String NO_WINNER = "SYSTEM_NO_WINNER";
    private boolean isPlayerTurn;
    private View view;
    private ImageView box1, box2, box3, box4, box5, box6, box7, box8, box9;
    private Button back;
    private ImageView[][] field;
    private Boolean [][] board;
    private TextView turn;
    private String username, secondPlauyerName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gameplay, container, false);
        initialize();
        setImagesClickListeners();
        return view;
    }

    private void initialize() {
        isPlayerTurn = new Random().nextBoolean();
        turn = view.findViewById(R.id.text_gameplay);
        turn.setVisibility(View.VISIBLE);
        username = Preferences.getStringFromPreferences(view.getContext(),"user");
        secondPlauyerName = getArguments().getString("secondPlayer", "secondPlayer");
        turn.setText((isPlayerTurn? username : secondPlauyerName) + ":");
        box1 = view.findViewById(R.id.single_box1);
        box2 = view.findViewById(R.id.single_box2);
        box3 = view.findViewById(R.id.single_box3);
        box4 = view.findViewById(R.id.single_box4);
        box5 = view.findViewById(R.id.single_box5);
        box6 = view.findViewById(R.id.single_box6);
        box7 = view.findViewById(R.id.single_box7);
        box8 = view.findViewById(R.id.single_box8);
        box9 = view.findViewById(R.id.single_box9);
        back = view.findViewById(R.id.back_single);
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
    }

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
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.single_box1:
                changePicture(0,0);
                checkForEndGame();
                break;
            case R.id.single_box2:
                changePicture(0,1);
                checkForEndGame();
                break;
            case R.id.single_box3:
                changePicture(0,2);
                checkForEndGame();
                break;
            case R.id.single_box4:
                changePicture(1,0);
                checkForEndGame();
                break;
            case R.id.single_box5:
                changePicture(1,1);
                checkForEndGame();
                break;
            case R.id.single_box6:
                changePicture(1,2);
                checkForEndGame();
                break;
            case R.id.single_box7:
                changePicture(2,0);
                checkForEndGame();
                break;
            case R.id.single_box8:
                changePicture(2,1);
                checkForEndGame();
                break;
            case R.id.single_box9:
                changePicture(2,2);
                checkForEndGame();
                break;
            case R.id.back_single:
                loadFragment(new Fragment_Home());
                break;
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.home_main, fragment);
        ft.commit();
    }

    private void restartGame(){
        final FragmentGameplayMulti1Dev fr = new FragmentGameplayMulti1Dev();
        Bundle bundle = new Bundle();
        bundle.putString("secondPlayer", secondPlauyerName);
        fr.setArguments(bundle);
        Utils.hideKeyboard(getActivity());
        loadFragment(fr);
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
                showAlert("DRAW", "New game?", R.drawable.ic_launcher_foreground, "PLAY", "NO");
                break;
            case NO_WINNER:
                turn.setText((isPlayerTurn? username : secondPlauyerName) + ":");
                break;
            case WINNER_PHONE:
                showAlert("YOU LOST", "New game?", R.drawable.ic_launcher_foreground, "PLAY", "NO");
                break;
            case WINNER_PLAYER:
                showAlert("YOU WON!!!", "New game?", R.drawable.ic_launcher_foreground, "PLAY", "NO");
                break;
        }
    }

    private void showAlert(String title, String msg, int icon, String posBtnText, String negativeBtnText){
        final PrettyDialog prettyDialog = new PrettyDialog(view.getContext());
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
                                restartGame();
                                prettyDialog.cancel();
                                // Do what you gotta do
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
                                loadFragment(new Fragment_Home());
                                prettyDialog.cancel();
                                // Do what you gotta do
                            }
                        }
                )
                .show();
    }
}
