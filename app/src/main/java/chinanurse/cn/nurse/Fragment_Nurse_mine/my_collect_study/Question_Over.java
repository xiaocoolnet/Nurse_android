package chinanurse.cn.nurse.Fragment_Nurse_mine.my_collect_study;

import android.support.v7.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import chinanurse.cn.nurse.Fragment_Nurse_mine.my_collect_study.over.FirstFragment_Over;
import chinanurse.cn.nurse.Fragment_Nurse_mine.my_collect_study.over.SecondFragment_Over;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.WebView.News_WebView_url;
import chinanurse.cn.nurse.bean.News_list_type;

public class Question_Over extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout btn_back;
    private RadioGroup nurse_rg;
    private RadioButton nurse_rb1, nurse_rb2;
    private LinearLayout nurse_frame;
    private FragmentManager fragmentManager;
    private Fragment[] fragments;
    private FirstFragment_Over firstFragment_over;
    private SecondFragment_Over secondFragment_over;
    private int index, currentIndex;
    /**
     * 推送消息发送广播
     */
    private MyReceiver receiver;
    private News_list_type.DataBean newstypebean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question__over);
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter("com.USER_ACTION");
        registerReceiver(receiver, filter);
        btn_back = (RelativeLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        nurse_rg = (RadioGroup) findViewById(R.id.nurse_rg);
        nurse_rb1 = (RadioButton) findViewById(R.id.nurse_rb1);
        nurse_rb2 = (RadioButton) findViewById(R.id.nurse_rb2);


        nurse_rb1.setChecked(true);
        firstFragment_over = new FirstFragment_Over();
        secondFragment_over = new SecondFragment_Over();
        fragments = new Fragment[]{firstFragment_over, secondFragment_over};
        //默认显示第一个fragment
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.nurse_frame, firstFragment_over);
        fragmentTransaction.commit();
//        initFragment.
        //设置RadioGroup的点击事件
        nurse_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.nurse_rb1:
                        index = 0;
                        nurse_rb1.setChecked(true);
                        break;
                    case R.id.nurse_rb2:
                        index = 1;
                        nurse_rb2.setChecked(true);
                        break;
                }

                if (currentIndex != index) {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.hide(fragments[currentIndex]);
                    if (!fragments[index].isAdded()) {
                        fragmentTransaction.add(R.id.nurse_frame, fragments[index]);
                    }
                    fragmentTransaction.show(fragments[index]);
                    fragmentTransaction.commit();
                }
                currentIndex = index;


            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_back:
                unregisterReceiver(receiver);
                finish();
                break;

        }


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
                            Intent intent = new Intent(Question_Over.this, News_WebView_url.class);
                            intent.putExtras(bundle);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            Question_Over.this.startActivity(intent);
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
