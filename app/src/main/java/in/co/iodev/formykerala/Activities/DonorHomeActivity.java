package in.co.iodev.formykerala.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import in.co.iodev.formykerala.Constants.Constants;
import in.co.iodev.formykerala.Controllers.CheckInternet;
import in.co.iodev.formykerala.Controllers.HTTPPostGet;
import in.co.iodev.formykerala.R;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static java.lang.Boolean.FALSE;

public class DonorHomeActivity extends FragmentActivity {
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 3;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    Context context;
    SharedPreferences sharedPref;
    String TimeIndex,FireBaseRegistrationURL= Constants.FireBAseRegistrationURL,StringData;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_home);
        sharedPref=getDefaultSharedPreferences(getApplicationContext());
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager)findViewById(R.id.container);
        context=this;
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabs);
        try{
            TimeIndex=sharedPref.getString("TimeIndex","");
            Log.d("TimeIndexDonor",TimeIndex);
            Log.d("FireBase Instance",String.valueOf(FirebaseInstanceId.getInstance().getToken()));
            JSONObject FireBaseINFO = new JSONObject();
            try {
                FireBaseINFO.put("TimeIndex", TimeIndex);
                FireBaseINFO.put("UserGroup", "Donor");
                FireBaseINFO.put("PUSH_Token", FirebaseInstanceId.getInstance().getToken());
                StringData=FireBaseINFO.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new FireBaseRegistration().execute(FireBaseRegistrationURL);
        }catch (Exception e){
            e.printStackTrace();
        }
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
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            SharedPreferences sharedPref=getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = sharedPref.edit();
             editor.remove("TimeIndex");
            editor.commit();
            sharedPref.edit().apply();

            startActivity(new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
    private class FireBaseRegistration extends AsyncTask<String, Void, String> {
        String response="Network Error";

        @Override
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                try {
                    Log.d("Sending data",StringData);
                    response= HTTPPostGet.getJsonResponse(urls[0],StringData);
                    Log.i("FireBase response",response.toString());
                    return response;
                } catch (Exception e) {
                    e.printStackTrace();
                    return "Error!";
                }
            } catch (Exception e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        @Override
        protected void onPreExecute() {
            CheckInternet CI=new CheckInternet();
            CI.isOnline(context);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            JSONObject responseObject= null;
            try {
                responseObject = new JSONObject(response);
                Log.d("FireBaseRegistration",responseObject.getString("Message"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }
}
