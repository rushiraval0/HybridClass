package com.example.hybridclass;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class JoinClassActivity extends AppCompatActivity {

    private EditText eClassCode;
    private MaterialButton bJoinClass;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_class);
        eClassCode = (EditText) findViewById(R.id.class_code);
        bJoinClass = (MaterialButton) findViewById(R.id.joinClassButton);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();



        bJoinClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("User").child(user.getUid()).child("classList").push();
                mRef.setValue(eClassCode.getText().toString().trim());
                Toast.makeText(JoinClassActivity.this,"Successfully Joined Classroom" , Toast.LENGTH_SHORT).show();
            }
        });

    }
}