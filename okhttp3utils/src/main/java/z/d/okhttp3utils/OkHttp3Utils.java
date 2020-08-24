package z.d.okhttp3utils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttp3Utils {

    private static long READ_TIME = 8000L;
    private static long WRITE_TIME = 8000L;
    private static long CONNECT_TIME = 10000L;

    private static OkHttpClient mOkhttpClient;
    private static Handler mHander;

    public static void initEvent() {

        if (mOkhttpClient == null) {
            mOkhttpClient = new OkHttpClient().newBuilder()
                    .readTimeout(READ_TIME, TimeUnit.MILLISECONDS)
                    .writeTimeout(WRITE_TIME, TimeUnit.MILLISECONDS)
                    .connectTimeout(CONNECT_TIME, TimeUnit.MILLISECONDS).build();

            mHander = new Handler(Looper.getMainLooper());
        }

    }


    /**
     * 构造一个get请求的方法
     */
    public static void sendGetRequest(String url, final ResultListener listener) {
        //创建一个request
        Request request = new Request.Builder().get().url(url).build();
        //构建一个Call
        mOkhttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(final Call call, IOException e) {
                mHander.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onFilure(call);
                    }
                });
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                parseResponse(call, response, listener);

            }
        });

    }

    /**
     * 构造一个带参数的GET请求方法
     *
     * @param
     * @return
     */
    public static void sendGetRequest(String url, final Map<String, String> map, final ResultListener listener) {

        if (!map.isEmpty()) {
            url += spliceParams(map);//拼接参数
        }
        //创建一个 Request 对象
        final Request request = new Request.Builder().get().url(url).build();
        //构造一个Call 对象 参数是request
        Call call = mOkhttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, IOException e) {
                mHander.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onFilure(call);
                    }
                });
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                parseResponse(call, response, listener);
            }
        });


    }

    /**
     * 解析服务器返回成功数据
     *
     * @param call
     * @param response
     * @param listener
     */
    private static void parseResponse(final Call call, final Response response, final ResultListener listener) {
        if (response != null) {
            if (response.isSuccessful()) {
                final String object = pareData(response);
                mHander.post(new Runnable() {
                    @Override
                    public void run() {

                        listener.onSucess(call, object);

                    }
                });

            } else {
                mHander.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onFilure(call);
                    }
                });
            }

        } else {
            mHander.post(new Runnable() {
                @Override
                public void run() {
                    listener.onFilure(call);
                }
            });
        }
    }


    /**
     * 构造一个post 网络请求
     */
    public static void sendPostRequest(String url, Map<String, String> map, final ResultListener listener) {
        //创建FromBody 添加post参数
        FormBody.Builder builder = new FormBody.Builder();
        if (!map.isEmpty()) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        //构建RequestBody
        RequestBody body = builder.build();
        //构建一个request对象
        Request request = new Request.Builder().post(body).url(url).build();
        //创建Call对象,并返回数据
        mOkhttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, IOException e) {
                mHander.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onFilure(call);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                parseResponse(call, response, listener);
            }
        });


    }


    /**
     * 下载一张图片的请求
     */
    public static void downLoadPic(String url, final ResultListener listener) {
        Request request = new Request.Builder().url(url).build();
        mOkhttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, IOException e) {
                mHander.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onFilure(call);
                    }
                });
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    InputStream stream = response.body().byteStream();
                    FileOutputStream outputStream = new
                            FileOutputStream(new File("/sdcard/" + System.currentTimeMillis() + ".jpg"));

                    byte[] by = new byte[1024];
                    int len = 0;
                    while ((len = stream.read(by)) != -1) {
                        outputStream.write(by, 0, len);
                    }
                    outputStream.flush();
                    mHander.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onSucess(call, null);
                        }
                    });
                }
            }
        });

    }


    /**
     * 拼接 get 方法参数
     *
     * @param
     * @param map
     * @return
     */
    private static String spliceParams(Map<String, String> map) {
        String urlParams = "";
        for (Map.Entry<String, String> entry : map.entrySet()) {
            try {
                if (urlParams.length() == 0) {
                    urlParams += "?" + entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "UTF-8");
                } else {
                    urlParams += "&" + entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "UTF-8");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        return urlParams;
    }

    private static String pareData(Response response) {
        String params = null;
        try {
            params = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return params;
    }


}
