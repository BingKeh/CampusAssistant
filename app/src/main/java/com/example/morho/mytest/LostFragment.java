package com.example.morho.mytest;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LostFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText name, text_location, text_ot;
    private Spinner spinner;


    public LostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LostFragment newInstance(String param1, String param2) {
        LostFragment fragment = new LostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lost, container, false);
        name = (EditText) view.findViewById(R.id.text_name);
        text_location = (EditText) view.findViewById(R.id.text_location);
        text_ot = (EditText) view.findViewById(R.id.text_ot);
        spinner = (Spinner) view.findViewById(R.id.spinner);
        view.findViewById(R.id.lost_btn).setOnClickListener(this);
        return view;
    }

    public void do_lost(View v) throws UnsupportedEncodingException {
        String title = name.getText().toString();
        String location = text_location.getText().toString();
        String ot = text_ot.getText().toString();
        String cate = spinner.getSelectedItem().toString();
        Log.e("spinner test", "you have select " + cate);
        App_DATA data = (App_DATA) getActivity().getApplication();
        String name = data.getName();
        String params[] = { name, title, location, cate, ot };
        new lost().execute(params);
    }


    @Override
    public void onClick(View v) {
        try {
            do_lost(v);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private class lost extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            Log.e("lost test", "do lost succeed!");
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean flag = false;
            try {
                flag = new NetTool().do_upload_lost(params);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return flag;
        }
    }
}
