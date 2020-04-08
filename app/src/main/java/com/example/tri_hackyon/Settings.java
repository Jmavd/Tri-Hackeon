package com.example.tri_hackyon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    public static final String MESSAGE_SETTINGS = "com.example.tri_hackyon.MESSAGE";
    private String password;
    private TextView oldPassBox,newPassBox,newPassBox2;
    private Button enterButton;
    private CryptoHelper crypto;
    private String[][] parsedArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        password = getIntent().getStringExtra(MainActivity.MESSAGE_MAIN);
        setA();
        enterButton = findViewById(R.id.changeButton);
        setListeners();
    }

    //sets listeners
    private void setListeners(){
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            ChangePassword();
            }
        }
        );
    }

    //the method for changing password
    private void ChangePassword(){
        crypto = new CryptoHelper();
        oldPassBox = findViewById(R.id.oldPassBox);
        newPassBox = findViewById(R.id.newPassBox1);
        newPassBox2 = findViewById(R.id.newPassBox2);
        if(verifyOld()) {
            if (newPassBox.getText().toString().equals(newPassBox2.getText().toString())){
                try {
                    updateHash();
                    rebaseDB();
                    exit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else
            Toast.makeText(this, "New Passwords Don't Match", Toast.LENGTH_SHORT).show();
        }
        else
        Toast.makeText(this, "Invalid Old Password", Toast.LENGTH_SHORT).show();
    }

    //decrypts the SQL db and reencrypts it with a new key
    private boolean rebaseDB() throws Exception{
        SQLHelper myDb = new SQLHelper(this); //instance of sqlHelper
        Cursor res = myDb.getEData(); //instance of SQL's cursor
        String oldKey = oldPassBox.getText().toString();
        String newKey = newPassBox.getText().toString();
        if (res.getCount() != 0){
            parsedArr = new String[3][res.getCount()];
            int i = 0;
            while (res.moveToNext()) { //combs through DB and adds anything to a semi-array
                parsedArr[0][i] = crypto.decrypt(res.getString(2),oldKey); //username
                parsedArr[1][i] = crypto.decrypt(res.getString(1),oldKey); //password
                parsedArr[2][i] = crypto.decrypt(res.getString(3),oldKey); //domain
            }
            myDb.emptyDB();
            while (i>=0){
                String col0 = crypto.encrypt(parsedArr[0][i],newKey);
                String col1 = crypto.encrypt(parsedArr[1][i],newKey);
                String col2 = crypto.encrypt(parsedArr[2][i],newKey);
                myDb.insertEncryptedData(col2,col0,col1);
                i--;
            }

            password = newKey;
            return true;
        }
        return false;
    }

    //updates the stored hash
    private void updateHash(){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        String hash = crypto.digestString(crypto.digestString(newPassBox.getText().toString()+"Immasaltyboi"));
        edit.putString("password", hash);
        edit.apply();
    }

    //checks if the hash matches with the stored hash
    private boolean verifyOld(){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        String oldPass = crypto.digestString(crypto.digestString(oldPassBox.getText().toString()+"Immasaltyboi"));
        return (oldPass.equals(sharedPreferences.getString("password", "")));
    }

    //sets the variable used for determining  prior activity
    private void setA(){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt("inta", 5);
        edit.apply();
    }

    //exits, passing new pass to main
    private void exit(){
        Intent toMain = new Intent(Settings.this, MainActivity.class);
        toMain.putExtra(MESSAGE_SETTINGS, password);
        startActivity(toMain);
    }
}
