package in.co.iodev.formykerala.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.co.iodev.formykerala.Constants.Constants;
import in.co.iodev.formykerala.Controllers.CheckInternet;
import in.co.iodev.formykerala.Controllers.HTTPPostGet;
import in.co.iodev.formykerala.Controllers.ProgressBarHider;
import in.co.iodev.formykerala.R;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class ReceiverRequirementsStatus extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;
    SharedPreferences sharedPref;
    String url= Constants.Get_Receivers_Requirement;
    JSONArray Mainproducts,products;
    ListView product_status_list;
    String TimeIndex;
    String StringData;
    Product_Request_Adapter adapter;
    ImageView search_button;
    Button check_status,logout;
    Boolean submit=false;
    EditText item_search;
    Context context;
    ImageView back;
    ProgressDialog progress;
    JSONObject object1;
    String status;
    String FireBaseRegistrationURL= Constants.FireBAseRegistrationURL,FireBaseRegData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.setAppLocale(MainActivity.languagePreferences.getString("LOCALE_CODE", null), getResources());
        setContentView(R.layout.activity_receiver_requirements_status);
        sharedPref=getDefaultSharedPreferences(getApplicationContext());
        logout=findViewById(R.id.logout);

/*
        if(sharedPref.getBoolean("Login",FALSE))
        {
            Toast.makeText(getApplicationContext(),"LOGGED IN ALREADY--REDIRECT",Toast.LENGTH_LONG).show();
        }*/

        TimeIndex=sharedPref.getString("TimeIndex","");
        if(!sharedPref.contains(TimeIndex+"FirstLogin"))
        {
            /*Toast.makeText(getApplicationContext(),"In",Toast.LENGTH_SHORT).show();*/
            final AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            String infoAlert = getString(R.string.info_alert);
            builder.setMessage(infoAlert)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })

            .show();
            sharedPref.edit().putBoolean(TimeIndex+"FirstLogin",TRUE).apply();
        }

        try{
            TimeIndex=sharedPref.getString("TimeIndex","");
            Log.d("TimeIndexDonor",TimeIndex);
            Log.d("FireBase Instance",String.valueOf(FirebaseInstanceId.getInstance().getToken()));
            JSONObject FireBaseINFO = new JSONObject();
            try {
                FireBaseINFO.put("TimeIndex", TimeIndex);
                FireBaseINFO.put("UserGroup", "Reciever");
                FireBaseINFO.put("PUSH_Token", FirebaseInstanceId.getInstance().getToken());
                FireBaseRegData=FireBaseINFO.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new FireBaseRegistration().execute(FireBaseRegistrationURL);
        }catch (Exception e){
            e.printStackTrace();
        }
        back=findViewById(R.id.back_button);

        try{
            Log.d("FireBase Instance",String.valueOf(FirebaseInstanceId.getInstance().getToken()));
        }catch (Exception e){
            e.printStackTrace();
        }
        product_status_list=findViewById(R.id.product_status_listview);
        adapter=new Product_Request_Adapter();

        search_button=findViewById(R.id.search_button);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    search();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(TimeIndex+"Login",FALSE);
                editor.remove("TimeIndex");
              /*  editor.putBoolean("Edited",FALSE);
                editor.putBoolean("EditedR",FALSE);
*/
                editor.commit();
                sharedPref.edit().apply();

                startActivity(new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();

            }

        });
        item_search=findViewById(R.id.item_search);
        item_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    search();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        check_status=findViewById(R.id.check_status);

        context=this;
        JSONObject timeindex=new JSONObject();
        try {
            timeindex.put("TimeIndex",TimeIndex);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringData=timeindex.toString();
        new HTTPAsyncTask2().execute(url);
        check_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReceiverRequirementsStatus.this, AcceptorsView.class));

            }

        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
    private void search() throws Exception {
        if(!item_search.getText().toString().equals(""))
        {products=new JSONArray();
        int j=0;
            for (int i=0;i<Mainproducts.length();i++)
            {     final JSONObject object=new JSONObject(String.valueOf(Mainproducts.getJSONObject(i)));
                if(object.getString("name").toLowerCase().contains(item_search.getText().toString()))
                {
                    products.put(j,object);
                    j++;

                }
            }}
        else {
            products=new JSONArray();
            products=Mainproducts;
        }
        product_status_list.setAdapter(adapter);
        Log.d("Items",products.toString()+" "+Mainproducts.toString());

    }

    public void request(View view) {

    }

    public void view_items(View view) {
    }
    private class Product_Request_Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return products.length();
        }

        @Override
        public Object getItem(int position) {
            Object o=null;
            try {
                o= products.get(position);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return o;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;
            ViewHolder1 holder = null;

            if(view == null) {
                view = getLayoutInflater().inflate(R.layout.product_status_list_item,parent,false);
                holder =new ViewHolder1(view);
                view.setTag(holder);
            }
            else {
                holder = (ViewHolder1) view.getTag();
            }


            try {
                final ViewHolder1 finalHolder = holder;
                final JSONObject object=new JSONObject(String.valueOf(products.getJSONObject(position)));
                Log.d("seby",object.toString());
                finalHolder.ProductName.setText(String.valueOf(object.getString("name")));
                finalHolder.Quantity.setText(String.valueOf(object.getString("number")));
                holder.Status.setText(status);
                   /* if(items.has(holder.ProductName.getText().toString())) {
                        Log.d("Items",items.getString(holder.ProductName.getText().toString()));
                        holder.Quantity.setText(items.getString(holder.ProductName.getText().toString()));
                        holder.selected.setChecked(true);

                    }
                    else {
                        holder.selected.setChecked(FALSE);
                        holder.Quantity.setText("");
                    }*/



            }catch (Exception e){
            }



            return view;
        }
    }
    private class ViewHolder1 {
        TextView ProductName;
        TextView Quantity;
        TextView Status;

        public ViewHolder1(View v) {
        ProductName = (TextView) v.findViewById(R.id.product_name);
        Quantity=v.findViewById(R.id.requested_quantity);
        Status=v.findViewById(R.id.status_item);






    }
}



    private class HTTPAsyncTask2 extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {
            String response="Network Error";
            // params comes from the execute() call: params[0] is the url.
            try {
                try {
                    if(!submit)
                        response= HTTPPostGet.getJsonResponse(url,StringData);
                    else
                        response= HTTPPostGet.getJsonResponse(url,StringData);
                    Log.d("sj",StringData.toString());
                    return response;
                } catch (Exception e) {
                    e.printStackTrace();
                    return "Error!";
                }
                finally {
                    progress.cancel();
                }
            } catch (Exception e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        @Override
        protected void onPreExecute() {
            CheckInternet CI=new CheckInternet();
            CI.isOnline(context);
            progress=new ProgressDialog(ReceiverRequirementsStatus.this);
            progress.setMessage("Loading...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.show();

        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            progress.cancel();
            JSONObject responseObject= null;
            try {
                if (!submit)
                {JSONArray parentObject = new JSONObject(result).getJSONArray("Items");
                    Log.d("sjt",result.toString());
                    object1=new JSONObject(result);
                    status=object1.getString("Status_Now");
                    products = new JSONArray();
                    Mainproducts=new JSONArray();
                    products=parentObject;
                    Mainproducts=parentObject;

                    Log.d("Responseitem",products.toString());
                    product_status_list.setAdapter(adapter);
                }
                else
                {Log.d("Responseitem",result);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean(TimeIndex+"Edited", TRUE);
                    editor.commit();


                    submit=false;


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }    }


    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        String toastText = getString(R.string.please_click_back_again_to_exit);
        Toast.makeText(getApplicationContext(), toastText,Toast.LENGTH_SHORT).show();

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
                    Log.d("Sending data",FireBaseRegData);
                    response= HTTPPostGet.getJsonResponse(urls[0],FireBaseRegData);
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
