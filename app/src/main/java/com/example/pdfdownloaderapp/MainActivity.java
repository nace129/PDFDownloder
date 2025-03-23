package com.example.pdfdownloaderapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    EditText pdf1, pdf2, pdf3, pdf4, pdf5;
    Button btnDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        pdf1 = findViewById(R.id.pdf1);
        pdf2 = findViewById(R.id.pdf2);
        pdf3 = findViewById(R.id.pdf3);
        pdf4 = findViewById(R.id.pdf4);
        pdf5 = findViewById(R.id.pdf5);
        btnDownload = findViewById(R.id.btnDownload);

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDownload(pdf1.getText().toString(), "IoE.pdf");
                startDownload(pdf2.getText().toString(), "IoE_Economy.pdf");
                startDownload(pdf3.getText().toString(), "everything-for-cities.pdf");
                startDownload(pdf4.getText().toString(), "Cisco_2014_ASR.pdf");
                startDownload(pdf5.getText().toString(), "P3.pdf");
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void startDownload(String url, String fileName) {
        Intent intent = new Intent(MainActivity.this, DownloadService.class);
        intent.putExtra("fileUrl", url);
        intent.putExtra("fileName", fileName);
        startService(intent);
    }
}
