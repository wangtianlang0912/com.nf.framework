package com.nf.framework.http.downLoad;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nf.framework.exception.LogUtil;

/**
 * 
 * 文件下载进度监控
 */
public class DownLoadFileProcressDAO {  
	private static DownLoadFileProcressDAO dao=null;
	private Context context; 
	private  DownLoadFileProcressDAO(Context context) { 
		this.context=context;
	}
	public static  DownLoadFileProcressDAO getInstance(Context context){
		if(dao==null){
			  synchronized( DownLoadFileProcressDAO.class ) {
				  dao=new DownLoadFileProcressDAO(context); 
			  }
		}
		return dao;
	}
	public  SQLiteDatabase getConnection() {
		SQLiteDatabase sqliteDatabase = null;
		try { 
			sqliteDatabase= new DBHelper(context).getReadableDatabase();
		} catch (Exception e) {  
			LogUtil.writeExceptionLog(context, e);
		}
		return sqliteDatabase;
	}

	/**
	 * 查看数据库中是否有数据
	 */
	public synchronized boolean isHasInfors(String urlstr,int userId) {
		SQLiteDatabase database = getConnection();
		int count = 0;
		Cursor cursor = null;
		try {
			String sql = "select count(*)  from download_info where url=? and userId=?";
			cursor = database.rawQuery(sql, new String[] { urlstr,String.valueOf(userId) });
			if (cursor.moveToFirst()) {
				count = cursor.getInt(0);
			} 
		} catch (Exception e) {
			LogUtil.writeExceptionLog(context, e);
			e.printStackTrace();
		} finally {
			if (null != database) {
				database.close();
			}
			if (null != cursor) {
				cursor.close();
			}
		}
		return count != 0;
	}
	/**
	 * 查看数据库中是否有数据
	 */
	public synchronized boolean isHasInfors(int downloadItemId,int userId) {
		SQLiteDatabase database = getConnection();
		int count = 0;
		Cursor cursor = null;
		try {
			String sql = "select count(*)  from download_info where downloadItemId=? and userId=?";
			cursor = database.rawQuery(sql, new String[] { String.valueOf(downloadItemId),String.valueOf(userId) });
			if (cursor.moveToFirst()) {
				count = cursor.getInt(0);
			} 
		} catch (Exception e) {
			LogUtil.writeExceptionLog(context, e);
			e.printStackTrace();
		} finally {
			if (null != database) {
				database.close();
			}
			if (null != cursor) {
				cursor.close();
			}
		}
		return count != 0;
	}
	/**
	 * 保存 下载的具体信息
	 */
	public synchronized void saveInfos(List<DownLoadInfoVO> infos) {
		SQLiteDatabase database = getConnection();
		try {
			for (DownLoadInfoVO info : infos) {
				String sql = "insert into download_info(thread_id,start_pos, totalFileSize,compelete_size,url,downLoadItemId,userId) values (?,?,?,?,?,?,?)";
				Object[] bindArgs = { info.getThreadId(), info.getStartPos(),
						info.getTotalFileSize(), info.getCompeleteSize(),
						info.getUrl(),info.getDownLoadItemId(),info.getUserId()};
				database.execSQL(sql, bindArgs);
			}
		} catch (Exception e) {
			LogUtil.writeExceptionLog(context, e);
			e.printStackTrace();
		} finally {
			if (null != database) {
				database.close();
			}
		}
	}
	/**
	 * 保存 下载的具体信息
	 */
	public synchronized void saveInfo(DownLoadInfoVO info) {
		SQLiteDatabase database = getConnection();
		try {
				String sql = "insert into download_info(thread_id,start_pos, totalFileSize,compelete_size,url,downLoadItemId,userId) values (?,?,?,?,?,?,?)";
				Object[] bindArgs = { info.getThreadId(), info.getStartPos(),
						info.getTotalFileSize(), info.getCompeleteSize(),
						info.getUrl(),info.getDownLoadItemId(),info.getUserId()};
				database.execSQL(sql, bindArgs);
		} catch (Exception e) {
			LogUtil.writeExceptionLog(context, e);
			e.printStackTrace();
		} finally {
			if (null != database) {
				database.close();
			}
		}
	}

