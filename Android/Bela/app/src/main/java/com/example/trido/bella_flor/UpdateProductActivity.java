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
import com.example.trido.bella_flor.entities.Product;
import com.google.firebase.database.DatabaseReference;

import static com.example.trido.bella_flor.LoginActivity.LOGIN;
import static com.example.trido.bella_flor.PdActivity.PROD;

public class UpdateProductActivity extends AppCompatActivity {

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

        edtType.setText(PROD.getType());
        edtPice.setText(PROD.getPrice());
        if (PROD.getWeight().equals("1kg")){
            edtWeightK.setChecked(true);
        }else{
            edtWeightG.setChecked(true);
        }

        pd = new Product();

        btnProduct.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if (edtType.getText().equals("") || edtPice.getText().toString().equals("")) {
                    Toast.makeText(UpdateProductActivity.this,"Preencha todos os campos", Toast.LENGTH_LONG).show();
                } else if ((edtWeightK.isChecked() && edtWeightG.isChecked())) {
                    Toast.makeText(UpdateProductActivity.this,"Apenas selecione um tipo de peso", Toast.LENGTH_LONG).show();
                } else if ((edtWeightK.isChecked() == false && edtWeightG.isChecked() == false)){
                    Toast.makeText(UpdateProductActivity.this,"Selecione o tipo do peso", Toast.LENGTH_LONG).show();
                } else {

                    pd.setType(edtType.getText().toString());
                    pd.setPrice(edtPice.getText().toString());
                    if (edtWeightK.isChecked()){
                        pd.setWeight("1kg");
                    }else{
                        pd.setWeight("400g");
                    }
                    pd.setId(PROD.getId());
                    pd.setSalesman(LOGIN.getLogin());

                    SaveProduct(pd);
                    ReturnMain();
                }
            }
        });
    }
    private boolean SaveProduct(Product pd){
        try {

            firebase = configFirebase.getfireBase().child("Product");
            firebase.child(pd.getId()).setValue(pd);
            Toast.makeText(UpdateProductActivity.this,"Produto Atualizado!", Toast.LENGTH_LONG).show();

            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private void ReturnMain(){
        Intent intent = new Intent(UpdateProductActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
