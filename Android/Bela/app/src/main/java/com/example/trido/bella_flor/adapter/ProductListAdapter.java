package com.example.trido.bella_flor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.trido.bella_flor.R;
import com.example.trido.bella_flor.entities.Client;
import com.example.trido.bella_flor.entities.Product;

import java.util.ArrayList;

import static com.example.trido.bella_flor.LoginActivity.LOGIN;

public class ProductListAdapter extends ArrayAdapter<Product> {

    private ArrayList<Product> pd;
    private Context context;

    public ProductListAdapter(Context c, ArrayList<Product> objects) {
        super(c, 0, objects );
        this.context = c;
        this.pd = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

         if (pd != null ){

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.recyclerview_item_product, parent, false);

            TextView txtViewType = (TextView) view.findViewById(R.id.textViewType);
            TextView txtViewWeight = (TextView) view.findViewById(R.id.textViewWeight);
            TextView txtViewPrice = (TextView) view.findViewById(R.id.textViewPrice);

            Product pd2 = pd.get(position);

            txtViewType.setText(pd2.getType());
            txtViewWeight.setText("Peso: "+pd2.getWeight());
            txtViewPrice.setText("R$: "+pd2.getPrice());

        }else{
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.empty, parent, false);
        }
        return view;
    }
}