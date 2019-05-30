package com.example.trido.bella_flor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trido.bella_flor.dao.configFirebase;
import com.example.trido.bella_flor.entities.Client;
import com.example.trido.bella_flor.entities.Product;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import static com.example.trido.bella_flor.LoginActivity.LOGIN;

public class ProductActivity extends AppCompatActivity {

    private EditText edtType, edtPice;
    private CheckBox edtWeightK, edtWeightG;
    private Button btnProduct;
    private Product pd;
    private DatabaseReference firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        edtType = (EditText) findViewById(R.id.editTextType);
        edtPice = (EditText) findViewById(R.id.editTextPrice);
        edtWeightK = (CheckBox) findViewById(R.id.kg);
        edtWeightG = (CheckBox) findViewById(R.id.g);
        btnProduct = (Button) findViewById(R.id.FINISHPRODUCT);

        pd = new Product();

        btnProduct.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if (edtType.getText().equals("") || edtPice.getText().toString().equals("")) {
                    Toast.makeText(ProductActivity.this,"Preencha todos os campos", Toast.LENGTH_LONG).show();
                } else if ((edtWeightK.isChecked() && edtWeightG.isChecked())) {
                    Toast.makeText(ProductActivity.this,"Apenas selecione um tipo de peso", Toast.LENGTH_LONG).show();
                } else if ((edtWeightK.isChecked() == false && edtWeightG.isChecked() == false)){
                    Toast.makeText(ProductActivity.this,"Selecione o tipo do peso", Toast.LENGTH_LONG).show();
                } else {

                    pd.setType(edtType.getText().toString());
                    pd.setPrice(edtPice.getText().toString());
                    if (edtWeightK.isChecked()){
                        pd.setWeight("1kg");
                    }else{
                        pd.setWeight("400g");
                    }
                    pd.setId(getCurrentTimeStamp());
                    pd.setSalesman(LOGIN.getLogin());

                    SaveProduct(pd);
                    ReturnMain();
                }
            }
        });
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
    private boolean SaveProduct(Product pd){
        try {

            firebase = configFirebase.getfireBase().child("Product");
            firebase.child(pd.getId()).setValue(pd);
            Toast.makeText(ProductActivity.this,"Produto Cadastrado!", Toast.LENGTH_LONG).show();

            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private void ReturnMain(){
        Intent intent = new Intent(ProductActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
