package org.fox.o2o.web.local;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/frontend")
public class LocalController {
	@RequestMapping(value="/register",method = RequestMethod.GET)
	private String Rgister() {
		return "/frontend/register";
	}
}
