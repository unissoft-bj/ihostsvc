/**
 * 
 */
package net.wyun.wm.domain.reception;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import net.wyun.wm.domain.WmsData;

/**
 * @author Xuecheng
 *
 */
@Entity
@Table(name = "recpt_activity")
public class ReceptionActivity extends WmsData{
	
	public enum SourceType {
		MACADRESS, AUDIO, PHOTO;
	}

	
	public ReceptionActivity(){
		this.createt = new Date();
	}
     private String reception_id; //    smallint unsigned      NOT NULL auto_increment primary key,
     private SourceType resource_type = SourceType.AUDIO; //   varchar(10)            NOT NULL default '',                  #mac address, photo, audio etc.
     private String resource_info = ""; //   varchar(25)            NOT NULL default ''                   #possible id, description
     
     private String file = ""; //            varchar(25)            NOT NULL default '',
     private boolean     keep = false; //            boolean                NOT NULL default 0,
     private boolean     sentToServer = false; //	     sent_to_server  boolean                NOT NULL DEFAULT 0,

	public String getReception_id() {
		return reception_id;
	}

	public void setReception_id(String reception_id) {
		this.reception_id = reception_id;
	}

	public SourceType getResource_type() {
		return resource_type;
	}

	public void setResource_type(SourceType resource_type) {
		this.resource_type = resource_type;
	}

	public String getResource_info() {
		return resource_info;
	}

	public void setResource_info(String resource_info) {
		this.resource_info = resource_info;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public boolean isKeep() {
		return keep;
	}

	public void setKeep(boolean keep) {
		this.keep = keep;
	}

	@Column(name = "sent_to_server", nullable = false)
	public boolean isSentToServer() {
		return sentToServer;
	}

	public void setSentToServer(boolean sentToServer) {
		this.sentToServer = sentToServer;
	}
  

}
