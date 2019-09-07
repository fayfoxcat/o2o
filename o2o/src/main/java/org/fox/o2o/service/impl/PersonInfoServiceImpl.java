package org.fox.o2o.service.impl;

import java.util.List;

import org.fox.o2o.dao.PersonInfoDao;
import org.fox.o2o.dto.PersonInfoExecution;
import org.fox.o2o.entity.PersonInfo;
import org.fox.o2o.enums.PersonInfoStateEnum;
import org.fox.o2o.service.PersonInfoService;
import org.fox.o2o.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonInfoServiceImpl implements PersonInfoService {
	@Autowired
	private PersonInfoDao personInfoDao;
	
	/**
	 * 根据userId返回对应用户信息
	 */
	@Override
	public PersonInfo getPersonInfoById(Long userId) {
		return personInfoDao.queryPersonInfoById(userId);
	}
	
	/**
	 * 分页返回用户信息列表
	 */
	@Override
	public PersonInfoExecution getPersonInfoList(PersonInfo personInfoCondition, int pageIndex, int pageSize) {
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<PersonInfo> personInfoList = personInfoDao.queryPersonInfoList(personInfoCondition, rowIndex, pageSize);
		int count = personInfoDao.queryPersonInfoCount(personInfoCondition);
		PersonInfoExecution se = new PersonInfoExecution();
		if (personInfoList != null) {
			se.setPersonInfoList(personInfoList);
			se.setCount(count);
		} else {
			se.setState(PersonInfoStateEnum.INNER_ERROR.getState());
		}
		return se;
	}
	
	/**
	 * 新增用户
	 */
	@Override
	@Transactional
	public PersonInfoExecution addPersonInfo(PersonInfo personInfo) {
		if (personInfo == null) {
			return new PersonInfoExecution(PersonInfoStateEnum.EMPTY);
		} else {
			try {
				int effectedNum = personInfoDao.insertPersonInfo(personInfo);
				if (effectedNum <= 0) {
					return new PersonInfoExecution(PersonInfoStateEnum.INNER_ERROR);
				} else {// 创建成功
					personInfo = personInfoDao.queryPersonInfoById(personInfo.getUserId());
					return new PersonInfoExecution(PersonInfoStateEnum.SUCCESS,personInfo);
				}
			} catch (Exception e) {
				throw new RuntimeException("新增用户信息出错: "+ e.getMessage());
			}
		}
	}
	
	/**
	 * 修改用户个人信息
	 */
	@Override
	@Transactional
	public PersonInfoExecution modifyPersonInfo(PersonInfo personInfo) {
		if (personInfo == null || personInfo.getUserId() == null) {
			return new PersonInfoExecution(PersonInfoStateEnum.EMPTY);
		} else {
			try {
				int effectedNum = personInfoDao.updatePersonInfo(personInfo);
				if (effectedNum <= 0) {
					return new PersonInfoExecution(PersonInfoStateEnum.INNER_ERROR);
				} else {// 创建成功
					personInfo = personInfoDao.queryPersonInfoById(personInfo.getUserId());
					return new PersonInfoExecution(PersonInfoStateEnum.SUCCESS,personInfo);
				}
			} catch (Exception e) {
				throw new RuntimeException("更新个人信息出错: "+ e.getMessage());
			}
		}
	}

}
