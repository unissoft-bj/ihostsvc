/**
 * 
 */
package net.wyun.wm.domain.mac;


import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Xuecheng
 *
 */
public interface MacRepository extends CrudRepository<Mac, String> {

	Mac findByMac(Long mac);
	
	@Modifying
	@Transactional
	@Query("DELETE FROM Mac mac WHERE (mac.createt > :cutOff)")
	int removeByCreate_tGreaterThan(@Param("cutOff") Date cutOff);
	
	//@Query(value = "SELECT mac FROM Mac mac WHERE mac.create_t > :cutOff")
	List<Mac> findByCreatetGreaterThan(@Param("cutOff") Date cutOff);

	/**
	 * for some reason, this delete method doesn't work. ???
	 * @param cutOff
	 * @return
	 */
	List<Mac> deleteByCreatetAfter(@Param("cutOff") Date cutOff);
	
}
