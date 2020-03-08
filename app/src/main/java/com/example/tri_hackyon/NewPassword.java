package com.example.tri_hackyon;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class NewPassword extends AppCompatActivity {

    //TextView changedEncryptionTitle;
    CheckBox cqbx2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        cqbx2 = (CheckBox) findViewById(R.id.checkEncryption);

        cqbx2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        configureSavePasswordButton();
    }
    public void showDialog(){
        Dialog dialog = new Dialog(NewPassword.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.encrypted_passwords_popup);

        Button btn = dialog.findViewById(R.id.buttonEnterPswd);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NewPassword.this, "Entered!",Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    /*public void onCheckboxClicked(View view) {
        changedEncryptionTitle = (TextView)findViewById(R.id.textTitle);

        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()) {
            case R.id.checkEncryption:
                if(checked)
                    changedEncryptionTitle.setText("Encrypted");
                else
                    changedEncryptionTitle.setText("Unencrypted");
                break;
        }
    }*/

    private void configureSavePasswordButton(){
        Button buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
