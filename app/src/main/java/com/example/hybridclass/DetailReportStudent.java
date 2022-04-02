package com.example.hybridclass;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailReportStudent extends AppCompatActivity {
    private FirebaseAuth auth;
    CircleImageView circleImageView;
    TextView textView1;
    TextView textView5;
    TextInputEditText textInputEditText;
//    TextInputLayout textInputLayout;
    CardView cardView;
    private FirebaseDatabase database;
    private FirebaseUser users;
    private DatabaseReference myRef;
    String temp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_report_student);
        auth= FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        circleImageView = findViewById(R.id.USERIMAGE);
        textView1 = findViewById(R.id.StudentEmailGen);
        textInputEditText = findViewById(R.id.reviewText);
//        textInputLayout = findViewById(R.id.outlinedTextField);
        cardView = findViewById(R.id.subReview);
        textView5 = findViewById(R.id.this_Sections);

        Bundle bundle = getIntent().getExtras();
        String testName = bundle.getString("test");
        System.out.println("tag   "+bundle.getString("test"));


        String e=auth.getCurrentUser().getEmail();
        int x=e.indexOf("@");
        temp=" ";
        String user=e.substring(0,x);
        myRef=database.getReference().child("Results").child(testName).child(user);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("tag  "+snapshot.getValue());
                temp = "Your score is :  "+snapshot.getValue();
                textView5.setText(temp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        myRef=database.getReference().child("Review").child(testName).child(user);
        System.out.println("tag :: "+myRef);

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                System.out.println("tag  value :: "+snapshot.getValue() + "  key "+snapshot.getKey());
                if(snapshot.exists() && snapshot.getKey().equalsIgnoreCase("Review") )
                {
                    textInputEditText.setText(snapshot.getValue().toString());
                }
                if(snapshot.exists() && snapshot.getKey().equalsIgnoreCase("Reviewed By") )
                {
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        textView1.setText(user);

    }
}