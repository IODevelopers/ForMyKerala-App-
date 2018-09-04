package in.co.iodev.formykerala.Activities;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.sql.Time;
import java.util.Iterator;

import in.co.iodev.formykerala.Constants.Constants;
import in.co.iodev.formykerala.Controllers.CheckInternet;
import in.co.iodev.formykerala.Controllers.HTTPPostGet;
import in.co.iodev.formykerala.R;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;


public class EditQuantityFragment_New extends Fragment {

    SharedPreferences sharedPref;
    String url= Constants.Get_Biased_Request;
    String url2=Constants.Accept_Request;
    JSONArray Mainproducts,products;
    ListView product_request_list;
    String TimeIndex;
    String StringData;
    JSONObject LastIndex;
    Product_Request_Adapter adapter;
    Boolean submit=false;
    Button submit_button;
    ImageView search_button;
    EditText item_search;
    JSONObject items;

    private ProgressBar progressBar;

    ProgressDialog progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_edit_quantity, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPref=getDefaultSharedPreferences(getContext());
        TimeIndex=sharedPref.getString("TimeIndex","");
        if(sharedPref.getBoolean(TimeIndex+"FirstLogin",FALSE))
        {
            /*Toast.makeText(getApplicationContext(),"In",Toast.LENGTH_SHORT).show();*/
            final AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(getContext());
            }
            String infoAlert = getString(R.string.info_alert);
            builder.setMessage(infoAlert)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })

                    .show();
            sharedPref.edit().putBoolean(TimeIndex+"FirstLogin",FALSE).apply();
        }
        //
        //
        //
        //
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
        setListViewFooter();
        setListOnScrollListener();
        adapter=new Product_Request_Adapter();
        final String localeCode=MainActivity.languagePreferences.getString("LOCALE_CODE", null);
        final FloatingActionButton voice=view.findViewById(R.id.voice);
        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voice.setClickable(false);
                MediaPlayer mp = new MediaPlayer();

                try {
                    if(localeCode.equals("ml"))
                    { mp=MediaPlayer.create(getContext(),R.raw.donorhome_mal);
                        mp.start();}
                    else if (localeCode.equals("en"))
                    {
                        mp=MediaPlayer.create(getContext(),R.raw.donorhome_eng);
                        mp.start();
                    }
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            voice.setClickable(true);

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        product_request_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                JSONObject object=null;
                String name=null;
                String status=null;
                try {
                    object=products.getJSONObject(position);
                   name=object.getString("Name");
                   status=object.getString("Status_Now");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(!status.equals("Completed"))
                {  DialogBox dialogBox=new DialogBox(getActivity(),name,position);
                dialogBox.show();
                //Adding width and blur
                Window window=dialogBox.getWindow();
                WindowManager.LayoutParams lp = dialogBox.getWindow().getAttributes();
                lp.dimAmount=0.8f;
                dialogBox.getWindow().setAttributes(lp);
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            else {
                    Toast.makeText(getContext(),"This Request has been completed",Toast.LENGTH_SHORT).show();
                }
            }
        });
        new HTTPAsyncTask2().execute(url);

    }
    private void setListViewFooter(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.footer_listview_progressbar, null);
        progressBar = view.findViewById(R.id.progressBar);
        product_request_list.addFooterView(progressBar);
    }

    private void setListOnScrollListener(){
        product_request_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == SCROLL_STATE_IDLE && product_request_list.getLastVisiblePosition() ==
                        products.length()){
                    progressBar.setVisibility(View.VISIBLE);
                    JSONObject timeindex=new JSONObject();
                    try {
                        timeindex.put("LastEvaluatedKey",LastIndex);
                        timeindex.put("TimeIndex", TimeIndex);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    StringData=timeindex.toString();
                   // Log.d("jisjoe",timeindex.toString());
                    if(timeindex.has("LastEvaluatedKey"))
                   new HTTPAsyncTask3().execute(url);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {}
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
                Log.d("seby", String.valueOf(object));
                JSONObject object1=object.getJSONObject("Items");
                Log.d("seby", String.valueOf(object.getString("Status_Now")));
                String status= String.valueOf(object.getString("Status_Now"));
                if(status.equals("Accepted"))
                    status="Partially Accepted";
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
                 holder.Status.setText(status);


            }catch (Exception e){
            }



            return view;
        }
    }
    private class ViewHolder1 {
        TextView ProductName;
        TextView Quantity,Product,Status;



        public ViewHolder1(View v) {
            ProductName = (TextView) v.findViewById(R.id.product_name);
            Quantity=v.findViewById(R.id.requested_quantity);
            Product=v.findViewById(R.id.products);
             Status=v.findViewById(R.id.Status);





        }
    }

    private class HTTPAsyncTask2 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String response="Network Error";
            // params comes from the execute() call: params[0] is the url.

                try {
                    if(!submit)
                    {    Log.d("sj69",StringData.toString());
                    response= HTTPPostGet.getJsonResponse(url,StringData);}
                    else
                        response= HTTPPostGet.getJsonResponse(url2,StringData);
                    Log.d("sj",StringData.toString());
                    return response;
                } catch (Exception e) {
                    e.printStackTrace();
                    return "Error!";
                }
                finally {
                    progress.cancel();
                }

        }
        @Override
        protected void onPreExecute() {
            progress=new ProgressDialog(getContext());
            String loadingMessage = getString(R.string.loading);
            progress.setMessage(loadingMessage);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.show();

            CheckInternet CI=new CheckInternet();
            CI.isOnline(getContext());
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            progress.cancel();
            JSONObject responseObject= null;
            try {
                if (!submit)
                {   JSONObject jsonObject=new JSONObject(result);
                    JSONArray parentObject=null;
                    if(!jsonObject.has("Message"))
                    parentObject = new JSONObject(result).getJSONArray("Items");
                   try {
                       LastIndex = jsonObject.getJSONObject("LastEvaluatedKey");
                   }
                   catch (Exception e)
                   {

                   }
                 /*   JSONArray parentObject2 = new JSONObject(result).getJSONArray("0");*/
                    Log.d("ResponseitemA",jsonObject.toString());
                    products = new JSONArray();
                    Mainproducts=new JSONArray();
                    Mainproducts=parentObject;
                    products=parentObject;
                   if(jsonObject.has("Message"))
                    { product_request_list.setVisibility(View.INVISIBLE);
                        getView().findViewById(R.id.no_entry).setVisibility(View.INVISIBLE);
                        getView().findViewById(R.id.not_verified).setVisibility(View.VISIBLE);
                    }
                    else if(products.length()==0)
                    {
                        getView().findViewById(R.id.not_verified).setVisibility(View.INVISIBLE);
                        product_request_list.setVisibility(View.INVISIBLE);
                        getView().findViewById(R.id.no_entry).setVisibility(View.VISIBLE);
                        Log.d("msg",products.toString());
                        product_request_list.setAdapter(adapter);
                    }
                    else {
                        product_request_list.setVisibility(View.VISIBLE);
                        getView().findViewById(R.id.no_entry).setVisibility(View.INVISIBLE);
                        getView().findViewById(R.id.not_verified).setVisibility(View.INVISIBLE);
                       Log.d("msg",products.toString());
                       product_request_list.setAdapter(adapter);
                    }


                }
                else
                {  JSONObject jsonObject=new JSONObject(result);
                    if(jsonObject.getString("Message").equals("Success")) {

                    }
                    Toast.makeText(getContext(),jsonObject.getString("Message"),Toast.LENGTH_SHORT).show();
                    submit=false;




                }


            } catch (JSONException e) {
                e.printStackTrace();
            }    }


    }
    private class HTTPAsyncTask3 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String response="Network Error";
            // params comes from the execute() call: params[0] is the url.

                try {
                    Log.d("sj_new",StringData.toString());
                        response= HTTPPostGet.getJsonResponse(url,StringData);
                   // Log.d("sj",StringData.toString());
                    return response;
                } catch (Exception e) {
                    e.printStackTrace();
                    return "Error!";
                }
                finally {
                    progress.cancel();
                }

        }
        @Override
        protected void onPreExecute() {


            CheckInternet CI=new CheckInternet();
            CI.isOnline(getContext());
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            progress.cancel();



            JSONObject responseObject= null;
            try {
                if (!submit)
                {   JSONObject jsonObject=new JSONObject(result);
                    JSONArray parentObject=null;
                    if(!jsonObject.has("Message"))
                        parentObject = new JSONObject(result).getJSONArray("Items");
                    if(jsonObject.has("LastEvaluatedKey")) {
                        LastIndex = jsonObject.getJSONObject("LastEvaluatedKey");
                    //    Log.d("jisjoe", LastIndex.toString());
                        /*   JSONArray parentObject2 = new JSONObject(result).getJSONArray("0");*/
                        Log.d("ResponseitemA", jsonObject.toString());
                        //products = new JSONArray();
                       // Mainproducts = new JSONArray();
                        Mainproducts = parentObject;
                        Log.d("jisjoe","Old="+products.length());

                        //products.put(parentObject);
                       // products = parentObject;
                        for(int h=0;h<parentObject.length();h++)
                        {
                            products.put(parentObject.getJSONObject(h));
                            Log.d("jisjoe","Item="+parentObject.getJSONObject(h)+"\n");
;                        }
                        Log.d("jisjoe","new="+products.length());
                        if (jsonObject.has("Message")) {
                            product_request_list.setVisibility(View.INVISIBLE);
                            getView().findViewById(R.id.no_entry).setVisibility(View.INVISIBLE);
                            getView().findViewById(R.id.not_verified).setVisibility(View.VISIBLE);
                        } else if (products.length() == 0) {
                            getView().findViewById(R.id.not_verified).setVisibility(View.INVISIBLE);
                            product_request_list.setVisibility(View.INVISIBLE);
                            getView().findViewById(R.id.no_entry).setVisibility(View.VISIBLE);
                            Log.d("msg", products.toString());
                            product_request_list.setAdapter(adapter);
                        } else {
                            product_request_list.setVisibility(View.VISIBLE);
                            getView().findViewById(R.id.no_entry).setVisibility(View.INVISIBLE);
                            getView().findViewById(R.id.not_verified).setVisibility(View.INVISIBLE);
                            Log.d("msg", products.toString());
                            product_request_list.setAdapter(adapter);
                            product_request_list.setSelection(adapter.getCount()-parentObject.length());
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                    else {
                        progressBar.setVisibility(View.GONE);
                    }


                }
                else
                {  JSONObject jsonObject=new JSONObject(result);
                    if(jsonObject.getString("Message").equals("Success")) {

                    }
                    Toast.makeText(getContext(),jsonObject.getString("Message"),Toast.LENGTH_SHORT).show();
                    submit=false;




                }


            } catch (JSONException e) {
                e.printStackTrace();
            }    }


    }
    public class DialogBox  extends Dialog {
        JSONArray products2=new JSONArray();
        public String name;
        JSONObject items2=new JSONObject();

        int position;
        public Boolean status;
        public TextView Name;
        Button accept,decline;
        public Activity activity;
        public DialogBox(Activity activity,String name,int position) {
            super(activity);
            this.name=name;
            this.position=position;

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialogcard);
            ListView product_request_list=findViewById(R.id.donor_items_edit_listview);
            Product_Request_Adapter2 adapter=new Product_Request_Adapter2();
            product_request_list.setAdapter(adapter);

            Name=findViewById(R.id.receiver_namw);
            JSONObject object=null;
            JSONObject object1=null;
            try {
                object=products.getJSONObject(position);
                object1=object.getJSONObject("Items");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Iterator<String> iter = object1.keys();
            int i=0;
            while (iter.hasNext()) {
                String key = iter.next();
                JSONObject ob=new JSONObject();
                try {
                    if(Integer.parseInt(object1.get(key).toString())!=0)
                    { ob.put("name",key);
                    ob.put("number",object1.get(key));
                    products2.put(i,ob);
                    i++;}
                } catch (JSONException e) {
                    e.printStackTrace();
                }


               }
               Log.d("prods",products2.toString());
            accept=findViewById(R.id.accept);
            decline=findViewById(R.id.decline);
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(items2.length()!=0)
                    { JSONObject data=new JSONObject();

                    try { JSONObject object=products.getJSONObject(position);
                        //Log.d("sj",object.getString("TimeIndex"));
                        data.put("Donor_TimeIndex",TimeIndex);
                        data.put("Request_TimeIndex",object.get("TimeIndex"));
                        data.put("Items",items2);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    StringData=data.toString();
                    submit=true;
                    new HTTPAsyncTask2().execute(url);
                    DialogBox.super.dismiss();}
                    else {
                        String toastText = getString(R.string.toast_value);
                        Toast.makeText(getContext(), toastText,Toast.LENGTH_LONG).show();
                    }
                }
            });
            decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    status=false;
                    DialogBox.super.dismiss();
                }
            });
            Name.setText(name);

        }
        Boolean getStatus()
        {
            return status;
        }
        private class Product_Request_Adapter2 extends BaseAdapter {

            @Override
            public int getCount() {
                return products2.length();
            }

            @Override
            public Object getItem(int position) {
                Object o=null;
                try {


                    o= products2.get(position);
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
                    view = getLayoutInflater().inflate(R.layout.accept_items,parent,false);
                    holder = new ViewHolder1(view);
                    view.setTag(holder);
                }
                else {
                    holder = (ViewHolder1) view.getTag();
                }


                try {
                    final ViewHolder1 finalHolder = holder;
                    JSONObject object=products2.getJSONObject(position);



                    finalHolder.ProductName.setText(String.valueOf(object.getString("name")));

                    finalHolder.Quantity.setText(String.valueOf(object.getString("number")));
                   /* if(items2.has(holder.ProductName.getText().toString())) {
                        Log.d("Items",items2.getString(holder.ProductName.getText().toString()));
                        holder.Quantity.setText(items2.getString(holder.ProductName.getText().toString()));
                        holder.selected.setChecked(true);

                    }
                    else {
                        holder.selected.setChecked(FALSE);
                        holder.Quantity.setText("");
                    }*/
                    if(items2.has(holder.ProductName.getText().toString()))
                    {
                        holder.Product.setText(items.getString(holder.ProductName.getText().toString()));
                    }
                    else
                        holder.Product.setText("");
                    holder.Product.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {



                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            if (!finalHolder.Product.getText().toString().equals(""))
                            {if(finalHolder.Product.getText().toString().equals("0")||Integer.parseInt(finalHolder.Product.getText().toString())>Integer.parseInt(finalHolder.Quantity.getText().toString()))
                            {
                                String toastText = getString(R.string.toast_reqiured_quantity);
                                Toast.makeText(getContext(), toastText,Toast.LENGTH_LONG).show();
                                finalHolder.Product.setText("");
                            }

                            else
                            { try {
                                items2.remove(finalHolder.ProductName.getText().toString());
                                items2.put(finalHolder.ProductName.getText().toString(),finalHolder.Product.getText().toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }}
                        if(finalHolder.Product.getText().toString().equals(""))
                            {
                                items2.remove(finalHolder.ProductName.getText().toString());
                            }
                        Log.d("test",items2.toString());
                        }
                    });

                }catch (Exception e){
                }



                return view;
            }

        private class ViewHolder1 {
            TextView ProductName;
            TextView Quantity,Product;



            public ViewHolder1(View v) {
                ProductName = (TextView) v.findViewById(R.id.product_name);
                Quantity=v.findViewById(R.id.quantity);
                Product=v.findViewById(R.id.edit_quantity);






            }
        }
    }}
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser&&sharedPref!=null) {

            getFragmentManager().beginTransaction().detach(this).attach(this).commit(); } else{
    }
}}