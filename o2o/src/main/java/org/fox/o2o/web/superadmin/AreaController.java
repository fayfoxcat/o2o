package org.fox.o2o.web.superadmin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fox.o2o.entity.Area;
import org.fox.o2o.service.AreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/superadmin")
public class AreaController { 
	Logger logger = LoggerFactory.getLogger(AreaController.class);
	@Autowired
	private AreaService areaService;
	@RequestMapping(value = "/listarea",method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> listArea(){
		logger.info("=====strat=====");
		long startTime = System.currentTimeMillis();
		Map<String,Object> modelMap = new HashMap<String,Object>();
		List<Area> list = new ArrayList<Area>();
	try {
		list = areaService.getAreaList();
		modelMap.put("rows", list);
		modelMap.put("total", list.size());
	}catch(Exception e) {
		e.printStackTrace();
		modelMap.put("sucess", false);
		modelMap.put("errMsg", e.toString());
	}
	logger.error("test error!");
	long endTime = System.currentTimeMillis();
	logger.debug("花费时间：[{} ms]",endTime-startTime);
	logger.info("=====end=====");
	return modelMap;
	}
}
