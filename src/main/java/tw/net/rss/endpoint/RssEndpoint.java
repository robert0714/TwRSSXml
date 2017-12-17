package tw.net.rss.endpoint;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import tw.net.rss.model.Item;
import tw.net.rss.model.Rss;
import tw.net.rss.service.RssService;

@RestController
@RequestMapping(value = "/sightseeing")
public class RssEndpoint {
	@Autowired
	private RssService service;

	/** The logger. */
	private final Logger LOGGER = LoggerFactory.getLogger(RssService.class);
	private static String ALL = "ALL";

	/***
	 * 快取
	 */
	private LoadingCache<String, Item[]> cacheData = CacheBuilder.newBuilder().maximumSize(1000)
			.expireAfterAccess(24, TimeUnit.HOURS).build(new CacheLoader<String, Item[]>() {
				@Override
				public Item[] load(String searchData) throws Exception {
					Item[] result = new Item[0];
					String xml = service.getOriginalXML();
					Rss data = service.comvert(xml);
					if (data != null && data.getChannel() != null) {
						return data.getChannel().getItems();
					}

					return result;
				}
			});

	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE }, value = "/")
	public ResponseEntity<Item[]> listdata() {
		Item[] data = null;
		try {
			data = cacheData.get(ALL);
		} catch (ExecutionException e) {
			LOGGER.warn("An exception occurred while " + "fetching place details!", e.getCause());
			return null;
		}

		final ResponseEntity<Item[]> result = new ResponseEntity<Item[]>(data, HttpStatus.OK);

		return result;
	}
}
