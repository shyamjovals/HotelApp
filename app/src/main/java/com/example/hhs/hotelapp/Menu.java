package com.example.hhs.hotelapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by hhs on 25/2/17.
 */

public class Menu extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "CardViewActivity";
    FloatingActionButton fab;


    public Menu() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.menu, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerViewAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter);

        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                dialog.setCancelable(false);
                dialog.setTitle("Proceed to payment");
                dialog.setMessage("Are you sure you want to checkout?" );
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {




                        Intent i = new Intent(view.getContext(),Payment.class);
                        startActivity(i);
                    }
                })
                        .setNegativeButton("No ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                final AlertDialog alert = dialog.create();
                alert.show();
            }
        });

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, " Clicked on Item " + position);
            }
        });
    }

    private ArrayList<DataObject> getDataSet() {

        ArrayList foods = new ArrayList<String>();
        ArrayList desc = new ArrayList<String>();
        ArrayList rate = new ArrayList<Integer>();
        ArrayList results = new ArrayList<DataObject>();

        foods.add("Lentil Soup");
        foods.add("Tomato Soup");
        foods.add("Coconut Soup");
        foods.add("Chicken Curry");
        foods.add("Chicken Biryani");
        foods.add("Fish Biryani");
        foods.add("Goat Biryani");
        foods.add("Alu Gobi");
        foods.add("Naan");
        foods.add("Onion Kulcha");
        foods.add("Gobi Paratha");
        foods.add("Kulfi");
        foods.add("Mango Pudding");
        foods.add("Mango Lassi  ");
        foods.add("Soda Pop");

        desc.add("Indian Style lentil Soup");
        desc.add("Indian Style Tomato Soup");
        desc.add("Indian Style Coconut Soup");
        desc.add("Lightly spiced boneless....");
        desc.add("Boneless chicken rice....");
        desc.add("Seasonal white fish....");
        desc.add("Goat in a lightly spiced ....");
        desc.add("Specially spiced potatoes,....");
        desc.add("Traditional Indian bread....");
        desc.add("White bread stuffed with....");
        desc.add("wheat bread with cauliflower...");
        desc.add("sweetened milk ice pista....");
        desc.add("Vanilla pudding with fresh....");
        desc.add("Cold sweetened yogurt drink");
        desc.add("Sprite, Lemonade,Fanta....");

        rate.add(266);
        rate.add(234);
        rate.add(216);
        rate.add(733);
        rate.add(999);
        rate.add(1132);
        rate.add(455);
        rate.add(178);
        rate.add(487);
        rate.add(244);
        rate.add(555);
        rate.add(143);
        rate.add(275);
        rate.add(75);
        rate.add(75);









        for (int index = 0; index < 15; index++) {
            DataObject obj = new DataObject(foods.get(index).toString(), desc.get(index).toString(), rate.get(index).toString());
            results.add(index, obj);
        }
        return results;
    }
}
