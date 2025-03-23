package com.example.pdfdownloaderapp;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadService extends IntentService {
    private static final String TAG = "DownloadService";

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String fileUrl = intent.getStringExtra("fileUrl");
            String fileName = intent.getStringExtra("fileName");
            downloadFile(fileUrl, fileName);
        }
    }

    private void savePdfToAppStorage(Context context, byte[] pdfData, String fileName) {
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(pdfData);
            fos.flush();
            Toast.makeText(context, "File saved in app storage", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error saving file", Toast.LENGTH_LONG).show();
        }
    }

//    private void savePdfToDownloads(Context context, byte[] pdfData, String fileName) {
//        ContentResolver resolver = context.getContentResolver();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(MediaStore.Downloads.DISPLAY_NAME, fileName);
//        contentValues.put(MediaStore.Downloads.MIME_TYPE, "application/pdf");
//
//        Uri fileUri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);
//
//        if (fileUri != null) {
//            try (OutputStream outputStream = resolver.openOutputStream(fileUri)) {
//                if (outputStream != null) {
//                    outputStream.write(pdfData);
//                    outputStream.flush();
//                    Toast.makeText(context, "File saved to Downloads", Toast.LENGTH_LONG).show();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//                Toast.makeText(context, "Error saving file", Toast.LENGTH_LONG).show();
//            }
//        }
//    }

    private void downloadFile(String fileUrl, String fileName) {
        try {
            URL url = new URL(fileUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.e(TAG, "Server returned HTTP " + connection.getResponseCode());
                return;
            }

            InputStream input = new BufferedInputStream(connection.getInputStream());
            File downloadDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "PDFs");
            if (!downloadDir.exists()) {
                downloadDir.mkdirs();
            }

            File outputFile = new File(downloadDir, fileName);
            FileOutputStream output = new FileOutputStream(outputFile);

            byte[] data = new byte[1024];
            int count;
            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();

            Log.d(TAG, "File downloaded: " + outputFile.getAbsolutePath());

        } catch (Exception e) {
            Log.e(TAG, "Download error: " + e.getMessage());
        }
    }
}
