package in.co.iodev.formykerala.Activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.design.widget.TabLayout;

import in.co.iodev.formykerala.R;

public class counter extends Fragment {
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 4;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;




    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_donor_home, container, false);




        return rootView;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager = (ViewPager) view.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }




    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
           /* switch (position)
            {
                case 0:

                  *//*  tokens tab1=new tokens();*//*

                    return tab1;
                case 1:

                   *//* jobs tab2=new jobs();*//*
                    return tab2;
                case 2:

                    *//*search tab3=new search();*//*
                    return tab3;

                default: return null;

            }
*/           return null;

        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return NUM_PAGES;
        }

        @Override
        public String getPageTitle(int position) {
            // Show 3 total pages.
            switch (position)
            {
                case 0:
                    return "tokens";
                case 1:
                    return "jobs";
                case 2:return "search";

                default: return null;

            }
        }
    }
    public void onPageChange(ViewPager viewPager)
    {}

}
