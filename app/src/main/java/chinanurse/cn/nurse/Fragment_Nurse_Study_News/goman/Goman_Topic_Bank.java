package chinanurse.cn.nurse.Fragment_Nurse_Study_News.goman;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.WebView.News_WebView_url;
import chinanurse.cn.nurse.bean.News_list_type;

/**
 * Created by Administrator on 2016/7/1.
 */
public class Goman_Topic_Bank extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout  rela_more, rela_fragment;
    private LinearLayout line_rn, line_nation, line_competency;
    private View view_1, view_2, view_3;
    private TextView tv_rn, tv_nation, tv_conpetency;


    private Goman_topic_NR_fragment nr_fragment;
    private Goman_topic_Nation_fragment nation_fragment;
    private Goman_topic_Competency_fragment competency_fragment;
    private FragmentManager fragmentManager;
    private Fragment[] fragments;
    private int index, currentIndex;

    private RelativeLayout btn_back;
    private TextView top_title;

    private String pagename;
    private Intent intent;
    private FragmentTransaction fragmentTransaction;
    private Activity mactivity;
    /**
     * 推送消息发送广播
     */
    private MyReceiver receiver;
    private News_list_type.DataBean newstypebean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goman_topic_bank);
        mactivity = this;
        intent = getIntent();
        initView();
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter("com.USER_ACTION");
        registerReceiver(receiver, filter);

    }

    public void initView() {
        rela_more = (RelativeLayout) findViewById(R.id.rela_more);
        rela_more.setVisibility(View.GONE);

        rela_fragment = (RelativeLayout) findViewById(R.id.rela_fragment);
        line_rn = (LinearLayout) findViewById(R.id.line_rn);
        line_nation = (LinearLayout) findViewById(R.id.line_nation);
        line_competency = (LinearLayout) findViewById(R.id.line_competency);
        view_1 = findViewById(R.id.view_1);
        view_2 = findViewById(R.id.view_2);
        view_3 = findViewById(R.id.view_3);
        tv_rn = (TextView) findViewById(R.id.tv_rn);
        tv_nation = (TextView) findViewById(R.id.tv_nation);
        tv_conpetency = (TextView) findViewById(R.id.tv_conpetency);

        line_rn.setOnClickListener(this);
        line_nation.setOnClickListener(this);
        line_competency.setOnClickListener(this);
        rela_more.setOnClickListener(this);


        fragmentManager = getSupportFragmentManager();
        nr_fragment = new Goman_topic_NR_fragment();
        nation_fragment = new Goman_topic_Nation_fragment();
        competency_fragment = new Goman_topic_Competency_fragment();
        fragments = new Fragment[]{nr_fragment, nation_fragment, competency_fragment};

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.rela_fragment, nr_fragment).commit();


        btn_back = (RelativeLayout) findViewById(R.id.btn_back);
        top_title = (TextView) findViewById(R.id.top_title);
        top_title.setText(intent.getStringExtra("top_title"));
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.line_rn:
                tv_rn.setTextColor(getResources().getColor(R.color.purple));
                tv_nation.setTextColor(getResources().getColor(R.color.gray4));
                tv_conpetency.setTextColor(getResources().getColor(R.color.gray4));
                view_1.setBackgroundColor(getResources().getColor(R.color.purple));
                view_2.setBackgroundColor(getResources().getColor(R.color.whilte));
                view_3.setBackgroundColor(getResources().getColor(R.color.whilte));
                nr_fragment = new Goman_topic_NR_fragment();
                Bundle bundle = new Bundle();
                pagename = tv_rn.getText().toString();
                bundle.putString("pagename", pagename);
                nr_fragment.setArguments(bundle);
                //如果transaction  commit（）过  那么我们要重新得到transaction
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.rela_fragment, nr_fragment).commit();
                index = 0;

                break;


            case R.id.line_nation:
                tv_rn.setTextColor(getResources().getColor(R.color.gray4));
                tv_nation.setTextColor(getResources().getColor(R.color.purple));
                tv_conpetency.setTextColor(getResources().getColor(R.color.gray4));
                view_1.setBackgroundColor(getResources().getColor(R.color.whilte));
                view_2.setBackgroundColor(getResources().getColor(R.color.purple));
                view_3.setBackgroundColor(getResources().getColor(R.color.whilte));
                nation_fragment = new Goman_topic_Nation_fragment();
                Bundle bundlenation = new Bundle();
                pagename = tv_nation.getText().toString();
                bundlenation.putString("pagename", pagename);
                nation_fragment.setArguments(bundlenation);
                //如果transaction  commit（）过  那么我们要重新得到transaction
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.rela_fragment, nation_fragment).commit();
                index = 1;

                break;


            case R.id.line_competency:
                tv_rn.setTextColor(getResources().getColor(R.color.gray4));
                tv_nation.setTextColor(getResources().getColor(R.color.gray4));
                tv_conpetency.setTextColor(getResources().getColor(R.color.purple));
                view_1.setBackgroundColor(getResources().getColor(R.color.whilte));
                view_2.setBackgroundColor(getResources().getColor(R.color.whilte));
                view_3.setBackgroundColor(getResources().getColor(R.color.purple));
                competency_fragment = new Goman_topic_Competency_fragment();
                Bundle bundleconpetency = new Bundle();
                pagename = tv_conpetency.getText().toString();
                bundleconpetency.putString("pagename", pagename);
                competency_fragment.setArguments(bundleconpetency);
                //如果transaction  commit（）过  那么我们要重新得到transaction
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.rela_fragment, competency_fragment).commit();
                index = 2;
                break;

            case R.id.rela_more:
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                break;


        }

        if (currentIndex != index) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.hide(fragments[currentIndex]);
            if (!fragments[index].isAdded()) {
                fragmentTransaction.add(R.id.rela_fragment, fragments[index]);
            }
            fragmentTransaction.show(fragments[index]);
            fragmentTransaction.commit();
        }
        currentIndex = index;


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
                    }).create().show();
            context.unregisterReceiver(this);
//            AlertDialog alert = builder.create();
//            alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//            alert.show();
        }
    }
}
