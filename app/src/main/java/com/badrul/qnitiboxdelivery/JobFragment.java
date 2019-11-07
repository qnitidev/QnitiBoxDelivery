package com.badrul.qnitiboxdelivery;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class JobFragment extends Fragment implements JobAdapter.OnItemClicked{

    List<Job> jobList;
    ImageButton logout,activeOn,activeOff;


    //the recyclerview
    RecyclerView recyclerView;
    // ImageButton logout;

    String userEmail_Shared;
    String userID;
    String image;
    TextView userNama_tx;
    TextView userCredit_tx;
    ImageView imgGone,imgJobOff;
    TextView txtGone,txtJobOff;
    //int curCheckPosition = 0;
    String sellerLocation;
    String currentDate;
    TextView showtodayQTT;
    String sellerID;
    String delivererLocation;
    String activeStatus;
    String delivererID;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_job, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, getActivity().getApplicationContext().MODE_PRIVATE);
        delivererLocation = sharedPreferences.getString(Config.DE_LOCATION,"Not Available");
        activeStatus = sharedPreferences.getString(Config.DE_ACTIVE,"NO");
        delivererID = sharedPreferences.getString(Config.DE_ID2,"Not Available");

        currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        //logout =myView.findViewById(R.id.logoutBtn);
        userNama_tx = myView.findViewById(R.id.userNama1);
        recyclerView = myView.findViewById(R.id.recylcerView);
        logout = myView.findViewById(R.id.logout);
        imgGone = myView.findViewById(R.id.imageViewGone);
        txtGone = myView.findViewById(R.id.textViewGone);
        imgJobOff = myView.findViewById(R.id.imageViewJobOff);
        txtJobOff = myView.findViewById(R.id.textViewJobOff);
        activeOn = myView.findViewById(R.id.activeBtnYes);
        activeOff = myView.findViewById(R.id.activeBtnNo);

        //userNama_tx.setText(delivererLocation);

        // Set the layout manager to your recyclerview and reverse the position
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        //initializing the joblist

        jobList = new ArrayList<>();

        if (activeStatus.equalsIgnoreCase("YES")){

            activeOff.setVisibility(View.VISIBLE);
            activeOn.setVisibility(View.GONE);
          imgJobOff.setVisibility(View.GONE);
           txtJobOff.setVisibility(View.GONE);
            loadJob();

        }else{

           activeOff.setVisibility(View.GONE);
           activeOn.setVisibility(View.VISIBLE);
            imgJobOff.setVisibility(View.VISIBLE);
          txtJobOff.setVisibility(View.VISIBLE);

        }

        activeOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               activeOff.setVisibility(View.VISIBLE);
               activeOn.setVisibility(View.GONE);
               imgJobOff.setVisibility(View.GONE);
               txtJobOff.setVisibility(View.GONE);

                final ProgressDialog loading = ProgressDialog.show(getActivity(),"Please Wait","Contacting Server",false,false);
                //Creating a string request
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ACTIVE_STATUS,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {


                                //If we are getting success from server
                                if(response.equalsIgnoreCase("Success")){

                                    loading.dismiss();
                                    //Creating a shared preference
                                    SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                                    //Creating editor to store values to shared preferences
                                    SharedPreferences.Editor editor = sharedPreferences.edit();

                                    //Adding values to editor
                                    editor.putString(Config.DE_ACTIVE,"YES");

                                    //Saving values to editor
                                    editor.commit();

                                    Toast.makeText(getActivity(),"You now active. You will receive job notification when available",
                                            Toast.LENGTH_LONG).show();

                                    JobFragment fragment = (JobFragment)
                                            getFragmentManager().findFragmentById(R.id.fragment_container);

                                    getFragmentManager().beginTransaction()
                                            .detach(fragment)
                                            .attach(fragment)
                                            .commit();


                                }else{
                                    //If the server response is not success
                                    //Displaying an error message on toast
                                    loading.dismiss();
                                    Toast.makeText(getActivity(), "Error. Please try again", Toast.LENGTH_LONG).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //You can handle error here if you want
                                loading.dismiss();
                                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                    Toast.makeText(getActivity(),"No internet . Please check your connection",
                                            Toast.LENGTH_LONG).show();
                                }
                                else{

                                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }){


                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String, String>();
                        //Adding parameters to request
                        params.put("delivererID", delivererID);
                        params.put("activeStatus", "YES");

                        //returning parameter
                        return params;
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        30000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                //Adding the string request to the queue
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
                requestQueue.add(stringRequest);

            }
        });


        activeOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               activeOff.setVisibility(View.GONE);
                activeOn.setVisibility(View.VISIBLE);
                imgJobOff.setVisibility(View.VISIBLE);
               txtJobOff.setVisibility(View.VISIBLE);

                final ProgressDialog loading = ProgressDialog.show(getActivity(),"Please Wait","Contacting Server",false,false);
                //Creating a string request
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ACTIVE_STATUS,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {


                                //If we are getting success from server
                                if(response.equalsIgnoreCase("Success")){

                                    loading.dismiss();
                                    //Creating a shared preference
                                    SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                                    //Creating editor to store values to shared preferences
                                    SharedPreferences.Editor editor = sharedPreferences.edit();

                                    //Adding values to editor
                                    editor.putString(Config.DE_ACTIVE,"NO");

                                    //Saving values to editor
                                    editor.commit();

                                    Toast.makeText(getActivity(),"You now inactive. You will no longer receive job notification",
                                            Toast.LENGTH_LONG).show();

                                    JobFragment fragment = (JobFragment)
                                            getFragmentManager().findFragmentById(R.id.fragment_container);

                                    getFragmentManager().beginTransaction()
                                            .detach(fragment)
                                            .attach(fragment)
                                            .commit();


                                }else{
                                    //If the server response is not success
                                    //Displaying an error message on toast
                                    loading.dismiss();
                                    Toast.makeText(getActivity(), "Error. Please try again", Toast.LENGTH_LONG).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //You can handle error here if you want
                                loading.dismiss();
                                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                    Toast.makeText(getActivity(),"No internet . Please check your connection",
                                            Toast.LENGTH_LONG).show();
                                }
                                else{

                                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }){


                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String, String>();
                        //Adding parameters to request
                        params.put("delivererID", delivererID);
                        params.put("activeStatus", "NO");

                        //returning parameter
                        return params;
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        30000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                //Adding the string request to the queue
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
                requestQueue.add(stringRequest);

            }
        });
        /*JobFragment fragment = (JobFragment)
                getFragmentManager().findFragmentById(R.id.fragment_container);

        getFragmentManager().beginTransaction()
                .detach(fragment)
                .attach(fragment)
                .commit();*/

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Creating an alert dialog to confirm logout
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getActivity());
                alertDialogBuilder.setMessage("Do you want to logout?");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                //Getting out sharedpreferences
                                SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                                //Getting editor
                                SharedPreferences.Editor editor = preferences.edit();

                                //Puting the value false for loggedin
                                editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);

                                //Putting blank value to email
                                editor.putString(Config.ID_SHARED_PREF, "");

                                //Saving the sharedpreferences
                                editor.clear();
                                editor.commit();

                                //Starting login activity
                                Intent intent = new Intent(getActivity().getApplicationContext(), LoginPage.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getActivity().finish();
                                startActivity(intent);
                                getActivity().getSupportFragmentManager().popBackStack();
                            }
                        });

                alertDialogBuilder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });

                //Showing the alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        return myView;
    }


    private void loadJob() {

        final ProgressDialog loading = ProgressDialog.show(getActivity(),"Please Wait","Contacting Server",false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.EMPTY_JOB+delivererLocation,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting job object from json array
                                JSONObject job = array.getJSONObject(i);

                                //adding the job to job list
                                jobList.add(new Job(
                                        job.getString("orderID"),
                                        job.getString("sellerName"),
                                        job.getString("sellerAddress"),
                                        job.getString("sellerPhone"),
                                        job.getString("nameID"),
                                        job.getString("puLocation"),
                                        job.getString("phoneID"),
                                        job.getString("orderType"),
                                        job.getString("totalPrice"),
                                        job.getString("orderStatus"),
                                        job.getString("delivererID")

                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                            JobAdapter adapter = new JobAdapter(getActivity().getApplicationContext(), jobList);
                            recyclerView.setAdapter(adapter);
                            adapter.setOnClick(JobFragment.this);

                            if (adapter.getItemCount() == 0) {
                                imgGone.setVisibility(View.VISIBLE);
                                txtGone.setVisibility(View.VISIBLE);
                            } else{

                                imgGone.setVisibility(View.GONE);
                                txtGone.setVisibility(View.GONE);
                            }

                            //add shared preference ID,nama,credit here
                            loading.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(getActivity(),"No internet . Please check your connection",
                                    Toast.LENGTH_LONG).show();
                        }
                        else{

                            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //adding our stringrequest to queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);


    }
/*
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", curCheckPosition);
    }

   /* public void setRvadapter (List<Product> jobList) {

        ProductsAdapter myAdapter = new ProductsAdapter(this,jobList) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);

    }*/



    @Override
    public void onItemClick(int position) {
        // The onClick implementation of the RecyclerView item click
        //ur intent code here
        Job job = jobList.get(position);
        //Toast.makeText(FoodMenu.this, job.getLongdesc(),
        //      Toast.LENGTH_LONG).show();

        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(Config.SHARED_PREF_NAME,
                getActivity().getApplicationContext().MODE_PRIVATE);

        // Creating editor to store values to shared preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Adding values to editor

        editor.putString(Config.ORDER_ID, job.getOrderID());
        editor.putString(Config.SELLER_NAME, job.getSellerName());
        editor.putString(Config.SELLER_LOCATION, job.getSellerAddress());
        editor.putString(Config.SELLER_PHONE, job.getSellerPhone());
        editor.putString(Config.RECEIVER_NAME, job.getReceiverName());
        editor.putString(Config.RECEIVER_LOCATION, job.getReceiverAddress());
        editor.putString(Config.RECEIVER_PHONE, job.getReceiverPhone());
        editor.putString(Config.FOOD_TYPE, job.getFoodName());
        editor.putString(Config.FOOD_PRICE, job.getFoodPrice());
        editor.putString(Config.ORDER_STATUS, job.getOrderStatus());
        editor.putString(Config.ORDER_DELIVER_ID, job.getDelivererID());


        // Saving values to editor
        editor.commit();

        Intent i = new Intent(getActivity().getApplicationContext(), JobDetails.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        //finish();
    }

}