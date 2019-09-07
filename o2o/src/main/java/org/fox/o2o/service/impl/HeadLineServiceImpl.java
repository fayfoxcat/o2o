package org.fox.o2o.service.impl;

import java.io.IOException;
import java.util.List;

import org.fox.o2o.dao.HeadLineDao;
import org.fox.o2o.entity.HeadLine;
import org.fox.o2o.service.HeadLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HeadLineServiceImpl implements HeadLineService {
	@Autowired
	private HeadLineDao headLineDao;
	
	@Override
	public List<HeadLine> getHeadLineList(HeadLine headLineCondition) throws IOException {
		return headLineDao.queryHeadLine(headLineCondition);
	}

}
