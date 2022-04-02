package com.example.hybridclass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class GenerateCode extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private final static String TAG = "GEN_CODE";

    private FirebaseFirestore db;
    private String uid;
    private Spinner teacherSpinner;
    private String currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_code);

        Button genCodeBtn = findViewById(R.id.genCodeBtn);
        teacherSpinner = findViewById(R.id.spinner);
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        uid = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();

        populateSpinner();

        genCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isConnectedtoInternet(GenerateCode.this)) {
                    generateCode();
                } else {
                    Toast.makeText(GenerateCode.this, "Please connect to internet to generate code", Toast.LENGTH_LONG).show();
                }

            }
        });
    }


    private void populateSpinner() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final List<String> myClassList = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, myClassList);
        adapter.setDropDownViewResource(R.layout.custom_spinner);
        teacherSpinner.setAdapter(adapter);
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

    private void generateCode() {

        final String spinnerValue = teacherSpinner.getSelectedItem().toString();
        System.out.println(spinnerValue + " TAG");

        final DocumentReference moduleRef = db.collection("School")
                .document("0DKXnQhueh18DH7TSjsb")
                .collection("Modules")
                .document(spinnerValue);

        moduleRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    DocumentSnapshot documentSnapshot = task.getResult();
                    assert documentSnapshot != null;

                    if (documentSnapshot.exists()) {

                        final String moduleID = documentSnapshot.getId();
                        System.out.println(moduleID+"  TAG  ");

                        final DocumentReference documentReference = db.collection("School")
                                .document("0DKXnQhueh18DH7TSjsb")
                                .collection("Modules")
                                .document(moduleID);

                                                documentReference.update("qr_code", genRandomString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Code generated successfully");

                                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot snapshot = task.getResult();

                                            assert snapshot != null;
                                            if (snapshot.exists()) {
                                                final String qrCode = snapshot.getString("qr_code");

                                                                                                currentDate = new SimpleDateFormat("dd MM yyyy", Locale.ENGLISH).format(new Date());

                                                                                                final Map<String, Object> module = new HashMap<>();
                                                module.put("module", moduleID);

                                                                                                final Map<String, Object> date = new HashMap<>();
                                                date.put("date", currentDate);
                                                date.put("attendance", 0);

                                                                                                db.collection("School")
                                                        .document("0DKXnQhueh18DH7TSjsb")
                                                        .collection("Attendance")
                                                        .document(moduleID)
                                                        .set(module);

                                                                                                DocumentReference dateCheck = db.collection("School")
                                                        .document("0DKXnQhueh18DH7TSjsb")
                                                        .collection("Attendance")
                                                        .document(moduleID)
                                                        .collection("Date")
                                                        .document(currentDate);

                                                /*Checks if code was already generated for the selected module on the current date
                                                and asks the user if they want to overwrite the previous attendance
                                                 */
                                                dateCheck.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {

                                                            DocumentSnapshot docSnap = task.getResult();
                                                            assert docSnap != null;
                                                            if (docSnap.exists()) {

                                                                AlertDialog.Builder builder = new AlertDialog.Builder(GenerateCode.this);
                                                                builder.setTitle("Overwrite Attendance?");
                                                                builder.setMessage("Generating a QR code will overwrite previous attendance. Continue?");
                                                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {

                                                                                                                                                db.collection("School")
                                                                                .document("0DKXnQhueh18DH7TSjsb")
                                                                                .collection("Attendance")
                                                                                .document(moduleID)
                                                                                .collection("Date")
                                                                                .document(currentDate)
                                                                                .set(date);

                                                                                                                                                CollectionReference studentRef = db.collection("School")
                                                                                .document("0DKXnQhueh18DH7TSjsb")
                                                                                .collection("Modules")
                                                                                .document(moduleID)
                                                                                .collection("Students");

                                                                                                                                                Query query = studentRef.orderBy("student_id", Query.Direction.DESCENDING);
                                                                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                                                if (task.isSuccessful()) {
                                                                                    for (QueryDocumentSnapshot queryDocumentSnapshot : Objects.requireNonNull(task.getResult())) {

                                                                                        String studentID = queryDocumentSnapshot.getId();
                                                                                        Log.d("STU_ID", studentID);

                                                                                                                                                                                Map<String, Object> attended = new HashMap<>();
                                                                                        attended.put("attended", false);
                                                                                        attended.put("student_id", studentID);

                                                                                        db.collection("School")
                                                                                                .document("0DKXnQhueh18DH7TSjsb")
                                                                                                .collection("Attendance")
                                                                                                .document(moduleID)
                                                                                                .collection("Date")
                                                                                                .document(currentDate)
                                                                                                .collection("Students")
                                                                                                .document(studentID)
                                                                                                .set(attended);

                                                                                                                                                                                Map<String, Object> stuID = new HashMap<>();
                                                                                        stuID.put("student_id", studentID);

                                                                                        db.collection("School")
                                                                                                .document("0DKXnQhueh18DH7TSjsb")
                                                                                                .collection("AttendanceRecord")
                                                                                                .document(studentID)
                                                                                                .set(stuID);

                                                                                                                                                                                Map<String, Object> attend = new HashMap<>();
                                                                                        attend.put("date", currentDate);
                                                                                        attend.put("module", moduleID);
                                                                                        attend.put("attended", false);
                                                                                        attend.put("time", FieldValue.serverTimestamp());

                                                                                                                                                                                String docName = moduleID.replaceAll("\\s+", "") + currentDate.replaceAll("\\s+", "");

                                                                                        db.collection("School")
                                                                                                .document("0DKXnQhueh18DH7TSjsb")
                                                                                                .collection("AttendanceRecord")
                                                                                                .document(studentID)
                                                                                                .collection("Records")
                                                                                                .document(docName)
                                                                                                .set(attend, SetOptions.merge());
                                                                                    }
                                                                                }
                                                                            }
                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                System.out.println("TAG  "+e);
                                                                            }
                                                                        });;

                                                                                                                                                Intent intent = new Intent(GenerateCode.this, CodeScreen.class);
                                                                        intent.putExtra("QR_CODE", qrCode);
                                                                        intent.putExtra("MOD_ID", moduleID);
                                                                        startActivity(intent);

                                                                    }
                                                                });
                                                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        dialog.dismiss();
                                                                    }
                                                                });
                                                                AlertDialog alert = builder.create();
                                                                alert.show();
                                                            } else {

                                                                                                                                db.collection("School")
                                                                        .document("0DKXnQhueh18DH7TSjsb")
                                                                        .collection("Attendance")
                                                                        .document(moduleID)
                                                                        .collection("Date")
                                                                        .document(currentDate)
                                                                        .set(date);

                                                                                                                                CollectionReference studentRef = db.collection("School")
                                                                        .document("0DKXnQhueh18DH7TSjsb")
                                                                        .collection("Modules")
                                                                        .document(moduleID)
                                                                        .collection("Students");

                                                                                                                                Query query = studentRef.orderBy("student_id", Query.Direction.DESCENDING);
                                                                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                                        if (task.isSuccessful()) {
                                                                            for (QueryDocumentSnapshot queryDocumentSnapshot : Objects.requireNonNull(task.getResult())) {

                                                                                String studentID = queryDocumentSnapshot.getId();
                                                                                Log.d("STU_ID", studentID);

                                                                                                                                                                Map<String, Object> attended = new HashMap<>();
                                                                                attended.put("attended", false);
                                                                                attended.put("student_id", studentID);

                                                                                db.collection("School")
                                                                                        .document("0DKXnQhueh18DH7TSjsb")
                                                                                        .collection("Attendance")
                                                                                        .document(moduleID)
                                                                                        .collection("Date")
                                                                                        .document(currentDate)
                                                                                        .collection("Students")
                                                                                        .document(studentID)
                                                                                        .set(attended);

                                                                                                                                                                Map<String, Object> stuID = new HashMap<>();
                                                                                stuID.put("student_id", studentID);

                                                                                db.collection("School")
                                                                                        .document("0DKXnQhueh18DH7TSjsb")
                                                                                        .collection("AttendanceRecord")
                                                                                        .document(studentID)
                                                                                        .set(stuID);

                                                                                                                                                                Map<String, Object> attend = new HashMap<>();
                                                                                attend.put("date", currentDate);
                                                                                attend.put("module", moduleID);
                                                                                attend.put("attended", false);
                                                                                attend.put("time", FieldValue.serverTimestamp());

                                                                                                                                                                String docName = moduleID.replaceAll("\\s+", "") + currentDate.replaceAll("\\s+", "");

                                                                                db.collection("School")
                                                                                        .document("0DKXnQhueh18DH7TSjsb")
                                                                                        .collection("AttendanceRecord")
                                                                                        .document(studentID)
                                                                                        .collection("Records")
                                                                                        .document(docName)
                                                                                        .set(attend, SetOptions.merge());
                                                                            }
                                                                        }
                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        System.out.println("TAG  "+e);
                                                                    }
                                                                });;

                                                                                                                                Intent intent = new Intent(GenerateCode.this, CodeScreen.class);
                                                                intent.putExtra("QR_CODE", qrCode);
                                                                intent.putExtra("MOD_ID", moduleID);
                                                                startActivity(intent);
                                                            }
                                                        } else {
                                                            Toast.makeText(GenerateCode.this, "Error: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        System.out.println("TAG  "+e);
                                                    }
                                                });;
                                            } else {
                                                Toast.makeText(GenerateCode.this, "Nothing found.", Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            Toast.makeText(GenerateCode.this, "Error: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        System.out.println("TAG  "+e);
                                    }
                                });;
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                System.out.println("TAG  "+e);
                            }
                        });
                    } else {
                        Toast.makeText(GenerateCode.this, "Document doesn't exist.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(GenerateCode.this, "Error: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("TAG  "+e);
            }
        });
    }

        private String genRandomString() {
        char[] characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 25; i++) {
            char c = characters[random.nextInt(characters.length)];
            stringBuilder.append(c);
        }
        return stringBuilder.toString();

    }

        public static boolean isConnectedtoInternet(@NonNull Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            Toast.makeText(context, "You're not connected to the internet", Toast.LENGTH_SHORT).show();
            return false;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

        @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}