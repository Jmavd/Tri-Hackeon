package com.example.tri_hackyon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    int i = 0;
    int z = 0;
    String storedPswd = "ain't null";
    CheckBox cqbx;
    private int a;
    private int b;
    private int h;
    SQLHelper myDb;
    private String password = "";
    private boolean auth = false;
    public static final String MESSAGE_MAIN = "com.example.tri_hackyon.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadA();
        grabH();
        if (h == 0)
            configureCreateEncryptedPassword();
        Intent intent = getIntent();
        grabIntent(intent);
        loadData();
        cqbx = findViewById(R.id.checkBox);
        authActivities();
        checkListEmpty();
        loadVariable();
        configureButtons();
    }

    //checks if user has created a password
    private void grabH(){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        h = sharedPreferences.getInt("inth", 0);
    }

    //authenticates user and runs various tasks
    private void authActivities(){
        CryptoHelper crypto = new CryptoHelper();
        try {
            auth = (crypto.digestString(crypto.digestString(password+"Immasaltyboi")).equals(storedPswd));
        } catch (Exception ignored){}
        if(auth){
            Button buttonToPassword = findViewById(R.id.buttonToPassword);
            Button deleteButton = findViewById(R.id.buttonToDelete);
            Button sendButton = findViewById(R.id.toSend);
            buttonToPassword.setEnabled(true);
            deleteButton.setEnabled(true);
            sendButton.setEnabled(true);
            cqbx.setChecked(true);
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
    }

    //checks if there are any entries listed in the column
    private void checkListEmpty(){ //checks if the list is empty
        TextView colChk = findViewById(R.id.usercol);
        if((colChk.getText().toString()).equals("Username:\n")){
            colChk.append("Nothing Found");
        }
    }

    //grabs the intent from the prior activity
    private void grabIntent(Intent intent){
        if (b==4){
            password = intent.getStringExtra(EnterEncryptedPassword.EXTRA_MESSAGE);
            a=0;
            applyA();
        }
        else if (b==1){
            password = intent.getStringExtra(NewPassword.MESSAGE_NEW);
            a=0;
            applyA();
        }
        else if (b==2){
            password = intent.getStringExtra(DeletePassword.MESSAGE_DELETE);
            a=0;
            applyA();
        }
        else if (b == 3){
            password = intent.getStringExtra(SendPassword.MESSAGE_SEND);
            a=0;
            applyA();
        }
        else if(b == 5){
            password = intent.getStringExtra(Settings.MESSAGE_SETTINGS);
            a=0;
            applyA();
        }
        else if(b==6){
            password = intent.getStringExtra(HelpPage.MESSAGE_HELP);
            a=0;
            applyA();
        }
    }

    //gets the variable for determining prior activity
    private void loadA(){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        b = sharedPreferences.getInt("inta", 0);
    }

    //configures clicks
    private void configureButtons(){
        Button buttonToPassword = findViewById(R.id.buttonToPassword);
        Button deleteButton = findViewById(R.id.buttonToDelete);
        Button sendButton = findViewById(R.id.toSend);
        ImageView settings = findViewById(R.id.toSettings);
        ImageView help = findViewById(R.id.toHelp);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toAdd = new Intent(MainActivity.this, Settings.class);
                toAdd.putExtra(MESSAGE_MAIN, password);
                startActivity(toAdd);
            }
        }
        );

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toAdd = new Intent(MainActivity.this, HelpPage.class);
                toAdd.putExtra(MESSAGE_MAIN, password);
                startActivity(toAdd);
            }
        }
        );

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toAdd = new Intent(MainActivity.this, SendPassword.class);
                toAdd.putExtra(MESSAGE_MAIN, password);
                startActivity(toAdd);
            }
        }
        );

        buttonToPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent toAdd = new Intent(MainActivity.this, NewPassword.class);
                    toAdd.putExtra(MESSAGE_MAIN, password);
                    startActivity(toAdd);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent newPass = new Intent(MainActivity.this, DeletePassword.class);
                    newPass.putExtra(MESSAGE_MAIN, password);
                    startActivity(newPass);
            }
        });

        cqbx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (z==0){
                    if(cqbx.isChecked()&&!auth){
                        storeVariable();
                        //configureCreateEncryptedPassword();
                        }}
                if(auth&&!cqbx.isChecked()){
                    auth = false;
                    Button buttonToPassword = findViewById(R.id.buttonToPassword);
                    Button deleteButton = findViewById(R.id.buttonToDelete);
                    Button sendButton = findViewById(R.id.toSend);
                    buttonToPassword.setEnabled(false);
                    deleteButton.setEnabled(false);
                    sendButton.setEnabled(false);
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
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newPass = new Intent(MainActivity.this, DeletePassword.class);
                newPass.putExtra(MESSAGE_MAIN, password);
                startActivity(newPass);
            }
        });

    }

    //sets the variable for determining prior activity
    private void applyA(){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor editA = sharedPreferences.edit();
        editA.putInt("inta", a);
        editA.apply();
    }

    //runs the method for creating app password
    private void configureCreateEncryptedPassword(){
        startActivity(new Intent(MainActivity.this, CreateEncryptedPassword.class));
    }

    //runs the method for entering app password
    private void configureEnterEncryptedPassword(){
        startActivity(new Intent(MainActivity.this, EnterEncryptedPassword.class));
    }

    //when the user unchecks the box
    public void resetList(){
        TextView ucol = findViewById(R.id.usercol);
        TextView pcol = findViewById(R.id.passcol);
        TextView dcol = findViewById(R.id.domcol);
        ucol.setText("Username:\n");
        pcol.setText("Password:\n");
        dcol.setText("Domain:\n");
    }

    //parses the unencrypted SQL database
    public void parseDBU() {
        myDb = new SQLHelper(this); //instance of sqlHelper
        Cursor res = myDb.getUData(); //instance of SQL's cursor
        if (res.getCount() != 0) {
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

    //parses the encrypted SQL database
    public void parseDBE(String key) throws Exception {
        CryptoHelper crypto = new CryptoHelper();
        myDb = new SQLHelper(this); //instance of sqlHelper
        Cursor res = myDb.getEData(); //instance of SQL's cursor
        if (res.getCount() != 0){
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

    //updates the lists on the homescreen
    public void updateList(String u, String p, String d){ //actually updates the homescreen list
        TextView ucol = findViewById(R.id.usercol);
        TextView pcol = findViewById(R.id.passcol);
        TextView dcol = findViewById(R.id.domcol);
        ucol.append(u);
        pcol.append(p);
        dcol.append(d);

    }

    //loads the hashed password
    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        storedPswd = sharedPreferences.getString("password", "");
    }

    //stores a variable used for checking if a password was created
    public void storeVariable(){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("inti", i);

        editor.apply();
    }

    //loads a variable for checking if a password was created
    public void loadVariable(){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        z = sharedPreferences.getInt("inti", 0);
    }
}
