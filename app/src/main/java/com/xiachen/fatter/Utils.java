package com.xiachen.fatter;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.squareup.okhttp.Response;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit.Call;
import retrofit.BaseUrl;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.Query;

/**
 * Created by xiachen on 15/12/7.
 */
public class Utils {

    public static String getTagName() {
        return tagName;
    }

    public static void setTagName(String tagName) {
        Utils.tagName = tagName;
    }

    public static String tagName = "never";

    public static void showToast(Context c, int s) {
        Toast.makeText(c, c.getString(s), Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context c, String s) {
        Toast.makeText(c, s, Toast.LENGTH_SHORT).show();
    }

    public static void showSnackbar(View v, String s1, String s2, View.OnClickListener listener) {
        Snackbar.make(v, s1, Snackbar.LENGTH_LONG).setAction(s2, listener).show();

    }

    public static void showSnackbar(View v, int i1, int i2, View.OnClickListener listener) {
        Snackbar.make(v, i1, Snackbar.LENGTH_LONG).setAction(v.getContext().getText(i2), listener).show();

    }

    public static String formatDate(Date date) {
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        String time = mFormat.format(date);
        return time;
    }

    public static void showLog(Class c, String s) {
        Log.d(c.getSimpleName(), s);
    }

    public static void showLog(String s) {
        Log.d(tagName, s);
    }

    public interface UpdateService {
        @Multipart
        @GET("")
        Call<Response> update();

    }

    static Retrofit retrofit = new Retrofit.Builder().baseUrl(Constant.SERVER_URL).build();

    static UpdateService updateService = retrofit.create(UpdateService.class);
}
