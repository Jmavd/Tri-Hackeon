package com.example.tri_hackyon;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateEncryptedPassword extends AppCompatActivity {
    public EditText firstCreatePassword, firstConfirmPassword;
    CryptoHelper crypt;
    public String sentText;
    public String ultiText;
    private Button createButton;
    private String checkPswdString, checkConfirmString;
    private int y;
    private int h = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_encrypted_password);
        firstCreatePassword = findViewById(R.id.firstPopupCreatePassword);
        firstConfirmPassword = findViewById(R.id.firstPopupConfirmPassword);
        createButton = findViewById(R.id.firstPopupButtonCreate);
        passwordCheck();
    }

    //determines if password was created
    private void applyH(){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor editH = sharedPreferences.edit();
        editH.putInt("inth", h);
        editH.apply();
    }

    //compares two password boxes
    public void passwordCheck(){
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Context context = getActivity();
                SharedPreferences sharedPref = context.getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE);*/
                checkPswdString = firstCreatePassword.getText().toString();
                checkConfirmString = firstConfirmPassword.getText().toString();
                if (checkConfirmString.equals(checkPswdString)) {
                    if (!checkPswdString.isEmpty()) {
                        encryptData();
                        grabVariable();
                        y++;
                        //y=0;// This is code that, if needed, clears the variable so that it will function like new
                        h++;
                        applyH();
                        applyVariable();
                        startActivity(new Intent(CreateEncryptedPassword.this, MainActivity.class));
                    }
                    else {Toast.makeText(getApplicationContext(), "Please enter a Password", Toast.LENGTH_LONG).show();}
                }
                else {Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_LONG).show();}
            }
        });
    }

    //grabs a variable used for determining password creation
    private void grabVariable(){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        y = sharedPreferences.getInt("inti", 0);
    }

    //sets a variable used for determining password creation
    private void applyVariable(){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sharedPreferences.edit();

        editor2.putInt("inti", y);

        editor2.apply();
    }

    //hashes the password
    public void encryptData() {
        crypt = new CryptoHelper();
        sentText = firstCreatePassword.getText().toString();
        ultiText = crypt.digestString(crypt.digestString(sentText+"Immasaltyboi"));
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("password", ultiText);
        editor.apply();
        Toast.makeText(this, "Password Created!", Toast.LENGTH_SHORT).show();
    }

}
