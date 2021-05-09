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
import com.tech.elibrary.modles.Books;
import com.tech.elibrary.modles.Orders;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerOrders extends AppCompatActivity {

    ListView listView;
    private List<Orders> user;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_orders);

        listView = (ListView)findViewById(R.id.cusorderlist);

        user = new ArrayList<>();

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

                MyAdapter adapter = new MyAdapter(CustomerOrders.this, R.layout.custom_book_orderlist, (ArrayList<Orders>) user);
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
            final ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
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

                holder = (ViewHolder) view.getTag();
            }

            holder.COL1.setText(user.get(position).getId());
            holder.COL2.setText(user.get(position).getName());
            holder.COL3.setText(user.get(position).getPrice());
            holder.COL4.setText(user.get(position).getGetDate());
            holder.COL5.setText(user.get(position).getGiveDate());

            System.out.println(holder);

            final String idd = user.get(position).getId();

            holder.imageButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                            .setTitle("Do you want to delete this task?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    String userid = user.getUid();

                                    FirebaseDatabase.getInstance().getReference("Orders").child(userid).child(idd).removeValue();
                                    //remove function not written
                                    MDToast mdToast = MDToast.makeText(CustomerOrders.this, "Delete completed", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);
                                    mdToast.show();

                                }
                            })

                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            })
                            .show();
                }
            });

            holder.imageButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                    View view1 = inflater.inflate(R.layout.custom_update_orders,null);
                    dialogBuilder.setView(view1);

                    final TextView textView1 = (TextView) view1.findViewById(R.id.oobookname);
                    final TextView textView2 = (TextView)view1.findViewById(R.id.oobookauthor);
                    final TextView textView3 = (TextView)view1.findViewById(R.id.ooobbookprice);
                    final TextView textView4 = (TextView)view1.findViewById(R.id.oobookpages);
                    final TextView textView5 = (TextView)view1.findViewById(R.id.oobooktype);
                    final EditText editText6 = (EditText)view1.findViewById(R.id.ooderingDate);
                    final EditText editText7 = (EditText)view1.findViewById(R.id.oreplaceDate);
                    final Button button = (Button)view1.findViewById(R.id.oorderbook);

                    final AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String userid = user.getUid();


                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Orders").child(userid).child(idd);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String name = snapshot.child("name").getValue().toString();
                            String author = snapshot.child("author").getValue().toString();
                            String price = snapshot.child("price").getValue().toString();
                            String pages = snapshot.child("pages").getValue().toString();
                            String type = snapshot.child("booktype").getValue().toString();
                            String get = snapshot.child("getDate").getValue().toString();
                            String give = snapshot.child("giveDate").getValue().toString();

                            textView1.setText(name);
                            textView2.setText(author);
                            textView3.setText(price);
                            textView4.setText(pages);
                            textView5.setText(type);
                            editText6.setText(get);
                            editText7.setText(give);

                            MDToast mdToast = MDToast.makeText(CustomerOrders.this, "You can only change Ordering date and replacing dat", MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING);
                            mdToast.show();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String name = textView1.getText().toString();
                            String author = textView2.getText().toString();
                            String price =textView3.getText().toString();
                            String pages = textView4.getText().toString();
                            String type = textView5.getText().toString();
                            String get = editText6.getText().toString();
                            String give = editText7.getText().toString();


                            if (get.isEmpty()) {
                                editText6.setError("Ordering date is required");
                            }else if (give.isEmpty()) {
                                editText7.setError("Replacing date is required");
                            }else {
//
                                HashMap map = new HashMap();
                                map.put("name",name);
                                map.put("author",author);
                                map.put("price",price);
                                map.put("pages",pages);
                                map.put("booktype",type);
                                map.put("getDate",get);
                                map.put("giveDate", give);
                                reference.updateChildren(map);

                                MDToast mdToast = MDToast.makeText(CustomerOrders.this, "Order Updated Successfully", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);
                                mdToast.show();

                                alertDialog.dismiss();
                            }
                        }
                    });
                }
            });

            return view;
        }

    }

}