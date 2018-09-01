package in.co.iodev.formykerala.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Locale;

import in.co.iodev.formykerala.Controllers.CheckInternet;
import in.co.iodev.formykerala.Controllers.HTTPGet;

import in.co.iodev.formykerala.R;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static in.co.iodev.formykerala.Constants.Constants.Get_App_Version;
import static java.lang.Boolean.FALSE;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
Button receiver,donor,help;
String url=Get_App_Version;
String appversion;
String TimeIndex;
LinearLayout role,updater;
TextView network;

TextView mTextView;
Spinner mSpinner;
public static SharedPreferences languagePreferences;
SharedPreferences.Editor editor;

Boolean noupdate=true,internet=true;
    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        languagePreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = languagePreferences.edit();
        String localeCode = languagePreferences.getString("LOCALE_CODE",null);
        if(localeCode != null){
            setAppLocale(languagePreferences.getString("LOCALE_CODE", null), getResources());
        }
        setContentView(R.layout.activity_main);

        sharedPref=getDefaultSharedPreferences(getApplicationContext());
        TimeIndex = sharedPref.getString("TimeIndex","");
        role = findViewById(R.id.role_selection);
        updater = findViewById(R.id.updater);
        network = findViewById(R.id.internet);
        new HTTPAsyncTask3().execute(url);

        mTextView = findViewById(R.id.tv_main);
        receiver = findViewById(R.id.role_receiver);
        donor = findViewById(R.id.role_Donor);
        help = findViewById(R.id.help);
        mSpinner =findViewById(R.id.spinner);

        if(localeCode == null) {
            receiver.setVisibility(View.GONE);
            donor.setVisibility(View.GONE);
            help.setVisibility(View.GONE);
            mTextView.setText("Choose Language\n(remember your selection will be final)");
        }else{
            mSpinner.setVisibility(View.GONE);
        }

        ArrayAdapter<CharSequence> mAdapter = ArrayAdapter.createFromResource(this, R.array.available_languages, android.R.layout.simple_spinner_item);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mAdapter);
        int initialSelectedPosition=mSpinner.getSelectedItemPosition();
        mSpinner.setSelection(initialSelectedPosition, false);
        mSpinner.setOnItemSelectedListener(this);
      }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        String spinnerText = adapterView.getItemAtPosition(position).toString();
        Toast.makeText(adapterView.getContext(), spinnerText, Toast.LENGTH_SHORT).show();
        if(spinnerText.equals("Malayalam")){
            editor.putString("LOCALE_CODE","ml");
            editor.commit();
            setAppLocale("ml", getResources());
        }else if(spinnerText.equals("English")){
            editor.putString("LOCALE_CODE","en");
            editor.commit();
            setAppLocale("en", getResources());
        }

        mTextView.setText(getString(R.string.select_your_role));
        receiver.setText(getString(R.string.receiver));
        donor.setText(R.string.donor);
        help.setText(R.string.help);
        receiver.setVisibility(View.VISIBLE);
        donor.setVisibility(View.VISIBLE);
        help.setVisibility(View.VISIBLE);
        mSpinner.setVisibility(View.GONE);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    public static void setAppLocale(String localeCode, Resources res){
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            conf.setLocale(new Locale(localeCode.toLowerCase()));
        }else{
            conf.locale = new Locale(localeCode.toLowerCase());
        }
        res.updateConfiguration(conf,dm);
    }

    public void receiver() {
        Intent intent = new Intent(MainActivity.this,ReceiverLogin.class);
        startActivity(intent);
        MainActivity.this.finish();
    }
    public void donor() {
        Intent intent = new Intent(MainActivity.this,DonorLogin.class);
        startActivity(intent);
        MainActivity.this.finish();
    }
    private void help_view() {
        Intent intent = new Intent(MainActivity.this,Help_view.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void redirect()
    {
        if(noupdate)
            if(sharedPref.getBoolean(TimeIndex+"Login",FALSE))
            {
                if(sharedPref.getBoolean(TimeIndex+"EditedR",FALSE)) {
                    startActivity(new Intent(getApplicationContext(), ReceiverRequirementsStatus.class)); //TO VIEW ADDED REQUESTS
                    MainActivity.this.finish();
                }
                else
                if(sharedPref.getBoolean(TimeIndex+"Edited",FALSE)){
                    startActivity(new Intent(getApplicationContext(),ReceiverSelectRequirement.class)); //TO SELECT ITEMS
                    MainActivity.this.finish();
                }
                else {
                    startActivity(new Intent(getApplicationContext(), ReceiverDetails.class)); //TO ADD NEW REQUEST
                    MainActivity.this.finish();
                }

            }
            else if(sharedPref.getBoolean(TimeIndex+"DLogin",FALSE))
            {
                if(sharedPref.getBoolean(TimeIndex+"DEditedR",FALSE)) {
                    startActivity(new Intent(getApplicationContext(), DonorHomeActivity.class)); //TO VIEW ADDED REQUESTS
                    MainActivity.this.finish();
                }
                else{
                    startActivity(new Intent(getApplicationContext(),DonorDetails.class)); //TO ADD NEW REQUEST
                    MainActivity.this.finish();
                }

            }
            else
                role.setVisibility(View.VISIBLE);
                updater.setVisibility(View.INVISIBLE);
    }

    private class HTTPAsyncTask3 extends AsyncTask<String, Void, String> {
        String response="Network Error";
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                try {
                        response= HTTPGet.getJsonResponse(url);

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
            role.setVisibility(View.INVISIBLE);
            updater.setVisibility(View.VISIBLE);
            CheckInternet CI=new CheckInternet();
            CI.isOnline(getApplication());
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            JSONObject responseObject= null;
            try {
                 Log.d("Responseitem1",result);
                PackageManager pm = getBaseContext().getPackageManager();
                PackageInfo pInfo = null;
                try {
                    pInfo =  pm.getPackageInfo(getApplicationContext().getPackageName(),0);

                } catch (PackageManager.NameNotFoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                appversion = pInfo.versionName;
                Log.d("Responseitem1",appversion);
                String update_version=null;
                if(result.equals("Error!"))
                {
                    internet=false;
                    role.setVisibility(View.INVISIBLE);
                    updater.setVisibility(View.INVISIBLE);
                    network.setVisibility(View.VISIBLE);
                }
                else
                    update_version=new JSONObject(response).getString("version");
                if(internet&&Double.parseDouble(appversion)<Double.parseDouble(update_version))
                {  role.setVisibility(View.INVISIBLE);
                    updater.setVisibility(View.INVISIBLE);
                    network.setVisibility(View.INVISIBLE);
                    final AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(MainActivity.this);
                    }
                    builder
                            .setMessage("Please update app to continue")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    noupdate=false;
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" +"in.co.iodev.formykerala")));
                                    System.exit(0);

                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    System.exit(0);

                                }
                            })
                            .setCancelable(false)
                            .show();

                }
                else
                {if (internet) {
                    noupdate = true;
                    redirect();
                    receiver.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(internet)
                            receiver();
                            else
                            {
                                role.setVisibility(View.INVISIBLE);
                                updater.setVisibility(View.INVISIBLE);
                                network.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    donor.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(internet)
                            donor();
                            else
                            {
                                role.setVisibility(View.INVISIBLE);
                                updater.setVisibility(View.INVISIBLE);
                                network.setVisibility(View.VISIBLE);
                            }

                        }
                    });
                    help.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(internet)
                                help_view();
                            else
                            {
                                role.setVisibility(View.INVISIBLE);
                                updater.setVisibility(View.INVISIBLE);
                                network.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }}

            } catch (Exception e) {
                e.printStackTrace();
            }    }


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }}
