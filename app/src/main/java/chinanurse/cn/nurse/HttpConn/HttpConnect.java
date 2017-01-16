package chinanurse.cn.nurse.HttpConn;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import chinanurse.cn.nurse.UrlPath.UrlPath;

/**
 * Created by Administrator on 2016/6/6.
 */
public class HttpConnect {
    private static int responsecode = 0;
    private static final int REQUEST_TIMEOUT = 5*1000;//设置请求超时5秒钟
    private static final int SO_TIMEOUT = 5*1000;  //设置等待数据超时时间5秒钟


    //是否连接网络
    public static boolean isConnnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivityManager) {
            NetworkInfo networkInfo[] = connectivityManager.getAllNetworkInfo();

            if (null != networkInfo) {
                for (NetworkInfo info : networkInfo) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
//                        String type = NetworkInfo.getTypeName();
                        Log.e("检查网络", "the net is ok");
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /*
    获取新闻分类列表
     */
    public static String conn_new_first() {
        String parh = UrlPath.news_firstpage + "&parentid=1";
        String result = getHttp(parh);
        return result;
    }

    /*
    获取新闻界面资讯文章的相关文章
     */
    public static String getNewslistAbout(String refid) {
        String path = UrlPath.news_list_about + refid;
        String result = getHttp(path);
        return result;
    }

    /*
    获取新闻界面资讯文章的相关文章
     */
    public static String getNewsTitleImage(String typeid) {
        String path = UrlPath.news_list_about + typeid;
        String result = getHttp(path);
        return result;
    }

    /*
    连接网络工具类
     */
    public static String getHttp(String path) {
        try {
            URL url = new URL(path);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(3000);
            int code = con.getResponseCode();
            if (code == 200) {
                InputStream in = con.getInputStream();
                return getHttpMethod(in);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getHttpMethod(InputStream in) {

        ByteArrayOutputStream bu = new ByteArrayOutputStream();
        byte[] buffe = new byte[1024];
        int lin;
        try {
            while ((lin = in.read(buffe)) != -1) {
                bu.write(buffe, 0, lin);
            }
            in.close();
            bu.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = new String((bu.toByteArray()));
        return result;
    }


    /**
     * //     * 网络可用状态下，通过post方式向server端发送请求，并返回响应数据
     * //     *
     * //     * @param market_uri
     * //     *            请求网址
     * //     * @param nameValuePairs
     * //     *            参数信息
     * //     * @param context
     * //     *            上下文
     * //     * @return 响应数据
     * //
     */
    public static String getResponseForPost(String market_uri, List<NameValuePair> nameValuePairs, Context context) {
        if (isConnnected(context)) {
            if (null == market_uri || "".equals(market_uri)) {
                return null;
            }
            HttpPost request = new HttpPost(market_uri);
            try {
                request.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
                return getRespose(request);
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            } catch (RuntimeException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * //     * 响应客户端请求
     * //     *
     * //     * @param //
     * //     *            客户端请求get/post
     * //     * @return 响应数据
     * //
     */
    private static String getRespose(HttpUriRequest request) {
        try {
            HttpResponse httpResponse = getHttpClient().execute(request);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (HttpStatus.SC_OK == statusCode) {
                return EntityUtils.toString(httpResponse.getEntity());
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (ConnectTimeoutException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static HttpClient getHttpClient(){
        BasicHttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
        HttpClient client = new DefaultHttpClient(httpParams);
        return client;
    }
    /**
     * 工作圈图像上传
     *
     * @param market_uri
     * @param nameValuePairs
     * @param context
     * @return
     */
    public static String getResponseForImg(String market_uri, List<NameValuePair> nameValuePairs, Context context) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost(market_uri);
        try {
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            for (int index = 0; index < nameValuePairs.size(); index++) {

                Log.e("YAG", "YAG_nameValuePairs____=" + nameValuePairs.size());

                Log.e("YAG", "YAG_——————" + nameValuePairs.get(index).getName());

                if (nameValuePairs.get(index).getName().equalsIgnoreCase("upfile")) {

                    Log.e("YAG", "YAG_1111————————");
                    entity.addPart(nameValuePairs.get(index).getName(), new FileBody(new File(nameValuePairs.get(index).getValue())));
                    Log.e("YAG", "YAG——22222————————");
                } else {
                    Log.e("YAG", "YAG——333333————————");
                    entity.addPart(nameValuePairs.get(index).getName(), new StringBody(nameValuePairs.get(index).getValue(), Charset.forName("UTF-8")));
                    Log.e("YAG", "YAG——44444————————");
                }
            }


            Log.e("YAG", "YAG——55555————————");
            httpPost.setEntity(entity);
            Log.e("YAG", "YAG——6666————————");

            //报异常  No such file or directory
            HttpResponse response = httpClient.execute(httpPost, localContext);
            Log.e("YAG", "YAG——777777————————");
            responsecode = response.getStatusLine().getStatusCode();
            Log.e("YAG", "YAG___responsecode====" + responsecode + "");

            if (responsecode == 200) {


                HttpEntity resEntity = response.getEntity();
                String Response = EntityUtils.toString(resEntity);

                Log.e("YAG", "Response------" + Response);
                return Response;
            }
        } catch (Exception e) {
            Log.e("YAG", "YAG——eeee————————");
            e.printStackTrace();

            Log.e("YAG", Log.getStackTraceString(e));

        }

        Log.e("YAG", "Response------777777");
        return null;
    }





}
