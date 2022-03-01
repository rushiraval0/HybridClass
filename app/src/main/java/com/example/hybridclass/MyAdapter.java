package com.example.hybridclass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    public MyAdapter(Context context, ArrayList<Classroom> list) {
        this.context = context;
        this.list = list;
    }

    Context context;
    ArrayList<Classroom> list;


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

    public static class MyViewHolder extends RecyclerView.ViewHolder{


        TextView className,classDescription,classCode,studentCount;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            className = itemView.findViewById(R.id.classNameGen);
            classDescription = itemView.findViewById(R.id.classDescriptionGen);
            classCode = itemView.findViewById(R.id.classCodeGen);
            studentCount = itemView.findViewById(R.id.studentCountGen);
        }
    }
}
