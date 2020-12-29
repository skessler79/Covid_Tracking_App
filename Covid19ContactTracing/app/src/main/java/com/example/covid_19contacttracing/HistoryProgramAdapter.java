package com.example.covid_19contacttracing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryProgramAdapter extends RecyclerView.Adapter<HistoryProgramAdapter.ViewHolder>
{
    Context context;
    String[] programNameList;
    String[] programDescriptionList;
    int[] images;

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView rowName, rowDescription;
        ImageView rowImage;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            rowName = itemView.findViewById(R.id.history_text_view1);
            rowDescription = itemView.findViewById(R.id.history_text_view2);
            rowImage = itemView.findViewById(R.id.history_image_view);
        }
    }

    public HistoryProgramAdapter(Context context, String[] programNameList, String[] programDescriptionList, int[] images)
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
        View view = inflater.inflate(R.layout.history_single_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryProgramAdapter.ViewHolder holder, int position)
    {
        holder.rowName.setText(programNameList[position]);
        holder.rowDescription.setText(programDescriptionList[position]);
        holder.rowImage.setImageResource(images[position]);
    }

    @Override
    public int getItemCount()
    {
        try
        {
            return programNameList.length;
        }
        catch(Exception e)
        {
            return 0;
        }
    }
}
