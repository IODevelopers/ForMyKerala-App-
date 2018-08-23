package in.co.iodev.formykerala.Activities;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import in.co.iodev.formykerala.Constants.Constants;
import in.co.iodev.formykerala.Controllers.CheckInternet;
import in.co.iodev.formykerala.Controllers.HTTPPostGet;
import in.co.iodev.formykerala.Controllers.ProgressBarHider;
import in.co.iodev.formykerala.R;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
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
        Context context;
        ProgressBarHider hider;
        int count=0;

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
            context=this.getContext();
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
            new HTTPAsyncTask1().execute(url);
            submit_button=view.findViewById(R.id.donor_items_submit);
            hider=new ProgressBarHider(submit_button.getRootView(),submit_button);
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
                    Log.d("sjt",StringData.toString());

                    hider.show();
                    new HTTPAsyncTask1().execute(url2);
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
                    if(items.has(object.getString("name")))
                    { //finalHolder.Quantity.setText(items.getString(object.getString("name")));
                    }
                    else
                        finalHolder.Quantity.setText(object.getString("number"));
                   /* if(items.has(holder.ProductName.getText().toString())) {
                        Log.d("Items",items.getString(holder.ProductName.getText().toString()));
                        holder.Quantity.setText(items.getString(holder.ProductName.getText().toString()));
                        holder.selected.setChecked(true);

                    }
                    else {
                        holder.selected.setChecked(FALSE);
                        holder.Quantity.setText("");
                    }*/
                    holder.Quantity.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                    holder.Quantity.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {



                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            try {
                                items.remove(finalHolder.ProductName.getText().toString());
                                Log.d("sjt",items.toString());
                                items.put(finalHolder.ProductName.getText().toString(),finalHolder.Quantity.getText().toString());
                                Log.d("sjt2",items.toString());

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            try {
                                items.remove(finalHolder.ProductName.getText().toString());
                                Log.d("sjt3",items.toString());

                                items.put(finalHolder.ProductName.getText().toString(),finalHolder.Quantity.getText().toString());
                                Log.d("sjt4",items.toString());

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

        private class HTTPAsyncTask1 extends AsyncTask<String, Void, String> {
            String response="Network Error";
            @Override
            protected String doInBackground(String... urls) {

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

                        products = new JSONArray();
                        Mainproducts=new JSONArray();
                       products=parentObject;
                       Mainproducts=parentObject;

                        Log.d("Responseitem",products.toString());
                       product_request_list.setAdapter(adapter);
                        }
                    else
                    {Log.d("Responseitem",result);
                       JSONObject jsonObject=new JSONObject(response);
                       if(jsonObject.getString("Message").equals("Success")) {
                           SharedPreferences.Editor editor = sharedPref.edit();

                           editor.putBoolean("Edited", TRUE);
                           editor.commit();
                       }
                        Toast.makeText(getContext(),jsonObject.getString("Message"),Toast.LENGTH_SHORT).show();
                        submit=false;


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }    }


        }
    }
