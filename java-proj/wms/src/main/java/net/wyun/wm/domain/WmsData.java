/**
 * 
 */
package net.wyun.wm.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author michael
 *
 */
@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class WmsData {

	@GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name= "id", columnDefinition = "VARCHAR(36)")
    @Id
	protected String id; // int unsigned NOT NULL auto_increment primary key,

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name = "create_t", nullable = false)
	protected Date     createt;  //         datetime          not NULL,
	
	public Date getCreatet() {
		return createt;
	}

	public void setCreatet(Date createt) {
		this.createt = createt;
	}
	
	
	
}
