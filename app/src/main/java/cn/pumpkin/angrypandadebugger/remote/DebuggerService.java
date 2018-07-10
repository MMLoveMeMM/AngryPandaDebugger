package cn.pumpkin.angrypandadebugger.remote;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pumpkin.angrypandadebugger.aidl.IRemoteService;
import cn.pumpkin.angrypandadebugger.file.FileUtils;

public class DebuggerService extends Service {

    private final static String TAG=DebuggerService.class.getName();

    public DebuggerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        // throw new UnsupportedOperationException("Not yet implemented");
        Log.d(TAG,"binder ok +++++++++++++");
        return stub;
    }

    public IRemoteService.Stub stub=new IRemoteService.Stub(){
        @Override
        public void startWork() throws RemoteException {
            parseJson();
        }
    };

    public void parseJson(){
        String jsonStr = FileUtils.getFromAssets(getApplicationContext(),"weather.json");
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
