package zd.cn.myokhttp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import z.d.okhttp3utils.OkHttp3Utils;
import z.d.okhttp3utils.ResultListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPer();
        initView();
    }

    private void initPer() {
        int per = PermissionChecker.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (per != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
        }

    }

    private void initView() {

        final Map<String, String> map = new HashMap<>();

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                map.put("page", "4");
                map.put("count", "2");
                map.put("type", "video");

                OkHttp3Utils.downLoadPic("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598270559250&di=77f0c8a16cc615d304266749451fab28&imgtype=0&src=http%3A%2F%2Ffiles.eduuu.com%2Fimg%2F2012%2F05%2F22%2F170109_4fbb55d532871.jpg", new ResultListener() {
                    @Override
                    public void onSucess(Call call, String object) {
                        Toast.makeText(getApplicationContext(), "成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFilure(Call call) {
                        Toast.makeText(getApplicationContext(), "失败", Toast.LENGTH_SHORT).show();
                    }
                });

//               OkHttp3Utils.sendPostRequest("https://api.apiopen.top/getJoke", map, new ResultListener() {
//                   @Override
//                   public void onSucess(Call call, String object) {
//                       Log.e("----成功--",object);
//                       Toast.makeText(getApplicationContext(),"成功",Toast.LENGTH_SHORT).show();
//                   }
//
//                   @Override
//                   public void onFilure(Call call) {
//                       Log.e("-----失败-","------");
//                       Toast.makeText(getApplicationContext(),"失败",Toast.LENGTH_SHORT).show();
//                   }
//               });

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean per = false;
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                per = true;
            } else {
                per = false;
            }
        }
        if(!per){
            initPer();
        }

    }
}
