package com.example.trido.bella_flor.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trido.bella_flor.ClActivity;
import com.example.trido.bella_flor.ClientActivity;
import com.example.trido.bella_flor.R;
import com.example.trido.bella_flor.entities.Client;

import java.util.ArrayList;

import static com.example.trido.bella_flor.LoginActivity.LOGIN;

public class ClientListAdapter extends ArrayAdapter<Client> {

    private ArrayList<Client> cl;
    private Context context;

    public ClientListAdapter(Context c, ArrayList<Client> objects) {
        super(c, 0, objects );
        this.context = c;
        this.cl = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        if (cl != null ){

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.recyclerview_item, parent, false);

            TextView txtViewName = (TextView) view.findViewById(R.id.textViewName);
            TextView txtViewPerson = (TextView) view.findViewById(R.id.textViewPerson);
            TextView txtViewCNPJ = (TextView) view.findViewById(R.id.textViewCNPJ);
            TextView txtViewIE = (TextView) view.findViewById(R.id.textViewIE);
            TextView txtViewAdress = (TextView) view.findViewById(R.id.textViewAdress);
            TextView txtViewCEP = (TextView) view.findViewById(R.id.textViewCEP);
            TextView txtViewTel = (TextView) view.findViewById(R.id.textViewTel);
            TextView txtViewEmail = (TextView) view.findViewById(R.id.textViewEmail);
            TextView txtViewBorhood = (TextView) view.findViewById(R.id.textViewnBorhood);

            Client cl2 = cl.get(position);

                txtViewName.setText(cl2.getName());
                txtViewPerson.setText("Pessoa " + cl2.getPerson());
                txtViewEmail.setText(cl2.getEmail());
                txtViewCNPJ.setText(cl2.getCnpj());
                txtViewIE.setText(cl2.getIe());
                txtViewAdress.setText(cl2.getUf() + ", " + cl2.getCountry() + ", " + cl2.getPublic_place());
                txtViewBorhood.setText(cl2.getNeighborhood()+ ", N: " + cl2.getNumber());
                txtViewCEP.setText(cl2.getCep());
                txtViewTel.setText("Tel: " + cl2.getTel() + " Cel:" + cl2.getCel());

        }else{
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.empty, parent, false);
        }
        return view;
    }
}
