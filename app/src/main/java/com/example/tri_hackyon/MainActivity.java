package com.example.tri_hackyon;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int i = 0;

    EditText storedPswd;
    EditText storingPswd;
    EditText enterTextPswd;
    CheckBox cqbx;
    SQLHelper myDb; //instance of SQLhelper class

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        parseDB(); //sets the list on the homescreen

        configureNewPasswordButton();

        storingPswd = (EditText) findViewById(R.id.editOneTextPopup);
        enterTextPswd = (EditText) findViewById(R.id.editTextPopup);

        cqbx = (CheckBox) findViewById(R.id.checkBox);

        cqbx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i==0){
                    if(cqbx.isChecked()){
                        firstShowDialog();}
                    else{}}
                else{
                    if(cqbx.isChecked()){
                        showDialog();}
                    else{
                    /*Do Nothing For Now*/}}
            }
        });
    }
    public void showDialog(){
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.encrypted_passwords_popup);

        Button btn = dialog.findViewById(R.id.buttonEnterPswd);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(enterTextPswd == storedPswd){
                    Toast.makeText(MainActivity.this, "Entered!",Toast.LENGTH_SHORT).show(); }
                else{
                    Toast.makeText(MainActivity.this, "I'm sorry, that is wrong", Toast.LENGTH_SHORT).show();}
            }
        });

        dialog.show();
    }
    public void firstShowDialog(){
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.first_encrypted_passwords_popup);

        Button btn = dialog.findViewById(R.id.buttonOneEnterPswd);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Entered!",Toast.LENGTH_SHORT).show();
                i++;
                storedPswd = storingPswd;
            }
        });

        dialog.show();
    }

    private void configureNewPasswordButton(){
        Button buttonToPassword = (Button) findViewById(R.id.buttonToPassword);
        buttonToPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i==0){
                    firstShowDialog();}
                else{
                    startActivity(new Intent(MainActivity.this, NewPassword.class));}
            }
        });
    }

    public void parseDB(){
        myDb = new SQLHelper(this); //instance of sqlHelper
        Cursor res = myDb.getAllData(); //instance of SQL's cursor

        if (res.getCount() == 0) {              //if DB is empty
            updateList("Nothing found");
            return;
        }
        else{             //if an entry exists
            StringBuffer buffer = new StringBuffer();
            while (res.moveToNext()) { //combs through DB and adds anything to a semi-array
                buffer.append("Username :" + res.getString(2) + "\n");
                buffer.append("Password :" + res.getString(1) + "\n"); //made an oops in SQL helper class, quickfix
                buffer.append("Domain :" + res.getString(3) + "\n \n");
            }
            updateList(buffer.toString());
            }
    }

    public void updateList(String message){ //actually updates the homescreen list
        TextView textView = (TextView) findViewById(R.id.dbList);
        textView.setText(message);

    }
}
