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

public class CreateClassActivity extends AppCompatActivity {

    private EditText eClassName;
    private EditText eClassCode;
    private MaterialButton bCreateClass;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class);

        eClassName = (EditText) findViewById(R.id.create_class_name);
        eClassCode = (EditText) findViewById(R.id.class_code);
        bCreateClass = (MaterialButton) findViewById(R.id.createClassButton);
        mAuth = FirebaseAuth.getInstance();

        DAOClass dao = new DAOClass();

//




        bCreateClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firebaseUser = mAuth.getCurrentUser();
                String className =eClassName.getText().toString();
                String classCode =eClassCode.getText().toString();
                String userName =firebaseUser.getEmail();

                Classroom c = new Classroom(className,classCode,userName);
                dao.add(c).addOnSuccessListener(suc-> {
                    Toast.makeText(CreateClassActivity.this,className+" "+" "+classCode+" "+" "+userName , Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(er->{
                    Toast.makeText(CreateClassActivity.this, ""+er.getMessage(), Toast.LENGTH_SHORT).show();
                });


            }
        });
    }
}