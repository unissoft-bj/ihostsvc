/**
 * 
 */
package net.wyun.wm.domain.reception;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author Xuecheng
 *
 */
public interface ReceptionRepository extends CrudRepository<Reception, String>{
	

	List<Reception> findByCreatetAfterAndAgentIdOrderByCreatetDesc(@Param("cutOff") Date cutOff, @Param("agentid") String agentId);
}
