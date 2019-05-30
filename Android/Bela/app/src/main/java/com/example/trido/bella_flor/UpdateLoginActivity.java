package com.example.trido.bella_flor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trido.bella_flor.dao.configFirebase;
import com.example.trido.bella_flor.entities.Login;
import com.google.firebase.database.DatabaseReference;

import static com.example.trido.bella_flor.LoginActivity.LOGIN;

public class UpdateLoginActivity extends AppCompatActivity {

    private EditText user;
    private EditText newPassword, confirmPassword;
    private Button btnUpdateLogin;

    private DatabaseReference firebase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_login);

        user = (EditText) findViewById(R.id.edtTextLogin);
        newPassword = (EditText) findViewById(R.id.edtTextPassword);
        confirmPassword = (EditText) findViewById(R.id.editTextPasswordConfirm);
        btnUpdateLogin = (Button) findViewById(R.id.LOGINUPDATE);

        user.setText(LOGIN.getLogin());

        btnUpdateLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newPassword.getText().toString().equals(confirmPassword.getText().toString())){
                    LOGIN.setPassword(newPassword.getText().toString());
                    updateUser(LOGIN);
                    ReturnLogin();
                }else{
                    Toast.makeText(UpdateLoginActivity.this,"Senhas não conferem", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private boolean updateUser(Login log){
        try {

            firebase = configFirebase.getfireBase().child("Login");
            firebase.child(log.getLogin()).setValue(log);
            Toast.makeText(UpdateLoginActivity.this,"Usuário atualizado!", Toast.LENGTH_LONG).show();

            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    private void ReturnLogin(){
        Intent intent = new Intent(UpdateLoginActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
