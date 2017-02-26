package com.example.hhs.hotelapp;

/**
 * Created by hhs on 25/2/17.
 */

import android.app.Dialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView
        .Adapter<MyRecyclerViewAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<DataObject> mDataset,fooddata;
    int quant;
    int imgind=0;
    public int[]imgs={R.drawable.food1,R.drawable.food2,R.drawable.food3,R.drawable.food4,R.drawable.food5};
    private static MyClickListener myClickListener;



    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView name, desc, price;
        ToggleButton order;
        ImageView imageView;

        public DataObjectHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            desc = (TextView) itemView.findViewById(R.id.desc);
            price = (TextView) itemView.findViewById(R.id.price);
            order = (ToggleButton) itemView.findViewById(R.id.order);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);

            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MyRecyclerViewAdapter(ArrayList<DataObject> myDataset) {
        fooddata=new ArrayList<>();
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_row, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        holder.name.setText(mDataset.get(position).getmText1());
        holder.desc.setText(mDataset.get(position).getmText2());
        holder.price.setText(mDataset.get(position).getmText3());
        holder.imageView.setImageResource(imgs[imgind]);
        imgind++;
        if(imgind==5)
        {
            imgind=0;
        }
        holder.order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!holder.order.isChecked())
                {
                    holder.order.toggle();
                    holder.order.toggle();
                }
                else
                {
                Toast.makeText(v.getContext(),"Clicked at : "+position,Toast.LENGTH_SHORT).show();
                    final Dialog num = new Dialog(v.getContext());
                num.setTitle("Select Quantity");
                num.setContentView(R.layout.quantity_dialog);


                NumberPicker numberpicker = (NumberPicker)num.findViewById(R.id.numberPicker);
                final TextView textview = (TextView)num.findViewById(R.id.textView);
                TextView ok = (TextView) num.findViewById(R.id.ok);


                numberpicker.setMinValue(1);

                numberpicker.setMaxValue(11);


                numberpicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {



                        textview.setText("Quantity : "+ newVal);
                        quant=newVal;
                    }
                });

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        //adapter
                       // fooddata.add(mDataset.get(position));
                        int cost=mDataset.get(position).getmText4()*quant;
                        fooddata.add(new DataObject(mDataset.get(position).getmText1()+" : "+quant,mDataset.get(position).getmText2(),String.valueOf(cost)));
                        Dist.foods=new ArrayList<DataObject>(fooddata);
                        Dist.amount=Dist.amount+cost;
                        num.dismiss();
                    }
                });


                num.show();

            }
            }
        });





    }



    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}