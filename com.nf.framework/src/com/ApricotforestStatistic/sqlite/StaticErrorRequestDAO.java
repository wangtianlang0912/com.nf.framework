package com.ApricotforestStatistic.sqlite;


import java.sql.SQLException;
import java.util.List;

import android.content.Context;

import com.ApricotforestStatistic.Service.StatisticBaseDataService;
import com.ApricotforestStatistic.VO.StaticErrorRequestVO;
import com.j256.ormlite.dao.Dao;


public class StaticErrorRequestDAO {

	public StaticErrorRequestDAO(){}
	private static class StaticErrorRequestDAOHolder {  
		private static final StaticErrorRequestDAO INSTANCE = new StaticErrorRequestDAO();  
	}  
	public static final StaticErrorRequestDAO getInstance() {  
	   return StaticErrorRequestDAOHolder.INSTANCE;  
	}  
	/**
	 * 插入或更新事件统计计数
	 * 根据requestUrl errorReponseCode,errorReponseCode 插入
	 * @param eventName
	 * @param relatedParam
	 */
	public synchronized void  insertErrorRequest(Context context,String dbFileName,String requestUrl,String errorReponseCode,String occurTime,boolean isRequestByPost,SqlFileModifedCallBack callBack){
		if(dbFileName==null||context==null){
			return;
		}
		StaticDatabaseHelper	dbOpenHelper=StaticDatabaseHelper.getInstance(context,dbFileName);
		try {
			StaticErrorRequestVO staticErrorRequest=new StaticErrorRequestVO();
			staticErrorRequest.setRequestUrl(requestUrl);
			staticErrorRequest.setErrorReponseCode(errorReponseCode);
			staticErrorRequest.setOccurTime(occurTime);
			staticErrorRequest.setRequestType(isRequestByPost?"post":"get");
			String netType=StatisticBaseDataService.getCurrentNetState(context);
			staticErrorRequest.setNetType(netType!=null?netType:"");
			staticErrorRequest.setNetWorking(StatisticBaseDataService.getSimOperator(context));
			Dao<StaticErrorRequestVO, String> staticErrorRequestDao=	dbOpenHelper.getStaticErrorRequestDAO();
			
			staticErrorRequestDao.create(staticErrorRequest);
			if(callBack!=null){
				callBack.AfterSqlFileModifed();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
		}
	}
	/***
	 * 获取全部对象列表
	 * @return
	 */
	public List<StaticErrorRequestVO> getStaticErrorRequestListFromSql(Context context,String dbFileName){
		StaticDatabaseHelper	dbOpenHelper=StaticDatabaseHelper.getInstance(context,dbFileName);
		try {
			Dao<StaticErrorRequestVO, String> staticErrorRequestDao=	dbOpenHelper.getStaticErrorRequestDAO();
			List<StaticErrorRequestVO> list=staticErrorRequestDao.queryForAll();
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
		}
		return null;
	}
	
}

	