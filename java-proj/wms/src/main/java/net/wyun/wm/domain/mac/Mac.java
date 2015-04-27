package net.wyun.wm.domain.mac;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.wyun.wm.domain.MacAccount;
import net.wyun.wm.domain.account.Account;
import net.wyun.wm.domain.role.Role;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * phone to mac is one-to-many relationship ??
 *  insert into mac (mac, password, smscheck, enabled, create_t) values (x'0123456789AB' , 'pwd', 0, 1, now());
 * 
 * @author Xuecheng
 *
 */
@Entity
@Table(name = "mac")
@JsonIgnoreProperties({"id", "smscheck", "enabled", "modify_t"})
public class Mac implements UserDetails{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2743400966884414348L;

	public Mac(){create_t = new Date();}
	
	public Mac(String macStr){
		create_t = new Date();
		this.mac = MacAddressUtil.toLong(macStr);
		this.password = UUID.randomUUID().toString();
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "mac_id")
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
		return MacAddressUtil.toStandardMacString(mac);
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
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "mac")
	private Set<MacAccount> macAccounts = new HashSet<MacAccount>();
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
	
	public boolean isPhonePasswordPresent(){
    	return getAccount() != null && !getAccount().getPassword().isEmpty();
    }
	
	@Transient
	public Role getRole(){
		if(macAccounts.isEmpty()){
			return null; //anonymous account
		}
		Optional<MacAccount> oma = macAccounts.stream()
		           .reduce((ma1, ma2) -> ma1.getCreate_t().getTime() > ma2.getCreate_t().getTime()? ma1 : ma2);
		
		Set<Role> roles = oma.get().getAccount().getRoles();
		
		if(roles.isEmpty()) return null;
		
		Role r = null;
		for(Role p:roles){
			r = p;
			break;
		}
		
		return r;
	}
	
	@Transient
	public boolean contains(String phone){
		if(macAccounts.isEmpty()) return false;
		
		for(MacAccount ma:macAccounts){
			Account a = ma.getAccount();
			if(a.getPhone().equals(phone)) return true;
		}
		
		return false;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		//if there is no account binded, considering the basic permissions here or just a empty collection
		Account a = getAccount();
		Collection<GrantedAuthority> permissions = new HashSet<GrantedAuthority>();
		
		return null != a ? a.getAuthorities() : permissions;
	}

	@Override
	public String getUsername() {
		return MacAddressUtil.toMacString(mac);
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

}
