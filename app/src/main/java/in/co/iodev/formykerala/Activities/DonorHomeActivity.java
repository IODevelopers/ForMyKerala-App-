package in.co.iodev.formykerala.Activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;

import in.co.iodev.formykerala.R;

public class DonorHomeActivity extends FragmentActivity {
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 3;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_home);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager)findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }




    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position)
            {
                case 0:

                    EditItemFragment tab1=new EditItemFragment();

                    return tab1;
                case 1:

                  EditQuantityFragment tab2=new EditQuantityFragment();
                    return tab2;
                case 2:

                   AcceptedItemFragment tab3=new AcceptedItemFragment();
                    return tab3;

                default: return null;

            }

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
