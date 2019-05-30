/*
package br.edu.ifc.videira.crud;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ViewHolder extends RecyclerView.ViewHolder {

    View mView;

    public ViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setDetails(Context ctx, String adress, String description, String price, String image){

        TextView mAdress = mView.findViewById(R.id.textViewAdress);
        TextView mDescription = mView.findViewById(R.id.textViewDescription);
        TextView mPrice = mView.findViewById(R.id.textViewPrice);
        ImageView mImage = mView.findViewById(R.id.imgView);

        mAdress.setText(adress);
        mDescription.setText(description);
        mPrice.setText(price);
        Picasso.get().load(image).into(mImage);
    }
}
*/