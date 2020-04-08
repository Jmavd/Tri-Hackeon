package com.example.tri_hackyon;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.OutputStream;
import ioio.lib.api.Uart;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;

public class SendPassword extends IOIOActivity {

    private TextView sendBox, error;
    private Uart uart;
    private OutputStream out;
    private boolean pressed = false;
    private Button sendBut, sendBack;
    private SQLHelper myDb;
    private String[] [] parsedArray;
    private Integer lastUnc = 0;
    private String stringToSend,password;
    public static final String MESSAGE_SEND = "com.example.tri_hackyon.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_password);
        sendBut = findViewById(R.id.sendSend);
        sendBack = findViewById(R.id.sendBack);
        sendBox = findViewById(R.id.sendTextBox);
        error = findViewById(R.id.sendError);
        Intent intent = getIntent();
        password = intent.getStringExtra(MainActivity.MESSAGE_MAIN);
        parsedArray = new String [4] [getArrSize()];
        if(password.isEmpty()) {
            parseDBU();
            setA(0);
        }
        else {
            setA(3);
            parseDBU();
            try {
                parseDBE(password);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        checkListEmpty();
        setListeners();
    }

    //sets the various onclicks
    private void setListeners(){
        sendBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toMain = new Intent(SendPassword.this, MainActivity.class);
                toMain.putExtra(MESSAGE_SEND, password);
                startActivity(toMain);
            }
        });
        sendBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInput();

            }
        });
    }

    //sets the variable to determine activity intent for main
    private void setA(int a){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt("inta", a);
        edit.apply();
    }

    //gets the size the array should be
    public int getArrSize(){
        myDb = new SQLHelper(this);
        Cursor unc = myDb.getUData();
        if(!password.isEmpty()){
        Cursor enc = myDb.getEData();
        return (unc.getCount()+enc.getCount());
        }
        else
            return unc.getCount();
    }

    //parse SQL DB and update list
    public void parseDBU() {
        myDb = new SQLHelper(this); //instance of sqlHelper
        Cursor res = myDb.getUData(); //instance of SQL's cursor
            if (res.getCount() != 0) { //if the DB isn't empty
            String domain;
            String ID;
            String user;
            String password;
            int index = 0;
            while (res.moveToNext()) { //combs through DB and adds anything to a semi-array
                user = (res.getString(2));
                ID = ((res.getString(0))); //made an oops in SQL helper class, quickfix
                domain = (res.getString(3));
                password = (res.getString(1));
                parsedArray[0] [index] = ID;
                parsedArray[1] [index] = password;
                parsedArray[2] [index] = user;
                parsedArray[3] [index] = domain;
                user = (user+"\n");
                domain = (domain+"\n");
                String indStr = (index+1)+"\n";
                updateList(indStr, user, domain);
                index++;
            }
            lastUnc = index;
        }
    }
    public void parseDBE(String key) throws Exception {
        CryptoHelper crypto = new CryptoHelper();
        SQLHelper thisDB = new SQLHelper(this); //instance of sqlHelper
        Cursor res = thisDB.getEData(); //instance of SQL's cursor
        if (res.getCount() != 0) {              //if DB is empty
            String domain;
            String pass;
            String user;
            String ID;
            int index = lastUnc;
            while (res.moveToNext()) { //combs through DB and adds anything to a semi-array
                ID = (res.getString(0));
                user = (crypto.decrypt(res.getString(2), key));
                pass = (crypto.decrypt(res.getString(1), key)); //made an oops in SQL helper class, quickfix
                domain = (crypto.decrypt(res.getString(3), key));
                parsedArray[0][index] = ID;
                parsedArray[1][index] = pass;
                parsedArray[2][index] = user;
                parsedArray[3][index] = domain;
                user = (user + "\n");
                domain = (domain + "\n");
                String indStr = (index+1)+"\n";
                updateList(indStr, user, domain);
                index++;
            }
        }
    }
    public void updateList(String i, String u, String d){ //actually updates the password list
        TextView icol = findViewById(R.id.sendIDCol);
        TextView ucol = findViewById(R.id.sendUserCol);
        TextView dcol = findViewById(R.id.sendDomCol);
        icol.append(i);
        ucol.append(u);
        dcol.append(d);
    }
    public void checkListEmpty(){ //checks if the list is empty
        TextView colChk = findViewById(R.id.sendIDCol);
        if((colChk.getText().toString()).equals("ID:\n")){
            colChk.append("Nothing Found");
        }
    }

    //the code that controls the IOIO-OTG
    class Looper extends BaseIOIOLooper { //This is the code that runs on the IOIO
        //runs once when connected
        @Override
        protected void setup() throws ConnectionLostException { //runs when IOIO is connected
            toast("Connected!");
            uart = ioio_.openUart(10,3,9600, Uart.Parity.NONE, Uart.StopBits.ONE);
            out = uart.getOutputStream();
            enableUi(true);
        }

        //runs repeatedly while connected
        @Override
        public void loop() throws InterruptedException { //runs repeatedly after setup
            if(pressed) {
                try {
                    out.write(stringToSend.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                pressed = false;
            }
            Thread.sleep(100);
        }

        //runs when disconnected
        @Override
        public void disconnected() {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            uart.close();
            enableUi(false);

        }
        //runs if IOIO has wrong firmware
        @Override
        public void incompatible() {
            toast("Incompatible firmware version!");
        }
}

    //gets the input for the password to send
    private void getInput(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int toSend;
                try {
                    toSend = Integer.parseInt(sendBox.getText().toString());
                } catch (NumberFormatException e){
                    toSend = -1;
                }

                if((toSend<=parsedArray[1].length)&&(toSend>0)){
                    stringToSend = parsedArray[1][toSend - 1];
                    pressed = true;
                    Intent toMain = new Intent(SendPassword.this, MainActivity.class);
                    toMain.putExtra(MESSAGE_SEND, password);
                    startActivity(toMain);
                }
                else
                    Toast.makeText(SendPassword.this, "Invalid ID", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected IOIOLooper createIOIOLooper() {
        return new Looper();
    }

    //sends a toast on the UI thread
    private void toast(final String message) {
        final Context context = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int numConnected_ = 0;

    //used to enable/disable button
    private void enableUi(final boolean enable) {
        // This is slightly trickier than expected to support a multi-IOIO use-case.

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (enable) {
                    if (numConnected_++ == 0) {
                        sendBut.setEnabled(true);
                        error.setVisibility(View.INVISIBLE);
                    }
                } else {
                    if (--numConnected_ == 0) {
                        sendBut.setEnabled(false);
                        error.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }
}