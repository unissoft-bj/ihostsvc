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

public interface SurveyeeRepository extends CrudRepository<Surveyee, String>{

	List<Surveyee> deleteByCreatetAfter(@Param("cutOff") Date cutOff);
}
