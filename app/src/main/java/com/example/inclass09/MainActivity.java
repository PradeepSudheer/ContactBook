package com.example.inclass09;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NewContactScreen.IListener, ContactDetailsScreen.IListener1 {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() == null){
            getSupportFragmentManager().beginTransaction().add(R.id.containerView, new LoginScreen()).commit();
        }else{
            getSupportFragmentManager().beginTransaction().add(R.id.containerView, new ContactsListScreen()).commit();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void callMainScreen() {
        getSupportFragmentManager().beginTransaction().replace(R.id.containerView,new ContactsListScreen()).commit();
    }


    @Override
    public void callMainScreen1() {
        getSupportFragmentManager().beginTransaction().replace(R.id.containerView,new ContactsListScreen()).commit();
    }
}