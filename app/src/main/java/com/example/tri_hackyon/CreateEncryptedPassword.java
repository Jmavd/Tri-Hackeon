package com.example.tri_hackyon;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateEncryptedPassword extends AppCompatActivity {
    public EditText firstCreatePassword, firstConfirmPassword;
    CryptoHelper crypt;
    public String sentText;
    public String ultiText;
    private Button createButton;
    private String storedPassword;
    private String checkPswdString, checkConfirmString;
    private int y;
    private int h = 0;

    //public static final String SHARED_PREFS = "sharedPrefs";
    //public static final String TEXT = "text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_encrypted_password);
        loadData();
        firstCreatePassword = findViewById(R.id.firstPopupCreatePassword);
        firstConfirmPassword = findViewById(R.id.firstPopupConfirmPassword);
        loadData();
        updateData();
        enterCreatedPassword();
    }

    private void applyH(){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor editH = sharedPreferences.edit();
        editH.putInt("inth", h);
        editH.apply();
    }

    private void enterCreatedPassword(){
        Button cancelButton = findViewById(R.id.firstPopupButtonCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        createButton = findViewById(R.id.firstPopupButtonCreate);
        passwordCheck();
    }

    public void passwordCheck(){
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Context context = getActivity();
                SharedPreferences sharedPref = context.getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE);*/
                checkPswdString = firstCreatePassword.getText().toString();
                checkConfirmString = firstConfirmPassword.getText().toString();
                if (checkConfirmString.equals(checkPswdString)) {
                    if (!checkPswdString.isEmpty()) {
                        encryptData();
                        grabVariable();
                        y++;
                        //y=0;// This is code that, if needed, clears the variable so that it will function like new
                        h++;
                        applyH();
                        applyVariable();
                        startActivity(new Intent(CreateEncryptedPassword.this, MainActivity.class));
                    }
                    else {Toast.makeText(getApplicationContext(), "Please enter a Password", Toast.LENGTH_LONG).show();}
                }
                else {Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_LONG).show();}
            }
        });
    }

    private void grabVariable(){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        y = sharedPreferences.getInt("inti", 0);
    }

    private void applyVariable(){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sharedPreferences.edit();

        editor2.putInt("inti", y);

        editor2.apply();
    }

    public void encryptData() {
        crypt = new CryptoHelper();
        sentText = firstCreatePassword.getText().toString();
        ultiText = crypt.digestString(crypt.digestString(sentText+"Immasaltyboi"));
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("password", ultiText);
        editor.apply();
        Toast.makeText(this, "Password Created!", Toast.LENGTH_SHORT).show();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        storedPassword = sharedPreferences.getString("password", ""); // "" at the end sets the default value to nothing
    }

    public void updateData(){
        firstCreatePassword.setText(storedPassword);
    }

}
