package com.zizibujuan.drip.server.dao.mysql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zizibujuan.drip.server.dao.ExerciseDao;
import com.zizibujuan.drip.server.util.ExerciseType;
import com.zizibujuan.drip.server.util.dao.AbstractDao;
import com.zizibujuan.drip.server.util.dao.DatabaseUtil;

/**
 * 维护习题 数据访问实现类
 * @author jinzw
 * @since 0.0.1
 */
public class ExerciseDaoImpl extends AbstractDao implements ExerciseDao {

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
	public int add(Map<String, Object> exerciseInfo) {
		int exerId = -1;
		Connection con = null;
		try{
			con = getDataSource().getConnection();
			con.setAutoCommit(false);
			// 添加习题
			Object oUserId = exerciseInfo.get("userId");
			Object oExerType = exerciseInfo.get("exerType");
			
			exerId = this.addExercise(con, exerciseInfo);
			// 如果存在选项，则添加习题选项
			Object options = exerciseInfo.get("options");
			List<Integer> optionIds = null;
			if(options != null){
				ArrayList<String> optionContents = (ArrayList<String>)options;
				if(optionContents.size()>0){
					optionIds = this.addOptions(con, exerId, optionContents);
				}
			}
			// 如果存在答案，则添加答案
			Object oAnswers = exerciseInfo.get("answers");
			if(oAnswers != null){
				if(ExerciseType.SINGLE_OPTION.equals(oExerType)){
					ArrayList<String> answers = (ArrayList<String>)oAnswers;
					List<Integer> optIds = null;
					if(optionIds != null){
						optIds = new ArrayList<Integer>();
						for(String i : answers){
							optIds.add(optionIds.get(Integer.valueOf(i)));
						}
					}
					this.addAnswer(con, exerId, oUserId, optIds, answers);
				}
			}
			
			// 如果存在习题解析，则添加习题解析
			Object oGuide = exerciseInfo.get("guide");
			if(oGuide != null){
				this.addGuide(con, exerId, oUserId, oGuide);
			}
			con.commit();
		}catch(Exception e){
			DatabaseUtil.safeRollback(con);
		}finally{
			DatabaseUtil.closeConnection(con);
		}
		return exerId;
	}
	
	private static final String SQL_INSERT_EXERCISE = 
			"INSERT INTO DRIP_EXERCISE (CONTENT,EXER_TYPE, EXER_CATEGORY, CRT_TM, CRT_USER_ID) VALUES (?,?,?,now(),?)";
	// 1. 新增习题
	private int addExercise(Connection con, Map<String,Object> exerciseInfo) throws SQLException{
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
	private List<Integer> addOptions(Connection con, int exerId, List<String> optionContents) throws SQLException {
		List<Integer> result = new ArrayList<Integer>();
		int len = optionContents.size();
		for(int i = 0; i < len; i++){
			int id = DatabaseUtil.insert(con, SQL_INSERT_EXER_OPTION, exerId, optionContents.get(i),(i+1));
			result.add(id);
		}
		return result;
	}
	
	private static final String SQL_INSERT_EXER_GUIDE = "INSERT INTO DRIP_EXER_GUIDE " +
			"(EXER_ID, CONTENT,CRT_TM, CRT_USER_ID) VALUES (?,?,now(),?)";
	private void addGuide(Connection con, int exerId, Object oUserId, Object oGuide) throws SQLException {
		DatabaseUtil.insert(con, SQL_INSERT_EXER_GUIDE, exerId, oGuide, oUserId);
	}
	
	private static final String SQL_INSERT_EXER_ANSWER = "INSERT INTO DRIP_ANSWER " +
			"(EXER_ID,CRT_TM,CRT_USER_ID) VALUES " +
			"(?,now(),?)";
	private static final String SQL_INSERT_EXER_ANSWER_DETAIL = "INSERT INTO DRIP_ANSWER_DETAIL " +
			"(ANSWER_ID,OPT_ID,CONTENT) VALUES " +
			"(?,?,?)";
	private void addAnswer(Connection con, int exerId, Object oUserId, List<Integer> optIds, List<String> answers) throws SQLException{
		int answerId = DatabaseUtil.insert(con, SQL_INSERT_EXER_ANSWER, exerId, oUserId);
		
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
	}

	
	// 2. 回答习题
	// 3. 新增习题的同时，回答习题，放在一个事务中。

}
