package com.example.inclass09;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends ArrayAdapter<Contact> {
    ArrayList<Contact> allContacts;
    IListener4 mListener4;
    private static String TAG = "TAG";
    public interface IListener4{
        void callDetailsScreen(Contact contact);
        void customnotify(Contact contact, int position);
    }

    public ContactsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Contact> objects, IListener4 mListener4) {
        super(context, resource, objects);
        this.allContacts = objects;
        this.mListener4 = mListener4;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.contact_card_view,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else{
            holder = (ViewHolder) convertView.getTag();
        }
        final Contact contact = getItem(position);

        holder.name.setText(contact.name);
        holder.delete.setOnClickListener (new View.OnClickListener () {; @Override public void onClick (View v) {
            allContacts.remove (position);
            mListener4.customnotify(contact,position);

        } });
        holder.cardView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mListener4.callDetailsScreen(contact);
            }
        });
        return convertView;
}

    @Override
    public int getCount() {
        return allContacts.size();
    }

    public static class ViewHolder {
        TextView name;
        Button delete;
        CardView cardView;

        ViewHolder(View view){
            name = view.findViewById(R.id.nameID);
            delete = view.findViewById(R.id.delete);
            cardView = view.findViewById(R.id.cardViewID);
        }
    }

}
