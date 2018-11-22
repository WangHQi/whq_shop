package wanghuiqi.bawie.com.whq_shop.model;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * date:2018-11-22
 * author:王慧琦(琦小妹i)
 * function:
 */
public class HttpUtils {

    private final Handler handler;
    private final OkHttpClient httpClient;
    private static HttpUtils mHttpUtils;

    private HttpUtils(){
        //创建主线程的handler
        handler = new Handler(Looper.getMainLooper());
        httpClient = new OkHttpClient.Builder()
                .connectTimeout(500, TimeUnit.MILLISECONDS)
                .readTimeout(5000, TimeUnit.MILLISECONDS)
                .writeTimeout(5000, TimeUnit.MILLISECONDS)
                .build();
    }
    //创建接口
    public interface OKhttpInterface{
        void Failed(Exception e);
        void Success(String data);
    }
    public void doGet(String url, final OKhttpInterface mOkhttpInterface){
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if (mOkhttpInterface!=null){
                    //切换到主线程
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mOkhttpInterface.Failed(e);
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response!=null&&response.isSuccessful()){
                    final String data = response.body().string();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mOkhttpInterface!=null){
                                mOkhttpInterface.Success(data);
                                return;
                            }
                        }
                    });
                }
            }
        });
    }
    //单例模式
    public static HttpUtils getInstance(){
        if (mHttpUtils==null){
            synchronized (HttpUtils.class){
                if (mHttpUtils==null){
                    return mHttpUtils=new HttpUtils();
                }
            }
        }
        return mHttpUtils;
    }
}
