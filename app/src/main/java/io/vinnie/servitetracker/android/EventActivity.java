package io.vinnie.servitetracker.android;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import io.vinnie.servitetracker.android.fragments.EventTotalsFragment;
import io.vinnie.servitetracker.android.fragments.EventLogFragment;

public class EventActivity extends Activity implements ActionBar.TabListener, ViewPager.OnPageChangeListener {

    private ViewPager viewPager;
    private EventPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String eventId = getIntent().getStringExtra("eventId");
        String name = getIntent().getStringExtra("name");

        setTitle(name);

        viewPager = new ViewPager(this);
        viewPager.setId(R.id.viewpager);
        setContentView(viewPager);

        adapter = new EventPagerAdapter(getFragmentManager());
        adapter.tallyFragment = EventTotalsFragment.newInstance(eventId, name);
        adapter.logFragment = EventLogFragment.newInstance(eventId, name);

        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(this);

        ActionBar ab = getActionBar();
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ab.addTab(ab.newTab().setText(R.string.tally).setTabListener(this));
        ab.addTab(ab.newTab().setText(R.string.log).setTabListener(this));
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        getActionBar().setSelectedNavigationItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class EventPagerAdapter extends FragmentPagerAdapter {

        private EventTotalsFragment tallyFragment;
        private EventLogFragment logFragment;

        public EventPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return tallyFragment;
                case 1:
                    return logFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
