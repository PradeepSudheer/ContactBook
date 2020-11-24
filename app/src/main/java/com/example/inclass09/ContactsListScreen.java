package com.example.inclass09;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;


public class ContactsListScreen extends Fragment implements View.OnClickListener {
    private static String TAG = "TAG";
    Button addNewContact;
    ListView contactsListView;
    static ArrayList<Contact> contacts = new ArrayList<>();

     ContactsAdapter contactsAdapter;
    private   FirebaseAuth mAuth;

    public ContactsListScreen() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_contacts_list_screen, container, false);
        addNewContact = view.findViewById(R.id.addNewContact);
        addNewContact.setOnClickListener(this);
        view.findViewById(R.id.signout).setOnClickListener(this);
        contactsListView = view.findViewById(R.id.contactsListView);
        contactsAdapter = new ContactsAdapter(getContext(), R.layout.contact_card_view, contacts, new ContactsAdapter.IListener4() {
            @Override
            public void callDetailsScreen(Contact contact) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.containerView,new ContactDetailsScreen(contact)).addToBackStack(null).commit();
            }


            @Override
            public void customnotify(Contact contact,int position) {
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

                contactsAdapter.notifyDataSetChanged();
            }
        });
        contactsListView.setAdapter(contactsAdapter);
        getData();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addNewContact:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.containerView,new NewContactScreen()).addToBackStack(null).commit();
                break;
            case R.id.signout:
                FirebaseAuth.getInstance().signOut();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.containerView,new LoginScreen()).commit();
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public  void getData(){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        db.collection("users").document(mAuth.getCurrentUser().getUid()).collection("contacts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                contacts.clear();
                for(QueryDocumentSnapshot document: value){
                    contacts.add(document.toObject(Contact.class));
                }
                contactsAdapter.notifyDataSetChanged();
            }
        });




    }
}