//CLEANUP NEEDED, much code commented out, unneeded packages
package com.example.tri_hackyon;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class NewPassword extends AppCompatActivity {

    //TextView changedEncryptionTitle;
    //EditText enterTextPswd;
    //CheckBox cqbx2;

    //SQL object, text fields and button object
    SQLHelper myDb;
    EditText editSite, editUser, editPass, tmpkey;
    Button btnAdd;
    CheckBox encCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        myDb = new SQLHelper(this); //instance of new SQL DB

        //setting up text fields to feed into variables
        editSite = (EditText) findViewById(R.id.enterWebName);
        editUser = (EditText) findViewById(R.id.enterUserName);
        editPass = (EditText) findViewById(R.id.enterPass);
        tmpkey = (EditText) findViewById(R.id.tempKey);
        btnAdd = (Button) findViewById(R.id.buttonSave);
        encCheck = (CheckBox) findViewById(R.id.checkEncryption);
        AddData();
        backButton();
    }

    //adds data in text fields to the SQL DB on click of the button
    public void AddData() {
        btnAdd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(encCheck.isChecked()){
                            try {
                                addEncrypted();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            addUnencrypted();
                        }
                    }
                }
        );
    }

    public void backButton() {
        Button button = findViewById(R.id.newPwdBack);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewPassword.this, MainActivity.class));
            }
        });
    }

    public void addUnencrypted() {
        boolean isInserted = myDb.insertData(editSite.getText().toString(), //checks text boxes and passes it to SQLHelper's data inserter
                editUser.getText().toString(),
                editPass.getText().toString(), false);
        if (isInserted == true)
            Toast.makeText(NewPassword.this, "data inserted", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(NewPassword.this, "data not inserted", Toast.LENGTH_LONG).show();

        startActivity(new Intent(NewPassword.this, MainActivity.class)); //moves back to homescreen
    }

    public void addEncrypted() throws Exception {
        CryptoHelper crypto = new CryptoHelper();
        String key = tmpkey.getText().toString();
        boolean isInserted = myDb.insertData(crypto.encrypt(editSite.getText().toString(), key), //checks text boxes and passes it to SQLHelper's data inserter
                crypto.encrypt(editUser.getText().toString(),key),
                crypto.encrypt(editPass.getText().toString(),key),
                true);
        if (isInserted == true)
            Toast.makeText(NewPassword.this, "data inserted", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(NewPassword.this, "data not inserted", Toast.LENGTH_LONG).show();

        startActivity(new Intent(NewPassword.this, MainActivity.class)); //moves back to homescreen
    }
}

