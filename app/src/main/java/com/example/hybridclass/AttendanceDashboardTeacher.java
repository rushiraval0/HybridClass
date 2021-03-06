package com.example.hybridclass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AttendanceDashboardTeacher extends AppCompatActivity {

    TextView nameDisplay;
    TextView idDisplay;
    private FirebaseAuth fAuth;
    private FirebaseFirestore db;
    private long backPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_dashboard_teacher);

        nameDisplay = findViewById(R.id.nameDisplay);
        idDisplay = findViewById(R.id.idDisplay);
        CardView genCard = findViewById(R.id.genCard);
        CardView attendanceCard = findViewById(R.id.attendanceCard);
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        getNameID();

        genCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String teacherName = nameDisplay.getText().toString();
                String teacherID = idDisplay.getText().toString();

                Intent intent = new Intent(AttendanceDashboardTeacher.this, GenerateCode.class);
                intent.putExtra("TEACHER_NAME", teacherName);
                intent.putExtra("TEACHER_ID", teacherID);
                startActivity(intent);
            }
        });

        attendanceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AttendanceDashboardTeacher.this, AttendanceSelect.class);
                startActivity(intent);
            }
        });
    }

    private void getNameID() {

        String uid = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();

        DocumentReference documentReference = db.collection("School")
                .document("0DKXnQhueh18DH7TSjsb")
                .collection("User")
                .document(uid);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {

                    DocumentSnapshot documentSnapshot = task.getResult();

                    assert documentSnapshot != null;
                    if (documentSnapshot.exists()) {

                        String teacherName = documentSnapshot.getString("name");
                        String teacherID = documentSnapshot.getString("teacher_id");

                        nameDisplay.setText(teacherName);
                        idDisplay.setText(teacherID);
                    } else {
                        Toast.makeText(AttendanceDashboardTeacher.this, "Document doesn't exist", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AttendanceDashboardTeacher.this, "Error: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
