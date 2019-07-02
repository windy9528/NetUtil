package bwie.com.retrofitutil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * date:2019/7/2
 * name:windy
 * function:
 */
public class RetrofitUtil {

    private static RetrofitUtil instance;
    public static Retrofit retrofit;
    public static OkHttpClient okHttpClient;

    private RetrofitUtil() {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    /**
     * retrofit动态代理模式
     *
     * @param service
     * @param <T>
     * @return
     */
    public <T> T create(final Class<T> service) {
        return retrofit.create(service);
    }


    /**
     * 拼接链接
     *
     * @param url
     */
    public static void setBaseUrl(String url) {
        retrofit.newBuilder().baseUrl(url);
    }

    /**
     * 自定义拦截器
     *
     * @param interceptor
     */
    public static void setLoggingInterceptor(Interceptor interceptor) {
        okHttpClient.newBuilder().addInterceptor(interceptor);
    }

    /**
     * 单例之饿汉式双重锁模式
     *
     * @return
     */
    public static RetrofitUtil getInstance() {
        if (instance == null) {
            synchronized (RetrofitUtil.class) {
                if (instance == null) {
                    instance = new RetrofitUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 网络判断
     * @param context
     * @return
     */
    public static boolean isNetworkConnection(Context context) {
        if (context != null) {
            ConnectivityManager service = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = service.getActiveNetworkInfo();
            if (info != null) {
                return info.isConnected();
            }
        }
        return false;
    }
}
