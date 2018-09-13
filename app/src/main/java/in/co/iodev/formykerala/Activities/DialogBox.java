package in.co.iodev.formykerala.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import in.co.iodev.formykerala.R;

/**
 * Created by seby on 8/21/2018.
 */

public class DialogBox  extends Dialog {

        public String name;
        public Boolean status;
        public TextView Name;
        public JSONArray products;
        Button accept,decline;
        public Activity activity;
        private JSONObject items=new JSONObject();
        public DialogBox(Activity activity,String name,JSONArray products) {
            super(activity);
            this.name=name;

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialogcard);
            Name=findViewById(R.id.receiver_namw);
            accept=findViewById(R.id.accept);
            decline=findViewById(R.id.decline);
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    status=true;
                }
            });
            decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    status=false;
                }
            });
            Name.setText(name);

        }
       Boolean getStatus()
       {
           return status;
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
                view = getLayoutInflater().inflate(R.layout.donor_quantity_items,parent,false);
                holder = new ViewHolder1(view);
                view.setTag(holder);
            }
            else {
                holder = (ViewHolder1) view.getTag();
            }


            try {
                final ViewHolder1 finalHolder = holder;

                JSONObject object=products.getJSONObject(position);
                JSONObject object1=object.getJSONObject("Items");
                String product="",qty="";
                Iterator<String> iter = object1.keys();
                while (iter.hasNext()) {
                    String key = iter.next();

                    product+="\n"+key;

                    try {
                        qty+="\n"+object1.get(key);
                    } catch (JSONException e) {
                        // Something went wrong!
                    }}

                finalHolder.ProductName.setText(String.valueOf(object.getString("Name")));

                finalHolder.Quantity.setText(qty);
                finalHolder.Product.setText(product);
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
        TextView Quantity,Product;



        public ViewHolder1(View v) {
            ProductName = (TextView) v.findViewById(R.id.product_name);
            Quantity=v.findViewById(R.id.requested_quantity);
            Product=v.findViewById(R.id.products);






        }
    }
    }

