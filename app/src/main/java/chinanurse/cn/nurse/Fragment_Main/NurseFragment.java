package chinanurse.cn.nurse.Fragment_Main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import chinanurse.cn.nurse.FragmentTag;
import chinanurse.cn.nurse.Fragment_Nurse.CommunityFragment;
import chinanurse.cn.nurse.Fragment_Nurse.activity.MessageActivity;
import chinanurse.cn.nurse.Fragment_Nurse.adapter.MessageAdapter;
import chinanurse.cn.nurse.Fragment_Nurse.bean.MessageBean;
import chinanurse.cn.nurse.Fragment_Nurse.constant.CommunityNetConstant;
import chinanurse.cn.nurse.Fragment_Nurse.net.NurseAsyncHttpClient;
import chinanurse.cn.nurse.LoginActivity;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.utils.LogUtils;
import chinanurse.cn.nurse.utils.ToastUtils;
import cz.msebera.android.httpclient.Header;

/**
 * Created by zhuchongkun on 2016/12/10.
 * 护士站
 */

public class NurseFragment extends Fragment implements View.OnClickListener {
    private UserBean user;
    private TextView tvCommunity, tvEmploy, tv_message;
    private RelativeLayout rl_message;
    private int index = 0;
    private Long lastTime = 0L;
    private Long ceartTime = 0L;
    private String TAG = "NurseFragment";
    SharedPreferences sp;
    /**
     * 当前Fragment的key
     */
    private FragmentTag mCurrentTag;
    /**
     * 当前Fragment
     */
    private Fragment mCurrentFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = View.inflate(getActivity(), R.layout.fragment_nurse, null);
        user = new UserBean(getActivity());
        sp = getActivity().getSharedPreferences("MESSAGE", getActivity().MODE_PRIVATE);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        mCurrentTag = FragmentTag.TAG_COMMUNITY;
        mCurrentFragment = new CommunityFragment();
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.nurse_replace_container, mCurrentFragment,
                        mCurrentTag.getTag()).show(mCurrentFragment).commit();
    }


    private void initView() {
        tvCommunity = (TextView) getView().findViewById(R.id.nurse_first_community);
        tvCommunity.setOnClickListener(this);
        tvEmploy = (TextView) getView().findViewById(R.id.nurse_second_employ);
        tvEmploy.setOnClickListener(this);
        rl_message = (RelativeLayout) getView().findViewById(R.id.rl_nurse_message);
        rl_message.setOnClickListener(this);
        tv_message = (TextView) getView().findViewById(R.id.tv_nurse_message);
        tv_message.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nurse_first_community:
                tvCommunity.setTextColor(this.getResources().getColor(R.color.whilte));
                tvCommunity.setBackgroundResource(R.drawable.nurse_fragment_title_community_press);
                tvEmploy.setTextColor(this.getResources().getColor(R.color.gray8));
                tvEmploy.setBackgroundResource(R.drawable.nurse_fragment_title_employ_normal);
                index = 0;
                switchFragmentone(FragmentTag.TAG_COMMUNITY);
                break;
            case R.id.nurse_second_employ:
                tvCommunity.setTextColor(this.getResources().getColor(R.color.gray8));
                tvCommunity.setBackgroundResource(R.drawable.nurse_fragment_title_community_normal);
                tvEmploy.setTextColor(this.getResources().getColor(R.color.whilte));
                tvEmploy.setBackgroundResource(R.drawable.nurse_fragment_title_employ_press);
                index = 1;
                switchFragmentone(FragmentTag.TAG_EMPLOY);
                break;
            case R.id.rl_nurse_message:
                if (user.getUserid() != null && user.getUserid().length() > 0) {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putLong("lastTime", ceartTime);
                    editor.commit();
                    Intent intentMessge = new Intent();
                    intentMessge.setClass(getActivity(), MessageActivity.class);
                    getActivity().startActivity(intentMessge);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    private void getMessageListByPager() {
//        入参：userid,pager
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", user.getUserid());
        requestParams.put("pager", "1");
        NurseAsyncHttpClient.get(CommunityNetConstant.GET_MY_MESSAGELIST, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    LogUtils.e(TAG, "--getMessageListByPager->" + response);
//                    {
//                        "id": "39",
//                            "from_userid": "",
//                            "centent": "帖子置顶",
//                            "type": "6",
//                            "tid": "62",
//                            "create_time": "1484055827",
//                            "fu_name": null,
//                            "fu_level": null,
//                            "fu_photo": null,
//                            "fu_community_admin": null,
//                            "fu_sex": null,
//                            "forum_title": "其实我想发帖"
//                    },
                    try {
                        if ("success".equals(response.optString("status"))) {
                            JSONArray jsonArray = response.getJSONArray("data");
                            LogUtils.e(TAG, "--JSONArray->" + jsonArray);
                            LogUtils.e(TAG, "--JSONArray->" + jsonArray.length());
                            if (jsonArray.length() > 0) {
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                LogUtils.e(TAG, "--jsonObject->" + jsonObject);
                                ceartTime = jsonObject.getLong("create_time");
                                //TODO 判断最新信息的时间与lastTime 大小
                                lastTime = sp.getLong("lastTime", 0L);
                                //未登录不显示小红点
                                if (ceartTime > lastTime && user.getUserid() != null
                                        && user.getUserid().length() > 0) {
                                    tv_message.setVisibility(View.VISIBLE);
                                } else {
                                    tv_message.setVisibility(View.GONE);
                                }
                            } else {
                                tv_message.setVisibility(View.GONE);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

        });
    }

    /**
     * 切换Fragment
     *
     * @param to 目标Fragment
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
                                .add(R.id.nurse_replace_container, toF, to.getTag()).commit(); // 隐藏当前的fragment，add下一个到Activity中
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
                    if (detailpage.equals(to) || to.equals(FragmentTag.TAG_DETAILWORK)) {
                        try {
                            toF = (Fragment) Class.forName(to.getTag()).newInstance();
                            getChildFragmentManager().beginTransaction().hide(currentF)
                                    .add(R.id.nurse_replace_container, toF, to.getTag()).commit();
                        } catch (Exception e) {
                        }
                    } else {
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

    @Override
    public void onResume() {
        super.onResume();
        StatService.onPageStart(getActivity(), "护士站");
        getMessageListByPager();

    }

    @Override
    public void onPause() {
        super.onPause();
        // 配对页面埋点，与start的页面名称要一致
        StatService.onPageEnd(getActivity(), "护士站");
    }
}
