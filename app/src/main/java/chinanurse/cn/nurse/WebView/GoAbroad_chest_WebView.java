package chinanurse.cn.nurse.WebView;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.bean.News_list_type;
import chinanurse.cn.nurse.bean.UserBean;

/**
 * Created by Administrator on 2016/7/11.
 */
public class GoAbroad_chest_WebView extends AppCompatActivity {
    private WebView web_webview;
    private Intent intent;
    private String chesttype;
    private Activity mactivity;
    private UserBean user;
    private TextView top_title;
    private RelativeLayout btn_back;
    /**
     * 推送消息发送广播
     */
    private MyReceiver receiver;
    private News_list_type.DataBean newstypebean;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goabroad_chest_webview);
        mactivity = this;
        user = new UserBean(mactivity);
        intent = getIntent();
        chesttype = intent.getStringExtra("chesttype");

        goabroadview();
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter("com.USER_ACTION");
        registerReceiver(receiver, filter);
    }

    private void goabroadview() {
        web_webview = (WebView) findViewById(R.id.goabroad_web_webview);
        top_title = (TextView) findViewById(R.id.top_title);
        btn_back = (RelativeLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unregisterReceiver(receiver);
                finish();
            }
        });


        if ("1".equals(chesttype)) {
            top_title.setText(R.string.chest_online_translation);
        } else if ("2".equals(chesttype)) {
            top_title.setText(R.string.chest_exchange_rate_query);
        } else if ("3".equals(chesttype)) {
            top_title.setText(R.string.chest_time_difference_query);
        } else if ("4".equals(chesttype)) {
            top_title.setText(R.string.chest_degree_certificate);
        } else if ("5".equals(chesttype)) {
            top_title.setText(R.string.chest_weather_query);
        } else if ("6".equals(chesttype)) {
            top_title.setText(R.string.chest_map_query);
        } else if ("7".equals(chesttype)) {
            top_title.setText(R.string.chest_ticket_query);
        } else if ("8".equals(chesttype)) {
            top_title.setText(R.string.chest_hotel_reservation);
        } else if ("9".equals(chesttype)) {
            top_title.setText(R.string.chest_visa_query);
        }else if ("11".equals(chesttype)) {
            web_webview.setWebChromeClient(new WebChromeClient(){
                @Override
                public void onReceivedTitle(WebView view, String title) {
                    super.onReceivedTitle(view, title);
                    top_title.setText(title);
                    top_title.setTextSize(16);
                }
            });
        }else if ("12".equals(chesttype)) {
            web_webview.setWebChromeClient(new WebChromeClient(){
                @Override
                public void onReceivedTitle(WebView view, String title) {
                    super.onReceivedTitle(view, title);
                    top_title.setText(title);
                    top_title.setTextSize(16);
                    top_title.setPadding(40,0,0,0);
                }
            });
        }

        web_webview.getSettings().setJavaScriptEnabled(true);
        web_webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        web_webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        gotoweb();
    }

    private void gotoweb() {
        //WebView加载web资源
        if ("1".equals(chesttype)) {//在线翻译
            web_webview.loadUrl("http://fanyi.youdao.com/");
        } else if ("2".equals(chesttype)) {//汇率查询
            web_webview.loadUrl("http://www.boc.cn/sourcedb/whpj/");
        } else if ("3".equals(chesttype)) {//时差查询
            web_webview.loadUrl("http://time.123cha.com/");
        } else if ("4".equals(chesttype)) {//学历认证
            web_webview.loadUrl("http://www.chsi.com.cn/xlcx/");
        } else if ("5".equals(chesttype)) {//天气查询
            web_webview.loadUrl("http://www.weather.com.cn/");
        } else if ("6".equals(chesttype)) {//地图查询
            web_webview.loadUrl("http://map.baidu.com/");
        } else if ("7".equals(chesttype)) {//机票查询
            web_webview.loadUrl("http://flight.qunar.com/");
        }else if("8".equals(chesttype)){//酒店预订
            web_webview.loadUrl("http://m.ctrip.com/html5/");
//        }else if("9".equals(chesttype)){//签证查询
//            web_webview.loadUrl();
        }else if ("10".equals(chesttype)){
            web_webview.loadUrl("http://crm.chinanurse.cn/form/sign_up.php");
        }else if ("11".equals(chesttype)){
            web_webview.loadUrl("http://app.chinanurse.cn/index.php?g=portal&m=article&a=index&id=406&type=2");
        }else if ("12".equals(chesttype)){//排行榜
            web_webview.loadUrl("http://app.chinanurse.cn/index.php?g=portal&m=article&a=index&id=407&type=2");
        }
//        web_webview.loadUrl(study_Data.getPath());
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        web_webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
    }
    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
            newstypebean = (News_list_type.DataBean) intent.getSerializableExtra("fndinfo");

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("是否点击查看")
                    .setCancelable(false)
                    .setPositiveButton("立即查看", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("fndinfo", newstypebean);
                            Intent intent = new Intent(mactivity, News_WebView_url.class);
                            intent.putExtras(bundle);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mactivity.startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            alert.show();
            context.unregisterReceiver(this);
        }
    }
}
