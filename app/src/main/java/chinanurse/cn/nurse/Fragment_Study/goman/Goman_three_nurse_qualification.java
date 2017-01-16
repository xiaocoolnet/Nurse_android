package chinanurse.cn.nurse.Fragment_Study.goman;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.baidu.mobstat.StatService;

import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.bean.UserBean;

/**
 * Created by Administrator on 2016/11/5.
 */

public class Goman_three_nurse_qualification extends Fragment implements View.OnClickListener{
    private View mView;
    private UserBean user;
    private LinearLayout goman_simulation_test,goman_old_exam,goman_sprint_test,goman_core_point,goman_yati_book,goman_tutorial_essence;
    private Intent intent;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = View.inflate(getActivity(), R.layout.goman_first_nuse_qualification, null);
        user = new UserBean(getActivity());
        goman();
        return mView;
    }

    private void goman() {
        goman_simulation_test = (LinearLayout) mView.findViewById(R.id.goman_simulation_test);//模拟考试
        goman_simulation_test.setOnClickListener(this);
        goman_old_exam = (LinearLayout) mView.findViewById(R.id.goman_old_exam);//历年真题
        goman_old_exam.setOnClickListener(this);
        goman_sprint_test = (LinearLayout) mView.findViewById(R.id.goman_sprint_test);//考试冲刺
        goman_sprint_test.setOnClickListener(this);
        goman_core_point = (LinearLayout) mView.findViewById(R.id.goman_core_point);//核心考点
        goman_core_point.setOnClickListener(this);
        goman_yati_book = (LinearLayout) mView.findViewById(R.id.goman_yati_book);//押题密卷
        goman_yati_book.setOnClickListener(this);
        goman_tutorial_essence = (LinearLayout) mView.findViewById(R.id.goman_tutorial_essence);//辅导精华
        goman_tutorial_essence.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.goman_simulation_test://模拟考试
                intent = new Intent(getActivity(),Goman_topic_second.class);
                intent.putExtra("firstpage","主管护师");
                intent.putExtra("secondpage","模拟考试");
                startActivity(intent);
                break;
            case R.id.goman_old_exam://历年真题
                intent = new Intent(getActivity(),Goman_topic_second.class);
                intent.putExtra("firstpage","主管护师");
                intent.putExtra("secondpage","历年真题");
                startActivity(intent);
                break;
            case R.id.goman_sprint_test://考试冲刺
                intent = new Intent(getActivity(),Goman_topic_second.class);
                intent.putExtra("firstpage","主管护师");
                intent.putExtra("secondpage","考试冲刺");
                startActivity(intent);
                break;
            case R.id.goman_core_point://核心考点
                intent = new Intent(getActivity(),Goman_topic_second.class);
                intent.putExtra("firstpage","主管护师");
                intent.putExtra("secondpage","核心考点");
                startActivity(intent);
                break;
            case R.id.goman_yati_book://押题密卷
                intent = new Intent(getActivity(),Goman_topic_second.class);
                intent.putExtra("firstpage","主管护师");
                intent.putExtra("secondpage","押题密卷");
                startActivity(intent);
                break;
            case R.id.goman_tutorial_essence://辅导精华
                intent = new Intent(getActivity(),Goman_topic_second.class);
                intent.putExtra("firstpage","主管护师");
                intent.putExtra("secondpage","辅导精华");
                startActivity(intent);
                break;
        }

    }
    @Override
    public void onResume() {
        super.onResume();
        StatService.onPageStart(getActivity(), "8万道题库_主管护师");
    }

    @Override
    public void onPause() {
        super.onPause();
        // 配对页面埋点，与start的页面名称要一致
        StatService.onPageEnd(getActivity(), "8万道题库_主管护师");
    }
}
