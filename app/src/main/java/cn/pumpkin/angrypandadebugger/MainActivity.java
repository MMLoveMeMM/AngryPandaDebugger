package cn.pumpkin.angrypandadebugger;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pumpkin.angrypandadebugger.file.FileUtils;

public class MainActivity extends Activity {

    private final static String TAG=MainActivity.class.getName();

    private Button mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLoad();

        mBtn=(Button)findViewById(R.id.button);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 运行后点击按钮
                initLoad();
            }
        });

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

}
