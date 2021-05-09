package com.tech.elibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tech.elibrary.modles.Books;
import com.valdesekamdem.library.mdtoast.MDToast;

public class AddBooks extends AppCompatActivity {

    EditText editText1;
    EditText editText2;
    EditText editText3;
    EditText editText4;
    EditText editText5;
    EditText editText6;
    EditText editText7;
    Button button;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_books);

        editText1=(EditText)findViewById(R.id.bookid);
        editText2=(EditText)findViewById(R.id.bookName);
        editText3=(EditText)findViewById(R.id.bookAuthor);
        editText4=(EditText)findViewById(R.id.bookprice);
        editText5=(EditText)findViewById(R.id.bookpages);
        editText6=(EditText)findViewById(R.id.bookgenere);
        editText7=(EditText)findViewById(R.id.booktypee);
        button=(Button)findViewById(R.id.bookadd);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reference = FirebaseDatabase.getInstance().getReference("Books");

                final String id = editText1.getText().toString();
                final String name = editText2.getText().toString();
                final String author = editText3.getText().toString();
                final String price = editText4.getText().toString();
                final String pages = editText5.getText().toString();
                final String genere = editText6.getText().toString();
                final String type = editText7.getText().toString();

                if (id.isEmpty()) {
                    editText1.setError("ID is required");
                } else if (name.isEmpty()) {
                    editText2.setError("Name is required");
                }  else if (author.isEmpty()) {
                    editText2.setError("Author is required");
                } else if (price.isEmpty()) {
                    editText2.setError("Price is required");
                } else if (pages.isEmpty()) {
                    editText2.setError("Pages is required");
                } else if (genere.isEmpty()) {
                    editText2.setError("Genere is required");
                }else if (type.isEmpty()) {
                    editText2.setError("Book Type is required");
                }else {

                    Books books = new Books(id,name,author,price,pages,genere,type);
                    reference.child(id).setValue(books);

                    MDToast mdToast = MDToast.makeText(AddBooks.this, "Task added successfully", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);
                    mdToast.show();

                }
            }
        });

    }
}