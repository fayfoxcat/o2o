package org.fox.o2o.dao;

import java.util.List;

import org.fox.o2o.entity.Area;

public interface AreaDao {
	//列出区域列表 返回areaList
	List <Area> queryArea();
}
