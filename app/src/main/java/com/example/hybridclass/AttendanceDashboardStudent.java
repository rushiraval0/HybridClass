package com.example.hybridclass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class AttendanceDashboardStudent extends AppCompatActivity {

    private long backPressed;
    private FirebaseFirestore db;
    private FirebaseAuth fAuth;
    TextView nameDisplay;
    TextView idDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_dashboard_student);

        nameDisplay = findViewById(R.id.nameDisplay);
        idDisplay = findViewById(R.id.idDisplay);
        CardView scanCard = findViewById(R.id.scanCard);
        CardView moduleCard = findViewById(R.id.moduleCard);
        CardView attendanceCard = findViewById(R.id.attendanceCard);
        CardView settingsCard = findViewById(R.id.settingsCard);
        db = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        getNameID();

        scanCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AttendanceDashboardStudent.this, ModuleSelect.class);
                String studentName = nameDisplay.getText().toString();
                String studentID = idDisplay.getText().toString();

                intent.putExtra("STUDENT_NAME", studentName);
                intent.putExtra("STUDENT_ID", studentID);
                startActivity(intent);
            }
        });

        attendanceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(AttendanceDashboardStudent.this, StudentAttendanceScreen.class);
                    intent.putExtra("STU_ID", idDisplay.getText().toString());
                    startActivity(intent);

            }
        });
    }

    private void getNameID() {
        String uid = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        System.out.println(uid+" TAG");

        DocumentReference documentReference = db.collection("School")
                .document("0DKXnQhueh18DH7TSjsb")
                .collection("User")
                .document(uid);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {

                    DocumentSnapshot documentSnapshot = task.getResult();

                    assert documentSnapshot != null;
                    if(documentSnapshot.exists()) {

                        String studentName = documentSnapshot.getString("name");
                        String studentID = documentSnapshot.getString("student_id");
                        System.out.println(studentID+" TAG");

                        idDisplay.setText(studentID);
                        idDisplay.setVisibility(View.INVISIBLE);
                    }
                    else {
                        Toast.makeText(AttendanceDashboardStudent.this, "Document doesn't exist", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(AttendanceDashboardStudent.this, "Error: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
