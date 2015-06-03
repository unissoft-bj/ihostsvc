/**
 * 
 */
package net.wyun.wm.domain;


import java.util.Date;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Xuecheng
 *
 */
public interface MacAccountRepository extends CrudRepository<MacAccount, String>{

	@Modifying
	@Transactional
	@Query("DELETE FROM MacAccount ma WHERE (ma.create_t > :cutOff)")
	int removeByCreate_tGreaterThan(@Param("cutOff") Date cutOff);

}
