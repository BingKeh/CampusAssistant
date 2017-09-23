package com.example.morho.mytest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Morho on 9/1/2017.
 */

public class TermSelectDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        getArguments();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.term_select, null);
        builder.setView(view);
        builder.setPositiveButton("OK", null);
        builder.setTitle("选择课程表");


        return builder.create();
    }
}
