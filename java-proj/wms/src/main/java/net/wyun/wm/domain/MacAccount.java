/**
 * 
 */
package net.wyun.wm.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import net.wyun.wm.domain.account.Account;
import net.wyun.wm.domain.mac.Mac;

/**
 * @author Xuecheng
 *
 */
@Entity
@Table(name = "mac_account")
public class MacAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id; // int unsigned NOT NULL auto_increment primary key,
	
	@Column(name = "mac_id")
	private Mac mac; //Integer mac_id; // int unsigned NOT NULL,
	@Column(name = "account_id")
	private Account account; //String account_id; // char(32) NOT NULL,
	
	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "mac_id")  
	public Mac getMac() {
		return mac;
	}

	public void setMac(Mac mac) {
		this.mac = mac;
	}

	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id") 
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	
	
	//additional fields
	private Date create_t; // timestamp NOT NULL,
	private Date modify_t; // timestamp DEFAULT NULL,

	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
	public Date getCreate_t() {
		return create_t;
	}

	public void setCreate_t(Date create_t) {
		this.create_t = create_t;
	}

	public Date getModify_t() {
		return modify_t;
	}

	public void setModify_t(Date modify_t) {
		this.modify_t = modify_t;
	}

}
