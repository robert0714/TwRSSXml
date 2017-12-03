package tw.net.rss.service;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test; 
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
  
/***
 * 測試JAXB從schema(xsd)轉成POJOs讓Jackson進行轉換
 * https://stackify.com/java-xml-jackson/
 * **/
public class RSSJacksonConvertorTest {
	private RssService service ; 

	private XmlMapper xmlMapper;

	boolean isWindows = System.getProperty("os.name")
			  .toLowerCase().startsWith("windows");
	
	@Before
	public void setUp() throws Exception { 
		service = new RssService();
		JacksonXmlModule module = new JacksonXmlModule();
		module.setDefaultUseWrapper(false); 
		JaxbAnnotationModule jaxbModule = new JaxbAnnotationModule();
		
		this.xmlMapper = new XmlMapper();	
		this.xmlMapper.registerModule(jaxbModule);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	@Ignore
	public void testWeb() throws Exception {
		final String xml = service.getOriginalXML();
		System.out.println(xml); 
		develop.odata.etl.model.jaxb.Rss object = this.xmlMapper.readValue(xml, develop.odata.etl.model.jaxb.Rss.class);
		System.out.println(object);  
	} 
}
