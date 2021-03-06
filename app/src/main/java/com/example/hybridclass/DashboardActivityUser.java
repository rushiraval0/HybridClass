package com.example.hybridclass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardActivityUser extends AppCompatActivity {

    ImageButton userLogout;
    TextView email;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    ImageView joinClass;
    ImageView viewNotice;
    ImageView viewMaterial;
    ImageView attendQuiz;
    ImageView markAttendance;
    ImageView viewResult;
    MaterialButton viewClassroom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_user);
        viewClassroom = findViewById(R.id.todoB);

        userLogout = (ImageButton) findViewById(R.id.logOutStudent);
        email = (TextView) findViewById(R.id.textView2);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        joinClass = (ImageView) findViewById(R.id.image_view1);
        viewMaterial = (ImageView) findViewById(R.id.image_view22);
        viewNotice = (ImageView) findViewById(R.id.image_view23);
        attendQuiz = (ImageView) findViewById(R.id.image_view21);
        viewResult = (ImageView) findViewById(R.id.image_view);
        markAttendance = (ImageView) findViewById(R.id.image_view20);


        markAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivityUser.this,AttendanceDashboardStudent.class));
            }
        });

        viewResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivityUser.this,ViewResultStudent.class));
            }
        });


        attendQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivityUser.this,ViewQuiz.class));
            }
        });

        viewNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivityUser.this,ViewEnoticeActivity.class));
            }
        });

        viewMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivityUser.this,DisplayMaterial.class));
            }
        });

        viewClassroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivityUser.this,DisplayMyClasses.class));
            }
        });


        joinClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivityUser.this,JoinClassActivity.class));
            }
        });

        email.setText(firebaseUser.getEmail());

        userLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(DashboardActivityUser.this,LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
    }
}