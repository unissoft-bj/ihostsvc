/**
 * 
 */
package net.wyun.wm.rest;

import javax.servlet.http.HttpServletRequest;

import net.wyun.wm.domain.mac.MacAddressLookupService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Xuecheng
 *
 */
@RestController
public class MacAddressController {
	
	    @Autowired
	    @Qualifier("macAddrLookupService")
	    MacAddressLookupService lookupSvc;
	
		@RequestMapping("/mymac")
		public String getMyMac(HttpServletRequest req) {
			
		    String ipAddress = req.getRemoteAddr();  
		    String mac = lookupSvc.getMacAddrByIP(ipAddress);   
			
			return mac;
		}
		


}
