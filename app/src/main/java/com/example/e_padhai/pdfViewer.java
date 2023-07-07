package com.example.e_padhai;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

public class pdfViewer extends AppCompatActivity {
private String uri;
private PDFView pdfView;
private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        pd=new ProgressDialog(this);
        uri=getIntent().getStringExtra("url");
        pdfView=findViewById(R.id.pdfView);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        pd.setMessage("Loading...");
        pd.show();
        executor.execute(() -> {

            //Background work here
            InputStream inputStream = null;
            try {
                URL url = new URL(uri);
                // below is the step where we are
                // creating our connection.
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    // response is success.
                    // we are getting input stream from url
                    // and storing it in our variable.
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                // this is the method
                // to handle errors.
                Log.d("thiws",e.toString());
                e.printStackTrace();
                pd.dismiss();
            }


            InputStream finalInputStream = inputStream;
            handler.post(() -> {
                //UI Thread work here
                pdfView.fromStream(finalInputStream).load();

                pd.dismiss();

            });
        });



    }

}