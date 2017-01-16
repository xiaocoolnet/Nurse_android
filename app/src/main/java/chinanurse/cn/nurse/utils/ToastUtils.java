package chinanurse.cn.nurse.utils;

import java.util.regex.Pattern;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import static android.widget.Toast.*;

public class ToastUtils {
	private  static  Toast mToast;
	public static void ToastShort(Context mContext, String msg) {
		if (mToast==null){
			mToast = Toast.makeText(mContext, msg, LENGTH_SHORT);
			mToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		}else{
			mToast.setText(msg);
			mToast.setDuration(LENGTH_SHORT);
		}
		mToast.show();
	}

	public static void ToastLong(Context mContext, String msg) {
		if (mToast==null){
			mToast = Toast.makeText(mContext, msg, LENGTH_LONG);
			mToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		}else{
			mToast.setText(msg);
			mToast.setDuration(LENGTH_LONG);
		}
		mToast.show();
	}

	public static String getOnlyNumber(String s) {
		String result = "";
		if ((s == null) || (s.equals(""))) {
			return result;
		}
		try {
			result = Pattern.compile("[^0-9]").matcher(s).replaceAll("").trim();
		} catch (Exception e) {
			result = "";
			e.printStackTrace();
		}
		return result;
	}

	public static String getAreaCode(String phoneNumber) {
		String result = "";
		if ((phoneNumber == null) || (phoneNumber.equals(""))) {
			return result;
		}
		try {
			String purifyPhoneNumber = getOnlyNumber(phoneNumber);
			if ((purifyPhoneNumber == null) || (purifyPhoneNumber.equals(""))) {
				return result;
			}
			if ((purifyPhoneNumber.indexOf("86") == 0) && purifyPhoneNumber.length() > 10) {
				result = "86";
			} else if ((phoneNumber.indexOf("+") == 0) && (phoneNumber.contains(" "))) {
				result = phoneNumber.substring(0, phoneNumber.indexOf(" "));
				result = getOnlyNumber(result);
			}
		} catch (Exception e) {
			result = "";
			e.printStackTrace();
		}
		return result;
	}

	public static String purifyPhoneNumber(String phoneNumber) {
		String result = "";
		if (phoneNumber == null) {
			return result;
		}
		try {
			String purifyPhoneNumber = getOnlyNumber(phoneNumber);
			if ((purifyPhoneNumber == null) || (purifyPhoneNumber.equals(""))) {
				return result;
			}
			if ((purifyPhoneNumber.indexOf("86") == 0) && purifyPhoneNumber.length() > 10) {
				phoneNumber = purifyPhoneNumber.substring(2);
			} else if ((phoneNumber.indexOf("+") == 0) && (phoneNumber.contains(" "))) {
				result = phoneNumber.substring(phoneNumber.indexOf(" ") + 1, phoneNumber.length());
			}
			result = getOnlyNumber(phoneNumber);
		} catch (Exception e) {
			result = "";
			e.printStackTrace();
		}
		return result;
	}
}
