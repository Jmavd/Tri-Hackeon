//CLEANUP NEEDED, much code commented out, unneeded packages
package com.example.tri_hackyon;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
    EditText editSite,editUser,editPass;
    Button btnAdd;
    private String compStoredPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        myDb = new SQLHelper(this); //instance of new SQL DB

        //setting up text fields to feed into variables
        editSite = (EditText)findViewById(R.id.enterWebName);
        editUser = (EditText)findViewById(R.id.enterUserName);
        editPass = (EditText)findViewById(R.id.enterPass);
        btnAdd = (Button)findViewById(R.id.buttonSave);
        AddData();
        loadCompStoredPassword();
        backButton();
    }

    //adds data in text fields to the SQL DB on click of the button
    public void AddData() {
        btnAdd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted = myDb.insertData(editSite.getText().toString(), //checks text boxes and passes it to SQLHelper's data inserter
                                editUser.getText().toString(),
                                editPass.getText().toString());
                        if (isInserted == true)
                            Toast.makeText(NewPassword.this, "data inserted", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(NewPassword.this, "data not inserted", Toast.LENGTH_LONG).show();

                        startActivity(new Intent(NewPassword.this, MainActivity.class)); //moves back to homescreen
                    }
                }
        );
    }

    private void loadCompStoredPassword(){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        compStoredPassword = sharedPreferences.getString("enteredPassword", "");
    }

    public void backButton(){
        Button button = findViewById(R.id.newPwdBack);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewPassword.this, MainActivity.class));
            }
        });
    }

}

//Jonathan's depreciated code
        /*
        cqbx2 = (CheckBox) findViewById(R.id.checkEncryption);
        changedEncryptionTitle = (TextView) findViewById(R.id.textTitle);
        enterTextPswd = (EditText) findViewById(R.id.editTextPopup);
        // For right now, the following code is obsolete: storedPswd = ((EditText) findViewById(R.id.userPswd);

        cqbx2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cqbx2.isChecked()){
                    showDialog();
                    // Same as above: if(enterTextPswd == storedPswd){
                    //      changedEncryptionTitle.setText("Encrypted") }
                    changedEncryptionTitle.setText("Encrypted Password");}
                else{
                    changedEncryptionTitle.setText("Unencrypted Password");
                /*Do Nothing For Now}
            }
        });

        configureSavePasswordButton();
    }
    public void showDialog(){
        Dialog dialog = new Dialog(NewPassword.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.encrypted_passwords_popup);

        Button btn = dialog.findViewById(R.id.buttonEnterPswd);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NewPassword.this, "Entered!",Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void configureSavePasswordButton(){
        Button buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

         */
