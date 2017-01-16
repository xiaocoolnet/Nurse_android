package chinanurse.cn.nurse.Fragment_Mine;

import android.app.Activity;
import android.net.Uri;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;

import chinanurse.cn.nurse.Fragment_Mine.personal.GetresumeFragment_personal;
import chinanurse.cn.nurse.Fragment_Mine.personal.RecruitlistFragment_personal;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.bean.UserBean;

public class My_personal_recruit extends Activity implements View.OnClickListener,GetresumeFragment_personal.OnFragmentInteractionListener {
    private static final int GETMYRECIVERESUMELIST = 1;
    private LinearLayout back;
    private RelativeLayout left,center,right;
    private TextView text_left,text_center,text_right;
    private View line_left,line_center,line_right;
    private RadioGroup mRadio;
    private Activity mactivity;
    private UserBean user;
    /**
     * 推送消息发送广播
     */
//    private MyReceiver receiver;
//    private News_list_type.DataBean newstypebean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_myrecruit);

        mactivity = this;
        user = new UserBean(mactivity);
//        receiver = new MyReceiver();
//        IntentFilter filter = new IntentFilter("com.USER_ACTION");
//        registerReceiver(receiver, filter);
        myrecruitview();

    }

    private void myrecruitview() {
        back= (LinearLayout) findViewById(R.id.myrecruit_back);
        left= (RelativeLayout) findViewById(R.id.myrecruit_left);
        center= (RelativeLayout) findViewById(R.id.myrecruit_center);
        text_left= (TextView) findViewById(R.id.text_left);
        text_center= (TextView) findViewById(R.id.text_center);
        line_left=findViewById(R.id.line_left);
        line_center=findViewById(R.id.line_center);
        left.setOnClickListener(this);
        center.setOnClickListener(this);
        back.setOnClickListener(this);
        FragmentManager fm=getFragmentManager();
        FragmentTransaction t=fm.beginTransaction();
        GetresumeFragment_personal f=new GetresumeFragment_personal();
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
//                unregisterReceiver(receiver);
                finish();
                break;
            case R.id.myrecruit_left:
                init();
                text_left.setTextColor(getResources().getColor(R.color.indicator));
                line_left.setBackgroundColor(getResources().getColor(R.color.indicator));
                FragmentManager fm1=getFragmentManager();
                FragmentTransaction t1=fm1.beginTransaction();
                GetresumeFragment_personal f1=new GetresumeFragment_personal(My_personal_recruit.this);
                t1.replace(R.id.fm_content, f1);
                t1.commit();
                break;
            case R.id.myrecruit_center:
                init();
                text_center.setTextColor(getResources().getColor(R.color.indicator));
                line_center.setBackgroundColor(getResources().getColor(R.color.indicator));
                FragmentManager fm2=getFragmentManager();
                FragmentTransaction t2=fm2.beginTransaction();
                RecruitlistFragment_personal f2=new RecruitlistFragment_personal(My_personal_recruit.this);
                t2.replace(R.id.fm_content, f2);
                t2.commit();
                break;
        }
    }
    public void init(){
        text_left.setTextColor(getResources().getColor(R.color.gray4));
        text_center.setTextColor(getResources().getColor(R.color.gray4));
        line_left.setBackgroundColor(getResources().getColor(R.color.white));
        line_center.setBackgroundColor(getResources().getColor(R.color.white));
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }



//    public class MyReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(final Context context, Intent intent) {
//            newstypebean = (News_list_type.DataBean) intent.getSerializableExtra("fndinfo");
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setTitle("新通知")
//                    .setMessage(newstypebean.getPost_title())
//                    .setCancelable(false)
//                    .setPositiveButton("立即查看", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            Bundle bundle = new Bundle();
//                            bundle.putSerializable("fndinfo", newstypebean);
//                            Intent intent = new Intent(mactivity, News_WebView_url.class);
//                            intent.putExtras(bundle);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            mactivity.startActivity(intent);
//                        }
//                    })
//                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
//                        }
//                    }).create().show();
//            context.unregisterReceiver(this);
////            AlertDialog alert = builder.create();
////            alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
////            alert.show();
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onPageStart(this, "个人我的招聘");

    }

    @Override
    protected void onPause() {
        super.onPause();
        // 配对页面埋点，与start的页面名称要一致
        StatService.onPageEnd(this, "个人我的招聘");
    }
}
