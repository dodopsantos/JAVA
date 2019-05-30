package com.example.trido.bella_flor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.trido.bella_flor.R;
import com.example.trido.bella_flor.entities.Order;

import java.util.ArrayList;

import static com.example.trido.bella_flor.LoginActivity.LOGIN;

public class ComissionListAdapter extends ArrayAdapter<Order> {

    private ArrayList<Order> or;
    private Context context;
    double values = 0;

    public ComissionListAdapter(Context c, ArrayList<Order> objects) {
        super(c, 0, objects );
        this.context = c;
        this.or = objects;
    }
    private void Price(){
        for (int i = 0; i < or.size(); i++){
          values += Double.parseDouble(or.get(i).getCommission());
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;


            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.recyclerview_item_comission, parent, false);

            TextView txtComission = (TextView) view.findViewById(R.id.txtComission);

            Price();

            txtComission.setText("Comissão de "+LOGIN.getLogin()+": R$ "+values);

        return view;
    }
}
