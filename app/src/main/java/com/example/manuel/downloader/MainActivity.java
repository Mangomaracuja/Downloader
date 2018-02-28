package com.example.manuel.downloader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressBar pBar;
    private EditText urlEt;
    private Button button;
    private TextView tv;

    private boolean isDownloading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pBar = findViewById(R.id.progressBar);
        urlEt = findViewById(R.id.editText);
        button = findViewById(R.id.btn);
        tv = findViewById(R.id.textView);

        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == button){
            if(isDownloading) return;
            Log.i("tagiiii","inteeent");
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d("DownloadService","Permission is granted");

                //return true;
            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
            Intent intent = new Intent(this, DownloadService.class);
            intent.putExtra("url", urlEt.getText().toString());
            intent.putExtra("receiver", new DownloadReceiver(new Handler()));
            startService(intent);
            isDownloading = true;
        }
    }

    private class DownloadReceiver extends ResultReceiver {
        @SuppressLint("RestrictedApi")
        public DownloadReceiver(Handler handler) {
            super(handler);
        }

        @SuppressLint("RestrictedApi")
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == DownloadService.UPDATE_PROGRESS) {
                int progress = resultData.getInt("progress");
                pBar.setProgress(progress);
                tv.setText(progress + "%");
                if(progress == 100) {
                    isDownloading = false;
                    Toast.makeText(getApplicationContext(), "Download abgeschlossen!",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
