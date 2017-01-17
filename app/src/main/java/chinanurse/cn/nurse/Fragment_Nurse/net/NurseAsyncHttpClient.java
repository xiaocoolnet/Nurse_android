package chinanurse.cn.nurse.Fragment_Nurse.net;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by zhuchongkun on 2016/12/16.
 */

public class NurseAsyncHttpClient {
    private static AsyncHttpClient client =new AsyncHttpClient();
    static {
        client.setTimeout(5000);
    }
    public static void  get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(url, params, responseHandler);
    }
    public static void  get(String url, AsyncHttpResponseHandler responseHandler) {
        client.get(url, responseHandler);
    }

    public static void post(String url,RequestParams params,AsyncHttpResponseHandler responseHandler){
        client.post(url, params, responseHandler);
    }

    public static void post(String url,AsyncHttpResponseHandler responseHandler){
        client.post(url, responseHandler);
    }
}
