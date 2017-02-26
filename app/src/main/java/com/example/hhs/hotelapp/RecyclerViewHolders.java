package com.example.hhs.hotelapp;

/**
 * Created by hhs on 25/2/17.
 */

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView hotel_name;
    public ImageView hotel_photo;
    public RecyclerViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        hotel_name = (TextView)itemView.findViewById(R.id.hotel_name);
        hotel_photo = (ImageView)itemView.findViewById(R.id.hotel_photo);
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(view.getContext(), "Clicked Hotel Position = " + getPosition(), Toast.LENGTH_SHORT).show();
        Intent i = new Intent(view.getContext(),Hotel_indi.class);
        view.getContext().startActivity(i);
    }
}
