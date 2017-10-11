package com.example.morho.mytest;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class NewsFragment extends Fragment implements lost_item_Adapter.onItemClickListener, SwipeRefreshLayout.OnRefreshListener{

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private List<Lost_Item_Entity> list;
    private lost_item_Adapter adapter;
    private SwipeRefreshLayout refreshLayout;
    RecyclerView recyclerView;
    private int[] bg_array = new int[] {
            R.drawable.bg,
            R.drawable.bg1,
            R.drawable.bg2,
            R.drawable.bg3,
            R.drawable.bg4,
            R.drawable.bg5,
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NewsFragment() {

    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static NewsFragment newInstance(int columnCount) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tools_list, container, false);
        View list_view = view.findViewById(R.id.list);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_list);
        new lost_net().execute("");
        // Set the adapter
        if (list_view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) list_view;
            RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
//            if (mColumnCount <= 1) {
//                recyclerView.setLayoutManager(new LinearLayoutManager(context));
//            } else {
//                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
//            }
            list = new ArrayList<>();
//            for (int i = 0; i < 1; i++) {
//                Lost_Item_Entity data = new Lost_Item_Entity();
//                data.initDefaultData(i);
//                data.setImg(bg_array[i % 6]);
//                list.add(data);
//            }
            lost_item_Adapter adapter = new lost_item_Adapter(getContext() ,list);
            this.adapter = adapter;
            adapter.setOnClickListener(this);
            recyclerView.setAdapter(adapter);
        }
        setHasOptionsMenu(false);
        refreshLayout.setOnRefreshListener(this);
        System.out.println("tools fragment created!");

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
        mListener.onListFragmentInteraction();
        //bg_array = getResources().getIntArray(R.array.bg_img);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view, int position) {
        Log.e("Recycle View test", "view " + position + " clicked!");
        Intent intent = new Intent(this.getContext(), LostDetailActivity.class);
        intent.putExtra("IMG_VALUE", bg_array[position % 6]);
        String transitionName = getString(R.string.lost_img_transition);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this.getActivity(), view.findViewById(R.id.card_img), transitionName);
        ActivityCompat.startActivity(this.getContext(), intent, options.toBundle());
    }

    @Override
    public void onLongClick(View view, int position) {
        Log.e("Recycle View test", "view " + position + " long clicked!");
        adapter.removeItem(position);
    }

    public void onPostGet(List<HashMap<String, String>> list) {
        List<Lost_Item_Entity> item_list = this.adapter.getList();
        Log.e("lost list info", list + "");
        Log.e("lost item list info", item_list + "");
        for (int i = 0; i < list.size(); i++) {
            HashMap<String, String> data = list.get(i);
            String context = data.get("ps");
            Lost_Item_Entity entity = new Lost_Item_Entity();
            entity.initDefaultData(i);
            entity.setContext(context);
            entity.setImg(bg_array[i % 6]);
            entity.setTitle(data.get("title"));
            adapter.addItem(adapter.getItemCount(), entity);
        }
        this.refreshLayout.setRefreshing(false);
    }

    public void doRefresh() {
        Log.e("newsFragment info", "do refreshing!");
        this.adapter.resetList();
        new lost_net().execute("");
    }

    @Override
    public void onRefresh() {
        doRefresh();
    }

    public void isRefreshing(boolean Refreshing) {
        if (Refreshing) {
            this.refreshLayout.setRefreshing(true);
            doRefresh();
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction();
    }

    private class lost_net extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            onPostGet(hashMaps);
        }

        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            List<HashMap<String, String>> lost_list = null;
            try {
                lost_list = new NetTool().get_lost_list();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (lost_list != null) {
                return lost_list;
            }
            return null;
        }
    }


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}
