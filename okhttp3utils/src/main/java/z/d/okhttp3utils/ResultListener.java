package z.d.okhttp3utils;

import org.json.JSONObject;

import okhttp3.Call;

public interface ResultListener {

    void onSucess(Call call, String object);
    void onFilure(Call call);
}
