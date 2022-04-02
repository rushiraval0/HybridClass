package com.example.hybridclass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button signIn;
    private ImageButton regBtn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email=(EditText) findViewById(R.id.etEmail);
        password=(EditText) findViewById(R.id.etPassword);
        signIn=(Button) findViewById(R.id.btLogin);
        regBtn=(ImageButton) findViewById(R.id.btRegister);
        mAuth= FirebaseAuth.getInstance();

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn();
            }
        });

    }

    private void logIn(){
        String _email=email.getText().toString().trim();
        String _password=password.getText().toString().trim();

        if(_email.isEmpty())
        {
            email.setError("Email is required");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(_email).matches()) {
            email.setError("Email is invalid");
            email.requestFocus();
            return;
        }
        if(_password.isEmpty())
        {
            password.setError("Password is required");
            password.requestFocus();
            return;
        }
        if(_password.length()<6)
        {
            password.setError("Password < 6");
            password.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(_email,_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(LoginActivity.this,"Successfully Logged In",Toast.LENGTH_LONG).show();


                        FirebaseUser user = mAuth.getCurrentUser();
                        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(user.getUid());

                        mDatabaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String role = " ";
                                role = snapshot.child("role").getValue().toString();

                                if(role.equalsIgnoreCase("teacher"))
                                {
                                    startActivity(new Intent(LoginActivity.this,DashboardActivity.class));
                                }
                                else
                                {
                                    startActivity(new Intent(LoginActivity.this,DashboardActivityUser.class));
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });



                }
                else
                {
                    Toast.makeText(LoginActivity.this,"Error Logging In",Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this,"Error "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}