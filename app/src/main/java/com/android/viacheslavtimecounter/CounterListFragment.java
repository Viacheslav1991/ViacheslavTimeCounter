package com.android.viacheslavtimecounter;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.session.MediaSession;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.android.viacheslavtimecounter.model.StatisticDoingsLab;
import com.android.viacheslavtimecounter.model.Doing;
import com.android.viacheslavtimecounter.model.DoingName;
import com.android.viacheslavtimecounter.model.DoingNameLab;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// update statistic before opening of one!!!!!!!!!!!!!!!!!!!
public class CounterListFragment extends Fragment {
    private static final String ADAPTER_POSITION = "adapter_position";
    // Идентификатор уведомления
    private static final int NOTIFY_ID = 101;

    // Идентификатор канала
    private static String CHANNEL_ID = "Current doing channel";

    private RecyclerView mRecyclerView;
    private CountAdapter mCountAdapter;
    private FloatingActionButton fab;
    private Callbacks mCallbacks;
    private int mHolderPosition;
    private Activity mActivity;

    public interface Callbacks {
        void onDoingNameSelected(Doing doing);

        void onNewDoingName();

        void onChangeDoingName(String title);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
        mActivity = (Activity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onStart() {
        updateUI();
        super.onStart();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_count_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_statistic_day:
                StatisticDoingsLab.getStatisticDoingsLab(getActivity()).updateStatisticDates();
//                Intent intent = new Intent(getActivity(), StatisticPagerActivity.class);
                Intent intent = StatisticPagerActivity.newIntent(getActivity(), StatisticPagerActivity.TYPE_STATISTIC_DAY);
                startActivity(intent);
                return true;
            case R.id.show_statistic_week:
                intent = StatisticPagerActivity.newIntent(getActivity(), StatisticPagerActivity.TYPE_STATISTIC_WEEK);
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
        fab.setOnClickListener(v -> {
            mCallbacks.onNewDoingName();
        });


        if (LastStartedDoingPreferences.getStartedDoingID(getActivity()) != null) {
            Doing doing = StatisticDoingsLab
                    .getStatisticDoingsLab(getActivity())
                    .getDayDoings(new MyCalendar())
                    .getDoing(Objects.requireNonNull(LastStartedDoingPreferences.getStartedDoingID(mActivity)));
            showNotification(doing);
            mCallbacks.onDoingNameSelected(doing);
        }

        updateUI();
        return view;
    }

    public void updateUI() {
        mHolderPosition = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getInt(ADAPTER_POSITION, -1);
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
            mDoing = StatisticDoingsLab
                    .getStatisticDoingsLab(getActivity())
                    .getDayDoings(new MyCalendar())
                    .getDoing(mDoingName.getTitle()); //get today's doing(may be move to adapter?)
            if (mDoing != null) {
                mTotalTimeTextView.setText(TimeHelper.getTime(mDoing.getTotalTimeInt()));
            } else {
                mTotalTimeTextView.setText(TimeHelper.getTime(0));
            }
        }

        @Override
        public void onClick(View v) {
            if (mHolderPosition == getAdapterPosition()) {
                return;
            }
            PreferenceManager.getDefaultSharedPreferences(getActivity())
                    .edit()
                    .putInt(ADAPTER_POSITION, getAdapterPosition())
                    .apply();

            if (mDoing == null) {
                mDoing = new Doing(mDoingName.getTitle(), mDoingName.getColor());
                StatisticDoingsLab.getStatisticDoingsLab(getActivity())
                        .getDayDoings(new MyCalendar())
                        .addDoing(mDoing);
            }


            String dateStr = TimeHelper.getDateString(new MyCalendar());
            LastStartedDoingPreferences.setStartTime(getActivity(), System.currentTimeMillis()
                    , mDoing.getId(), mDoing.getTotalTimeInt(), dateStr);

            mCallbacks.onDoingNameSelected(mDoing);

            showNotification(mDoing);
            updateUI();
        }

        @Override
        public boolean onLongClick(View v) {
            PopupMenu popupMenu = new PopupMenu(getActivity(), v);
            popupMenu.inflate(R.menu.doing_name_long_press_menu);
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.menu_item_delete:
                        DoingNameLab.getDoingNameLab(getActivity())
                                .deleteDoingName(mDoingName);
                        updateUI();
                        break;
                    case R.id.menu_item_change:
                        mCallbacks.onChangeDoingName(mDoingName.getTitle());
                        updateUI();
                        break;
                    default:
                        break;
                }
                return false;
            });
            popupMenu.show();
            return false;
        }

    }

    private void showNotification(Doing doing) {
        Intent notificationIntent = new Intent(mActivity, mActivity.getClass());
        PendingIntent contentIntent = PendingIntent.getActivity(mActivity,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        RemoteViews remoteViews = new RemoteViews(mActivity.getPackageName(), R.layout.notification);
        remoteViews.setTextViewText(R.id.notificationTextView, "Custom notification text");
        remoteViews.setTextColor(R.id.notificationTextView, doing.getColor());
        remoteViews.setOnClickPendingIntent(R.id.root, contentIntent);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(mActivity)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOnlyAlertOnce(true)
                .setContentTitle(doing.getTitle())
                .setPriority(Notification.PRIORITY_MAX)
                .setWhen(LastStartedDoingPreferences.getStartTimeMillis(mActivity))
                .setUsesChronometer(true)
                .setOngoing(true)
                .setColor(doing.getColor())
//                .setCustomContentView(remoteViews)
//                .setStyle(new Notification.DecoratedCustomViewStyle())
                .setContentIntent(contentIntent);


//                .setColorized(true);
//                .setContent(remoteViews);

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(mActivity);
        // === Removed some obsoletes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }

        notificationManager.notify(NOTIFY_ID, builder.build());
    }

}
