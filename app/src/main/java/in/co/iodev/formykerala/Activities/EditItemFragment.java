package in.co.iodev.formykerala.Activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
import java.util.HashMap;
import java.util.Map;

import in.co.iodev.formykerala.Constants.Constants;
import in.co.iodev.formykerala.HTTPGet;
import in.co.iodev.formykerala.HTTPPostGet;
import in.co.iodev.formykerala.R;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
    public class EditItemFragment extends Fragment {

        SharedPreferences sharedPref;
        String url= Constants.Get_Donation_items;
        String url2=Constants.Send_Donation_items;
        JSONArray Mainproducts,products;
        ListView product_request_list;
        String TimeIndex;
        String StringData;
        Product_Request_Adapter adapter;
        Boolean submit=false;
        Button submit_button;
        ImageView search_button;
        EditText item_search;
        JSONObject items;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_edit_items, container, false);
        }
        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            sharedPref=getDefaultSharedPreferences(getContext());

            TimeIndex=sharedPref.getString("TimeIndex","");

            items=new JSONObject();
            JSONObject timeindex=new JSONObject();
            try {
                timeindex.put("TimeIndex",TimeIndex);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            StringData=timeindex.toString();
            submit=false;
            Log.d("sj",StringData.toString());

            product_request_list=view.findViewById(R.id.donor_items_edit_listview);
            adapter=new Product_Request_Adapter();
            new HTTPAsyncTask2().execute(url);
            submit_button=view.findViewById(R.id.donor_items_submit);
            submit_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    submit=true;
                    JSONObject timeindex=new JSONObject();
                    try {
                        timeindex.put("TimeIndex",TimeIndex);
                        timeindex.put("DonationItems",items);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    StringData=timeindex.toString();
                    submit=true;
                    Log.d("sj",StringData.toString());

                    new HTTPAsyncTask2().execute(url2);
                }
            });

        }

//        private void search() {
//            if(!item_search.getText().toString().equals(""))
//            {products.clear();
//                for (int i=0;i<Mainproducts.size();i++)
//                {
//                    if(Mainproducts.get(i).equals(item_search.getText().toString()))
//                    {
//                        products.add(Mainproducts.get(i));
//
//                    }
//                }}
//            else {
//                products.clear();
//                products.addAll(Mainproducts);
//            }
//            product_request_list.setAdapter(adapter);
//            Log.d("Items",items.toString()+" "+products.toString()+" "+Mainproducts.toString());
//
//        }

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
                    view = getLayoutInflater().inflate(R.layout.donor_edit_items,parent,false);
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
                    finalHolder.ProductName.setText(String.valueOf(object.getString("name")));
                    items.put(object.getString("name"),object.get("number"));
                    finalHolder.Quantity.setText(String.valueOf(object.getString("number")));
                   /* if(items.has(holder.ProductName.getText().toString())) {
                        Log.d("Items",items.getString(holder.ProductName.getText().toString()));
                        holder.Quantity.setText(items.getString(holder.ProductName.getText().toString()));
                        holder.selected.setChecked(true);

                    }
                    else {
                        holder.selected.setChecked(FALSE);
                        holder.Quantity.setText("");
                    }*/

                    holder.Quantity.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {



                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            try {
                                items.remove(finalHolder.ProductName.getText().toString());
                                items.put(finalHolder.ProductName.getText().toString(),finalHolder.Quantity.getText().toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            try {
                                items.remove(finalHolder.ProductName.getText().toString());
                                items.put(finalHolder.ProductName.getText().toString(),finalHolder.Quantity.getText().toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                }catch (Exception e){
                }



                return view;
            }
        }
        private class ViewHolder1 {
            TextView ProductName;
            EditText Quantity;



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
                                response= HTTPPostGet.getJsonResponse(url2,StringData);
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
                       product_request_list.setAdapter(adapter);
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
    }
