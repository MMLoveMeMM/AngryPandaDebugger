package cn.pumpkin.angrypandadebugger.proxy;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;

import cn.pumpkin.angrypandadebugger.aidl.IRemoteService;

/**
 * @author: zhibao.Liu
 * @version:
 * @date: 2018/7/10 16:43
 * @des:
 * @see {@link }
 */

public class ServiceProxy implements ServiceConnection {

    private final static String SERVICE_DEFUALT_CLASSNAME = "cn.pumpkin.angrypandadebugger.remote.DebuggerService";
    private static ServiceProxy instance;

    private IRemoteService service;
    private static String gPackageName;
    private static String gClassName;
    private static Context gContext;

    public static ServiceProxy getInstance() {
        return instance;
    }

    private ServiceProxy() {
    }

    public static void init(Context context, Looper looper, String packageName) {

        if (instance != null) {
            // TODO: Already initialized
            return;
        }
        gContext = context.getApplicationContext();
        gPackageName = (packageName == null ? context.getPackageName() : packageName);
        gClassName = SERVICE_DEFUALT_CLASSNAME;
        instance = new ServiceProxy();

    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder srv) {
        service = IRemoteService.Stub.asInterface(srv);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        if(service!=null){
            service=null;
        }
    }

    public void onBindService() {

        if (service == null) {
            Intent iSrv = new Intent().setClassName(gPackageName, gClassName);
            if (!gContext.bindService(iSrv, instance, Service.BIND_AUTO_CREATE)) {
                Log.e("AISERVICE", "remote service bind failed");
            }
            return;
        }
    }

    public void startWork(){
        if(service!=null){
            try {
                service.startWork();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
