package com.example.morho.mytest;


import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ToolFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ToolFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Fragment fragmentNow;
    private Fragment[] fragments = new Fragment[3];
    private ViewGroup[] viewGroups = new ViewGroup[3];
    private int taggedView = 0;


    public ToolFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ToolFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ToolFragment newInstance(String param1, String param2) {
        ToolFragment fragment = new ToolFragment();
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
        View view = inflater.inflate(R.layout.fragment_tool, container, false);
        GridLayout gridLayout = (GridLayout) view.findViewById(R.id.tools_grid);
        int parentWidth = this.getActivity().getWindowManager().getDefaultDisplay().getWidth();
        Log.e("GridView msg", "The width is " + parentWidth);
        String items[] = getResources().getStringArray(R.array.tool_items);
        TypedArray array = getResources().obtainTypedArray(R.array.tools_icon);
        for (int i = 0; i < 3; i++) {
            LayoutInflater myinflater = LayoutInflater.from(this.getContext());
            LinearLayout layout = (LinearLayout) myinflater.inflate(R.layout.tools_item, null);
            TextView textView = (TextView) layout.findViewById(R.id.tool_text);
            textView.setText(items[i]);
            ImageView imageView = (ImageView) layout.findViewById(R.id.imageView);
            imageView.setImageDrawable(array.getDrawable(i));
            imageView.setColorFilter(getResources().getColor(R.color.colorPrimary));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(parentWidth / 3, ViewGroup.LayoutParams.WRAP_CONTENT);
            layout.setLayoutParams(params);
            layout.setTag(i);
            layout.setOnClickListener(this);
            gridLayout.addView(layout);
            if (i == 0) {
                layout.setBackground(getActivity().getDrawable(R.color.grey_white));
            } else {
                layout.setBackground(getActivity().getDrawable(R.color.white));
            }
            this.viewGroups[i] = layout;
        }
        FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.lost_frag);
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        ScoreFragment scoreFragment = new ScoreFragment();
        this.fragmentNow = scoreFragment;
        this.fragments[0] = this.fragmentNow;
        transaction.add(R.id.lost_frag, scoreFragment);
        transaction.show(scoreFragment);
        transaction.commit();
        return view;
    }

    private void changeFragment(FragmentTransaction transaction, int index) {
        if (this.fragmentNow != null && fragments[index] == this.fragmentNow) {
            return;
        } else if(fragments[index] == null) {

            switch (index) {
                case 0:
                    fragments[index] = new ScoreFragment();
                    break;
                case 1:
                    fragments[index] = new LostFragment();
                    Log.e("fragment test", "Lost created!");
                    break;
            }
            transaction.add(R.id.lost_frag, fragments[index]);
        }
        transaction.hide(this.fragmentNow);
        this.fragmentNow = fragments[index];
        transaction.show(this.fragmentNow);
    }

    @Override
    public void onClick(View v) {
        int index = (int) v.getTag();

        /*  Test for fragment, no work with the last one!
            2017/10/06  01:43
         */
        if (index == 2) {
            return;
        }
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        changeFragment(transaction, index);
        if (taggedView != index) {
            this.viewGroups[taggedView].setBackground(getActivity().getDrawable(R.color.white));
            v.setBackground(getActivity().getDrawable(R.color.grey_white));
            taggedView = index;
        }
        Log.e("lost_click", "the index is " + index);
        transaction.commit();
    }

}
