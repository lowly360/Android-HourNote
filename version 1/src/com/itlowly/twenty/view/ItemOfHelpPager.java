package com.itlowly.twenty.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itlowly.twenty.R;

public class ItemOfHelpPager extends LinearLayout {

	private Button btn_item_help;
	private TextView tv_item_help_1;
	private TextView tv_item_help_2;
	private  boolean flag = true;

	public ItemOfHelpPager(Context context) {
		super(context);

	}

	public ItemOfHelpPager(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.setOrientation(VERTICAL);

		TypedArray array = context.obtainStyledAttributes(attrs,
				R.styleable.ItemOfHelpPager);

		LayoutInflater.from(context).inflate(R.layout.view_help_item, this,
				true);

		btn_item_help = (Button) findViewById(R.id.btn_item_help);

		tv_item_help_1 = (TextView) findViewById(R.id.tv_item_help_1);
		tv_item_help_2 = (TextView) findViewById(R.id.tv_item_help_2);

		btn_item_help.setText(array
				.getString(R.styleable.ItemOfHelpPager_btnText));
		tv_item_help_1.setText(array
				.getString(R.styleable.ItemOfHelpPager_helpText1));
		tv_item_help_2.setText(array
				.getString(R.styleable.ItemOfHelpPager_helpText2));

		btn_item_help.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				showText(flag);
				
			}
		});

	}

	/**
	 * 显示按钮下的提示
	 * @param flag2
	 */
	public void showText(boolean flag2) {
		flag = !flag2;
		
		Drawable img_on, img_off;
		Resources res = getResources();
		img_off = res.getDrawable(R.drawable.arrow);
		img_on = res.getDrawable(R.drawable.arrow_down);
		//调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
		
		if (flag2) {
			img_on.setBounds(0, 0, img_on.getMinimumWidth(), img_on.getMinimumHeight());
			btn_item_help.setCompoundDrawables(null, null, img_on, null); //设置左图标
			
			tv_item_help_1.setVisibility(View.VISIBLE);
			if (tv_item_help_2.getText().equals("")) {

			} else {//如果没有字符，则不显示任何东西
				tv_item_help_2.setVisibility(View.VISIBLE);
			}

		} else {
			img_off.setBounds(0, 0, img_off.getMinimumWidth(), img_off.getMinimumHeight());
			btn_item_help.setCompoundDrawables(null, null, img_off, null); //设置左图标
			
			tv_item_help_1.setVisibility(View.GONE);
			tv_item_help_2.setVisibility(View.GONE);
		}
	}

}
