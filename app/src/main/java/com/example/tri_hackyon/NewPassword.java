//CLEANUP NEEDED, much code commented out, unneeded packages
package com.example.tri_hackyon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

public class NewPassword extends AppCompatActivity {
    private static final int MAX_LENGTH = 3;
    SQLHelper myDb;
    EditText editSite, editUser, editPass;
    Button btnAdd,genCheck;
    CheckBox encCheck;
    private SecureRandom sr;
    public static final String MESSAGE_NEW = "com.example.tri_hackyon.YESSAGE";
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        sr = new SecureRandom();
        setVars();
        setA();
        AddData();
        backButton();
    }

    //adds data in text fields to the SQL DB on click of the button

    private void setVars(){
        Intent intent = getIntent();
        password = intent.getStringExtra(MainActivity.MESSAGE_MAIN);
        myDb = new SQLHelper(this); //instance of new SQL DB
        editSite = findViewById(R.id.enterWebName);
        editUser = findViewById(R.id.enterUserName);
        editPass = findViewById(R.id.enterPass);
        genCheck = findViewById(R.id.genPass);
        genCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    editPass.setText(random());
            }
        });
        btnAdd = findViewById(R.id.buttonSave);
        encCheck = findViewById(R.id.checkEncryption);

    }

    private void AddData() {
        btnAdd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(encCheck.isChecked()){
                            try {
                                addEncrypted();
                               exit();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            addUnencrypted();
                            exit();
                        }
                    }
                }
        );
    }

    private void setA(){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt("inta", 1);
        edit.apply();
    }

    private void exit(){
        Intent toMain = new Intent(NewPassword.this, MainActivity.class);
        toMain.putExtra(MESSAGE_NEW, password);
        startActivity(toMain);//}
    }

    public String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(MAX_LENGTH)+9;
        char tempChar;
        for (int i = 0; i <= randomLength; i++){
            tempChar = (char) (sr.nextInt(95) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    private void backButton() {
        Button button = findViewById(R.id.newPwdBack);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });
    }

    private void addUnencrypted() {
        boolean isInserted = myDb.insertData(editSite.getText().toString(), //checks text boxes and passes it to SQLHelper's data inserter
                editUser.getText().toString(),
                editPass.getText().toString());
        if (isInserted){
            Toast.makeText(NewPassword.this, "data inserted", Toast.LENGTH_LONG).show();}
        else
            Toast.makeText(NewPassword.this, "data not inserted", Toast.LENGTH_LONG).show();

    }

    private void addEncrypted() throws Exception {
        CryptoHelper crypto = new CryptoHelper();
        String key = password;
        boolean isInserted = myDb.insertEncryptedData(crypto.encrypt(editSite.getText().toString(), key), //checks text boxes and passes it to SQLHelper's data inserter
                crypto.encrypt(editUser.getText().toString(),key),
                crypto.encrypt(editPass.getText().toString(),key));
        if (isInserted)
            Toast.makeText(NewPassword.this, "Password Added!", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(NewPassword.this, "Error: Data not inserted", Toast.LENGTH_LONG).show();
    }
}
