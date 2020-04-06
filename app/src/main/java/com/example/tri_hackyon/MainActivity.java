package com.example.tri_hackyon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
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
    String storedPswd = "ain't null";
    CheckBox cqbx;
    private int a = 0;
    private int b = 0;
    SQLHelper myDb; //instance of SQLhelper class
    private String password = "";
    private boolean auth = false;
    public static final String MESSAGE_MAIN = "com.example.tri_hackyon.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        CryptoHelper crypto = new CryptoHelper();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadA();
        //b=0;
        //applyA(); //TEST CODE - KEEP
        loadData();
        Intent intent = getIntent();
        if (b==0){
            password = intent.getStringExtra(EnterEncryptedPassword.EXTRA_MESSAGE);
            a=0;
            applyA();}
        else if (b==1){
            password = intent.getStringExtra(NewPassword.MESSAGE_NEW);
            a=0;
            applyA();}
        else if (b==2){
            password = intent.getStringExtra(DeletePassword.MESSAGE_DELETE);
            a=0;
            applyA();}
        cqbx = (CheckBox) findViewById(R.id.checkBox);
        try {
            auth = (crypto.digestString(crypto.digestString(password+"Immasaltyboi")).equals(storedPswd));
        } catch (Exception e){}
        if(auth){
            //cqbx.setChecked(true);
            try {
                parseDBU();
                parseDBE(password);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                parseDBU();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        loadVariable();
        configureButtons();

        Button buteen = (Button) findViewById(R.id.button);
        buteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configureCreateEncryptedPassword();
            }
        });


        cqbx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (z==0){
                    if(cqbx.isChecked()&&!auth){
                        storeVariable();
                        configureCreateEncryptedPassword(); }}
                    if(auth==true&&!cqbx.isChecked()){
                        auth = false;
                        resetList();
                        try {
                            parseDBU();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                if (z!=0){
                    if(cqbx.isChecked()&&!auth){
                        configureEnterEncryptedPassword();}}
                /*if(auth==true&&!cqbx.isChecked()){
                    auth = false;
                    resetList();
                    try {
                        parseDBU();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }*/
            }
        });

        //tempStoredPswd = (TextView) findViewById(R.id.textTitle);
        //tempStoredPswd.setText(password); // -- simple check for whether or not the password is being passed correctly
    }

    private void loadA(){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        //b=0;
        b = sharedPreferences.getInt("inta", 0);
    }

    private String grabIntent(Intent intent){
        String yee = "false";
        yee=intent.getStringExtra(NewPassword.MESSAGE_NEW);
        if(yee==null)
            yee = intent.getStringExtra(EnterEncryptedPassword.EXTRA_MESSAGE);
        return yee;
    }


    private void configureButtons(){
        Button buttonToPassword = (Button) findViewById(R.id.buttonToPassword);
        Button debugButton = (Button) findViewById(R.id.debug);
        Button deleteButton = (Button) findViewById(R.id.buttonToDelete);
        buttonToPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(auth) {
                    a = 1;
                    applyA();
                    Intent toAdd = new Intent(MainActivity.this, NewPassword.class);
                    toAdd.putExtra(MESSAGE_MAIN, password);
                    startActivity(toAdd);
                }
                else if(z==0)
                    configureCreateEncryptedPassword();
                else {
                    configureEnterEncryptedPassword();
                }

            }
        });
        debugButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, NewPassword.class));}
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(auth) {
                    a=2;
                    applyA();
                    Intent newPass = new Intent(MainActivity.this, DeletePassword.class);
                    newPass.putExtra(MESSAGE_MAIN, password);
                    startActivity(newPass);
                }
                else if(z==0){
                    storeVariable();
                    configureCreateEncryptedPassword();
                }
                else {
                    configureEnterEncryptedPassword();
                }
            }
        });
    }

    private void applyA(){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor editA = sharedPreferences.edit();
        editA.putInt("inta", a);
        editA.apply();
    }

    private void configureCreateEncryptedPassword(){
        startActivity(new Intent(MainActivity.this, CreateEncryptedPassword.class));
    }

    private void configureEnterEncryptedPassword(){
        startActivity(new Intent(MainActivity.this, EnterEncryptedPassword.class));
    }

    public void resetList(){
        TextView ucol = (TextView) findViewById(R.id.usercol);
        TextView pcol = (TextView) findViewById(R.id.passcol);
        TextView dcol = (TextView) findViewById(R.id.domcol);
        ucol.setText("Username:\n");
        pcol.setText("Password:\n");
        dcol.setText("Domain:\n");
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

    public void parseDBE(String key) throws Exception {
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
                pass = ((res.getString(1))); //made an oops in SQL helper class, quickfix
                domain = (res.getString(3));
                user = (crypto.decrypt(user,key)+"\n");
                pass = (crypto.decrypt(pass,key)+"\n");
                domain = (crypto.decrypt(domain,key)+"\n");
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
