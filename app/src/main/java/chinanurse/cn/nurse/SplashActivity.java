package chinanurse.cn.nurse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import chinanurse.cn.nurse.Fragment_Nurse.bean.ForumDataBean;
import chinanurse.cn.nurse.UrlPath.NetBaseConstant;

/*
*2016-6-2
* 欢迎界面
 */
public class SplashActivity extends AppCompatActivity {


    private SharedPreferences sp;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.splash).showImageOnFail(R.mipmap.splash).cacheInMemory(true).cacheOnDisc(true).build();
    private ImageView iv_photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0){
            finish();
            return;
        }
        iv_photo= (ImageView) findViewById(R.id.iv_photo);
        if (AppApplication.photo==null||AppApplication.photo.equals("")){
            iv_photo.setBackgroundResource(R.mipmap.splash);
        }else{
            imageLoader.displayImage(NetBaseConstant.NET_IMAGE_HOST + AppApplication.photo, iv_photo, options);
        }
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
