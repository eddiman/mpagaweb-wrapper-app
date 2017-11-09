package no.uib.edvard.pagawebmobil;

import android.content.Intent;
import android.net.http.SslError;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    WebView webViewMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initGui();
        initWebView();

    }

    private void initGui() {

        webViewMain = (WebView) findViewById(R.id.webview_main);
        setTitle("TEST");
    }

    private void initWebView() {

        webViewMain.setWebViewClient(new MyWebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webViewMain.getSettings().setDomStorageEnabled(true);
        webViewMain.getSettings().setJavaScriptEnabled(true);
        // Set cache size to 8 mb by default. should be more than enough
        webViewMain.getSettings().setAppCacheMaxSize(1024*1024*8);

        // This next one is crazy. It's the DEFAULT location for your app's cache
        // But it didn't work for me without this line
        webViewMain.getSettings().setAppCachePath("/data/data/"+ getPackageName() +"/cache");
        webViewMain.getSettings().setAllowFileAccess(true);
        webViewMain.getSettings().setAppCacheEnabled(true);

        webViewMain.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webViewMain.getSettings().setSaveFormData(true);

//        webViewMain.loadUrl("javascript:(function() { document.getElementById('username').value = '" + "asdasd" + "'; ;})()");
        webViewMain.loadUrl("https://muib.bluegarden.net");
//        webViewMain.loadUrl("javascript:document.getElementById('username').value = '"+"USER"+"';" );


    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            switch (view.getTitle()){
                case "HR Portalen":
                    MainActivity.this.setTitle("Timeregistrering");
                    break;
                default:
                    MainActivity.this.setTitle(view.getTitle());
                    break;
            }



        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return false;
        }
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error){
            //Your code to do
            Toast.makeText(getApplicationContext(), "Your Internet Connection May not be active Or " + error , Toast.LENGTH_LONG).show();
            Log.d("WebView error", error.toString() + "");

        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed(); // Ignore SSL certificate errors
        }

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if(webViewMain.getTitle().equals("HR Portalen")) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else if (webViewMain.canGoBack()) {
                        webViewMain.goBack();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }


}
