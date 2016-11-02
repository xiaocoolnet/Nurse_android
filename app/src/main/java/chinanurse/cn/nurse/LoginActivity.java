package chinanurse.cn.nurse;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.loginragment.Login_login;
import chinanurse.cn.nurse.loginragment.Login_register;

public class LoginActivity extends AppCompatActivity {
    private Activity mactivity;
    private Context mContext;
    private RadioGroup mRadioGroup;
    private RadioButton mRadiobutton;
    private UserBean user;
    private Intent intent;
    private String now_btn;
    private RelativeLayout btn_back;
    private TextView title_top;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mactivity = this;
        mContext = this;
        user = new UserBean(mactivity);

        mRadioGroup = (RadioGroup) findViewById(R.id.login_radio);
        btn_back = (RelativeLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_top = (TextView) findViewById(R.id.top_title);
        title_top.setText("登录");
        title_top.setTextColor(getResources().getColor(R.color.purple));
//        mFrame = (FrameLayout) findViewById(R.id.login_frag);
        mRadiobutton = (RadioButton) findViewById(R.id.login_mlogin);
        //默认登录按钮被选中
        mRadiobutton.setChecked(true);
        initFragment(new Login_login(now_btn));
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.login_mlogin:
                        initFragment(new Login_login(now_btn));
                        break;
                    case R.id.login_mregist:
                        initFragment(new Login_register());
                        break;
                }
            }
        });
    }
    /**
     * 初始化fragment
     **/
    public void initFragment(Fragment mFragment){
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.login_frag, mFragment);
        ft.commit();
    }
}
