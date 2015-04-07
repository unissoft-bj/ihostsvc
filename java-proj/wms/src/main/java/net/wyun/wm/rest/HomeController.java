/*
 * Copyright 2014 Matrix wifi
 */

package net.wyun.wm.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//testing class for wms. we can check if the system is working or not
@RestController
public class HomeController {

	public static String HOME_INFO = "{'wms':'matrix wifi system', 'version': '0.1.0'}";
	@RequestMapping("/wms")
	public String home() {
		return HOME_INFO;
	}

}
