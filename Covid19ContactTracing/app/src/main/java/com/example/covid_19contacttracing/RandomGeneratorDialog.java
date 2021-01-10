package com.example.covid_19contacttracing;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;


public class RandomGeneratorDialog extends AppCompatDialogFragment {

    //Declare Object
    private Admin admin;
    private Context context;

    public RandomGeneratorDialog (){
        super();

        //initialize admin
        admin = new Admin();
    }

    public RandomGeneratorDialog(Context rContext){
        super();

        //initialize admin
        admin = new Admin();

        this.context = rContext;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder((getActivity()));
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.random_generator_dialog, null);

        builder.setView(view).setTitle("Confirmation")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                admin.randomVisitGenerator(context);
            }
        });
        return builder.create();
    }
}
