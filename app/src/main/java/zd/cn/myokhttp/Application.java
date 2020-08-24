package zd.cn.myokhttp;

import z.d.okhttp3utils.OkHttp3Utils;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        OkHttp3Utils.initEvent();
    }
}
