package tw.net.rss.service;

  
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
 
import tw.net.rss.model.Rss;

@SpringBootApplication
public class RssApplication implements CommandLineRunner  {
	 
	
	@Autowired
	private RssService service ;
	
	public static void main(String[] args) {
		SpringApplication.run(RssApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String xml = service.getOriginalXML();
		Rss data = service.comvert(xml);
		service.exportXLSX(data);
		 
	}
		
		
}
