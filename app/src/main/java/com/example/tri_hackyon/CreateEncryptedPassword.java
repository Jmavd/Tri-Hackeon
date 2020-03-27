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
    private Button cancelButton;
    private Button createButton;
    private String storedPassword;
    private String checkPswdString;

    //public static final String SHARED_PREFS = "sharedPrefs";
    //public static final String TEXT = "text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_encrypted_password);
        loadData();
        firstCreatePassword = (EditText) findViewById(R.id.firstPopupCreatePassword);
        //storedPassword = firstPopupCreatePassword.getText().toString(); //(Temporary Storage - now deprecated)
        loadData();
        updateData();

        enterCreatedPassword();
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
                checkPswdString = firstCreatePassword.getText().toString(); //WILL FIX THIS CHECK LATER
                if (checkPswdString != null && !checkPswdString.isEmpty()){
                    saveData();
                    startActivity(new Intent(CreateEncryptedPassword.this, MainActivity.class));}
                else {Toast.makeText(getApplicationContext(), "I'm sorry, please enter a password.", Toast.LENGTH_LONG).show();}
            }
        });
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("password", firstCreatePassword.getText().toString());

        editor.apply();

        Toast.makeText(this, "Password Created!", Toast.LENGTH_SHORT).show();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        storedPassword = sharedPreferences.getString("password", ""); // "" at the end sets the default value to nothing
    }

    public void updateData(){
        firstCreatePassword.setText(storedPassword);
    }
}
