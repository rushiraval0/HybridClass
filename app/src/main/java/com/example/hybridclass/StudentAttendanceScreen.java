package com.example.hybridclass;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class StudentAttendanceScreen extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth fAuth;
    private String uid;
    private String studentID;
    private TextView totalAttTV;
    private TextView lecAttTV;
    private TextView lecMissedTV;
    private TextView percentageTV;
    private ProgressBar attendProgress;
    private Spinner spinner;
    private Button submitBtn;
    private int totalCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance_screen);



        db = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        uid = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        totalAttTV = findViewById(R.id.totalAttTV);
        lecAttTV = findViewById(R.id.lecAttTV);
        lecMissedTV = findViewById(R.id.lecMissedTV);
        percentageTV = findViewById(R.id.percentageTV);
        attendProgress = findViewById(R.id.attendProgress);
        spinner = findViewById(R.id.spinner);
        submitBtn = findViewById(R.id.submitBtn);

        Intent intent = getIntent();
        studentID = intent.getStringExtra("STU_ID");
        System.out.println(studentID+" TAG");

        getRecord();
        populateSpinner();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    uid = fAuth.getCurrentUser().getUid();

                    final String spinnerValue = spinner.getSelectedItem().toString();

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


                                    String e=fAuth.getCurrentUser().getEmail();
                                    int x=e.indexOf("@");
                                    String user=e.substring(0,x);

                                    studentID = user;

                                    Bundle bundle = new Bundle();
                                    bundle.putString("MOD_ID", spinnerValue);
                                    bundle.putString("STU_ID", studentID);
                                    Intent intent = new Intent(StudentAttendanceScreen.this, PersonalAttendance.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            } else {
                                Toast.makeText(StudentAttendanceScreen.this, "Error: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

            }
        });
    }

    private void populateSpinner() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final List<String> myClassList = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, myClassList);
        adapter.setDropDownViewResource(R.layout.custom_spinner);
        spinner.setAdapter(adapter);
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("User").child(user.getUid());


        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int c = 0;
                for (DataSnapshot childSnapshot : snapshot.child("classList").getChildren()) {
                    myClassList.add(childSnapshot.getValue(String.class));
                    System.out.println("RUSY " + c + " " + myClassList);
                    c++;
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Method to retrieve student's attendance
    private void getRecord() {

        Intent intent = getIntent();
        studentID = intent.getStringExtra("STU_ID");
        System.out.println(studentID+" TAG");
        assert studentID != null;
        final CollectionReference recordRef = db.collection("School")
                .document("0DKXnQhueh18DH7TSjsb")
                .collection("AttendanceRecord")
                .document(studentID)
                .collection("Records");

        //Finds total number of lectures
        Query totalQuery = recordRef.orderBy("date", Query.Direction.DESCENDING);
        totalQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {

                    totalCounter = 0;

                    for (QueryDocumentSnapshot ignored : Objects.requireNonNull(task.getResult())) {

                        totalCounter++;
                    }

                    Query attendedQuery = recordRef.whereEqualTo("attended", true);

                    attendedQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {

                                int attendedCounter = 0;

                                for (QueryDocumentSnapshot ignored : Objects.requireNonNull(task.getResult())) {

                                    attendedCounter++;
                                }

                                String totalAttendance = "Total Attendance: " + attendedCounter + "/" + totalCounter;
                                totalAttTV.setText(totalAttendance);
                                String lecAttended = "Lectures Attended: " + attendedCounter;
                                lecAttTV.setText(lecAttended);

                                float totalPercentage = (float) attendedCounter / totalCounter * 100;
                                String formattedNum = String.format(Locale.getDefault(), "%.2f", totalPercentage);

                                int percentage = Math.round(totalPercentage);
                                String showPercent = formattedNum + "%";

                                float floatPercent = Float.parseFloat(formattedNum);

                                if (Double.isNaN(floatPercent)) {
                                    String attStr = "No Attendance";
                                    percentageTV.setText(attStr);
                                } else {
                                    percentageTV.setText(showPercent);
                                }

                                attendProgress.setProgress(percentage, true);

                                Query missedQuery = recordRef.whereEqualTo("attended", false);
                                missedQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                        if (task.isSuccessful()) {

                                            //Counter for missed lectures
                                            int missedCounter = 0;

                                            //Wherever the attended field equals false, increment the counter
                                            for (QueryDocumentSnapshot ignored : Objects.requireNonNull(task.getResult())) {

                                                missedCounter++;
                                            }

                                            String missedLec = "Lectures Missed: " + missedCounter;
                                            lecMissedTV.setText(missedLec);
                                        } else {
                                            Toast.makeText(StudentAttendanceScreen.this, "Error: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(StudentAttendanceScreen.this, "Error: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(StudentAttendanceScreen.this, "Error: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}
