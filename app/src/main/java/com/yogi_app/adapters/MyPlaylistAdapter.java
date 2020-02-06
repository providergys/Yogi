package com.yogi_app.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.yogi_app.fragments.AudioPlayList;
import com.yogi_app.fragments.SongsPlaylist;
import com.yogi_app.fragments.VedioPlaylist;

import java.util.ArrayList;
import java.util.List;

public class MyPlaylistAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private String[] tabTitles = new String[]{};

    public MyPlaylistAdapter(FragmentManager fm, String[] tabTitles) {
        super(fm);
        this.tabTitles = tabTitles;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                AudioPlayList tab1 = new AudioPlayList();
                return tab1;
            case 1:
                SongsPlaylist tab2 = new SongsPlaylist();
                return tab2;
            case 2:
                VedioPlaylist tab3 = new VedioPlaylist();
                return tab3;
            default:
                return mFragmentList.get(position);
        }
    }

    public void addFragment(Fragment fragment, String title, int position) {
        mFragmentList.add(position, fragment);
        mFragmentTitleList.add(position, title);
    }

    public void removeFragment(Fragment fragment, int position) {
        mFragmentList.remove(position);
        mFragmentTitleList.remove(position);
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}