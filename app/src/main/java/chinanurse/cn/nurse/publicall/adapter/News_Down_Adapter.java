package chinanurse.cn.nurse.publicall.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.LoginActivity;
import chinanurse.cn.nurse.publicall.SecondPage;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.UrlPath.NetBaseConstant;
import chinanurse.cn.nurse.WebView.News_WebView;
import chinanurse.cn.nurse.bean.FirstPageNews;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.imageload.MyApplication;

/**
 * Created by Administrator on 2016/6/22.
 */
public class News_Down_Adapter extends BaseAdapter {
    private static final int REQUEST_CODE = 2;
    private static final int CHECKHADFAVORITEONE = 3;
    private static final int BTNCHECKHADFAVORITEONE = 4;
    private Activity mactivity;
    private List<FirstPageNews.DataBean> fndlist;
    private int typeid;
    private UserBean user;
    private String time, titlename, titletype;
    private String termidnew, termnamenew;
    private SharedPreferences prefences;

    private String webtitleId,web_title,webId,description;
    private String likesize;
    private CommonViewHolder commonHolder;
    private Handler handelerone;
    private int positionone;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.img_bg_nor).showImageOnFail(R.mipmap.img_bg_nor).cacheInMemory(true).cacheOnDisc(true).build();





    public News_Down_Adapter(Activity mactivity, List<FirstPageNews.DataBean> fndlist, int typeid,Handler handler) {
        this.fndlist = fndlist;
        if (null == mactivity || "".equals(mactivity)) {
            Log.e("mactivity", "=============mactivity");
        } else {
            this.mactivity = mactivity;
        }

        this.typeid = typeid;
        this.handelerone = handler;
        user = new UserBean(mactivity);
    }

    @Override
    public int getCount() {
        return fndlist.size();
    }

    @Override
    public Object getItem(int position) {
        return fndlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        this.positionone =  position;
        int layoutType = getItemViewType(position);
        commonHolder = null;
        LayoutInflater inflater = null;
        time = fndlist.get(position).getPost_modified().toString().substring(5, 10);
        prefences=mactivity.getSharedPreferences("nursenum", Context.MODE_PRIVATE);
        String isopen=prefences.getString("isopen",null);

//        View view=
        if (convertView == null) {
            switch (typeid) {
                case 0:
//                    inflater = (LayoutInflater) mactivity.getSystemService(mactivity.LAYOUT_INFLATER_SERVICE);
                    convertView = View.inflate(mactivity, R.layout.news_first_lv_item, null);
                    commonHolder = new CommonViewHolder();
                    //小于3张图片整个布局
                    commonHolder.linearlayout_first = (LinearLayout) convertView.findViewById(R.id.linearlayout_first);
                    //大于3张图片真个布局
                    commonHolder.linearlayout_first_other = (LinearLayout) convertView.findViewById(R.id.linearlayout_first_other);
                    //小于三张布局详情
                    commonHolder.lv_title = (TextView) convertView.findViewById(R.id.lv_titile);
                    commonHolder.lv_refad = (TextView) convertView.findViewById(R.id.lv_read);
                    commonHolder.lv_time = (TextView) convertView.findViewById(R.id.lv_time);
                    commonHolder.tv_term_name = (TextView) convertView.findViewById(R.id.tv_term_name);
//                    commonHolder.lv_content = (TextView) convertView.findViewById(R.id.lv_content);
                    commonHolder.lv_image = (ImageView) convertView.findViewById(R.id.lv_image);
                    //整个布局的点击事件
                    commonHolder.lv_LL_title = (LinearLayout) convertView.findViewById(R.id.linearlayout_title_list);
                    //大于三张布局详情
                    commonHolder.lv_titile_other = (TextView) convertView.findViewById(R.id.lv_titile_other);
                    commonHolder.lv_image_one = (ImageView) convertView.findViewById(R.id.lv_image_one);
                    commonHolder.lv_image_two = (ImageView) convertView.findViewById(R.id.lv_image_two);
                    commonHolder.lv_image_three = (ImageView) convertView.findViewById(R.id.lv_image_three);
                    commonHolder.tv_term_name_other = (TextView) convertView.findViewById(R.id.tv_term_name_other);
                    commonHolder.lv_read_other = (TextView) convertView.findViewById(R.id.lv_read_other);
                    commonHolder.lv_time_other = (TextView) convertView.findViewById(R.id.lv_time_other);
                    break;
                case 1:
//                    convertView = inflater.inflate(R.layout.news_first_lv_item, null);
                    convertView = View.inflate(mactivity, R.layout.news_first_lv_item, null);
                    commonHolder = new CommonViewHolder();
                    commonHolder.lv_title = (TextView) convertView.findViewById(R.id.lv_titile);
                    commonHolder.lv_refad = (TextView) convertView.findViewById(R.id.lv_read);
                    commonHolder.lv_time = (TextView) convertView.findViewById(R.id.lv_time);
                    commonHolder.tv_term_name = (TextView) convertView.findViewById(R.id.tv_term_name);
//                    commonHolder.lv_content = (TextView) convertView.findViewById(R.id.lv_content);
                    commonHolder.lv_image = (ImageView) convertView.findViewById(R.id.lv_image);
                    commonHolder.lv_LL_title = (LinearLayout) convertView.findViewById(R.id.linearlayout_title_list);
                    commonHolder.lv_linear_image = (LinearLayout) convertView.findViewById(R.id.lv_linear_image);//隐藏小于3
                    commonHolder.lv_image_one = (ImageView) convertView.findViewById(R.id.lv_image_one);
                    commonHolder.lv_image_two = (ImageView) convertView.findViewById(R.id.lv_image_two);
                    commonHolder.lv_image_three = (ImageView) convertView.findViewById(R.id.lv_image_three);
                    break;
                case 2:
                    convertView = View.inflate(mactivity, R.layout.news_fourth_adapetr, null);
                    commonHolder = new CommonViewHolder();

                    commonHolder.lv_collect_image = (ImageView) convertView.findViewById(R.id.four_image_collect);//收藏图标
                    commonHolder.lv_collect_num = (TextView) convertView.findViewById(R.id.news_fourth_collect);//收藏数量
                    commonHolder.lv_like_image = (ImageView) convertView.findViewById(R.id.four_image_like);//点赞图标
                    commonHolder.lv_like = (TextView) convertView.findViewById(R.id.news_fourth_like);//点赞数量

                    commonHolder.lv_title = (TextView) convertView.findViewById(R.id.news_fourth_title);
                    commonHolder.lv_refad = (TextView) convertView.findViewById(R.id.new_fourth_attention);
                    commonHolder.lv_time = (TextView) convertView.findViewById(R.id.new_fourth_time);
                    commonHolder.lv_image = (ImageView) convertView.findViewById(R.id.news_fourth_lv_title_image);
                    commonHolder.lv_LL_title = (LinearLayout) convertView.findViewById(R.id.news_fourth_linearlayout);
                    commonHolder.textview = (TextView) convertView.findViewById(R.id.textview);
                    break;
                case 3:
//                    convertView = inflater.inflate(R.layout.news_first_lv_item, null);
                    convertView = View.inflate(mactivity, R.layout.news_second_lv_item, null);
                    commonHolder = new CommonViewHolder();
                    commonHolder.lv_title = (TextView) convertView.findViewById(R.id.lv_titile);
                    commonHolder.lv_refad = (TextView) convertView.findViewById(R.id.lv_read);
                    commonHolder.lv_time = (TextView) convertView.findViewById(R.id.lv_time);
//                    commonHolder.lv_content = (TextView) convertView.findViewById(R.id.lv_content);
                    commonHolder.lv_image = (ImageView) convertView.findViewById(R.id.lv_image);
                    commonHolder.lv_LL_title = (LinearLayout) convertView.findViewById(R.id.linearlayout_title_list);
                    commonHolder.lv_linear_image = (LinearLayout) convertView.findViewById(R.id.lv_linear_image);//隐藏小于3
                    commonHolder.lv_image_one = (ImageView) convertView.findViewById(R.id.lv_image_one);
                    commonHolder.lv_image_two = (ImageView) convertView.findViewById(R.id.lv_image_two);
                    commonHolder.lv_image_three = (ImageView) convertView.findViewById(R.id.lv_image_three);
                    break;
                //5万道题
                case 4:
                    inflater = (LayoutInflater) mactivity.getSystemService(mactivity.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.item_goman_topic, null);
                    commonHolder = new CommonViewHolder();
                    commonHolder.tv_topic = (TextView) convertView.findViewById(R.id.tv_topic);
                    commonHolder.tv_eye = (TextView) convertView.findViewById(R.id.tv_eye);
                    commonHolder.tv_collect = (TextView) convertView.findViewById(R.id.tv_collect);
                    commonHolder.tv_tv_collect = (TextView) convertView.findViewById(R.id.tv_tv_collect);
                    commonHolder.image_3 = (ImageView) convertView.findViewById(R.id.image_3);
                    commonHolder.image_collect = (ImageView) convertView.findViewById(R.id.image_collect);
                    commonHolder.btn_linear_like = (LinearLayout) convertView.findViewById(R.id.btn_linear_like);
                    commonHolder.btn_linear_collect = (LinearLayout) convertView.findViewById(R.id.btn_linear_collect);
                    break;
                case 5:
                    convertView = View.inflate(mactivity, R.layout.nurse_opreation_adapter, null);
                    commonHolder = new CommonViewHolder();

                    commonHolder.lv_collect_image = (ImageView) convertView.findViewById(R.id.four_image_collect);//收藏图标
                    commonHolder.lv_collect_num = (TextView) convertView.findViewById(R.id.news_fourth_collect);//收藏数量
                    commonHolder.lv_like_image = (ImageView) convertView.findViewById(R.id.four_image_like);//点赞图标
                    commonHolder.lv_like = (TextView) convertView.findViewById(R.id.news_fourth_like);//点赞数量
                    commonHolder.lv_title = (TextView) convertView.findViewById(R.id.news_fourth_title);
                    commonHolder.lv_refad = (TextView) convertView.findViewById(R.id.new_fourth_attention);
                    commonHolder.lv_time = (TextView) convertView.findViewById(R.id.new_fourth_time);
                    commonHolder.lv_image = (ImageView) convertView.findViewById(R.id.news_fourth_lv_title_image);
                    commonHolder.lv_LL_title = (LinearLayout) convertView.findViewById(R.id.news_fourth_linearlayout);
                    commonHolder.textview = (TextView) convertView.findViewById(R.id.textview);
                    break;
                case 6:
//                    inflater = (LayoutInflater) mactivity.getSystemService(mactivity.LAYOUT_INFLATER_SERVICE);
                    convertView = View.inflate(mactivity, R.layout.news_book_fragment_adapter, null);
                    commonHolder = new CommonViewHolder();
                    commonHolder.lv_title = (TextView) convertView.findViewById(R.id.lv_titile);
                    commonHolder.lv_refad = (TextView) convertView.findViewById(R.id.lv_read);
                    commonHolder.tv_term_name = (TextView) convertView.findViewById(R.id.tv_term_name);
//                    commonHolder.lv_content = (TextView) convertView.findViewById(R.id.lv_content);
                    commonHolder.lv_image = (ImageView) convertView.findViewById(R.id.lv_image);//隐藏大于3
                    commonHolder.lv_LL_title = (LinearLayout) convertView.findViewById(R.id.linearlayout_title_list);
                    commonHolder.lv_linear_image = (LinearLayout) convertView.findViewById(R.id.lv_linear_image);//隐藏小于3
                    commonHolder.lv_image_one = (ImageView) convertView.findViewById(R.id.lv_image_one);
                    commonHolder.lv_image_two = (ImageView) convertView.findViewById(R.id.lv_image_two);
                    commonHolder.lv_image_three = (ImageView) convertView.findViewById(R.id.lv_image_three);
                    commonHolder.btn_linear_like = (LinearLayout) convertView.findViewById(R.id.btn_linear_like);
                    commonHolder.btn_linear_collect = (LinearLayout) convertView.findViewById(R.id.btn_linear_collect);
                    commonHolder.image_3 = (ImageView) convertView.findViewById(R.id.image_3);
                    commonHolder.image_collect = (ImageView) convertView.findViewById(R.id.image_collect);
                    commonHolder.tv_collect = (TextView) convertView.findViewById(R.id.tv_collect);
                    commonHolder.tv_tv_collect = (TextView) convertView.findViewById(R.id.tv_tv_collect);
                    break;
            }
            convertView.setTag(commonHolder);
        } else {
            commonHolder = (CommonViewHolder) convertView.getTag();
        }
        switch (typeid) {
            case 0:
                if (fndlist.get(position).getThumb() != null && fndlist.get(position).getThumb().size() > 2) {
                    commonHolder.linearlayout_first.setVisibility(View.GONE);
                    commonHolder.linearlayout_first_other.setVisibility(View.VISIBLE);
                    commonHolder.lv_titile_other.setText(fndlist.get(position).getPost_title().toString() + "");
                    commonHolder.lv_read_other.setText(fndlist.get(position).getPost_hits().toString() + "");
                    commonHolder.lv_time_other.setText(time + "");
                    Log.e("termname", "========================>" + fndlist.get(position).getTerm_name().toString());
                    commonHolder.tv_term_name_other.setText(fndlist.get(position).getTerm_name().toString() + "");
                    commonHolder.tv_term_name_other.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            termidnew = fndlist.get(position).getTerm_id().toString();
                            termnamenew = fndlist.get(position).getTerm_name().toString();
                            Intent intent = new Intent(mactivity, SecondPage.class);
                            intent.putExtra("termid", termidnew);
                            intent.putExtra("termname", termnamenew);
                            mactivity.startActivity(intent);
                        }
                    });
                    if (isopen != null){
                        if ("true".equals(isopen)){
                            //imglist.get(i)为网络图片路径，lv_image为imageview
                            imageLoader.displayImage(NetBaseConstant.NET_PIC_PREFIX_THUMB+fndlist.get(position).getThumb().get(0).getUrl().toString(),commonHolder.lv_image_one,options);
                            imageLoader.displayImage(NetBaseConstant.NET_PIC_PREFIX_THUMB+fndlist.get(position).getThumb().get(1).getUrl().toString(),commonHolder.lv_image_two,options);
                            imageLoader.displayImage(NetBaseConstant.NET_PIC_PREFIX_THUMB+fndlist.get(position).getThumb().get(2).getUrl().toString(),commonHolder.lv_image_three,options);
                        }
                    }else{
                        //imglist.get(i)为网络图片路径，lv_image为imageview
                        imageLoader.displayImage(NetBaseConstant.NET_PIC_PREFIX_THUMB+fndlist.get(position).getThumb().get(0).getUrl().toString(),commonHolder.lv_image_one,options);
                        imageLoader.displayImage(NetBaseConstant.NET_PIC_PREFIX_THUMB+fndlist.get(position).getThumb().get(1).getUrl().toString(),commonHolder.lv_image_two,options);
                        imageLoader.displayImage(NetBaseConstant.NET_PIC_PREFIX_THUMB+fndlist.get(position).getThumb().get(2).getUrl().toString(),commonHolder.lv_image_three,options);
                    }


                } else if (fndlist.get(position).getThumb() != null && 0 < fndlist.get(position).getThumb().size() && fndlist.get(position).getThumb().size() <= 2) {
                    commonHolder.linearlayout_first.setVisibility(View.VISIBLE);
                    commonHolder.linearlayout_first_other.setVisibility(View.GONE);
                    commonHolder.lv_title.setText(fndlist.get(position).getPost_title().toString() + "");
                    commonHolder.lv_refad.setText(fndlist.get(position).getPost_hits().toString() + "");
                    commonHolder.lv_time.setText(time + "");
                    Log.e("termname", "========================>" + fndlist.get(position).getTerm_name().toString());
                    commonHolder.tv_term_name.setText(fndlist.get(position).getTerm_name().toString() + "");
                    commonHolder.tv_term_name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            termidnew = fndlist.get(position).getTerm_id().toString();
                            termnamenew = fndlist.get(position).getTerm_name().toString();
                            Intent intent = new Intent(mactivity, SecondPage.class);
                            intent.putExtra("termid", termidnew);
                            intent.putExtra("termname", termnamenew);
                            mactivity.startActivity(intent);
                        }
                    });
                    if (isopen != null) {
                        if ("true".equals(isopen)) {
                            //imglist.get(i)为网络图片路径，lv_image为imageview
                            imageLoader.displayImage(NetBaseConstant.NET_PIC_PREFIX_THUMB + fndlist.get(position).getThumb().get(0).getUrl().toString(), commonHolder.lv_image, options);
                        }
                    }else{
                        imageLoader.displayImage(NetBaseConstant.NET_PIC_PREFIX_THUMB + fndlist.get(position).getThumb().get(0).getUrl().toString(), commonHolder.lv_image, options);
                    }
                } else {
                    commonHolder.linearlayout_first.setVisibility(View.VISIBLE);
                    commonHolder.linearlayout_first_other.setVisibility(View.GONE);
                    commonHolder.lv_image.setVisibility(View.GONE);
                    commonHolder.lv_title.setText(fndlist.get(position).getPost_title().toString() + "");
                    commonHolder.lv_refad.setText(fndlist.get(position).getPost_hits().toString() + "");
                    commonHolder.lv_time.setText(time + "");
                    Log.e("termname", "========================>" + fndlist.get(position).getTerm_name().toString());
                    commonHolder.tv_term_name.setText(fndlist.get(position).getTerm_name().toString() + "");
                    commonHolder.tv_term_name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            termidnew = fndlist.get(position).getTerm_id().toString();
                            termnamenew = fndlist.get(position).getTerm_name().toString();
                            Intent intent = new Intent(mactivity, SecondPage.class);
                            intent.putExtra("termid", termidnew);
                            intent.putExtra("termname", termnamenew);
                            mactivity.startActivity(intent);
                        }
                    });
                }
                break;
            case 1:
                commonHolder.lv_title.setText(fndlist.get(position).getPost_title().toString() + "");
                commonHolder.lv_refad.setText(fndlist.get(position).getPost_hits().toString() + "");
                commonHolder.lv_time.setText(time + "");
                commonHolder.tv_term_name.setText(titlename + "");
                commonHolder.tv_term_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mactivity, SecondPage.class);
                        intent.putExtra("termid", titletype);
                        intent.putExtra("termname", titlename);
                        mactivity.startActivity(intent);
                    }
                });
                if (fndlist.get(position).getThumb() != null && fndlist.get(position).getThumb().size() > 2) {
                    commonHolder.lv_image.setVisibility(View.GONE);
                    commonHolder.lv_linear_image.setVisibility(View.VISIBLE);
                    if (isopen != null){
                        if ("true".equals(isopen)){
                            //imglist.get(i)为网络图片路径，lv_image为imageview
                            imageLoader.displayImage(NetBaseConstant.NET_PIC_PREFIX_THUMB+fndlist.get(position).getThumb().get(0).getUrl().toString(),commonHolder.lv_image_one,options);
                            imageLoader.displayImage(NetBaseConstant.NET_PIC_PREFIX_THUMB+fndlist.get(position).getThumb().get(1).getUrl().toString(),commonHolder.lv_image_two,options);
                            imageLoader.displayImage(NetBaseConstant.NET_PIC_PREFIX_THUMB+fndlist.get(position).getThumb().get(2).getUrl().toString(),commonHolder.lv_image_three,options);
                        }
                    }else{
                        //imglist.get(i)为网络图片路径，lv_image为imageview
                        imageLoader.displayImage(NetBaseConstant.NET_PIC_PREFIX_THUMB+fndlist.get(position).getThumb().get(0).getUrl().toString(),commonHolder.lv_image_one,options);
                        imageLoader.displayImage(NetBaseConstant.NET_PIC_PREFIX_THUMB+fndlist.get(position).getThumb().get(1).getUrl().toString(),commonHolder.lv_image_two,options);
                        imageLoader.displayImage(NetBaseConstant.NET_PIC_PREFIX_THUMB+fndlist.get(position).getThumb().get(2).getUrl().toString(),commonHolder.lv_image_three,options);
                    }
                    } else if (fndlist.get(position).getThumb() != null && 0 < fndlist.get(position).getThumb().size() && fndlist.get(position).getThumb().size() <= 2) {
                    commonHolder.lv_image.setVisibility(View.VISIBLE);
                    commonHolder.lv_linear_image.setVisibility(View.GONE);
                    commonHolder.lv_title.setHeight(170);
                    if (isopen != null) {
                        if ("true".equals(isopen)) {
                            //imglist.get(i)为网络图片路径，lv_image为imageview
                            imageLoader.displayImage(NetBaseConstant.NET_PIC_PREFIX_THUMB + fndlist.get(position).getThumb().get(0).getUrl().toString(), commonHolder.lv_image, options);
                        }
                    }else{
                        imageLoader.displayImage(NetBaseConstant.NET_PIC_PREFIX_THUMB + fndlist.get(position).getThumb().get(0).getUrl().toString(), commonHolder.lv_image, options);
                    }
                    } else {
                    commonHolder.lv_title.setHeight(170);
                    commonHolder.lv_image.setVisibility(View.GONE);
                    commonHolder.lv_linear_image.setVisibility(View.GONE);
                }
                //不加这一句会提示：ImageLoader must be init with configuration before 且不显示图片
