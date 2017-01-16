package chinanurse.cn.nurse;

import android.app.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.baidu.mobstat.StatService;
import chinanurse.cn.nurse.Fragment_Main.NewsFragment;
import chinanurse.cn.nurse.WebView.News_WebView_url;
import chinanurse.cn.nurse.bean.News_list_type;

public class MainActivity extends AppCompatActivity {

    /**
     * 当前Fragment的key
     */
    private FragmentTag mCurrentTag;
    /**
     * 当前Fragment
     */
    private Fragment mCurrentFragment;
    private Activity mactivity;

    private Intent intent;
    private RadioGroup mRadio;
    private RadioButton mRadioButton1, mRadioButton2, mRadioButton5, mRadioButton3, mRadioButton4;
    private String radioNum;
    private Bundle savedInstanceState;
    /**
     * 推送消息发送广播
     */
    private MyReceiver receiver;
    private News_list_type.DataBean newstypebean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        setContentView(R.layout.activity_main);
        mactivity = this;
        this.savedInstanceState = savedInstanceState;
        intent = getIntent();
        radioNum = intent.getStringExtra("login_now");
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter("com.USER_ACTION");
        registerReceiver(receiver, filter);
        mRadio = (RadioGroup) findViewById(R.id.group);
        mRadioButton1 = (RadioButton) findViewById(R.id.rb1);
        mRadioButton2 = (RadioButton) findViewById(R.id.rb2);
        mRadioButton3 = (RadioButton) findViewById(R.id.rb3);
        mRadioButton4 = (RadioButton) findViewById(R.id.rb4);
        mRadioButton5 = (RadioButton) findViewById(R.id.rb5);
        mRadioButton1.setChecked(true);
        if (savedInstanceState == null) {
            // 记录当前Fragment
            mCurrentTag = FragmentTag.TAG_NEWS;
            mCurrentFragment = new NewsFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.main_fragment, mCurrentFragment,
                            mCurrentTag.getTag()).show(mCurrentFragment).commit();
        }
        //设置RadioGroup的点击事件
        mRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb1:
                        getCurrentTage();
                        break;
                    case R.id.rb2:
                        switchFragmentAddHide(FragmentTag.TAG_STUDY);
                        break;
                    case R.id.rb3:
                        switchFragmentAddHide(FragmentTag.TAG_ABROAD);

                        break;
                    case R.id.rb4:
                        switchFragmentAddHide(FragmentTag.TAG_NURSE);

                        break;
                    case R.id.rb5:
                        switchFragmentAddHide(FragmentTag.TAG_MINE);
                        break;
                }
            }
        });
    }


    public void getCurrentTage() {
        switchFragmentAddHide(FragmentTag.TAG_NEWS);
        mRadioButton1.setChecked(true);
    }

    public FragmentTag getCurrentTage_three() {
        FragmentTag detailpage = FragmentTag.TAG_NURSE;
        if (detailpage.equals(mCurrentTag)) {
            return mCurrentTag;
        }
        return FragmentTag.TAG_NURSE;
    }

    /**
     * 切换Fragment并且隐藏当前
     *
     * @param to 目标Fragment
     */
    public void switchFragmentAddHide(FragmentTag to) {
        if (to != null) {
            if (!mCurrentTag.equals(to)) {
                Fragment currentF = getSupportFragmentManager().findFragmentByTag(
                        mCurrentTag.getTag());
                Fragment toF = getSupportFragmentManager().findFragmentByTag(to.getTag());
                if (null == toF) { // 先判断是否被add过
                    try {
                        // 更新当前Fragment
                        mCurrentTag = to;
                        mCurrentFragment = toF;
                        // 未add过，使用反射新建一个Fragment并add到FragmentManager中
                        toF = (Fragment) Class.forName(to.getTag()).newInstance();
//                        if (mCurrentTag.equals(FragmentTag.TAG_COMMUNITY_LIST)){
                        getSupportFragmentManager().beginTransaction().remove(currentF).add(R.id.main_fragment, toF, to.getTag()).commit(); // 隐藏当前的fragment，add下一个到Activity中
//                        }else {
//                            getSupportFragmentManager().beginTransaction().hide(currentF).add(R.id.main_fragment, toF, to.getTag()).commit(); // 隐藏当前的fragment，add下一个到Activity中
//                        }
                        // 切换按钮动画
                    } catch (Exception e) {
                    }
                } else {
                    // 更新当前Fragment
                    mCurrentTag = to;
                    mCurrentFragment = toF;

//                    if (to.equals(FragmentTag.TAG_NURSE)){
//                        try {
//                            toF = (Fragment) Class.forName(to.getTag()).newInstance();
//                            getSupportFragmentManager().beginTransaction().hide(currentF)
//                                    .add(R.id.nurse_secondpage_container, toF, to.getTag()).commit();
//                        } catch (Exception e) {
//                        }
//                    }else {
                    // add过，直接hide当前，并show出目标Fragment
                    getSupportFragmentManager().beginTransaction().hide(currentF).show(toF).commit(); // 隐藏当前的fragment，显示下一个
//                    }
                }
            }
        }
    }

    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
            newstypebean = (News_list_type.DataBean) intent.getSerializableExtra("fndinfo");
            String title = intent.getStringExtra("title");
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("新通知")
                    .setMessage(title)
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

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onPageStart(this, "主");

    }

    @Override
    protected void onPause() {
        super.onPause();
        // 配对页面埋点，与start的页面名称要一致
        StatService.onPageEnd(this, "主");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
