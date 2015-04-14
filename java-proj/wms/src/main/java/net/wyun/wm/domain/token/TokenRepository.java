/**
 * 
 */
package net.wyun.wm.domain.token;


import org.springframework.data.repository.CrudRepository;

/**
 * @author Xuecheng
 *
 */
public interface TokenRepository extends CrudRepository<Token, Long> {

	Token findByToken(int token);
}
