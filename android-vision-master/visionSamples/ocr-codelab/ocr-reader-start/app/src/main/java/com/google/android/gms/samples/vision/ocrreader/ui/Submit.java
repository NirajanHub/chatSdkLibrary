package com.google.android.gms.samples.vision.ocrreader.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.samples.vision.ocrreader.R;

import java.util.ArrayList;

public class Submit extends AppCompatActivity {

    private EditText name, manufacturer, manufactureDate, expiryDate, batchNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_content);
        name = findViewById(R.id.tv_name);
        manufacturer = findViewById(R.id.tv_manufacturer);
        manufactureDate = findViewById(R.id.tv_manufacturer_date);
        expiryDate = findViewById(R.id.tv_expiry_date);
        batchNumber = findViewById(R.id.tv_batch_number);
        try {

            ArrayList<String> arrayList = getIntent().getStringArrayListExtra("array");
            Log.d("nirajan", arrayList.toString());
            name.setText(arrayList.get(0));
            manufacturer.setText(arrayList.get(1));
            manufactureDate.setText(arrayList.get(2));
            expiryDate.setText(arrayList.get(3));
            batchNumber.setText(arrayList.get(4));
            LinearLayout linearLayout = findViewById(R.id.ll_extras);

            for (int i = 4; i < arrayList.size(); i++) {
                EditText editText = new EditText(this);
                editText.setText(arrayList.get(i));
                linearLayout.addView(editText);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
