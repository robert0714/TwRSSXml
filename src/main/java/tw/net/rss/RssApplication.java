package tw.net.rss;

  
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
 
import tw.net.rss.model.Rss;
import tw.net.rss.service.RssService;

@SpringBootApplication
public class RssApplication implements CommandLineRunner  {
	 
	
	@Autowired
	private RssService service ;
	
	public static void main(String[] args) {
		SpringApplication.run(RssApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		String xml = service.getOriginalXML();
//		System.out.println(xml);;
//		Rss data = service.comvert(xml);
//		service.exportXLSX(data);
		 
	}
		
		
}
