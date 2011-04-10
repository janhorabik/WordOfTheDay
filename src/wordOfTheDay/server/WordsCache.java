package wordOfTheDay.server;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheManager;
import wordOfTheDay.client.Word9;
import wordOfTheDay.server.service.NoteServiceImpl;

import com.google.appengine.api.memcache.jsr107cache.GCacheFactory;

public class WordsCache {

	private static final Logger log = Logger.getLogger(NoteServiceImpl.class
			.getName());

	private static final WordsCache wordOfTheDayCacheInstance = new WordsCache();

	public static WordsCache getInstance() {
		return wordOfTheDayCacheInstance;
	}

	private WordsCache() {
		try {
			log.severe("WordsChache: constructor start");
			Map props = new HashMap();
			props.put(GCacheFactory.EXPIRATION_DELTA, 86400);
			cache = CacheManager.getInstance().getCacheFactory()
					.createCache(Collections.emptyMap());
			log.severe("WordsChache: constructor finish");
		} catch (CacheException e) {
			e.printStackTrace();
		}
	}

	public Word9 get(WordKey key) {
		System.out.println("getting " + key + " from cache");
		return (Word9) cache.get(key);
	}

	public void clear() {
		cache.clear();
	}

	public void put(WordKey key, Word9 word) {
		cache.put(key, word);
	}

	private Cache cache;
}