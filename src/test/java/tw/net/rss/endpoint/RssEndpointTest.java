package tw.net.rss.endpoint;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import tw.net.rss.RssApplication;
 

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = RssApplication.class)
public class RssEndpointTest {

	/** The rest template. */
	@Autowired
	private TestRestTemplate restTemplate;

	// @Autowired
	// private RoadEventService service ;

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRecord() throws Exception {
		String uri = "/sightseeing/";

		
		ResponseEntity<String> response = this.restTemplate.getForEntity(uri, String.class);
		System.out.println(response.getBody());
		;
		// Assert.assertEquals("[{\"comment\":\"光復南路與信義路的交叉路口.號誌異常 光復閃黃
		// 信義閃紅\",\"name\":\"燈號不正常\",\"gpsX1\":\"121.55738\",\"gpsY1\":\"25.03321\"}]",
		// response.getBody());
	}
}
