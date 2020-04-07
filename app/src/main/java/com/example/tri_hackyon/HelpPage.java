package com.example.tri_hackyon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class HelpPage extends AppCompatActivity {

    public static final String MESSAGE_SETTINGS = "com.example.tri_hackyon.MESSAGE";
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        password = getIntent().getStringExtra(MainActivity.MESSAGE_MAIN);
        setA();
        //does something
    }

    private void setA(){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt("inta", 6);
        edit.apply();
    }

    private void exit(){
        Intent toMain = new Intent(HelpPage.this, MainActivity.class);
        toMain.putExtra(MESSAGE_SETTINGS, password);
        startActivity(toMain);
    }
}
