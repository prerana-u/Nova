package com.example.project;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class SliderAdapter  extends FragmentPagerAdapter {

    public SliderAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new Slide1();
        }
        else if (position == 1) {
            return new Slide2();
        }
        else if (position == 2) {
            return new Slide3();
        }
        else {
            return new Slide4();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
