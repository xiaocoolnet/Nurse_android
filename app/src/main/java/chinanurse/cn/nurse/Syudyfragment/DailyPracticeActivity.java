package chinanurse.cn.nurse.Syudyfragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.dao.CommunalInterfaces;
import chinanurse.cn.nurse.new_Activity.Question_Activity;

/**
 * Created by wzh on 2016/6/21.
 */
public class DailyPracticeActivity extends Activity implements View.OnClickListener {
    private static final int GETTESTLISTTITLE = 1;
    private TextView tvTitleone, tvTitletwo, tvTitlethree, tvTitlefour, tvTitlefive, tvTitlesix,
            tvNumone, tvNumtwo, tvNumthree, tvNumfour, tvNumfive, tvNumsix, top_title;
    private RelativeLayout rlBack, answer_one, answer_two, answer_three, answer_four, answer_five, answer_six;
    private String[] title;
    private String[] num;
    private SharedPreferences sp;
    private Activity mactivity;
    private UserBean user;
    private Intent intent;
    private String type;
    private SharedPreferences.Editor editor;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommunalInterfaces.STUDY_TITLE:
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    try {
                        String status = jsonObject.getString("status");
                        if (status.equals("success")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            int length = jsonArray.length();
                            title = new String[length];
                            num = new String[length];
                            JSONObject object;
                            for (int i = 0; i < length; i++) {
                                object = (JSONObject) jsonArray.get(i);
                                title[i] = object.getString("name");
                                num[i] = object.getString("count");
                            }
                            tvTitleone.setText(title[0]);
                            tvTitletwo.setText(title[1]);
                            tvTitlethree.setText(title[2]);
                            tvTitlefour.setText(title[3]);
                            tvTitlefive.setText(title[4]);
                            tvTitlesix.setText(title[5]);
                            tvNumone.setText(num[0]);
                            tvNumtwo.setText(num[1]);
                            tvNumthree.setText(num[2]);
                            tvNumfour.setText(num[3]);
                            tvNumfive.setText(num[4]);
                            tvNumsix.setText(num[5]);
                            editor.putString("title0", title[0]);
                            editor.putString("title1", title[1]);
                            editor.putString("title2", title[2]);
                            editor.putString("title3", title[3]);
                            editor.putString("title4", title[4]);
                            editor.putString("title5", title[5]);
                            editor.putString("num0", num[0]);
                            editor.putString("num1", num[1]);
                            editor.putString("num2", num[2]);
                            editor.putString("num3", num[3]);
                            editor.putString("num4", num[4]);
                            editor.putString("num5", num[5]);
                            editor.commit();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        }
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_practice);
        mactivity = this;
        user = new UserBean(mactivity);
        sp = getSharedPreferences("sp", Activity.MODE_PRIVATE);
        editor = sp.edit();
        init();
        tvTitleone.setText(sp.getString("title0", ""));
        tvTitletwo.setText(sp.getString("title1", ""));
        tvTitlethree.setText(sp.getString("title2", ""));
        tvTitlefour.setText(sp.getString("title3", ""));
        tvTitlefive.setText(sp.getString("title4", ""));
        tvTitlesix.setText(sp.getString("title5", ""));
        tvNumone.setText(sp.getString("num0", ""));
        tvNumtwo.setText(sp.getString("num1", ""));
        tvNumthree.setText(sp.getString("num2", ""));
        tvNumfour.setText(sp.getString("num0", ""));
        tvNumfive.setText(sp.getString("num0", ""));
        tvNumsix.setText(sp.getString("num0", ""));
        type = "1";
        if (HttpConnect.isConnnected(mactivity)) {
            new StudyRequest(mactivity, handler).getstudyTitle(user.getUserid(), type, GETTESTLISTTITLE);
        } else {
            Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void init() {
        tvTitleone = (TextView) findViewById(R.id.study_daily_titleone);
        tvTitletwo = (TextView) findViewById(R.id.study_daily_titletwo);
        tvTitlethree = (TextView) findViewById(R.id.study_daily_titlethree);
        tvTitlefour = (TextView) findViewById(R.id.study_daily_titlefour);
        tvTitlefive = (TextView) findViewById(R.id.study_daily_titlefive);
        tvTitlesix = (TextView) findViewById(R.id.study_daily_titlesix);
        tvNumone = (TextView) findViewById(R.id.study_daily_numone);
        tvNumtwo = (TextView) findViewById(R.id.study_daily_numtwo);
        tvNumthree = (TextView) findViewById(R.id.study_daily_numthree);
        tvNumfour = (TextView) findViewById(R.id.study_daily_numfour);
        tvNumfive = (TextView) findViewById(R.id.study_daily_numfive);
        tvNumsix = (TextView) findViewById(R.id.study_daily_numsix);
        top_title = (TextView) findViewById(R.id.top_title);
        top_title.setText(R.string.stu_every_relation_title_text);
        rlBack = (RelativeLayout) findViewById(R.id.btn_back);
        rlBack.setOnClickListener(this);
        answer_one = (RelativeLayout) findViewById(R.id.start_answer_one);
        answer_one.setOnClickListener(this);
        answer_two = (RelativeLayout) findViewById(R.id.start_answer_two);
        answer_two.setOnClickListener(this);
        answer_three = (RelativeLayout) findViewById(R.id.start_answer_three);
        answer_three.setOnClickListener(this);
        answer_four = (RelativeLayout) findViewById(R.id.start_answer_four);
        answer_four.setOnClickListener(this);
        answer_five = (RelativeLayout) findViewById(R.id.start_answer_five);
        answer_five.setOnClickListener(this);
        answer_six = (RelativeLayout) findViewById(R.id.start_answer_six);
        answer_six.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.start_answer_one:
                intent = new Intent(this, Question_Activity.class);
                intent.putExtra("questionNUM", "39");
                Log.e("questioncount_39", tvNumone.getText().toString());

                intent.putExtra("questioncount", tvNumone.getText().toString());
                startActivity(intent);
                break;
            case R.id.start_answer_two:
                intent = new Intent(this, Question_Activity.class);
                intent.putExtra("questionNUM", "34");
                intent.putExtra("questioncount", tvNumtwo.getText().toString());
                startActivity(intent);
                break;
            case R.id.start_answer_three:
                intent = new Intent(this, Question_Activity.class);
                intent.putExtra("questionNUM", "35");
                intent.putExtra("questioncount", tvNumthree.getText().toString());
                startActivity(intent);
                break;
            case R.id.start_answer_four:
                intent = new Intent(this, Question_Activity.class);
                intent.putExtra("questionNUM", "36");
                intent.putExtra("questioncount", tvNumfour.getText().toString());
                startActivity(intent);
                break;
            case R.id.start_answer_five:
                intent = new Intent(this, Question_Activity.class);
                intent.putExtra("questionNUM", "37");
                intent.putExtra("questioncount", tvNumfive.getText().toString());
                startActivity(intent);
                break;
            case R.id.start_answer_six:
                intent = new Intent(this, Question_Activity.class);
                intent.putExtra("questionNUM", "38");
                intent.putExtra("questioncount", tvNumsix.getText().toString());
                startActivity(intent);
                break;

        }
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();
        client.disconnect();
    }
}
