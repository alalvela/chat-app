package db.dao;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.bson.Document;

import com.mongodb.BasicDBList;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import db.MongoClientProvider;
import model.User;

/**
 * Session Bean implementation class UserDAO
 */
@Stateless
@LocalBean
public class UserDAO {

	private final String dbName = "test";
	private final String userCollection = "users";
	private final String friendCollection = "friends";
	private MongoClient mongoClient;
	
	@EJB
	private MongoClientProvider mongoClientProvider;

	@PostConstruct
	private void init() {
		mongoClient = mongoClientProvider.getMongoClient();
	}
	
	public void addUser(User user) {
		getCollection(userCollection).insertOne(userToDocument(user));
		getCollection(friendCollection).insertOne(new Document()
				.append("username", user.getUsername())
				.append("friends", new BasicDBList()));
	}
		
	public User findUser(String username) {
		Document userDoc = findUserDocument(username);
		return (userDoc == null) ? new User() : documentToUser(userDoc);
	}
	
	public List<User> getAllUsers() {
		List<User> users = new ArrayList<>();
		MongoCursor<Document> cursor = getCollection(userCollection).find().iterator();
		try {
		    while (cursor.hasNext()) {	//cursor.next() je Document
		        users.add(documentToUser(cursor.next()));
		    }
		} finally {
		    cursor.close();
		}
		return users;
	}
	
	private MongoCollection<Document> getCollection(String collection) {
		return this.mongoClient.getDatabase(dbName).getCollection(collection);
	}
	
	private Document findUserDocument(String username) {
		return getCollection(userCollection).find(eq("username", username)).first();
	}

	private User documentToUser(Document document) {
		User user = new User();
		user.setUsername(document.getString("username"));
		user.setPassword(document.getString("password"));
		user.setFirstname(document.getString("firstname"));
		user.setLastname(document.getString("lastname"));
		return user;
	}
	
	private Document userToDocument(User user) {
		Document document = new Document();
		document.append("username", user.getUsername());
		document.append("password", user.getPassword());
		document.append("firstname", user.getFirstname());
		document.append("lastname", user.getLastname());
		return document;
	}
}
