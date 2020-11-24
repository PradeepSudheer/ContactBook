package com.example.inclass09;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class ContactDetailsScreen extends Fragment implements View.OnClickListener {
    TextView name,email,phone,type;
    Button delete;
    private FirebaseAuth mAuth;
    Contact contact;
    private static String TAG = "TAG";
    public ContactDetailsScreen(Contact contact) {
        // Required empty public constructor
        this.contact = contact;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_details_screen, container, false);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        phone = view.findViewById(R.id.phone);
        type = view.findViewById(R.id.type);
        delete = view.findViewById(R.id.delete);

        name.setText(contact.name);
        email.setText(contact.email);
        phone.setText(contact.phone);
        type.setText(contact.type);

        delete.setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof IListener1){
            mListener1 = (IListener1)context;
        }else{
            throw new RuntimeException(context.toString()+"must implement IListener");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.delete:
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser user  = mAuth.getCurrentUser();
                DocumentReference docRef = db.collection("users").document(user.getUid());
                docRef.collection("contacts").document(contact.id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: "+"error on deletin");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: ");
                    }
                });
                mListener1.callMainScreen1();
                break;
        }
    }

    IListener1 mListener1;
    public interface IListener1{
        void callMainScreen1();
    }


}