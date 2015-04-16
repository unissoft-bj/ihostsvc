/**
 * 
 */
package net.wyun.wm.domain.role;

import org.springframework.data.repository.CrudRepository;

/**
 * @author Xuecheng
 *
 */
public interface RoleRepository extends CrudRepository<Role, Long>{
    Role findByName(String name);
}
