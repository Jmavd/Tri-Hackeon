package com.example.tri_hackyon;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int temp;
    CheckBox cqbx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureNewPasswordButton();

        cqbx = (CheckBox) findViewById(R.id.checkBox);

        cqbx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cqbx.isChecked()){
                    showDialog();}
                else{
                /*Do Nothing For Now*/}
            }
        });
    }
    public void showDialog(){
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.encrypted_passwords_popup);

        Button btn = dialog.findViewById(R.id.buttonEnterPswd);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Entered!",Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void configureNewPasswordButton(){
        Button buttonToPassword = (Button) findViewById(R.id.buttonToPassword);
        buttonToPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewPassword.class));
            }
        });
    }
}
