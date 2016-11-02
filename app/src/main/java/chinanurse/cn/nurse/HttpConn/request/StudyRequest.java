package chinanurse.cn.nurse.HttpConn.request;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.NetUtil;
import chinanurse.cn.nurse.UrlPath.UrlPath;
import chinanurse.cn.nurse.dao.CommunalInterfaces;

/**
 * Created by wzh on 2016/6/19.
 */
public class StudyRequest {
    private Context mContext;
    private Handler handler;

    public StudyRequest(Context context, Handler handler) {
        super();
        this.mContext = context;
        this.handler = handler;


    }
    /**
     * 获得每日一练考试题列表
     *
     * @param userid,type
     * @param KEY
     */
    public void getstudyTitle(final String userid,final String type, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();
            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("type", type));
                String result = HttpConnect.getResponseForPost(UrlPath.STUDY_TITLE, params, mContext);
                Log.i("resultstudy", "------------->" + result);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            }

            ;
        }.start();
    }
    /**
     * 获取首页轮播图片
     *
     * @param typeid
     * @param KEY
     */
    public void getHttpImage(final String typeid, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("g", "apps"));
                params.add(new BasicNameValuePair("m", "index"));
                params.add(new BasicNameValuePair("typeid", typeid));
                String result = HttpConnect.getResponseForPost(UrlPath.news_title_list, params, mContext);
                Log.i("result", "------------->" + result);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            }

            ;
        }.start();
    }

    /**
     * 获取资讯文章列表
     *
     * @param channelid
     * @param KEY
     */
    public void getNewsList(final String channelid,final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            @Override
            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("channelid", channelid));
                String result = HttpConnect.getResponseForPost(UrlPath.news_list, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
                Log.i("resultlist", "------------->" + result);
            }
        }.start();
    }
    /**
     * 获取资讯文章列表
     *
     * @param channelid
     * @param KEY
     */
    public void getNewsListother(final String channelid,final String pager,final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            @Override
            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("channelid", channelid));
                params.add(new BasicNameValuePair("pager", pager));
                String result = HttpConnect.getResponseForPost(UrlPath.news_list, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
                Log.i("resultlist", "------------->" + result);
            }
        }.start();
    }
    /**
     * 获取新闻相关文章列表
     *
     * @param refid
     * @param KEY
     */
    public void getNewsListAbout(final String refid, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            @Override
            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("refid", refid));
                String result = HttpConnect.getResponseForPost(UrlPath.news_list_about, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
                Log.i("resultlistabout", "------------->" + result);
            }
        }.start();
    }

    /**
     * 获取新闻头部列表
     *
     * @param parentid
     * @param KEY
     */
    public void getNewsTitleFirst(final String parentid, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            @Override
            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("parentid", parentid));
                String result = HttpConnect.getResponseForPost(UrlPath.news_firstpage, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
                Log.i("resultlistabout", "------------->" + result);
            }
        }.start();
    }

    /**
     * 登陆
     *
     * @param phone
     * @param password
     */
    public void getLogin(final String phone, final String password, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            @Override
            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("phone", phone));
                params.add(new BasicNameValuePair("password", password));
                String result = HttpConnect.getResponseForPost(UrlPath.isLogin, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
                Log.i("resultlogint", "------------->" + result);
            }
        }.start();
    }

    /**
     * 获取个人资料
     *
     * @param userid
     * @param KEY
     */
    public void getuserinfo(final String userid, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                String result = HttpConnect.getResponseForPost(UrlPath.getuserinfo, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
                Log.i("resultuserinfo", "------------->" + result);
            }

            ;
        }.start();
    }

    /**
     * 注册
     *
     * @param phone
     * @param password
     * @param code
     * @param usertype
     * @param devicestate
     * devicestate
     */
    public void getRegister(final String phone, final String password, final String code, final String usertype, final String devicestate, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            @Override
            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("phone", phone));
                params.add(new BasicNameValuePair("password", password));
                params.add(new BasicNameValuePair("code", code));
                params.add(new BasicNameValuePair("usertype", usertype));
                params.add(new BasicNameValuePair("devicestate", devicestate));
                String result = HttpConnect.getResponseForPost(UrlPath.isRegister, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
                Log.i("resultregister", "------------->" + result);
            }
        }.start();
    }

    /**
     * 判断手机号是否被注册
     *
     * @param phone
     * @param KEY
     */
    public void isCanRegister(final String phone, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("phone", phone));
                String result = HttpConnect.getResponseForPost(UrlPath.isCanRegister, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
                Log.i("canreister", "------------->" + result);
            }

            ;
        }.start();
    }

    /**
     * 发送验证码
     *
     * @param phone
     * @param KEY
     */
    public void sendMobileCode(final String phone, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("phone", phone));
                String result = HttpConnect.getResponseForPost(UrlPath.sendMobileCode, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
                Log.i("code", "------------->" + result);
            }

            ;
        }.start();
    }

    /**
     * 点赞
     *
     * @param userid
     * @param id
     * @param type
     * @param KEY
     */
    public void setLike(final String userid, final String id, final String type, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("id", id));
                params.add(new BasicNameValuePair("type", type));
                String result = HttpConnect.getResponseForPost(UrlPath.setLike, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
                Log.i("result", "------------->" + result);
            }

            ;
        }.start();
    }

    /**
     * 取消点赞
     *
     * @param userid
     * @param id
     * @param type
     * @param KEY
     */
    public void resetLike(final String userid, final String id, final String type, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("id", id));
                params.add(new BasicNameValuePair("type", type));
                String result = HttpConnect.getResponseForPost(UrlPath.resetLike, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
                Log.i("result", "------------->" + result);
            }

            ;
        }.start();
    }

    /**
     * 修改个人资料[姓名]
     *
     * @param userid,nicename
     * @param KEY
     */
    public void updateusername(final String userid, final String nicename, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("nicename", nicename));
                String result = HttpConnect.getResponseForPost(UrlPath.updateusername, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
                Log.i("result", "------------->" + result);
            }

            ;
        }.start();
    }

    /**
     * 修改个人资料[头像]
     *
     * @param userid,avatar
     * @param KEY
     */
    public void updateUserAvatar(final String userid, final String avatar, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("avatar", avatar));
                String result = HttpConnect.getResponseForPost(UrlPath.updateUserAvatar, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
                Log.i("result", "------------->" + result);
            }

            ;
        }.start();
    }

    /**
     * 修改个人资料[性别]
     * (0=>女 1=>男)
     *
     * @param userid,sex
     * @param KEY
     */
    public void updateUserSex(final String userid, final String sex, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("sex", sex));
                String result = HttpConnect.getResponseForPost(UrlPath.updateUserSex, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
                Log.i("result", "------------->" + result);
            }

            ;
        }.start();
    }

    /**
     * 修改个人资料[手机]
     *
     * @param userid,phone
     * @param KEY
     */
    public void updateUserPhone(final String userid, final String phone, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("phone", phone));
                String result = HttpConnect.getResponseForPost(UrlPath.updateUserPhone, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
                Log.i("result", "------------->" + result);
            }

            ;
        }.start();
    }

    /**
     * 修改个人资料[邮箱]
     *
     * @param userid,email
     * @param KEY
     */
    public void updateUserEmail(final String userid, final String email, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("email", email));
                String result = HttpConnect.getResponseForPost(UrlPath.updateUserEmail, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
                Log.i("result", "------------->" + result);
            }

            ;
        }.start();
    }

    /**
     * 修改个人资料[生日]
     *
     * @param userid,birthday
     * @param KEY
     */
    public void updateUserBirthday(final String userid, final String birthday, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("birthday", birthday));
                String result = HttpConnect.getResponseForPost(UrlPath.updateUserBirthday, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
                Log.i("result", "------------->" + result);
            }

            ;
        }.start();
    }

    /**
     * 修改个人资料[地址]
     *
     * @param userid,address
     * @param KEY
     */
    public void updateUserAddress(final String userid, final String address, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("address", address));
                String result = HttpConnect.getResponseForPost(UrlPath.updateUserAddress, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
                Log.i("result", "------------->" + result);
            }

            ;
        }.start();
    }

    /**
     * 修改个人资料[学校]
     *
     * @param userid,school
     * @param KEY
     */
    public void updateUserSchool(final String userid, final String school, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("school", school));
                String result = HttpConnect.getResponseForPost(UrlPath.updateUserSchool, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
                Log.i("result", "------------->" + result);
            }

            ;
        }.start();
    }

    /**
     * 修改个人资料[专业]
     *
     * @param userid,major
     * @param KEY
     */
    public void updateUserMajor(final String userid, final String major, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("major", major));
                String result = HttpConnect.getResponseForPost(UrlPath.updateUserMajor, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
                Log.i("result", "------------->" + result);
            }

            ;
        }.start();
    }

    /**
     * 修改个人资料[学历]
     *
     * @param userid,education
     * @param KEY
     */
    public void updateUserEducation(final String userid, final String education, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("education", education));
                String result = HttpConnect.getResponseForPost(UrlPath.updateUserEducation, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
                Log.i("result", "------------->" + result);
            }

            ;
        }.start();
    }

    /**
     * 上传图片
     *
     * @param img
     * @param KEY
     */
    public void updateUserImg(final String img, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("upfile", img));
                Log.e("YAG", "YAG" + params.get(0).getValue());
                String result = HttpConnect.getResponseForImg(UrlPath.uploadavatar, params, mContext);

                //护士网上传头像的接口 result返回的是null
