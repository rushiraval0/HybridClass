package com.example.hybridclass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AttendanceSelect extends AppCompatActivity {

    private Spinner attendanceSpinner;
    private FirebaseFirestore db;
    private Button submitBtn;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_select2);

        attendanceSpinner = findViewById(R.id.attendanceSpinner);
        db = FirebaseFirestore.getInstance();
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        uid = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        CalendarView calendarView = findViewById(R.id.calendarView);
        submitBtn = findViewById(R.id.submitBtn);

        populateSpinner();


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                month = month + 1;

                final String datePicked;

                if (dayOfMonth < 10) {
                    datePicked = "0" + dayOfMonth + " " + "0" + month + " " + year;
                    Log.d("ATT_SLCT", datePicked);
                } else {
                    datePicked = dayOfMonth + " " + "0" + month + " " + year;
                }


                submitBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(isConnectedtoInternet(AttendanceSelect.this)){

                            final String spinnerValue = attendanceSpinner.getSelectedItem().toString();

                            //Determines database path for the record of the chosen date
                            DocumentReference dateRef = db.collection("School")
                                    .document("0DKXnQhueh18DH7TSjsb")
                                    .collection("Attendance")
                                    .document(spinnerValue)
                                    .collection("Date")
                                    .document(datePicked);

                            dateRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot documentSnapshot = task.getResult();

                                        assert documentSnapshot != null;
                                        if (documentSnapshot.exists()) {

                                            Intent intent = new Intent(AttendanceSelect.this, AttendanceScreen.class);

                                            Bundle bundle = new Bundle();
                                            bundle.putString("MOD_ID", spinnerValue);
                                            bundle.putString("DATE_PICKED", datePicked);
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(AttendanceSelect.this, "No attendance record for this module on " + datePicked, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(AttendanceSelect.this, "Please connect to internet to see attendance", Toast.LENGTH_SHORT).show();
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
        attendanceSpinner.setAdapter(adapter);
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

    public static boolean isConnectedtoInternet(@NonNull Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null)
        {
            Toast.makeText(context, "You're not connected to the internet", Toast.LENGTH_SHORT).show();
            return false;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}