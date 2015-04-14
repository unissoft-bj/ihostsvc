/**
 * 
 */
package net.wyun.wm.data;

/**
 * @author Xuecheng
 *
 */
public class RegisterRequest {
	

	private String token;
	private String mac;
	
	
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

}
