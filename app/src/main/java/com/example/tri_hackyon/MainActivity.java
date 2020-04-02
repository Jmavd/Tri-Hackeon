package com.example.tri_hackyon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int i = 0;
    int z = 0;
    //public final String SHARED_PREFS = "sharedPrefs";
    //public static final String TEXT = "text";
    /*public static void setDefaults(String key, String value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }*/

    TextView tempStoredPswd;
    String storedPswd = "";
    CheckBox cqbx;
    SQLHelper myDb; //instance of SQLhelper class
    String cryptoKey = "key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            parseDBenc(cryptoKey); //sets the list on the homescreen
        } catch (Exception e) {
            e.printStackTrace();
        }
        //z = 0;
        //loadData();
        loadVariable();
        loadData();
        configureButtons();

        Button buteen = (Button) findViewById(R.id.button);
        buteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreateEncryptedPassword.class));
            }
        });
        cqbx = (CheckBox) findViewById(R.id.checkBox);

        cqbx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (z==0){
                    if(cqbx.isChecked()){
                        storeVariable();
                        configureCreateEncryptedPassword(); }}
                if (z!=0){
                    if(cqbx.isChecked()){
                        configureEnterEncryptedPassword();}}
            }
        });
    }


    private void configureButtons(){
        Button buttonToPassword = (Button) findViewById(R.id.buttonToPassword);
        Button debugButton = (Button) findViewById(R.id.debug);
        Button deleteButton = (Button) findViewById(R.id.buttonToDelete);
        buttonToPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(z==0)
                    configureCreateEncryptedPassword();
                else
                    startActivity(new Intent(MainActivity.this, NewPassword.class));}
        });
        debugButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, NewPassword.class));}
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DeletePassword.class));
            }
        });
    }


    private void configureCreateEncryptedPassword(){
        startActivity(new Intent(MainActivity.this, CreateEncryptedPassword.class));
    }

    private void configureEnterEncryptedPassword(){
        startActivity(new Intent(MainActivity.this, EnterEncryptedPassword.class));
    }



    public void parseDBenc(String key) throws Exception {
        CryptoHelper crypto = new CryptoHelper();
        myDb = new SQLHelper(this); //instance of sqlHelper
        Cursor res = myDb.getAllData(); //instance of SQL's cursor

        if (res.getCount() == 0) {              //if DB is empty
            updateList("Nothing found","","");
            return;
        }
        else{             //if an entry exists
            StringBuffer buffer = new StringBuffer();
            while (res.moveToNext()) { //combs through DB and adds anything to a semi-array
                boolean encrypted = (res.getInt(4)!=0);
                String user = (res.getString(2));
                String pass = ((res.getString(1))); //made an oops in SQL helper class, quickfix
                String domain = (res.getString(3));
                if(encrypted){
                    user = (crypto.decrypt(user,key)+"\n");
                    pass = (crypto.decrypt(pass,key)+"\n");
                    domain = (crypto.decrypt(domain,key)+"\n");
                }
                else {
                    user = (user+"\n");
                    pass = (pass+"\n");
                    domain = (domain+"\n");
                }
                if(pass.length()<10)
                updateList(user, pass, domain);
            }
        }
    }

    public void updateList(String u, String p, String d){ //actually updates the homescreen list
        TextView ucol = (TextView) findViewById(R.id.usercol);
        TextView pcol = (TextView) findViewById(R.id.passcol);
        TextView dcol = (TextView) findViewById(R.id.domcol);
        ucol.append(u);
        pcol.append(p);
        dcol.append(d);

    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        storedPswd = sharedPreferences.getString("password", "");

    }
    /*public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        storedPswd = sharedPreferences.getString(TEXT, ""); // "" at the end sets the default value to nothing
    }*/

    public void storeVariable(){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("inti", i);

        editor.apply();
    }

    public void loadVariable(){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        z = sharedPreferences.getInt("inti", 0);
    }
}
