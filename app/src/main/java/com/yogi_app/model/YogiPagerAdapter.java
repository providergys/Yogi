package com.yogi_app.model;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.yogi_app.R;
import com.yogi_app.activity.ContactUs;
import com.yogi_app.activity.YogiLibraryActivity;
import com.yogi_app.fragments.AudioBooksFragment;
import com.yogi_app.fragments.DharmaBooksFragment;
import com.yogi_app.fragments.DharmaQuizFragment;
import com.yogi_app.fragments.SongsFragment;
import com.yogi_app.fragments.VIdeoFragment;

import java.util.ArrayList;
import java.util.List;


public class YogiPagerAdapter extends FragmentStatePagerAdapter {
    YogiLibraryActivity context;
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private String[] tabTitles = new String[]{};

    public YogiPagerAdapter(FragmentManager fm, String[] tabTitle) {
        super(fm);
        this.tabTitles= tabTitle;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                DharmaBooksFragment tab1 = new DharmaBooksFragment();
                return tab1;
            case 1:
                AudioBooksFragment tab2 = new AudioBooksFragment();
                return tab2;
            case 2:
                SongsFragment tab3 = new SongsFragment();
                return tab3;
            case 3:
                DharmaQuizFragment tab4= new DharmaQuizFragment();
                return tab4;
            case 4:
                VIdeoFragment tab5=new VIdeoFragment();
                return tab5;
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