package com.example.inclass09;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginScreen extends Fragment implements View.OnClickListener {
    private static String TAG = "TAG";

    EditText email,password;
    Button register,login;

    private FirebaseAuth mAuth;

    public LoginScreen() {
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
        View view = inflater.inflate(R.layout.fragment_login_screen, container, false);
        email = view.findViewById(R.id.editTextTextEmailAddress);
        password = view.findViewById(R.id.editTextTextPassword);
        register = view.findViewById(R.id.registerButton);
        login = view.findViewById(R.id.loginButton);
        register.setOnClickListener(this);
        login.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.registerButton:
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.containerView,new RegisterScreen()).commit();
                break;
            case R.id.loginButton:
                String emailString = email.getText().toString();
                String passwordString = password.getText().toString();
                mAuth = FirebaseAuth.getInstance();
                if(emailString.isEmpty()){
                    Toast.makeText(getContext(),"Please put in email",Toast.LENGTH_LONG).show();
                }else if(passwordString.isEmpty()){
                    Toast.makeText(getContext(),"Please enter password",Toast.LENGTH_LONG).show();
                }else{
                    mAuth.signInWithEmailAndPassword(emailString,passwordString)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Log.d(TAG, "onComplete: Logged in successfully");
                                        getActivity().getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.containerView,new ContactsListScreen()).commit();
                                    }else{

                                    }
                                }
                            });
                }
                break;
        }
    }
}