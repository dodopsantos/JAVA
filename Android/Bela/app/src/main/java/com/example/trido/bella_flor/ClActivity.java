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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trido.bella_flor.adapter.ClientListAdapter;
import com.example.trido.bella_flor.dao.configFirebase;
import com.example.trido.bella_flor.entities.Client;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.trido.bella_flor.LoginActivity.LOGIN;

public class ClActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Client> cls;
    private DatabaseReference fireBase;
    private ValueEventListener valueEventListener;
    private FloatingActionButton btnFloat;

    public static Client FLAG = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cl);

        cls = new ArrayList<>();
        listView  = (ListView) findViewById(R.id.listItens);
        adapter = new ClientListAdapter(this, cls);
        listView.setAdapter(adapter);
        fireBase = configFirebase.getfireBase().child("Client");

        btnFloat = (FloatingActionButton) findViewById(R.id.btnFloat);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView < ? > adapter, View view,int position, long arg){

                    Client client = cls.get(position);

                    FLAG = cls.get(position);
                    showUpdateDialog(client.getId(), client.getName(), FLAG);

                }
            }
        );

        btnFloat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Register();
            }
        });

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cls.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()){

                    Client cl = data.getValue(Client.class);
                    if (cl.getSalesman().equals(LOGIN.getLogin()) || LOGIN.getLogin().equals("jefferson")) {
                        cls.add(cl);
                    }
                }
                adapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }



    private void showUpdateDialog(final String clientID, String clientName, final Client array){
        AlertDialog.Builder dialobBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.update_dialog,null);

        dialobBuilder.setView(dialogView);

        final Button btnUpdate = (Button) dialogView.findViewById(R.id.btnUpdate);
        final Button btnDelete = (Button) dialogView.findViewById(R.id.btnDelete);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteClient(clientID);

            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), UpdateClientActivity.class);
                startActivity(intent);
                finish();

            }
        });

        dialobBuilder.setTitle("Cliente "+ clientName);

        AlertDialog alertDialog = dialobBuilder.create();
        alertDialog.show();
    }

    private void deleteClient(String clientID) {
        DatabaseReference drClient = FirebaseDatabase.getInstance().getReference("Client").child(clientID);
        drClient.removeValue();

        Toast.makeText(this, "Cliente deletado", Toast.LENGTH_LONG).show();
        Del();
    }
    private void Del(){
        Intent intent = new Intent(ClActivity.this, ClActivity.class);
        startActivity(intent);
        finish();
    }
    private void Register(){
        Intent intent = new Intent(ClActivity.this, ClientActivity.class);
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
