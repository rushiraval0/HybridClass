package com.example.hybridclass;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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

public class ModuleSelect extends AppCompatActivity  implements AdapterView.OnItemSelectedListener  {

    private Boolean codeCheck;
    private String uid;
    private FirebaseFirestore db;

    private String studentID;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_select);


        Button scanCodeBtn = findViewById(R.id.scanCodeBtn);
        spinner = findViewById(R.id.spinner);
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        uid = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();

        codeCheck = false;

        if (user != null) {
            Log.d(TAG, "User found");
        } else {
            Log.d(TAG, "User not found");
        }

                Intent intent = getIntent();
        studentID = intent.getStringExtra("STUDENT_ID");

        populateSpinner();

        scanCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    moduleCheck();
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

        private void moduleCheck() {

                final String spinnerValue = spinner.getSelectedItem().toString();

                DocumentReference moduleRef = db.collection("School")
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

                                                String qrCode = documentSnapshot.getString("qr_code");
                        String moduleID = documentSnapshot.getId();

                                                if (qrCode != null) {
                            codeCheck = true;
                        }

                                                if (codeCheck.equals(true)) {
                            Bundle data = new Bundle();
                            data.putString("MOD_ID", moduleID);
                            data.putString("QR_CODE", qrCode);
                            data.putString("STU_ID", studentID);
                            Intent intent = new Intent(ModuleSelect.this, CodeScanner.class);
                            intent.putExtras(data);
                            startActivity(intent);
                        } else if (codeCheck.equals(false)) {
                            Toast.makeText(ModuleSelect.this, "Code not generated for this module", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(ModuleSelect.this, "No document exists", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ModuleSelect.this, "Error. " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


        @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
