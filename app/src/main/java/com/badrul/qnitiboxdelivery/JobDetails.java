package com.badrul.qnitiboxdelivery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class JobDetails extends AppCompatActivity {

    TextView sellerAddress,receiverAddress,sellerName,sellerPhone,receiverName,receiverPhone,foodType,foodPrice,showQRtxt;
    String seAdd,seName,sePhone,reName,reAdd,rePhone,foType,foPrice,orStatus,orderID,deliverID,delivererID,scanID;
    ImageView showQR;
    Button acceptJob,scanQR,acceptOrder,cancelOrder;
    int hour;
    String curTime;
    String currentDate;
    int requestCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);


        sellerAddress = findViewById(R.id.sellerLocation);
        receiverAddress = findViewById(R.id.receiverLocation);
        sellerName = findViewById(R.id.txtSellerName);
        sellerPhone = findViewById(R.id.txtSellerPhone);
        receiverName = findViewById(R.id.txtReceiverName);
        receiverPhone = findViewById(R.id.txtReceiverPhone);
        foodType = findViewById(R.id.txtFoodType);
        foodPrice = findViewById(R.id.txtFoodPrice);
        showQR = findViewById(R.id.imageViewQR);
        showQRtxt = findViewById(R.id.showqrtxt);
        acceptJob = findViewById(R.id.acceptJob);
        scanQR = findViewById(R.id.scanBtn);
        acceptOrder = findViewById(R.id.acceptBtn);
        cancelOrder = findViewById(R.id.cancelBtn);


        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        orderID = sharedPreferences.getString(Config.ORDER_ID,"Not Available");
        seAdd = sharedPreferences.getString(Config.SELLER_LOCATION,"Not Available");
        reAdd = sharedPreferences.getString(Config.RECEIVER_LOCATION,"Not Available");
        seName = sharedPreferences.getString(Config.SELLER_NAME,"Not Available");
        sePhone = sharedPreferences.getString(Config.SELLER_PHONE,"Not Available");
        reName = sharedPreferences.getString(Config.RECEIVER_NAME,"Not Available");
        rePhone = sharedPreferences.getString(Config.RECEIVER_PHONE,"Not Available");
        foType = sharedPreferences.getString(Config.FOOD_TYPE,"Not Available");
        foPrice = sharedPreferences.getString(Config.FOOD_PRICE,"Not Available");
        orStatus = sharedPreferences.getString(Config.ORDER_STATUS,"Not Available");
        delivererID = sharedPreferences.getString(Config.DE_ID2,"Not Available");
        deliverID = sharedPreferences.getString(Config.ORDER_DELIVER_ID,"");

        Calendar currTime = Calendar.getInstance();
        hour = currTime.get(Calendar.HOUR_OF_DAY);

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
        curTime = sdf.format(currTime.getTime());

        currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        float scale = getResources().getDisplayMetrics().density;
        final int dpAsPixels = (int) (10*scale + 0.5f); //10 is the intended DP value

        sellerAddress.setText(seAdd);
        receiverAddress.setText(reAdd);
        sellerName.setText(seName);
        sellerPhone.setText(sePhone);
        receiverName.setText(reName);
        receiverPhone.setText(rePhone);
        foodType.setText(foType);
        foodPrice.setText("RM "+foPrice);


        if (orStatus.equalsIgnoreCase("Processing")&& deliverID.equalsIgnoreCase("")){

            acceptJob.setVisibility(View.VISIBLE);
            showQR.setVisibility(View.GONE);
            showQRtxt.setVisibility(View.GONE);
            scanQR.setVisibility(View.GONE);

        }

        if (orStatus.equalsIgnoreCase("Processing")&& !(deliverID.equalsIgnoreCase(""))){

            acceptJob.setVisibility(View.GONE);
            showQR.setVisibility(View.VISIBLE);
            showQRtxt.setVisibility(View.VISIBLE);
            scanQR.setVisibility(View.GONE);

        }

        if (orStatus.equalsIgnoreCase("On Delivery")){

            acceptJob.setVisibility(View.GONE);
            showQR.setVisibility(View.GONE);
            showQRtxt.setVisibility(View.GONE);
            scanQR.setVisibility(View.VISIBLE);

        }

        if (orStatus.equalsIgnoreCase("Complete")){

            acceptJob.setVisibility(View.GONE);
            showQR.setVisibility(View.GONE);
            showQRtxt.setVisibility(View.GONE);
            scanQR.setVisibility(View.GONE);

        }



        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(orderID, BarcodeFormat.QR_CODE,500,500);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            showQR.setImageBitmap(bitmap);
        } catch (
                WriterException e) {
            e.printStackTrace();
        }


        acceptJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(JobDetails.this);
                alertDialogBuilder.setMessage("Do you want to ACCEPT this job?");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                try {
                                    if (Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME) == 0) {

                                        Toast.makeText(getApplicationContext(),
                                                "Please set Automatic Date & Time to ON in the Settings",
                                                Toast.LENGTH_LONG).show();

                                        startActivityForResult(
                                                new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                                    } else if (Settings.Global.getInt(getContentResolver(),
                                            Settings.Global.AUTO_TIME_ZONE) == 0) {

                                        Toast.makeText(getApplicationContext(),
                                                "Please set Automatic Time Zone to ON in the Settings",
                                                Toast.LENGTH_LONG).show();

                                        startActivityForResult(
                                                new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                                    }

                                    else{


                                        final ProgressDialog loading = ProgressDialog.show(JobDetails.this,"Please Wait","Sending Data",false,false);

                                        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.ACCEPT_JOB_URL, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {



                                                if (response.equalsIgnoreCase("Success")) {

                                                    loading.dismiss();
                                                    Intent i = new Intent(JobDetails.this, LoginPage.class);
                                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(i);
                                                    finish();

                                                }
                                                else if(response.equalsIgnoreCase("Exist")){

                                                    loading.dismiss();
                                                    Toast.makeText(JobDetails.this, "Job already taken", Toast.LENGTH_LONG)
                                                            .show();
                                                }else{

                                                    loading.dismiss();
                                                    Toast.makeText(JobDetails.this, "Error. Please try again", Toast.LENGTH_LONG)
                                                            .show();
                                                }

                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {

                                                loading.dismiss();

                                                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                                    Toast.makeText(JobDetails.this,"No internet . Please check your connection",
                                                            Toast.LENGTH_LONG).show();

                                                    finish();
                                                    overridePendingTransition(0, 0);
                                                    startActivity(getIntent());
                                                    overridePendingTransition(0, 0);
                                                }
                                                else{

                                                    Toast.makeText(JobDetails.this, error.toString(), Toast.LENGTH_LONG).show();

                                                    finish();
                                                    overridePendingTransition(0, 0);
                                                    startActivity(getIntent());
                                                    overridePendingTransition(0, 0);
                                                }
                                            }
                                        }) {
                                            @Override
                                            protected Map<String, String> getParams() {
                                                Map<String, String> params = new HashMap<String, String>();
                                                params.put("orderID",orderID);
                                                params.put("delivererID",delivererID);

                                                return params;
                                            }

                                        };
                                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                                30000,
                                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                        requestQueue.add(stringRequest);


                                    }

                                } catch (Settings.SettingNotFoundException e) {
                                    e.printStackTrace();
                                }

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

        scanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {

                    ActivityCompat.requestPermissions(JobDetails.this, new String[]{Manifest.permission.CAMERA}, requestCode);
                } else {

                    startQRScanner();
                }

            }
        });

        acceptOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(JobDetails.this);
                alertDialogBuilder.setMessage("Do you want to confirm this order? (Cannot be change)");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                try {
                                    if (Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME) == 0) {

                                        Toast.makeText(getApplicationContext(),
                                                "Please set Automatic Date & Time to ON in the Settings",
                                                Toast.LENGTH_LONG).show();

                                        startActivityForResult(
                                                new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                                    } else if (Settings.Global.getInt(getContentResolver(),
                                            Settings.Global.AUTO_TIME_ZONE) == 0) {

                                        Toast.makeText(getApplicationContext(),
                                                "Please set Automatic Time Zone to ON in the Settings",
                                                Toast.LENGTH_LONG).show();

                                        startActivityForResult(
                                                new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                                    }

                                    else{


                                        final ProgressDialog loading = ProgressDialog.show(JobDetails.this,"Please Wait","Sending Data",false,false);

                                        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.ACCEPT_ORDER_URL, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {

                                                loading.dismiss();

                                                if(response.contains("Successfully")){

                                                    Toast.makeText(JobDetails.this, "Successfully accept order", Toast.LENGTH_LONG)
                                                            .show();

                                                    Intent i = new Intent(JobDetails.this, LoginPage.class);
                                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(i);
                                                    finish();
                                                }
                                                else if(response.contains("Error")) {

                                                    Toast.makeText(JobDetails.this, "Could not accept. Please try again", Toast.LENGTH_LONG)
                                                            .show();
                                                }
                                                Toast.makeText(JobDetails.this, response, Toast.LENGTH_LONG)
                                                        .show();



                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {

                                                loading.dismiss();

                                                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                                    Toast.makeText(JobDetails.this,"No internet . Please check your connection",
                                                            Toast.LENGTH_LONG).show();

                                                    finish();
                                                    overridePendingTransition(0, 0);
                                                    startActivity(getIntent());
                                                    overridePendingTransition(0, 0);
                                                }
                                                else{

                                                    Toast.makeText(JobDetails.this, error.toString(), Toast.LENGTH_LONG).show();

                                                    finish();
                                                    overridePendingTransition(0, 0);
                                                    startActivity(getIntent());
                                                    overridePendingTransition(0, 0);
                                                }
                                            }
                                        }) {
                                            @Override
                                            protected Map<String, String> getParams() {
                                                Map<String, String> params = new HashMap<String, String>();
                                                params.put("orderID",orderID);
                                                params.put("completeDate", currentDate);
                                                params.put("completeTime", curTime);

                                                return params;
                                            }

                                        };
                                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                                30000,
                                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                        requestQueue.add(stringRequest);


                                    }

                                } catch (Settings.SettingNotFoundException e) {
                                    e.printStackTrace();
                                }

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

        cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(JobDetails.this);
                //Adding Edit Text to Dialog
                final EditText edittext = new EditText(JobDetails.this);
                edittext.setBackgroundResource(R.drawable.textboxcornergrey);
                edittext.setPadding(dpAsPixels,dpAsPixels,dpAsPixels,dpAsPixels);
                FrameLayout container = new FrameLayout(JobDetails.this);
                FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                params.topMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                edittext.setLayoutParams(params);
                container.addView(edittext);

                alertDialogBuilder.setView(container);

                alertDialogBuilder.setMessage("Do you want to cancel this order? Please give a reason");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                final String cancelMsg = edittext.getText().toString().trim();


                                try {
                                    if (Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME) == 0) {

                                        Toast.makeText(getApplicationContext(),
                                                "Please set Automatic Date & Time to ON in the Settings",
                                                Toast.LENGTH_LONG).show();

                                        startActivityForResult(
                                                new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                                    } else if (Settings.Global.getInt(getContentResolver(),
                                            Settings.Global.AUTO_TIME_ZONE) == 0) {

                                        Toast.makeText(getApplicationContext(),
                                                "Please set Automatic Time Zone to ON in the Settings",
                                                Toast.LENGTH_LONG).show();

                                        startActivityForResult(
                                                new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                                    }

                                    else if (cancelMsg.length()<10){

                                        Toast.makeText(JobDetails.this, "Reasoning must be more than 10 characters", Toast.LENGTH_LONG).show();

                                    }else{

                                        final ProgressDialog loading = ProgressDialog.show(JobDetails.this,"Please Wait","Sending Data",false,false);

                                        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.CANCEL_ORDER_URL, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                if(response.contains("Successfully")){

                                                    Toast.makeText(JobDetails.this, "Successfully cancel order", Toast.LENGTH_LONG)
                                                            .show();

                                                    Intent i = new Intent(JobDetails.this, LoginPage.class);
                                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(i);
                                                    finish();
                                                }
                                                else if(response.contains("Error")) {

                                                    Toast.makeText(JobDetails.this, "Could not cancel. Please try again", Toast.LENGTH_LONG)
                                                            .show();
                                                }
                                                Toast.makeText(JobDetails.this, response, Toast.LENGTH_LONG)
                                                        .show();





                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {

                                                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                                    Toast.makeText(JobDetails.this,"No internet . Please check your connection",
                                                            Toast.LENGTH_LONG).show();

                                                    finish();
                                                    overridePendingTransition(0, 0);
                                                    startActivity(getIntent());
                                                    overridePendingTransition(0, 0);
                                                }
                                                else{

                                                    Toast.makeText(JobDetails.this, error.toString(), Toast.LENGTH_LONG).show();

                                                    finish();
                                                    overridePendingTransition(0, 0);
                                                    startActivity(getIntent());
                                                    overridePendingTransition(0, 0);
                                                }
                                                loading.dismiss();
                                            }
                                        }) {
                                            @Override
                                            protected Map<String, String> getParams() {
                                                Map<String, String> params = new HashMap<String, String>();
                                                params.put("orderID",orderID);
                                                params.put("completeDate", currentDate);
                                                params.put("completeTime", curTime);
                                                params.put("cancelMsg", cancelMsg);


                                                return params;
                                            }

                                        };
                                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                                30000,
                                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                        requestQueue.add(stringRequest);


                                    }

                                } catch (Settings.SettingNotFoundException e) {
                                    e.printStackTrace();
                                }



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
    }
    private void startQRScanner() {
        new IntentIntegrator(this).initiateScan();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(JobDetails.this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {

                scanID = result.getContents();

                if(scanID.equalsIgnoreCase(orderID)){

                    scanQR.setVisibility(View.GONE);
                    acceptOrder.setVisibility(View.VISIBLE);
                    cancelOrder.setVisibility(View.VISIBLE);
                }

            }

        }
    }}
