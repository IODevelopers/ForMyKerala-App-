package in.co.iodev.formykerala.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import in.co.iodev.formykerala.Constants.Constants;
import in.co.iodev.formykerala.Controllers.CheckInternet;
import in.co.iodev.formykerala.HTTPPostGet;
import in.co.iodev.formykerala.R;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static java.lang.Boolean.TRUE;

public class AcceptorsView extends AppCompatActivity {

    SharedPreferences sharedPref;
    String url= Constants.Get_Accepted_Request_Receiver;
    JSONArray Mainproducts,products;
    ListView product_status_list;
    String TimeIndex;
    String StringData;
    Product_Request_Adapter adapter;
    ImageView search_button;
    ImageView back;
    Button logout;
    Boolean submit=false;
    Context context;
    EditText item_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceptors_view);
        sharedPref=getDefaultSharedPreferences(getApplicationContext());
        context=this;
/*
        if(sharedPref.getBoolean("Login",FALSE))
        {
            Toast.makeText(getApplicationContext(),"LOGGED IN ALREADY--REDIRECT",Toast.LENGTH_LONG).show();
        }*/
        TimeIndex=sharedPref.getString("TimeIndex","");

        product_status_list=findViewById(R.id.product_status_listview);
        adapter=new Product_Request_Adapter();
        logout=findViewById(R.id.logout);
        back=findViewById(R.id.back_button);

        JSONObject timeindex=new JSONObject();
       try {
            timeindex.put("TimeIndex",TimeIndex);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringData=timeindex.toString();
        new HTTPAsyncTask2().execute(url);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));

            }

        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
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
                holder = new ViewHolder1(view);
                view.setTag(holder);
            }
            else {
                holder = (ViewHolder1) view.getTag();
            }


            try {
                final ViewHolder1 finalHolder = holder;
                final JSONObject object=new JSONObject(String.valueOf(products.getJSONObject(position)));
                Log.d("seby",object.toString());
                finalHolder.ProductName.setText(String.valueOf(object.getString("Name")));
                finalHolder.Quantity.setText(String.valueOf(object.getString("PhoneNumber")));
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



        public ViewHolder1(View v) {
            ProductName = (TextView) v.findViewById(R.id.product_name);
            Quantity=v.findViewById(R.id.requested_quantity);






        }
    }

    private class HTTPAsyncTask2 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String response=null;
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
                if (!submit)
                {JSONArray parentObject = new JSONObject(result).getJSONArray("Items");

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

                    editor.putBoolean("Edited", TRUE);
                    editor.commit();


                    submit=false;


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }    }


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AcceptorsView.this,ReceiverRequirementsStatus.class));
        AcceptorsView.this.finish();
        super.onBackPressed();
    }

}