//                String result = HttpConnect.getResponseForImg("http://nurse.xiaocool.net/index.php?g=apps&m=index&a=uploadavatar", params, mContext);
                Log.i("YAG", "修改用户的头像--->" + img);
                Log.i("YAG", "修改用户的头像--->" + result);
                Log.i("YAG", " http://nurse.xiaocool.net/uploads/microblog/" + img);


                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            }

            ;
        }.start();
    }

    /**
     * 获取每日一练考试题列表
     *
     * @param userid,type,count
     * @param KEY
     */
    public void TEXTLIST(final String userid, final String type, final String count, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("type", type));
                params.add(new BasicNameValuePair("count", count));
                String result = HttpConnect.getResponseForPost(UrlPath.TEXTLIST, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
                Log.i("YAG", "题目数量类型--->" + type + "------>" + userid + "-------->" + count);
                Log.i("YAG", "题目数量类型--->" + result);
            }

            ;
        }.start();
    }

    /**
     * 判断是否收藏过
     * userid,refid,type:1、新闻、2考试,4论坛帖子,5招聘,6用户
     *
     * @param userid,type,refid
     * @param KEY
     */
    public void ISCOLLECT(final String userid,final String refid, final String type, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("refid", refid));
                params.add(new BasicNameValuePair("type", type));
                String result = HttpConnect.getResponseForPost(UrlPath.ISCOLLECT, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            }

            ;
        }.start();
    }


    /**
     * 提交答案
     *
     * @param userid,type,count
     * @param KEY
     */
    public void SUBMITANSWER(final String userid, final String type, final String count, final String questionlist, final String answerlist, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("type", type));
                params.add(new BasicNameValuePair("count", count));
                params.add(new BasicNameValuePair("questionlist", questionlist));
                params.add(new BasicNameValuePair("answerlist", answerlist));
                String result = HttpConnect.getResponseForPost(UrlPath.SUBMITANSWER, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
                Log.i("YAG", "提交答案--->" + result);
            }

            ;
        }.start();
    }


    /**
     * 点击收藏
     *
     * @param userid,refid,type,title,description userid,refid,type:1、新闻、2考试,title,description
     * @param KEY
     */
    public void COLLEXT(final String userid, final String refid, final String type, final String title, final String description, final int KEY) {
        Log.e("YAG", "______修改用户的头像666--->" + type + userid + description + refid + title);
        new Thread() {
            Message msg = Message.obtain();

            public void run() {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("refid", refid));
                params.add(new BasicNameValuePair("type", type));
                params.add(new BasicNameValuePair("title", title));
                params.add(new BasicNameValuePair("description", description));
                String result = HttpConnect.getResponseForPost(UrlPath.COLLEXT, params, mContext);
                Log.e("YAG", "_____修改用户的头像--->" + result);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            }

            ;
        }.start();
    }

    /**
     * 点击取消收藏收藏
     *
     * @param userid,refid,type userid,refid,type:1、新闻、2考试
     * @param KEY
     */
    public void DELLCOLLEXT(final String userid, final String refid, final String type, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("refid", refid));
                params.add(new BasicNameValuePair("type", type));
                String result = HttpConnect.getResponseForPost(UrlPath.DELLCOLLEXT, params, mContext);
                Log.i("YAG", "修改用户的头像--->" + type + userid + refid);
                Log.i("YAG", "修改用户的头像--->" + result);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            }

            ;
        }.start();
    }


    public void employList() {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "";
                String result_data = NetUtil.getResponse(UrlPath.EMPLOY_LIST, data);
                Log.i("result","=====================>"+result_data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.EMPLOY_LIST;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }
    //发布招聘岗位
    public void send_PublishJob(final String userid, final String companyname, final String companyinfo, final String phone,
                                final String email, final String title, final String jobtype,
                                final String education, final String welfare, final String address,
                                final String count, final String salary, final String description,final String linkman) {
        new Thread() {
            Message msg = Message.obtain();


            public void run() {
                String data = "&userid=" + userid + "&companyname=" + companyname + "&companyinfo=" + companyinfo + "&phone=" + phone
                        + "&email=" + email + "&title=" + title + "&jobtype=" + jobtype + "&education=" + education
                        + "&welfare=" + welfare + "&address=" + address + "&count=" + count + "&salary=" + salary + "&description=" + description+"&linkman="+linkman;
                String result_data = NetUtil.getResponse(UrlPath.PUBLISHJOB, data);
                try {
                    Log.e("data", data);
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.PUBLISHJOB;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    /**
     * 上传简历
     *
     * @param userid,sex,avatar,name,refid,birthday,education,address,experience,certificate,currentsalary,phone,email,hiredate,wantcity,jobstate,wantsalary,wantposition,description
     * @param KEY
     */
    public void send_PublishResume(final String userid,final String sex, final String avatar, final String name,final String birthday,final String education, final String address,
                                   final String experience,final String certificate,final String currentsalary, final String phone,final String email,final String hiredate,
                                   final String wantcity,final String jobstate,final String wantsalary, final String wantposition,final String description,final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("sex", sex));
                params.add(new BasicNameValuePair("avatar", avatar));
                params.add(new BasicNameValuePair("name", name));
                params.add(new BasicNameValuePair("birthday", birthday));
                params.add(new BasicNameValuePair("education", education));
                params.add(new BasicNameValuePair("address", address));
                params.add(new BasicNameValuePair("experience", experience));
                params.add(new BasicNameValuePair("education", education));
                params.add(new BasicNameValuePair("certificate", certificate));
                params.add(new BasicNameValuePair("currentsalary", currentsalary));
                params.add(new BasicNameValuePair("phone", phone));
                params.add(new BasicNameValuePair("email", email));
                params.add(new BasicNameValuePair("hiredate", hiredate));
                params.add(new BasicNameValuePair("jobstate", jobstate));
                params.add(new BasicNameValuePair("wantsalary", wantsalary));
                params.add(new BasicNameValuePair("wantposition", wantposition));
                params.add(new BasicNameValuePair("description", description));
                params.add(new BasicNameValuePair("wantcity", wantcity));
                String result = HttpConnect.getResponseForPost(UrlPath.PUBLISHRESUME, params, mContext);
                Log.i("YAG", "上传简历--->" + result);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            }

            ;
        }.start();
    }
    /**
     * 修改简历
     *
     * @param userid,sex,avatar,name,refid,birthday,education,address,experience,certificate,currentsalary,phone,email,hiredate,wantcity,jobstate,wantsalary,wantposition,description
     * @param KEY
     */
    public void UpdataMyResume(final String userid,final String sex, final String avatar, final String name,final String birthday,final String education, final String address,
                                   final String experience,final String certificate,final String currentsalary, final String phone,final String email,final String hiredate,
                                   final String wantcity,final String jobstate,final String wantsalary, final String wantposition,final String description,final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("sex", sex));
                params.add(new BasicNameValuePair("avatar", avatar));
                params.add(new BasicNameValuePair("name", name));
                params.add(new BasicNameValuePair("birthday", birthday));
                params.add(new BasicNameValuePair("education", education));
                params.add(new BasicNameValuePair("address", address));
                params.add(new BasicNameValuePair("experience", experience));
                params.add(new BasicNameValuePair("education", education));
                params.add(new BasicNameValuePair("certificate", certificate));
                params.add(new BasicNameValuePair("currentsalary", currentsalary));
                params.add(new BasicNameValuePair("phone", phone));
                params.add(new BasicNameValuePair("email", email));
                params.add(new BasicNameValuePair("hiredate", hiredate));
                params.add(new BasicNameValuePair("jobstate", jobstate));
                params.add(new BasicNameValuePair("wantsalary", wantsalary));
                params.add(new BasicNameValuePair("wantposition", wantposition));
                params.add(new BasicNameValuePair("description", description));
                params.add(new BasicNameValuePair("wantcity", wantcity));
                String result = HttpConnect.getResponseForPost(UrlPath.UpdataMyResume, params, mContext);
                Log.i("YAG", "上传简历--->" + result);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            }

            ;
        }.start();
    }


    //投递简历
    public void send_ApplyJob(final String companyid, final String jobid, final String userid) {
        new Thread() {
            Message msg = Message.obtain();


            public void run() {
                String data = "&userid=" + userid + "&jobid=" + jobid + "&companyid=" + companyid;
                String result_data = NetUtil.getResponse(UrlPath.APPLYJOB, data);
                try {
                    Log.e("data", data);
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.APPLYJOB;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }


    public void talentList() {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "";
                String result_data = NetUtil.getResponse(UrlPath.TALENT_LIST, data);
                Log.i("result","=====================>"+result_data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = CommunalInterfaces.TALENT_LIST;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }


    //获取字典列表
    public void get_DictionaryList(final int type) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&type=" + type;
                String result_data = NetUtil.getResponse(UrlPath.DICTIONARYLIST, data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = type;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }


    /**
     * 获取论坛头部分类
     *
     * @param KEY
     */
    public void GETBBSTYPE(final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                String result = HttpConnect.getResponseForPost(UrlPath.GETBBSTYPE, params, mContext);
                Log.i("YAG", "论坛头部分类--->" + result);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            }

            ;
        }.start();
    }

    /**
     * 获取论坛数据列表
     *
     * @param type,ishot beginid(用于分页),type,ishot(热门为1)
     * @param KEY
     */
    public void GETCOMMUNITYLIST(final String type, final String ishot, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
//                params.add(new BasicNameValuePair("beginid", beginid));
                params.add(new BasicNameValuePair("type", type));
                params.add(new BasicNameValuePair("ishot", ishot));
                String result = HttpConnect.getResponseForPost(UrlPath.GETCOMMUNITYLIST, params, mContext);
                Log.i("YAG", "我的数据分类--->" + result);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            }

            ;
        }.start();
    }

    /**
     * 获取论坛帖子详情
     *
     * @param id
     * @param KEY
     */
    public void GETCOMMUNITYDETAILS(final String id, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
//                params.add(new BasicNameValuePair("beginid", beginid));
                params.add(new BasicNameValuePair("id", id));
                String result = HttpConnect.getResponseForPost(UrlPath.GETCOMMUNITYDETAILS, params, mContext);
                Log.i("YAG", "帖子详情--->" + result);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            }

            ;
        }.start();
    }

    /**
     * 发布帖子
     *
     * @param userid,typeid,title,content,picurl userid,typeid(分类),title,content,picurl(照片字符串:a.jpg,b.jpg)
     * @param KEY
     */
    public void POSTCOMMUNITY(final String userid, final String typeid, final String title, final String content, final String picurl, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
//                params.add(new BasicNameValuePair("beginid", beginid));
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("typeid", typeid));
                params.add(new BasicNameValuePair("title", title));
                params.add(new BasicNameValuePair("content", content));
                params.add(new BasicNameValuePair("picurl", picurl));
                String result = HttpConnect.getResponseForPost(UrlPath.POSTCOMMUNITY, params, mContext);
                Log.i("YAG", "帖子详情--->" + result);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            }

            ;
        }.start();
    }


    /**
     * 获取收藏列表
     *
     * @param userid
     * @param KEY
     */
    public void SETCOLLEXT(final String userid, final String type, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("type", type));
                String result = HttpConnect.getResponseForPost(UrlPath.SETCOLLEXT, params, mContext);
                Log.i("YAG", "我的收藏--->" + result);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            }

        }.start();
    }

    /**
     * 获取粉丝列表
     *
     * @param userid
     * @param KEY
     */
    public void GETFANSLIST(final String userid, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                String result = HttpConnect.getResponseForPost(UrlPath.GETFANSLIST, params, mContext);
                Log.i("YAG", "我的收藏--->" + result);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            }

            ;
        }.start();
    }

    /**
     * 获取关注列表
     *
     * @param userid
     * @param KEY
     */
    public void GETATTIONLIST(final String userid, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                String result = HttpConnect.getResponseForPost(UrlPath.GETATTIONLIST, params, mContext);
                Log.i("YAG", "我的关注--->" + result);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            }

            ;
        }.start();
    }


    /*
    *
    * 获取本公司收到简历列表
    * */

    public void getMyReciveResumeList(final String userid, final int key) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&userid=" + userid;
                String result_data = NetUtil.getResponse(UrlPath.GETMYRECIVERESUMELIST, data);
                try {
                    Log.e("result_data", "getMyReciveResumeList" + result_data);
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = key;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    /**
     * 添加评论
     *
     * @param userid，id，content，type，photo userid=600&id=4&content=你好&type=2&photo=9.jpg
     * @param KEY
     */
    public void ADDCOMMENT(final String userid, final String id, final String content, final String type, final String photo, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("id", id));
                params.add(new BasicNameValuePair("content", content));
                params.add(new BasicNameValuePair("type", type));
                params.add(new BasicNameValuePair("photo", photo));
                String result = HttpConnect.getResponseForPost(UrlPath.ADDCOMMENT, params, mContext);
                Log.i("YAG", "添加评论--->" + result);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            }

            ;
        }.start();
    }

    /**
     * 获取
     * 我做过的题
     * type 1每日一练
     * 2  考试题列表
     *
     * @param userid,type,count
     * @param KEY
     */
    public void TEXTLIST_OVER(final String userid, final String type, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("type", type));
                String result = HttpConnect.getResponseForPost(UrlPath.TEXTLIST_OVER, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
                Log.i("YAG", "题目数量类型--->" + type + "------>" + userid);
                Log.i("YAG", "题目数量类型--->" + result);
            }

            ;
        }.start();
    }


    /**
     * 获取
     * 错题
     * type 1每日一练
     * 2  考试题列表
     *
     * @param userid,type,count
     * @param KEY
     */
    public void TEXTLIST_ERROR(final String userid, final String type, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("type", type));
                String result = HttpConnect.getResponseForPost(UrlPath.TEXTLIST_OERROE, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
                Log.i("YAG", "题目数量类型--->" + type + "------>" + userid);
                Log.i("YAG", "题目数量类型--->" + result);
            }

            ;
        }.start();
    }


    /**
     * 获取我的今天是否签到
     */
    public void get_MySignLog(final String userid, final String day, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("day", day));
                String result = HttpConnect.getResponseForPost(UrlPath.GETMYSIGNLOG, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
                Log.i("YAG", "题目数量类型--->" + day + "------>" + userid);
                Log.i("YAG", "题目数量类型--->" + result);
            }

            ;
        }.start();
    }


    /**
     * 签到
     */
    public void get_SignDay(final String userid, final String day, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("day", day));
                String result = HttpConnect.getResponseForPost(UrlPath.SIGNDAY, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
                Log.i("YAG", "题目数量类型--->" + day + "------>" + userid);
                Log.i("YAG", "题目数量类型--->" + result);
            }

            ;
        }.start();
    }


    /**
     * 获取我的消息列表
     *
     * @param KEY
     */
    public void get_systemmessage(final String userid, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
//                params.add(new BasicNameValuePair("beginid", beginid));
                params.add(new BasicNameValuePair("userid", userid));
                String result = HttpConnect.getResponseForPost(UrlPath.GETSYSTEMMESSAGE, params, mContext);
                Log.i("YAG", "我的数据分类--->" + result);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            }

            ;
        }.start();
    }

    /**
     * 检测是否收藏
     * @param userid,refid,type
     *userid,refid,type:1、新闻、2考试,4论坛帖子,5招聘,6用户
     * @param KEY
     */
    public void CheckHadFavorite(final String userid,final String refid,final String type,final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
//                params.add(new BasicNameValuePair("beginid", beginid));
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("refid", refid));
                params.add(new BasicNameValuePair("type", type));
                String result = HttpConnect.getResponseForPost(UrlPath.CheckHadFavorite, params, mContext);
                Log.i("YAG", "我的数据分类wodeshoucang--->" + result);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            }

            ;
        }.start();
    }
    /**
     * 检测是否点赞
     * @param userid,id,type
     *userid,refid,type:1、新闻、2考试,4论坛帖子,5招聘,6用户
     * @param KEY
     */
    public void CheckHadLike(final String userid,final String id,final String type,final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
//                params.add(new BasicNameValuePair("beginid", beginid));
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("id", id));
                params.add(new BasicNameValuePair("type", type));
                String result = HttpConnect.getResponseForPost(UrlPath.CheckHadLike, params, mContext);
                Log.i("YAG", "我的数据分类wodeshoucang--->" + result);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            }

            ;
        }.start();
    }
     /*
    *
    * 获取本公司收到简历列表
    * */

    public void getResumeList(final String userid, final int key) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                String data = "&userid=" + userid;
                String result_data = NetUtil.getResponse(UrlPath.GETMYRECIVERESUMELIST, data);
                try {
                    Log.e("result_data", "getMyReciveResumeList" + result_data);
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = key;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }
    /**
     * 邀请面试
     * @param companyid,jobid,userid
     *companyid,jobid,userid
     * @param KEY
     */
    public void InviteJob(final String companyid,final String jobid,final String userid,final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("companyid", companyid));
                params.add(new BasicNameValuePair("jobid", jobid));
                params.add(new BasicNameValuePair("userid", userid));
                String result = HttpConnect.getResponseForPost(UrlPath.InviteJob, params, mContext);
                Log.i("YAG", "邀请面试--->" + result);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            }

            ;
        }.start();
    }
    /**
     * 发布企业认证
     * @param userid,companyname,companyinfo,create_time,phone,email,linkman,license（照片）
     *userid,companyname,companyinfo,create_time,phone,email,linkman,license（照片）
     * @param KEY
     */
    public void UpdataCompanyCertify(final String userid,final String companyname,final String companyinfo,final String create_time,final String phone,final String email,final String linkman,final String license,final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("companyname", companyname));
                params.add(new BasicNameValuePair("companyinfo", companyinfo));
                params.add(new BasicNameValuePair("create_time", create_time));
                params.add(new BasicNameValuePair("phone", phone));
                params.add(new BasicNameValuePair("email", email));
                params.add(new BasicNameValuePair("linkman", linkman));
                params.add(new BasicNameValuePair("license", license));
                String result = HttpConnect.getResponseForPost(UrlPath.UpdataCompanyCertify, params, mContext);
                Log.i("YAG", "邀请面试--->" + result);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            }

            ;
        }.start();
    }
    /**
     * 获取企业认证状态
     * @param userid,companyname,companyinfo,create_time,phone,email,linkman,license（照片）
     *userid,companyname,companyinfo,create_time,phone,email,linkman,license（照片）
     * @param KEY
     */
    public void getCompanyCertify(final String userid,final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));

                String result = HttpConnect.getResponseForPost(UrlPath.getCompanyCertify, params, mContext);
                Log.i("YAG", "邀请面试--->" + result);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            }

            ;
        }.start();
    }
    /**
     * 获取我的简历
     * @param userid
     * @param KEY
     */
    public void getResumeInfo(final String userid,final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                String result = HttpConnect.getResponseForPost(UrlPath.getResumeInfo, params, mContext);
                Log.i("YAG", "邀请面试--->" + result);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            }

            ;
        }.start();
    }
    /**
     * 判断是否投递简历
     * @param userid，companyid,jobid
     * @param KEY
     */
    public void ApplyJob_judge(final String userid,final String companyid,final String jobid,final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("companyid", companyid));
                params.add(new BasicNameValuePair("jobid", jobid));
                String result = HttpConnect.getResponseForPost(UrlPath.ApplyJob_judge, params, mContext);
                Log.i("YAG", "投递简历--->" + result);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            }

            ;
        }.start();
    }
    /**
     * 忘记密码
     * @param phone,code,password
     * @param KEY
     */
    public void forgetpwd(final String phone,final String code,final String password,final int KEY) {
        new Thread() {
            Message msg = Message.obtain();
            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("phone", phone));
                params.add(new BasicNameValuePair("code", code));
                params.add(new BasicNameValuePair("password", password));
                String result = HttpConnect.getResponseForPost(UrlPath.forgetpwd, params, mContext);
                Log.i("YAG", "忘记密码--->" + result);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        }.start();
    }
    /**
     * 判断是否邀请面试
     * @param userid，companyid,jobid
     * @param KEY
     */
    public void InviteJob_judge(final String userid,final String companyid,final String jobid,final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("companyid", companyid));
                params.add(new BasicNameValuePair("jobid", jobid));
                String result = HttpConnect.getResponseForPost(UrlPath.InviteJob_judge, params, mContext);
                Log.i("YAG", "投递简历--->" + result);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            }

            ;
        }.start();
    }
    /**
     * 企业获取本公司发布的招聘岗位列表
     * @param userid，companyid,jobid
     * @param KEY
     */
    public void getMyPublishJobList(final String userid,final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                String result = HttpConnect.getResponseForPost(UrlPath.getMyPublishJobList, params, mContext);
                Log.i("YAG", "投递简历--->" + result);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        }.start();
    }
    /**
     * 修改个人资料[真实姓名]
     *
     * @param userid,realname
     * @param KEY
     */
    public void UpdateUserRealName(final String userid, final String realname, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("realname", realname));
                String result = HttpConnect.getResponseForPost(UrlPath.UpdateUserRealName, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
                Log.i("result", "------------->" + result);
            }

            ;
        }.start();
    }
    /**
     * 获取个人积分详情
     *
     * @param userid
     * @param KEY
     */
    public void getRanking_User(final String userid, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                String result = HttpConnect.getResponseForPost(UrlPath.getRanking_User, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
                Log.i("result", "------------->" + result);
            }

            ;
        }.start();
    }
    /**
     * 获取积分排行榜
     *
     * @param KEY
     */
    public void getRankingList( final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                String result = HttpConnect.getResponseForPost(UrlPath.getRankingList, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
                Log.i("result", "------------->" + result);
            }

            ;
        }.start();
    }
    /**
     *邀请页面
     *@param userid
     * @param KEY
     */
    public void scorepengyou(final String userid,final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                String result = HttpConnect.getResponseForPost(UrlPath.scorepengyou, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
                Log.i("result", "------------->" + result);
            }

            ;
        }.start();
    }
    /**
     *邀请页面
     *@param userid
     * @param KEY
     */
    public void ADDSCORE(final String userid,final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                String result = HttpConnect.getResponseForPost(UrlPath.addscore, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
                Log.i("result", "------------->" + result);
            };
        }.start();
    }
    /**
     *邀请页面
     *@param userid,remarks
     * @param KEY
     */
    public void ADDSCORE_read(final String userid,final String remarks,final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("remarks", remarks));
                String result = HttpConnect.getResponseForPost(UrlPath.Addscore, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
                Log.i("resultread", "------------->" + result);
            }

            ;
        }.start();
    }
    /**
     *获取评论
     *@param refid
     * @param KEY
     */
    public void GETrefcomments(final String refid,final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("refid", refid));
                String result = HttpConnect.getResponseForPost(UrlPath.GETrefcomments, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
                Log.i("resultread", "------------->" + result);
            }

            ;
        }.start();
    }
    /**
     *个人收到我获取的邀请面试列表
     *@param userid
     * @param KEY
     */
    public void UserGetInvite(final String userid,final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                String result = HttpConnect.getResponseForPost(UrlPath.UserGetInvite, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
                Log.i("resultread", "------------->" + result);
            }

            ;
        }.start();
    }
    /**
     *获取我的综合正确率
     *@param userid
     * @param KEY
     */
    public void getSynAccuracy(final String userid,final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                String result = HttpConnect.getResponseForPost(UrlPath.getSynAccuracy, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
                Log.i("resultread", "------------->" + result);
            }

            ;
        }.start();
    }
    /**
     *获取我的综合正确率
     *@param userid,type
     * @param KEY
     */
    public void GetExampaper(final String userid,final String type,final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("type", type));
                String result = HttpConnect.getResponseForPost(UrlPath.GetExampaper, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
                Log.i("resultread", "------------->" + result);
            }

            ;
        }.start();
    }
    /**
     *获取折线图数据
     *@param userid,
     * @param KEY
     */
    public void GetLineChartData(final String userid,final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                String result = HttpConnect.getResponseForPost(UrlPath.GetLineChartData, params, mContext);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
                Log.i("GetLineChartData", "------------->" + result);
            }

            ;
        }.start();
    }
    /**
     * 获取设置已读
     *
     * @param KEY
     */
    public void GETREAD(final String userid,final String refid, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("refid", refid));
                params.add(new BasicNameValuePair("userid", userid));
                String result = HttpConnect.getResponseForPost(UrlPath.GETREAD, params, mContext);
                Log.i("YAG", "我的数据分类--->" + result);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            }

            ;
        }.start();
    }
    /**
     * 判断已读
     *
     * @param KEY
     */
    public void Messageread_judge(final String userid,final String refid, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("refid", refid));
                String result = HttpConnect.getResponseForPost(UrlPath.Messageread_judge, params, mContext);
                Log.i("YAG", "我的数据分类--->" + result);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            }

            ;
        }.start();
    }
    /**
     * 反馈信息
     * @param userid,companyname,companyinfo,create_time,phone,email,linkman,license（照片）
     *userid,companyname,companyinfo,create_time,phone,email,linkman,license（照片）
     * @param KEY
     */
    public void addfeedback(final String userid,final String content,final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("content", content));
                String result = HttpConnect.getResponseForPost(UrlPath.addfeedback, params, mContext);
                Log.i("YAG", "邀请面试--->" + result);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            }

            ;
        }.start();
    }
    /**
     * 获取我的消息已读列表
     *
     * @param KEY
     */
    public void getMessagereadlist(final String userid, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
//                params.add(new BasicNameValuePair("beginid", beginid));
                params.add(new BasicNameValuePair("userid", userid));
                String result = HttpConnect.getResponseForPost(UrlPath.getMessagereadlist, params, mContext);
                Log.i("YAG", "我的数据分类--->" + result);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            }

            ;
        }.start();
    }
    /**
     * 删除我的消息
     *
     * @param KEY
     */
    public void delMySystemMessag(final String userid,final String refid, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
//                params.add(new BasicNameValuePair("beginid", beginid));
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("refid", refid));
                String result = HttpConnect.getResponseForPost(UrlPath.delMySystemMessag, params, mContext);
                Log.i("YAG", "我的数据分类--->" + result);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            }

            ;
        }.start();
    }
    /**
     * 护理部唯独数据
     *
     * @param KEY
     */
    public void getNewslist_count(final String userid,final String time, final int KEY) {
        new Thread() {
            Message msg = Message.obtain();

            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
//                params.add(new BasicNameValuePair("beginid", beginid));
                params.add(new BasicNameValuePair("userid", userid));
                params.add(new BasicNameValuePair("time", time));
                String result = HttpConnect.getResponseForPost(UrlPath.getNewslist_count, params, mContext);
                Log.i("YAG", "我的数据分类--->" + result);
                msg.what = KEY;
                msg.obj = result;
                handler.sendMessage(msg);
            }

            ;
        }.start();
    }
}
