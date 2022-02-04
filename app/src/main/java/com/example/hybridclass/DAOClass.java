package com.example.hybridclass;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DAOClass {
    private DatabaseReference databaseReference;
    public DAOClass()
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Classroom.class.getSimpleName());
    }

    public Task<Void> add(Classroom c)
    {
        return databaseReference.push().setValue(c);
    }
}
