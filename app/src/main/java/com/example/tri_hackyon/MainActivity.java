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
        loadA(); //loads the variables for keeping track of intent
        grabH();
        if (h == 0)
            configureCreateEncryptedPassword();
        //b=0;
        //applyA(); //TEST CODE - KEEP
        Intent intent = getIntent();
        grabIntent(intent); //uses the A/B values to load the correct extra data from intents
        loadData(); //loads the hashed passwords
        cqbx = findViewById(R.id.checkBox); //doesn't have a good home
        authActivities(); //Checks if a valid password has been supplied from another method, parses SQL
        checkListEmpty(); //checks if there is data loaded
        loadVariable(); //loads values for checking if creating or entering password
        configureButtons();
    }

    private void grabH(){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        h = sharedPreferences.getInt("inth", 0);
    }

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

    private void checkListEmpty(){ //checks if the list is empty
        TextView colChk = findViewById(R.id.usercol);
        if((colChk.getText().toString()).equals("Username:\n")){
            colChk.append("Nothing Found");
        }
    }

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
    }

    private void loadA(){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        b = sharedPreferences.getInt("inta", 0);
    }

    private void configureButtons(){
        Button buttonToPassword = findViewById(R.id.buttonToPassword);
        Button deleteButton = findViewById(R.id.buttonToDelete);
        Button sendButton = findViewById(R.id.toSend);
        ImageView settings = findViewById(R.id.toSettings);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toAdd = new Intent(MainActivity.this, Settings.class);
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

        Button buteen = findViewById(R.id.button);
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
        TextView ucol = findViewById(R.id.usercol);
        TextView pcol = findViewById(R.id.passcol);
        TextView dcol = findViewById(R.id.domcol);
        ucol.setText("Username:\n");
        pcol.setText("Password:\n");
        dcol.setText("Domain:\n");
    }

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

    public void updateList(String u, String p, String d){ //actually updates the homescreen list
        TextView ucol = findViewById(R.id.usercol);
        TextView pcol = findViewById(R.id.passcol);
        TextView dcol = findViewById(R.id.domcol);
        ucol.append(u);
        pcol.append(p);
        dcol.append(d);

    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        storedPswd = sharedPreferences.getString("password", "");
    }

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
