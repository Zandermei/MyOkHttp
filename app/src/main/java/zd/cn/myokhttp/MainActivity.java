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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;



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

                Map<String, String> map = new HashMap<>();
                map.put("key","value");
                //通过 Map 集合添加数据
//               OkHttp3Utils.sendPostRequest("url", map, new ResultListener() {
//                   @Override
//                   public void onSucess(Call call, String object) {
//                       //返回成功--处理数据
//                       Toast.makeText(getApplicationContext(),"成功",Toast.LENGTH_SHORT).show();
//                   }
//
//                   @Override
//                   public void onFilure(Call call) {
//                       //返回失败
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
