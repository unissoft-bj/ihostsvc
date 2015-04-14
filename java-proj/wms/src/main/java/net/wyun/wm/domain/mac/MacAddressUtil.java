/**
 * 
 */
package net.wyun.wm.domain.mac;

import java.util.regex.Pattern;

import com.google.common.base.CharMatcher;
import com.google.common.base.Strings;

/**
 * @author Xuecheng
 *
 * class to manipulate mac address. for ex., convert mac string to Long
 * or convert Long to mac string
 */
public class MacAddressUtil {
	//ex. mac: 62-36-DD-6D-CE-05
	public static final String pattern = "[^A-Fa-f0-9\\s]";
	public static int BASE16 = 16;
	public static int BASE10 = 10;
	
	public static final String macValidator = "[0-9a-f]{2}([-:])[0-9a-f]{2}(\\1[0-9a-f]{2}){4}$";
	public static Pattern macpattern = Pattern.compile(macValidator);
	
	//this one is thread saft, no need synchronize
	public static boolean isValidMac(final String mac){
		return macpattern.matcher(mac.toLowerCase()).matches();
	}
	
	public static String clean(String mac){
		//maybe - or :
		return mac.replaceAll(pattern, "");
	}
	

	/**
	 * mac string long base10
	 * @param mac
	 * @return
	 */
	public static Long toLong(String mac){
		String s = clean(mac);
		//remove leading 0s
		String t = CharMatcher.is('0').trimLeadingFrom(s);
		//now we have hex
		
		return Long.parseLong(t, BASE16);
	}
	
	/**
	 * 
	 * @param mac_in_long
	 * @return MacAddress in string and length 12, <br>
	 *        for ex., '0123456789AB', letters are in uppercase
	 */
	public static String toMacString(Long mac_in_long){
		String ms = Long.toString(mac_in_long, BASE16).toUpperCase();
		
		//we still need to do the padding
		return Strings.padStart(ms, 12, '0');
	}
	
	/**
	 *  for ex., 0123456789AB ==> 01-23-45-67-89-AB
	 * @param mac_in_long
	 * @return 
	 */
	
	public static String toStandardMacString(Long mac_in_long){
		String ms = Long.toString(mac_in_long, BASE16).toUpperCase();
		
		//we still need to do the padding
		return formatMac(Strings.padStart(ms, 12, '0'));
	}
	
	/**
	 *  format a mac address with '-' for every 2 chars
	 *  for ex., 0123456789AB ==> 01-23-45-67-89-AB
	 * @param inMac
	 * @return
	 */

	static String formatMac(String inMac) {
		final StringBuilder b = new StringBuilder(18);
		for (int i = 0; i < inMac.length(); i++) {
			b.append(inMac.charAt(i));
			if (i % 2 == 1 && i != inMac.length() - 1)
				b.append('-');
		}
		return b.toString();
	}
}
