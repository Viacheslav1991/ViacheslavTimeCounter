package com.android.viacheslavtimecounter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import com.android.viacheslavtimecounter.model.StatisticDoingsLab;
import com.android.viacheslavtimecounter.model.StatisticList;

import java.util.List;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;


public class StatisticPagerActivity extends AppCompatActivity implements StatisticListFragment.Callbacks {

    private static final String EXTRA_STATISTIC_TYPE = "com.android.viacheslavtimecounter.statistictype";
    static final int TYPE_STATISTIC_DAY = 0;
    static final int TYPE_STATISTIC_WEEK = 1;

    private ViewPager mViewPager;
    private List<StatisticList> mStatisticLists;

    public static Intent newIntent(Context packageContext, int statisticType) {
        Intent intent = new Intent(packageContext, StatisticPagerActivity.class);
        intent.putExtra(EXTRA_STATISTIC_TYPE, statisticType);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_pager);
        mViewPager = findViewById(R.id.statistic_view_pager);
        int typeOfList = (int) getIntent().getSerializableExtra(EXTRA_STATISTIC_TYPE);
        if (typeOfList == TYPE_STATISTIC_DAY) {
            mStatisticLists = StatisticDoingsLab.getStatisticDoingsLab(getApplicationContext()).getDays();
        } else if (typeOfList == TYPE_STATISTIC_WEEK) {
            mStatisticLists = StatisticDoingsLab.getStatisticDoingsLab(getApplicationContext()).getWeeks();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int i) {
                return StatisticListFragment.newInstance(mStatisticLists.get(i).getDate(), mStatisticLists.get(i));
            }

            @Override
            public int getCount() {
                return mStatisticLists.size();
            }
        });
        mViewPager.setCurrentItem(Objects.requireNonNull(mViewPager.getAdapter()).getCount() - 1);
    }

    @Override
    public void onDateSelected(Integer i) {
        mViewPager.setCurrentItem(i);
    }

}
