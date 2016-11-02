package chinanurse.cn.nurse.Fragment_Nurse_mine;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import chinanurse.cn.nurse.Fragment_Nurse_mine.personal.GetresumeFragment_personal;
import chinanurse.cn.nurse.Fragment_Nurse_mine.work.GetresumeFragment;
import chinanurse.cn.nurse.Fragment_Nurse_mine.work.IdentityFragment;
import chinanurse.cn.nurse.Fragment_Nurse_mine.work.RecruitlistFragment;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.WebView.News_WebView_url;
import chinanurse.cn.nurse.bean.News_list_type;

public class Myrecruit extends AppCompatActivity implements View.OnClickListener,GetresumeFragment_personal.OnFragmentInteractionListener{
    private LinearLayout back;
    private RelativeLayout left,center,right;
    private TextView text_left,text_center,text_right;
    private View line_left,line_center,line_right;
    private RadioGroup mRadio;
    /**
     * 推送消息发送广播
     */
    private MyReceiver receiver;
    private News_list_type.DataBean newstypebean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myrecruit);
        myrecruitview();
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter("com.USER_ACTION");
        registerReceiver(receiver, filter);
    }

    private void myrecruitview() {
        back= (LinearLayout) findViewById(R.id.myrecruit_back);
        left= (RelativeLayout) findViewById(R.id.myrecruit_left);
        center= (RelativeLayout) findViewById(R.id.myrecruit_center);
        right= (RelativeLayout) findViewById(R.id.myrecruit_right);
        text_left= (TextView) findViewById(R.id.text_left);
        text_center= (TextView) findViewById(R.id.text_center);
        text_right= (TextView) findViewById(R.id.text_right);
        line_left=findViewById(R.id.line_left);
        line_center=findViewById(R.id.line_center);
        line_right=findViewById(R.id.line_right);
        left.setOnClickListener(this);
        center.setOnClickListener(this);
        right.setOnClickListener(this);
        back.setOnClickListener(this);
        FragmentManager fm=getFragmentManager();
        FragmentTransaction t=fm.beginTransaction();
        GetresumeFragment f=new GetresumeFragment();
        t.replace(R.id.fm_content, f);
        t.commit();
        LayoutInflater layoutinfo = this.getLayoutInflater();
        View view = layoutinfo.inflate(R.layout.activity_main,null);
        mRadio = (RadioGroup) view.findViewById(R.id.group);
        mRadio.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.myrecruit_back:
                unregisterReceiver(receiver);
                finish();
                break;
            case R.id.myrecruit_left:
                init();
                text_left.setTextColor(getResources().getColor(R.color.indicator));
                line_left.setBackgroundColor(getResources().getColor(R.color.indicator));
                FragmentManager fm1=getFragmentManager();
                FragmentTransaction t1=fm1.beginTransaction();
                GetresumeFragment f1=new GetresumeFragment();
                t1.replace(R.id.fm_content, f1);
                t1.commit();
                break;
            case R.id.myrecruit_center:
                init();
                text_center.setTextColor(getResources().getColor(R.color.indicator));
                line_center.setBackgroundColor(getResources().getColor(R.color.indicator));
                FragmentManager fm2=getFragmentManager();
                FragmentTransaction t2=fm2.beginTransaction();
                RecruitlistFragment f2=new RecruitlistFragment();
                t2.replace(R.id.fm_content, f2);
                t2.commit();
                break;
            case R.id.myrecruit_right:
                init();
                text_right.setTextColor(getResources().getColor(R.color.indicator));
                line_right.setBackgroundColor(getResources().getColor(R.color.indicator));
                FragmentManager fm3=getFragmentManager();
                FragmentTransaction t3=fm3.beginTransaction();
                IdentityFragment f3=new IdentityFragment();
                t3.replace(R.id.fm_content, f3);
                t3.commit();
                break;
        }
    }
    public void init(){
        text_left.setTextColor(getResources().getColor(R.color.gray4));
        text_center.setTextColor(getResources().getColor(R.color.gray4));
        text_right.setTextColor(getResources().getColor(R.color.gray4));
        line_left.setBackgroundColor(getResources().getColor(R.color.white));
        line_center.setBackgroundColor(getResources().getColor(R.color.white));
        line_right.setBackgroundColor(getResources().getColor(R.color.white));
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
            newstypebean = (News_list_type.DataBean) intent.getSerializableExtra("fndinfo");

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("新通知")
                    .setMessage(newstypebean.getPost_title())
                    .setCancelable(false)
                    .setPositiveButton("立即查看", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("fndinfo", newstypebean);
                            Intent intent = new Intent(Myrecruit.this, News_WebView_url.class);
                            intent.putExtras(bundle);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            Myrecruit.this.startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).create().show();
            context.unregisterReceiver(this);
//            AlertDialog alert = builder.create();
//            alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//            alert.show();
        }
    }
}
