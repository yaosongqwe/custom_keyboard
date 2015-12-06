package com.ysdemo.keyboard.view;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.NinePatchDrawable;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

import com.ysdemo.keyboard.R;

public class MyKeyboardView extends KeyboardView {
	
	private Context context;

	public MyKeyboardView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setContext(Context context) {
		this.context = context;
	}

	@Override
    public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
        List<Key> keys = getKeyboard().getKeys();
        for(Key key: keys) {
            if(key.label.equals("OK"))
            	resetOKBtn(key, canvas);
        }
    }
	
	/**
	 * 绘制OK键的点9图
	 * @author Song
	 * @param key
	 * @param canvas
	 */
	private void resetOKBtn(Key key,Canvas canvas) {
		//将OK键重新绘制
        NinePatchDrawable npd = (NinePatchDrawable) context.getResources().getDrawable(R.drawable.ok_undefined);
	    npd.setBounds(key.x, key.y + 1, key.x + key.width, key.y + key.height + 1);
	    npd.draw(canvas);
	}
}