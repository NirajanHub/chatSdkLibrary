package com.google.android.gms.samples.vision.ocrreader.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.samples.vision.ocrreader.R;

import java.util.ArrayList;

public class CapturedText extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.captured_text_activity);
        LinearLayout linearLayout = findViewById(R.id.main);

        Button done = findViewById(R.id.btn_done);
        ArrayList<String> textBlock = getIntent().getStringArrayListExtra("textBlock");
        final ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < textBlock.size(); i++) {
            final TextView textView = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 10, 10, 10);
            textView.setPadding(10, 10, 10, 10);
            textView.setBackground(getResources().getDrawable(R.drawable.border));
            textView.setLayoutParams(params);
            textView.setText(textBlock.get(i));
            linearLayout.addView(textView);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    arrayList.add(textView.getText().toString());
                }
            });
        }

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CapturedText.this, Submit.class);
                intent.putStringArrayListExtra("array", arrayList);
                startActivity(intent);
            }
        });
    }
}