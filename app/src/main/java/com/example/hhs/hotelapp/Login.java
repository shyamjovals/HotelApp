package com.example.hhs.hotelapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

/**
 * Created by hhs on 25/2/17.
 */

public class Login extends AppCompatActivity{

    String Base_url = "https://hotelapp-170a8.firebaseio.com/Users/";
    Firebase fb_db;
    TextView signup, login;
    EditText username,password;
    ImageView imageView;
     String uname,pass;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        signup = (TextView) findViewById(R.id.signup);
        login = (TextView) findViewById(R.id.login);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        imageView = (ImageView) findViewById(R.id.imageView);
        Firebase.setAndroidContext(this);
        fb_db = new Firebase(Base_url);

        getSupportActionBar().setTitle("Login");

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this,SignUp.class);
                startActivity(i);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uname = username.getText().toString();
                pass = password.getText().toString();


                new MyTask().execute();
//                Intent i = new Intent(Login.this, Home.class);
//                startActivity(i);
            }
        });
    }




    private class MyTask extends AsyncTask<String, Integer, String> {

        // Runs in UI before background thread is called
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = ProgressDialog.show(OrgSignin.this, "Message", "Logging in...");

        }

        // This is run in a background thread
        @Override
        protected String doInBackground(String... params) {
            // get the string from params, which is an array

            System.out.println("BGPROCESS login");
            fb_db.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                    {
                        CredsUpdate credsUpdate = postSnapshot.getValue(CredsUpdate.class);
                        String cuname = credsUpdate.getUname();
                        String cpass = credsUpdate.getUpass();

                        System.out.println("LOLOLOL : "+cuname);
                        System.out.println("LOLOLOL : "+cpass);

                        if((cuname.equals(uname))&&(cpass.equals(pass)))
                        {

                            Intent intent = new Intent(Login.this, Home.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
//                            Toast.makeText(OrgSignin.this,"Login Failed",Toast.LENGTH_SHORT).show();
                        }



                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    System.out.println("FIREBASE ERROR OCCOURED");
                }
            });

            return "SUCCESS";
        }



        // This runs in UI when background thread finishes
        @Override
        protected void onPostExecute(String result)
        {


//            progressDialog.dismiss();

        }
    }



}
