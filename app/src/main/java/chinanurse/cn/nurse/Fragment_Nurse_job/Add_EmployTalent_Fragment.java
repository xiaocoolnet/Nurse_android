package chinanurse.cn.nurse.Fragment_Nurse_job;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import chinanurse.cn.nurse.HttpConn.NetUtil;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.NursePage.sheng_shi_qu.City;
import chinanurse.cn.nurse.NursePage.sheng_shi_qu.District;
import chinanurse.cn.nurse.NursePage.sheng_shi_qu.Provence;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.utils.RegexUtil;
import chinanurse.cn.nurse.WebView.News_WebView_url;
import chinanurse.cn.nurse.adapter.Spinner_Adapter;
import chinanurse.cn.nurse.bean.News_list_type;
import chinanurse.cn.nurse.bean.Spinner_Bean;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.bean.mine_main_bean.Company_news_beans;
import chinanurse.cn.nurse.dao.CommunalInterfaces;
import chinanurse.cn.nurse.popWindow.Pop_position_location;

/**
 * Created by Administrator on 2016/7/11 0011.
 */
//public class Add_EmployTalent_Fragment extends Fragment implements View.OnClickListener {
public class Add_EmployTalent_Fragment extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final int GETCOMANYCERTIFY = 12;
    //二级、三级联动要用的
    private List<Provence> provences;
    private List<City> cities;
    private List<District> districts;
    private Provence provence;
    private City city;
    private District district;
    ArrayAdapter<Provence> adapter01;
    ArrayAdapter<City> adapter02;
    ArrayAdapter<District> adapter03;
    private String main_name, sheng_name, shi_name, qu_name;
    private ProgressDialog dialog;

    /**
     * 推送消息发送广播
     */
    private MyReceiver receiver;
    private News_list_type.DataBean newstypebean;



    private EditText ed_position_name, ed_company_name, ed_company_jianjie, ed_phone, ed_email, ed_work_address, tv_zhiwei_02,ed_company_lianxiren;
    private RelativeLayout re_tijiao;
    private UserBean user;

    private Spinner tv_work_02, tv_work_tiaojian_02, tv_fuli_02, tv_person_num_02, tv_meney_daiyu_02,tv_work_jobtime;

    private List<Spinner_Bean.DataBean> data_list_07, data_list_08, data_list_09, data_list_10, data_list_11,data_list_15;
    private Spinner_Adapter spinner_adapter;
    public static final int typeid_7 = 7, typeid_8 = 8, typeid_9 = 9, typeid_10 = 10, typeid_11 = 11,typeid_15 = 15;
    private Activity mactivity;
    private RelativeLayout btn_back;
    private TextView top_title, sheng_spinner, shi_spinner, qu_spinner, textview, tv;
    private LinearLayout layout_spinner;
    private ArrayAdapter<String> tv_work02, tv_work_tiaojian02, tv_fuli02, person_num, money_daiyu,jobtimeadapter;
    private List<String> jobnamelist, tiaojian02list, fulilist, personnumlist, moneydaiyulist,jobtimelist;
    private Pop_position_location popositon;
    private Company_news_beans.DataBean combean;
    private Boolean isname = true;
    private Boolean isnameone = true;


    /*
       *send_PublishJob方法参数 顺序
       (String userid, String companyname, String companyinfo, String phone, String email, String title, String jobtype,
               String education, String welfare, String address, String count, String salary, String description)
       * */
    private String userid, companyname, qiye_name, work_tiaojian, companyinfo, phone, email, position_name, jobtype, education, welfare, address, count, salary,jobtime, description,linkman;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommunalInterfaces.PUBLISHJOB:
                    Log.e("data", "-----------------------------5555");
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    if(jsonObject != null){
                    try {
                        String status = jsonObject.getString("status");
                        if (status.equals("success")) {
                            Toast.makeText(mactivity, "提交成功", Toast.LENGTH_SHORT).show();
                            re_tijiao.setClickable(true);
                            dialog.dismiss();
                            mactivity.finish();
                            //清空数据
//                            ed_position_name.setText("");
//                            ed_company_name.setText("");
//                            ed_company_jianjie.setText("");
//                            ed_phone.setText("");
//                            ed_email.setText("");
//                            ed_work_address.setText("");
//                            tv_zhiwei_02.setText("");

                        }else{
                            re_tijiao.setClickable(true);
                            dialog.dismiss();
                        }
                    } catch (JSONException e) {
                        re_tijiao.setClickable(true);
                        dialog.dismiss();
                        e.printStackTrace();
                    }
                    }else{
                        Toast.makeText(mactivity,R.string.net_erroy,Toast.LENGTH_SHORT).show();
                        re_tijiao.setClickable(true);
                        dialog.dismiss();
                    }
                    break;
                case typeid_7:

                    JSONObject jsonObject1 = (JSONObject) msg.obj;
                    if (jsonObject1 != null){
                    try {
                        String status = jsonObject1.getString("status");
                        if (status.equals("success")) {
                            JSONArray jsonArray = jsonObject1.getJSONArray("data");
                            data_list_07 = new ArrayList<Spinner_Bean.DataBean>();
                            jobnamelist = new ArrayList<String>();
                            jobnamelist.add("请选择");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jo = jsonArray.getJSONObject(i);
                                Spinner_Bean.DataBean dataBean = new Spinner_Bean.DataBean();
                                dataBean.setId(jo.getString("id"));
                                dataBean.setName(jo.getString("name"));
                                data_list_07.add(dataBean);
                                jobnamelist.add(jo.getString("name"));
                            }
                            tv_work02 = new ArrayAdapter<String>(mactivity, R.layout.spinner_layout_ymd, jobnamelist);
                            tv_work02.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                            tv_work_02.setAdapter(tv_work02);
                            tv_work02.notifyDataSetChanged();
//                            spinner_adapter = new Spinner_Adapter(mactivity, data_list_07);
//                            tv_work_02.setAdapter(spinner_adapter);
//                            spinner_adapter.notifyDataSetChanged();
                            tv_work_02.setSelection(0);
                            companyname = jobnamelist.get(0);
                        }else{
                            jobnamelist = new ArrayList<String>();
                            jobnamelist.add("请选择");
                            tv_work02 = new ArrayAdapter<String>(mactivity, R.layout.spinner_layout_ymd, jobnamelist);
                            tv_work02.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                            tv_work_02.setAdapter(tv_work02);
                            tv_work02.notifyDataSetChanged();
//                            spinner_adapter = new Spinner_Adapter(mactivity, data_list_07);
//                            tv_work_02.setAdapter(spinner_adapter);
//                            spinner_adapter.notifyDataSetChanged();
                            tv_work_02.setSelection(0);
                            companyname = jobnamelist.get(0);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }}else{
                        jobnamelist = new ArrayList<String>();
                        jobnamelist.add("请选择");
                        tv_work02 = new ArrayAdapter<String>(mactivity, R.layout.spinner_layout_ymd, jobnamelist);
                        tv_work02.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                        tv_work_02.setAdapter(tv_work02);
                        tv_work02.notifyDataSetChanged();
//                            spinner_adapter = new Spinner_Adapter(mactivity, data_list_07);
//                            tv_work_02.setAdapter(spinner_adapter);
//                            spinner_adapter.notifyDataSetChanged();
                        tv_work_02.setSelection(0);
                        companyname = jobnamelist.get(0);
                    }
                    break;
                case typeid_15:

                    JSONObject jsonObject15 = (JSONObject) msg.obj;
                    if (jsonObject15 != null){
                        try {
                            String status = jsonObject15.getString("status");
                            if (status.equals("success")) {
                                JSONArray jsonArray = jsonObject15.getJSONArray("data");
                                data_list_15 = new ArrayList<Spinner_Bean.DataBean>();
                                jobtimelist = new ArrayList<String>();
                                jobtimelist.add("请选择");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jo = jsonArray.getJSONObject(i);
                                    Spinner_Bean.DataBean dataBean = new Spinner_Bean.DataBean();
                                    dataBean.setId(jo.getString("id"));
                                    dataBean.setName(jo.getString("name"));
                                    data_list_15.add(dataBean);
                                    jobtimelist.add(jo.getString("name"));
                                }
                                jobtimeadapter = new ArrayAdapter<String>(mactivity, R.layout.spinner_layout_ymd, jobtimelist);
                                jobtimeadapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                                tv_work_jobtime.setAdapter(jobtimeadapter);
                                jobtimeadapter.notifyDataSetChanged();
//                            spinner_adapter = new Spinner_Adapter(mactivity, data_list_07);
//                            tv_work_02.setAdapter(spinner_adapter);
//                            spinner_adapter.notifyDataSetChanged();
                                tv_work_jobtime.setSelection(0);
                                companyname = jobtimelist.get(0);
                            }else{
                                jobtimelist = new ArrayList<String>();
                                jobtimelist.add("请选择");
                                jobtimeadapter = new ArrayAdapter<String>(mactivity, R.layout.spinner_layout_ymd, jobtimelist);
                                jobtimeadapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                                tv_work_jobtime.setAdapter(jobtimeadapter);
                                jobtimeadapter.notifyDataSetChanged();
//                            spinner_adapter = new Spinner_Adapter(mactivity, data_list_07);
//                            tv_work_02.setAdapter(spinner_adapter);
//                            spinner_adapter.notifyDataSetChanged();
                                tv_work_jobtime.setSelection(0);
                                companyname = jobtimelist.get(0);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }}else{
                        jobtimelist = new ArrayList<String>();
                        jobtimelist.add("请选择");
                        jobtimeadapter = new ArrayAdapter<String>(mactivity, R.layout.spinner_layout_ymd, jobtimelist);
                        jobtimeadapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                        tv_work_jobtime.setAdapter(tv_work02);
                        jobtimeadapter.notifyDataSetChanged();
//                            spinner_adapter = new Spinner_Adapter(mactivity, data_list_07);
//                            tv_work_02.setAdapter(spinner_adapter);
//                            spinner_adapter.notifyDataSetChanged();
                        tv_work_jobtime.setSelection(0);
                        companyname = jobtimelist.get(0);
                    }
                    break;
                case typeid_8:

                    JSONObject jsonObject1_8 = (JSONObject) msg.obj;
                    if (jsonObject1_8 != null){
                    try {
                        String status = jsonObject1_8.getString("status");
                        if (status.equals("success")) {
                            JSONArray jsonArray = jsonObject1_8.getJSONArray("data");
                            data_list_08 = new ArrayList<Spinner_Bean.DataBean>();
                            tiaojian02list = new ArrayList<String>();
                            tiaojian02list.add("请选择");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jo = jsonArray.getJSONObject(i);
                                Spinner_Bean.DataBean dataBean = new Spinner_Bean.DataBean();
                                dataBean.setId(jo.getString("id"));
                                dataBean.setName(jo.getString("name"));
                                data_list_08.add(dataBean);
                                tiaojian02list.add(jo.getString("name"));
                            }
//                            spinner_adapter = new Spinner_Adapter(mactivity, data_list_08);
//                            tv_work_tiaojian_02.setAdapter(spinner_adapter);
//                            spinner_adapter.notifyDataSetChanged();
                            tv_work_tiaojian02 = new ArrayAdapter<String>(mactivity, R.layout.spinner_layout_ymd, tiaojian02list);
                            tv_work_tiaojian02.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                            tv_work_tiaojian_02.setAdapter(tv_work_tiaojian02);
                            tv_work_tiaojian02.notifyDataSetChanged();
                            tv_work_tiaojian_02.setSelection(0);
                            work_tiaojian = tiaojian02list.get(0);

                        }else{
                            tiaojian02list = new ArrayList<String>();
                            tiaojian02list.add("请选择");
                            tv_work_tiaojian02 = new ArrayAdapter<String>(mactivity, R.layout.spinner_layout_ymd, tiaojian02list);
                            tv_work_tiaojian02.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                            tv_work_tiaojian_02.setAdapter(tv_work_tiaojian02);
                            tv_work_tiaojian02.notifyDataSetChanged();
                            tv_work_tiaojian_02.setSelection(0);
                            work_tiaojian = tiaojian02list.get(0);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }}else{
                        tiaojian02list = new ArrayList<String>();
                        tiaojian02list.add("请选择");
                        tv_work_tiaojian02 = new ArrayAdapter<String>(mactivity, R.layout.spinner_layout_ymd, tiaojian02list);
                        tv_work_tiaojian02.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                        tv_work_tiaojian_02.setAdapter(tv_work_tiaojian02);
                        tv_work_tiaojian02.notifyDataSetChanged();
                        tv_work_tiaojian_02.setSelection(0);
                        work_tiaojian = tiaojian02list.get(0);
                    }
                    break;
                case typeid_9:

                    JSONObject jsonObject1_9 = (JSONObject) msg.obj;
                    if (jsonObject1_9 != null){
                    try {
                        String status = jsonObject1_9.getString("status");
                        if (status.equals("success")) {
                            JSONArray jsonArray = jsonObject1_9.getJSONArray("data");
                            data_list_09 = new ArrayList<Spinner_Bean.DataBean>();
                            fulilist = new ArrayList<String>();
                            fulilist.add("请选择");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jo = jsonArray.getJSONObject(i);
                                Spinner_Bean.DataBean dataBean = new Spinner_Bean.DataBean();
                                dataBean.setId(jo.getString("id"));
                                dataBean.setName(jo.getString("name"));
                                data_list_09.add(dataBean);
                                fulilist.add(jo.getString("name"));
                            }
//                            spinner_adapter = new Spinner_Adapter(mactivity, data_list_09);
//                            tv_fuli_02.setAdapter(spinner_adapter);
//                            spinner_adapter.notifyDataSetChanged();
                            tv_fuli02 = new ArrayAdapter<String>(mactivity, R.layout.spinner_layout_ymd, fulilist);
                            tv_fuli02.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                            tv_fuli_02.setAdapter(tv_fuli02);
                            tv_fuli02.notifyDataSetChanged();
                            tv_fuli_02.setSelection(0);
                            welfare = fulilist.get(0);


                        }else{
                            fulilist = new ArrayList<String>();
                            fulilist.add("请选择");
                            tv_fuli02 = new ArrayAdapter<String>(mactivity, R.layout.spinner_layout_ymd, fulilist);
                            tv_fuli02.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                            tv_fuli_02.setAdapter(tv_fuli02);
                            tv_fuli02.notifyDataSetChanged();
                            tv_fuli_02.setSelection(0);
                            welfare = fulilist.get(0);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }}else{
                        fulilist = new ArrayList<String>();
                        fulilist.add("请选择");
                        tv_fuli02 = new ArrayAdapter<String>(mactivity, R.layout.spinner_layout_ymd, fulilist);
                        tv_fuli02.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                        tv_fuli_02.setAdapter(tv_fuli02);
                        tv_fuli02.notifyDataSetChanged();
                        tv_fuli_02.setSelection(0);
                        welfare = fulilist.get(0);
                    }
                    break;
                case typeid_10:

                    JSONObject jsonObject1_10 = (JSONObject) msg.obj;
                    if (jsonObject1_10 != null){
                    try {
                        String status = jsonObject1_10.getString("status");
                        if (status.equals("success")) {
                            JSONArray jsonArray = jsonObject1_10.getJSONArray("data");
                            data_list_10 = new ArrayList<Spinner_Bean.DataBean>();
                            personnumlist = new ArrayList<String>();
                            personnumlist.add("请选择");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jo = jsonArray.getJSONObject(i);
                                Spinner_Bean.DataBean dataBean = new Spinner_Bean.DataBean();
                                dataBean.setId(jo.getString("id"));
                                dataBean.setName(jo.getString("name"));
                                data_list_10.add(dataBean);
                                personnumlist.add(jo.getString("name"));
                            }
//                            spinner_adapter = new Spinner_Adapter(mactivity, data_list_10);
//                            tv_person_num_02.setAdapter(spinner_adapter);
//                            spinner_adapter.notifyDataSetChanged();
                            person_num = new ArrayAdapter<String>(mactivity, R.layout.spinner_layout_ymd, personnumlist);
                            person_num.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                            tv_person_num_02.setAdapter(person_num);
                            person_num.notifyDataSetChanged();
                            tv_person_num_02.setSelection(0);
                            count = personnumlist.get(0);


                        }else{
                            personnumlist = new ArrayList<String>();
                            personnumlist.add("请选择");
                            person_num = new ArrayAdapter<String>(mactivity, R.layout.spinner_layout_ymd, personnumlist);
                            person_num.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                            tv_person_num_02.setAdapter(person_num);
                            person_num.notifyDataSetChanged();
                            tv_person_num_02.setSelection(0);
                            count = personnumlist.get(0);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }}else{
                        personnumlist = new ArrayList<String>();
                        personnumlist.add("请选择");
                        person_num = new ArrayAdapter<String>(mactivity, R.layout.spinner_layout_ymd, personnumlist);
                        person_num.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                        tv_person_num_02.setAdapter(person_num);
                        person_num.notifyDataSetChanged();
                        tv_person_num_02.setSelection(0);
                        count = personnumlist.get(0);
                    }
                    break;
                case typeid_11:

                    JSONObject jsonObject1_11 = (JSONObject) msg.obj;
                    if (jsonObject1_11 != null){
                    try {
                        String status = jsonObject1_11.getString("status");
                        if (status.equals("success")) {
                            JSONArray jsonArray = jsonObject1_11.getJSONArray("data");
                            data_list_11 = new ArrayList<Spinner_Bean.DataBean>();
                            moneydaiyulist = new ArrayList<String>();
                            data_list_11.clear();
                            moneydaiyulist.clear();
                            moneydaiyulist.add("请选择");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jo = jsonArray.getJSONObject(i);
                                Spinner_Bean.DataBean dataBean = new Spinner_Bean.DataBean();
                                dataBean.setId(jo.getString("id"));
                                String moneyname = jo.getString("name");
                                isname = moneyname.contains("&lt;");
                                isnameone = moneyname.contains("&gt;");
                                if (isname){
                                    dataBean.setName("<"+jo.getString("name").substring(4));
                                    data_list_11.add(dataBean);
                                    moneydaiyulist.add("<"+jo.getString("name").substring(4));
                                }else if (isnameone){
                                    dataBean.setName(">"+jo.getString("name").substring(4));
                                    data_list_11.add(dataBean);
                                    moneydaiyulist.add(">"+jo.getString("name").substring(4));
                                }else{
                                    dataBean.setName(jo.getString("name"));
                                    data_list_11.add(dataBean);
                                    moneydaiyulist.add(jo.getString("name"));
                                }
                            }
//                            spinner_adapter = new Spinner_Adapter(mactivity, data_list_11);
//                            tv_meney_daiyu_02.setAdapter(spinner_adapter);
//                            spinner_adapter.notifyDataSetChanged();
                            money_daiyu = new ArrayAdapter<String>(mactivity, R.layout.spinner_layout_ymd, moneydaiyulist);
                            money_daiyu.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                            tv_meney_daiyu_02.setAdapter(money_daiyu);
                            money_daiyu.notifyDataSetChanged();
                            tv_meney_daiyu_02.setSelection(0);
                            salary = moneydaiyulist.get(0);
                        }else{
                            moneydaiyulist = new ArrayList<String>();
                            moneydaiyulist.add("请选择");
                            money_daiyu = new ArrayAdapter<String>(mactivity, R.layout.spinner_layout_ymd, moneydaiyulist);
                            money_daiyu.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                            tv_meney_daiyu_02.setAdapter(money_daiyu);
                            money_daiyu.notifyDataSetChanged();
                            tv_meney_daiyu_02.setSelection(0);
                            salary = moneydaiyulist.get(0);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }}else{
                        moneydaiyulist = new ArrayList<String>();
                        moneydaiyulist.add("请选择");
                        money_daiyu = new ArrayAdapter<String>(mactivity, R.layout.spinner_layout_ymd, moneydaiyulist);
                        money_daiyu.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                        tv_meney_daiyu_02.setAdapter(money_daiyu);
                        money_daiyu.notifyDataSetChanged();
                        tv_meney_daiyu_02.setSelection(0);
                        salary = moneydaiyulist.get(0);
                    }
                    break;
                case GETCOMANYCERTIFY:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            String data = json.getString("data");
                            if ("success".equals(json.optString("status"))) {
                                JSONObject item = new JSONObject(data);
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
                                if (!"".equals(item.getString("status")) && item.getString("status") != null) {
                                    if ("1".equals(item.getString("status"))) {
                                        ed_company_name.setText(combean.getCompanyname()+"");
                                        ed_company_jianjie.setText(combean.getCompanyinfo()+"");
                                        ed_phone.setText(combean.getPhone()+"");
                                        ed_email.setText(combean.getEmail() + "");
                                        ed_company_lianxiren.setText(combean.getLinkman() + "");
//                                     company_post_photo
                                        enable(false);
                                    } else {
                                        ed_company_name.setText("");
                                        ed_company_jianjie.setText("");
                                        ed_phone.setText("");
                                        ed_email.setText("");
                                        ed_company_lianxiren.setText("");
                                        enable(true);
                                    }
                                }
                            }else{
                                ed_company_name.setText("");
                                ed_company_jianjie.setText("");
                                ed_phone.setText("");
                                ed_email.setText("");
                                ed_company_lianxiren.setText("");
                                enable(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        ed_company_name.setText("");
                        ed_company_jianjie.setText("");
                        ed_phone.setText("");
                        ed_email.setText("");
                        ed_company_lianxiren.setText("");
                        enable(true);
                }
                    break;
            }
        }
    };


