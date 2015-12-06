package com.ysdemo.keyboard.view;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.ysdemo.keyboard.R;

/**
 * @Author: ys尘笑
 * @date 2015-12-6 下午5:21:36
 * @Description: 键盘功能控制工具类
 */
public class KeyboardUtil {
	private Context ctx;
	private MyKeyboardView keyboardView;
	private Keyboard k1;// 键盘
//	private Keyboard k2;
//	private Keyboard k3;
	public boolean isnun = false;// 是否数据键盘
	public boolean isupper = false;// 是否大写
	public final static int TYPE_NUMBER = 1; // 数量
	public final static int TYPE_PRICE = 2; // 价格
	private int type = -1;
	private KeyboardListener keyboardListener;

	private EditText ed;

	public interface KeyboardListener {
		void onOK();
	}

	/**
	 * @param ctx
	 * @param parent	包含MyKeyboardView的ViewGroup
	 * @param edit
	 */
	public KeyboardUtil(Context ctx, View parent, EditText edit) {
		this.ctx = ctx;
		this.ed = edit;
		//此处可替换键盘xml
		k1 = new Keyboard(ctx, R.xml.number);
		keyboardView = (MyKeyboardView) parent.findViewById(R.id.keyboard_view);
		keyboardView.setContext(ctx);
		keyboardView.setKeyboard(k1);
		keyboardView.setEnabled(true);
		keyboardView.setPreviewEnabled(true);
		keyboardView.setOnKeyboardActionListener(listener);
	}

	/**
	 * @param ctx	必须要用Activity实例作为上下文传入
	 * @param edit
	 */
	public KeyboardUtil(Context ctx, EditText edit) {
		this.ctx = ctx;
		this.ed = edit;
		//此处可替换键盘xml
		k1 = new Keyboard(ctx, R.xml.number);
		try {
			keyboardView = (MyKeyboardView) ((Activity)ctx).findViewById(R.id.keyboard_view);
			keyboardView.setContext(ctx);
			keyboardView.setKeyboard(k1);
			keyboardView.setEnabled(true);
			keyboardView.setPreviewEnabled(true);
			keyboardView.setOnKeyboardActionListener(listener);
		} catch (Exception e) {
			Log.e("keyboardView", "keyboardView init failed!");
		}
	}

	public void setKeyboardListener(KeyboardListener keyboardListener) {
		this.keyboardListener = keyboardListener;
	}

	public void setType(int typein) {
		type = typein;
	}

	private OnKeyboardActionListener listener = new OnKeyboardActionListener() {
		@Override
		public void swipeUp() {
		}

		@Override
		public void swipeRight() {
		}

		@Override
		public void swipeLeft() {
		}

		@Override
		public void swipeDown() {
		}

		@Override
		public void onText(CharSequence text) {
		}

		@Override
		public void onRelease(int primaryCode) {
		}

		@Override
		public void onPress(int primaryCode) {
		}

		@Override
		public void onKey(int primaryCode, int[] keyCodes) {
			Editable editable = ed.getText();
			int start = ed.getSelectionStart();
			if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 完成
				// hideKeyboard();
				keyboardListener.onOK();
			} else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
				if (editable != null && editable.length() > 0) {
					if (start > 0) {
						editable.delete(start - 1, start);
					}
				}
			} else if (primaryCode == Keyboard.KEYCODE_SHIFT) {// 大小写切换
				changeKey();
				keyboardView.setKeyboard(k1);

			} else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {// 键盘切换
				if (isnun) {
					isnun = false;
					keyboardView.setKeyboard(k1);
				} else {
					isnun = true;
//					keyboardView.setKeyboard(k2);
				}
			} else if (primaryCode == 57419) { // go left
				if (start > 0) {
					ed.setSelection(start - 1);
				}
			} else if (primaryCode == 57421) { // go right
				if (start < ed.length()) {
					ed.setSelection(start + 1);
				}
			} else if (primaryCode == 46) {	   // 小数点
				String text = ed.getText().toString();
				if (type == TYPE_PRICE) {
					if (!ed.getText().toString().contains(".") && text.length() <= 7) {
						editable.insert(start,
								Character.toString((char) primaryCode));
					}
				}
			} else {
				String text = ed.getText().toString();
				switch (type) {
				case TYPE_NUMBER:
					if (text.length() < 7) {
						editable.insert(start,
								Character.toString((char) primaryCode));
					}
					break;

				case TYPE_PRICE:
					if ((!text.contains(".") || text.length() - 1
							- text.indexOf(".") <= 1)
							&& text.length() < (text.contains(".")?10:7)) {
						//小数点后最长2位，接受7位整数
						editable.insert(start,
								Character.toString((char) primaryCode));
					}
					break;
				default:
					editable.insert(start,
							Character.toString((char) primaryCode));
					break;
				}

			}
		}
	};

	/**
	 * 键盘大小写切换
	 */
	private void changeKey() {
		List<Key> keylist = k1.getKeys();
		if (isupper) {// 大写切换小写
			isupper = false;
			for (Key key : keylist) {
				if (key.label != null && isword(key.label.toString())) {
					key.label = key.label.toString().toLowerCase();
					key.codes[0] = key.codes[0] + 32;
				}
			}
		} else {// 小写切换大写
			isupper = true;
			for (Key key : keylist) {
				if (key.label != null && isword(key.label.toString())) {
					key.label = key.label.toString().toUpperCase();
					key.codes[0] = key.codes[0] - 32;
				}
			}
		}
	}

	public void showKeyboard() {
		int visibility = keyboardView.getVisibility();
		if (visibility == View.GONE || visibility == View.INVISIBLE) {
			keyboardView.setVisibility(View.VISIBLE);
		}
	}

	public void hideKeyboard() {
		int visibility = keyboardView.getVisibility();
		if (visibility == View.VISIBLE) {
			keyboardView.setVisibility(View.INVISIBLE);
		}
	}

	private boolean isword(String str) {
		String wordstr = "abcdefghijklmnopqrstuvwxyz";
		if (wordstr.indexOf(str.toLowerCase()) > -1) {
			return true;
		}
		return false;
	}
}
