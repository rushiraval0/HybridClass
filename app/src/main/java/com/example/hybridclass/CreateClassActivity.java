package com.example.hybridclass;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateClassActivity extends AppCompatActivity {

    private EditText eClassName;
    private EditText eClassCode;
    private EditText eClassDescription;
    private MaterialButton bCreateClass;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class);

        eClassName = (EditText) findViewById(R.id.create_class_name);
        eClassCode = (EditText) findViewById(R.id.class_code);
        eClassDescription = (EditText) findViewById(R.id.class_description);
        bCreateClass = (MaterialButton) findViewById(R.id.createClassButton);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(user.getUid());

        DAOClass dao = new DAOClass();




        bCreateClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firebaseUser = mAuth.getCurrentUser();
                String className =eClassName.getText().toString();
                String classCode =eClassCode.getText().toString();
                String classDescription =eClassDescription.getText().toString();
                String userName =firebaseUser.getEmail();

                Classroom c = new Classroom(className,classCode,userName,classDescription);
                dao.add(c).addOnSuccessListener(suc-> {
                    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("User").child(user.getUid()).child("classList").push();
                    mRef.setValue(classCode);
                    Toast.makeText(CreateClassActivity.this,"Successfully Created Classroom" , Toast.LENGTH_SHORT).show();

                    Toast.makeText(CreateClassActivity.this,"Class Created!" , Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(er->{
                    Toast.makeText(CreateClassActivity.this, ""+er.getMessage(), Toast.LENGTH_SHORT).show();

                });


            }
        });
    }
}