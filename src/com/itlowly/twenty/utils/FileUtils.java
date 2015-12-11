package com.itlowly.twenty.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.widget.Toast;

public class FileUtils {

	/**
	 * 复制文件
	 * 
	 * @param oldPath
	 * @param newPath
	 */
	public static boolean copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();
			return false;
		}
		return true;

	}

	public static void fileCopy(File dbFile, File backup) throws IOException {
		FileChannel inChannel = new FileInputStream(dbFile).getChannel();
		FileChannel outChannel = new FileOutputStream(backup).getChannel();
		try {
			inChannel.transferTo(0, inChannel.size(), outChannel);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("复制单个文件操作出错");
		} finally {
			if (inChannel != null) {
				inChannel.close();
			}
			if (outChannel != null) {
				outChannel.close();
			}
		}
	}

	// 备份文件
	public static boolean backupToSD(Context context) {

		String oldpath = Environment.getDataDirectory()
				+ "/data/com.itlowly.twenty/databases/" + "LocalNote.db";

		String newPath = Environment.getExternalStorageDirectory().toString()
				+ "/TwentyHours";

		File destDir = new File(newPath);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}

		newPath = newPath + "/LocalNote.db";

		return copyFile(oldpath, newPath);
	}

	/**
	 * 从sd卡中还原数据
	 * 
	 * @param context
	 * @return
	 */
	public static boolean restoreFromSD(Context context) {

		String Path = Environment.getExternalStorageDirectory().toString()
				+ "/TwentyHours" + "/LocalNote.db";

		String newPath = Environment.getDataDirectory()
				+ "/data/com.itlowly.twenty/databases/" + "LocalNote.db";

		File destFile = new File(Path);

		if (!destFile.exists()) {
			Toast.makeText(context, "您还没备份过呢，请先备份过", 0).show();
			return false;
		} else {
			// 已经备份过,把备份文件拷贝到数据库文件夹下

			return copyFile(Path, newPath);

		}
	}

	/**
	 * 删除数据
	 * 
	 * @param databaseName
	 * @return
	 */
	public static boolean deleteData(String databaseName) {
		String Path = Environment.getDataDirectory()
				+ "/data/com.itlowly.twenty/databases/" + databaseName + ".db";

		File destFile = new File(Path);

		if (!destFile.exists()) {

			return false;
		} else {
			destFile.delete();
			return true;
		}
	}

}