//                MyApplication.imageLoader.init(ImageLoaderConfiguration.createDefault(mactivity));
                //imglist.get(i)为网络图片路径，lv_image为imageview
//                ImageLoader.getInstance().displayImage(fndlist.get(position).getThumb().toString(), commonHolder.lv_image);
                commonHolder.lv_LL_title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Message msg = new Message();
                        msg.what=20;
                        msg.obj=position;
                        handelerone.sendMessage(msg);
                        getBundle(position, "fndinfo", News_WebView.class, "新闻信息");
                    }
                });
                break;
            case 2:


                  if (user.getUserid() != null &&user.getUserid().length() > 0){
                      String userid = user.getUserid();
                      if (fndlist.get(position).getLikes() != null && fndlist.get(position).getLikes().size() > 0) {
                          commonHolder.lv_like.setText(fndlist.get(position).getLikes().size() + "");
                          for (int i = 0;i < fndlist.get(position).getLikes().size();i++){
                              if (userid.equals(fndlist.get(position).getLikes().get(i).getUserid())){
                                  commonHolder.lv_like_image.setBackgroundResource(R.mipmap.ic_like_sel);
                                  break;
                              }else{
                                  commonHolder.lv_like_image.setBackgroundResource(R.mipmap.ic_like_gray);
                              }
                          }
                      }else{
                          commonHolder.lv_like_image.setBackgroundResource(R.mipmap.ic_like_gray);
                          commonHolder.lv_like.setText("0");
                      }
                      if (fndlist.get(position).getFavorites() != null && fndlist.get(position).getFavorites().size() > 0) {
                          commonHolder.lv_collect_num.setText(fndlist.get(position).getFavorites().size() + "");
                          for (int i = 0;i < fndlist.get(position).getFavorites().size();i++){
                              if (userid.equals(fndlist.get(position).getFavorites().get(i).getUserid())){
                                  commonHolder.lv_collect_image.setBackgroundResource(R.mipmap.ic_collect_sel);
                                  break;
                              }else{
                                  commonHolder.lv_collect_image.setBackgroundResource(R.mipmap.ic_collect_nor);
                              }
                          }
                      }else{
                          commonHolder.lv_collect_image.setBackgroundResource(R.mipmap.ic_collect_nor);
                          commonHolder.lv_collect_num.setText("0");
                      }
                  }else{
                      if (fndlist.get(position).getLikes() != null && fndlist.get(position).getLikes().size() > 0) {
                            commonHolder.lv_like.setText(fndlist.get(position).getLikes().size() + "");
                          commonHolder.lv_like_image.setBackgroundResource(R.mipmap.ic_like_gray);
                        }else{
                            commonHolder.lv_like.setText("0");
                          commonHolder.lv_like_image.setBackgroundResource(R.mipmap.ic_like_gray);
                        }
                      if (fndlist.get(position).getFavorites() != null && fndlist.get(position).getFavorites().size() > 0) {
                          commonHolder.lv_collect_num.setText(fndlist.get(position).getFavorites().size() + "");
                          commonHolder.lv_collect_image.setBackgroundResource(R.mipmap.ic_collect_nor);
                      }else{
                          commonHolder.lv_collect_num.setText("0");
                          commonHolder.lv_collect_image.setBackgroundResource(R.mipmap.ic_collect_nor);}
                  }
                        commonHolder.lv_collect_image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                for (int i = 0;i < fndlist.size();i++){
                                    if (i == position){
                                        webtitleId = fndlist.get(position).getTerm_id().toString();
                                        webId = fndlist.get(position).getObject_id().toString();
                                        web_title = fndlist.get(position).getPost_title().toString();
                                        description = fndlist.get(position).getPost_excerpt().toString();
                                        if (user.getUserid() != null && user.getUserid().length() > 0) {
                                            Message msg = new Message();
                                            msg.what=1;
                                            msg.obj=position;
                                            handelerone.sendMessage(msg);
                                        }else{
                                            Intent intent =  new Intent(mactivity, LoginActivity.class);
                                            mactivity.startActivity(intent);
                                        }
                                    }
                                }
                            }
                        });
                        commonHolder.lv_like_image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                for (int i = 0;i < fndlist.size();i++){
                                    if (i == position){
                                        webtitleId = fndlist.get(position).getTerm_id().toString();
                                        webId = fndlist.get(position).getObject_id().toString();
                                        web_title = fndlist.get(position).getPost_title().toString();
                                        description = fndlist.get(position).getPost_excerpt().toString();
                                        if (user.getUserid() != null && user.getUserid().length() > 0) {
                                            Message msg = new Message();
                                            msg.what=0;
                                            msg.obj=position;
                                            handelerone.sendMessage(msg);
                                        }else{
                                            Intent intent =  new Intent(mactivity, LoginActivity.class);
                                            mactivity.startActivity(intent);
                                        }
                                    }
                                }
                            }
                        });

                        if (0 == position) {
                            commonHolder.textview.setVisibility(View.GONE);
                        } else {
                            commonHolder.textview.setVisibility(View.VISIBLE);
                        }
                        commonHolder.lv_title.setText(fndlist.get(position).getPost_title().toString() + "");
                        commonHolder.lv_refad.setText(fndlist.get(position).getPost_hits().toString() + "");
                        commonHolder.lv_time.setText(time + "");
                        if (fndlist.get(position).getThumb() != null && fndlist.get(position).getThumb().size() > 0) {
                            if (isopen != null) {
                                if ("true".equals(isopen)) {
                                    //imglist.get(i)为网络图片路径，lv_image为imageview
                                    imageLoader.displayImage(NetBaseConstant.NET_PIC_PREFIX_THUMB + fndlist.get(position).getThumb().get(0).getUrl().toString(), commonHolder.lv_image, options);
                                }
                            }else{
                                imageLoader.displayImage(NetBaseConstant.NET_PIC_PREFIX_THUMB + fndlist.get(position).getThumb().get(0).getUrl().toString(), commonHolder.lv_image, options);
                            }
                        } else {
                            commonHolder.lv_image.setVisibility(View.GONE);
                        }

                        //不加这一句会提示：ImageLoader must be init with configuration before 且不显示图片
