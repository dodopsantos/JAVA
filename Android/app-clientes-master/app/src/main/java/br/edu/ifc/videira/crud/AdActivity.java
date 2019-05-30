package br.edu.ifc.videira.crud;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.edu.ifc.videira.crud.adapters.AdListAdapter;
import br.edu.ifc.videira.crud.dao.configFirebase;
import br.edu.ifc.videira.crud.entities.Ad;

public class AdActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Ad> ads;
    private DatabaseReference fireBase;
    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);

            ads = new ArrayList<>();
            listView  = (ListView) findViewById(R.id.listItens);
            adapter = new AdListAdapter(this, ads);
            listView.setAdapter(adapter);
            fireBase = configFirebase.getfireBase().child("Ad");

            valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ads.clear();

                    for (DataSnapshot data : dataSnapshot.getChildren()){

                        Ad ad = data.getValue(Ad.class);
                        ads.add(ad);
                    }
                    adapter.notifyDataSetChanged();

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
