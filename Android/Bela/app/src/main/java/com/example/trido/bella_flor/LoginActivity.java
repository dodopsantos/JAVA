package com.example.trido.bella_flor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trido.bella_flor.dao.configFirebase;
import com.example.trido.bella_flor.entities.Login;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    EditText edtLogin, edtPassword;
    Button btnLogin;

    private DatabaseReference fireBase;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListener;

    private ArrayList<Login> log;

    public static Login LOGIN = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtLogin = (EditText) findViewById(R.id.editTextLogin);
        edtPassword = (EditText) findViewById(R.id.editTextPassword);
        btnLogin = (Button) findViewById(R.id.LOGINBUTTON);

        fireBase = configFirebase.getfireBase().child("Login");
        log = new ArrayList<>();

        callLogin();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < log.size(); i++){
                    System.out.println("Login: "+log.get(i).getPassword()+edtPassword.getText());
                    if (log.get(i).getLogin().equals(edtLogin.getText().toString()) &&
                        log.get(i).getPassword().equals(edtPassword.getText().toString())){
                        LOGIN = log.get(i);
                        ReturnMain();
                        Toast.makeText(LoginActivity.this,"Bem-Vindo!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    private void ReturnMain(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void callLogin() {

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                log.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()){
                    Login pd = data.getValue(Login.class);

                    log.add(pd);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        fireBase.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        fireBase.removeEventListener(valueEventListener);
    }

}
