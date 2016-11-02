package chinanurse.cn.nurse.Fragment_Nurse_mine;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.app.Fragment;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import chinanurse.cn.nurse.Fragment_Nurse_mine.mine_collext.Mine_Collect_First;
import chinanurse.cn.nurse.Fragment_Nurse_mine.mine_collext.Mine_Collect_Second;
import chinanurse.cn.nurse.MinePage.MyCollect.Mine_Collect_Third;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.WebView.News_WebView_url;
import chinanurse.cn.nurse.bean.News_list_type;
import chinanurse.cn.nurse.bean.UserBean;

/**
 * Created by Administrator on 2016/7/4.
 */
public class Mycollect extends AppCompatActivity implements View.OnClickListener {

    private int index = 0, currentIndex;
    private TextView tvFindWork, tvFindTalent, tvBook;
    private ImageView ivFindWorkLine, ivFindTalentLine, ivBookLine;
    private Fragment[] fragments;
    private ImageButton imagebutton_bi;
    private Mine_Collect_First collect_first, collect_first_2, collect_first_3;
    private Mine_Collect_Second collect_second;
    private Mine_Collect_Third collect_third;


    private RelativeLayout btn_back;
    private TextView title_top;
    private Activity mactivity;
    private UserBean user;
    /**
     * 推送消息发送广播
     */
    private MyReceiver receiver;
    private News_list_type.DataBean newstypebean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_mycollect);
        mactivity = this;
        user = new UserBean(mactivity);
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter("com.USER_ACTION");
        registerReceiver(receiver, filter);
        btn_back = (RelativeLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        title_top = (TextView) findViewById(R.id.top_title);
        title_top.setText(R.string.main_my_collect);

        //初始化listview
//        nurse_listview = (ListView) findViewById(R.id.nurse_firstfrag_lv);

        initView();
        collect_first = new Mine_Collect_First();
        collect_second = new Mine_Collect_Second();
//        collect_third = new Mine_Collect_Third();
//        fragments = new Fragment[]{collect_first, collect_second, collect_third};
        fragments = new Fragment[]{collect_first, collect_second};
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.nurse_secondpage_container, collect_first);
        fragmentTransaction.commit();

    }

    private void initView() {

        tvFindWork = (TextView) findViewById(R.id.nurse_second_findwork);
        tvFindWork.setOnClickListener(this);

        tvFindTalent = (TextView) findViewById(R.id.nurse_second_findtalents);
        tvFindTalent.setOnClickListener(this);

//        tvBook = (TextView) findViewById(R.id.nurse_second_book);
//        tvBook.setOnClickListener(this);

        ivFindWorkLine = (ImageView) findViewById(R.id.nurse_second_findwork_line);
        ivFindTalentLine = (ImageView) findViewById(R.id.nurse_second_findtalents_line);
//        ivBookLine = (ImageView) findViewById(R.id.nurse_second_book_line);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_back:
                unregisterReceiver(receiver);
                finish();
                break;

            case R.id.nurse_second_findwork:
                tvFindWork.setTextColor(this.getResources().getColor(R.color.purple));
                tvFindTalent.setTextColor(this.getResources().getColor(R.color.gray4));
//                tvBook.setTextColor(this.getResources().getColor(R.color.gray4));
                ivFindWorkLine.setBackgroundColor(this.getResources().getColor(R.color.purple));
                ivFindTalentLine.setBackgroundColor(this.getResources().getColor(R.color.gray2));
//                ivBookLine.setBackgroundColor(this.getResources().getColor(R.color.whilte));
                index = 0;

                break;
            case R.id.nurse_second_findtalents:
                tvFindWork.setTextColor(this.getResources().getColor(R.color.gray4));
                tvFindTalent.setTextColor(this.getResources().getColor(R.color.purple));
//                tvBook.setTextColor(this.getResources().getColor(R.color.gray4));
                ivFindWorkLine.setBackgroundColor(this.getResources().getColor(R.color.gray2));
                ivFindTalentLine.setBackgroundColor(this.getResources().getColor(R.color.purple));
//                ivBookLine.setBackgroundColor(this.getResources().getColor(R.color.whilte));
                index = 1;
                break;
//            case R.id.nurse_second_book:
//                tvFindWork.setTextColor(this.getResources().getColor(R.color.gray4));
//                tvFindTalent.setTextColor(this.getResources().getColor(R.color.gray4));
//                tvBook.setTextColor(this.getResources().getColor(R.color.purple));
//                ivFindWorkLine.setBackgroundColor(this.getResources().getColor(R.color.whilte));
//                ivFindTalentLine.setBackgroundColor(this.getResources().getColor(R.color.whilte));
//                ivBookLine.setBackgroundColor(this.getResources().getColor(R.color.purple));
//                index = 2;
//                break;
        }

        if (currentIndex != index) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.hide(fragments[currentIndex]);
            if (!fragments[index].isAdded()) {
                fragmentTransaction.add(R.id.nurse_secondpage_container, fragments[index]);
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
                    }).create().show();
            context.unregisterReceiver(this);
//            AlertDialog alert = builder.create();
//            alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//            alert.show();
        }
    }

}
