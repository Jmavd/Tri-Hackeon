package com.example.tri_hackyon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EnterEncryptedPassword extends AppCompatActivity {

    public String tempStoredPassword;
    public String compStoredPassword;
    public EditText enteredPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_encrypted_password);
        enteredPassword = (EditText) findViewById(R.id.popupEnterPassword);
        grabStoredPassword();
        //TextView temp = (TextView) findViewById(R.id.PopupTitle);
        //temp.setText(tempStoredPassword); Just another quick check
        enterPassword();
    }

    private void enterPassword(){
        Button cancelButton = (Button) findViewById(R.id.popupButtonCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button enterButton = (Button) findViewById(R.id.popupButtonEnter);

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compStoredPassword = enteredPassword.getText().toString();
                if (compStoredPassword.equals(tempStoredPassword)){
                    //note, later on, upon entering the right password, NEW CODE will have to be added that will show the encrypted passwords
                    //on the main activity. This will probably be done with a temporary int that is saved and checked on the homescreen
                    startActivity(new Intent(EnterEncryptedPassword.this, MainActivity.class));}
                else { Toast.makeText(getApplicationContext(), "I'm sorry, that password does not match.", Toast.LENGTH_LONG).show(); }
            }
        });
    }

    private void grabStoredPassword(){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        tempStoredPassword = sharedPreferences.getString("password", "");
    }
}