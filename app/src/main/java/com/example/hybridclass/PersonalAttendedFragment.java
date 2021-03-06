package com.example.hybridclass;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.Objects;

public class PersonalAttendedFragment extends Fragment {
    private String module;
    private String studentID;

    private FirebaseFirestore db;
    private PersonalAttendanceAdapter adapter;
    private RecyclerView recyclerView;

    public PersonalAttendedFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.personal_attended_fragment, container, false);
        recyclerView = v.findViewById(R.id.personalAttendedRV);

        db = FirebaseFirestore.getInstance();

                PersonalAttFragInterface activity = (PersonalAttFragInterface) getActivity();
        assert activity != null;
        module = activity.getModuleName();
        studentID = activity.getStudentID();

        getDates();

        return v;
    }

    private void getDates() {

                CollectionReference recordRef = db.collection("School")
                .document("0DKXnQhueh18DH7TSjsb")
                .collection("AttendanceRecord")
                .document(studentID)
                .collection("Records");

                final Query query = recordRef.whereEqualTo("module", module).whereEqualTo("attended", true);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot queryDocumentSnapshot : Objects.requireNonNull(task.getResult())) {
                        String date = queryDocumentSnapshot.getString("date");

                        Log.d("PERS", "Docs: " + date);
                    }
                }
            }
        });

                FirestoreRecyclerOptions<Date> dates = new FirestoreRecyclerOptions.Builder<Date>()
                .setQuery(query, Date.class)
                .build();

        adapter = new PersonalAttendanceAdapter(dates);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }



        @Override
    public void onStart() {
        super.onStart();
            adapter.startListening();

    }

        @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
