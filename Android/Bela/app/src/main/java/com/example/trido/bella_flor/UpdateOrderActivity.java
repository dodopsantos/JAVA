package com.example.trido.bella_flor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.trido.bella_flor.dao.configFirebase;
import com.example.trido.bella_flor.entities.Order;
import com.google.firebase.database.DatabaseReference;

import static com.example.trido.bella_flor.OrActivity.ORD;

public class UpdateOrderActivity extends AppCompatActivity {

    private DatabaseReference firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_client);

        ORD.setStatus("Autorizado");
        SaveOrder(ORD);
        ReturnMain();

    }

    private void ReturnMain(){
        Intent intent = new Intent(UpdateOrderActivity.this, OrActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean SaveOrder(Order or){
        try {

            firebase = configFirebase.getfireBase().child("Order");
            firebase.child(or.getId()).setValue(or);
            Toast.makeText(UpdateOrderActivity.this,"Pedido Autorizado!", Toast.LENGTH_LONG).show();

            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
