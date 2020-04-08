package com.example.tri_hackyon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HelpPage extends AppCompatActivity {

    public static final String MESSAGE_HELP = "com.example.tri_hackyon.MESSAGE";
    private String password;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_page);
        password = getIntent().getStringExtra(MainActivity.MESSAGE_MAIN);
        setA();
        setListener();
    }

    //sets the exit button's listener
    private void setListener(){
        back = findViewById(R.id.helpGoBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             exit();
            }
    });
    }

    //sets the var to determine activity
    private void setA(){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt("inta", 6);
        edit.apply();
    }

    //exits, passing pass to main
    private void exit(){
        Intent toMain = new Intent(HelpPage.this, MainActivity.class);
        toMain.putExtra(MESSAGE_HELP, password);
        startActivity(toMain);
    }
}
