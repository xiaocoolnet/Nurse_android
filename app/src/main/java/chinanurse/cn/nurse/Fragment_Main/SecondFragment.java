package chinanurse.cn.nurse.Fragment_Main;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import chinanurse.cn.nurse.FragmentTag;
import chinanurse.cn.nurse.Fragment_Nurse_job.Add_EmployTalent_Fragment;
import chinanurse.cn.nurse.Fragment_Nurse_job.Add_EmployWork_Fragment;
import chinanurse.cn.nurse.Fragment_Nurse_job.EmployWorkFragment;
import chinanurse.cn.nurse.Fragment_Nurse_job.IdentityFragment_ACTIVITY;
import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.LoginActivity;
import chinanurse.cn.nurse.MainActivity;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.bean.mine_main_bean.Company_news_beans;

/**
 * Created by wzh on 2016/6/26.
 */
public class SecondFragment extends Fragment implements View.OnClickListener {

    private static final int GETCOMANYCERTIFY = 1;
    private int index = 0, currentIndex;
    private TextView tvFindWork, tvFindTalent, tvBook;
    private ImageView ivFindWorkLine, ivFindTalentLine, ivBookLine;
    private UserBean user;
    private MainActivity main;
    /**
     * 当前Fragment的key
     */
    private FragmentTag mCurrentTag;
    private FragmentTag mTAG_HOME;
    /**
     * 当前Fragment
     */
    private Fragment mCurrentFragment;
    private Activity mactivity;


    private ImageButton imagebutton_bi;
    private Company_news_beans.DataBean combean;

    private Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GETCOMANYCERTIFY:
                    if (msg.obj != null){
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            String data = json.getString("data");
                            if ("success".equals(json.optString("status"))){
                                JSONObject item =  new JSONObject(data);
                                combean = new Company_news_beans.DataBean();
                                combean.setId(item.getString("id"));
                                combean.setUserid(item.getString("userid"));
                                combean.setCreate_time(item.getString("create_time"));
                                combean.setCompanyname(item.getString("companyname"));
                                combean.setCompanyinfo(item.getString("companyinfo"));
                                combean.setLinkman(item.getString("linkman"));
                                combean.setPhone(item.getString("phone"));
                                combean.setEmail(item.getString("email"));
                                combean.setLicense(item.getString("license"));
                                combean.setStatus(item.getString("status"));
                                if (!"".equals(item.getString("status"))&&item.getString("status") != null){
                                    if ("1".equals(item.getString("status"))){
                                        Intent intent = new Intent(getActivity(),Add_EmployTalent_Fragment.class);
                                        startActivity(intent);
                                        imagebutton_bi.setClickable(true);
                                    }else {
                                        AlertDialog.Builder builderone = new AlertDialog.Builder(getActivity());
                                        builderone.setMessage("注册认证后可查看")
                                                .setCancelable(false)
                                                .setPositiveButton("立即认证", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        Intent intent = new Intent(getActivity(),IdentityFragment_ACTIVITY.class);
                                                        startActivity(intent);
                                                        imagebutton_bi.setClickable(true);
                                                    }
                                                })
                                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        imagebutton_bi.setClickable(true);
                                                        dialog.cancel();
                                                    }
                                                }).create().show();
                                    }
                                }
                            }else{
                                imagebutton_bi.setClickable(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{

                    }
                    break;
            }
        }
    };



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
//        if (savedInstanceState == null) {
            // 记录当前Fragment
            mCurrentTag = FragmentTag.TAG_WORK;
            mCurrentFragment = new EmployWorkFragment();
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.nurse_secondpage_container, mCurrentFragment,
                            mCurrentTag.getTag()).show(mCurrentFragment).commit();
