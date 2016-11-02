package chinanurse.cn.nurse.adapter.community;


import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.bean.Nuser_community;

/**
 * Created by Administrator on 2016/7/13.
 */
public class Community_main_adapetr extends PagerAdapter {
    private static final int GETCOMMUNITYNOMAL = 2;
    private List<String> titlelist;
    private List<View> viewItems;
    private Activity macrivity;
    private static final int GETCOMMUNITYLIST = 1;
    // 每个item的页面view
    private View convertView;
    private ViewHodler holder = new ViewHodler();
    private String type;
    //下listview
    private String ishot = "0";
    //上listview
    private String isnewhot = "1";
    private List<Nuser_community.DataEntity> contentlist = new ArrayList<Nuser_community.DataEntity>();
    private List<Nuser_community.DataEntity> contentlist_2 = new ArrayList<Nuser_community.DataEntity>();

    private Nuser_community contentbean, contentbean_2;
    private Community_listview_Adapter clalistviewadapter, clalistviewadapter_2;

    private int mposition = 0;
    private ViewGroup mcontainer;


    private Handler hanlder = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GETCOMMUNITYLIST:
                    if (msg.obj != null) {
                        contentlist.clear();
                        String result = (String) msg.obj;
                        Gson gson = new Gson();
                        contentbean = gson.fromJson(result, Nuser_community.class);
                        if ("success".equals(contentbean.getStatus())) {
                            contentlist.addAll(contentbean.getData());
                            clalistviewadapter = new Community_listview_Adapter(macrivity, contentlist, 0);
                            holder.nurse_firstfrag_lv.setAdapter(clalistviewadapter);
//                            clalistviewadapter.addList(contentlist);
                            clalistviewadapter.notifyDataSetChanged();
                            //解决listview与scroview冲突
                            setListViewHeightBasedOnChildren(holder.nurse_firstfrag_lv);
                        }
                    }
                    break;
                case GETCOMMUNITYNOMAL:
                    if (msg.obj != null) {
                        contentlist_2.clear();
                        String result = (String) msg.obj;
                        Gson gson_2 = new Gson();
                        contentbean_2 = gson_2.fromJson(result, Nuser_community.class);
                        if ("success".equals(contentbean_2.getStatus())) {
                            contentlist_2.addAll(contentbean_2.getData());

                            clalistviewadapter_2 = new Community_listview_Adapter(macrivity, contentlist_2, 1);
                            holder.nurse_first_lv.setAdapter(clalistviewadapter_2);
//                            clalistviewadapter_2.addList(contentlist_2);
                            clalistviewadapter_2.notifyDataSetChanged();
                            //解决listview与scroview冲突
                            setListViewHeightBasedOnChildren(holder.nurse_first_lv);
                        }
                    }
                    break;
            }
        }
    };

    public Community_main_adapetr(Activity macrivity, List<String> titlelist, List<View> viewItems) {
        this.macrivity = macrivity;
        this.titlelist = titlelist;
        this.viewItems = viewItems;
    }

    public CharSequence getPageTitle(int position) {
        if (titlelist == null) {
            return titlelist.get(0);
        }
        return titlelist.get(position);

    }

    @Override
    public int getCount() {

        Log.e("viewItems", viewItems.size() + "_____viewItems.size();");
        if (viewItems == null)
            return 0;
        else {
            return viewItems.size();
        }


    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewItems.get(position));

    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        mposition = position;
        mcontainer = container;


        type = String.valueOf(position + 1);
        convertView = viewItems.get(position);
        //页面中的内容
        //上面listview
        holder.nurse_first_lv = (ListView) convertView.findViewById(R.id.nurse_first_lv);
        //下面listview
        holder.nurse_firstfrag_lv = (ListView) convertView.findViewById(R.id.nurse_firstfrag_lv);
        if (HttpConnect.isConnnected(macrivity)) {
            new StudyRequest(macrivity, hanlder).GETCOMMUNITYLIST(type, isnewhot, GETCOMMUNITYNOMAL);
            //获取论坛帖子数据
            Log.i("typelist", "----------->" + type);
            new StudyRequest(macrivity, hanlder).GETCOMMUNITYLIST(type, ishot, GETCOMMUNITYLIST);

        } else {
            Toast.makeText(macrivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
        }

//        clalistviewadapter = new Community_listview_Adapter(macrivity, contentlist, 0);
//        holder.nurse_firstfrag_lv.setAdapter(clalistviewadapter);
//        clalistviewadapter_2 = new Community_listview_Adapter(macrivity, contentlist_2, 1);
//        holder.nurse_first_lv.setAdapter(clalistviewadapter_2);


        container.addView(viewItems.get(position));
        return viewItems.get(position);
    }


    class ViewHodler {
        private ListView nurse_first_lv, nurse_firstfrag_lv;
    }

    /*
       解决scrollview下listview显示不全
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }




}
