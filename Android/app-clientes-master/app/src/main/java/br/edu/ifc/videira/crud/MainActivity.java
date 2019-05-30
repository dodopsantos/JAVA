package br.edu.ifc.videira.crud;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifc.videira.crud.adapters.AdListAdapter;
import br.edu.ifc.videira.crud.adapters.UserListAdapter;
import br.edu.ifc.videira.crud.dao.configFirebase;
import br.edu.ifc.videira.crud.entities.Ad;
import br.edu.ifc.videira.crud.entities.User;
import br.edu.ifc.videira.crud.fragment.PhotosMainFragment;
import br.edu.ifc.videira.crud.view_models.UserViewModel;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private List<User> usuarioList = new ArrayList<User>();
    private ArrayAdapter<User> arrayAdapterUser;
    private ListView listView;

    private UserViewModel mUserViewModel;
    public static final int NEW_USER_ACTIVITY_REQUEST_CODE = 1;
    public static final int LOGIN_ACTIVITY_REQUEST_CODE = 1;

    private PhotosMainFragment photosMainFragment;
    private android.support.v4.app.FragmentManager fragmentManager;
    private android.support.v4.app.FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //listView = (ListView) findViewById(R.id.listV_dados);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        //ShowDatabase();
        FragmentMainActivity();
    }

    private void ShowDatabase(){
        databaseReference.child("usuario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usuarioList.clear();
                for (DataSnapshot objSnapshot:dataSnapshot.getChildren()){
                    User u = objSnapshot.getValue(User.class);
                    usuarioList.add(u);
                }

                arrayAdapterUser = new ArrayAdapter<User>(MainActivity.this,
                        android.R.layout.simple_list_item_1,usuarioList);
                listView.setAdapter(arrayAdapterUser);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_send) {
            Intent i = new Intent(this, CarActivity.class);
            startActivityForResult(i, LOGIN_ACTIVITY_REQUEST_CODE);
        } else if (id == R.id.nav_user) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivityForResult(i, LOGIN_ACTIVITY_REQUEST_CODE);
        } else if (id == R.id.nav_new_user){
            Intent i = new Intent(this, AnnounceActivity.class);
            startActivityForResult(i, LOGIN_ACTIVITY_REQUEST_CODE);
        } else if (id == R.id.nav_bikes){
            Intent i = new Intent(this, BikeActivity.class);
            startActivityForResult(i, LOGIN_ACTIVITY_REQUEST_CODE);
        } else if (id == R.id.nav){
            Intent i = new Intent(this, AdActivity.class);
            startActivityForResult(i, LOGIN_ACTIVITY_REQUEST_CODE);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void FragmentMainActivity(){
        photosMainFragment = new PhotosMainFragment();

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.frameLayoutMain, photosMainFragment, "FragmentTelaIncial");

        fragmentTransaction.commit();
    }
}
