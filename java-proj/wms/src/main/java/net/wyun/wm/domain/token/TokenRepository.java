/**
 * 
 */
package net.wyun.wm.domain.token;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Xuecheng
 *
 */
public interface TokenRepository extends CrudRepository<Token, Long> {

	Token findByToken(int token);
	
	@Query(value = "SELECT count(*) FROM token", nativeQuery = true)
	int findCount();
}
