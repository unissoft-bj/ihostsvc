/**
 * 
 */
package net.wyun.wm.domain.mac;

/**
 * @author Xuecheng
 * BC-CF-CC-0B-96-48 172.16.0.101 dnat 552db9cf00000002 0 - 0/0 0/0 0/0 0/0 0 0 0/0 0/0 http://172.16.0.1:3990/favicon.ico
   80-CF-41-F4-B3-58 172.16.0.100 dnat 552db9ad00000001 0 - 0/0 0/0 0/0 0/0 0 0 0/0 0/0 http:///
 *
 */
public interface MacAddressLookupService {

	/**
	 * Retrieve the Mac address from the ip of a device
	 * @param ip
	 *         the ip of the device
	 * @return the mac address of the device
	 */
	String getMacAddrByIP(String ip);
}
