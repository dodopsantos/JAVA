package com.example.trido.bella_flor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trido.bella_flor.dao.configFirebase;
import com.example.trido.bella_flor.entities.Client;
import com.github.rtoshiro.util.format.MaskFormatter;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.text.SimpleDateFormat;
import java.util.Date;
import static com.example.trido.bella_flor.LoginActivity.LOGIN;



public class ClientActivity extends AppCompatActivity {

    private EditText edtName, edtCNPJ, edtIE, edtCountry, edtUF, edtpPlace, edtNumber, edtCEP, edtTel, edtCel, edtEmail, edtnBorhood;
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
        edtnBorhood = (EditText) findViewById(R.id.editTextnBorhood);

        SimpleMaskFormatter smfTel = new SimpleMaskFormatter("(NN)NNNN-NNNN");
        SimpleMaskFormatter smfCel = new SimpleMaskFormatter("(NN)NNNNN-NNNN");
        SimpleMaskFormatter smfCEP = new SimpleMaskFormatter("NNNNN-NNN");
        SimpleMaskFormatter smfUF = new SimpleMaskFormatter("LL");
        SimpleMaskFormatter smfCNPJ = new SimpleMaskFormatter("NN.NNN.NNN/NNNN-NN");
        SimpleMaskFormatter smfIE = new SimpleMaskFormatter("NNN.NNN.NNN");

        MaskTextWatcher mtwCNPJ = new MaskTextWatcher(edtCNPJ, smfCNPJ);
        MaskTextWatcher mtwUF = new MaskTextWatcher(edtUF, smfUF);
        MaskTextWatcher mtwCEP = new MaskTextWatcher(edtCEP, smfCEP);
        MaskTextWatcher mtwTel = new MaskTextWatcher(edtTel, smfTel);
        MaskTextWatcher mtwCel= new MaskTextWatcher(edtCel, smfCel);
        MaskTextWatcher mtwIE = new MaskTextWatcher(edtIE, smfIE);

        edtCel.addTextChangedListener(mtwCel);
        edtTel.addTextChangedListener(mtwTel);
        edtCEP.addTextChangedListener(mtwCEP);
        edtUF.addTextChangedListener(mtwUF);
        edtCNPJ.addTextChangedListener(mtwCNPJ);
        edtIE.addTextChangedListener(mtwIE);

        cl = new Client();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                cl = new Client();

                if (edtName.getText().equals("") ||
                        edtCNPJ.getText().toString().equals("") || edtIE.getText().toString().equals("") || edtCountry.getText().toString().equals("")
                        || edtUF.getText().toString().equals("") || edtpPlace.getText().toString().equals("") || edtNumber.getText().toString().equals("")
                        || edtCEP.getText().toString().equals("") || edtTel.getText().toString().equals("") || edtCel.getText().toString().equals("")
                        || edtEmail.getText().toString().equals("") || edtnBorhood.getText().toString().equals("")) {
                    Toast.makeText(ClientActivity.this,"Preencha todos os campos", Toast.LENGTH_LONG).show();
                } else if ((fisic.isChecked() && juridic.isChecked())) {
                    Toast.makeText(ClientActivity.this,"Apenas selecione um tipo de pessoa", Toast.LENGTH_LONG).show();
                } else if ((fisic.isChecked() == false && juridic.isChecked() == false)){
                    Toast.makeText(ClientActivity.this,"Selecione o tipo de pessoa", Toast.LENGTH_LONG).show();
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
                    cl.setId(getCurrentTimeStamp());
                    cl.setSalesman(LOGIN.getLogin());
                    cl.setNeighborhood(edtnBorhood.getText().toString());

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
            Toast.makeText(ClientActivity.this,"Cliente Cadastrado!", Toast.LENGTH_LONG).show();

            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static String getCurrentTimeStamp(){
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    private void ReturnMain(){
        Intent intent = new Intent(ClientActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
