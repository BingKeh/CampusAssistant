package com.example.morho.mytest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

/**
 * Created by Morho on 8/20/2017.
 */

public class WeekSelectDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {
    private View view;
    private postWeekChanged mListener;
    private int week = 1;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        getArguments();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.course_radio_group, null);
        this.view = view;
        builder.setView(view);
        builder.setTitle("选择周数");
        builder.setNegativeButton("取消", this);
        builder.setPositiveButton("确定", this);
        float scale = getResources().getDisplayMetrics().density;
        RadioGroup radio_list  = (RadioGroup) view.findViewById(R.id.radio_group);
        String[] array = new String[25];

        for (int i = 0; i < array.length; i++) {
            array[i] = "第" + (i + 1) + "周";
            RadioButton radioButton = (RadioButton) inflater.inflate(R.layout.course_select_item, null);
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(MyTool.dip2px(16, scale), MyTool.dip2px(16, scale), MyTool.dip2px(16, scale), MyTool.dip2px(16, scale));
            radioButton.setId(i);
            radioButton.setText(array[i]);
            radioButton.setPadding(MyTool.dip2px(24, scale), 0, 0, 0);
            radioButton.setTextSize(16);
            radio_list.addView(radioButton, params);
        }
        radio_list.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                System.out.println("You have selected " + "第" + (checkedId + 1) + "周");
                week = checkedId + 1;
            }
        });
        return  builder.create();
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case Dialog.BUTTON_POSITIVE:
                //do change the week in TimetableActivity
                mListener.do_week_changed(this.week);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof WeekSelectDialogFragment.postWeekChanged) {
            mListener = (WeekSelectDialogFragment.postWeekChanged) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
        //bg_array = getResources().getIntArray(R.array.bg_img);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface postWeekChanged {
        void do_week_changed(int week);
    }
}
