package com.example.trido.bella_flor;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.trido.bella_flor.adapter.ProductListAdapter;
import com.example.trido.bella_flor.dao.configFirebase;
import com.example.trido.bella_flor.entities.Client;
import com.example.trido.bella_flor.entities.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.trido.bella_flor.LoginActivity.LOGIN;

public class PdActivity extends AppCompatActivity {

    private ListView listViewProd;
    private ArrayAdapter adapter;
    private ArrayList<Product> pds;
    private DatabaseReference fireBase;
    private ValueEventListener valueEventListener;
    private FloatingActionButton btnFloat;

    public static Product PROD = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pd);

        pds = new ArrayList<>();
        listViewProd = (ListView) findViewById(R.id.listItensProdu);
        adapter = new ProductListAdapter(this, pds);
        listViewProd.setAdapter(adapter);
        fireBase = configFirebase.getfireBase().child("Product");

        btnFloat = (FloatingActionButton) findViewById(R.id.btnFloatProduct);


        btnFloat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                RegisterProduct();
            }
        });

        listViewProd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Product product = pds.get(position);

                PROD = pds.get(position);
                showProductDialog(product.getId(), product.getType(), PROD);
            }
        });

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pds.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()){
                    Product pd = data.getValue(Product.class);
                    if (pd.getSalesman().equals(LOGIN.getLogin()) || LOGIN.getLogin().equals("jefferson")) {
                        pds.add(pd);
                    }
                }
                adapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    private void showProductDialog(final String id, String type, Product prod) {

        AlertDialog.Builder dialobBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.update_dialog,null);

        dialobBuilder.setView(dialogView);

        final Button btnUpdate = (Button) dialogView.findViewById(R.id.btnUpdate);
        final Button btnDelete = (Button) dialogView.findViewById(R.id.btnDelete);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct(id);

            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), UpdateProductActivity.class);
                startActivity(intent);
                finish();

            }
        });

        dialobBuilder.setTitle("Produto "+ type);

        AlertDialog alertDialog = dialobBuilder.create();
        alertDialog.show();
    }

    private void deleteProduct(String productID) {
        DatabaseReference drProduct = FirebaseDatabase.getInstance().getReference("Product").child(productID);
        drProduct.removeValue();

        Toast.makeText(this, "Produto deletado", Toast.LENGTH_LONG).show();
        Del();
    }
    private void Del(){
        Intent intent = new Intent(PdActivity.this, PdActivity.class);
        startActivity(intent);
        finish();
    }


    private void RegisterProduct(){
        Intent intent = new Intent(PdActivity.this, ProductActivity.class);
        startActivity(intent);
        finish();
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
