package db;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import com.mongodb.MongoClient;

/**
 * Session Bean implementation class MongoClientProvider
 */
@Singleton
@LocalBean
@Startup
public class MongoClientProvider {

	@Resource(lookup = "java:global/LocalMongoClient")
	private MongoClient mongoClient;
	
	@Lock(LockType.READ)
	public MongoClient getMongoClient(){	
		return mongoClient;
	}
	
	@PostConstruct
	public void startup() {
		System.out.println("KREIRAN DB PROVIDER");
	}
}
