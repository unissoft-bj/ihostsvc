/**
 * 
 */
package net.wyun.wm.domain.account;

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
public interface AccountRepository extends CrudRepository<Account, String>{

	Account findByPhone(String phone);
	
	@Modifying
	@Transactional
	@Query("DELETE FROM Account a WHERE (a.createt > :cutOff)")
	int removeByCreate_tGreaterThan(@Param("cutOff") Date cutOff);
	
	List<Account> deleteByCreatetAfter(@Param("cutOff") Date cutOff);
}
