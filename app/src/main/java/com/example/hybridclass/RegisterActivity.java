package com.example.hybridclass;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final EditText email = findViewById(R.id.etEmail);
        final EditText name = findViewById(R.id.etUsername);
        final EditText password = findViewById(R.id.etPassword);
        final EditText confirmPassword = findViewById(R.id.etRePassword);
        final RadioGroup role = findViewById(R.id.rg);

        final Button btn = findViewById(R.id.btReg);
        DAOUser dao = new DAOUser();


        btn.setOnClickListener(view ->{
            int selectedId = role.getCheckedRadioButtonId();
            RadioButton radioButton = (RadioButton) findViewById(selectedId);

            User user = new User(email.getText().toString(),name.getText().toString(),password.getText().toString(),confirmPassword.getText().toString(),radioButton.getText().toString());
            dao.add(user).addOnSuccessListener(suc-> {
                Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(er->{
                Toast.makeText(this, ""+er.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } );



    }
}