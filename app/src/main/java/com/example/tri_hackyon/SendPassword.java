package com.example.tri_hackyon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.Uart;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;

public class SendPassword extends IOIOActivity {

    TextView sendBox, error;
    private ToggleButton button_;
    private byte[] arr = {'e','a','t',' ','m','o','r','e'};
    Uart uart;
    OutputStream out;
    boolean pressed = false;
    private Button buteen;
    private SQLHelper myDb;
    private String[] [] parsedArray;
    private Integer lastUnc;
    private String stringToSend;
    public static final String MESSAGE_SEND = "com.example.tri_hackyon.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_password);
        setA();
        buteen = findViewById(R.id.sendSend);
        sendBox = findViewById(R.id.sendTextBox);
        error = findViewById(R.id.sendError);
        parsedArray = new String [4] [getArrSize()];
        Intent intent = getIntent();
        final String password = intent.getStringExtra(MainActivity.MESSAGE_MAIN);
        parseDBU();

        try {
            parseDBE(password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        buteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInput();
                pressed = true;
                Intent toMain = new Intent(SendPassword.this, MainActivity.class);
                toMain.putExtra(MESSAGE_SEND, password);
                startActivity(toMain);
            }
        });
    }

    private void setA(){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt("inta", 3);
        edit.apply();
    }

    public int getArrSize(){
        myDb = new SQLHelper(this);
        Cursor unc = myDb.getUData();
        Cursor enc = myDb.getEData();
        return (unc.getCount()+enc.getCount());
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
                String indStr = (Integer.toString(index)+"\n");
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
        if (res.getCount() == 0) {              //if DB is empty
            updateList("Nothing found", "", "");
            return;
        } else {             //if an entry exists
            StringBuffer buffer = new StringBuffer();
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
                String indStr = (Integer.toString(index)+"\n");
                updateList(indStr, user, domain);
                index++;
            }
        }
    }
        public void updateList(String i, String u, String d){ //actually updates the homescreen list
            TextView ucol = (TextView) findViewById(R.id.sendIDCol);
            TextView pcol = (TextView) findViewById(R.id.sendUserCol);
            TextView dcol = (TextView) findViewById(R.id.sendDomCol);
            ucol.append(i);
            pcol.append(u);
            dcol.append(d);
        }

    class Looper extends BaseIOIOLooper { //This is the code that runs on the IOIO
        @Override
        protected void setup() throws ConnectionLostException { //runs when IOIO is connected
            showVersions(ioio_, "Connected!");
            uart = ioio_.openUart(10,3,9600, Uart.Parity.NONE, Uart.StopBits.ONE);
            out = uart.getOutputStream();
            enableUi(true);
        }

        @Override
        public void loop() throws ConnectionLostException, InterruptedException { //runs repeatedly after setup
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

        @Override
        public void disconnected() {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            uart.close();
            enableUi(false);
            toast("IOIO disconnected");

        }

        @Override
        public void incompatible() {
            showVersions(ioio_, "Incompatible firmware version!");
        }
}

    private void getInput(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int toSend;
                toSend = Integer.parseInt(sendBox.getText().toString());
                stringToSend = parsedArray[1][toSend];
            }
        });
    }

    @Override
    protected IOIOLooper createIOIOLooper() {
        return new Looper();
    }

    private void showVersions(IOIO ioio, String title) {
        toast(String.format("%s\n" +
                        "IOIOLib: %s\n" +
                        "Application firmware: %s\n" +
                        "Bootloader firmware: %s\n" +
                        "Hardware: %s",
                title,
                ioio.getImplVersion(IOIO.VersionType.IOIOLIB_VER),
                ioio.getImplVersion(IOIO.VersionType.APP_FIRMWARE_VER),
                ioio.getImplVersion(IOIO.VersionType.BOOTLOADER_VER),
                ioio.getImplVersion(IOIO.VersionType.HARDWARE_VER)));
    }

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

    private void enableUi(final boolean enable) {
        // This is slightly trickier than expected to support a multi-IOIO use-case.

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (enable) {
                    if (numConnected_++ == 0) {
                        buteen.setEnabled(true);
                        error.setVisibility(View.INVISIBLE);
                    }
                } else {
                    if (--numConnected_ == 0) {
                        buteen.setEnabled(false);
                        error.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }
}