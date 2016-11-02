package chinanurse.cn.nurse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

/*
*2016-6-2
* 欢迎界面
 */
public class SplashActivity extends AppCompatActivity {


    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //判断是否登录，如果已经登录，跳转到主界面
//                if (sp.getBoolean("isLogin", true)) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra("login_now", "0");
//                if(getIntent().getSerializableExtra("fndinfo") != null){
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("fndinfo",getIntent().getSerializableExtra("fndinfo"));
//                    intent.putExtras(bundle);
//                }
                startActivity(intent);
//
//                } else {
//                    //进入登录界面
//                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                }
                finish();
            }
        }.sendEmptyMessageDelayed(0, 3000);

    }
}
