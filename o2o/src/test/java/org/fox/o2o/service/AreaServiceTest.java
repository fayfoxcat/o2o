package org.fox.o2o.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.fox.o2o.BaseTest;
import org.fox.o2o.entity.Area;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AreaServiceTest extends BaseTest{
	@Autowired
	private AreaService areaService;
	
	@Test
	public void testGetAreaList() {
		List<Area> arealist = areaService.getAreaList();
		assertEquals("蚌埠学院",arealist.get(1).getAreaName());
	}
}
