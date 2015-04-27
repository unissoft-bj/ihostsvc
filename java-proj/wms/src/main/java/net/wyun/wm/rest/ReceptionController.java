/**
 * 
 */
package net.wyun.wm.rest;

import java.util.Date;

import net.wyun.wm.domain.reception.Reception;
import net.wyun.wm.domain.reception.ReceptionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Xuecheng
 *
 */
//@RequestMapping("/secure")
@RestController
public class ReceptionController {
	
	@Autowired
	ReceptionRepository receptionRepo;
	
	@RequestMapping(value="/reception", method = {RequestMethod.POST})
	Reception createUpdateReception(@RequestBody Reception reception){
		
		if(reception.getId() != null){
			reception.setModify_t(new Date());
		}
		return receptionRepo.save(reception);
		
	}
	

}
