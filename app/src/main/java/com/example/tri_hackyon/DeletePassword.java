package com.example.tri_hackyon;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_password);
        parseDB();
        setButtons();
    }


    public void setButtons(){
        Button delBack = (Button) findViewById(R.id.delBack);
        Button deleteButton = (Button) findViewById(R.id.delDelete);
        delBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DeletePassword.this, NewPassword.class));
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
        startActivity(new Intent(DeletePassword.this, MainActivity.class));
    }


    public void parseDB(){
        myDb = new SQLHelper(this); //instance of sqlHelper
        Cursor res = myDb.getAllData(); //instance of SQL's cursor

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

