package org.fox.o2o.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.fox.o2o.BaseTest;
import org.fox.o2o.entity.Area;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AreaDaoTest extends BaseTest{
	@Autowired
	private AreaDao areaDao;
	
	@Test
	public void testQueryArea() {
		List<Area> areaList = areaDao.queryArea();
		assertEquals(2,areaList.size());
	}
}
