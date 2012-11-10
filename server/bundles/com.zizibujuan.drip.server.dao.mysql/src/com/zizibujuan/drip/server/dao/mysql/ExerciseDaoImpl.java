package com.zizibujuan.drip.server.dao.mysql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zizibujuan.drip.server.dao.ActivityDao;
import com.zizibujuan.drip.server.dao.ExerciseDao;
import com.zizibujuan.drip.server.dao.UserDao;
import com.zizibujuan.drip.server.exception.dao.DataAccessException;
import com.zizibujuan.drip.server.util.ActionType;
import com.zizibujuan.drip.server.util.ExerciseType;
import com.zizibujuan.drip.server.util.dao.AbstractDao;
import com.zizibujuan.drip.server.util.dao.DatabaseUtil;

/**
 * 维护习题 数据访问实现类
 * @author jinzw
 * @since 0.0.1
 */
public class ExerciseDaoImpl extends AbstractDao implements ExerciseDao {
	private static final Logger logger = LoggerFactory.getLogger(ExerciseDaoImpl.class);
	private UserDao userDao;
	private ActivityDao activityDao;
	
	private static final String SQL_LIST_EXERCISE = 
			"SELECT DBID, CONTENT, CRT_TM, CRT_USER_ID FROM DRIP_EXERCISE ORDER BY CRT_TM DESC";
	@Override
	public List<Map<String, Object>> get() {
		return DatabaseUtil.queryForList(getDataSource(), SQL_LIST_EXERCISE);
	}
	
