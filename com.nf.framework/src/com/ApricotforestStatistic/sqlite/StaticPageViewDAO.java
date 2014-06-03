package com.ApricotforestStatistic.sqlite;


import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.ApricotforestStatistic.VO.StaticPageViewRecordVO;
import com.ApricotforestStatistic.VO.StaticPageViewSkipVO;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.PreparedUpdate;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;


public class StaticPageViewDAO {

	private StaticPageViewDAO(){}
	private static class StaticPageViewDAOHolder {  
		private static final StaticPageViewDAO INSTANCE = new StaticPageViewDAO();  
	}  
	public static final StaticPageViewDAO getInstance() {  
	   return StaticPageViewDAOHolder.INSTANCE;  
	}  
	/***
	 * 最新插入一条路径访问记录
	 * @param dbFileName 当前对应数据库
	 * @param preViewCode 从哪里来
	 * @param currentViewCode 当前页面
	 * @throws SQLException 
	 */
	 
	public synchronized void  insertPageView(Context context,String dbFileName,String currentViewCode,int userId,SqlFileModifedCallBack callBack) throws SQLException{
		if(dbFileName==null||context==null){
			return;
		}
		StaticDatabaseHelper	dbOpenHelper=StaticDatabaseHelper.getInstance(context,dbFileName);
		StaticPageViewRecordVO staticPageView=new StaticPageViewRecordVO();
		staticPageView.setCurrentViewCode(currentViewCode);
		staticPageView.setCount(1);
		staticPageView.setUserId(String.valueOf(userId));
		staticPageView.setStartTime(String.valueOf(System.currentTimeMillis()));
		Dao<StaticPageViewRecordVO, String> staticPageViewDao=	dbOpenHelper.getStaticViewPageDAO();
		staticPageViewDao.create(staticPageView);
		if(callBack!=null){
			callBack.AfterSqlFileModifed();
		}
	}
	/*****
	 * 修改当前记录的 结束时间 和 停留时间
	 * @param dbFileName
	 * @param preViewCode
	 * @param currentViewCode
	 * @throws SQLException 
	 */
	public synchronized void UpdatePageView(Context context,String dbFileName,String currentViewCode,SqlFileModifedCallBack callBack) throws SQLException{
		if(dbFileName==null||context==null){
			return;
		}
		StaticDatabaseHelper	dbOpenHelper=StaticDatabaseHelper.getInstance(context,dbFileName);
		Dao<StaticPageViewRecordVO, String> staticPageViewDao=	dbOpenHelper.getStaticViewPageDAO();
		QueryBuilder<StaticPageViewRecordVO,String> queryBuilder=staticPageViewDao.queryBuilder();
		queryBuilder.where().eq(StaticPageViewRecordVO.Column_CurrentViewCode,currentViewCode)
		.and().isNull(StaticPageViewRecordVO.Column_EndTime)
		.and().isNull(StaticPageViewRecordVO.Column_StateTime);
		PreparedQuery<StaticPageViewRecordVO> preparedQuery=queryBuilder.prepare();
		List<StaticPageViewRecordVO> queryList=staticPageViewDao.query(preparedQuery);
		if(!queryList.isEmpty()&&queryList.size()==1){
			StaticPageViewRecordVO staticPageView=queryList.get(0);
			UpdateBuilder<StaticPageViewRecordVO,String> updateBuilder=staticPageViewDao.updateBuilder();
			updateBuilder.where().eq(StaticPageViewRecordVO.Column_ID,staticPageView.getId());
			long currentTime=System.currentTimeMillis();
			String endTime=String.valueOf(currentTime);
			long startTime=Long.valueOf(staticPageView.getStartTime());
			String stateTime=String.valueOf(currentTime-startTime);
			updateBuilder.updateColumnValue(StaticPageViewRecordVO.Column_EndTime, endTime);
			updateBuilder.updateColumnValue(StaticPageViewRecordVO.Column_StateTime,stateTime);
			// prepare our sql statement
			PreparedUpdate<StaticPageViewRecordVO> preparedUpdate = updateBuilder.prepare();
			staticPageViewDao.update(preparedUpdate);
			if(callBack!=null){
				callBack.AfterSqlFileModifed();
			}
		}
	}
	/***
	 * 返回全部的页面访问记录
	 * @return
	 * @throws SQLException 
	 */
	public synchronized List<StaticPageViewRecordVO> getAllPageViewRecordList(Context context,String dbFileName) throws SQLException{
		
		List<StaticPageViewRecordVO> pageViewList=new ArrayList<StaticPageViewRecordVO>();
		StaticDatabaseHelper	dbOpenHelper=StaticDatabaseHelper.getInstance(context,dbFileName);
		Dao<StaticPageViewRecordVO, String> staticPageViewDao = null;
		staticPageViewDao = dbOpenHelper.getStaticViewPageDAO();
		pageViewList=	staticPageViewDao.queryForAll();
		return pageViewList;
	}
	/**
	 * 从数据库中获取所有需要上传的staticpageVIew
	 * 
	 * 合并preViewCode  currentViewCode 相同的记录，count计数 并计算平均访问时间
	 * @param dbFileName
	 * @return
	 */
	public synchronized List<StaticPageViewRecordVO> getUpLoadPageViewList(Context context,String dbFileName){
		List<StaticPageViewRecordVO> pageViewList=new ArrayList<StaticPageViewRecordVO>();
		StaticDatabaseHelper	dbOpenHelper=StaticDatabaseHelper.getInstance(context,dbFileName);
		SQLiteDatabase database=dbOpenHelper.getReadableDatabase();
		Cursor cursor=database.rawQuery("SELECT count(id),currentViewCode,avg(stateTime) FROM staticPageView " +
				"GROUP BY currentViewCode HAVING count(currentViewCode)>0;",null);
		while(cursor.moveToNext()){
			StaticPageViewRecordVO staticPageView=new  StaticPageViewRecordVO();
			staticPageView.setCurrentViewCode(cursor.getString(1));
			staticPageView.setCount(cursor.getInt(0));
			staticPageView.setAverageTime(cursor.getString(2));
			pageViewList.add(staticPageView);
		}
		cursor.close();
		cursor=null;
		return pageViewList;
	}
	/****
	 * 返回页面访问跳转权重记录
	 * @return
	 * @throws SQLException 
	 */
	public  synchronized List<StaticPageViewSkipVO> getUpLoadPageViewSkipList(Context context,String dbFileName) throws SQLException{
		List<StaticPageViewRecordVO> staticPageViewRecordList=getAllPageViewRecordList(context, dbFileName);
		List<StaticPageViewSkipVO> pageViewSkipUpLoadList=new ArrayList<StaticPageViewSkipVO>();
		if(staticPageViewRecordList==null||staticPageViewRecordList.isEmpty()){
			return pageViewSkipUpLoadList;
		}
		StaticDatabaseHelper	dbOpenHelper=StaticDatabaseHelper.getInstance(context,dbFileName);
		SQLiteDatabase database=dbOpenHelper.getReadableDatabase();
		HashSet<StaticPageViewRecordVO> staticPageViewRecordSet=new HashSet<StaticPageViewRecordVO>(staticPageViewRecordList);
		for(StaticPageViewRecordVO staticPageViewRecord:staticPageViewRecordSet){
			String currentViewCode=staticPageViewRecord.getCurrentViewCode();
			if(!TextUtils.isEmpty(currentViewCode)){
				List<StaticPageViewSkipVO> pageViewSkipList=new ArrayList<StaticPageViewSkipVO>();
				//先判断当前节点是否为表中的最后节点
				Cursor cursor=database.rawQuery("select currentViewCode from  staticPageView  ORDER BY id desc limit 1;",null);
				while(cursor.moveToNext()){///如果该节点=currentViewCode是表中的最终节点 
					if((cursor.getString(0)).equals(currentViewCode)){
						StaticPageViewSkipVO staticPageViewSkip=new StaticPageViewSkipVO();
						staticPageViewSkip.setCurrentViewCode(currentViewCode);
						staticPageViewSkip.setNextViewCode("退出应用");
						pageViewSkipList.add(staticPageViewSkip);
					}
				}
				//获取所有当前页面跳转的的下一个节点名称 
				cursor=database.rawQuery("select sp.currentViewCode from staticPageView sp" +
						",staticPageView sp2 where sp.id=sp2.id+1 and  sp2.currentViewCode like ? ",new String[]{currentViewCode});
				while(cursor.moveToNext()){
					StaticPageViewSkipVO staticPageViewSkip=new StaticPageViewSkipVO();
					staticPageViewSkip.setCurrentViewCode(currentViewCode);
					String nextViewCode=cursor.getString(0);
					staticPageViewSkip.setNextViewCode(nextViewCode.equals(currentViewCode)?"其他":nextViewCode);
					pageViewSkipList.add(staticPageViewSkip);
				}	
				cursor.close();
				cursor=null;
				//////////计算weight
				pageViewSkipList=calculatePageViewSkipWeight(pageViewSkipList);
				pageViewSkipUpLoadList.addAll(pageViewSkipList);
			}
		}
		return pageViewSkipUpLoadList;
		
	}
	/**
	 * 计算weight
	 * @param pageViewSkipList
	 * @return
	 */
	public static List<StaticPageViewSkipVO> calculatePageViewSkipWeight(List<StaticPageViewSkipVO> pageViewSkipList){
		if(pageViewSkipList!=null&&!pageViewSkipList.isEmpty()){
			HashSet<StaticPageViewSkipVO> hashSet=new HashSet<StaticPageViewSkipVO>(pageViewSkipList);
			for(StaticPageViewSkipVO pageViewSkip:pageViewSkipList){
				if(hashSet.contains(pageViewSkip)){
					pageViewSkip=getStaticPageViewSkipVOFromHashSet(hashSet, pageViewSkip);
					hashSet.remove(pageViewSkip);
					pageViewSkip.setCount(pageViewSkip.getCount()+1);
					hashSet.add(pageViewSkip);
				}
			}
			List<StaticPageViewSkipVO> weightStaticPageList=new ArrayList<StaticPageViewSkipVO>(hashSet);
			float count=(float)pageViewSkipList.size();
			DecimalFormat decimalFormat=	new DecimalFormat("##0.0");
			for(StaticPageViewSkipVO pageViewSkip:weightStaticPageList){
				pageViewSkip.setWeight(String.valueOf(decimalFormat.format((float)(pageViewSkip.getCount())/count*100)+"%"));
			}
			return weightStaticPageList;
		}
		
		return pageViewSkipList;
	}
	private static StaticPageViewSkipVO getStaticPageViewSkipVOFromHashSet(HashSet<StaticPageViewSkipVO> hashSet,StaticPageViewSkipVO searchPageViewSkip){
		if(hashSet!=null&&searchPageViewSkip!=null){
			for(StaticPageViewSkipVO staticPage:hashSet){
				if(searchPageViewSkip.equals(staticPage)){
					return staticPage;
				}
			}
		}
		return searchPageViewSkip;
	}
	/**
	 * 更新全部不完整的页面访问记录
	 * @param dbFileName
	 * @throws SQLException 
	 */
	public synchronized void UpdateAllPageViewRecord(Context context,String dbFileName) throws SQLException{
		StaticDatabaseHelper	dbOpenHelper=StaticDatabaseHelper.getInstance(context,dbFileName);
		Dao<StaticPageViewRecordVO, String> staticPageViewDao=	dbOpenHelper.getStaticViewPageDAO();
		QueryBuilder<StaticPageViewRecordVO,String> queryBuilder=staticPageViewDao.queryBuilder();
		queryBuilder.where().isNull(StaticPageViewRecordVO.Column_EndTime)
		.and().isNull(StaticPageViewRecordVO.Column_StateTime);
		PreparedQuery<StaticPageViewRecordVO> preparedQuery=queryBuilder.prepare();
		List<StaticPageViewRecordVO> queryList=staticPageViewDao.query(preparedQuery);
		for(StaticPageViewRecordVO staticPageView:queryList){
			UpdateBuilder<StaticPageViewRecordVO,String> updateBuilder=staticPageViewDao.updateBuilder();
			updateBuilder.where().eq(StaticPageViewRecordVO.Column_ID,staticPageView.getId());
			long currentTime=System.currentTimeMillis();
			String endTime=String.valueOf(currentTime);
			long startTime=Long.valueOf(staticPageView.getStartTime());
			String stateTime=String.valueOf(currentTime-startTime);
			updateBuilder.updateColumnValue(StaticPageViewRecordVO.Column_EndTime, endTime);
			updateBuilder.updateColumnValue(StaticPageViewRecordVO.Column_StateTime,stateTime);
			// prepare our sql statement
			PreparedUpdate<StaticPageViewRecordVO> preparedUpdate = updateBuilder.prepare();
			staticPageViewDao.update(preparedUpdate);
		}
	}
}

	