package in.co.iodev.formykerala.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.co.iodev.formykerala.Constants.Constants;
import in.co.iodev.formykerala.HTTPPostGet;
import in.co.iodev.formykerala.R;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class ReceiverRequirementsStatus extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;
    SharedPreferences sharedPref;
    String url= Constants.Get_all_cases;
    ArrayList Mainproducts,products;
    ListView product_status_list;
    String TimeIndex;
    String StringData;
    Product_Request_Adapter adapter;
    ImageView search_button;
    Button check_status;
    EditText item_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_requirements_status);
        sharedPref=getDefaultSharedPreferences(getApplicationContext());

/*
        if(sharedPref.getBoolean("Login",FALSE))
        {
            Toast.makeText(getApplicationContext(),"LOGGED IN ALREADY--REDIRECT",Toast.LENGTH_LONG).show();
        }*/
        TimeIndex=sharedPref.getString("TimeIndex","");

        product_status_list=findViewById(R.id.product_status_listview);
        adapter=new Product_Request_Adapter();
        new HTTPAsyncTask2().execute(url);
        search_button=findViewById(R.id.search_button);
        check_status=findViewById(R.id.check_status);
        item_search=findViewById(R.id.item_search);
        JSONObject timeindex=new JSONObject();
       /* try {
            timeindex.put("TimeIndex",TimeIndex);

        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        StringData=timeindex.toString();
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });
        check_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReceiverRequirementsStatus.this, AcceptorsView.class));

            }

        });

    }

    private void search() {
        if(!item_search.getText().toString().equals(""))
        {products.clear();
            for (int i=0;i<Mainproducts.size();i++)
            {
                if(Mainproducts.get(i).equals(item_search.getText().toString()))
                {
                    products.add(Mainproducts.get(i));

                }
            }}
        else {
            products.clear();
            products.addAll(Mainproducts);
        }
        product_status_list.setAdapter(adapter);

    }

    public void request(View view) {

    }

    public void view_items(View view) {
    }

    private class Product_Request_Adapter extends BaseAdapter {

        @Override
        public int getCount() {

            return products.size();
        }

        @Override
        public Object getItem(int position) {
            return products.get(position);
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
                holder.ProductName.setText(String.valueOf(products.get(position)));


            }catch (Exception e){
            }



            return view;
        }
    }
    private class ViewHolder1 {
        TextView ProductName,RequestedQuantity;



        public ViewHolder1(View v) {
            ProductName = (TextView) v.findViewById(R.id.product_name);
            RequestedQuantity = (TextView) v.findViewById(R.id.requested_quantity);








        }
    }

    private class HTTPAsyncTask2 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String response=null;
            // params comes from the execute() call: params[0] is the url.
            try {
                try {
                        response= HTTPPostGet.getJsonResponse(url,StringData);
                    Log.i("jisjoe",response.toString());
                    return response;
                } catch (Exception e) {
                    e.printStackTrace();
                    return "Error!";
                }
            } catch (Exception e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            JSONObject responseObject= null;
            try {
              JSONArray parentObject = new JSONObject(result).getJSONArray("Items");

                    products = new ArrayList<String>();
                    Mainproducts=new ArrayList<String>();
                    if (parentObject!= null) {
                        for (int i=0;i<parentObject.length();i++){
                            products.add(parentObject.getString(i));
                            Mainproducts.add(parentObject.getString(i));

                        }
                    }
                    Log.d("Responseitem",result.toString());
                    Log.d("Responseitem",products.toString());
                    product_status_list.setAdapter(adapter);



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
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}
