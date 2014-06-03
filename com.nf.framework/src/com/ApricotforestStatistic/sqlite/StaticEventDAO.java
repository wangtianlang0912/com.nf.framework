package com.ApricotforestStatistic.sqlite;


import java.sql.SQLException;
import java.util.List;

import android.content.Context;

import com.ApricotforestStatistic.Service.StatisticBaseDataService;
import com.ApricotforestStatistic.VO.StaticEventVO;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.PreparedUpdate;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;


public class StaticEventDAO {

	public StaticEventDAO(){}
	private static class StaticEventDAOHolder {  
		private static final StaticEventDAO INSTANCE = new StaticEventDAO();  
	}  
	public static final StaticEventDAO getInstance() {  
	   return StaticEventDAOHolder.INSTANCE;  
	}  
	/**
	 * 插入或更新事件统计计数
	 * 根据eventName userId,相关id，网络类型判断是否插入或者更新
	 * 更新则计数加一
	 * @param eventName
	 * @param relatedParam
	 */
	public synchronized void  insertOrUpdateEvent(Context context,String dbFileName,String eventName,String userId,String relatedParam,SqlFileModifedCallBack callBack){
		if(dbFileName==null||context==null){
			return;
		}
		StaticDatabaseHelper	dbOpenHelper=StaticDatabaseHelper.getInstance(context,dbFileName);
		try {
			StaticEventVO staticEvent=new StaticEventVO();
			staticEvent.setEventName(eventName!=null?eventName:"");
			staticEvent.setEventRelatedId(relatedParam!=null?relatedParam:"");
			String netType=StatisticBaseDataService.getCurrentNetState(context);
			staticEvent.setNetType(netType!=null?netType:"");
			staticEvent.setUserId(userId!=null?userId:"");
			staticEvent.setNetWorking(StatisticBaseDataService.getSimOperator(context));
			Dao<StaticEventVO, String> staticEventDao=	dbOpenHelper.getStaticEventDAO();
			
			List<StaticEventVO> list=isExistEventVO(staticEventDao, staticEvent);
			if(list==null||list.isEmpty()){
				staticEvent.setCount(1);
				staticEventDao.create(staticEvent);
			}else{
				StaticEventVO rawEventVO=list.get(0);
				Update(staticEventDao, rawEventVO);
			}
			if(callBack!=null){
				callBack.AfterSqlFileModifed();
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
		}
	}
	
	private List<StaticEventVO> isExistEventVO(Dao<StaticEventVO, String> staticEventDao,StaticEventVO staticEvent){
		
		QueryBuilder<StaticEventVO,String> queryBuilder=staticEventDao.queryBuilder();
		try {
			queryBuilder.where().eq(StaticEventVO.Colum_EventName, staticEvent.getEventName()!=null?staticEvent.getEventName():"")
			.and().eq(StaticEventVO.Colum_EventRelatedId,staticEvent.getEventRelatedId())
			.and().eq(StaticEventVO.Colum_NetType,staticEvent.getNetType())
			.and().eq(StaticEventVO.Colum_UserId,staticEvent.getUserId());
		
			// prepare our sql statement
			PreparedQuery<StaticEventVO> preparedQuery = queryBuilder.prepare();
			List<StaticEventVO> list=	staticEventDao.query(preparedQuery);	
			return list;
		} catch (SQLException e) {
		}
		return null;
	}
	private void Update(Dao<StaticEventVO, String> staticEventDao,StaticEventVO staticEvent){
		UpdateBuilder<StaticEventVO,String> updateBuilder=staticEventDao.updateBuilder();
		try {
			updateBuilder.where().eq(StaticEventVO.Colum_EventName, staticEvent.getEventName()!=null?staticEvent.getEventName():"")
			.and().eq(StaticEventVO.Colum_EventRelatedId,staticEvent.getEventRelatedId())
			.and().eq(StaticEventVO.Colum_NetType,staticEvent.getNetType())
			.and().eq(StaticEventVO.Colum_UserId,staticEvent.getUserId());
			updateBuilder.updateColumnValue(StaticEventVO.Colum_Count, staticEvent.getCount()+1);
			// prepare our sql statement
			PreparedUpdate<StaticEventVO> preparedUpdate = updateBuilder.prepare();
			staticEventDao.update(preparedUpdate);	
		} catch (SQLException e) {
		}
	}
	/***
	 * 获取全部对象列表
	 * @return
	 */
	public List<StaticEventVO> getStaticEventListFromSql(Context context,String dbFileName){
		StaticDatabaseHelper	dbOpenHelper=StaticDatabaseHelper.getInstance(context,dbFileName);
		try {
			Dao<StaticEventVO, String> staticEventDao=	dbOpenHelper.getStaticEventDAO();
			List<StaticEventVO> list=staticEventDao.queryForAll();
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
		}
		return null;
	}
}

	