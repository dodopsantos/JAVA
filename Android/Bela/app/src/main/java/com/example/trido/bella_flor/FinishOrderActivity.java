package com.example.trido.bella_flor;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.trido.bella_flor.adapter.OrderListAdapter;
import com.example.trido.bella_flor.dao.configFirebase;
import com.example.trido.bella_flor.entities.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.trido.bella_flor.LoginActivity.LOGIN;

public class FinishOrderActivity extends AppCompatActivity {
    private ListView listViewFinish;
    private ArrayAdapter adapter;
    private ArrayList<Order> fns;
    private DatabaseReference fireBase;
    private ValueEventListener valueEventListener;

    public static Order FINISH = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_order);

        fns = new ArrayList<>();
        listViewFinish = (ListView) findViewById(R.id.listItensFinish);
        adapter = new OrderListAdapter(this, fns);
        listViewFinish.setAdapter(adapter);
        fireBase = configFirebase.getfireBase().child("Order");

        listViewFinish.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Order order = fns.get(position);

                FINISH = fns.get(position);

                showOrderDelete(order.getId());

            }
        });


        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fns.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()){
                    Order fn = data.getValue(Order.class);
                    if (fn.getSalesman().equals(LOGIN.getLogin()) || LOGIN.getLogin().equals("jefferson")) {
                        if (fn.getStatus().equals("Finalizado")) {
                            fns.add(fn);
                        }
                    }

                }
                adapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    private void showOrderDelete(String id) {

        AlertDialog.Builder dialobBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.reactivate_order,null);

        dialobBuilder.setView(dialogView);

        final Button btnReactive = (Button) dialogView.findViewById(R.id.btnReactive);

        btnReactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FINISH.setStatus("Pendente");
                SaveOrder(FINISH);
                finish();
            }
        });
        dialobBuilder.setTitle("Deseja reativar o predido ?");

        AlertDialog alertDialog = dialobBuilder.create();
        alertDialog.show();
    }

    private boolean SaveOrder(Order finish) {
        try {

            fireBase = configFirebase.getfireBase().child("Order");
            fireBase.child(finish.getId()).setValue(finish);
            Toast.makeText(FinishOrderActivity.this,"Pedido Reativado!", Toast.LENGTH_LONG).show();

            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
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
