package com.example.whatflower.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.example.whatflower.R;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WebActivity extends AppCompatActivity {

    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        Intent intent = getIntent();
        String chatsItem = intent.getStringExtra("pictureItem");

        Log.i("WebActivity","userData - "+chatsItem);

        JsonObject jsonObject = JsonParser.parseString(chatsItem).getAsJsonObject();

        String wjUrl = jsonObject.get("wjUrl").getAsString();

        webView = findViewById(R.id.webview);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);

        webView.setWebViewClient(new WebViewClient());


        webView.loadUrl(wjUrl);

    }

    @Override
    public void onBackPressed() {

        if (webView.canGoBack()) {
            webView.goBack();
        } else {

            super.onBackPressed();
        }
    }
}