package zd.cn.myokhttp;

import android.app.Application;



public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化网络请求
       // OkHttp3Utils.initEvent();
    }
}