	/**
	 * 得到下载具体信息
	 */
	public synchronized List<DownLoadInfoVO> getInfos(String urlstr) {
		List<DownLoadInfoVO> list = new ArrayList<DownLoadInfoVO>();
		SQLiteDatabase database = getConnection();
		Cursor cursor = null;
		try {
			String sql = "select thread_id, start_pos, totalFileSize,compelete_size,url,downLoadItemId,userId from download_info where url=?";
			cursor = database.rawQuery(sql, new String[] { urlstr });
			while (cursor.moveToNext()) {
				DownLoadInfoVO info = new DownLoadInfoVO(cursor.getInt(0),
						cursor.getInt(1), cursor.getInt(2), cursor.getInt(3),
						cursor.getString(4),cursor.getInt(5),cursor.getInt(6));
				list.add(info);
			}
		} catch (Exception e) {
			LogUtil.writeExceptionLog(context, e);
		} finally {
			if (null != database) {
				database.close();
			}
			if (null != cursor) {
				cursor.close();
			}
		}
		return list;
	}
	/**
	 * 得到下载具体信息
	 */
	public synchronized List<DownLoadInfoVO> getInfos(int downloadItemid,int userId) {
		List<DownLoadInfoVO> list = new ArrayList<DownLoadInfoVO>();
		SQLiteDatabase database = getConnection();
		Cursor cursor = null;
		try {
			String sql = "select thread_id, start_pos, totalFileSize,compelete_size,url,downLoadItemId,userId from download_info where downloadItemid=? and userId=?";
			cursor = database.rawQuery(sql, new String[] {String.valueOf(downloadItemid),String.valueOf(userId)});
			while (cursor.moveToNext()) {
				DownLoadInfoVO info = new DownLoadInfoVO(cursor.getInt(0),
						cursor.getInt(1), cursor.getInt(2), cursor.getInt(3),
						cursor.getString(4),cursor.getInt(5),cursor.getInt(6));
				list.add(info);
			}
		} catch (Exception e) {
			LogUtil.writeExceptionLog(context, e);
		} finally {
			if (null != database) {
				database.close();
			}
			if (null != cursor) {
				cursor.close();
			}
		}
		return list;
	}
	/**
	 * 更新数据库中的下载信息
	 */
	public synchronized void updataInfos(int threadId, int compeleteSize, String urlstr) {
		SQLiteDatabase database = getConnection();
		try {
			String sql = "update download_info set compelete_size=? where thread_id=? and url=?";
			Object[] bindArgs = { compeleteSize, threadId, urlstr };
			database.execSQL(sql, bindArgs);
		} catch (Exception e) {
			LogUtil.writeExceptionLog(context, e);
		} finally {
			if (null != database) {
				database.close();
			}
		}
	}
	/**
	 * 下载完成后删除数据库中的数据
	 */
	public synchronized void deleteByItemId(int downLoadItemId,int userId) {
		SQLiteDatabase database = getConnection();
		try {
			database.delete("download_info", "downLoadItemId=? and userId=?", new String[] { String.valueOf(downLoadItemId),String.valueOf(userId) });
		} catch (Exception e) {
			LogUtil.writeExceptionLog(context, e);
		} finally {
			if (null != database) {
				database.close();
			}
		}
	}
	/**
	 * 下载完成后删除数据库中的数据
	 */
	public synchronized void delete(String url,int userId) {
		SQLiteDatabase database = getConnection();
		try {
			database.delete("download_info", "url=? and userId=?", new String[] { url,String.valueOf(userId) });
		} catch (Exception e) {
			LogUtil.writeExceptionLog(context, e);
		} finally {
			if (null != database) {
				database.close();
			}
		}
	}
	
    /**
     * 建立一个数据库帮助类
     */
	private class DBHelper extends SQLiteOpenHelper {
		//download.db-->数据库名
		private DBHelper(Context context) {
			super(context, "download.db", null, 1);
		}
    
    /**
     * 在download.db数据库下创建一个download_info表存储下载信息
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table download_info(_id integer PRIMARY KEY AUTOINCREMENT, thread_id integer, "
                + "start_pos integer, totalFileSize integer, compelete_size integer,url char, downLoadItemId integer, userId integer)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
}