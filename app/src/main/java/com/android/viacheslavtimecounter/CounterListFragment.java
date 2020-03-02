package com.android.viacheslavtimecounter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.viacheslavtimecounter.model.DayStatisticListDoingsLab;
import com.android.viacheslavtimecounter.model.Doing;
import com.android.viacheslavtimecounter.model.DoingName;
import com.android.viacheslavtimecounter.model.DoingNameLab;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    private Callbacks mCallbacks;

    public interface Callbacks {
        void onDoingNameSelected(Doing doing);

        void onNewDoingName();

        void onChangeDoingName(String title);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_count_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_statistic:
//                DailyEmploymentListLab.getInstance(getActivity()).updateLists();
                Intent intent = new Intent(getActivity(), StatisticPagerActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_count_list, container, false);
        mRecyclerView = view.findViewById(R.id.count_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.onNewDoingName();
            }
        });
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
        private Doing mDoing;

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
            mDoing = DayStatisticListDoingsLab
                    .getDayStatisticListDoingsLab(getActivity())
                    .getDayStatisticListDoings(new MyCalendar())
                    .getDoing(mDoingName.getTitle()); //get today's doing(may be move to adapter?)
            if (mDoing != null) {
                mTotalTimeTextView.setText(TimeHelper.getTime(mDoing.getTotalTimeInt()));
            } else {
                mTotalTimeTextView.setText(TimeHelper.getTime(0));
            }
        }

        @Override
        public void onClick(View v) {
            if (mDoing == null) {
                mDoing = new Doing(mDoingName.getTitle(), mDoingName.getColor());
//                DoingLab.getDoingLab(getActivity()).addDoing(mDoing);
//                mDoing.setTotalTimeInt(100);
                DayStatisticListDoingsLab.getDayStatisticListDoingsLab(getActivity())
                        .getDayStatisticListDoings(new MyCalendar())
                        .addDoing(mDoing);
            }
            mCallbacks.onDoingNameSelected(mDoing);
            updateUI();
        }

        @Override
        public boolean onLongClick(View v) {
            Toast.makeText(getActivity(), "LongClicked", Toast.LENGTH_LONG).show();
            PopupMenu popupMenu = new PopupMenu(getActivity(), v);
            popupMenu.inflate(R.menu.doing_name_long_press_menu);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_item_delete:
                            Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_LONG).show();
                            DoingNameLab.getDoingNameLab(getActivity())
                                    .deleteDoingName(mDoingName);
                            updateUI();
                            break;
                        case R.id.menu_item_change:
                            Toast.makeText(getActivity(), "Changed", Toast.LENGTH_LONG).show();
//                            mCallbacks.onChangeItemEmployments(mItem.getTitle());
                            mCallbacks.onChangeDoingName(mDoingName.getTitle());
                            updateUI();
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });
            popupMenu.show();
            return false;
        }
    }
}
