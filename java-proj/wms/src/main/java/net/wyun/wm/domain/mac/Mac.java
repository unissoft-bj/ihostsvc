package net.wyun.wm.domain.mac;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.wyun.wm.domain.MacAccount;
import net.wyun.wm.domain.account.Account;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * phone to mac is one-to-many relationship ??
 *  insert into mac (mac, password, smscheck, enabled, create_t) values (x'0123456789AB' , 'pwd', 0, 1, now());
 * 
 * @author Xuecheng
 *
 */
@Entity
@Table(name = "mac")
public class Mac implements UserDetails{

	
	public Mac(String macStr){
		create_t = new Date();
		this.mac = MacAddressUtil.toLong(macStr);
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id; // smallint unsigned NOT NULL auto_increment primary
						// key,

	//private String phone; // varchar(30) NOT NULL,
	private Long mac; // BIGINT UNSIGNED NOT NULL,
	// # token int UNSIGNED NOT NULL, # history. use the latest to verify
	// account, sms 上网码/
	private String password; // VARCHAR(36) NOT NULL, # password for mac/pw
								// login
	private boolean smscheck; // boolean NOT NULL default 0, # if the user needs
								// to be verified by sms
	private boolean enabled; // boolean NOT NULL default 0, # if false, disable
								// the account login with mac/password, user
								// login with phone/pw
	private Date create_t; // datetime NOT NULL,
	private Date modify_t; // datetime DEFAULT NULL

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getMac() {
		return mac;
	}

	public void setMac(Long mac) {
		this.mac = mac;
	}
	
	@Transient
	public String getMacInString(){
		return MacAddressUtil.toMacString(mac);
	}
	

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isSmscheck() {
		return smscheck;
	}

	public void setSmscheck(boolean smscheck) {
		this.smscheck = smscheck;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
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
	
	@Transient
	private Set<MacAccount> macAccounts = new HashSet<MacAccount>();
	@OneToMany(mappedBy = "mac")
	public Set<MacAccount> getMacAccounts() {
		return macAccounts;
	}

	
	//======================= userdetails related ========================//
	@Transient
	public Account getAccount(){
		//usually only one MacAccount in macAccounts set, very few it has more than one.
		if(macAccounts.isEmpty()){
			return null; //anonymous account
		}
		
		if(macAccounts.size() == 1) {
			for(MacAccount ma:macAccounts){
				return ma.getAccount();
			}
		}
		
		Optional<MacAccount> oma = macAccounts.stream()
		           .reduce((ma1, ma2) -> ma1.getCreate_t().getTime() > ma2.getCreate_t().getTime()? ma1 : ma2);
		
		return oma.get().getAccount();
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		return getAccount().getAuthorities();
	}

	@Override
	public String getUsername() {
		return MacAddressUtil.toMacString(mac);
	}

	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

}
