package com.example.hhs.hotelapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by hhs on 25/2/17.
 */

public class SignUp extends AppCompatActivity
{
    private String Base_url = "https://hotelapp-170a8.firebaseio.com/";
    private Firebase fb_db;

    TextView signup;
    EditText username,password,address, password2,mob;
    ImageView imageView;
    String uname,uaddr,mobnum,upass,upass2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        signup = (TextView) findViewById(R.id.signup);
        username = (EditText) findViewById(R.id.username);
        address = (EditText) findViewById(R.id.address);
        password = (EditText) findViewById(R.id.password);
        password2 = (EditText) findViewById(R.id.password2);
        mob = (EditText) findViewById(R.id.mob);
        imageView = (ImageView) findViewById(R.id.imageView);
        Firebase.setAndroidContext(this);
        fb_db = new Firebase(Base_url);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Register");

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uname = username.getText().toString();
                uaddr = address.getText().toString();
                mobnum = mob.getText().toString();
                upass = password.getText().toString();
                upass2 = password2.getText().toString();



                if(upass.equals(upass2))
                {
//                    Intent i = new Intent(SignUp.this,Login.class);
//                    startActivity(i);
                    new MyTask().execute();
                }else
                {
                    Toast.makeText(getApplicationContext(),"Invalid Passwords",Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



    private class MyTask extends AsyncTask<String, Integer, String> {

        // Runs in UI before background thread is called
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = ProgressDialog.show(Sign_In.this, "Message", "Logging in...");

        }

        // This is run in a background thread
        @Override
        protected String doInBackground(String... params) {
            // get the string from params, which is an array

          CredsUpdate credsUpdate = new CredsUpdate();
            credsUpdate.setUname(uname);
            credsUpdate.setUaddr(uaddr);
            credsUpdate.setMobnum(mobnum);
            credsUpdate.setUpass(upass);

            fb_db.child("Users").child(uname).setValue(credsUpdate);

            Intent i = new Intent(SignUp.this,Login.class);
            startActivity(i);

            return "SUCCESS";


        }




        // This runs in UI when background thread finishes
        @Override
        protected void onPostExecute(String result)
        {


        }
    }
}

