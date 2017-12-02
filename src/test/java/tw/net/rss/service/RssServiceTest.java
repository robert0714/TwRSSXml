package tw.net.rss.service;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import tw.net.rss.model.Rss;

public class RssServiceTest {
	RssService service ; 
	@Before
	public void setUp() throws Exception {
		service = new RssService();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetOriginalXML() throws Exception {
		final String xml = service.getOriginalXML();
		Assert.assertNotNull(xml);
	}

	@Test
	public void testComvert() throws Exception {
		final String xml = service.getOriginalXML();
		Rss rss = service.comvert(xml);
		Assert.assertNotNull(rss);
	}

}
