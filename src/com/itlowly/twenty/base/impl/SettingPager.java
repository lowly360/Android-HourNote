package com.itlowly.twenty.base.impl;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;

import com.bmob.BTPFileResponse;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.DeleteFileListener;
import com.bmob.btp.callback.UploadListener;
import com.itlowly.twenty.R;
import com.itlowly.twenty.activity.HomeActivity;
import com.itlowly.twenty.base.ContentBasePager;
import com.itlowly.twenty.bean.FileNameTable;
import com.itlowly.twenty.db.BmobDB;
import com.itlowly.twenty.fragment.ContentFragment;
import com.itlowly.twenty.fragment.LeftMenuFragment;

/**
 * 主页的实现类
 * 
 * @author Administrator
 * 
 */
public class SettingPager extends ContentBasePager implements OnClickListener {

	private Button leftmenu_setting_history;
	private Button leftmenu_setting_password;
	private Button leftmenu_setting_account;
	private Button leftmenu_setting_deletedata;
	private Button leftmenu_setting_about;
	private Button leftmenu_setting_exit;
	private ImageButton btn_setting_menu;
	private HomeActivity mainUI;
	private ContentFragment contentFragment;
	private Button leftmenu_setting_locate;
	private Button leftmenu_setting_backup;
	private Button leftmenu_setting_backupfromService;
	private SharedPreferences mPre;

	public SettingPager(Activity activity) {
		super(activity);
	}

	@Override
	public View initView() {
		mainUI = (HomeActivity) mActivity;

		mPre = mActivity.getSharedPreferences("config", Context.MODE_PRIVATE);

		mRootView = View.inflate(mActivity, R.layout.content_setting_pager,
				null);

		btn_setting_menu = (ImageButton) mRootView
				.findViewById(R.id.btn_setting_menu);

		leftmenu_setting_locate = (Button) mRootView
				.findViewById(R.id.leftmenu_setting_locate);

		leftmenu_setting_password = (Button) mRootView
				.findViewById(R.id.leftmenu_setting_password);

		leftmenu_setting_history = (Button) mRootView
				.findViewById(R.id.leftmenu_setting_history);

		leftmenu_setting_account = (Button) mRootView
				.findViewById(R.id.leftmenu_setting_account);

		leftmenu_setting_backup = (Button) mRootView
				.findViewById(R.id.leftmenu_setting_backup);

		leftmenu_setting_backupfromService = (Button) mRootView
				.findViewById(R.id.leftmenu_setting_backupfromService);

		leftmenu_setting_deletedata = (Button) mRootView
				.findViewById(R.id.leftmenu_setting_deletedata);

		leftmenu_setting_about = (Button) mRootView
				.findViewById(R.id.leftmenu_setting_about);

		leftmenu_setting_exit = (Button) mRootView
				.findViewById(R.id.leftmenu_setting_exit);

		if (mPre.getBoolean("LocateMode", false)) {
			leftmenu_setting_locate.setText("   开启用户模式");
		} else {
			leftmenu_setting_locate.setText("   开启本地模式");
		}

		initListener();

		return mRootView;

	}

	private void initListener() {
		leftmenu_setting_locate.setOnClickListener(this);
		leftmenu_setting_password.setOnClickListener(this);
		leftmenu_setting_history.setOnClickListener(this);
		leftmenu_setting_account.setOnClickListener(this);

		leftmenu_setting_backup.setOnClickListener(this);
		leftmenu_setting_backupfromService.setOnClickListener(this);
		leftmenu_setting_deletedata.setOnClickListener(this);

		leftmenu_setting_about.setOnClickListener(this);
		leftmenu_setting_exit.setOnClickListener(this);

		btn_setting_menu.setOnClickListener(this);

		boolean isLocate = mPre.getBoolean("LocateMode", false);

		if (!isLocate) {
			setLeftMenuButton(true);
		} else {
			setLeftMenuButton(false);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_setting_menu:
			mainUI.getSlidingMenu().toggle();
			break;

		case R.id.leftmenu_setting_locate:
			boolean locateMode = mPre.getBoolean("LocateMode", false);
			String userName = mPre.getString("LocateUser","LocalNote");

			if (locateMode) {//本地模式切换成用户模式
				//把locateMode设置为false，并提交
				mPre.edit().putBoolean("LocateMode",false).commit();
				
				leftmenu_setting_locate.setText("   开启本地模式");
				
				Toast.makeText(mActivity, "用户模式已开启", 0).show();
				
			} else {//用户模式切换成本地模式
				//把locateMode设置为true，并提交
				mPre.edit().putBoolean("LocateMode", true).commit();
				
				if (userName.equals("LocalNote")) {
					
				}else {
					// 如果已经登录了，先注销账号
					mPre.edit().putBoolean("SignIn", false).commit();
					//注销处理
					loginout();
				}
				
				leftmenu_setting_locate.setText("   开启用户模式");
				
				//备份数据到网上
				backupToService(userName);
				
				Toast.makeText(mActivity, "本地模式已开启", 0).show();
				
				// 转为本地数据
				mPre.edit().putString("LocateUser", "LocalNote").commit();
				// 通知homepage更变数据
				ContentFragment contentFragment2 = (ContentFragment) mainUI
						.getContentFragment();
				HomePager homePager = contentFragment2.getHomePager();
				homePager.UpdateAll();
				
				setLeftMenuButton(false);
			
			}
			
			//通知侧边栏改变侧边栏按钮提示
			LeftMenuFragment leftMenuFragment = (LeftMenuFragment) mainUI
					.getLeftMenuFragment();
			leftMenuFragment.setLoginBtn();
			break;

		case R.id.leftmenu_setting_password:

			break;

		case R.id.leftmenu_setting_history:

			break;

		case R.id.leftmenu_setting_account:

			break;

		case R.id.leftmenu_setting_backup:

			break;

		case R.id.leftmenu_setting_backupfromService:

			// String pathUrl =
			// Environment.getDataDirectory().get+"/"+fileName+".db";

			BmobDB db = new BmobDB(mActivity);

			db.clearAllUploadData();
			
			// 同步数据到服务器
			backupToService(mPre.getString("LocateUser", ""));

			break;

		case R.id.leftmenu_setting_deletedata:

			break;
		case R.id.leftmenu_setting_about: // 跳到关于页面
			mainUI = (HomeActivity) mActivity;
			contentFragment = (ContentFragment) mainUI.getContentFragment();
			contentFragment.setCurrenerPager(3);

			break;
		case R.id.leftmenu_setting_exit:

			loginout();

			break;
		}
	}

