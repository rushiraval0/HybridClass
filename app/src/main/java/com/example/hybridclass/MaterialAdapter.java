package com.example.hybridclass;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.MyViewHolder> {

    public MaterialAdapter(Context context, ArrayList<FileMaterial> list) {
        this.context = context;
        this.list = list;
    }

    Context context;
    ArrayList<FileMaterial> list;
    DownloadManager manager;


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.view_file_card,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        FileMaterial fileMaterial = list.get(position);
        holder.fileName.setText(fileMaterial.getPdfName());
        holder.fileClassCode.setText(fileMaterial.getClassCode());
        holder.fileDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(fileMaterial.getPdfUrl()));
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{


        TextView fileName;
        TextView fileClassCode;
        Button fileDownloadButton;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            fileName = itemView.findViewById(R.id.fileNameGen);
            fileClassCode = itemView.findViewById(R.id.fileClassCodeGen);
            fileDownloadButton = itemView.findViewById(R.id.fileDownloadButton);
        }
    }
}
