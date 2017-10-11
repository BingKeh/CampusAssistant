package com.example.morho.mytest;


import android.animation.Animator;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScoreFragment extends Fragment implements ListView.OnItemClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView listView;
    private HashMap<String, String> data;
    private MyScoreAdapter myScoreAdapter;
    private List<HashMap<String, String>> list, mFilterList, list_backup;
    protected ScoreBaseAdapter scoreBaseAdapter;
    private Animation animation;
    private LayoutAnimationController controller;

    public ScoreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScoreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScoreFragment newInstance(String param1, String param2) {
        ScoreFragment fragment = new ScoreFragment();
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
        View view = inflater.inflate(R.layout.fragment_score, container, false);
        listView = (ListView) view.findViewById(R.id.score_list);
        list = getList(view);
        list_backup = getList(view);
        scoreBaseAdapter = new ScoreBaseAdapter(getActivity(), list);
        myScoreAdapter = new MyScoreAdapter(view.getContext(), list, R.layout.list_item,
                new String[] {Scoredb.ScoreEntry.COLUMN_NAME_SC_YEARS,
                        Scoredb.ScoreEntry.COLUMN_NAME_SC_TERM,
                        Scoredb.ScoreEntry.COLUMN_NAME_SC_ID,
                        Scoredb.ScoreEntry.COLUMN_NAME_SC_NAME,
                        Scoredb.ScoreEntry.COLUMN_NAME_SC_SC},
                new int[] {R.id.sc_years, R.id.sc_term, R.id.sc_id, R.id.sc_name, R.id.sc_sc});
        animation = new AlphaAnimation(0f, 1f);
        animation.setDuration(500);
        controller = new LayoutAnimationController(animation, 0f);
        controller.setOrder(controller.ORDER_NORMAL);
        listView.setLayoutAnimation(controller);
        listView.setAdapter(scoreBaseAdapter);
        listView.requestFocus();
        //listView.setOnItemClickListener(this);
        //myScoreAdapter.notifyDataSetChanged();

        return view;
    }

    protected List<HashMap<String, String>> getList(View view) {
        HashMap<String, String> data = new HashMap<>();
        List<HashMap<String, String>> list = new ArrayList<>();
        ScoredbHelper scoredbHelper = new ScoredbHelper(view.getContext());
        list = Scoredb.get_list(scoredbHelper.getReadableDatabase());
        return list;
    }

    public void search_lsit(CharSequence charSequence) {
        new SearchFilter().filter(charSequence);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView name = (TextView) view.findViewById(R.id.sc_name);

        myScoreAdapter.setPostion(position);
        System.out.println("you have clicked " + name.getText().toString() + "The postion is " + position);
    }

    private class SearchFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String keyword = constraint.toString();
            FilterResults filterResults = new FilterResults();
            if (constraint == null || keyword.length() == 0) {
                mFilterList = new ArrayList<>();
                mFilterList.addAll(list_backup);
            } else {
                mFilterList = new ArrayList<>();
                for (Iterator<HashMap<String, String>> iterator = list.iterator();
                     iterator.hasNext();) {
                    HashMap<String, String> data = iterator.next();
                    String name = data.get(Scoredb.ScoreEntry.COLUMN_NAME_SC_NAME);
                    if (name.contains(keyword)) {
                        mFilterList.add(data);
                        System.out.println("We have " + name);
                    }
                }
            }
            filterResults.values = mFilterList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list = (List<HashMap<String, String>>) results.values;

            scoreBaseAdapter.changeList(list);
            scoreBaseAdapter.notifyDataSetChanged();
            scoreBaseAdapter.clearSelect();
//            myScoreAdapter = new MyScoreAdapter(getContext(), list, R.layout.list_item,
//                    new String[] {Scoredb.ScoreEntry.COLUMN_NAME_SC_YEARS,
//                            Scoredb.ScoreEntry.COLUMN_NAME_SC_TERM,
//                            Scoredb.ScoreEntry.COLUMN_NAME_SC_ID,
//                            Scoredb.ScoreEntry.COLUMN_NAME_SC_NAME,
//                            Scoredb.ScoreEntry.COLUMN_NAME_SC_SC},
//                    new int[] {R.id.sc_years, R.id.sc_term, R.id.sc_id, R.id.sc_name, R.id.sc_sc});
//            listView.setAdapter(myScoreAdapter);
        }
    }

}
