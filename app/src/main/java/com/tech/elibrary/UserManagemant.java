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
import com.tech.elibrary.modles.Users;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserManagemant extends AppCompatActivity {

    ListView listView;
    List<Users> user;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_managemant);

        listView=(ListView)findViewById(R.id.userlist);
        user=new ArrayList<>();

        ref = FirebaseDatabase.getInstance().getReference("User");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user.clear();

                for (DataSnapshot taskDatasnap : dataSnapshot.getChildren()){

                    Users users = taskDatasnap.getValue(Users.class);
                    user.add(users);
                }

                MyAdapter adapter = new MyAdapter(UserManagemant.this, R.layout.custom_userlist, (ArrayList<Users>) user);
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

    class MyAdapter extends ArrayAdapter<Users> {
        LayoutInflater inflater;
        Context myContext;
        List<Map<String, String>> newList;
        List<Users> user;


        public MyAdapter(Context context, int resource, ArrayList<Users> objects) {
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
                view = inflater.inflate(R.layout.custom_userlist, null);

                holder.COL1 = (TextView) view.findViewById(R.id.uname);
                holder.COL2 = (TextView) view.findViewById(R.id.uemail);
                holder.COL3 = (TextView) view.findViewById(R.id.ucontact);
                holder.imageButton1=(ImageButton)view.findViewById(R.id.ucomplete);
                holder.imageButton2=(ImageButton)view.findViewById(R.id.uedit);

                view.setTag(holder);
            } else {

                holder = (ManageBooks.ViewHolder) view.getTag();
            }

            holder.COL1.setText(user.get(position).getName());
            holder.COL2.setText(user.get(position).getEmail());
            holder.COL3.setText(user.get(position).getPhone());

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

                                    FirebaseDatabase.getInstance().getReference("User").child(idd).removeValue();
                                    //remove function not written
                                    MDToast mdToast = MDToast.makeText(UserManagemant.this, "Delete completed", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);
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
                    View view1 = inflater.inflate(R.layout.custom_updateuser,null);
                    dialogBuilder.setView(view1);

                    final EditText editText1 = (EditText)view1.findViewById(R.id.Usid);
                    final EditText editText2 = (EditText)view1.findViewById(R.id.Usname);
                    final EditText editText3 = (EditText)view1.findViewById(R.id.usRemail);
                    final EditText editText4 = (EditText)view1.findViewById(R.id.usAge);
                    final EditText editText5 = (EditText)view1.findViewById(R.id.uscontct);
                    final EditText editText6 = (EditText)view1.findViewById(R.id.uspassword);
                    final Button button = (Button)view1.findViewById(R.id.usregister);

                    final AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String userid = user.getUid();

                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String id = snapshot.child("id").getValue().toString();
                            String name = snapshot.child("name").getValue().toString();
                            String email = snapshot.child("email").getValue().toString();
                            String age = snapshot.child("age").getValue().toString();
                            String contact = snapshot.child("phone").getValue().toString();
                            String password = snapshot.child("password").getValue().toString();

                            editText1.setText(id);
                            editText2.setText(name);
                            editText3.setText(email);
                            editText4.setText(age);
                            editText5.setText(contact);
                            editText6.setText(password);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String id = editText1.getText().toString();
                            String name = editText2.getText().toString();
                            String email =editText3.getText().toString();
                            String age = editText4.getText().toString();
                            String contact = editText5.getText().toString();
                            String password = editText6.getText().toString();


                            if (id.isEmpty()) {
                                editText1.setError("ID is required");
                            } else if (name.isEmpty()) {
                                editText2.setError("Name is required");
                            }  else if (email.isEmpty()) {
                                editText3.setError("Email is required");
                            } else if (age.isEmpty()) {
                                editText4.setError("Age is required");
                            } else if (contact.isEmpty()) {
                                editText5.setError("Contact is required");
                            } else if (password.isEmpty()) {
                                editText6.setError("Password is required");
                            }else {
//
                                HashMap map = new HashMap();
                                map.put("id", id);
                                map.put("name",name);
                                map.put("email",email);
                                map.put("age",age);
                                map.put("phone",contact);
                                map.put("password",password);
                                reference.updateChildren(map);

                                MDToast mdToast = MDToast.makeText(UserManagemant.this, "User Updated Successfully", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);
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