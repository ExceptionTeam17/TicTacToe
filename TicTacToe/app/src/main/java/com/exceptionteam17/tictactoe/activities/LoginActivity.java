package com.exceptionteam17.tictactoe.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.exceptionteam17.tictactoe.R;
import com.exceptionteam17.tictactoe.model.utils.Preferences;
import com.exceptionteam17.tictactoe.model.utils.Utils;
import com.exceptionteam17.tictactoe.model.database.DatabaseHelper;

public final class LoginActivity extends AppCompatActivity {

    private EditText username;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        removeActionBar();
        Utils.hideKeyboard(this);
        initElements();
        setOnLoginListener();
    }

    private void setOnLoginListener(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredUser = username.getText().toString().trim();
                if(enteredUser.isEmpty()){
                    username.setError("Enter valid username");
                    return;
                }

                Preferences.addStringToPreferences(v.getContext(), "user", enteredUser);
                Utils.hideKeyboard((Activity) v.getContext());

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("user", enteredUser);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setOnLayoutListener(){
        findViewById(R.id.login_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard((Activity) v.getContext());
                findViewById(R.id.login_fake).requestFocus();
            }
        });
    }

    private void initElements(){
        username = findViewById(R.id.login_username);
        login = findViewById(R.id.login_login);
        String usernameStr = Preferences.getStringFromPreferences(this, "user");
        if(!usernameStr.isEmpty() && !usernameStr.equals(Preferences.EMPTY)){
            username.setText(usernameStr);
        }
        setOnLayoutListener();
    }

    private void removeActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }
}