//                MyApplication.imageLoader.init(ImageLoaderConfiguration.createDefault(mactivity));
                        //imglist.get(i)为网络图片路径，lv_image为imageview
//                ImageLoader.getInstance().displayImage(fndlist.get(position).getThumb().get(0).getUrl().toString(), commonHolder.lv_image);
                break;
            case 3:
                commonHolder.lv_title.setText(fndlist.get(position).getPost_title().toString() + "");
                commonHolder.lv_refad.setText(fndlist.get(position).getPost_hits().toString() + "");
                commonHolder.lv_time.setText(time + "");
                //不加这一句会提示：ImageLoader must be init with configuration before 且不显示图片
//                MyApplication.imageLoader.init(ImageLoaderConfiguration.createDefault(mactivity));
                //imglist.get(i)为网络图片路径，lv_image为imageview
//                ImageLoader.getInstance().displayImage(fndlist.get(position).getThumb().toString(), commonHolder.lv_image);
                if (fndlist.get(position).getThumb() != null && fndlist.get(position).getThumb().size() > 2) {
                    commonHolder.lv_image.setVisibility(View.GONE);
                    commonHolder.lv_linear_image.setVisibility(View.VISIBLE);
                    if (isopen != null){
                        if ("true".equals(isopen)){
                            //imglist.get(i)为网络图片路径，lv_image为imageview
                            imageLoader.displayImage(NetBaseConstant.NET_PIC_PREFIX_THUMB+fndlist.get(position).getThumb().get(0).getUrl().toString(),commonHolder.lv_image_one,options);
                            imageLoader.displayImage(NetBaseConstant.NET_PIC_PREFIX_THUMB+fndlist.get(position).getThumb().get(1).getUrl().toString(),commonHolder.lv_image_two,options);
                            imageLoader.displayImage(NetBaseConstant.NET_PIC_PREFIX_THUMB+fndlist.get(position).getThumb().get(2).getUrl().toString(),commonHolder.lv_image_three,options);
                        }
                    }else{
                        //imglist.get(i)为网络图片路径，lv_image为imageview
                        imageLoader.displayImage(NetBaseConstant.NET_PIC_PREFIX_THUMB+fndlist.get(position).getThumb().get(0).getUrl().toString(),commonHolder.lv_image_one,options);
                        imageLoader.displayImage(NetBaseConstant.NET_PIC_PREFIX_THUMB+fndlist.get(position).getThumb().get(1).getUrl().toString(),commonHolder.lv_image_two,options);
                        imageLoader.displayImage(NetBaseConstant.NET_PIC_PREFIX_THUMB+fndlist.get(position).getThumb().get(2).getUrl().toString(),commonHolder.lv_image_three,options);
                    }
                } else if (fndlist.get(position).getThumb() != null && 0 < fndlist.get(position).getThumb().size() && fndlist.get(position).getThumb().size() <= 2) {
                    commonHolder.lv_image.setVisibility(View.VISIBLE);
                    commonHolder.lv_linear_image.setVisibility(View.GONE);
                    commonHolder.lv_title.setHeight(170);
                    if (isopen != null) {
                        if ("true".equals(isopen)) {
                            //imglist.get(i)为网络图片路径，lv_image为imageview
                            imageLoader.displayImage(NetBaseConstant.NET_PIC_PREFIX_THUMB + fndlist.get(position).getThumb().get(0).getUrl().toString(), commonHolder.lv_image, options);
                        }
                    }else{
                        imageLoader.displayImage(NetBaseConstant.NET_PIC_PREFIX_THUMB + fndlist.get(position).getThumb().get(0).getUrl().toString(), commonHolder.lv_image, options);
                    }
                } else {

                    commonHolder.lv_image.setVisibility(View.GONE);
                    commonHolder.lv_linear_image.setVisibility(View.GONE);
                    commonHolder.lv_title.setHeight(170);}
                break;
            case 4:
                if (user.getUserid() != null &&user.getUserid().length() > 0){
                    String userid = user.getUserid();
                    if (fndlist.get(position).getLikes() != null && fndlist.get(position).getLikes().size() > 0) {
                        commonHolder.tv_collect.setText(fndlist.get(position).getLikes().size() + "");
                        for (int i = 0;i < fndlist.get(position).getLikes().size();i++){
                            if (userid.equals(fndlist.get(position).getLikes().get(i).getUserid())){
                                commonHolder.image_3.setBackgroundResource(R.mipmap.ic_like_sel);
                                break;
                            }else{
                                commonHolder.image_3.setBackgroundResource(R.mipmap.ic_like_gray);
                            }
                        }
                    }else{
                        commonHolder.image_3.setBackgroundResource(R.mipmap.ic_like_gray);
                        commonHolder.tv_collect.setText("0");
                    }
                    if (fndlist.get(position).getFavorites() != null && fndlist.get(position).getFavorites().size() > 0) {
                        commonHolder.tv_tv_collect.setText(fndlist.get(position).getFavorites().size() + "");
                        for (int i = 0;i < fndlist.get(position).getFavorites().size();i++){
                            if (userid.equals(fndlist.get(position).getFavorites().get(i).getUserid())){
                                commonHolder.image_collect.setBackgroundResource(R.mipmap.ic_collect_sel);
                                break;
                            }else{
                                commonHolder.image_collect.setBackgroundResource(R.mipmap.ic_collect_nor);
                            }
                        }
                    }else{
                        commonHolder.image_collect.setBackgroundResource(R.mipmap.ic_collect_nor);
                        commonHolder.tv_tv_collect.setText("0");
                    }
                }else{
                    if (fndlist.get(position).getLikes() != null && fndlist.get(position).getLikes().size() > 0) {
                        commonHolder.tv_collect.setText(fndlist.get(position).getLikes().size() + "");
                        commonHolder.image_3.setBackgroundResource(R.mipmap.ic_like_gray);
                    }else{
                        commonHolder.tv_collect.setText("0");
                        commonHolder.image_3.setBackgroundResource(R.mipmap.ic_like_gray);
                    }
                    if (fndlist.get(position).getFavorites() != null && fndlist.get(position).getFavorites().size() > 0) {
                        commonHolder.tv_tv_collect.setText(fndlist.get(position).getFavorites().size() + "");
                        commonHolder.image_collect.setBackgroundResource(R.mipmap.ic_collect_nor);
                    }else{
                        commonHolder.tv_tv_collect.setText("0");
                        commonHolder.image_collect.setBackgroundResource(R.mipmap.ic_collect_nor);}
                }
                commonHolder.tv_topic.setText(fndlist.get(position).getPost_title().toString() + "");
                if (null != fndlist.get(position).getPost_hits() && fndlist.get(position).getPost_hits().length() > 0){
                    commonHolder.tv_eye.setText(fndlist.get(position).getPost_hits().toString() + "人学习");
                }else{
                    commonHolder.tv_eye.setText("0人学习");
                }
                commonHolder.btn_linear_like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0;i < fndlist.size();i++){
                            if (i == position){
                                webtitleId = fndlist.get(position).getTerm_id().toString();
                                webId = fndlist.get(position).getObject_id().toString();
                                web_title = fndlist.get(position).getPost_title().toString();
                                description = fndlist.get(position).getPost_excerpt().toString();
                                if (user.getUserid() != null && user.getUserid().length() > 0) {
                                    Message msg = new Message();
                                    msg.what=5;
                                    msg.obj=position;
                                    handelerone.sendMessage(msg);
                                }else{
                                    Intent intent =  new Intent(mactivity, LoginActivity.class);
                                    mactivity.startActivity(intent);
                                }
                            }
                        }
                    }
                });
                commonHolder.btn_linear_collect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0;i < fndlist.size();i++){
                            if (i == position){
                                webtitleId = fndlist.get(position).getTerm_id().toString();
                                webId = fndlist.get(position).getObject_id().toString();
                                web_title = fndlist.get(position).getPost_title().toString();
                                description = fndlist.get(position).getPost_excerpt().toString();
                                if (user.getUserid() != null && user.getUserid().length() > 0) {
                                    Message msg = new Message();
                                    msg.what=6;
                                    msg.obj=position;
                                    handelerone.sendMessage(msg);
                                }else{
                                    Intent intent =  new Intent(mactivity, LoginActivity.class);
                                    mactivity.startActivity(intent);
                                }
                            }
                        }

                    }
                });
                //不加这一句会提示：ImageLoader must be init with configuration before 且不显示图片
