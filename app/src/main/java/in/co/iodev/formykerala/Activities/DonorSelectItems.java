package in.co.iodev.formykerala.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import in.co.iodev.formykerala.Controllers.CheckInternet;
import in.co.iodev.formykerala.Controllers.HTTPGet;
import in.co.iodev.formykerala.Controllers.HTTPPostGet;
import in.co.iodev.formykerala.Controllers.ProgressBarHider;
import in.co.iodev.formykerala.R;
import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class DonorSelectItems extends AppCompatActivity {

    SharedPreferences sharedPref;
    String url= Constants.Get_Item_list;
    String url2=Constants.Send_Donation_items;
    ArrayList Mainproducts,products;
    ListView product_request_list;
    String TimeIndex;
    String StringData;
    Product_Request_Adapter adapter;
    Boolean submit=false;
    Button submit_button;
    ImageView search_button;
    ImageView back;
    EditText item_search;
    JSONObject items;
    Context context;
    ProgressBarHider hider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_select_items);
        sharedPref=getDefaultSharedPreferences(getApplicationContext());

        TimeIndex=sharedPref.getString("TimeIndex","");

        items=new JSONObject();
        context=this;
        product_request_list=findViewById(R.id.product_request_listview);
        adapter=new Product_Request_Adapter();
        new HTTPAsyncTask2().execute(url);
        submit_button=findViewById(R.id.submit_button);
        search_button=findViewById(R.id.search_button);
        item_search=findViewById(R.id.item_search);
        back=findViewById(R.id.back_button);
        hider=new ProgressBarHider(submit_button.getRootView(),submit_button);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject timeindex=new JSONObject();
                try {
                    timeindex.put("TimeIndex",TimeIndex);
                    timeindex.put("DonationItems",items);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                StringData=timeindex.toString();
                Log.d("seby",StringData.toString());
                submit=true;
                hider.show();
                new HTTPAsyncTask2().execute(url);
            }
        });
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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
        product_request_list.setAdapter(adapter);
        Log.d("Items",items.toString()+" "+products.toString()+" "+Mainproducts.toString());

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
                view = getLayoutInflater().inflate(R.layout.product_select_list_item,parent,false);
                holder = new ViewHolder1(view);
                view.setTag(holder);
            }
            else {
                holder = (ViewHolder1) view.getTag();
            }


            try {
                final ViewHolder1 finalHolder = holder;
                holder.ProductName.setText(String.valueOf(products.get(position)));
                if(items.has(holder.ProductName.getText().toString())) {
                    Log.d("Items",items.getString(holder.ProductName.getText().toString()));
                    holder.Quantity.setText(items.getString(holder.ProductName.getText().toString()));
                    holder.selected.setChecked(true);

                }
                else {
                    holder.selected.setChecked(FALSE);
                    holder.Quantity.setText("");
                }
                holder.selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        try
                        {if(isChecked)
                        {
                            items.put(finalHolder.ProductName.getText().toString(),finalHolder.Quantity.getText().toString());

                        }
                        else
                        {
                            items.remove(finalHolder.ProductName.getText().toString());
                        }}
                        catch (Exception e){

                        }

                    }
                });
                holder.Quantity.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        finalHolder.selected.setChecked(FALSE);

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        finalHolder.selected.setChecked(FALSE);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                        finalHolder.selected.setChecked(TRUE);

                    }
                });
                if(finalHolder.Quantity.getText().toString().equals(""))
                {
                    finalHolder.selected.setChecked(FALSE);
                }

            }catch (Exception e){
            }



            return view;
        }
    }
    private class ViewHolder1 {
        TextView ProductName;
        CheckBox selected;
        EditText Quantity;



        public ViewHolder1(View v) {
            ProductName = (TextView) v.findViewById(R.id.product_name);
            selected=v.findViewById(R.id.check_product);
            Quantity=v.findViewById(R.id.edit_quantity);






        }
    }

    private class HTTPAsyncTask2 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String response="Network Error";
            // params comes from the execute() call: params[0] is the url.
            try {
                try {

                    if (!submit)
                        response= HTTPGet.getJsonResponse(urls[0]);
                    else
                        response= HTTPPostGet.getJsonResponse(url2,StringData);
                    Log.i("jisjoe",response.toString());
                    return response;
                } catch (Exception e) {
                    e.printStackTrace();
                    return "Error!";
                }
                finally {
                    hider.hide();
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
                hider.hide();
                if (!submit)
                {JSONArray parentObject = new JSONObject(result).getJSONArray("Items");

                    products = new ArrayList<String>();
                    Mainproducts=new ArrayList<String>();
                    if (parentObject!= null) {
                        for (int i=0;i<parentObject.length();i++){
                            products.add(parentObject.getString(i));
                            Mainproducts.add(parentObject.getString(i));

                        }
                    }

                    Log.d("Responseitem",products.toString());
                    product_request_list.setAdapter(adapter);}
                else
                {Log.d("Responseitem",result);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("DEditedR", TRUE);
                    editor.apply();
                    Intent intent = new Intent(DonorSelectItems.this, DonorHomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);

                    submit=false;
                   DonorSelectItems.this.finish();

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }    }


    }
    boolean doubleBackToExitPressedOnce = false;

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
