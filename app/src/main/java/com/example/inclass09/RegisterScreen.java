package com.example.inclass09;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.LogDescriptor;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterScreen extends Fragment implements View.OnClickListener {
    EditText email,password;
    private static String TAG = "TAG";
    private FirebaseAuth mAuth;

    public RegisterScreen() {
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
        View view = inflater.inflate(R.layout.fragment_register_screen, container, false);
        email = view.findViewById(R.id.editTextTextEmailAddress);
        password = view.findViewById(R.id.editTextTextPassword);
        view.findViewById(R.id.registerButton).setOnClickListener(this);
        view.findViewById(R.id.cancelButton).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancelButton:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                            .replace(R.id.containerView,new LoginScreen())
                                .commit();
                break;
            case R.id.registerButton:
                mAuth = FirebaseAuth.getInstance();

                String emailString = email.getText().toString();
                String passwordString = password.getText().toString();
                mAuth = FirebaseAuth.getInstance();
                if(emailString.isEmpty()){
                    Toast.makeText(getContext(),"Please put in email",Toast.LENGTH_LONG).show();
                }else if(passwordString.isEmpty()){
                    Toast.makeText(getContext(),"Please enter password",Toast.LENGTH_LONG).show();
                }else {
                    mAuth.createUserWithEmailAndPassword(emailString, passwordString)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "onComplete: registered in successfully");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        Map<String, Object> map = new HashMap<>();
                                        db.collection("users").document(user.getUid()).set(map);

                                        Log.d(TAG, "onComplete: " + user.getUid());

                                        getActivity().getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.containerView, new ContactsListScreen()).commit();
                                    } else {
                                        try {
                                            throw new Exception();
                                        } catch (Exception e) {
                                            AlertUtils.showOKDialog(getActivity(), getResources().getString(R.string.error),
                                                    getResources().getString(R.string.user_already_registered));
                                        }
                                    }
                                }
                            });
                }
                break;
        }
    }
}