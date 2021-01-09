package com.example.covid_19contacttracing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryProgramAdapter extends RecyclerView.Adapter<HistoryProgramAdapter.ViewHolder>
{
    Context context;
    ArrayList<String> programNameList;
    ArrayList<String> programDescriptionList;
    ArrayList<Integer> images;

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView rowName, rowDescription;
        ImageView rowImage;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            rowName = itemView.findViewById(R.id.itemName);
            rowDescription = itemView.findViewById(R.id.itemDescription);
            rowImage = itemView.findViewById(R.id.itemImage);
        }
    }

    public HistoryProgramAdapter(Context context, ArrayList<String> programNameList, ArrayList<String> programDescriptionList, ArrayList<Integer> images)
    {
        this.context = context;
        this.programNameList = programNameList;
        this.programDescriptionList = programDescriptionList;
        this.images = images;
    }

    @NonNull
    @Override
    public HistoryProgramAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.single_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryProgramAdapter.ViewHolder holder, int position)
    {
        holder.rowName.setText(programNameList.get(position));
        holder.rowDescription.setText(programDescriptionList.get(position));
        holder.rowImage.setImageResource(images.get(position));
    }

    @Override
    public int getItemCount()
    {
        try
        {
            return programNameList.size();
        }
        catch(Exception e)
        {
            return 0;
        }
    }
}
