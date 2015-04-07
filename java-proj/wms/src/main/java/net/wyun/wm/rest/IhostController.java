/**
 * 
 */
package net.wyun.wm.rest;

import java.util.Date;
import java.util.List;

import net.wyun.wm.domain.IHost;
import net.wyun.wm.domain.IHostRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;

/**
 * @author Xuecheng
 *
 */
@RestController
public class IhostController {
	
	String id;
	
	@Autowired
	IHostRepository repo;
	
	@RequestMapping(value="/ihost", method=RequestMethod.GET)
	public IHost getId() {
		if(null != id) return repo.findOne(id);
		
		//get
		List<IHost> ihostList = Lists.newArrayList(repo.findAll());
		if(!ihostList.isEmpty()){
			IHost ih = ihostList.get(0);
			id = ih.getUuidHex();
			return ih;
		}
		//create a new one
		IHost i = repo.save(new IHost());
		id = i.getUuidHex();
		
		return i;
	}
	
	@RequestMapping(value="/ihost", method=RequestMethod.PUT)
	public IHost updateIHost(@RequestBody IHost v){ 
		 v.setModify_t(new Date());
		 return repo.save(v);
	}

}
