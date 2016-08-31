package com.example.yeqinglu.myapplication;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {
    String Greenear = "http://api.qljiang.com/student/1";
    String response = null;
    String urlAddress = null;
    URL url;
    HttpURLConnection urlConnection;
    private static final int UPDATE_TEXT = 1;

    TextView textView = null;
    ProgressBar progressBar = null;

    //在子线程中更新UI
    //在子线程里执行耗时任务，再根据任务的执行结果来更新响应的UI控件
    private Handler handler = new Handler() {
        //handleessage()方法中的代码在主线程中运行
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TEXT:
                    //执行UI操作
                    textView.setText(response);
                    break;
                default:
                    break;
            }
        }
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textview);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        //Message消息机制传输
        new Thread(new Runnable() {
            @Override
            public void run() {
                Login();
                Message message = new Message();
                message.what = UPDATE_TEXT;
                //将Message对象发送出去
                handler.sendMessage(message);
            }
        }).start();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    private void Login() {
        //用HttpClient发送请求，分为五步
        //第一步：创建HttpClient对象

        HttpClient httpCient = new DefaultHttpClient();
        //第二步：创建代表请求的对象,参数是访问的服务器地址
        HttpGet httpGet = new HttpGet(Greenear);

        //第三步：执行请求，获取服务器发还的相应对象
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpCient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            //第五步：从相应对象当中取出数据，放到entity当中
            HttpEntity entity = httpResponse.getEntity();
            try {
                response = EntityUtils.toString(entity, "utf-8");//将entity当中的数据转换为字符串
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    //HttpURLConnection
    public String doGet(String username, String password) {
        String getUrl = urlAddress + "?username=" + username + "&password" + password;
        try {
            url = new URL(getUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream is = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String response = "";
            String readLine = null;
            while ((readLine = br.readLine()) != null) {
                response = response + readLine;
            }
            is.close();
            ;
            br.close();
            ;
            urlConnection.disconnect();
            return response;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String doPost(String username, String password) {
        try {
            url = new URL(urlAddress);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setInstanceFollowRedirects(false);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.connect();

            DataOutputStream out = new DataOutputStream(urlConnection.getOutputStream());
            String content = "username=" + username + "&password" + password;
            out.writeBytes(content);
            out.flush();
            out.close();
            ;

            InputStream is = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String response = "";
            String readLine = null;
            while ((readLine = br.readLine()) != null) {
                response = response + readLine;
            }
            is.close();
            br.close();
            urlConnection.disconnect();
            return response;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.yeqinglu.myapplication/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.yeqinglu.myapplication/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    //AsyncTask使用
    class DownloadTask extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected  void onPreExecute()
        {
            super.onPreExecute();
            progressBar.isShown();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            int i = 1;
            while (i<500)
            {
                i++;
                publishProgress(i);
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
//            progressBar.setProgress(values);


        }



        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }




}
