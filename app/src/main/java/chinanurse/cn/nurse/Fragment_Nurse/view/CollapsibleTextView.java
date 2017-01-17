package chinanurse.cn.nurse.Fragment_Nurse.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import chinanurse.cn.nurse.utils.LogUtils;
import chinanurse.cn.nurse.R;

public class CollapsibleTextView extends LinearLayout implements OnClickListener {

	/** 最大显示的行数 */
	private static final int DEFAULT_MAX_LINE_COUNT = 2;
	/** 实际展示的行数 */
	private static final int DEFAULT_SHOW_LINE_COUNT = 2;

	private static final int COLLAPSIBLE_STATE_NONE = 0;
	/** View处于展开状态 **/
	private static final int COLLAPSIBLE_STATE_SHRINKUP = 1;
	/** view收缩时状态 **/
	private static final int COLLAPSIBLE_STATE_SPREAD = 2;
	/** 显示内容的View */
	private TextView tv_context;
	/** 展开/收起按钮 */
	private TextView bt_spread;
	private String shrinkup;
	private String spread;
	private boolean flag;
	private int nowType;
	private CollapsibleTextView coTextView;
	private Context mContext;
	/** 判断是不是点击了查看更多、收起 */
	private boolean isClicke = false;

	public CollapsibleTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		shrinkup = context.getString(R.string.desc_shrinkup);
		spread = context.getString(R.string.desc_spread);
		View view = inflate(context, R.layout.collapsible_textview, this);
		view.setPadding(0, -1, 0, 0);
		tv_context = (TextView) view.findViewById(R.id.tv_context);
		bt_spread = (TextView) view.findViewById(R.id.bt_spread);
		bt_spread.setOnClickListener(this);
	}

	public CollapsibleTextView(Context context) {
		this(context, null);
	}

	/**
	 * 赋值
	 */
	public final void setDesc(CharSequence charSequence, CollapsibleTextView tv, BufferType bufferType) {
		this.coTextView = tv;
		// 对内容中的网址进行处理;
		tv_context.setAutoLinkMask(Linkify.ALL);
		tv_context.setMovementMethod(LinkMovementMethod.getInstance());
		tv_context.setText(charSequence, bufferType);
		// 初始类型
		coTextView.setNowType(COLLAPSIBLE_STATE_SPREAD);
//		tv_context.setOnLongClickListener(new OnLongClickListener() {
//
//			@SuppressWarnings("deprecation")
//			@Override
//			public boolean onLongClick(View arg0) {
//				// TODO Auto-generated method stub
//				ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
//				cmb.setText(tv_context.getText());
//				ToastUtils.ToastLong(mContext, "已复制");
//				return false;
//			}
//		});
		flag = false;
		requestLayout();
	}

	@Override
	public void onClick(View v) {
		flag = false;
		isClicke = true;
		requestLayout();
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (!flag) {
			flag = true;
			tv_context.post(new Runnable() {
				@Override
				public void run() {
					int lineCount = tv_context.getLineCount();
					LogUtils.e("lineCount", "lineCount--->" + lineCount);
					if (lineCount <= DEFAULT_MAX_LINE_COUNT) {
						bt_spread.setVisibility(View.GONE);
						tv_context.setMaxLines(DEFAULT_MAX_LINE_COUNT + 1);
						coTextView.setNowType(COLLAPSIBLE_STATE_NONE);
					} else {
						// 第一次进入操作（没有点击并且是第一次进入）；
						if (!isClicke) {
							tv_context.setMaxLines(DEFAULT_SHOW_LINE_COUNT);
							bt_spread.setVisibility(View.VISIBLE);
							bt_spread.setText(spread);
							coTextView.setNowType(COLLAPSIBLE_STATE_SPREAD);
							// 点击了查看更多、收起转换状态；
						} else if (isClicke) {
							isClicke = false;
							if (coTextView.getNowType() == COLLAPSIBLE_STATE_SPREAD) {
								tv_context.setMaxLines(DEFAULT_SHOW_LINE_COUNT);
								bt_spread.setVisibility(View.VISIBLE);
								bt_spread.setText(spread);
								coTextView.setNowType(COLLAPSIBLE_STATE_SHRINKUP);
							} else if (coTextView.getNowType() == COLLAPSIBLE_STATE_SHRINKUP) {
								tv_context.setMaxLines(Integer.MAX_VALUE);
								bt_spread.setVisibility(View.VISIBLE);
								bt_spread.setText(shrinkup);
								coTextView.setNowType(COLLAPSIBLE_STATE_SPREAD);
							}
							// 滑动listView 从新载入到可见界面 不做操作，保持原有状态；(为了后面看得人能够更理解写上)
						}
					}
				}
			});
		}
	}

	public int getNowType() {
		return nowType;
	}

	public void setNowType(int nowType) {
		this.nowType = nowType;
	}

}
