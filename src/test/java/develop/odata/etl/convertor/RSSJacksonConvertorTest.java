package develop.odata.etl.convertor;

 
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient; 
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
  
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File; 
import java.io.FileOutputStream;
import java.io.IOException; 
import java.io.RandomAccessFile;
import java.util.ArrayList; 
import java.util.List; 
 
import net.sf.sevenzipjbinding.ExtractAskMode;
import net.sf.sevenzipjbinding.ExtractOperationResult;
import net.sf.sevenzipjbinding.IArchiveExtractCallback;
import net.sf.sevenzipjbinding.IInArchive;
import net.sf.sevenzipjbinding.ISequentialOutStream;
import net.sf.sevenzipjbinding.PropID;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.SevenZipException;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream; 
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils; 
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
 
import develop.odata.etl.model.rss.Rss; 
 
 
public class RSSJacksonConvertorTest {
 

	private XmlMapper xmlMapper;
	//SSL protocal太過老舊，舊到jdk都不支援
	private String resourceUrl = "https://www.taiwan.net.tw/rss.aspx?sNo=0001002";
	private String resourceUrl2 = "https://mail.google.com/mail/u/0/#inbox";
	
	private RestTemplate restTemplate; 
	private CloseableHttpClient 	httpClient;
	private boolean isWindows = System.getProperty("os.name")
			  .toLowerCase().startsWith("windows");
	private String homeUser = System.getProperty("user.home");
	@Before
	public void setUp() throws Exception { 
		JacksonXmlModule module = new JacksonXmlModule();
		module.setDefaultUseWrapper(false);
		this.xmlMapper = new XmlMapper(module);		
		
//		SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
//		        SSLContext.getDefault(),
//		        new String[] { "SSLv2Hello","SSLv3","TLSv1","TLSv1.1","TLSv1.2"},
//		        null,
//		        SSLConnectionSocketFactory.getDefaultHostnameVerifier());
//		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
//		        .register("http", PlainConnectionSocketFactory.getSocketFactory())
//		        .register("https", socketFactory)
//		        .build();
//
//		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
//		CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(cm).build();
//		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpclient);
		this.httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier())
				.build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);        
		this.restTemplate = new RestTemplate(requestFactory);
	 
	}

	@After
	public void tearDown() throws Exception {
	}
	protected void downloadCurlForWin() throws ClientProtocolException, IOException {
		 
		
		HttpGet httpGet = new HttpGet("https://winampplugins.co.uk/curl/curl_7_53_1_openssl_nghttp2_x64.7z");
		CloseableHttpResponse response1 = this.httpClient .execute(httpGet);
		try {
		    System.out.println(response1.getStatusLine());
		    HttpEntity entity1 = response1.getEntity();
		    // do something useful with the response body
		    // and ensure it is fully consumed
		    final byte[] data =  EntityUtils.toByteArray(entity1);
		    File file7z =  new File(homeUser,"curl.7z");
		    FileUtils.writeByteArrayToFile(file7z, data); 
		    un7zip(file7z, homeUser);
		    
		} finally {
		    response1.close();
		}
	}
	
	 
	@Test
//	@Ignore
	public void testWeb() throws Exception { 
		Process process;
		if (isWindows) {
			downloadCurlForWin();
			
			String cmd = String.format("%s\\curl  %s", homeUser, resourceUrl);
			process = Runtime.getRuntime().exec(cmd);
		} else {
		    process = Runtime.getRuntime()
		      .exec(String.format("curl  %s", resourceUrl));
		}		
		String xml = IOUtils.toString(	process.getInputStream());
		System.out.println(xml); 
		Rss object = this.xmlMapper.readValue(xml, Rss.class); 		 
		System.out.println(object);  
	} 
	
	protected  void un7zip(File f, String destDir) throws IOException {
		RandomAccessFile	 randomAccessFile = new RandomAccessFile(f, "r");
		
		try(
		IInArchive  inArchive = SevenZip.openInArchive(null, // autodetect archive type
                 new RandomAccessFileInStream(randomAccessFile));){

         System.out.println("   Hash   |    Size    | Filename");
         System.out.println("----------+------------+---------");

         int count = inArchive.getNumberOfItems();
         List<Integer> itemsToExtract = new ArrayList<Integer>();
         for (int i = 0; i < count; i++) {
             if (!((Boolean) inArchive.getProperty(i, PropID.IS_FOLDER))
                     .booleanValue()) {
                 itemsToExtract.add(Integer.valueOf(i));
             }
         }
         int[] items = new int[itemsToExtract.size()];
         int i = 0;
         for (Integer integer : itemsToExtract) {
             items[i++] = integer.intValue();
         }
         inArchive.extract(items, false, // Non-test mode
                 new MyExtractCallback(inArchive,destDir));
		}
	} 
	public static class MyExtractCallback implements IArchiveExtractCallback {
		private final IInArchive inArchive;
		private final String extractPath;

		public MyExtractCallback(IInArchive inArchive, String extractPath) {
			this.inArchive = inArchive;
			this.extractPath = extractPath;
		}

		@Override
		public ISequentialOutStream getStream(final int index, ExtractAskMode extractAskMode) throws SevenZipException {
			return new ISequentialOutStream() {
				@Override
				public int write(byte[] data) throws SevenZipException {
					String filePath = inArchive.getStringProperty(index, PropID.PATH);
					FileOutputStream fos = null;
					try {
						File dir = new File(extractPath);
						File path = new File(extractPath , filePath);
						if (!dir.exists()) {
							dir.mkdirs();
						}
						if (!path.exists()) {
							path.createNewFile();
						}
						fos = new FileOutputStream(path, true);
						fos.write(data);
					} catch (IOException e) {
						e.printStackTrace();
//						logger.severe(e.getLocalizedMessage());
					} finally {
						try {
							if (fos != null) {
								fos.flush();
								fos.close();
							}
						} catch (IOException e) {
							e.printStackTrace();
//							logger.severe(e.getLocalizedMessage());
						}
					}
					return data.length;
				}
			};
		}

		@Override
		public void prepareOperation(ExtractAskMode extractAskMode) throws SevenZipException {
		}

		@Override
		public void setOperationResult(ExtractOperationResult extractOperationResult) throws SevenZipException {
		}

		@Override
		public void setCompleted(long completeValue) throws SevenZipException {
		}

		@Override
		public void setTotal(long total) throws SevenZipException {
		}
	}
}
