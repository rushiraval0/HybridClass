package com.example.hybridclass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DashboardActivity extends AppCompatActivity {

    ImageButton userLogout;
    TextView email;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    ImageView createClass;
    MaterialButton viewClassroom;
    ImageView uploadMaterial;
    ImageView uploadNotice;
    ImageView createQuiz;
    ImageView viewResult;
    boolean isAdmin = false;
    String role="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        uploadMaterial = findViewById(R.id.image_view22);
        viewClassroom = findViewById(R.id.todoB);
        userLogout = (ImageButton) findViewById(R.id.logOutB);
        email = (TextView) findViewById(R.id.textView2);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        createClass = (ImageView) findViewById(R.id.image_view1);
        uploadNotice = findViewById(R.id.image_view23);
        createQuiz = findViewById(R.id.image_view21);
        viewResult = findViewById(R.id.image_view);

        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(firebaseUser.getUid());

            mDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    role = snapshot.child("role").getValue().toString();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });




        viewResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardActivity.this,ViewResult.class);
                i.putExtra("ISAdmin",isAdmin);
                startActivity(i);
            }
        });

        createQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this,CreateQuiz.class));
            }
        });

        uploadNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this,UploadEnoticeActivity.class));
            }
        });

        uploadMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this,UploadMaterialActivity.class));
            }
        });

        viewClassroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this,DisplayMyClasses.class));
            }
        });

        createClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this,CreateClassActivity.class));
            }
        });

        if(role.equalsIgnoreCase("teacher"))
        {
            isAdmin=true;
        }

        email.setText(firebaseUser.getEmail());

        userLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(DashboardActivity.this,LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
    }
}