//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View mView = View.inflate(getActivity(), R.layout.add_employtalent_fragment, null);
//        mactivity = this.getActivity();
//        user = new UserBean(mactivity);
//        initData(mView);
//        return mView;
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_employtalent_fragment);
        mactivity = this;
        user = new UserBean(mactivity);
        popositon = new Pop_position_location(Add_EmployTalent_Fragment.this);
        initData();
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter("com.USER_ACTION");
        registerReceiver(receiver, filter);
    }

    private void initData() {
        dialog = new ProgressDialog(mactivity, android.app.AlertDialog.THEME_HOLO_LIGHT);
        btn_back = (RelativeLayout) findViewById(R.id.btn_back);//返回
        btn_back.setOnClickListener(this);
        top_title = (TextView) findViewById(R.id.top_title);//标题
        top_title.setText("编辑企业信息");
        ed_position_name = (EditText) findViewById(R.id.ed_position_name);//职位名称
        ed_company_name = (EditText) findViewById(R.id.ed_company_name);//企业名称
        ed_company_jianjie = (EditText) findViewById(R.id.ed_company_jianjie);//企业简介
        ed_company_lianxiren = (EditText) findViewById(R.id.ed_company_lianxiren);//企业联系人
        ed_phone = (EditText) findViewById(R.id.ed_phone);//联系电话
        ed_email = (EditText) findViewById(R.id.ed_email);//联系邮箱
        layout_spinner = (LinearLayout) findViewById(R.id.layout_spinner);//三级联动触发(点击)
        layout_spinner.setOnClickListener(this);
        sheng_spinner = (TextView) findViewById(R.id.sheng_spinner);//省
        shi_spinner = (TextView) findViewById(R.id.shi_spinner);//市
        qu_spinner = (TextView) findViewById(R.id.qu_spinner);//区
        ed_work_address = (EditText) findViewById(R.id.ed_work_address);//街道详细地址

        tv_work_02 = (Spinner) findViewById(R.id.tv_work_02);//招聘职位
        tv_work_02.setDropDownVerticalOffset(100);
        tv_work_02.setDropDownWidth(400);
        tv_work_02.setOnItemSelectedListener(this);

        tv_work_tiaojian_02 = (Spinner) findViewById(R.id.tv_work_tiaojian_02);//招聘条件
        tv_work_tiaojian_02.setDropDownVerticalOffset(140);
        tv_work_tiaojian_02.setDropDownWidth(400);
        tv_work_tiaojian_02.setOnItemSelectedListener(this);

        tv_fuli_02 = (Spinner) findViewById(R.id.tv_fuli_02);//福利待遇
        tv_fuli_02.setDropDownVerticalOffset(140);
        tv_fuli_02.setDropDownWidth(400);
        tv_fuli_02.setOnItemSelectedListener(this);

        tv_person_num_02 = (Spinner) findViewById(R.id.tv_person_num_02);//招聘人数
        tv_person_num_02.setDropDownVerticalOffset(140);
        tv_person_num_02.setDropDownWidth(400);
        tv_person_num_02.setOnItemSelectedListener(this);

        tv_meney_daiyu_02 = (Spinner) findViewById(R.id.tv_meney_daiyu_02);//薪资待遇
        tv_meney_daiyu_02.setDropDownVerticalOffset(140);
        tv_meney_daiyu_02.setDropDownWidth(400);
        tv_meney_daiyu_02.setOnItemSelectedListener(this);

        tv_work_jobtime = (Spinner) findViewById(R.id.tv_work_jobtime);//工作年限
        tv_work_jobtime.setDropDownVerticalOffset(140);
        tv_work_jobtime.setDropDownWidth(400);
        tv_work_jobtime.setOnItemSelectedListener(this);


        tv_zhiwei_02 = (EditText) findViewById(R.id.tv_zhiwei_02);

        re_tijiao = (RelativeLayout) findViewById(R.id.re_tijiao);
        re_tijiao.setOnClickListener(this);

        textview = (TextView) findViewById(R.id.textview);
        spinnershow();
    }

    private void enable(boolean isfalse) {
        ed_company_name.setEnabled(isfalse);
        ed_company_jianjie.setEnabled(isfalse);
        ed_company_lianxiren.setEnabled(isfalse);
        ed_phone.setEnabled(isfalse);
        ed_email.setEnabled(isfalse);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                unregisterReceiver(receiver);
                finish();
                break;
            case R.id.layout_spinner://三级联动
                popositon.showAsDropDown(textview);
                break;
            case R.id.re_tijiao://提交
                re_tijiao.setClickable(false);
                submit();
                break;
        }
    }

    private void submit() {
        userid = user.getUserid();
        position_name = ed_position_name.getText().toString();//职位名称
        qiye_name = ed_company_name.getText().toString();//企业名称
        companyinfo = ed_company_jianjie.getText().toString();//企业简介
        linkman = ed_company_lianxiren.getText().toString();//公司联系人
        phone = ed_phone.getText().toString();//企业联系电话
        email = ed_email.getText().toString();//企业联系邮箱
        description = tv_zhiwei_02.getText().toString();//职位要求

        address = sheng_spinner.getText().toString() +"-"+ shi_spinner.getText().toString() +"-"+qu_spinner.getText().toString() +"\t"+ ed_work_address.getText().toString();//工作地点
        if ("请选择".equals(companyname)) {
            companyname = "";
        } else if ("请选择".equals(work_tiaojian)) {
            work_tiaojian = "";
        } else if ("请选择".equals(welfare)) {
            welfare = "";
        } else if ("请选择".equals(count)) {
            count = "";
        } else if ("请选择".equals(salary)) {
            salary = "";
        }
//                education = "本科及以上";
        if (position_name == null || "".equals(position_name)) {
            Toast.makeText(mactivity, "请输入职位名称", Toast.LENGTH_SHORT).show();
        } else if (qiye_name == null || "".equals(qiye_name)) {
            Toast.makeText(mactivity, "请输入企业名称", Toast.LENGTH_SHORT).show();
        } else if (companyinfo == null || "".equals(companyinfo)) {
            Toast.makeText(mactivity, "请输入企业简介", Toast.LENGTH_SHORT).show();
        } else if (linkman == null || "".equals(linkman)) {
            Toast.makeText(mactivity, "请输入联系人", Toast.LENGTH_SHORT).show();
        } else if (email == null || "".equals(email)) {
            email = "";
        }else if (phone == null || "".equals(phone)) {
            Toast.makeText(mactivity, "请输入电话号码", Toast.LENGTH_SHORT).show();
        }else if (!isCanGetCodeemile() ) {
            Toast.makeText(mactivity, "请输入正确的邮箱", Toast.LENGTH_SHORT).show();
        }else if (!ispHONE() ) {
            Toast.makeText(mactivity, "请输入正确的联系电话", Toast.LENGTH_SHORT).show();
        } else if (address == null || "".equals(address)) {
            Toast.makeText(mactivity, "请输入详细地址", Toast.LENGTH_SHORT).show();
        } else if (description == null || "".equals(description)) {
            Toast.makeText(mactivity, "请输入求职要求", Toast.LENGTH_SHORT).show();
        } else if (companyname == null || "".equals(companyname)) {
            Toast.makeText(mactivity, "请选择招聘职位", Toast.LENGTH_SHORT).show();
        } else if (salary == null || "".equals(salary)) {
            Toast.makeText(mactivity, "请选择薪资待遇", Toast.LENGTH_SHORT).show();
        }else if (jobtime == null || "".equals(jobtime)) {
            Toast.makeText(mactivity, "请选择工作年限", Toast.LENGTH_SHORT).show();
        } else {
            if (NetUtil.isConnnected(mactivity)) {
                dialog.setMessage("正在提交...");
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.show();
                new StudyRequest(mactivity, handler).send_PublishJob(userid, qiye_name,companyinfo, phone, email,
                        position_name, companyname, work_tiaojian, welfare, address, count, salary, description,linkman,jobtime);
                re_tijiao.setClickable(true);
            }else{
                re_tijiao.setClickable(true);
            }
        }
        Log.e("companyname", companyname);
        Log.e("userid", userid);
        re_tijiao.setClickable(true);
    }
