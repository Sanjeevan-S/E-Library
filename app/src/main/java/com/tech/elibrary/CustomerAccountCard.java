package com.tech.elibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.tech.elibrary.modles.AccCard;
import com.tech.elibrary.modles.Books;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerAccountCard extends AppCompatActivity {

    Button button;
    ListView listView;
    private List<AccCard> user;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_account_card);

        button = (Button)findViewById(R.id.account);
        listView = (ListView)findViewById(R.id.Cardlistview);

        user = new ArrayList<>();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerAccountCard.this, AddCard.class);
                startActivity(intent);
            }
        });

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userid = firebaseUser.getUid();

        ref = FirebaseDatabase.getInstance().getReference("AccCard").child(userid).getRef();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user.clear();

                for (DataSnapshot taskDatasnap : dataSnapshot.getChildren()){

                    AccCard accCard = taskDatasnap.getValue(AccCard.class);
                    user.add(accCard);
                }

                MyAdapter adapter = new MyAdapter(CustomerAccountCard.this, R.layout.custom_acc_list, (ArrayList<AccCard>) user);
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
        ImageButton imageButton1;
        ImageButton imageButton2;

    }

    class MyAdapter extends ArrayAdapter<AccCard> {
        LayoutInflater inflater;
        Context myContext;
        List<Map<String, String>> newList;
        List<AccCard> user;


        public MyAdapter(Context context, int resource, ArrayList<AccCard> objects) {
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
            final ManageBooks.ViewHolder holder;
            if (view == null) {
                holder = new ManageBooks.ViewHolder();
                view = inflater.inflate(R.layout.custom_acc_list, null);

                holder.COL1 = (TextView) view.findViewById(R.id.actype);
                holder.COL2 = (TextView) view.findViewById(R.id.acname);
                holder.COL3 = (TextView) view.findViewById(R.id.acnumber);
                holder.imageButton1=(ImageButton)view.findViewById(R.id.accomplete);
                holder.imageButton2=(ImageButton)view.findViewById(R.id.acedit);

                view.setTag(holder);
            } else {

                holder = (ManageBooks.ViewHolder) view.getTag();
            }

            holder.COL1.setText(user.get(position).getType());
            holder.COL2.setText(user.get(position).getName());
            holder.COL3.setText(user.get(position).getNumber());

            System.out.println(holder);

            final String idd = user.get(position).getNumber();

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

                                    FirebaseDatabase.getInstance().getReference("AccCard").child(userid).child(idd).removeValue();
                                    //remove function not written
                                    MDToast mdToast = MDToast.makeText(CustomerAccountCard.this, "Delete completed", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);
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
                    View view1 = inflater.inflate(R.layout.custom_update_accard_details,null);
                    dialogBuilder.setView(view1);

                    final EditText editText1 = (EditText)view1.findViewById(R.id.ccardType);
                    final EditText editText2 = (EditText)view1.findViewById(R.id.cOwnername);
                    final EditText editText3 = (EditText)view1.findViewById(R.id.cnumber);
                    final EditText editText4 = (EditText)view1.findViewById(R.id.cexpire);
                    final EditText editText5 = (EditText)view1.findViewById(R.id.ccvs);
                    final Button button = (Button)view1.findViewById(R.id.caddcard);

                    final AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String userid = user.getUid();

                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("AccCard").child(userid).child(idd);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String type = snapshot.child("type").getValue().toString();
                            String name = snapshot.child("name").getValue().toString();
                            String number = snapshot.child("number").getValue().toString();
                            String expire = snapshot.child("expire").getValue().toString();
                            System.out.println("hhhhhhhhhhhhhh"+expire);
                            String csv = snapshot.child("cvs").getValue().toString();

                            editText1.setText(type);
                            editText2.setText(name);
                            editText3.setText(number);
                            editText4.setText(expire);
                            editText5.setText(csv);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String type = editText1.getText().toString();
                            String name = editText2.getText().toString();
                            String number =editText3.getText().toString();
                            String expire = editText4.getText().toString();
                            String csv = editText5.getText().toString();


                            if (type.isEmpty()) {
                                editText1.setError("Card type is required");
                            } else if (name.isEmpty()) {
                                editText2.setError("Name is required");
                            }  else if (number.isEmpty()) {
                                editText3.setError("Card number is required");
                            } else if (expire.isEmpty()) {
                                editText4.setError("Expire date is required");
                            } else if (csv.isEmpty()) {
                                editText5.setError("CSV is required");
                            }else {
//
                                HashMap map = new HashMap();
                                map.put("type",type);
                                map.put("name",name);
                                map.put("number",number);
                                map.put("expire",expire);
                                map.put("cvs",csv);
                                reference.updateChildren(map);

                                MDToast mdToast = MDToast.makeText(CustomerAccountCard.this, "Card Updated Successfully", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);
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
