package chinanurse.cn.nurse.Fragment_Mine.mine_news;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.alibaba.sdk.android.push.notification.CPushMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import chinanurse.cn.nurse.UrlPath.NetBaseConstant;
import chinanurse.cn.nurse.WebView.News_WebView_url;
import chinanurse.cn.nurse.bean.News_list_type;

/**
 * @author: 正纬
 * @since: 15/4/9
 * @version: 1.1
 * @feature: 用于接收推送的通知和消息
 */
public class MyMessageReceiver extends MessageReceiver {

    // 消息接收部分的LOG_TAG
    public static final String REC_TAG = "receiverlogtage";
    public News_list_type.DataBean newstypebean;
    public News_list_type.DataBean.ThumbBean newsthumbean;
    public News_list_type.DataBean.LikesBean newslikeean;
    public News_list_type.DataBean.FavoritesBean newsfavorbean;
    public News_list_type.DataBean.CommentsBean newscommentbean;
    public List<News_list_type.DataBean.ThumbBean> listthum;
    public List<News_list_type.DataBean.LikesBean> listlike;
    public List<News_list_type.DataBean.FavoritesBean> listfavor;
    public List<News_list_type.DataBean.CommentsBean> listcomments;
    /**
     * 推送通知的回调方法
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     */
    @Override
    public void onNotification(final Context context, String title, String summary, Map<String, String> extraMap) {
        // TODO 处理推送通知
        if ( null != extraMap ) {
            for (Map.Entry<String, String> entry : extraMap.entrySet()) {
                Log.i(REC_TAG,"@Get diy param : Key1=" + entry.getKey() + " , Value1=" + entry.getValue());
                if ("news".equals(entry.getKey())){
                    try {
                        JSONObject json = new JSONObject(entry.getValue());
                        newstypebean = new News_list_type.DataBean();
                        newstypebean.setIstop(json.getString("istop"));
                        newstypebean.setPost_hits(json.getString("post_hits"));
                        newstypebean.setObject_id(json.getString("object_id"));
                        newstypebean.setPost_like(json.getString("post_like"));
                        newstypebean.setTerm_id(json.getString("term_id"));
                        newstypebean.setRecommended(json.getString("recommended"));
                        newstypebean.setTerm_name(json.getString("term_name"));
                        newstypebean.setPost_source(json.getString("post_source"));
                        newstypebean.setPost_excerpt(json.getString("post_excerpt"));
                        newstypebean.setSmeta(json.getString("smeta"));
                        newstypebean.setPost_title(json.getString("post_title"));
                        newstypebean.setPost_date(json.getString("post_date"));
                        listthum = new ArrayList<>();
                        JSONArray jsonone = new JSONArray(json.getString("thumb"));
                        if (jsonone !=null&&jsonone.length() > 0){
                        for (int i = 0;i < jsonone.length();i++){
                            JSONObject jo=jsonone.getJSONObject(i);
                            newsthumbean = new News_list_type.DataBean.ThumbBean();
                            newsthumbean.setUrl(jo.getString("url"));
                            newsthumbean.setAlt(jo.getString("alt"));
                            listthum.add(newsthumbean);
                        }}
                        newstypebean.setThumb(listthum);
                        listfavor = new ArrayList<>();
                        JSONArray jsonthree = new JSONArray(json.getString("favorites"));
                        if (jsonthree != null&&jsonthree.length() > 0){
                        for (int i = 0;i < jsonthree.length();i++){
                                JSONObject josonthree=jsonthree.getJSONObject(i);
                                newsfavorbean = new News_list_type.DataBean.FavoritesBean();
                                newsfavorbean.setUserid(josonthree.getString("userid"));
                                listfavor.add(newsfavorbean);
                            }
                        }
                        newstypebean.setFavorites(listfavor);
                        listcomments = new ArrayList<>();
                        JSONArray jsonfour = new JSONArray(json.getString("comments"));
                        if (jsonfour != null&&jsonfour.length() > 0){
                        for (int i = 0;i < jsonfour.length();i++){
                            JSONObject josonfour=jsonfour.getJSONObject(i);
                            newscommentbean = new News_list_type.DataBean.CommentsBean();
                            newscommentbean.setUserid(josonfour.getString("userid"));
                            newscommentbean.setUsername(josonfour.getString("username"));
                            listcomments.add(newscommentbean);
                        }}
                        newstypebean.setComments(listcomments);
                        listlike = new ArrayList<>();
                        JSONArray jsontwo = new JSONArray(json.getString("likes"));
                        for (int i = 0;i < jsontwo.length();i++) {
                            if (jsontwo != null && jsontwo.length() > 0) {
                                JSONObject josontwo = jsontwo.getJSONObject(i);
                                newslikeean = new News_list_type.DataBean.LikesBean();
                                newslikeean.setUserid(josontwo.getString("userid"));
                                newslikeean.setAvatar(NetBaseConstant.NET_HOST + "/" + josontwo.getString("avatar"));
                                newslikeean.setUsername(josontwo.getString("username"));
                                listlike.add(newslikeean);
                            }
                        }
                        newstypebean.setLikes(listlike);
                        Intent pointIntent = new Intent();
                        pointIntent.setAction("com.USER_ACTION");
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("fndinfo", newstypebean);
                        pointIntent.putExtra("title", summary);
                        pointIntent.putExtras(bundle);
                        context.sendBroadcast(pointIntent);
                        Log.e("value","----->"+json.getString("istop"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String urlobject = entry.getValue();
                }

            }
        } else {
            Log.i(REC_TAG,"@收到通知 && 自定义消息为空");
        }
        Log.i(REC_TAG,"收到一条推送通知 ： " + title );
    }

    /**
     * 推送消息的回调方法
     *
     * @param context
     * @param cPushMessage
     */
    @Override
    public void onMessage(Context context, CPushMessage cPushMessage) {
        try {

            Log.i(REC_TAG,"收到一条推送消息 ： " + cPushMessage.getTitle());

            // 持久化推送的消息到数据库
//            new MessageDao(context).add(new MessageEntity(cPushMessage.getMessageId().substring(6, 16), Integer.valueOf(cPushMessage.getAppId()), cPushMessage.getTitle(), cPushMessage.getContent(), new SimpleDateFormat("HH:mm:ss").format(new Date())));

            // 刷新下消息列表
//            ActivityBox.CPDMainActivity.initMessageView();
        } catch (Exception e) {
            Log.i(REC_TAG, e.toString());
        }
    }

    /**
     * 从通知栏打开通知的扩展处理
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     */



    @Override
    public void onNotificationOpened(Context context, String title, String summary, String extraMap) {
//    public void onNotificationOpened(Context context, String title, String summary, Map<String, String> extraMap) {
        CloudPushService cloudPushService = PushServiceFactory.getCloudPushService();
//        cloudPushService.setNotificationSoundFilePath();
        Log.i(REC_TAG,"onNotificationOpened ： " + " : " + title + " : " + summary + " : " + extraMap);
        Log.i(REC_TAG,"onNotificationOpened1 ： " + " : " + cloudPushService);
        //处理

        try {
            JSONObject object = new JSONObject(extraMap);
            if ("success".equals(object.optString("status"))) {
                Log.i("status","=============>"+object.optString("status"));
                JSONObject json = new JSONObject(object.getString("news"));
                newstypebean = new News_list_type.DataBean();
                newstypebean.setIstop(json.getString("istop"));
                Log.i("istop", "=============>" + json.getString("istop"));
                newstypebean.setPost_hits(json.getString("post_hits"));
                newstypebean.setObject_id(json.getString("object_id"));
                newstypebean.setPost_like(json.getString("post_like"));
                newstypebean.setTerm_id(json.getString("term_id"));
                newstypebean.setRecommended(json.getString("recommended"));
                newstypebean.setTerm_name(json.getString("term_name"));
                newstypebean.setPost_source(json.getString("post_source"));
                newstypebean.setPost_excerpt(json.getString("post_excerpt"));
                newstypebean.setSmeta(json.getString("smeta"));
                newstypebean.setPost_title(json.getString("post_title"));
                newstypebean.setPost_date(json.getString("post_date"));
                listthum = new ArrayList<>();
                JSONArray jsonone = new JSONArray(json.getString("thumb"));
                if (jsonone != null && jsonone.length() > 0) {
                    for (int i = 0; i < jsonone.length(); i++) {
                        JSONObject jo = jsonone.getJSONObject(i);
                        newsthumbean = new News_list_type.DataBean.ThumbBean();
                        newsthumbean.setUrl(jo.getString("url"));
                        newsthumbean.setAlt(jo.getString("alt"));
                        listthum.add(newsthumbean);
                    }
                }
                newstypebean.setThumb(listthum);
                listfavor = new ArrayList<>();
                JSONArray jsonthree = new JSONArray(json.getString("favorites"));
                if (jsonthree != null && jsonthree.length() > 0) {
                    for (int i = 0; i < jsonthree.length(); i++) {
                        JSONObject josonthree = jsonthree.getJSONObject(i);
                        newsfavorbean = new News_list_type.DataBean.FavoritesBean();
                        newsfavorbean.setUserid(josonthree.getString("userid"));
                        listfavor.add(newsfavorbean);
                    }
                }
                newstypebean.setFavorites(listfavor);
                listcomments = new ArrayList<>();
                JSONArray jsonfour = new JSONArray(json.getString("comments"));
                if (jsonfour != null && jsonfour.length() > 0) {
                    for (int i = 0; i < jsonfour.length(); i++) {
                        JSONObject josonfour = jsonfour.getJSONObject(i);
                        newscommentbean = new News_list_type.DataBean.CommentsBean();
                        newscommentbean.setUserid(josonfour.getString("userid"));
                        newscommentbean.setUsername(josonfour.getString("username"));
                        listcomments.add(newscommentbean);
                    }
                    newstypebean.setComments(listcomments);
                    listlike = new ArrayList<>();
                    JSONArray jsontwo = new JSONArray(json.getString("likes"));
                    for (int i = 0; i < jsontwo.length(); i++) {
                        if (jsontwo != null && jsontwo.length() > 0) {
                            JSONObject josontwo = jsontwo.getJSONObject(i);
                            newslikeean = new News_list_type.DataBean.LikesBean();
                            newslikeean.setUserid(josontwo.getString("userid"));
                            newslikeean.setAvatar(NetBaseConstant.NET_HOST + "/" + josontwo.getString("avatar"));
                            newslikeean.setUsername(josontwo.getString("username"));
                            listlike.add(newslikeean);
                        }
                    }
                }
                newstypebean.setLikes(listlike);
//                Intent pointIntent = new Intent();
//                pointIntent.setAction("com.USER_ACTIONTWO");
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("fndinfo", newstypebean);
//                pointIntent.putExtras(bundle);
//                context.sendBroadcast(pointIntent);
                Bundle bundle = new Bundle();
                bundle.putSerializable("fndinfo", newstypebean);

                Intent intent = new Intent(context, News_WebView_url.class);
                Log.e("tername", "-------------->" + newstypebean.getTerm_name());
                intent.putExtras(bundle);
                intent.putExtra("title", summary);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
            } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }


    @Override
    public void onNotificationRemoved(Context context, String messageId) {
        Log.i(REC_TAG, "onNotificationRemoved ： " + messageId);
    }


    @Override
    protected void onNotificationClickedWithNoAction(Context context, String title, String summary, String extraMap) {
        Log.i(REC_TAG,"onNotificationClickedWithNoAction ： " + " : " + title + " : " + summary + " : " + extraMap);
    }
}