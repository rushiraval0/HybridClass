package com.example.hybridclass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {



    public MyAdapter(Context context, ArrayList<Classroom> list,onClassListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    Context context;
    ArrayList<Classroom> list;
    onClassListener listener;


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_layout,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Classroom classroom = list.get(position);
        holder.className.setText(classroom.getClassName());
        holder.classDescription.setText(classroom.getClassDescription());
        holder.classCode.setText(classroom.getClassCode());
        holder.studentCount.setText((Integer.toString(classroom.getStudentCount())));

    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        TextView className,classDescription,classCode,studentCount;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            className = itemView.findViewById(R.id.classNameGen);
            classDescription = itemView.findViewById(R.id.classDescriptionGen);
            classCode = itemView.findViewById(R.id.classCodeGen);
            studentCount = itemView.findViewById(R.id.studentCountGen);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClassClick(view,getAdapterPosition());
        }
    }

    public interface onClassListener{
        void onClassClick(View v,int position);
    }
}
