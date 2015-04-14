package net.wyun.wm.domain.mac;

import org.springframework.stereotype.Service;


@Service("mockMacAddrLookupService")
public class MacAddressLookupServiceImpl implements MacAddressLookupService{

	@Override
	public String getMacAddrByIP(String ip) {
		// TODO: map ip to mac address in Chilli
		String mac = "00-FF-32-CC-E9-FB";
		return mac;
	}

}
