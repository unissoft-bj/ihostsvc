/**
 * 
 */
package net.wyun.wm.domain.mac;


import org.springframework.data.repository.CrudRepository;

/**
 * @author Xuecheng
 *
 */
public interface MacRepository extends CrudRepository<Mac, Integer> {

	Mac findByMac(Long mac);

	
}
