/**
 * 
 */
package net.wyun.wm.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Xuecheng
 *
 */
@Entity
@Table(name = "recpt_activity")
public class ReceptionActivity {
	
	public enum SourceType {
		MACADRESS, AUDIO, PHOTO;
	}
	
	 private Long id;         //     smallint unsigned      NOT NULL auto_increment primary key,

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}

     private int reception_id; //    smallint unsigned      NOT NULL auto_increment primary key,
     private SourceType resource_type; //   varchar(10)            NOT NULL default '',                  #mac address, photo, audio etc.
     private String resource_info; //   varchar(25)            NOT NULL default ''                   #possible id, description

}
