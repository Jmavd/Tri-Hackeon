package com.example.tri_hackyon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DeletePassword extends AppCompatActivity {

    SQLHelper myDb;
    EditText delBox;
    public static final String MESSAGE_DELETE = "com.example.tri_hackyon.MESSAGE";
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        password = intent.getStringExtra(MainActivity.MESSAGE_MAIN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_password);
        setA();
        parseDBU();
        try {
            parseDBE(password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setButtons();
    }

    private void setA(){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt("inta", 2);
        edit.apply();
    }



    public void setButtons(){
        Button delBack = (Button) findViewById(R.id.delBack);
        Button deleteButton = (Button) findViewById(R.id.delDelete);
        delBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteID();
            }
        });

    }

    private void deleteID() {
        int ID = 0;
        myDb = new SQLHelper(this);
        delBox = (EditText)findViewById(R.id.idDelText);
        try {
            ID = Integer.parseInt(delBox.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(DeletePassword.this, "Invalid ID", Toast.LENGTH_LONG).show();
            return;
        }
        myDb.deleteData(ID);
        Toast.makeText(DeletePassword.this, "Entry Deleted", Toast.LENGTH_LONG).show();
        exit();
    }

    private void exit(){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt("inta", 2);
        Intent toMain = new Intent(DeletePassword.this, MainActivity.class);
        toMain.putExtra(MESSAGE_DELETE, password);
        startActivity(toMain);//}
    }

    public void parseDBU() {
        myDb = new SQLHelper(this); //instance of sqlHelper
        Cursor res = myDb.getUData(); //instance of SQL's cursor

        if (res.getCount() == 0) {              //if DB is empty
            updateList("Nothing found","","");
            return;
        }
        else{             //if an entry exists
            StringBuffer buffer = new StringBuffer();
            String domain;
            String pass;
            String user;
            while (res.moveToNext()) { //combs through DB and adds anything to a semi-array
                user = (res.getString(2));
                pass = ((res.getString(1))); //made an oops in SQL helper class, quickfix
                domain = (res.getString(3));
                user = (user+"\n");
                pass = (pass+"\n");
                domain = (domain+"\n");
                updateList(user, pass, domain);
            }
        }
    }

    private void parseDBE(String key) throws Exception {
        CryptoHelper crypto = new CryptoHelper();
        myDb = new SQLHelper(this); //instance of sqlHelper
        Cursor res = myDb.getEData(); //instance of SQL's cursor
        if (res.getCount() == 0) {              //if DB is empty
            updateList("Nothing found","","");
            return;
        }
        else{             //if an entry exists
            StringBuffer buffer = new StringBuffer();
            String domain;
            String pass;
            String user;
            while (res.moveToNext()) { //combs through DB and adds anything to a semi-array
                user = (res.getString(2));
                pass = (res.getString(1)); //made an oops in SQL helper class, quickfix
                domain = (res.getString(3));
                user = (crypto.decrypt(user,key)+"\n");
                pass = (crypto.decrypt(pass,key)+"\n");
                domain = (crypto.decrypt(domain,key)+"\n");
                updateList(user, pass, domain);
            }
        }
    }
    public void parseDB(){
        myDb = new SQLHelper(this); //instance of sqlHelper
        Cursor res = myDb.getUData(); //instance of SQL's cursor

        if (res.getCount() == 0) {              //if DB is empty
            updateList("Nothing found","","");
            return;
        }
        else{             //if an entry exists
            StringBuffer buffer = new StringBuffer();
            while (res.moveToNext()) { //combs through DB and adds anything to a semi-array
                String id = (res.getString(0) + "\n");
                String user = (res.getString(2) + "\n"); //made an oops in SQL helper class, quickfix
                String domain = (res.getString(3) + "\n");
                updateList(id, user, domain);
            }
        }
    }

    public void updateList(String i, String u, String d) { //actually updates the homescreen list
        TextView icol = (TextView) findViewById(R.id.idDelCol);
        TextView ucol = (TextView) findViewById(R.id.userDelCol);
        TextView dcol = (TextView) findViewById(R.id.domDelCol);
        icol.append(i);
        ucol.append(u);
        dcol.append(d);
    }
}

