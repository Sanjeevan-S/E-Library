package com.tech.elibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tech.elibrary.modles.Orders;
import com.tech.elibrary.modles.Users;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewOrders extends AppCompatActivity {

    ListView listView;
    List<Orders> user;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);

        listView=(ListView)findViewById(R.id.vieworders);
        user=new ArrayList<>();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userid = firebaseUser.getUid();

        ref = FirebaseDatabase.getInstance().getReference("Orders").child(userid).getRef();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user.clear();

                for (DataSnapshot taskDatasnap : dataSnapshot.getChildren()){

                    Orders orders = taskDatasnap.getValue(Orders.class);
                    user.add(orders);

                    System.out.println(orders);
                }

               MyAdapter adapter = new MyAdapter(ViewOrders.this, R.layout.custom_book_orderlist, (ArrayList<Orders>) user);
                listView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    static class ViewHolder {

        TextView COL1;
        TextView COL2;
        TextView COL3;
        TextView COL4;
        TextView COL5;
        ImageButton imageButton1;
        ImageButton imageButton2;

    }

    class MyAdapter extends ArrayAdapter<Orders> {
        LayoutInflater inflater;
        Context myContext;
        List<Map<String, String>> newList;
        List<Orders> user;


        public MyAdapter(Context context, int resource, ArrayList<Orders> objects) {
            super(context, resource, objects);
            myContext = context;
            user = objects;
            inflater = LayoutInflater.from(context);
            int y;
            String barcode;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            final CustomerOrders.ViewHolder holder;
            if (view == null) {
                holder = new CustomerOrders.ViewHolder();
                view = inflater.inflate(R.layout.custom_book_orderlist, null);

                holder.COL1 = (TextView) view.findViewById(R.id.boid);
                holder.COL2 = (TextView) view.findViewById(R.id.boname);
                holder.COL3 = (TextView) view.findViewById(R.id.boprice);
                holder.COL4 = (TextView) view.findViewById(R.id.boget);
                holder.COL5 = (TextView) view.findViewById(R.id.bogive);
                holder.imageButton1=(ImageButton)view.findViewById(R.id.bocomplete);
                holder.imageButton2=(ImageButton)view.findViewById(R.id.boedit);

                view.setTag(holder);
            } else {

                holder = (CustomerOrders.ViewHolder) view.getTag();
            }

            holder.COL1.setText(user.get(position).getId());
            holder.COL2.setText(user.get(position).getName());
            holder.COL3.setText(user.get(position).getPrice());
            holder.COL4.setText(user.get(position).getGetDate());
            holder.COL5.setText(user.get(position).getGiveDate());

            System.out.println(holder);

            return view;
        }

    }

}