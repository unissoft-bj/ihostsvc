/**
 * 
 */
package net.wyun.wm.data;

import java.io.Serializable;

import net.wyun.wm.domain.mac.Mac;

/**
 * @author Xuecheng
 *
 */
public class UserDto{

	public UserDto(Mac mac) {
		this.mac = mac.getMacInString();
		this.name = mac.getMacInString();
		this.mac_pwd = mac.getPassword();
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
