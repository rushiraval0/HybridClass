package com.example.hybridclass;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AttendedFragment extends Fragment {

    private FirebaseFirestore db;
    private AttendanceAdapter attendAdapter;
    private RecyclerView recyclerView;
    private String module;
    private String date;
    ArrayList<String> students;
    FloatingActionButton fab;
    Button b;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.attended_fragment, container, false);
        recyclerView = v.findViewById(R.id.attendedRV);

        fab = v.findViewById(R.id.fab);

        db = FirebaseFirestore.getInstance();

        AttFragInterface activity = (AttFragInterface) getActivity();
        assert activity != null;
        module = activity.getModule();
        date = activity.getDate();
        runQuery();
        getStudents();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getStudents();
            }
        });

        return v;
    }

    private void runQuery() {

        CollectionReference moduleRef = db.collection("School")
                .document("0DKXnQhueh18DH7TSjsb")
                .collection("Attendance")
                .document(module)
                .collection("Date")
                .document(date)
                .collection("Student");
        System.out.println(getActivity()+" TAGE");

        //Looks for the students who HAVE attended the module
        Query query = moduleRef.whereEqualTo("attended", true);
        students = new ArrayList<>();
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : Objects.requireNonNull(task.getResult())) {
                        queryDocumentSnapshot.getId();
                        System.out.println("TAG1  "+queryDocumentSnapshot.getId());
                        students.add(queryDocumentSnapshot.getId().toString());

                    }
                } else {
                    Toast.makeText(getContext(), "Query failed", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("TAG  "+e);
            }
        });
    }

    private void getStudents() {


        //Builds the RecyclerView
        System.out.println("TAG1  "+students);
        attendAdapter = new AttendanceAdapter(getContext(), students);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(attendAdapter);
        attendAdapter.notifyDataSetChanged();


    }

}
