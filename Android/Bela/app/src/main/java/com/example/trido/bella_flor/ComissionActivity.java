package com.example.trido.bella_flor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.trido.bella_flor.adapter.ComissionListAdapter;
import com.example.trido.bella_flor.dao.configFirebase;
import com.example.trido.bella_flor.entities.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.trido.bella_flor.LoginActivity.LOGIN;

public class ComissionActivity extends AppCompatActivity {

    private ArrayList<Order> or;
    private DatabaseReference fireBase;
    private ValueEventListener valueEventListener;
    private TextView txtNameComission, txtValueComission, txtMonthComission, txtOldMonthComission;
    private Double oldMonth = 0.0, month = 0.0, flag = 0.0;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comission);

        or = new ArrayList<>();
        time = getCurrentTimeStamp();
        fireBase = configFirebase.getfireBase().child("Order");
        txtNameComission = (TextView) findViewById(R.id.txtNameComission);
        txtValueComission = (TextView) findViewById(R.id.txtValueComission);
        txtMonthComission = (TextView) findViewById(R.id.txtMonthComission);
        txtOldMonthComission = (TextView) findViewById(R.id.txtOldMonthComission);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                or.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()){
                    Order cl = data.getValue(Order.class);
                        if (cl.getSalesman().equals(LOGIN.getLogin()) || LOGIN.getLogin().equals("jefferson")){
                            or.add(cl);
                        }
                }

                for (int i = 0; i < or.size(); i++){
                    String array[] = new String[2];
                    array = or.get(i).getTime().split("-");

                    int timeInt = Integer.parseInt(time);
                    int arrayInt = Integer.parseInt(array[1]);

                    if (or.get(i).getCommission() == null){

                    }else
                    if (time.equals(array[1])){
                        if (or.get(i).getStatus().equals("Finalizado")){
                            System.out.println("MONTH: "+month+"OR: "+or.get(i).getCommission());
                            flag = Double.parseDouble(or.get(i).getCommission().replace(",","."));
                            System.out.println("PREÇO: "+flag);
                            month += flag;
                        }
                    }else
                    if (timeInt - arrayInt == 1){
                        if (or.get(i).getStatus().equals("Finalizado")) {
                            oldMonth += Double.parseDouble(or.get(i).getCommission());
                        }
                    }
                }

                txtNameComission.setText(LOGIN.getLogin());
                txtValueComission.setText("R$: "+month.toString());
                txtMonthComission.setText("Comissão do mês: "+time);
                txtOldMonthComission.setText("Mês anterior: R$ "+oldMonth.toString());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    public static String getCurrentTimeStamp(){
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
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
