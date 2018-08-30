package in.co.iodev.formykerala.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
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

/**
 * Created by seby on 8/30/2018.
 */

public class DonorEdit extends Fragment {

    SharedPreferences sharedPref;
    String url= Constants.Get_Item_list;
    String url2=Constants.Send_Donation_items;
    ArrayList Mainproducts,products;
    ListView product_request_list;
    String TimeIndex;
    String StringData;
    Product_Request_Adapter adapter;
    Boolean submit=false;
    Button submit_button,logout;
    ImageView search_button;
    ImageView back;
    EditText item_search;
    JSONObject items;
    Context context;
    ProgressBarHider hider;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_donor_select_items, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        sharedPref=getDefaultSharedPreferences(getContext());

        TimeIndex=sharedPref.getString("TimeIndex","");

        items=new JSONObject();
        try{
        JSONArray array=new JSONArray(getArguments().getString("products"));
        for (int i=0;i<array.length();i++)
        {
            final JSONObject object=new JSONObject(String.valueOf(array.getJSONObject(i)));
            items.put(object.getString("name"),object.getString("number"));
        }}
        catch (Exception e)
        {

        }
        context=getContext();
        product_request_list=view.findViewById(R.id.product_request_listview);
        adapter=new Product_Request_Adapter();
        new HTTPAsyncTask2().execute(url);
        submit_button=view.findViewById(R.id.submit_button);
        search_button=view.findViewById(R.id.search_button);
        item_search=view.findViewById(R.id.item_search);
        back=view.findViewById(R.id.back_button);
        logout=view.findViewById(R.id.logout);
        hider=new ProgressBarHider(submit_button.getRootView(),submit_button);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(items.length()!=0)
                { JSONObject timeindex=new JSONObject();
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
                    new HTTPAsyncTask2().execute(url);}
                else

                {
                    hider.hide();
                    Toast.makeText(getContext(),"Please a Choose a Requirement",Toast.LENGTH_SHORT).show();            }
            }
        });
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });
        item_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                search();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
       logout.setVisibility(View.GONE);}

    private void search() {
        if(!item_search.getText().toString().equals(""))
        {products.clear();
            for (int i=0;i<Mainproducts.size();i++)
            {
                if(Mainproducts.get(i).toString().toLowerCase().contains(item_search.getText().toString()))
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
                        }
                        else
                        {
                            items.remove(finalHolder.ProductName.getText().toString());
                        }}
                        catch (Exception e){

                        }

                    }
                });
                holder.Quantity.addTextChangedListener(new TextWatcher()  {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        items.remove(finalHolder.ProductName.getText().toString());
                        try {
                            items.put(finalHolder.ProductName.getText().toString(),finalHolder.Quantity.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        finalHolder.selected.setChecked(FALSE);


                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        items.remove(finalHolder.ProductName.getText().toString());
                        try {
                            items.put(finalHolder.ProductName.getText().toString(),finalHolder.Quantity.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        finalHolder.selected.setChecked(FALSE);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        items.remove(finalHolder.ProductName.getText().toString());
                        try {
                            items.put(finalHolder.ProductName.getText().toString(),finalHolder.Quantity.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        finalHolder.selected.setChecked(TRUE);
                        Log.d("sjt2",items.toString());
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
                    editor.putBoolean(TimeIndex+"DEditedR", TRUE);
                    editor.apply();
                    Intent intent = new Intent(getActivity(), DonorHomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);


                    submit=false;

                }


            } catch (JSONException e) {
                e.printStackTrace();

            }
        }




    }

}

