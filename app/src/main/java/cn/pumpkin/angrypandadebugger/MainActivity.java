package cn.pumpkin.angrypandadebugger;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pumpkin.angrypandadebugger.file.FileUtils;
import cn.pumpkin.angrypandadebugger.proxy.ServiceProxy;

public class MainActivity extends Activity {

    private final static String TAG=MainActivity.class.getName();

    private Button mBtn;

    private Thread mThread1;
    private Thread mThread2;

    // 枷锁
    private static Object lock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initLoad();
        lock=MainActivity.this;

        mBtn=(Button)findViewById(R.id.button);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 运行后点击按钮
                initLoad();
            }
        });

        // 现在开几个线程
        mThread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized(lock) {
                    // 死循环,不释放锁
                    while (true) {
                        initLoad();
                    }
                }
            }
        });
        mThread1.setName("initThread");
        // mThread1.start();

        mThread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized(lock){
                    // 死循环,不释放锁
                    while (true){
                        threadLoad();
                    }
                }
            }
        });
        mThread2.setName("loadthread");
        // mThread2.start();

        initProxy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Thread(new Runnable() {
            @Override
            public void run() {

                while(true){
                    doWork();
                    try {
                        Thread.sleep(5*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();

    }

    public void initProxy(){
        ServiceProxy.init(getApplicationContext(),null,null);
        ServiceProxy.getInstance().onBindService();
    }

    public void doWork(){
        ServiceProxy.getInstance().startWork();
    }

    public void initLoad(){
        String jsonStr = FileUtils.getFromAssets(MainActivity.this,"weather.json");
        Log.i(TAG,"jsonStr : "+jsonStr);
        // 比如我要断点看一下text值
        /*"answer": {
            "text": "好的，今天下午三点我会提醒您"
        }*/
        try {
            JSONObject rootJson=new JSONObject(jsonStr);
            JSONObject answerJson=rootJson.getJSONObject("answer");
            String text=answerJson.optString("text"); // 断点处
            Log.i(TAG,"text : "+text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void threadLoad(){
        String jsonStr = FileUtils.getFromAssets(MainActivity.this,"weather.json");
        Log.i(TAG,"jsonStr : "+jsonStr);
        // 比如我要断点看一下text值
        /*"answer": {
            "text": "好的，今天下午三点我会提醒您"
        }*/
        try {
            JSONObject rootJson=new JSONObject(jsonStr);
            JSONObject answerJson=rootJson.getJSONObject("used_state");
            String state=answerJson.optString("state"); // 断点处
            Log.i(TAG,"text : "+state);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
