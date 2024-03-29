package net.wyun.wm;

import static org.junit.Assert.assertEquals;

import java.net.HttpCookie;
import java.net.URI;
import java.util.List;

import net.wyun.wm.domain.mac.MacRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("dev")
@IntegrationTest("server.port:0")
public class ApplicationTests {

	@Value("${local.server.port}")
	private int port;

	private RestTemplate template = new TestRestTemplate();
	
	@Autowired
	MacRepository macRepo;
	
	 @Before
	    public void setUp() {
	       
	    }
	
	@Test
	public void placeHolder(){
		ResponseEntity<String> response = template.getForEntity("http://localhost:"
				+ port + "/", String.class);
		System.out.println("TO BE Implemented." + response);
	}

	@Test
	public void homePageLoads() {
		ResponseEntity<String> response = template.getForEntity("http://localhost:"
				+ port + "/", String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void userEndpointProtected() {
		ResponseEntity<String> response = template.getForEntity("http://localhost:"
				+ port + "/user", String.class);
		assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
	}

	//@Test
	public void resourceEndpointProtected() {
		ResponseEntity<String> response = template.getForEntity("http://localhost:"
				+ port + "/resource", String.class);
		assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
	}

	//@Test
	public void loginSucceeds() {
		ResponseEntity<String> response = template.getForEntity("http://localhost:"
				+ port + "/resource", String.class);
		String csrf = getCsrf(response.getHeaders());
		MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
		form.set("username", "60-36-DD-6D-CE-05");
		form.set("password", "04473204-a056-4b25-8702-a9a2e7ec0299");
		form.set("phone", "");
		form.set("phone_pass", "");
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-XSRF-TOKEN", csrf);
		headers.put("COOKIE", response.getHeaders().get("Set-Cookie"));
		RequestEntity<MultiValueMap<String, String>> request = new RequestEntity<MultiValueMap<String, String>>(
				form, headers, HttpMethod.POST, URI.create("http://localhost:" + port
						+ "/login"));
		System.out.println("start exchange.");
		ResponseEntity<Void> location = template.exchange(request, Void.class);
		
		System.out.println(location.toString());
		assertEquals(HttpStatus.OK,
				location.getStatusCode());
	}

	private String getCsrf(HttpHeaders headers) {
		for (String header : headers.get("Set-Cookie")) {
			List<HttpCookie> cookies = HttpCookie.parse(header);
			for (HttpCookie cookie : cookies) {
				if ("XSRF-TOKEN".equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

}
