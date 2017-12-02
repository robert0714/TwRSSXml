package tw.net.rss.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
 
import net.sf.sevenzipjbinding.ExtractAskMode;
import net.sf.sevenzipjbinding.ExtractOperationResult;
import net.sf.sevenzipjbinding.IArchiveExtractCallback;
import net.sf.sevenzipjbinding.IInArchive;
import net.sf.sevenzipjbinding.ISequentialOutStream;
import net.sf.sevenzipjbinding.PropID;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.SevenZipException;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import tw.net.rss.model.Item;
import tw.net.rss.model.Rss;

@Component
public class RssService {
	private XmlMapper xmlMapper;
	// SSL protocal太過老舊，舊到jdk都不支援
	private String resourceUrl = "https://www.taiwan.net.tw/rss.aspx?sNo=0001002";
	private String curlUrl = "https://winampplugins.co.uk/curl/curl_7_53_1_openssl_nghttp2_x64.7z";
	private RestTemplate restTemplate;
	private CloseableHttpClient httpClient;
	private boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
	private String homeUser = System.getProperty("user.home");

	
	public RssService() {
		super();
		JacksonXmlModule module = new JacksonXmlModule();
		module.setDefaultUseWrapper(false);
		this.xmlMapper = new XmlMapper(module);

		// SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
		// SSLContext.getDefault(),
		// new String[] { "SSLv2Hello","SSLv3","TLSv1","TLSv1.1","TLSv1.2"},
		// null,
		// SSLConnectionSocketFactory.getDefaultHostnameVerifier());
		// Registry<ConnectionSocketFactory> socketFactoryRegistry =
		// RegistryBuilder.<ConnectionSocketFactory>create()
		// .register("http", PlainConnectionSocketFactory.getSocketFactory())
		// .register("https", socketFactory)
		// .build();
		//
		// PoolingHttpClientConnectionManager cm = new
		// PoolingHttpClientConnectionManager(socketFactoryRegistry);
		// CloseableHttpClient httpclient =
		// HttpClients.custom().setConnectionManager(cm).build();
		// HttpComponentsClientHttpRequestFactory requestFactory = new
		// HttpComponentsClientHttpRequestFactory(httpclient);
		this.httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);
		this.restTemplate = new RestTemplate(requestFactory);
	}

	public String getOriginalXML()throws ClientProtocolException, IOException {
		Process process;
		if (isWindows) {
			downloadCurlForWin();
			
			String cmd = String.format("%s\\curl  %s", homeUser, resourceUrl);
			process = Runtime.getRuntime().exec(cmd);
		} else {
		    process = Runtime.getRuntime()
		      .exec(String.format("curl  %s", resourceUrl));
		}		
		final String xml = IOUtils.toString(	process.getInputStream());
		return xml ; 
	}
	public Rss comvert(String xml ) throws JsonParseException, JsonMappingException, IOException {  
		Rss result = this.xmlMapper.readValue(xml, Rss.class); 
		return result ; 
	}
	public void exportXLSX(Rss data) throws IOException {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date =  sdf.format(new Date());
		String fileName = "rss-"+date+".xlsx" ;
		try (HSSFWorkbook wb = new HSSFWorkbook();
				FileOutputStream fileOut = new FileOutputStream(new File(homeUser, fileName));) {
			HSSFSheet sheet = wb.createSheet();
			HSSFRow row = sheet.createRow(0);

			HSSFCell cellTitle = row.createCell(0);
			cellTitle.setCellValue("Title ");

			HSSFCell cellDescription = row.createCell(1);
			cellDescription.setCellValue("Description");

			HSSFCell cellAuthor = row.createCell(2);
			cellAuthor.setCellValue("Author");

			HSSFCell cellPubDate = row.createCell(3);
			cellPubDate.setCellValue("PubDate");

			HSSFCell cellStartDate = row.createCell(4);
			cellStartDate.setCellValue("StartDate");

			HSSFCell cellEndDate = row.createCell(5);
			cellEndDate.setCellValue("EndDate");

			final Item[] items = data.getChannel().getItems();
			int i = 0;
			for (final Item item : items) {
				++i;
				String title = item.getTitle();
				String description = StringUtils.trim(item.getDescription());
				String author = item.getAuthor();
				String pubDate = item.getPubDate();
				Date startDate = item.getStartDate();
				Date endDate = item.getEndDate();
				HSSFRow eachRow = sheet.createRow(i);

				HSSFCell cellTitle1 = eachRow.createCell(0);
				cellTitle1.setCellValue(title);				
				int columnIndex1 = cellTitle1.getColumnIndex();
				sheet.autoSizeColumn(columnIndex1);

				
				HSSFCell cellDescription1 = eachRow.createCell(1);
				cellDescription1.setCellValue(description);
				int columnIndex2 = cellDescription1.getColumnIndex();
				sheet.autoSizeColumn(columnIndex2);
				

				HSSFCell cellAuthor1 = eachRow.createCell(2);
				cellAuthor1.setCellValue(author);
				int columnIndex3 = cellAuthor1.getColumnIndex();
				sheet.autoSizeColumn(columnIndex3);
				

				HSSFCell cellPubDate1 = eachRow.createCell(3);
				cellPubDate1.setCellValue(pubDate);
				int columnIndex4 = cellPubDate1.getColumnIndex();
				sheet.autoSizeColumn(columnIndex4);
				

				HSSFCell cellStartDate1 = eachRow.createCell(4);
				cellStartDate1.setCellValue(sdf.format(startDate));
				int columnIndex5= cellStartDate1.getColumnIndex();
				sheet.autoSizeColumn(columnIndex5);
				
				

				HSSFCell cellEndDate1 = eachRow.createCell(5);
				cellEndDate1.setCellValue(sdf.format(endDate));
				int columnIndex6= cellEndDate1.getColumnIndex();
				sheet.autoSizeColumn(columnIndex6);
			}

			// write this workbook to an Outputstream.
			wb.write(fileOut);
			fileOut.flush();

		}
	}

	protected void downloadCurlForWin() throws ClientProtocolException, IOException {
		final HttpGet httpGet = new HttpGet(curlUrl); 
		try(CloseableHttpResponse response1 = this.httpClient.execute(httpGet);) {
			HttpEntity entity1 = response1.getEntity();
			// do something useful with the response body
			// and ensure it is fully consumed
			final byte[] data = EntityUtils.toByteArray(entity1);
			File file7z = new File(homeUser, "curl.7z");
			FileUtils.writeByteArrayToFile(file7z, data);
			un7zip(file7z, homeUser);
		} 
	}

	protected void un7zip(File f, String destDir) throws IOException {
		RandomAccessFile randomAccessFile = new RandomAccessFile(f, "r");

		try (IInArchive inArchive = SevenZip.openInArchive(null, // autodetect archive type
				new RandomAccessFileInStream(randomAccessFile));) {

			System.out.println("   Hash   |    Size    | Filename");
			System.out.println("----------+------------+---------");

			int count = inArchive.getNumberOfItems();
			List<Integer> itemsToExtract = new ArrayList<Integer>();
			for (int i = 0; i < count; i++) {
				if (!((Boolean) inArchive.getProperty(i, PropID.IS_FOLDER)).booleanValue()) {
					itemsToExtract.add(Integer.valueOf(i));
				}
			}
			int[] items = new int[itemsToExtract.size()];
			int i = 0;
			for (Integer integer : itemsToExtract) {
				items[i++] = integer.intValue();
			}
			inArchive.extract(items, false, // Non-test mode
					new MyExtractCallback(inArchive, destDir));
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
						File path = new File(extractPath, filePath);
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
						// logger.severe(e.getLocalizedMessage());
					} finally {
						try {
							if (fos != null) {
								fos.flush();
								fos.close();
							}
						} catch (IOException e) {
							e.printStackTrace();
							// logger.severe(e.getLocalizedMessage());
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
