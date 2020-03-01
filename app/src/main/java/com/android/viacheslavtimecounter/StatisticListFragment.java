package com.android.viacheslavtimecounter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.viacheslavtimecounter.model.DayStatisticListDoings;
import com.android.viacheslavtimecounter.model.DayStatisticListDoingsLab;
import com.android.viacheslavtimecounter.model.Doing;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class StatisticListFragment extends Fragment {
    private static final String ARG_DAY_LIST_DATE = "day_list_date";

    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;


    private RecyclerView mRecyclerView;
    private Button mDateButton;
    private Callbacks mCallbacks;
    private DayStatisticListDoings mDayStatisticListDoings;

    public interface Callbacks {
         void onDateSelected(Integer i);
    }

    public static StatisticListFragment newInstance(Calendar date) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_DAY_LIST_DATE, date);

        StatisticListFragment fragment = new StatisticListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistic_list, container, false);

        mDateButton = view.findViewById(R.id.select_date_button);
        mRecyclerView = view.findViewById(R.id.statistic_recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Calendar date = (Calendar) getArguments().getSerializable(ARG_DAY_LIST_DATE);
        mDateButton.setText(TimeHelper.getDateString(date));
        mDateButton.setOnClickListener(v -> {
            FragmentManager manager = getFragmentManager();
            DatePickerFragment dialog = new DatePickerFragment();
            dialog.setTargetFragment(StatisticListFragment.this, REQUEST_DATE);
            dialog.show(manager, DIALOG_DATE);
        });

        mDayStatisticListDoings = DayStatisticListDoingsLab.getDayStatisticListDoingsLab(getActivity())
                .getDayStatisticListDoings(date);
        mRecyclerView.setAdapter(new LineAdapter(mDayStatisticListDoings.getDoings()));     //change!
        return view;
    }

    private class LineAdapter extends RecyclerView.Adapter<StatisticListFragment.LineHolder> {
        private List<Doing> mDoings;

        //Change!

        public LineAdapter(List<Doing> doings) {
            mDoings = doings;
        }

        @NonNull
        @Override
        public StatisticListFragment.LineHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_count,
                    viewGroup, false);

            return new StatisticListFragment.LineHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull LineHolder lineHolder, int i) {
            Doing doing = mDoings.get(i);
            lineHolder.bind(doing);
        }

        @Override
        public int getItemCount() {
            return mDoings.size();
        }
    }

    private class LineHolder extends RecyclerView.ViewHolder {
        private Doing mDoing;

        private TextView mTitleTextView;
        private TextView mTotalTimeTextView;
        private View mView;

        public LineHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            mTitleTextView = itemView.findViewById(R.id.employment_title);
            mTotalTimeTextView = itemView.findViewById(R.id.employment_time_text_view);
        }

        public void bind(Doing doing) {
            mDoing = doing;
            mView.setBackgroundColor(mDoing.getColor());
            mTitleTextView.setText(doing.getTitle());
            mTotalTimeTextView.setText(TimeHelper.getTime(doing.getTotalTimeInt()));
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);


            for (int i = 0; i < DayStatisticListDoingsLab
                    .getDayStatisticListDoingsLab(getActivity())
                    .getSize(); i++) {
                DayStatisticListDoings list = DayStatisticListDoingsLab
                        .getDayStatisticListDoingsLab(getActivity())
                        .getDayStatisticListDoings(i);
                if (TimeHelper.compareDate(list.getDate(), calendar)) {
                    mCallbacks.onDateSelected(i);
                }
            }

        }
    }
}
