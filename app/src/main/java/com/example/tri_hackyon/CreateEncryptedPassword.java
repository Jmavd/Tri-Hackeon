package com.example.tri_hackyon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateEncryptedPassword extends AppCompatActivity {
    public EditText firstCreatePassword;
    CryptoHelper crypt;
    public String sentText;
    public String ultiText;
    private Button cancelButton;
    private Button createButton;
    private String storedPassword;
    private String checkPswdString;
    private String compStoredPassword;
    private int y;

    //public static final String SHARED_PREFS = "sharedPrefs";
    //public static final String TEXT = "text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_encrypted_password);
        loadData();
        loadCompStoredPassword();
        firstCreatePassword = (EditText) findViewById(R.id.firstPopupCreatePassword);
        //storedPassword = firstPopupCreatePassword.getText().toString(); //(Temporary Storage - now deprecated)
        loadData();
        updateData();
        Button overrideButton = (Button) findViewById(R.id.button2);
        overrideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overrideCheck();
            }
        });
        enterCreatedPassword();
    }

    private void overrideCheck(){
        encryptData();
        startActivity(new Intent(CreateEncryptedPassword.this, MainActivity.class));
    }

    private void enterCreatedPassword(){
        cancelButton = (Button) findViewById(R.id.firstPopupButtonCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        createButton = (Button) findViewById(R.id.firstPopupButtonCreate);
        passwordCheck();
    }

    public void passwordCheck(){
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Context context = getActivity();
                SharedPreferences sharedPref = context.getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE);*/ //attempted implementation of shared pref
                checkPswdString = firstCreatePassword.getText().toString();
                if (checkPswdString != null && !checkPswdString.isEmpty()){
                    encryptData();
                    grabVariable();
                    y++;
                    //y=0;// This is code that, if needed, clears the variable so that it will function like new
                    applyVariable();
                    startActivity(new Intent(CreateEncryptedPassword.this, MainActivity.class));}
                else {Toast.makeText(getApplicationContext(), "I'm sorry, please enter a password.", Toast.LENGTH_LONG).show();}
            }
        });
    }

    private void grabVariable(){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        y = sharedPreferences.getInt("inti", 0);
    }
    private void applyVariable(){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sharedPreferences.edit();

        editor2.putInt("inti", y);

        editor2.apply();
    }

    public void encryptData() {
        crypt = new CryptoHelper();
        sentText = firstCreatePassword.getText().toString();
        ultiText = crypt.digestString(sentText);
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("password", ultiText);
        editor.apply();
        Toast.makeText(this, "Password Created!", Toast.LENGTH_SHORT).show();
    }

    private void loadCompStoredPassword(){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        compStoredPassword = sharedPreferences.getString("enteredPassword", "");
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        storedPassword = sharedPreferences.getString("password", ""); // "" at the end sets the default value to nothing
    }

    public void updateData(){
        firstCreatePassword.setText(storedPassword);
    }
}
