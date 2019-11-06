package com.badrul.qnitiboxdelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class JobDetails extends AppCompatActivity {

    TextView sellerAddress,receiverAddress,sellerName,sellerPhone,receiverName,receiverPhone,foodType,foodPrice;
    String seAdd,seName,sePhone,reName,reAdd,rePhone,foType,foPrice,orStatus,orderID;
    ImageView showQR;

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

        sellerAddress.setText(seAdd);
        receiverAddress.setText(reAdd);
        sellerName.setText(seName);
        sellerPhone.setText(sePhone);
        receiverName.setText(reName);
        receiverPhone.setText(rePhone);
        foodType.setText(foType);
        foodPrice.setText("RM "+foPrice);


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


    }
}
