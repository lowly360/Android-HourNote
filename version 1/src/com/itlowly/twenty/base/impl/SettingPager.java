package com.itlowly.twenty.base.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.DeleteFileListener;
import com.bmob.btp.callback.DownloadListener;
import com.bmob.btp.callback.UploadListener;
import com.itlowly.twenty.R;
import com.itlowly.twenty.activity.HistoryActivity;
import com.itlowly.twenty.activity.HomeActivity;
import com.itlowly.twenty.activity.LoginActivity;
import com.itlowly.twenty.activity.UserActivity;
import com.itlowly.twenty.base.ContentBasePager;
import com.itlowly.twenty.bean.FileNameTable;
import com.itlowly.twenty.db.BmobDB;
import com.itlowly.twenty.db.LocalNoteDB;
import com.itlowly.twenty.fragment.ContentFragment;
import com.itlowly.twenty.fragment.LeftMenuFragment;
import com.itlowly.twenty.utils.DensityUtils;
import com.itlowly.twenty.utils.FileUtils;
import com.itlowly.twenty.utils.Md5Uils;

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

		Drawable lock = mActivity.getResources().getDrawable(R.drawable.lock);
		lock.setBounds(0, 0, lock.getMinimumWidth(), lock.getMinimumHeight());

		Drawable lock_open = mActivity.getResources().getDrawable(
				R.drawable.lock_open);
		lock_open.setBounds(0, 0, lock_open.getMinimumWidth(),
				lock_open.getMinimumHeight());

		if (mPre.getBoolean("isLocalPasswd", false)) {
			leftmenu_setting_password.setCompoundDrawables(null, null, lock,
					null);
		} else {
			leftmenu_setting_password.setCompoundDrawables(null, null,
					lock_open, null);
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
			String userName = mPre.getString("LocateUser", "LocalNote");

			if (locateMode) {// 本地模式切换成用户模式
				// 把locateMode设置为false，并提交
				mPre.edit().putBoolean("LocateMode", false).commit();

				leftmenu_setting_locate.setText("   开启本地模式");

				Toast.makeText(mActivity, "用户模式已开启", Toast.LENGTH_SHORT).show();

			} else {// 用户模式切换成本地模式
				// 把locateMode设置为true，并提交
				mPre.edit().putBoolean("LocateMode", true).commit();

				if (userName.equals("LocalNote")) {

				} else {
					// 如果已经登录了，先注销账号
					mPre.edit().putBoolean("SignIn", false).commit();
					// 注销处理
					loginout();
				}

				leftmenu_setting_locate.setText("   开启用户模式");

				// 备份数据到网上
				backupToService(userName, null, null);

				Toast.makeText(mActivity, "本地模式已开启", Toast.LENGTH_SHORT).show();

				// 转为本地数据
				mPre.edit().putString("LocateUser", "LocalNote").commit();
				// 通知homepage更变数据
				ContentFragment contentFragment2 = (ContentFragment) mainUI
						.getContentFragment();
				HomePager homePager = contentFragment2.getHomePager();
				homePager.UpdateAll();

				setLeftMenuButton(false);

			}

			// 通知侧边栏改变侧边栏按钮提示
			LeftMenuFragment leftMenuFragment = (LeftMenuFragment) mainUI
					.getLeftMenuFragment();
			leftMenuFragment.setLoginBtn();
			break;

		case R.id.leftmenu_setting_password:

			boolean isLocalPasswd = mPre.getBoolean("isLocalPasswd", false);

			if (!isLocalPasswd) {
				System.out.println("???????");
				showLocalPasswdDialog(true);

			} else {
				System.out.println("??!!!!??");
				showLocalPasswdDialog(false); // 输入密码取消本地密码

			}

			break;

		case R.id.leftmenu_setting_history:
			// 历史纪录
			Intent intent = new Intent(mActivity, HistoryActivity.class);

			mActivity.startActivity(intent);

			break;

		case R.id.leftmenu_setting_account:

			// 用户账号

			HomeActivity mainUI = (HomeActivity) mActivity;

			Intent userIntent = new Intent(mActivity, UserActivity.class);

			mainUI.startActivityForResult(userIntent, 100);

			break;

		case R.id.leftmenu_setting_backup:

			showBackUpDialog();

			break;

		case R.id.leftmenu_setting_backupfromService:
			// 同步数据
			// 应分为两个选择 1.备份上网 2.从网上同步到本地内容

			// 显示对话框

			showBakUpOnNetDialog();

			break;

		case R.id.leftmenu_setting_deletedata:
			// 删除所有数据

			showDeleteDataDialog();

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
	 * 显示删除数据对话框
	 */
	private void showDeleteDataDialog() {
		final Dialog dialog = new Dialog(mActivity, R.style.CustomDialog);

		View view = View.inflate(mActivity, R.layout.dialog_setting_deletedata,
				null);

		ImageView iv_ok = (ImageView) view.findViewById(R.id.iv_ok);
		ImageView iv_cancel = (ImageView) view.findViewById(R.id.iv_cancel);

		iv_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		iv_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				LocalNoteDB db = new LocalNoteDB(mActivity);

				if (db.deleteAllData()) {

					Toast.makeText(mActivity, "删除成功", Toast.LENGTH_SHORT)
							.show();

					// 通知主页更改数据
					HomeActivity mainUI = (HomeActivity) mActivity;

					ContentFragment fragment = (ContentFragment) mainUI
							.getContentFragment();
					HomePager pager = (HomePager) fragment.getPager();
					pager.UpdateAll();

				} else {
					Toast.makeText(mActivity, "删除失败", Toast.LENGTH_SHORT)
							.show();
				}

				dialog.dismiss();
			}
		});

		dialog.setContentView(view);

		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();

		lp.width = DensityUtils.dp2px(mActivity, 230);
		lp.height = DensityUtils.dp2px(mActivity, 200);

		dialog.getWindow().setAttributes(lp);
		dialog.show();
	}

	/**
	 * 显示设置本地密码对话框
	 * 
	 * @param isPassWd
	 *            是否设置新密码或取消本地密码
	 */
	private void showLocalPasswdDialog(final boolean isPassWd) {

		final Dialog dialog = new Dialog(mActivity, R.style.CustomDialog);

		View view = View.inflate(mActivity, R.layout.dialog_setting_passwd,
				null);

		ImageView iv_ok = (ImageView) view.findViewById(R.id.iv_ok);
		ImageView iv_cancel = (ImageView) view.findViewById(R.id.iv_cancel);
		TextView tv_dialog_hint = (TextView) view
				.findViewById(R.id.tv_dialog_hint);

		final EditText et_setting_password_first = (EditText) view
				.findViewById(R.id.et_setting_password_first);
		final EditText et_setting_password_second = (EditText) view
				.findViewById(R.id.et_setting_password_second);

		if (!isPassWd) {
			tv_dialog_hint.setText("取消本地密码");
			et_setting_password_second.setVisibility(View.GONE);
		}

		iv_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		iv_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String first_passwd = et_setting_password_first.getText()
						.toString().trim();
				String second_passwd = et_setting_password_second.getText()
						.toString().trim();

				if (first_passwd.equals("")) {
					Toast.makeText(mActivity, "请输入密码！！！", Toast.LENGTH_SHORT)
							.show();
					return;

				}

				if (isPassWd) {
					// 设置新密码
					if (second_passwd.equals("")) {
						Toast.makeText(mActivity, "请再次输入密码确认",
								Toast.LENGTH_SHORT).show();
						return;
					}
					if (second_passwd.equals(first_passwd)) {
						String encode = Md5Uils.encode(first_passwd
								+ "TwentyHours");

						mPre.edit().putBoolean("isLocalPasswd", true).commit();

						mPre.edit().putString("LocalPassWd", encode).commit();

						setPassWdIcon();
						dialog.dismiss();
					} else {
						Toast.makeText(mActivity, "两次密码不一致，请确认",
								Toast.LENGTH_SHORT).show();
					}

				} else {
					// 取消本地密码
					String LocalPassWd = mPre.getString("LocalPassWd", "");
					if (Md5Uils.encode(first_passwd + "TwentyHours").equals(
							LocalPassWd)) {
						mPre.edit().putBoolean("isLocalPasswd", false).commit();
						mPre.edit().putString("LocalPassWd", "").commit();

						setPassWdIcon();
						dialog.dismiss();
					} else {
						Toast.makeText(mActivity, "密码输入错误，请重新输入",
								Toast.LENGTH_SHORT).show();

						et_setting_password_first.setText("");
					}
				}

			}
		});

		dialog.setContentView(view);

		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();

		lp.width = DensityUtils.dp2px(mActivity, 312);
		lp.height = DensityUtils.dp2px(mActivity, 255);

		dialog.getWindow().setAttributes(lp);
		dialog.show();

	}

	/**
	 * 设置本地密码条目的右图案资源
	 */
	private void setPassWdIcon() {
		Drawable lock = mActivity.getResources().getDrawable(R.drawable.lock);
		lock.setBounds(0, 0, lock.getMinimumWidth(), lock.getMinimumHeight());

		Drawable lock_open = mActivity.getResources().getDrawable(
				R.drawable.lock_open);
		lock_open.setBounds(0, 0, lock_open.getMinimumWidth(),
				lock_open.getMinimumHeight());

		if (mPre.getBoolean("isLocalPasswd", false)) {
			leftmenu_setting_password.setCompoundDrawables(null, null, lock,
					null);
		} else {
			leftmenu_setting_password.setCompoundDrawables(null, null,
					lock_open, null);
		}
	}

	/**
	 * 显示备份与还原对话框
	 */
	private void showBackUpDialog() {
		final Dialog dialog = new Dialog(mActivity, R.style.CustomDialog);
		View view = View.inflate(mActivity, R.layout.dialog_setting_backup,
				null);

		TextView tv_backup_backuptosd = (TextView) view
				.findViewById(R.id.tv_backup_backuptosd);
		TextView tv_backup_backupfromsd = (TextView) view
				.findViewById(R.id.tv_backup_backupfromsd);

		// 备份到sd卡中
		tv_backup_backuptosd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (FileUtils.backupToSD(mActivity)) {

					Toast.makeText(mActivity, "备份成功", Toast.LENGTH_SHORT)
							.show();

					dialog.dismiss();
				} else {
					Toast.makeText(mActivity, "备份失败，请重启软件再试",
							Toast.LENGTH_SHORT).show();

					dialog.dismiss();
				}
			}
		});
		// 从sd卡中还原备份
		tv_backup_backupfromsd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (FileUtils.restoreFromSD(mActivity)) {
					Toast.makeText(mActivity, "还原成功", Toast.LENGTH_SHORT)
							.show();
					// 通知主页面，更新数据

					// 通知homepage更变数据
					ContentFragment contentFragment = (ContentFragment) mainUI
							.getContentFragment();
					HomePager homePager = contentFragment.getHomePager();
					homePager.UpdateAll();

				}
				dialog.dismiss();
			}
		});

		dialog.setContentView(view);

		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();

		lp.width = DensityUtils.dp2px(mActivity, 312);
		lp.height = DensityUtils.dp2px(mActivity, 255);

		dialog.getWindow().setAttributes(lp);
		dialog.show();

	}

	/**
	 * 显示同步数据对话框
	 */
	private void showBakUpOnNetDialog() {
		final Dialog dialog = new Dialog(mActivity, R.style.CustomDialog);
		View view = View.inflate(mActivity,
				R.layout.dialog_setting_backuponnet, null);

		final TextView tv_backup_backuptonet = (TextView) view
				.findViewById(R.id.tv_backup_backuptonet);
		final TextView tv_backup_backupfromnet = (TextView) view
				.findViewById(R.id.tv_backup_backupfromnet);

		final LinearLayout ll_progress = (LinearLayout) view
				.findViewById(R.id.ll_progress);

		final TextView tv_setting_llprogress = (TextView) view
				.findViewById(R.id.tv_setting_llprogress);

		// 备份到云端中
		tv_backup_backuptonet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				BmobDB db = new BmobDB(mActivity);
				// 删除bmobSDK自动生成的上传缓存，以便我可以直接更新服务端的文件
				db.clearAllUploadData();

				ll_progress.setVisibility(View.VISIBLE);
				tv_backup_backuptonet.setVisibility(View.GONE);
				tv_backup_backupfromnet.setVisibility(View.GONE);

				// 同步数据到服务器
				backupToService(mPre.getString("LocateUser", ""),
						tv_setting_llprogress, dialog);

			}
		});
		// 从云端中还原备份
		tv_backup_backupfromnet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				ll_progress.setVisibility(View.VISIBLE);
				tv_backup_backuptonet.setVisibility(View.GONE);
				tv_backup_backupfromnet.setVisibility(View.GONE);

				restoreFromNet(dialog, tv_setting_llprogress);

				// 通知主页面，更新数据

			}
		});

		dialog.setContentView(view);

		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();

		lp.width = DensityUtils.dp2px(mActivity, 312);
		lp.height = DensityUtils.dp2px(mActivity, 255);

		dialog.getWindow().setAttributes(lp);
		dialog.show();

	}

	/**
	 * 从网上下载文件到本地
	 * 
	 * @param dialog
	 * @param tv_setting_llprogress
	 */
	protected void restoreFromNet(final Dialog dialog,
			final TextView tv_progress) {
		// TODO Auto-generated method stub
		BmobQuery<FileNameTable> query = new BmobQuery<FileNameTable>();
		// 查询fileLocateName叫“userName”的数据
		query.addWhereEqualTo("fileLocateName",
				mPre.getString("LocateUser", ""));

		query.setLimit(2);

		query.findObjects(mActivity, new FindListener<FileNameTable>() {

			@Override
			public void onError(int arg0, String arg1) {
				Toast.makeText(mActivity, "获取文件名失败！" + arg0, 0).show();

				// 获取失败，证明没同步过数据，所以直接跳转到主页面

			}

			@Override
			public void onSuccess(List<FileNameTable> arg0) {
				// Toast.makeText(LoginActivity.this,
				// "获取文件名成功！" + arg0, 0).show();

				String fileName = arg0.get(0).getFileServerName();

				BmobProFile.getInstance(mActivity).download(fileName,
						new DownloadListener() {

							@Override
							public void onError(int arg0, String arg1) {

							}

							@Override
							public void onSuccess(String fullPath) {

								carryDatebase(fullPath);

							}

							@Override
							public void onProgress(String arg0, int arg1) {
								if (tv_progress != null) {
									tv_progress.setText("同步中~~" + arg1 + "%");
								}
								if (arg1 == 100) {
									if (dialog != null) {
										Toast.makeText(mActivity, "还原成功",
												Toast.LENGTH_SHORT).show();

										dialog.dismiss();
									}
								}

							}
						});

			}
		});
	}

	/**
	 * 把下载好的文件复制到数据库文件中
	 * 
	 * @param fullPath
	 */
	private void carryDatebase(String fullPath) {
		File downFile = new File(fullPath);

		String path = Environment.getDataDirectory()
				+ "/data/com.itlowly.twenty/databases/"
				+ mPre.getString("LocateUser", "") + ".db";

		try {
			FileUtils.fileCopy(new File(fullPath), new File(path));
		} catch (IOException e) {
			System.out.println("复制失败");
			e.printStackTrace();
		}

		downFile.delete();

		// 通知homepage更变数据
		ContentFragment contentFragment = (ContentFragment) mainUI
				.getContentFragment();
		HomePager homePager = contentFragment.getHomePager();
		homePager.UpdateAll();

	}

	/**
	 * 查询服务器是已经存有数据，如果有则先删除数据后，再同步数据到网上
	 */
	private void backupToService(String userName, final TextView tv_progress,
			final Dialog dialog) {

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
				upLoadData(pathUrl, fileLocateName, 1, null, tv_progress,
						dialog);
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
								Log.i("bmob", "删除文件失败：" + errormsg + "("
										+ errorcode + ")");
							}

							@Override
							public void onSuccess() {
								Log.i("bmob", "删除文件成功");

								// 删除成功后再保存数据
								upLoadData(pathUrl, fileLocateName, 0,
										arg0.get(0), tv_progress, dialog);

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
			final int flag, final FileNameTable fileObject,
			final TextView tv_progress, final Dialog dialog) {
		BmobProFile.getInstance(mActivity).upload(pathUrl,
				new UploadListener() {

					@Override
					public void onError(int arg0, String arg1) {
						Toast.makeText(mActivity, "备份数据失败" + arg1,
								Toast.LENGTH_SHORT).show();

					}

					@Override
					public void onSuccess(String fileName, String url,
							BmobFile file) {
						Toast.makeText(mActivity, "备份数据成功", Toast.LENGTH_SHORT)
								.show();

						// 保存新的文件名到服务器
						FileNameTable fiBmob = new FileNameTable();
						fiBmob.setFileLocateName(fileLocateName);
						fiBmob.setFileServerName(fileName);

						if (flag == 0) {
							if (fileObject != null) {
								fileObject.setFileLocateName(fileLocateName);
								fileObject.setFileServerName(fileName);

								fileObject.update(mActivity);
							} else {
								System.out.println("同步过程：发生未知错误，fileobjec为空");
							}

						} else if (flag == 1) {
							fiBmob.save(mActivity);
						}

					}

					@Override
					public void onProgress(int arg0) {
						if (tv_progress != null) {
							tv_progress.setText("同步中~~" + arg0 + "%");
						}
						if (arg0 == 100) {
							if (dialog != null) {
								dialog.dismiss();
							}
						}
					}
				});
	}

	/**
	 * 注销
	 */
	public void loginout() {
		Toast.makeText(mActivity, "注销成功！", Toast.LENGTH_SHORT).show();

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
	 * 
	 * @param isClickable
	 */
	public void setLeftMenuButton(boolean isClickable) {

		if (isClickable) {
			leftmenu_setting_backupfromService.setTextColor(Color.BLACK);
			leftmenu_setting_account.setTextColor(Color.BLACK);
			leftmenu_setting_backup.setTextColor(Color.GRAY);
			leftmenu_setting_exit.setTextColor(Color.BLACK);
		} else {
			leftmenu_setting_backupfromService.setTextColor(Color.GRAY);
			leftmenu_setting_account.setTextColor(Color.GRAY);
			leftmenu_setting_backup.setTextColor(Color.BLACK);
			leftmenu_setting_exit.setTextColor(Color.GRAY);
		}
		leftmenu_setting_backup.setClickable(!isClickable);
		leftmenu_setting_backupfromService.setClickable(isClickable);
		leftmenu_setting_account.setClickable(isClickable);
		leftmenu_setting_exit.setClickable(isClickable);

	}

}
