package com.android.viacheslavtimecounter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.viacheslavtimecounter.model.DayDoings;
import com.android.viacheslavtimecounter.model.StatisticDoingsLab;
import com.android.viacheslavtimecounter.model.Doing;
import com.android.viacheslavtimecounter.model.StatisticList;
import com.android.viacheslavtimecounter.model.WeekDoings;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.ContentValues.TAG;
import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.WEEK_OF_YEAR;

public class StatisticListFragment extends Fragment {
    private static final String ARG_DAY_LIST_DATE = "day_list_date";
    private static final String ARG_STATISTIC_LIST = "statistic_list";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;


    private RecyclerView mRecyclerView;
    private Button mDateButton;
    private Callbacks mCallbacks;
    private StatisticList mStatisticList;

    public interface Callbacks {
        void onDateSelected(Integer i);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    public static StatisticListFragment newInstance(Calendar date, StatisticList statisticList) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DAY_LIST_DATE, date);
        args.putSerializable(ARG_STATISTIC_LIST, statisticList);

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
        Log.i(TAG, "onCreateView: " + date.get(Calendar.DAY_OF_MONTH));

        mStatisticList = (StatisticList) getArguments().getSerializable(ARG_STATISTIC_LIST);
        if (mStatisticList instanceof DayDoings) {
            mDateButton.setText(TimeHelper.getDateString(date));
        } else if (mStatisticList instanceof WeekDoings) {
            mDateButton.setText(TimeHelper.getWeekDateString(date));
        }

        mDateButton.setOnClickListener(v -> {
            FragmentManager manager = getFragmentManager();
            DatePickerFragment dialog = new DatePickerFragment();
            dialog.setTargetFragment(StatisticListFragment.this, REQUEST_DATE);
            dialog.show(manager, DIALOG_DATE);
        });

        mRecyclerView.setAdapter(new LineAdapter(mStatisticList.getDoings()));     //change!

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
            if (mStatisticList instanceof DayDoings) {
                for (int i = 0; i < StatisticDoingsLab.getStatisticDoingsLab(getActivity()).getDays().size(); i++) {
                    DayDoings list = StatisticDoingsLab
                            .getStatisticDoingsLab(getActivity())
                            .getDayStatisticListDoings(i);
                    if (TimeHelper.compareDate(list.getDate(), calendar)) {
                        mCallbacks.onDateSelected(i);
                    }
                }
            } else if (mStatisticList instanceof WeekDoings) {
                for (int i = 0; i < StatisticDoingsLab.getStatisticDoingsLab(getActivity()).getWeeks().size(); i++) {
                    WeekDoings weekDoings = (WeekDoings) StatisticDoingsLab
                            .getStatisticDoingsLab(getActivity())
                            .getWeeks().get(i);
                    if (weekDoings.getDate().get(WEEK_OF_YEAR)==calendar.get(WEEK_OF_YEAR)&&
                            calendar.get(DAY_OF_WEEK) >= calendar.getFirstDayOfWeek() &&
                            calendar.get(DAY_OF_WEEK) <= calendar.getFirstDayOfWeek() + 6) {
                        mCallbacks.onDateSelected(i);
                    }
                }
//                mCallbacks.onDateSelected(0);
            }

           /* for (int i = 0; i < StatisticDoingsLab
                    .getStatisticDoingsLab(getActivity())
                    .getSize(); i++) {
                DayDoings list = StatisticDoingsLab
                        .getStatisticDoingsLab(getActivity())
                        .getDayStatisticListDoings(i);
                if (TimeHelper.compareDate(list.getDate(), calendar)) {
                    mCallbacks.onDateSelected(i);
                }
            }*/

        }
    }
}
