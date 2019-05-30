package com.example.trido.bella_flor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trido.bella_flor.dao.configFirebase;
import com.example.trido.bella_flor.entities.Client;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.trido.bella_flor.ClActivity.FLAG;
import static com.example.trido.bella_flor.LoginActivity.LOGIN;

public class UpdateClientActivity extends AppCompatActivity {
    private EditText edtName, edtCNPJ, edtIE, edtCountry, edtUF, edtpPlace, edtNumber, edtCEP, edtTel, edtCel, edtEmail, edtBairro;
    private Button btnRegister;
    private CheckBox fisic, juridic;
    private Client cl;
    private DatabaseReference firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);


        edtName = (EditText) findViewById(R.id.editTextName);
        fisic = (CheckBox) findViewById(R.id.fisic);
        juridic = (CheckBox) findViewById(R.id.juridic);
        edtCNPJ = (EditText) findViewById(R.id.editTextCNPJ);
        edtIE = (EditText) findViewById(R.id.editTextIE);
        edtCountry = (EditText) findViewById(R.id.editTextCountry);
        edtUF = (EditText) findViewById(R.id.editTextUF);
        edtpPlace = (EditText) findViewById(R.id.editTextpPlace);
        edtNumber = (EditText) findViewById(R.id.editTextNumber);
        edtCEP = (EditText) findViewById(R.id.editTextCEP);
        edtTel = (EditText) findViewById(R.id.editTexTel);
        edtCel = (EditText) findViewById(R.id.editTextCel);
        edtEmail = (EditText) findViewById(R.id.editTextEmail);
        btnRegister = (Button) findViewById(R.id.FINISHBUTTON);
        edtBairro = (EditText) findViewById(R.id.editTextnBorhood);

        SimpleMaskFormatter smfTel = new SimpleMaskFormatter("(NN)NNNNN-NNNN");
        SimpleMaskFormatter smfCel = new SimpleMaskFormatter("(NN)NNNN-NNNN");
        SimpleMaskFormatter smfCEP = new SimpleMaskFormatter("NNNNN-NNN");
        SimpleMaskFormatter smfUF = new SimpleMaskFormatter("LL");
        SimpleMaskFormatter smfCNPJ = new SimpleMaskFormatter("NN.NNN.NNN/NNNN-NN");

        MaskTextWatcher mtwCNPJ = new MaskTextWatcher(edtCNPJ, smfCNPJ);
        MaskTextWatcher mtwUF = new MaskTextWatcher(edtUF, smfUF);
        MaskTextWatcher mtwCEP = new MaskTextWatcher(edtCEP, smfCEP);
        MaskTextWatcher mtwTel = new MaskTextWatcher(edtTel, smfTel);
        MaskTextWatcher mtwCel= new MaskTextWatcher(edtCel, smfCel);

        edtCel.addTextChangedListener(mtwCel);
        edtTel.addTextChangedListener(mtwTel);
        edtCEP.addTextChangedListener(mtwCEP);
        edtUF.addTextChangedListener(mtwUF);
        edtCNPJ.addTextChangedListener(mtwCNPJ);

        edtName.setText(FLAG.getName());
        if (FLAG.getPerson().equals("Jurídica")){
            juridic.setChecked(true);

        }else{
            fisic.setChecked(false);
        }
        edtCNPJ.setText(FLAG.getCnpj());
        edtIE.setText(FLAG.getIe());
        edtCountry.setText(FLAG.getCountry());
        edtUF.setText(FLAG.getUf());
        edtpPlace.setText(FLAG.getPublic_place());
        edtNumber.setText(FLAG.getNumber());
        edtCEP.setText(FLAG.getCep());
        edtTel.setText(FLAG.getTel());
        edtCel.setText(FLAG.getCel());
        edtEmail.setText(FLAG.getEmail());
        edtBairro.setText(FLAG.getNeighborhood());

        cl = new Client();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                cl = new Client();

                if (edtName.getText().equals("") ||
                        edtCNPJ.getText().toString().equals("") || edtIE.getText().toString().equals("") || edtCountry.getText().toString().equals("")
                        || edtUF.getText().toString().equals("") || edtpPlace.getText().toString().equals("") || edtNumber.getText().toString().equals("")
                        || edtCEP.getText().toString().equals("") || edtTel.getText().toString().equals("") || edtCel.getText().toString().equals("")
                        || edtEmail.getText().toString().equals("") || edtBairro.getText().toString().equals("")) {
                    Toast.makeText(UpdateClientActivity.this,"Preencha todos os campos", Toast.LENGTH_LONG).show();
                } else if ((fisic.isChecked() && juridic.isChecked())) {
                    Toast.makeText(UpdateClientActivity.this,"Apenas selecione um tipo de pessoa", Toast.LENGTH_LONG).show();
                } else if ((fisic.isChecked() == false && juridic.isChecked() == false)){
                    Toast.makeText(UpdateClientActivity.this,"Selecione o tipo de pessoa", Toast.LENGTH_LONG).show();
                } else {
                    cl.setName(edtName.getText().toString());
                    if (fisic.isChecked()){
                        cl.setPerson("Fisíca");
                    }else{
                        cl.setPerson("Jurídica");
                    }
                    cl.setCnpj(edtCNPJ.getText().toString());
                    cl.setIe(edtIE.getText().toString());
                    cl.setCountry(edtCountry.getText().toString());
                    cl.setUf(edtUF.getText().toString());
                    cl.setPublic_place(edtpPlace.getText().toString());
                    cl.setNumber(edtNumber.getText().toString());
                    cl.setCep(edtCEP.getText().toString());
                    cl.setTel(edtTel.getText().toString());
                    cl.setCel(edtCel.getText().toString());
                    cl.setEmail(edtEmail.getText().toString());
                    cl.setNeighborhood(edtBairro.getText().toString());
                    cl.setId(FLAG.getId());
                    cl.setSalesman(LOGIN.getLogin());

                    SaveClient(cl);
                    ReturnMain();
                }
            }
        });
    }

    private boolean SaveClient(Client cl){
        try {

            firebase = configFirebase.getfireBase().child("Client");
            firebase.child(cl.getId()).setValue(cl);
            Toast.makeText(UpdateClientActivity.this,"Cliente Atualizado!", Toast.LENGTH_LONG).show();

            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private void ReturnMain(){
        Intent intent = new Intent(UpdateClientActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

