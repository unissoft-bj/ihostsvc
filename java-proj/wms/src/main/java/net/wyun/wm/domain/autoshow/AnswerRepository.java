/**
 * 
 */
package net.wyun.wm.domain.autoshow;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author Xuecheng
 *
 */
public interface AnswerRepository extends CrudRepository<Answer, Integer>{
	
	List<Answer> deleteByCreatetAfter(@Param("cutOff") Date cutOff);

}
