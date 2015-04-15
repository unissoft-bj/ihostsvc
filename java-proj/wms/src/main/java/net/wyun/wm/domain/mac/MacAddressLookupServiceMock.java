/**
 * 
 */
package net.wyun.wm.domain.mac;

import org.springframework.stereotype.Service;

/**
 * @author Xuecheng
 *
 */
@Service("mockMacAddrLookupService")
public class MacAddressLookupServiceMock implements MacAddressLookupService{

	@Override
	public String getMacAddrByIP(String ip) {
		// TODO: map ip to mac address in Chilli
		return MacAddressUtil.randomMacAddr();

	}

}