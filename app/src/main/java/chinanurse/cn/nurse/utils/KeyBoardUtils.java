package chinanurse.cn.nurse.utils;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class KeyBoardUtils {
	public static void showKeyBoardByTime(final EditText et, int time) {
		new Timer().schedule(new TimerTask()

		{
			public void run()

			{
				InputMethodManager inputManager =

				(InputMethodManager) et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

				inputManager.showSoftInput(et, 0);

			}

		}, time);
	}
	public static void hidekeyBoardByTime(final EditText et, int time){
		new Timer().schedule(new TimerTask()

		{
			public void run()

			{
				InputMethodManager inputManager =

				(InputMethodManager) et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

				inputManager.hideSoftInputFromWindow(et.getWindowToken(), 0);

			}

		}, time);
	}
}