//        }
    }
    public void initView() {
        main = (MainActivity) getActivity();
        tvFindWork = (TextView) getView().findViewById(R.id.nurse_second_findwork);
        tvFindWork.setOnClickListener(this);

        tvFindTalent = (TextView) getView().findViewById(R.id.nurse_second_findtalents);
        tvFindTalent.setOnClickListener(this);

        tvBook = (TextView) getView().findViewById(R.id.nurse_second_book);
        tvBook.setOnClickListener(this);

        imagebutton_bi = (ImageButton) getView().findViewById(R.id.imagebutton_bi);

        imagebutton_bi.setOnClickListener(this);

        ivFindWorkLine = (ImageView) getView().findViewById(R.id.nurse_second_findwork_line);
        ivFindTalentLine = (ImageView) getView().findViewById(R.id.nurse_second_findtalents_line);
        ivBookLine = (ImageView) getView().findViewById(R.id.nurse_second_book_line);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = View.inflate(getActivity(), R.layout.nurse_employ_secondpage, null);
        user = new UserBean(getActivity());
        return mView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nurse_second_findwork:
                tvFindWork.setTextColor(this.getResources().getColor(R.color.purple));
                tvFindTalent.setTextColor(this.getResources().getColor(R.color.gray4));
                tvBook.setTextColor(this.getResources().getColor(R.color.gray4));
                ivFindWorkLine.setBackgroundColor(this.getResources().getColor(R.color.purple));
                ivFindTalentLine.setBackgroundColor(this.getResources().getColor(R.color.whilte));
                ivBookLine.setBackgroundColor(this.getResources().getColor(R.color.whilte));
                imagebutton_bi.setVisibility(View.VISIBLE);
                index = 0;

                switchFragmentone(FragmentTag.TAG_WORK);
                break;
            case R.id.nurse_second_findtalents:
                tvFindWork.setTextColor(this.getResources().getColor(R.color.gray4));
                tvFindTalent.setTextColor(this.getResources().getColor(R.color.purple));
                tvBook.setTextColor(this.getResources().getColor(R.color.gray4));
                ivFindWorkLine.setBackgroundColor(this.getResources().getColor(R.color.whilte));
                ivFindTalentLine.setBackgroundColor(this.getResources().getColor(R.color.purple));
                ivBookLine.setBackgroundColor(this.getResources().getColor(R.color.whilte));
                imagebutton_bi.setVisibility(View.VISIBLE);
                index = 1;
                switchFragmentone(FragmentTag.TAG_TALENT);
                break;
            case R.id.nurse_second_book:
                tvFindWork.setTextColor(this.getResources().getColor(R.color.gray4));
                tvFindTalent.setTextColor(this.getResources().getColor(R.color.gray4));
                tvBook.setTextColor(this.getResources().getColor(R.color.purple));
                ivFindWorkLine.setBackgroundColor(this.getResources().getColor(R.color.whilte));
                ivFindTalentLine.setBackgroundColor(this.getResources().getColor(R.color.whilte));
                ivBookLine.setBackgroundColor(this.getResources().getColor(R.color.purple));
                imagebutton_bi.setVisibility(View.GONE);
                index = 2;
                switchFragmentone(FragmentTag.TAG_BOOK);
                break;
            case R.id.imagebutton_bi:

                if (index == 0) {
                    if (user.getUserid() != null&&user.getUserid().length() > 0){
                        if ("1".equals(user.getUsertype())){
                            Intent intent = new Intent(getActivity(),Add_EmployWork_Fragment.class);
                            startActivity(intent);
                        }else if ("2".equals(user.getUsertype())){
                            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("系统提示").setMessage("您是企业用户，不能编辑个人简历")
                                    .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).create().show();
                        }
                    }else{
                        Intent intent = new Intent(getActivity(),LoginActivity.class);
                        startActivity(intent);
                    }

//                    index = 3;
//                    Log.e("index3", index + "");
                } else if (index == 1) {
                    imagebutton_bi.setClickable(false);
                    if (user.getUserid() != null&&user.getUserid().length() > 0){
                        if ("1".equals(user.getUsertype())){
                            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("系统提示").setMessage("您是个人用户，不能编辑企业信息")
                                    .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).create().show();
                        }else if ("2".equals(user.getUsertype())){
                            if (HttpConnect.isConnnected(getActivity())){
                                new StudyRequest(getActivity(),handler).getCompanyCertify(user.getUserid(),GETCOMANYCERTIFY);
                            }else{
                                Toast.makeText(getActivity(), R.string.net_erroy,Toast.LENGTH_SHORT).show();
                            }

                        }
                    }else{
                        Intent intent = new Intent(getActivity(),LoginActivity.class);
                        startActivity(intent);
                    }
//                    Log.e("index4", index + "");
//                    index = 4;
                } else  // index=2 时
                {
                    Log.e("index_else", index + "");
//                    imagebutton_bi.setVisibility(View.GONE);
                }
//                imagebutton_bi.setVisibility(View.GONE);
                break;
        }

//        if (currentIndex != index) {
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.hide(fragments[currentIndex]);
//            if (!fragments[index].isAdded()) {
//                fragmentTransaction.add(R.id.nurse_secondpage_container, fragments[index]);
//            }
//            fragmentTransaction.show(fragments[index]);
//            fragmentTransaction.commit();
//        }
//        currentIndex = index;
    }
    public void visible(){
        imagebutton_bi.setVisibility(View.GONE);
    }
    public void visible_gone(){
        imagebutton_bi.setVisibility(View.VISIBLE);
    }

    /**
     * 切换Fragment
     *
     * @param to
     *            目标Fragment
     */
    public void switchFragmentone(FragmentTag to) {
        if (to != null) {
            if (!mCurrentTag.equals(to)) {
                Fragment currentF = getChildFragmentManager().findFragmentByTag(
                        mCurrentTag.getTag());
                Fragment toF = getChildFragmentManager().findFragmentByTag(to.getTag());
                if (null == toF) { // 先判断是否被add过
                    try {
                        // 切换按钮动画
                        // 更新当前Fragment
                        mCurrentTag = to;
                        mCurrentFragment = toF;
                        // 未add过，使用反射新建一个Fragment并add到FragmentManager中
                        toF = (Fragment) Class.forName(to.getTag()).newInstance();
                        getChildFragmentManager().beginTransaction().hide(currentF)
                                .add(R.id.nurse_secondpage_container, toF, to.getTag()).commit(); // 隐藏当前的fragment，add下一个到Activity中
                        // 更新当前Fragment
                        mCurrentTag = to;
                        mCurrentFragment = toF;
                    } catch (Exception e) {
                    }
                } else {
                    // 更新当前Fragment
                    mCurrentTag = to;
                    mCurrentFragment = toF;
                    FragmentTag detailpage = FragmentTag.TAG_DETAILTALENT;
                    if (detailpage.equals(to)||to.equals(FragmentTag.TAG_DETAILWORK)){
                        try {
                        toF = (Fragment) Class.forName(to.getTag()).newInstance();
                        getChildFragmentManager().beginTransaction().hide(currentF)
                                .add(R.id.nurse_secondpage_container, toF, to.getTag()).commit();
                        } catch (Exception e) {
                        }
                    }else {
                        // add过，直接hide当前，并show出目标Fragment
                        getChildFragmentManager().beginTransaction().hide(currentF)
                                .show(toF).commit(); // 隐藏当前的fragment，显示下一个
                    }
                    // 更新当前Fragment
                    mCurrentTag = to;
                    mCurrentFragment = toF;

                }
            }
        }
    }
}
