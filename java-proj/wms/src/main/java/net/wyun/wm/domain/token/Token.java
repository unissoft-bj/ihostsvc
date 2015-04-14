/**
 * 
 */
package net.wyun.wm.domain.token;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import net.wyun.wm.domain.token.TokenRequest.UserRole;

/**
 * @author Xuecheng
 *
 */
@Entity
@Table(name = "token")
public class Token {
	public Token(){}
	
	public Token(String phone, int token) {
		this.phone = phone;
		this.token = token;
		this.create_t = new Date();
	}

	private Long id; //            smallint unsigned  NOT NULL auto_increment primary key,

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
	
    private String phone; //         varchar(30)        NOT NULL,
    private int token;         //int UNSIGNED       NOT NULL,                          
    private String agent_id; //      varchar(36)        NOT NULL,
    private Date create_t; //      datetime           NOT NULL,
    private boolean used = false; //          boolean            NOT NULL default 0
    private String mac = "";          //mac of the device which uses this token to register, this should be updated if someone uses this token
    
    private UserRole user_role;

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getToken() {
		return token;
	}

	public void setToken(int token) {
		this.token = token;
	}

	public String getAgent_id() {
		return agent_id;
	}

	public void setAgent_id(String agent_id) {
		this.agent_id = agent_id;
	}

	public Date getCreate_t() {
		return create_t;
	}

	public void setCreate_t(Date create_t) {
		this.create_t = create_t;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

}
