package db.dao;

import static com.mongodb.client.model.Filters.eq;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;

import db.MongoClientProvider;
import dto.Friendship;

/**
 * Session Bean implementation class FriendshipDAO
 */
@Stateless
@LocalBean
public class FriendshipDAO {

	private String dbName = "test";
	private final String friendCollection = "friends";
	private MongoClient mongoClient;
	
	@EJB
	private MongoClientProvider mongoClientProvider;

	@PostConstruct
	private void init() {
		mongoClient = mongoClientProvider.getMongoClient();
	}
	
	public void addFriendship(Friendship friendship) {
		getFriendsCollection().updateOne(eq("username", friendship.getSender()), Updates.addToSet("friends", friendship.getReciever()));
		getFriendsCollection().updateOne(eq("username", friendship.getReciever()), Updates.addToSet("friends", friendship.getSender()));
	} 
	
	public void removeFriendship(Friendship friendship) {
		getFriendsCollection().updateOne(eq("username", friendship.getSender()), Updates.pull("friends", friendship.getReciever()));
		getFriendsCollection().updateOne(eq("username", friendship.getReciever()), Updates.pull("friends", friendship.getSender()));		
	}
	
	private MongoCollection<Document> getFriendsCollection() {
		return this.mongoClient.getDatabase(dbName).getCollection(friendCollection);
	}

}
