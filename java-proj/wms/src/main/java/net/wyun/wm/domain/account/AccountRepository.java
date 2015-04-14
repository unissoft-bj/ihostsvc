/**
 * 
 */
package net.wyun.wm.domain.account;

import org.springframework.data.repository.CrudRepository;

/**
 * @author Xuecheng
 *
 */
public interface AccountRepository extends CrudRepository<Account, String>{

	Account findByPhone(String phone);
}
