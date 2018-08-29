package in.co.iodev.formykerala.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import in.co.iodev.formykerala.Controllers.CheckInternet;
import in.co.iodev.formykerala.Controllers.HTTPGet;

import in.co.iodev.formykerala.R;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static in.co.iodev.formykerala.Constants.Constants.Get_App_Version;
import static java.lang.Boolean.FALSE;

public class MainActivity extends AppCompatActivity {
Button receiver,donor,help;
String url=Get_App_Version;
String appversion;
    String TimeIndex;
LinearLayout role,updater;
TextView network;
Boolean noupdate=true,internet=true;
    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPref=getDefaultSharedPreferences(getApplicationContext());
        TimeIndex=sharedPref.getString("TimeIndex","");
        role = findViewById(R.id.role_selection);
        updater = findViewById(R.id.updater);
        network = findViewById(R.id.internet);
        new HTTPAsyncTask3().execute(url);

        receiver = findViewById(R.id.role_receiver);
        donor = findViewById(R.id.role_Donor);
        help=findViewById(R.id.help);
      }

    public void receiver() {
        startActivity(new Intent(MainActivity.this,ReceiverLogin.class));
        MainActivity.this.finish();
    }
    public void donor() {
        startActivity(new Intent(MainActivity.this,DonorLogin.class));
        MainActivity.this.finish();
    }
    private void help_view() {
        startActivity(new Intent(MainActivity.this,Help_view.class));
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
                else
                if(sharedPref.getBoolean(TimeIndex+"DEdited",FALSE)){
                    startActivity(new Intent(getApplicationContext(),DonorSelectItems.class)); //TO SELECT ITEMS
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
