package com.android.viacheslavtimecounter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.viacheslavtimecounter.model.DayStatisticListDoingsLab;
import com.android.viacheslavtimecounter.model.Doing;
import com.android.viacheslavtimecounter.model.DoingName;
import com.android.viacheslavtimecounter.model.DoingNameLab;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.GregorianCalendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
// update statistic before opening of one!!!!!!!!!!!!!!!!!!!
public class CounterListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private CountAdapter mCountAdapter;
    private FloatingActionButton fab;
//    private Callbacks mCallbacks;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_count_list, container, false);
        mRecyclerView = view.findViewById(R.id.count_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fab = view.findViewById(R.id.fab);
//        fab.setOnClickListener(view1 -> mCallbacks.onNewItemEmployments());
        updateUI();
        return view;
    }

    public void updateUI() {
        List<DoingName> names = DoingNameLab.getDoingNameLab(getActivity()).getDoingNames();
        if (mCountAdapter == null) {
            mCountAdapter = new CountAdapter(names);
            mRecyclerView.setAdapter(mCountAdapter);
        } else {
            mCountAdapter.setNames(names);
            mCountAdapter.notifyDataSetChanged();
        }
    }


    private class CountAdapter extends RecyclerView.Adapter<CountHolder> {
        private List<DoingName> mDoingNames;

        public CountAdapter(List<DoingName> doingNames) {
            mDoingNames = doingNames;
        }

        @NonNull
        @Override
        public CountHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_count,
                    parent, false);

            return new CountHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CountHolder holder, int position) {
            holder.bind(mDoingNames.get(position));
        }

        @Override
        public int getItemCount() {
            return mDoingNames.size();
        }

        public void setNames(List<DoingName> names) {
            mDoingNames = names;
        }
    }

    private class CountHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView mTitleTextView;
        private TextView mTotalTimeTextView;
        private View mView;
        private DoingName mDoingName;

        public CountHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            mView = itemView;
            mTitleTextView = itemView.findViewById(R.id.employment_title);
            mTotalTimeTextView = itemView.findViewById(R.id.employment_time_text_view);
        }

        public void bind(DoingName doingName) {
            mDoingName = doingName;
            mView.setBackgroundColor(doingName.getColor());
            mTitleTextView.setText(mDoingName.getTitle());
            Doing doing = DayStatisticListDoingsLab
                    .getDayStatisticListDoingsLab(getActivity())
                    .getDayStatisticListDoings(new GregorianCalendar())
                    .getDoing(mDoingName.getTitle()); //get today's doing(may be move to adapter?)
            if (doing != null) {
                mTotalTimeTextView.setText(TimeHelper.getTime(doing.getTotalTimeInt()));
            } else {
                mTotalTimeTextView.setText(TimeHelper.getTime(0));
            }
        }

        @Override
        public void onClick(View v) {
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }
}
