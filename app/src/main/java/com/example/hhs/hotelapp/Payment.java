package com.example.hhs.hotelapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by hhs on 25/2/17.
 */

public class Payment extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    int numMessages=0;
    String noticontent="";
    NotificationManager mNotificationManager;
    private static String LOG_TAG = "CardViewActivity";
    TextView pay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
       // mAdapter = new MyRecyclerViewAdapter(getDataSet());
        mAdapter = new MyRecyclerViewAdapter(Dist.foods);
        mRecyclerView.setAdapter(mAdapter);
        for(int i=0;i<Dist.foods.size();i++)
        {
            noticontent=noticontent+Dist.foods.get(i).getmText1()+" - "+Dist.foods.get(i).getmText3()+"\n";
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pay = (TextView) findViewById(R.id.pay);
        pay.setText(pay.getText()+" : "+String.valueOf(Dist.amount));
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Payment.this);
                dialog.setCancelable(false);
                dialog.setTitle("Proceed to payment");
                dialog.setMessage("Are you sure you want to checkout?" );
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(Payment.this,"Thank you ! Payment Succesfull!",Toast.LENGTH_SHORT).show();
                        displayNotification();
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
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
        ArrayList results = new ArrayList<DataObject>();
        for (int index = 0; index < 4; index++) {
            DataObject obj = new DataObject("Food Name : " + index, "Description : " + index, "Price : " +index+"/-");
            results.add(index, obj);
        }
        return results;
    }
    protected void displayNotification() {
        Log.i("Start", "notification");

   /* Invoking the default notification service */


        Intent intent = new Intent(getApplicationContext(),Payment.class);

        PendingIntent pendingIntent = PendingIntent
                .getActivity(getApplicationContext(),
                        0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        android.app.Notification notification = new android.app.Notification.Builder(getApplicationContext())
                .setTicker("Food Order")
                .setContentTitle("Vibes")
                .setContentText("Your food order is complete!!")
                .setStyle(new Notification.BigTextStyle()
                        .bigText(noticontent))
                .setSmallIcon(R.drawable.food2)
                .setContentIntent(pendingIntent).getNotification();

        notification.flags = android.app.Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(5, notification);
    }
}
