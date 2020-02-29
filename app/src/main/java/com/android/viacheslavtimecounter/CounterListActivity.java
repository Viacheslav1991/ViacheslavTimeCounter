package com.android.viacheslavtimecounter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.android.viacheslavtimecounter.model.Doing;

public class CounterListActivity extends AppCompatActivity implements CounterListFragment.Callbacks{

    private static final String NEW_DOING_NAME = "new_doing_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new CounterListFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    @Override
    public void onDoingNameSelected(Doing doing) {
        Fragment counterFragment = CounterFragment.newInstance(doing.getId());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.counter_container, counterFragment)
                .commit();
    }

    @Override
    public void onNewDoingName() {
        FragmentManager manager = getSupportFragmentManager();
        AddDoingNameFragment dialog = new AddDoingNameFragment();
        dialog.show(manager, NEW_DOING_NAME);
    }
}
