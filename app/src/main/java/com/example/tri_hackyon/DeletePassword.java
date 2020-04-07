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
    private int[] IDArr;
    private int lastUnc = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        password = intent.getStringExtra(MainActivity.MESSAGE_MAIN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_password);
        setA();
        IDArr = new int[getArrSize()];
        parseDBU();
        try {
            parseDBE(password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        checkListEmpty();
        setButtons();
    }

    private void checkListEmpty(){ //checks if the list is empty
        TextView colChk = findViewById(R.id.idDelCol);
        if((colChk.getText().toString()).equals("ID:\n")){
            colChk.append("Nothing Found");
        }
    }

    private int getArrSize(){
        myDb = new SQLHelper(this);
        Cursor unc = myDb.getUData();
        Cursor enc = myDb.getEData();
        return (unc.getCount()+enc.getCount());
    }

    private void setA(){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt("inta", 2);
        edit.apply();
    }

    public void setButtons(){
        Button delBack = findViewById(R.id.delBack);
        Button deleteButton = findViewById(R.id.delDelete);
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
        int entryNum;
        myDb = new SQLHelper(this);
        delBox = findViewById(R.id.idDelText);
        try {
            entryNum = Integer.parseInt(delBox.getText().toString());

        } catch (NumberFormatException e) {
            Toast.makeText(DeletePassword.this, "Not a Number!", Toast.LENGTH_LONG).show();
            return;
        }

        if(!(entryNum >= 1 && entryNum<=IDArr.length)){
            Toast.makeText(DeletePassword.this, "Invalid ID", Toast.LENGTH_LONG).show();
            return;
        }
        boolean enc = (entryNum>lastUnc); //if the accessed entry is encrypted
        myDb.deleteData(IDArr[entryNum-1],enc);
        Toast.makeText(DeletePassword.this, "Entry Deleted", Toast.LENGTH_LONG).show();
        exit();
    }

    private void exit(){
        Intent toMain = new Intent(DeletePassword.this, MainActivity.class);
        toMain.putExtra(MESSAGE_DELETE, password);
        startActivity(toMain);//}
    }

    private void parseDBU() {
        myDb = new SQLHelper(this); //instance of sqlHelper
        Cursor res = myDb.getUData(); //instance of SQL's cursor
        if (res.getCount() != 0) {
            String domain;
            String ID;
            String user;
            int index = 0;
            while (res.moveToNext()) { //combs through DB and adds anything to a semi-array
                user = (res.getString(2));
                ID = ((res.getString(0))); //made an oops in SQL helper class, quickfix
                domain = (res.getString(3));
                password = (res.getString(1));
                IDArr[index] = Integer.parseInt(ID);
                user = (user+"\n");
                domain = (domain+"\n");
                String indStr = (index + 1) +"\n";
                updateList(indStr, user, domain);
                index++;
            }
            lastUnc = index;
        }
    }

    private void parseDBE(String key) throws Exception {
        CryptoHelper crypto = new CryptoHelper();
        SQLHelper thisDB = new SQLHelper(this); //instance of sqlHelper
        Cursor res = thisDB.getEData(); //instance of SQL's cursor
        if (res.getCount() != 0) {
            String domain;
            String user;
            String ID;
            int index = lastUnc;
            while (res.moveToNext()) { //combs through DB and adds anything to a semi-array
                ID = (res.getString(0));
                user = (crypto.decrypt(res.getString(2), key));
                domain = (crypto.decrypt(res.getString(3), key));
                IDArr[index] = Integer.parseInt(ID);
                user = (user + "\n");
                domain = (domain + "\n");
                String indStr = (index+1)+"\n";
                updateList(indStr, user, domain);
                index++;
            }
        }
    }

    public void updateList(String i, String u, String d) { //actually updates the homescreen list
        TextView icol = findViewById(R.id.idDelCol);
        TextView ucol = findViewById(R.id.userDelCol);
        TextView dcol = findViewById(R.id.domDelCol);
        icol.append(i);
        ucol.append(u);
        dcol.append(d);
    }
}

