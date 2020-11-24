package com.example.inclass09;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;

public class NewContactScreen extends Fragment implements View.OnClickListener {
    EditText name,email,phone,type;
    private static String TAG = "TAG";
    private FirebaseAuth mAuth;
    public NewContactScreen() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof NewContactScreen.IListener){
            mListener = (NewContactScreen.IListener)context;
        }else{
            throw new RuntimeException(context.toString()+"must implement IListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_contact_screen, container, false);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        phone = view.findViewById(R.id.phone);
        type = view.findViewById(R.id.type);
        view.findViewById(R.id.submit).setOnClickListener(this);
        view.findViewById(R.id.back).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit:
                String nameString = name.getText().toString();
                String emailString = email.getText().toString();
                String phoneString = phone.getText().toString();
                String typeString  = type.getText().toString();
                if(nameString.length()==0){
                    AlertUtils.showOKDialog(getActivity(),getResources().getString(R.string.error),
                            getResources().getString(R.string.name_hint));
                }else if(emailString.length()==0){
                    AlertUtils.showOKDialog(getActivity(),getResources().getString(R.string.error),
                            getResources().getString(R.string.email_hint));
                }else if(phoneString.length() ==0){
                    AlertUtils.showOKDialog(getActivity(),getResources().getString(R.string.error),
                            getResources().getString(R.string.phone_hint));
                }else if(typeString.length() ==0){
                    AlertUtils.showOKDialog(getActivity(),getResources().getString(R.string.error),
                            getResources().getString(R.string.phone_type_hint));
                }else{
                    mAuth = FirebaseAuth.getInstance();
                    final Contact contact = new Contact(nameString,emailString,phoneString,typeString);
                    HashMap<String, String> map = new HashMap<>();
                    map.put("name",contact.name);
                    map.put("email",contact.email);
                    map.put("phone",contact.phone);
                    map.put("type",contact.type);


                    FirebaseFirestore db  = FirebaseFirestore.getInstance();

                    DocumentReference docRef = db.collection("users").document(mAuth.getCurrentUser().getUid()).collection("contacts").document();
                    map.put("id", docRef.getId());

                    docRef.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: ");
                            mListener.callMainScreen();
                            Log.d(TAG, "onSuccess: ");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                    getActivity().getSupportFragmentManager().popBackStack();
                }


                break;
            case R.id.back:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
        }
    }
    IListener mListener;
    public interface IListener{
        void callMainScreen();
    }
}