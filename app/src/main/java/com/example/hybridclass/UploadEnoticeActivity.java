package com.example.hybridclass;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UploadEnoticeActivity extends AppCompatActivity {

    private Button uploadNotice;
    private TextView noticeStatus;
    private ImageView uploadImageNotice;
    String noticeName="";
    private final int REQ=1;

    private Uri noticeData;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    String downloadUrl="";
    String title="";
    private Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_enotice);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        spinner = findViewById(R.id.spinnerNoticeTeacher);
        populateSpinner();

        noticeStatus = findViewById(R.id.tvSelectedNotice);
        uploadImageNotice = findViewById(R.id.addNotice);

        uploadImageNotice.setOnClickListener((view -> { openGallery(); }));
        uploadNotice = findViewById(R.id.btUploadNotice);

        uploadNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(noticeData==null)
                {
                    Toast.makeText(UploadEnoticeActivity.this,"Please Upload Notice",Toast.LENGTH_LONG).show();
                }

                uploadNoticeFunc();
            }
        });


    }

    private void uploadNoticeFunc() {

        StorageReference reference = storageReference.child("notice/"+noticeName+"-"+System.currentTimeMillis()+".pdf");
        reference.putFile(noticeData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isComplete());
                Uri uri = uriTask.getResult();
                uploadData(String.valueOf(uri));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadEnoticeActivity.this,"Exception : "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void uploadData(String downloadUrl) {
        String uniqueKey = databaseReference.child("notice").push().getKey();
        ENotice notice = new ENotice(downloadUrl,spinner.getSelectedItem().toString());


        databaseReference.child("notice").child(uniqueKey).setValue(notice).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(UploadEnoticeActivity.this,"Uploaded Successfully",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadEnoticeActivity.this,"Exception : "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }


    private void openGallery(){
        Intent i = new Intent();
        i.setType("application/pdf");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Select PDF File"),REQ);
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

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ && resultCode == RESULT_OK)
        {
            noticeData=data.getData();

            if(noticeData.toString().startsWith("content://"))
            {
                Cursor cursor = null;
                try {
                    cursor = UploadEnoticeActivity.this.getContentResolver().query(noticeData,null,null,null,null);
                    if(cursor!=null && cursor.moveToFirst())
                    {
                        noticeName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if(noticeData.toString().startsWith("file://"))
            {
                noticeName = new File(noticeData.toString()).getName();
            }

            noticeStatus.setText(noticeName);
        }
    }
}