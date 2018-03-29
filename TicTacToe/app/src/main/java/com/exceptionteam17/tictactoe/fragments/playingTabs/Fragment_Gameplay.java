package com.exceptionteam17.tictactoe.fragments.playingTabs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.exceptionteam17.tictactoe.R;
import com.exceptionteam17.tictactoe.fragments.Fragment_Home;
import com.exceptionteam17.tictactoe.model.database.DatabaseHelper;
import com.exceptionteam17.tictactoe.model.utils.Preferences;
import com.exceptionteam17.tictactoe.model.utils.Utils;

import java.util.Random;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public final class Fragment_Gameplay extends Fragment implements View.OnClickListener{

    private final static String WINNER_PHONE = "SYSTEM_PHONE";
    private final static String WINNER_PLAYER = "SYSTEM_PLAYER";
    private final static String GAME_OVER = "SYSTEM_GAME_OVER";
    private final static String NO_WINNER = "SYSTEM_NO_WINNER";
    private boolean isPlayerTurn, isSimple, isGameOver;
    private View view;
    private ImageView box1, box2, box3, box4, box5, box6, box7, box8, box9;
    private Button back;
    private ImageView[][] field;
    private int a [][];
    private int sum;
    private Boolean [][] board;
    private DatabaseHelper db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gameplay, container, false);
        initialize();
        setImagesClickListeners();
        if(!isPlayerTurn){
            if(isSimple) {
                Random r = new Random();
                play(r.nextInt(3), r.nextInt(3));
            } else {
                changePicture(0,0);
            }
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(this.getActivity() != null) {
            Utils.hideKeyboard(this.getActivity());
        }
    }

    private void initialize() {
        db = DatabaseHelper.getInstance(this.getContext());
        if(getArguments() != null && getArguments().containsKey("isSimple")){
            isSimple = getArguments().getBoolean("isSimple");
        } else {
            isSimple = true;
        }
        isPlayerTurn = new Random().nextBoolean();
        isGameOver = false;
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

        a = new int[][]{
                {0,0,0},
                {0,0,0},
                {0,0,0}
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
                play(0,0);
                break;
            case R.id.single_box2:
                play(0,1);
                break;
            case R.id.single_box3:
                play(0,2);
                break;
            case R.id.single_box4:
                play(1,0);
                break;
            case R.id.single_box5:
                play(1,1);
                break;
            case R.id.single_box6:
                play(1,2);
                break;
            case R.id.single_box7:
                play(2,0);
                break;
            case R.id.single_box8:
                play(2,1);
                break;
            case R.id.single_box9:
                play(2,2);
                break;
            case R.id.back_single:
                loadFragment(new Fragment_Home());
                break;
        }
    }

    private void play(int x, int y){
        if(changePicture(x,y)){
            if(!isPlayerTurn && !isGameOver){
                computerMove();
                Log.e("ivan", "NOW ITS phone move");
            } else {
                Log.e("ivan", "NOW ITS player move");
            }
        }
    }

    private void loadFragment(Fragment fragment) {
        if(getActivity() == null){
            return;
        }
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.home_main, fragment);
        ft.commit();
    }

    private boolean changePicture(int x, int y){
        if(isPlayerTurn) {
            if(board[x][y] == null) {
                field[x][y].setImageResource(R.drawable.x);
                isPlayerTurn = false;
                Log.e("ivan", ""+isPlayerTurn);
                board[x][y] = true;
                a[x][y]+=2;
                checkForEndGame();
                Log.e("ivan", "player pic changed");
                return true;
            }
        }  else {
            if(board[x][y] == null) {
                field[x][y].setImageResource(R.drawable.o);
                isPlayerTurn = true;
                Log.e("ivan", ""+isPlayerTurn);
                board[x][y] = false;
                a[x][y]++;
                checkForEndGame();
                Log.e("ivan", "phone pic changed");
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
                isGameOver = true;
                showAlert("DRAW", "New game?", R.mipmap.ic_popup, "PLAY", "NO");
                db.addUserDraw(Preferences.getStringFromPreferences(this.getContext(), "user"));
                break;
            case NO_WINNER:
                if(!isPlayerTurn){
                    int i = 0;
                    for(int j =0; j <3; j++){
                        for(int k = 0; k<3; k++){
                            if(board[j][k] == null){
                                i++;
                            }
                        }
                    }
                    if(i == 1){
                        computerMove();
                    }
                }
                break;
            case WINNER_PHONE:
                isGameOver = true;
                showAlert("YOU LOST", "New game?", R.mipmap.ic_popup, "PLAY", "NO");
                db.addUserLose(Preferences.getStringFromPreferences(this.getContext(), "user"));
                break;
            case WINNER_PLAYER:
                isGameOver = true;
                showAlert("YOU WON!!!", "New game?", R.mipmap.ic_popup, "PLAY", "NO");
                db.addUserWin(Preferences.getStringFromPreferences(this.getContext(), "user"));
                break;
            default:
                Log.e("ivan", "shit");
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
                                final Fragment_Gameplay fr = new Fragment_Gameplay();
                                Bundle bundle = new Bundle();
                                bundle.putBoolean("isSimple", isSimple);
                                fr.setArguments(bundle);
                                loadFragment(fr);
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

    private void computerMove(){
        if(isSimple){
            int position = new Random().nextInt(9);
            Log.e("test", "" + position);
            while(board[position/3][position%3] != null){
                position +=1;
                position %=9;
            }
            changePicture(position / 3,position % 3);
        } else {
            makeOwn();
        }
    }

    public void makeOwn() {
        sum = 0;

        for (int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                sum += a[i][j];
            }
        }

        if (a[0][0] == a[0][1] && a[0][0] == 1 && a[0][2] == 0) {
            changePicture(0,2);

        }else if (a[0][1] == a[0][2] && a[0][1] == 1 && a[0][0] == 0) {
            changePicture(0,0);

        }else if (a[0][0] == a[0][2] && a[0][0] == 1 && a[0][1] == 0) {
            changePicture(0,1);

        }else if (a[1][0] == a[1][1] && a[1][0] == 1 && a[1][2] == 0) {
            changePicture(1,2);

        }else if (a[1][1] == a[1][2] && a[1][1] == 1 && a[1][0] == 0) {
            changePicture(1,0);

        }else if (a[1][0] == a[1][2] && a[1][0] == 1 && a[1][1] == 0) {
            changePicture(1,1);

        }else if (a[2][0] == a[2][1] && a[2][0] == 1 && a[2][2] == 0) {
            changePicture(2,2);

        }else if (a[2][1] == a[2][2] && a[2][1] == 1 && a[2][0] == 0) {
            changePicture(2,0);

        }else if (a[2][0] == a[2][2] && a[2][0] == 1 && a[2][1] == 0) {
            changePicture(2,1);

        }else if (a[0][0] == a[1][0] && a[0][0] == 1 && a[2][0] == 0) {
            changePicture(2,0);

        }else if (a[1][0] == a[2][0] && a[1][0] == 1 && a[0][0] == 0) {
            changePicture(0,0);

        }else if (a[0][0] == a[2][0] && a[0][0] == 1 && a[1][0] == 0) {
            changePicture(1,0);

        }else if (a[0][1] == a[1][1] && a[0][1] == 1 && a[2][1] == 0) {
            changePicture(2,1);

        }else if (a[1][1] == a[2][1] && a[1][1] == 1 && a[0][1] == 0) {
            changePicture(0,1);

        }else if (a[0][1] == a[2][1] && a[0][1] == 1 && a[1][1] == 0) {
            changePicture(1,1);

        }else if (a[0][2] == a[1][2] && a[0][2] == 1 && a[2][2] == 0) {
            changePicture(2,2);

        }else if (a[1][2] == a[2][2] && a[1][2] == 1 && a[0][2] == 0) {
            changePicture(0,2);

        }else if (a[0][2] == a[2][2] && a[0][2] == 1 && a[1][2] == 0) {
            changePicture(1,2);

        }else if (a[0][0] == a[1][1] && a[0][0] == 1 && a[2][2] == 0) {
            changePicture(2,2);

        }else if (a[1][1] == a[2][2] && a[1][1] == 1 && a[0][0] == 0) {
            changePicture(0,0);

        }else if (a[0][0] == a[2][2] && a[0][0] == 1 && a[1][1] == 0) {
            changePicture(1,1);

        }else if (a[0][2] == a[1][1] && a[0][2] == 1 && a[2][0] == 0) {
            changePicture(2,0);

        }else if (a[1][1] == a[2][0] && a[1][1] == 1 && a[0][2] == 0) {
            changePicture(0,2);

        }else if (a[0][2] == a[2][0] && a[0][2] == 1 && a[1][1] == 0) {
            changePicture(1,1);

        }else {
            prevent();
        }
    }

    public void prevent() {
        if (a[0][0] == a[0][1] && a[0][0] == 2 && a[0][2] == 0) {
            changePicture(0,2);

        }else if (a[0][1] == a[0][2] && a[0][1] == 2 && a[0][0] == 0) {
            changePicture(0,0);

        }else if (a[0][0] == a[0][2] && a[0][0] == 2 && a[0][1] == 0) {
            changePicture(0,1);

        }else if (a[1][0] == a[1][1] && a[1][0] == 2 && a[1][2] == 0) {
            changePicture(1,2);

        }else if (a[1][1] == a[1][2] && a[1][1] == 2 && a[1][0] == 0) {
            changePicture(1,0);

        }else if (a[1][0] == a[1][2] && a[1][0] == 2 && a[1][1] == 0) {
            changePicture(1,1);

        }else if (a[2][0] == a[2][1] && a[2][0] == 2 && a[2][2] == 0) {
            changePicture(2,2);

        }else if (a[2][1] == a[2][2] && a[2][1] == 2 && a[2][0] == 0) {
            changePicture(2,0);

        }else if (a[2][0] == a[2][2] && a[2][0] == 2 && a[2][1] == 0) {
            changePicture(2,1);

        }else if (a[0][0] == a[1][0] && a[0][0] == 2 && a[2][0] == 0) {
            changePicture(2,0);

        }else if (a[1][0] == a[2][0] && a[1][0] == 2 && a[0][0] == 0) {
            changePicture(0,0);

        }else if (a[0][0] == a[2][0] && a[0][0] == 2 && a[1][0] == 0) {
            changePicture(1,0);

        }else if (a[0][1] == a[1][1] && a[0][1] == 2 && a[2][1] == 0) {
            changePicture(2,1);

        }else if (a[1][1] == a[2][1] && a[1][1] == 2 && a[0][1] == 0) {
            changePicture(0,1);

        }else if (a[0][1] == a[2][1] && a[0][1] == 2 && a[1][1] == 0) {
            changePicture(1,1);

        }else if (a[0][2] == a[1][2] && a[0][2] == 2 && a[2][2] == 0) {
            changePicture(2,2);

        }else if (a[1][2] == a[2][2] && a[1][2] == 2 && a[0][2] == 0) {
            changePicture(0,2);

        }else if (a[0][2] == a[2][2] && a[0][2] == 2 && a[1][2] == 0) {
            changePicture(1,2);

        }else if (a[0][0] == a[1][1] && a[0][0] == 2 && a[2][2] == 0) {
            changePicture(2,2);

        }else if (a[1][1] == a[2][2] && a[1][1] == 2 && a[0][0] == 0) {
            changePicture(0,0);

        }else if (a[0][0] == a[2][2] && a[0][0] == 2 && a[1][1] == 0) {
            changePicture(1,1);

        }else if (a[0][2] == a[1][1] && a[0][2] == 2 && a[2][0] == 0) {
            changePicture(2,0);

        }else if (a[1][1] == a[2][0] && a[1][1] == 2 && a[0][2] == 0) {
            changePicture(0,2);

        }else if (a[0][2] == a[2][0] && a[0][2] == 2 && a[1][1] == 0) {
            changePicture(1,1);

        }else {
            specialMove();
        }
    }

    public void specialMove() {
        if (a[0][0] == 1 && a[1][1] == 2 && sum == 3) {
            changePicture(2,2);

        }else if (a[0][0] == 1 && a[2][1] == 2 && sum == 3) {
            changePicture(0,2);

        }else if (a[0][0] == 1 && a[1][2] == 2 && sum == 3) {
            changePicture(2,0);

        }else if (a[0][0] == 1 && a[2][2] == 2 && sum == 3) {
            changePicture(2,0);

        }else if (a[2][0] == 1 && a[1][0] == 2 && a[0][0] == 1 && a[2][2] == 2 && sum == 6) {
            changePicture(0,2);

        }else if (a[0][0] == 1 && (a[0][2] == 2 || a[2][0] == 2) && sum == 3) {
            changePicture(2,2);

        }else if (a[0][0] == 1 && a[0][1] == 2 && sum == 3) {
            changePicture(2,0);

        }else if (a[0][0] == 1 && a[0][1] == 2 && a[2][0] == 1 && a[1][0] == 2 && sum == 6) {
            changePicture(1,1);

        }else if (a[0][0] == 1 && a[1][0] == 2 && sum == 3) {
            changePicture(0,2);

        }else if (a[0][0] == 1 && a[1][0] == 2 && a[0][2] == 1 && a[0][1] == 2 && sum == 6) {
            changePicture(1,1);

        } else {
            int position = new Random().nextInt(9);
            while(board[position/3][position%3] != null){
                position +=1;
                position %=9;
            }
            changePicture(position / 3,position % 3);
        }
    }
}
