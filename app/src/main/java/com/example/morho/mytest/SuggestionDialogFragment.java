package com.example.morho.mytest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.zip.Inflater;

/**
 * Created by Morho on 8/8/2017.
 */

public class SuggestionDialogFragment extends DialogFragment implements DialogInterface.OnClickListener{
    private View view;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.suggestion_text, null);
        this.view = view;
        builder.setView(view);
        builder.setTitle("建议反馈");
        builder.setMessage("\n请写下你的建议并为应用打分！");
        builder.setNegativeButton("取消", this);
        builder.setPositiveButton("提交", this);
        this.setCancelable(false);
        return  builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case Dialog.BUTTON_POSITIVE : {
                try {
                    Field field = getDialog().getClass().getSuperclass().getDeclaredField("mShowing");
                    field.setAccessible(true);
                    field.set(getDialog(), false);
                    getDialog().dismiss();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                EditText text = (EditText) getDialog().findViewById(R.id.sug_text);
                String suggestion = text.getText().toString();
                text.setVisibility(View.GONE);
                ProgressBar bar = (ProgressBar) getDialog().findViewById(R.id.process_sug);
                bar.setVisibility(View.VISIBLE);
                App_DATA data = (App_DATA) getActivity().getApplication();
                String name = data.getName();

                new do_sug().execute(name, suggestion);
                break;
            }
            case Dialog.BUTTON_NEGATIVE : {
                Field field = null;
                try {
                    field = getDialog().getClass().getSuperclass().getDeclaredField("mShowing");
                    field.setAccessible(true);
                    field.set(getDialog(), true);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                this.dismiss();
            }
        }

    }

    public void postSuggest(Boolean flag) throws Throwable {
        if (flag) {
            Toast.makeText(getContext(), "提交成功！", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "提交失败！", Toast.LENGTH_SHORT).show();

        }

        android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(new do_delay(), 1000);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    private class do_sug extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            boolean flag = false;
            try {
                Thread.sleep(1000);
                flag  = new NetTool().do_upload_sug(params);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return flag;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            try {
                postSuggest(aBoolean);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    private class do_delay implements Runnable {

        @Override
        public void run() {
            try {
                System.out.println("suggestion ok!");
                Field field = getDialog().getClass().getSuperclass().getDeclaredField("mShowing");
                field.setAccessible(true);
                field.set(getDialog(), true);
                SuggestionDialogFragment.this.dismiss();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }
}
