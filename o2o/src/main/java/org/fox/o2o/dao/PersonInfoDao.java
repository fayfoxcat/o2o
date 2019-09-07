package org.fox.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.fox.o2o.entity.PersonInfo;

public interface PersonInfoDao {

	/**
	 * 返回查询列表
	 * @param personInfoCondition
	 * @param rowIndex
	 * @param pageSize
	 * @return
	 */
	List<PersonInfo> queryPersonInfoList(@Param("personInfoCondition") PersonInfo personInfoCondition,@Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

	/**
	 * 查询用户总数
	 * @param personInfoCondition
	 * @return
	 */
	int queryPersonInfoCount(@Param("personInfoCondition") PersonInfo personInfoCondition);

	/**
	 * 根据Id查询返回用户信息
	 * @param userId
	 * @return
	 */
	PersonInfo queryPersonInfoById(long userId);

	/**
	 * 创建用户
	 * @param 
	 * @return
	 */
	int insertPersonInfo(PersonInfo personInfo);

	/**
	 * 更新用户信息
	 * @param 
	 * @return
	 */
	int updatePersonInfo(PersonInfo personInfo);

	/**
	 * 删除用户
	 * @param 
	 * @return
	 */
	int deletePersonInfo(long userId);
}
