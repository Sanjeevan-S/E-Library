package com.tech.elibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.tech.elibrary.modles.Books;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageBooks extends AppCompatActivity {

    Button button;
    ListView listView;
    private List<Books> user;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_books);

        button = (Button)findViewById(R.id.button);
        listView = (ListView)findViewById(R.id.listview);

        user = new ArrayList<>();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageBooks.this, AddBooks.class);
                startActivity(intent);
            }
        });

        ref = FirebaseDatabase.getInstance().getReference("Books");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user.clear();

                for (DataSnapshot taskDatasnap : dataSnapshot.getChildren()){

                    Books books = taskDatasnap.getValue(Books.class);
                    user.add(books);
                }

                MyAdapter adapter = new MyAdapter(ManageBooks.this, R.layout.custom_booklist, (ArrayList<Books>) user);
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
        ImageButton imageButton1;
        ImageButton imageButton2;

    }

    class MyAdapter extends ArrayAdapter<Books> {
        LayoutInflater inflater;
        Context myContext;
        List<Map<String, String>> newList;
        List<Books> user;


        public MyAdapter(Context context, int resource, ArrayList<Books> objects) {
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
                view = inflater.inflate(R.layout.custom_booklist, null);

                holder.COL1 = (TextView) view.findViewById(R.id.bookidd);
                holder.COL2 = (TextView) view.findViewById(R.id.booknamee);
                holder.COL3 = (TextView) view.findViewById(R.id.bookauthorr);
                holder.COL4 = (TextView) view.findViewById(R.id.booktypee);
                holder.imageButton1=(ImageButton)view.findViewById(R.id.complete);
                holder.imageButton2=(ImageButton)view.findViewById(R.id.edit);

                view.setTag(holder);
            } else {

                holder = (ViewHolder) view.getTag();
            }

            holder.COL1.setText(user.get(position).getId());
            holder.COL2.setText(user.get(position).getName());
            holder.COL3.setText(user.get(position).getAuthor());
            holder.COL4.setText(user.get(position).getBooktype());

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

                                    FirebaseDatabase.getInstance().getReference("Books").child(idd).removeValue();
                                    System.out.println(FirebaseDatabase.getInstance().getReference("Books").child(userid).child(idd));
                                    //remove function not written
                                    MDToast mdToast = MDToast.makeText(ManageBooks.this, "Delete completed", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);
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
                    View view1 = inflater.inflate(R.layout.custom_updatebook,null);
                    dialogBuilder.setView(view1);

                    final EditText editText1 = (EditText)view1.findViewById(R.id.bbookid);
                    final EditText editText2 = (EditText)view1.findViewById(R.id.bbookName);
                    final EditText editText3 = (EditText)view1.findViewById(R.id.bbookAuthor);
                    final EditText editText4 = (EditText)view1.findViewById(R.id.bbookprice);
                    final EditText editText5 = (EditText)view1.findViewById(R.id.bbookpages);
                    final EditText editText6 = (EditText)view1.findViewById(R.id.bbookgenere);
                    final EditText editText7 = (EditText)view1.findViewById(R.id.bbooktypee);
                    final Button button = (Button)view1.findViewById(R.id.update);

                    final AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();

                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Books").child(idd);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String id = snapshot.child("id").getValue().toString();
                            String name = snapshot.child("name").getValue().toString();
                            String author = snapshot.child("author").getValue().toString();
                            String price = snapshot.child("price").getValue().toString();
                            String pages = snapshot.child("pages").getValue().toString();
                            String genere = snapshot.child("genere").getValue().toString();
                            String type = snapshot.child("booktype").getValue().toString();

                            editText1.setText(id);
                            editText2.setText(name);
                            editText3.setText(author);
                            editText4.setText(price);
                            editText5.setText(pages);
                            editText6.setText(genere);
                            editText7.setText(type);

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
                                String author =editText3.getText().toString();
                                String price = editText4.getText().toString();
                                String pages = editText5.getText().toString();
                                String genere = editText6.getText().toString();
                                String type = editText7.getText().toString();


                                if (id.isEmpty()) {
                                    editText1.setError("ID is required");
                                } else if (name.isEmpty()) {
                                    editText2.setError("Name is required");
                                }  else if (author.isEmpty()) {
                                    editText3.setError("Author is required");
                                } else if (price.isEmpty()) {
                                    editText4.setError("Price is required");
                                } else if (pages.isEmpty()) {
                                    editText5.setError("Pages is required");
                                } else if (genere.isEmpty()) {
                                    editText6.setError("Genere is required");
                                }else if (type.isEmpty()) {
                                    editText7.setError("Book Type is required");
                                }else {
//
                                    HashMap map = new HashMap();
                                    map.put("id", id);
                                    map.put("name",name);
                                    map.put("author",author);
                                    map.put("price",price);
                                    map.put("pages",pages);
                                    map.put("genere",genere);
                                    map.put("booktype",type);
                                    reference.updateChildren(map);

                                    MDToast mdToast = MDToast.makeText(ManageBooks.this, "Book Updated Successfully", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);
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