//                MyApplication.imageLoader.init(ImageLoaderConfiguration.createDefault(mactivity));
                //imglist.get(i)为网络图片路径，lv_image为imageview
//                ImageLoader.getInstance().displayImage(list_study.get(position).getStr_img().toString(), commonHolder.lv_image);

                break;
            case 5:
                if (user.getUserid() != null &&user.getUserid().length() > 0){
                    String userid = user.getUserid();
                    if (fndlist.get(position).getLikes() != null && fndlist.get(position).getLikes().size() > 0) {
                        commonHolder.lv_like.setText(fndlist.get(position).getLikes().size() + "");
                        for (int i = 0;i < fndlist.get(position).getLikes().size();i++){
                            if (userid.equals(fndlist.get(position).getLikes().get(i).getUserid())){
                                commonHolder.lv_like_image.setBackgroundResource(R.mipmap.ic_like_sel);
                                break;
                            }else{
                                commonHolder.lv_like_image.setBackgroundResource(R.mipmap.ic_like_gray);
                            }
                        }
                    }else{
                        commonHolder.lv_like_image.setBackgroundResource(R.mipmap.ic_like_gray);
                        commonHolder.lv_like.setText("0");
                    }
                    if (fndlist.get(position).getFavorites() != null && fndlist.get(position).getFavorites().size() > 0) {
                        commonHolder.lv_collect_num.setText(fndlist.get(position).getFavorites().size() + "");
                        for (int i = 0;i < fndlist.get(position).getFavorites().size();i++){
                            if (userid.equals(fndlist.get(position).getFavorites().get(i).getUserid())){
                                commonHolder.lv_collect_image.setBackgroundResource(R.mipmap.ic_collect_sel);
                                break;
                            }else{
                                commonHolder.lv_collect_image.setBackgroundResource(R.mipmap.ic_collect_nor);
                            }
                        }
                    }else{
                        commonHolder.lv_collect_image.setBackgroundResource(R.mipmap.ic_collect_nor);
                        commonHolder.lv_collect_num.setText("0");
                    }
                }else{
                    if (fndlist.get(position).getLikes() != null && fndlist.get(position).getLikes().size() > 0) {
                        commonHolder.lv_like.setText(fndlist.get(position).getLikes().size() + "");
                        commonHolder.lv_like_image.setBackgroundResource(R.mipmap.ic_like_gray);
                    }else{
                        commonHolder.lv_like.setText("0");
                        commonHolder.lv_like_image.setBackgroundResource(R.mipmap.ic_like_gray);
                    }
                    if (fndlist.get(position).getFavorites() != null && fndlist.get(position).getFavorites().size() > 0) {
                        commonHolder.lv_collect_num.setText(fndlist.get(position).getFavorites().size() + "");
                        commonHolder.lv_collect_image.setBackgroundResource(R.mipmap.ic_collect_nor);
                    }else{
                        commonHolder.lv_collect_num.setText("0");
                        commonHolder.lv_collect_image.setBackgroundResource(R.mipmap.ic_collect_nor);}
                }
                commonHolder.lv_collect_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0;i < fndlist.size();i++){
                            if (i == position){
                                webtitleId = fndlist.get(position).getTerm_id().toString();
                                webId = fndlist.get(position).getObject_id().toString();
                                web_title = fndlist.get(position).getPost_title().toString();
                                description = fndlist.get(position).getPost_excerpt().toString();
                                if (user.getUserid() != null && user.getUserid().length() > 0) {
                                    Message msg = new Message();
                                    msg.what=1;
                                    msg.obj=position;
                                    handelerone.sendMessage(msg);
                                }else{
                                    Intent intent =  new Intent(mactivity, LoginActivity.class);
                                    mactivity.startActivity(intent);
                                }
                            }
                        }
                    }
                });
                commonHolder.lv_like_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0;i < fndlist.size();i++){
                            if (i == position){
                                webtitleId = fndlist.get(position).getTerm_id().toString();
                                webId = fndlist.get(position).getObject_id().toString();
                                web_title = fndlist.get(position).getPost_title().toString();
                                description = fndlist.get(position).getPost_excerpt().toString();
                                if (user.getUserid() != null && user.getUserid().length() > 0) {
                                    Message msg = new Message();
                                    msg.what=0;
                                    msg.obj=position;
                                    handelerone.sendMessage(msg);
                                }else{
                                    Intent intent =  new Intent(mactivity, LoginActivity.class);
                                    mactivity.startActivity(intent);
                                }
                            }
                        }
                    }
                });
                if (0 == position) {
                    commonHolder.textview.setVisibility(View.GONE);
                } else {
                    commonHolder.textview.setVisibility(View.VISIBLE);
                }
                commonHolder.lv_title.setText(fndlist.get(position).getPost_title().toString() + "");
                commonHolder.lv_refad.setText(fndlist.get(position).getPost_hits().toString() + "");
                if (null != fndlist.get(position).getPost_hits() && fndlist.get(position).getPost_hits().length() > 0){
                    commonHolder.lv_time.setText(fndlist.get(position).getPost_hits().toString() + "人学习");
                }else{
                    commonHolder.lv_time.setText("0人学习");
                }
                if (fndlist.get(position).getThumb() != null && fndlist.get(position).getThumb().size() > 0) {
                    if (isopen != null) {
                        if ("true".equals(isopen)) {
                            //imglist.get(i)为网络图片路径，lv_image为imageview
                            imageLoader.displayImage(NetBaseConstant.NET_PIC_PREFIX_THUMB + fndlist.get(position).getThumb().get(0).getUrl().toString(), commonHolder.lv_image, options);
                        }
                    }else{
                        imageLoader.displayImage(NetBaseConstant.NET_PIC_PREFIX_THUMB + fndlist.get(position).getThumb().get(0).getUrl().toString(), commonHolder.lv_image, options);
                    }
                } else {
                    commonHolder.lv_image.setVisibility(View.GONE);
                }
                break;
            case 6:
                if (user.getUserid() != null &&user.getUserid().length() > 0){
                    String userid = user.getUserid();
                    if (fndlist.get(position).getLikes() != null && fndlist.get(position).getLikes().size() > 0) {
                        commonHolder.tv_collect.setText(fndlist.get(position).getLikes().size() + "");
                        for (int i = 0;i < fndlist.get(position).getLikes().size();i++){
                            if (userid.equals(fndlist.get(position).getLikes().get(i).getUserid())){
                                commonHolder.image_3 .setBackgroundResource(R.mipmap.ic_like_sel);
                                break;
                            }else{
                                commonHolder.image_3 .setBackgroundResource(R.mipmap.ic_like_gray);
                            }
                        }
                    }else{
                        commonHolder.image_3.setBackgroundResource(R.mipmap.ic_like_gray);
                        commonHolder.tv_collect.setText("0");
                    }
                    if (fndlist.get(position).getFavorites() != null && fndlist.get(position).getFavorites().size() > 0) {
                        commonHolder.tv_tv_collect.setText(fndlist.get(position).getFavorites().size() + "");
                        for (int i = 0;i < fndlist.get(position).getFavorites().size();i++){
                            if (userid.equals(fndlist.get(position).getFavorites().get(i).getUserid())){
                                commonHolder.image_collect.setBackgroundResource(R.mipmap.ic_collect_sel);
                                break;
                            }else{
                                commonHolder.image_collect.setBackgroundResource(R.mipmap.ic_collect_nor);
                            }
                        }
                    }else{
                        commonHolder.image_collect.setBackgroundResource(R.mipmap.ic_collect_nor);
                        commonHolder.tv_tv_collect.setText("0");
                    }
                }else{
                    if (fndlist.get(position).getLikes() != null && fndlist.get(position).getLikes().size() > 0) {
                        commonHolder.tv_collect.setText(fndlist.get(position).getLikes().size() + "");
                        commonHolder.image_3.setBackgroundResource(R.mipmap.ic_like_gray);
                    }else{
                        commonHolder.tv_collect.setText("0");
                        commonHolder.image_3.setBackgroundResource(R.mipmap.ic_like_gray);
                    }
                    if (fndlist.get(position).getFavorites() != null && fndlist.get(position).getFavorites().size() > 0) {
                        commonHolder.tv_tv_collect.setText(fndlist.get(position).getFavorites().size() + "");
                        commonHolder.image_collect.setBackgroundResource(R.mipmap.ic_collect_nor);
                    }else{
                        commonHolder.tv_tv_collect.setText("0");
                        commonHolder.image_collect.setBackgroundResource(R.mipmap.ic_collect_nor);}
                }
                commonHolder.lv_title.setText(fndlist.get(position).getPost_title().toString() + "");
                commonHolder.lv_refad.setText(fndlist.get(position).getPost_hits().toString() + "");
                Log.e("termname", "========================>" + fndlist.get(position).getTerm_name().toString());
                commonHolder.tv_term_name.setText(fndlist.get(position).getTerm_name().toString() + "");
                commonHolder.tv_term_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        termidnew = fndlist.get(position).getTerm_id().toString();
                        termnamenew = fndlist.get(position).getTerm_name().toString();
                        Intent intent = new Intent(mactivity, SecondPage.class);
                        intent.putExtra("termid", termidnew);
                        intent.putExtra("termname", termnamenew);
                        mactivity.startActivity(intent);
                    }
                });
                if (fndlist.get(position).getThumb() != null && fndlist.get(position).getThumb().size() > 2) {
                    commonHolder.lv_image.setVisibility(View.GONE);
                    commonHolder.lv_linear_image.setVisibility(View.VISIBLE);
                    //imglist.get(i)为网络图片路径，lv_image为imageview
                    if (isopen != null){
                        if ("true".equals(isopen)){
                            //imglist.get(i)为网络图片路径，lv_image为imageview
                            imageLoader.displayImage(NetBaseConstant.NET_PIC_PREFIX_THUMB+fndlist.get(position).getThumb().get(0).getUrl().toString(),commonHolder.lv_image_one,options);
                            imageLoader.displayImage(NetBaseConstant.NET_PIC_PREFIX_THUMB+fndlist.get(position).getThumb().get(1).getUrl().toString(),commonHolder.lv_image_two,options);
                            imageLoader.displayImage(NetBaseConstant.NET_PIC_PREFIX_THUMB+fndlist.get(position).getThumb().get(2).getUrl().toString(),commonHolder.lv_image_three,options);
                        }
                    }else{
                        //imglist.get(i)为网络图片路径，lv_image为imageview
                        imageLoader.displayImage(NetBaseConstant.NET_PIC_PREFIX_THUMB+fndlist.get(position).getThumb().get(0).getUrl().toString(),commonHolder.lv_image_one,options);
                        imageLoader.displayImage(NetBaseConstant.NET_PIC_PREFIX_THUMB+fndlist.get(position).getThumb().get(1).getUrl().toString(),commonHolder.lv_image_two,options);
                        imageLoader.displayImage(NetBaseConstant.NET_PIC_PREFIX_THUMB+fndlist.get(position).getThumb().get(2).getUrl().toString(),commonHolder.lv_image_three,options);
                    }

                } else if (fndlist.get(position).getThumb() != null && 0 < fndlist.get(position).getThumb().size() && fndlist.get(position).getThumb().size() <= 2) {
                    commonHolder.lv_image.setVisibility(View.VISIBLE);
                    commonHolder.lv_linear_image.setVisibility(View.GONE);

                    if (isopen != null) {
                        if ("true".equals(isopen)) {
                            //imglist.get(i)为网络图片路径，lv_image为imageview
                            imageLoader.displayImage(NetBaseConstant.NET_PIC_PREFIX_THUMB + fndlist.get(position).getThumb().get(0).getUrl().toString(), commonHolder.lv_image, options);
                        }
                    }else{
                        imageLoader.displayImage(NetBaseConstant.NET_PIC_PREFIX_THUMB + fndlist.get(position).getThumb().get(0).getUrl().toString(), commonHolder.lv_image, options);
                    }
                } else {
                    commonHolder.lv_image.setVisibility(View.GONE);
                    commonHolder.lv_linear_image.setVisibility(View.GONE);
                }
                commonHolder.btn_linear_like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0;i < fndlist.size();i++){
                            if (i == position){
                                webtitleId = fndlist.get(position).getTerm_id().toString();
                                webId = fndlist.get(position).getObject_id().toString();
                                web_title = fndlist.get(position).getPost_title().toString();
                                description = fndlist.get(position).getPost_excerpt().toString();
                                if (user.getUserid() != null && user.getUserid().length() > 0) {
                                    Message msg = new Message();
                                    msg.what=0;
                                    msg.obj=position;
                                    handelerone.sendMessage(msg);
                                }else{
                                    Intent intent =  new Intent(mactivity, LoginActivity.class);
                                    mactivity.startActivity(intent);
                                }
                            }
                        }
                    }
                });
                commonHolder.btn_linear_collect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0;i < fndlist.size();i++){
                            if (i == position){
                                webtitleId = fndlist.get(position).getTerm_id().toString();
                                webId = fndlist.get(position).getObject_id().toString();
                                web_title = fndlist.get(position).getPost_title().toString();
                                description = fndlist.get(position).getPost_excerpt().toString();
                                if (user.getUserid() != null && user.getUserid().length() > 0) {
                                    Message msg = new Message();
                                    msg.what=1;
                                    msg.obj=position;
                                    handelerone.sendMessage(msg);
                                }else{
                                    Intent intent =  new Intent(mactivity, LoginActivity.class);
                                    mactivity.startActivity(intent);
                                }
                            }
                        }
                    }
                });
                break;
        }
        return convertView;
    }

    @SuppressWarnings("rawtypes")
    public void getBundle(final int position, String key, Class clazz, String str) {
        FirstPageNews.DataBean fndData = fndlist.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable(key, fndData);
        bundle.putString("titletype", titletype);
        bundle.putString("position", String.valueOf(position));
        Intent intent = new Intent(mactivity, clazz);
        intent.putExtras(bundle);
        mactivity.startActivityForResult(intent, REQUEST_CODE);

        mactivity.startActivity(intent);
    }

    public static class CommonViewHolder {
        private TextView lv_title, lv_refad, lv_time, lv_like, tv_term_name, textview, lv_collect_num,lv_titile_other,tv_term_name_other,lv_read_other,lv_time_other;
        private ImageView lv_image, lv_image_one, lv_image_two, lv_image_three, lv_collect_image, lv_like_image;
        private LinearLayout lv_LL_title, lv_linear_image,linearlayout_first,linearlayout_first_other;
        //五万道题
        private TextView tv_topic, tv_eye, tv_collect,tv_tv_collect;
        private ImageView image_3,image_collect;
        private LinearLayout btn_linear_like,btn_linear_collect;

    }
}
