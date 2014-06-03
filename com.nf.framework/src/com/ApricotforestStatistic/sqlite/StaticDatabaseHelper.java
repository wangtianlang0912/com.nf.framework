package com.ApricotforestStatistic.sqlite;

import java.io.File;
import java.sql.SQLException;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ApricotforestStatistic.VO.StaticErrorRequestVO;
import com.ApricotforestStatistic.VO.StaticEventVO;
import com.ApricotforestStatistic.VO.StaticPageViewRecordVO;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public  class StaticDatabaseHelper extends OrmLiteSqliteOpenHelper {
	
    private static final int DATABASE_VERSION =1;
	
    private static StaticDatabaseHelper staticDbHelper=null;
    private static String currentDataBaseName;
	public static StaticDatabaseHelper getInstance(Context context,String dataBaseName){
		
		if(staticDbHelper==null||currentDataBaseName!=dataBaseName){
			staticDbHelper=	new StaticDatabaseHelper(context,dataBaseName);
			currentDataBaseName=dataBaseName;
		}
		return staticDbHelper;
	}
	
    private Dao<StaticEventVO,String> staticEventDAO;
	
	public Dao<StaticEventVO, String> getStaticEventDAO() throws SQLException {
		if (staticEventDAO == null) {
			staticEventDAO = getDao(StaticEventVO.class);
		}
		return staticEventDAO;
	}
	private Dao<StaticPageViewRecordVO,String> staticPageViewDAO;
	
	public Dao<StaticPageViewRecordVO, String> getStaticViewPageDAO() throws SQLException {
		if (staticPageViewDAO == null) {
			staticPageViewDAO = getDao(StaticPageViewRecordVO.class);
		}
		return staticPageViewDAO;
	}
	
	private Dao<StaticErrorRequestVO,String> staticRequestDAO;
	
	public Dao<StaticErrorRequestVO, String> getStaticErrorRequestDAO() throws SQLException {
		if (staticRequestDAO == null) {
			staticRequestDAO = getDao(StaticErrorRequestVO.class);
		}
		return staticRequestDAO;
	}
	
	private StaticDatabaseHelper(Context context,String dataBaseName) {
		
        super(new DatabaseContext(context), dataBaseName, null, DATABASE_VERSION);
    }
	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		// TODO Auto-generated method stub
		try { 
			   TableUtils.createTable(arg1, StaticEventVO.class);
			   TableUtils.createTable(arg1, StaticPageViewRecordVO.class);
			   TableUtils.createTable(arg1, StaticErrorRequestVO.class);
			  } catch (SQLException e) { 
			   Log.e(StaticDatabaseHelper.class.getName(), "创建数据库失败", e); 
			  } 
	}
	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1,
			int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}
	
	
}
/**
 * 这个类用来解决在2.1及以前的版本不能打开放在SD卡上的数据库文件的问题
 * 
 * @author niufei
 * 
 */
class DatabaseContext extends ContextWrapper {
 
    private static final String DB_PATH = "/";
 
    public DatabaseContext(Context base) {
        super(base);
    }
 
    @Override
    public File getDatabasePath(String name) {
        File path = new File(DB_PATH);
        if (!path.exists()) {
            path.mkdirs();
        }
 
        return new File(path, name);
    }
 
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode,
            SQLiteDatabase.CursorFactory factory) {
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
        return result;
    }
}