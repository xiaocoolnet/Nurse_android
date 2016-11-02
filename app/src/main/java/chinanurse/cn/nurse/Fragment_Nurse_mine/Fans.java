package chinanurse.cn.nurse.Fragment_Nurse_mine;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import chinanurse.cn.nurse.Fragment_Nurse_mine.fans_and_attion.AttentionFragment;
import chinanurse.cn.nurse.Fragment_Nurse_mine.fans_and_attion.FansFragment;
import chinanurse.cn.nurse.Fragment_Nurse_mine.personal.GetresumeFragment_personal;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.WebView.News_WebView_url;
import chinanurse.cn.nurse.bean.News_list_type;

public class Fans extends Activity implements View.OnClickListener,GetresumeFragment_personal.OnFragmentInteractionListener{
    private LinearLayout back;
    private RelativeLayout fans,attention;
    private TextView title_left,title_right,line_left,line_right;
    private Intent intent;
    private String pagetype;
    /**
     * 推送消息发送广播
     */
    private MyReceiver receiver;
    private News_list_type.DataBean newstypebean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fans);
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter("com.USER_ACTION");
        registerReceiver(receiver, filter);
        intent = getIntent();
        pagetype = intent.getStringExtra("pagetype");
        back= (LinearLayout) findViewById(R.id.fans_back);
        fans= (RelativeLayout) findViewById(R.id.fans);
        attention= (RelativeLayout) findViewById(R.id.attention);
        title_left= (TextView) findViewById(R.id.title_left);
        title_right= (TextView) findViewById(R.id.title_right);
        line_left= (TextView) findViewById(R.id.line_left);
        line_right= (TextView) findViewById(R.id.line_right);
        back.setOnClickListener(this);
        fans.setOnClickListener(this);
        attention.setOnClickListener(this);
        FragmentManager fm=getFragmentManager();
        FragmentTransaction t=fm.beginTransaction();
        FansFragment f1=new FansFragment();
        AttentionFragment f2=new AttentionFragment();
        if ("1".equals(pagetype)){
            //设置字体颜色和对应的下划线颜色
            title_left.setTextColor(getResources().getColor(R.color.indicator));
            line_left.setBackgroundColor(getResources().getColor(R.color.indicator));
            line_right.setBackgroundColor(getResources().getColor(R.color.white));
            title_right.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
            t.replace(R.id.fm_content, f1);
        }else{
            //设置字体颜色和对应的下划线颜色
            line_left.setBackgroundColor(getResources().getColor(R.color.white));
            title_left.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
            title_right.setTextColor(getResources().getColor(R.color.indicator));
            line_right.setBackgroundColor(getResources().getColor(R.color.indicator));
            t.replace(R.id.fm_content, f2);
        }
        t.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fans_back:
                unregisterReceiver(receiver);
                finish();
                break;
            case R.id.fans:
                //设置字体颜色和对应的下划线颜色
                title_left.setTextColor(getResources().getColor(R.color.indicator));
                line_left.setBackgroundColor(getResources().getColor(R.color.indicator));
                line_right.setBackgroundColor(getResources().getColor(R.color.white));
                title_right.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                FragmentManager fm=getFragmentManager();
                FragmentTransaction t=fm.beginTransaction();
                FansFragment f1=new FansFragment();
                t.replace(R.id.fm_content, f1);
                t.commit();
                break;
            case R.id.attention:
                //设置字体颜色和对应的下划线颜色
                line_left.setBackgroundColor(getResources().getColor(R.color.white));
                title_left.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                title_right.setTextColor(getResources().getColor(R.color.indicator));
                line_right.setBackgroundColor(getResources().getColor(R.color.indicator));
                FragmentManager fm2=getFragmentManager();
                FragmentTransaction t2=fm2.beginTransaction();
                AttentionFragment f2=new AttentionFragment();
                t2.replace(R.id.fm_content, f2);
                t2.commit();
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
                            Intent intent = new Intent(Fans.this, News_WebView_url.class);
                            intent.putExtras(bundle);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            Fans.this.startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).create().show();
//            AlertDialog alert = builder.create();
//            alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//            alert.show();
        }
    }
}