	/**
	 * 查询服务器是已经存有数据，如果有则先删除数据后，再同步数据到网上
	 */
	private void backupToService(String userName) {

		final String fileLocateName = userName;

		final String pathUrl = mActivity.getDatabasePath(fileLocateName)
				.getPath() + ".db";

		BmobQuery<FileNameTable> query = new BmobQuery<FileNameTable>();
		// 查询fileLocateName叫“userName”的数据
		query.addWhereEqualTo("fileLocateName", userName);

		System.out.println("同步过程：userName" + userName);

		query.setLimit(2);

		query.findObjects(mActivity, new FindListener<FileNameTable>() {

			@Override
			public void onError(int arg0, String arg1) {
				System.out.println("同步过程：查询不到文件名");

				// 找不到说明没有同步过，直接保存新的数据到网上
				upLoadData(pathUrl, fileLocateName, 1, null);
			}

			@Override
			public void onSuccess(final List<FileNameTable> arg0) {
				System.out.println("同步过程：查询文件名成功,准备同步数据到服务器");

				// 先删除服务器数据
				BmobProFile.getInstance(mActivity).deleteFile(
						arg0.get(0).getFileServerName(),
						new DeleteFileListener() {

							@Override
							public void onError(int errorcode, String errormsg) {
								// TODO Auto-generated method stub
								Log.i("bmob", "删除文件失败：" + errormsg + "("
										+ errorcode + ")");
							}

							@Override
							public void onSuccess() {
								// TODO Auto-generated method stub
								Log.i("bmob", "删除文件成功");

								// 删除成功后再保存数据
								upLoadData(pathUrl, fileLocateName, 0,
										arg0.get(0));

							}

						});
			}
		}

		);
	}

	/**
	 * 上传数据到服务器
	 * 
	 * @param pathUrl
	 * @param fileLocateName
	 */
	private void upLoadData(String pathUrl, final String fileLocateName,
			final int flag, final FileNameTable fileObject) {
		BTPFileResponse response = BmobProFile.getInstance(mActivity).upload(
				pathUrl, new UploadListener() {

					@Override
					public void onError(int arg0, String arg1) {
						Toast.makeText(mActivity, "备份数据成功", 0).show();

					}

					@Override
					public void onSuccess(String fileName, String url,
							BmobFile file) {
						Toast.makeText(mActivity, "备份数据成功", 0).show();

						// 保存新的文件名到服务器
						FileNameTable fiBmob = new FileNameTable();
						fiBmob.setFileLocateName(fileLocateName);
						fiBmob.setFileServerName(fileName);

						if (flag == 0) {

							if (fileObject != null) {
								
								fileObject.setFileLocateName(fileLocateName);
								fileObject.setFileServerName(fileName);
								
								fileObject.update(mActivity);
							}else {
								System.out.println("同步过程：发生未知错误，fileobjec为空");
							}

						} else if (flag == 1) {
							fiBmob.save(mActivity);
						}

					}

					@Override
					public void onProgress(int arg0) {
						// TODO Auto-generated
						// method stub

					}
				});
	}

	/**
	 * 注销
	 */
	public void loginout() {
		Toast.makeText(mActivity, "注销成功！", 0).show();

		mPre.edit().putString("LocateUser", "LocalNote").commit();
		mPre.edit().putBoolean("SignIn", false).commit();

		// 通知homepage更变数据
		ContentFragment contentFragment2 = (ContentFragment) mainUI
				.getContentFragment();
		HomePager homePager = contentFragment2.getHomePager();
		homePager.UpdateAll();

		LeftMenuFragment leftMenuFragment = (LeftMenuFragment) mainUI
				.getLeftMenuFragment();
		leftMenuFragment.setLoginBtn();

		setLeftMenuButton(false);

	}
	
	/**
	 * 设置setting界面中的按钮是否能被点击
	 * @param isClickable
	 */
	private void setLeftMenuButton(boolean isClickable){
		
		if (isClickable) {
			leftmenu_setting_backupfromService.setTextColor(Color.BLACK);
			leftmenu_setting_account.setTextColor(Color.BLACK);
			leftmenu_setting_exit.setTextColor(Color.BLACK);
		}else {
			leftmenu_setting_backupfromService.setTextColor(Color.GRAY);
			leftmenu_setting_account.setTextColor(Color.GRAY);
			leftmenu_setting_exit.setTextColor(Color.GRAY);
		}
		
		leftmenu_setting_backupfromService.setClickable(isClickable);
		leftmenu_setting_account.setClickable(isClickable);
		leftmenu_setting_exit.setClickable(isClickable);
		
	}

}
