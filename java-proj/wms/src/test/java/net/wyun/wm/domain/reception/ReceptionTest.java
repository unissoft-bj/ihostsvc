/**
 * 
 */
package net.wyun.wm.domain.reception;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Xuecheng
 *
 */
public class ReceptionTest {

	@Test
	public void testReception() throws JsonProcessingException {
		Reception reception = new Reception();
		reception.setId("606-dfd3-11e4-90f4");
		reception.setCreatet(new Date());
		reception.setAgentId("90b1e606-dfd3-11e4-90f4-000c29c5df73");
		ObjectMapper mapper = new ObjectMapper();
		String r_str = mapper.writeValueAsString(reception);
		System.out.println(r_str);
	}

}
