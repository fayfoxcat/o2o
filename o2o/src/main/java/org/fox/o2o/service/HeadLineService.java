package org.fox.o2o.service;

import java.io.IOException;
import java.util.List;

import org.fox.o2o.entity.HeadLine;

public interface HeadLineService {
	
	/**
	 * 根据传入的条件返回指定的头条列表
	 * @param headLineCondition
	 * @return
	 * @throws IOException
	 */
	public List<HeadLine> getHeadLineList(HeadLine headLineCondition) throws IOException;
}
