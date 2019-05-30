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

public class OrderListAdapter extends ArrayAdapter<Order> {
    private ArrayList<Order> or;
    private Context context;

    public OrderListAdapter(Context c, ArrayList<Order> objects) {
        super(c, 0, objects );
        this.context = c;
        this.or = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        if (or != null ){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.recyclerview_item_order, parent, false);

            TextView txtViewName = (TextView) view.findViewById(R.id.txtViewName);
            TextView txtViewPrice = (TextView) view.findViewById(R.id.txtViewPrice);
            TextView txtViewCNPJ = (TextView) view.findViewById(R.id.txtViewCNPJ);
            TextView txtViewIE = (TextView) view.findViewById(R.id.txtViewIE);
            TextView txtViewAdress = (TextView) view.findViewById(R.id.txtViewAdress);
            TextView txtViewpPlace = (TextView) view.findViewById(R.id.txtViewpPlace);
            TextView txtViewCEP = (TextView) view.findViewById(R.id.txtViewCEP);
            TextView txtViewProduct = (TextView) view.findViewById(R.id.txtViewpProduct);
            TextView txtViewTime = (TextView) view.findViewById(R.id.txtViewpTime);
            TextView txtVewComission = (TextView) view.findViewById(R.id.txtViewComission);
            TextView txtVewStatus = (TextView) view.findViewById(R.id.txtViewStatus);
            TextView txtviewInvoice = (TextView) view.findViewById(R.id.txtViewInvoice);
            TextView txtviewPayForm = (TextView) view.findViewById(R.id.txtViewPayForm);
            TextView txtviewPayType = (TextView) view.findViewById(R.id.txtViewPayType);
            TextView txtviewDescription = (TextView) view.findViewById(R.id.txtViewDescription);

            Order or2 = or.get(position);

            txtViewName.setText(or2.getName());
            txtViewPrice.setText("R$ "+or2.getPrice());
            txtVewStatus.setText(or2.getStatus());
            txtViewCNPJ.setText(or2.getCnpj());
            txtViewIE.setText(or2.getIe());
            txtViewAdress.setText(or2.getUf()+", "+or2.getCountry()+ ", N: "+or2.getNumber());
            txtViewpPlace.setText(or2.getPublic_place());
            txtViewCEP.setText(or2.getCep());
            if (or2.getType2() == null){
                txtViewProduct.setText(or2.getType()+" - "+or2.getWeight()+" - Qtd.: "+or2.getSize());
            }else{
                txtViewProduct.setText(or2.getType()+" - "+or2.getWeight()+" - Qtd.: "+or2.getSize()+" / "+or2.getType2()+" - "+or2.getWeight2()+ " - Qts.:"+or2.getSize2());
            }
            txtVewComission.setText("Comissão: R$ "+or2.getCommission());
            txtViewTime.setText("Data: "+or2.getTime());
            txtviewInvoice.setText("Nota fiscal: "+or2.getInvoice());
            txtviewPayType.setText("Forma de pagamento: "+or2.getPaymentType());
            txtviewPayForm.setText("Prazo: "+or2.getPaymentForm()+" - "+or2.getPaymentLong());
            txtviewDescription.setText("Observações: "+or2.getDescription());

        }else{
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.empty, parent, false);
        }
        return view;
    }
}
