package com.example.tri_hackyon;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class EnterEncryptedPassword extends AppCompatActivity {
    public String tempStoredPassword;
    public String compStoredPassword;
    public EditText enteredPassword;
    public static final String EXTRA_MESSAGE = "com.example.tri_hackyon.MESSAGE";
    CryptoHelper crypt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_encrypted_password);
        setA();
        enteredPassword = findViewById(R.id.popupEnterPassword);
        grabStoredPassword();
        enterPassword();
    }

    //for determining activity
    private void setA(){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt("inta", 4);
        edit.apply();
    }

    //sets listeners and grabs hash of textbox string
    private void enterPassword(){
        Button cancelButton = findViewById(R.id.popupButtonCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button enterButton = findViewById(R.id.popupButtonEnter);

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crypt = new CryptoHelper();
                compStoredPassword = crypt.digestString(crypt.digestString(enteredPassword.getText().toString()+"Immasaltyboi"));//compares and salts the password
                test();
            }
        });

    }

    //gets hash of stored password
    private void grabStoredPassword(){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        tempStoredPassword = sharedPreferences.getString("password", "");
    }

    //exits, passing pass to main
    public void exit(){
        Intent toMain = new Intent(EnterEncryptedPassword.this, MainActivity.class);
        toMain.putExtra(EXTRA_MESSAGE, enteredPassword.getText().toString());
        startActivity(toMain);
    }

    //checks if the password is valid
    private void test(){
        if (compStoredPassword.equals(tempStoredPassword)) {
            exit();
        }
        else { Toast.makeText(getApplicationContext(), "Incorrect Password", Toast.LENGTH_LONG).show(); }
    }

}