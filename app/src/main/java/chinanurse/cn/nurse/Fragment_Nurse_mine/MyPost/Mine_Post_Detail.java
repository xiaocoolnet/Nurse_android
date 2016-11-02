package chinanurse.cn.nurse.Fragment_Nurse_mine.MyPost;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.bean.mine_main_bean.Mine_Post_bean;

public class Mine_Post_Detail extends AppCompatActivity {

    private Mine_Post_bean.DataBean post_bean;
    private String pagertype;


    private static final int GETCOMMUNITYDETAILS = 1;
    private Activity mactivity;
    private String postdetails;
    private Intent intent;
    private ListView post_detail_show;
    private RelativeLayout btn_back;//返回
    //标题,题目,发帖人,所在部门,发帖人等级,点赞,时间,内容,需要隐藏的部分
    private TextView top_title, community_title, lv_name, lv_position, lv_level, lv_text_like, lv_time, lv_answ, show_detail_num;
    private LinearLayout community_care, lv_collect, lv_thumb_up, lv_invitation_num;//关注（点击）,点击收藏,点赞,帖子
    private ImageView lv_image, lv_shared, main_thumb_up;//头像,分享,已点赞
    private EditText et_write_detail;

    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GETCOMMUNITYDETAILS:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        Gson gson = new Gson();

                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine__post__detail);

        mactivity = this;
        intent = getIntent();
        post_bean = intent.getParcelableExtra("goabroad");
        pagertype = intent.getStringExtra("pagertype");
        postdetailshow();

    }

    private void postdetailshow() {
        btn_back = (RelativeLayout) findViewById(R.id.btn_back);
        top_title = (TextView) findViewById(R.id.top_title);
        community_title = (TextView) findViewById(R.id.community_title);
        lv_image = (ImageView) findViewById(R.id.lv_image);
        lv_position = (TextView) findViewById(R.id.lv_position);
        lv_level = (TextView) findViewById(R.id.lv_level);
        lv_name = (TextView) findViewById(R.id.lv_name);
        lv_text_like = (TextView) findViewById(R.id.lv_text_like);
        lv_time = (TextView) findViewById(R.id.lv_time);
        lv_answ = (TextView) findViewById(R.id.lv_answ);
        lv_collect = (LinearLayout) findViewById(R.id.lv_collect);
        lv_thumb_up = (LinearLayout) findViewById(R.id.lv_thumb_up);
        lv_invitation_num = (LinearLayout) findViewById(R.id.lv_invitation_num);
        post_detail_show = (ListView) findViewById(R.id.post_detail_show);
        top_title.setText("帖子详情");
        lv_shared = (ImageView) findViewById(R.id.lv_shared);
        main_thumb_up = (ImageView) findViewById(R.id.show_detail_main_thumb_up);
        community_title = (TextView) findViewById(R.id.community_title);
        et_write_detail = (EditText) findViewById(R.id.et_community_write_detail);
        show_detail_num = (TextView) findViewById(R.id.show_detail_num);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        community_title.setText(post_bean.getTitle());
        lv_name.setText(post_bean.getName());
//        lv_position.setText("");
        lv_time.setText(post_bean.getWrite_time());
        lv_answ.setText(post_bean.getContent());
//        lv_level.setText("");


    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i("onResume", "==============onResume");
        communityshow();
    }

    private void communityshow() {
        if (HttpConnect.isConnnected(mactivity)) {
            new StudyRequest(mactivity, handler).GETCOMMUNITYDETAILS(postdetails, GETCOMMUNITYDETAILS);
        } else {
            Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
        }
    }
}
