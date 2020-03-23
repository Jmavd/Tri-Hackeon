package com.example.tri_hackyon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EnterEncryptedPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_encrypted_password);

        enterPassword();
    }

    private void enterPassword(){
        Button cancelButton = (Button) findViewById(R.id.popupButtonCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button createButton = (Button) findViewById(R.id.popupButtonEnter);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Will check the entered password later
                finish();
            }
        });
    }
}