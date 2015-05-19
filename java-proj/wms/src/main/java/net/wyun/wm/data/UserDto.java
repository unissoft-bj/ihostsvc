/**
 * 
 */
package net.wyun.wm.data;

import java.io.Serializable;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.wyun.wm.domain.Permission;
import net.wyun.wm.domain.account.Account;
import net.wyun.wm.domain.mac.Mac;
import net.wyun.wm.security.WmsAuthenticationProvider;

/**
 * @author Xuecheng
 *
 */
public class UserDto{

	private static final Logger logger = LoggerFactory.getLogger(UserDto.class);
	public UserDto() {}
	public UserDto(Mac mac) {
		this.mac = mac.getMacInString();
		this.name = mac.getMacInString();
		this.mac_pwd = mac.getPassword();
		
		StringBuilder perm = new StringBuilder();
		if(mac.getAccount() != null){
			Account acct = mac.getAccount();
			Collection<Permission> c = acct.getPermissions();
			for(Permission p:c){
				perm.append(p.getName()).append(',');
			}
		}
		logger.debug("mac {} has permission: {}", mac, perm.toString());
		
		this.role = mac.getRole() == null?"UNKNOWN":mac.getRole().getName();
		this.phone = mac.getAccount() == null?"UNKNOWN":mac.getAccount().getPhone();
	}
	
	private String name; //client side needs it

	private String mac;
	private String mac_pwd;
	private String role;
	private String phone;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getMac_pwd() {
		return mac_pwd;
	}
	public void setMac_pwd(String mac_pwd) {
		this.mac_pwd = mac_pwd;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}
