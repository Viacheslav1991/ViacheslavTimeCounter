package com.android.viacheslavtimecounter;

import android.os.Bundle;


import com.android.viacheslavtimecounter.model.DayStatisticListDoings;
import com.android.viacheslavtimecounter.model.DayStatisticListDoingsLab;

import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;


public class StatisticPagerActivity extends AppCompatActivity implements StatisticListFragment.Callbacks {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_pager);

        mViewPager = findViewById(R.id.statistic_view_pager);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int i) {
                DayStatisticListDoings list = DayStatisticListDoingsLab
                        .getDayStatisticListDoingsLab(getApplicationContext())
                        .getDayStatisticListDoings(i);
                return StatisticListFragment.newInstance(list.getDate());
            }

            @Override
            public int getCount() {
                return DayStatisticListDoingsLab
                        .getDayStatisticListDoingsLab(getApplicationContext())
                        .getSize();
            }
        });
        mViewPager.setCurrentItem(Objects.requireNonNull(mViewPager.getAdapter()).getCount()-1);
    }

    @Override
    public void onDateSelected(Integer i) {
        mViewPager.setCurrentItem(i);
    }

}