	/**
	 * 新增习题。<br/>
	 * <pre>
	 * 习题的数据格式为：
	 * 		exerType: 题型
	 * 		exerCategory: 习题所属科目中的分类
	 * 		content： 习题内容
	 * 		options：Array  题目选项
	 * 		answers: Array  习题答案列表
	 * 		guide: 习题解析
	 * </pre>
	 * @param exerciseInfo 习题信息
	 * @return 新增习题的标识
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Long add(Map<String, Object> exerciseInfo) {
		Long exerId = -1l;
		Connection con = null;
		try{
			con = getDataSource().getConnection();
			con.setAutoCommit(false);
			// 添加习题
			Object oUserId = exerciseInfo.get("userId");
			Object oExerType = exerciseInfo.get("exerType");
			
			Long userId = Long.valueOf(oUserId.toString());
			
			exerId = this.addExercise(con, exerciseInfo);
			// 如果存在选项，则添加习题选项
			Object options = exerciseInfo.get("options");
			List<Long> optionIds = null;
			if(options != null){
				ArrayList<String> optionContents = (ArrayList<String>)options;
				if(optionContents.size()>0){
					optionIds = this.addOptions(con, exerId, optionContents);
				}
			}
			
			// 习题添加成功后，在用户的“创建的习题数”上加1
			// 同时修改后端和session中缓存的该记录
			userDao.increaseExerciseCount(con, userId);
			// 在活动表中插入一条记录
			addActivity(con, userId,exerId,ActionType.SAVE_EXERCISE);
			
			// 如果存在答案，则添加答案
			Object oAnswers = exerciseInfo.get("answers");
			if(oAnswers != null){
				Long answerId = null;
				if(ExerciseType.SINGLE_OPTION.equals(oExerType)){
					ArrayList<String> answers = (ArrayList<String>)oAnswers;
					List<Long> optIds = null;
					if(optionIds != null){
						optIds = new ArrayList<Long>();
						for(String i : answers){
							optIds.add(optionIds.get(Integer.valueOf(i)));
						}
					}
					answerId = this.addAnswer(con, exerId, oUserId, optIds, answers);
				}
				
				// 答案回答完成后，在用户的“已回答的习题数”上加1
				// 同时修改后端和session中缓存的该记录
				userDao.increaseAnswerCount(con, userId);
				// 在活动表中插入一条记录
				addActivity(con, userId,answerId,ActionType.ANSWER_EXERCISE);
			}
			
			// 如果存在习题解析，则添加习题解析
			Object oGuide = exerciseInfo.get("guide");
			if(oGuide != null){
				this.addGuide(con, exerId, oUserId, oGuide);
			}
			
			con.commit();
		}catch(SQLException e){
			DatabaseUtil.safeRollback(con);
			throw new DataAccessException(e);
		}catch(DataAccessException e){
			DatabaseUtil.safeRollback(con);
			throw e;
		}finally{
			DatabaseUtil.closeConnection(con);
		}
		return exerId;
	}

	private void addActivity(Connection con,Long userId, Long contentId, String actionType) {
		// FIXME:是不是直接传各自的参数更好一些，而不是现在传入map对象，还需要两遍转换
		Map<String,Object> activityInfo = new HashMap<String, Object>();
		activityInfo.put("USER_ID", userId);
		activityInfo.put("ACTION_TYPE", actionType);
		activityInfo.put("IS_IN_HOME", 1);
		activityInfo.put("CONTENT_ID", contentId);
		activityDao.add(con, activityInfo);
	}
	
	private static final String SQL_INSERT_EXERCISE = 
			"INSERT INTO DRIP_EXERCISE (CONTENT,EXER_TYPE, EXER_CATEGORY, CRT_TM, CRT_USER_ID) VALUES (?,?,?,now(),?)";
	// 1. 新增习题
	private Long addExercise(Connection con, Map<String,Object> exerciseInfo){
		Object oContent = exerciseInfo.get("content");
		Object oExerType = exerciseInfo.get("exerType");
		Object oExerCategory = exerciseInfo.get("exerCategory");
		Object oUserId = exerciseInfo.get("userId");
		return DatabaseUtil.insert(con,SQL_INSERT_EXERCISE, oContent, oExerType, oExerCategory, oUserId);
	}
	
	private static final String SQL_INSERT_EXER_OPTION = "INSERT INTO DRIP_EXER_OPTION " +
			"(EXER_ID,CONTENT,OPT_SEQ) VALUES " +
			"(?,?,?)";
	// 2. 添加选项
	private List<Long> addOptions(Connection con, Long exerId, List<String> optionContents){
		List<Long> result = new ArrayList<Long>();
		int len = optionContents.size();
		for(int i = 0; i < len; i++){
			Long id = DatabaseUtil.insert(con, SQL_INSERT_EXER_OPTION, exerId, optionContents.get(i),(i+1));
			result.add(id);
		}
		return result;
	}
	
	private static final String SQL_INSERT_EXER_GUIDE = "INSERT INTO DRIP_EXER_GUIDE " +
			"(EXER_ID, CONTENT,CRT_TM, CRT_USER_ID) VALUES (?,?,now(),?)";
	private void addGuide(Connection con, Long exerId, Object oUserId, Object oGuide){
		DatabaseUtil.insert(con, SQL_INSERT_EXER_GUIDE, exerId, oGuide, oUserId);
	}
	
	private static final String SQL_INSERT_EXER_ANSWER = "INSERT INTO DRIP_ANSWER " +
			"(EXER_ID,CRT_TM,CRT_USER_ID) VALUES " +
			"(?,now(),?)";
	private static final String SQL_INSERT_EXER_ANSWER_DETAIL = "INSERT INTO DRIP_ANSWER_DETAIL " +
			"(ANSWER_ID,OPT_ID,CONTENT) VALUES " +
			"(?,?,?)";
	// TODO：移到AnswerDao中？
	private Long addAnswer(Connection con, Long exerId, Object oUserId, List<Long> optIds, List<String> answers){
		Long answerId = DatabaseUtil.insert(con, SQL_INSERT_EXER_ANSWER, exerId, oUserId);
		
		if(optIds == null){
			for(String content : answers){
				DatabaseUtil.insert(con, SQL_INSERT_EXER_ANSWER_DETAIL, answerId, null,content);
			}
		}else{
			int i = 0;
			for(String content : answers){
				DatabaseUtil.insert(con, SQL_INSERT_EXER_ANSWER_DETAIL, answerId, optIds.get(i),content);
				i++;
			}
		}
		return answerId;
	}

	// TODO:不在sql中联合查询编码，而是从缓存中获取编码对应的文本信息
	private static final String SQL_GET_EXERCISE = "SELECT " +
			"DBID \"id\"," +
			"CONTENT \"content\"," +
			"EXER_TYPE \"exerType\"," +
			"EXER_CATEGORY \"exerCategory\"," +
			"CRT_TM \"createTime\"," +
			"CRT_USER_ID \"createUserId\"," +
			"UPT_TM \"updateTime\"," +
			"UPT_USER_ID \"updateUserId\" " +
			"FROM " +
			"DRIP_EXERCISE WHERE DBID=?";
	@Override
	public Map<String, Object> get(Long exerciseId) {
		Map<String,Object> exercise = DatabaseUtil.queryForMap(getDataSource(), SQL_GET_EXERCISE, exerciseId);
		if(!exercise.isEmpty()){
			String exerType = exercise.get("exerType").toString();
			if (ExerciseType.SINGLE_OPTION.equals(exerType)
					|| ExerciseType.MULTI_OPTION.equals(exerType)
					|| ExerciseType.FILL.equals(exerType)) {
				List<Map<String,Object>> options = this.getExerciseOptions(exerciseId);
				exercise.put("options", options);
			}
		}
		return exercise;
	}

	private final static String SQL_LIST_EXERCISE_OPTION = "SELECT " +
			"DBID \"id\"," +
			"EXER_ID \"exerId\"," +
			"CONTENT \"content\"," +
			"OPT_SEQ \"seq\" " +
			"FROM DRIP_EXER_OPTION WHERE EXER_ID=? " +
			"ORDER BY OPT_SEQ";
	private List<Map<String,Object>> getExerciseOptions(Long exerciseId){
		return DatabaseUtil.queryForList(getDataSource(), SQL_LIST_EXERCISE_OPTION, exerciseId);
	}
	
	// 2. 回答习题
	// 3. 新增习题的同时，回答习题，放在一个事务中。
	
	
	public void setUserDao(UserDao userDao) {
		logger.info("注入userDao");
		this.userDao = userDao;
	}

	public void unsetUserDao(UserDao userDao) {
		if (this.userDao == userDao) {
			logger.info("注销userDao");
			this.userDao = null;
		}
	}
	
	public void setActivityDao(ActivityDao activityDao) {
		logger.info("注入ActivityDao");
		this.activityDao = activityDao;
	}

	public void unsetActivityDao(ActivityDao activityDao) {
		if (this.activityDao == activityDao) {
			logger.info("注销ActivityDao");
			this.activityDao = null;
		}
	}
}
