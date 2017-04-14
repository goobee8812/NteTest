package com.example.administrator.networktest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    TextView responseText = null;
    Button send_request = null;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        responseText = (TextView)findViewById(R.id.response_text);
        send_request = (Button)findViewById(R.id.send_request_btn);
        send_request.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.send_request_btn:
//                sendRequestWithHttpURLConnection();
//                break;
//            default:
//                break;
//        }
        if(view.getId() == R.id.send_request_btn){
            sendRequestWithHttpURLConnection();
        }
    }
    private void sendRequestWithHttpURLConnection(){
        //开启线程来发起网络请求
        Log.d(TAG, "sendRequestWithHttpURLConnection: -----------------");
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL("https://www.jd.com");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8100);
                    InputStream in = connection.getInputStream();
                    //下面对获取到的输入流进行读取
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null){
                        response.append(line);
                    }
                    Log.d(TAG, "run: -----show" + response.toString());
                    showResponse(response.toString());
                }catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(reader != null){
                        try{
                            reader.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                    if(connection != null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    private void showResponse(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //在这里进行UI操作
                responseText.setText(s);
                Log.d(TAG, "run: TextViewShow--------");
            }
        });
    }
}
