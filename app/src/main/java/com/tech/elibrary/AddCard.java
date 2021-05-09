package com.tech.elibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tech.elibrary.modles.AccCard;
import com.tech.elibrary.modles.Books;
import com.valdesekamdem.library.mdtoast.MDToast;

public class AddCard extends AppCompatActivity {

    EditText editText1;
    EditText editText2;
    EditText editText3;
    EditText editText4;
    EditText editText5;
    Button button;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        editText1=(EditText)findViewById(R.id.cardType);
        editText2=(EditText)findViewById(R.id.Ownername);
        editText3=(EditText)findViewById(R.id.number);
        editText4=(EditText)findViewById(R.id.expire);
        editText5=(EditText)findViewById(R.id.cvs);
        button=(Button)findViewById(R.id.addcard);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reference = FirebaseDatabase.getInstance().getReference("AccCard");

                final String type = editText1.getText().toString();
                final String name = editText2.getText().toString();
                final String number = editText3.getText().toString();
                final String expire = editText4.getText().toString();
                final String csv = editText5.getText().toString();

                if (type.isEmpty()) {
                    editText1.setError("Card Type is required");
                } else if (name.isEmpty()) {
                    editText2.setError("Name is required");
                }  else if (number.isEmpty()) {
                    editText2.setError("Card Number is required");
                } else if (expire.isEmpty()) {
                    editText2.setError("Expire date is required");
                } else if (csv.isEmpty()) {
                    editText2.setError("CSV is required");
                } else {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String userid = user.getUid();
                    AccCard accCard = new AccCard(userid,type,name,number,expire,csv);
                    reference.child(userid).child(number).setValue(accCard);

                    MDToast mdToast = MDToast.makeText(AddCard.this, "Card added successfully", MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);
                    mdToast.show();

                }
            }
        });
    }
}