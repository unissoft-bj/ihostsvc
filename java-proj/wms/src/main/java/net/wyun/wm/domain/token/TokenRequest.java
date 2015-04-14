/**
 * 
 */
package net.wyun.wm.domain.token;

/**
 * @author Xuecheng
 *
 */
public class TokenRequest {
	
	public enum Type {web, sms}
	public enum UserRole{ROLE_USER, ROLE_SALES, ROLE_SERVICE, ROLE_MANAGER }
	
	UserRole userRole;
	String phone;
	Type   type;
	
	public UserRole getUserRole() {
		return userRole;
	}
	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	

}
