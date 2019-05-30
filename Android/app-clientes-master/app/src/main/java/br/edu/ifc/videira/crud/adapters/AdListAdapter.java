package br.edu.ifc.videira.crud.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import br.edu.ifc.videira.crud.R;
import br.edu.ifc.videira.crud.entities.Ad;

import static br.edu.ifc.videira.crud.R.*;

public class AdListAdapter extends ArrayAdapter<Ad> {

    private ArrayList<Ad> ad;
    private Context context;

    private StorageReference storageReference;
    private FirebaseStorage storage;


    public AdListAdapter(Context c, ArrayList<Ad> objects) {
        super(c,0, objects  );
        this.context = c;
        this.ad = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        if (ad != null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.recyclerview_item, parent, false);

            ImageView imageView = (ImageView) view.findViewById(id.imgView);
            TextView txtViewBrand = (TextView) view.findViewById(id.textViewBrand);
            TextView txtViewModel = (TextView) view.findViewById(id.textViewModel);
            TextView txtViewYear = (TextView) view.findViewById(id.textViewYear);
            TextView txtViewPrice = (TextView) view.findViewById(id.textViewPrice);
            TextView txtViewDescription = (TextView) view.findViewById(id.textViewDescription);
            TextView txtViewAdress = (TextView) view.findViewById(id.textViewAdress);
            TextView txtViewPhone = (TextView) view.findViewById(id.textViewPhone);
            TextView txtViewEmail = (TextView) view.findViewById(id.textViewEmail);

            Ad ad2 = ad.get(position);

            Picasso.get().load(ad2.getPhoto1()).into(imageView);
            txtViewBrand.setText(ad2.getBrand());
            txtViewModel.setText(ad2.getModel());
            txtViewYear.setText(ad2.getYear());
            txtViewPrice.setText("R$ "+ad2.getPrice());
            txtViewDescription.setText(ad2.getDescription());
            txtViewAdress.setText(ad2.getAdress());
            txtViewPhone.setText(ad2.getPhone());
            txtViewEmail.setText(ad2.getEmail());

        }
        return view;
    }
}