//    private Boolean isCanGetCode() {
//        if (!RegexUtil.checkMobile(phone)){
//            return false;
//        }
//        return true;
//    }
    private Boolean ispHONE() {
            if (!RegexUtil.checkPhone(phone)) {
                return false;
    }
        return true;
    }
    private Boolean isCanGetCodeemile() {
        if (email == null || "".equals(email)) {
            email = "";
        }else {
            if (!RegexUtil.checkEmail(email)) {
                return false;
            }
        }
        return true;
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.tv_work_02://招聘岗位   用 companyname 来接受， 名称与接口的字段相同(而非“企业名称”)
                companyname = jobnamelist.get(position);
                break;
            case R.id.tv_work_tiaojian_02://招聘条件
                work_tiaojian = tiaojian02list.get(position);
                break;
            case R.id.tv_fuli_02://福利待遇
                welfare = fulilist.get(position);
                break;
            case R.id.tv_person_num_02://招聘人数
                count = personnumlist.get(position);
                break;
            case R.id.tv_meney_daiyu_02://薪资待遇
                salary = moneydaiyulist.get(position);
                break;
            case R.id.tv_work_jobtime://工作年限
                jobtime = jobtimelist.get(position);
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("onResume", "=========>onResume");
        StatService.onPageStart(this, "公司招聘信息编辑");
        spinnershownew();
    }

    private void spinnershownew() {
        if (NetUtil.isConnnected(mactivity)) {
            //字典  招聘职位
            new StudyRequest(mactivity, handler).getCompanyCertify(user.getUserid(), GETCOMANYCERTIFY);
        }
    }

    //
    private void spinnershow() {
        if (NetUtil.isConnnected(mactivity)) {
            //字典  招聘职位
            new StudyRequest(mactivity, handler).get_DictionaryList(typeid_7);
            //字典  招聘条件
            new StudyRequest(mactivity, handler).get_DictionaryList(typeid_8);
            //字典  福利待遇
            new StudyRequest(mactivity, handler).get_DictionaryList(typeid_9);
            //字典  招聘人数
            new StudyRequest(mactivity, handler).get_DictionaryList(typeid_10);
            //字典  薪资待遇
            new StudyRequest(mactivity, handler).get_DictionaryList(typeid_11);
            //工作年限
            new StudyRequest(mactivity, handler).get_DictionaryList(typeid_15);
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
            context.unregisterReceiver(this);
            //            AlertDialog alert = builder.create();
//            alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//            alert.show();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        // 配对页面埋点，与start的页面名称要一致
        StatService.onPageEnd(this, "公司招聘信息编辑");
    }
}
