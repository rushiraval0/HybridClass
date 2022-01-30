package com.example.hybridclass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final EditText email = findViewById(R.id.etEmail);
        final EditText name = findViewById(R.id.etUsername);
        final EditText password = findViewById(R.id.etPassword);
        final EditText confirmPassword = findViewById(R.id.etRePassword);
        final RadioGroup role = findViewById(R.id.rg);
        mAuth = FirebaseAuth.getInstance();

        final Button btn = findViewById(R.id.btReg);
        DAOUser dao = new DAOUser();


        btn.setOnClickListener(view ->{
            int selectedId = role.getCheckedRadioButtonId();
            RadioButton radioButton = (RadioButton) findViewById(selectedId);


//            dao.add(user).addOnSuccessListener(suc-> {
//                Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show();
//            }).addOnFailureListener(er->{
//                Toast.makeText(this, ""+er.getMessage(), Toast.LENGTH_SHORT).show();
//            });

            String _email = email.getText().toString();
            String _password = password.getText().toString();
            System.out.println("Successful");

            mAuth.createUserWithEmailAndPassword(_email,_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        System.out.println("Successful");
                        User user = new User(email.getText().toString(),name.getText().toString(),password.getText().toString(),confirmPassword.getText().toString(),radioButton.getText().toString());

                        FirebaseDatabase.getInstance().getReference(User.class.getSimpleName())
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(RegisterActivity.this,"Successful",Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(RegisterActivity.this,"Failed.",Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                    else {
                            Toast.makeText(RegisterActivity.this,"Failed.",Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(RegisterActivity.this,"Failed."+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        } );



    }
}