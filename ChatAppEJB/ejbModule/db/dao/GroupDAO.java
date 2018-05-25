package db.dao;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.or;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Updates;

import db.MongoClientProvider;
import model.Group;
import model.Message;

/**
 * Session Bean implementation class GroupDAO
 */
@Stateless
@LocalBean
@SuppressWarnings("unchecked")
public class GroupDAO {

	private final String dbName = "chatapp";
	private final String groupCollection = "groups";
	private final String groupMessageCollection = "groupMessage";
	private final String messageCollection = "messages";
	private MongoClient mongoClient;
		
	@EJB
	private MongoClientProvider mongoClientProvider;

	@PostConstruct
	private void init() {
		mongoClient = mongoClientProvider.getMongoClient();
	}
	
	private MongoCollection<Document> getCollection(String collection) {
		return this.mongoClient.getDatabase(dbName).getCollection(collection);
	}
	
	public void insertGroup(Group group) {
		getCollection(groupCollection).insertOne(groupToDocument(group));
		getCollection(groupMessageCollection).insertOne(new Document().append("name", group.getName())
																		.append("ids", new ArrayList<>()));
	}
	
	public void addMember(String groupName, String username) {
		getCollection(groupCollection).updateOne(eq("name", groupName), Updates.addToSet("members", username));
	}
	
	public void removeMember(String groupName, String username) {
		getCollection(groupCollection).updateOne(eq("name", groupName), Updates.pull("members", username));		
	}
	
	public void addMessageIdToGroup(String groupName, String messageId) {
		getCollection(groupMessageCollection).updateOne(eq("name", groupName), Updates.addToSet("ids", messageId));
	}
	
	public List<Message> getMessagesFromGroup(String name) {
		List<Message> ret = new ArrayList<>();
		
		Document doc = getCollection(groupMessageCollection).find(eq("name", name)).first();
		List<String> messageIds = doc.get("ids", List.class);
		
		MongoCursor<Document> cursor = getCollection(messageCollection).find().iterator();
		try {
		    while (cursor.hasNext()) {	//cursor.next() je Document
		    	Document docMessage = cursor.next();
		        Message msg = documentToMessage(docMessage);
		        if(messageIds.contains(msg.getId())) {
		        	ret.add(msg);
		        }
		    }
		} finally {
		    cursor.close();
		}
		// find all messages with ids in messageIds
		return ret;
	}
	
	public List<String> getGroupMemberUsernames(String groupName) {
		Document doc = getCollection(groupCollection).find(eq("name", groupName)).first();
		List<String> members = doc.get("members", List.class);
		return members;
	}
	
	public List<Group> getGroupsFromMember(String username) {
		List<Group> groups = new ArrayList<>();
		MongoCursor<Document> cursor = getCollection(groupCollection).find().iterator();
		try {
		    while (cursor.hasNext()) {	//cursor.next() je Document
		    	Document doc = cursor.next();
		    	List<String> members = doc.get("members", List.class);
		    	if (members.contains(username)) {
		    		groups.add(documentToGroup(doc));
		    	}
		    }
		} finally {
		    cursor.close();
		}
		return groups;
	}
	
	private Group documentToGroup(Document document) {
		Group group = new Group();
		group.setCreator(document.getString("creator"));
		group.setName(document.getString("name"));
		return group;
	}

	private Document groupToDocument(Group group) {
		Document document = new Document();
		document.append("creator", group.getCreator());
		document.append("name", group.getName());
		document.append("members", new ArrayList<>());
		return document;
	}
	
	private Message documentToMessage(Document document) {
		Message msg = new Message();
		msg.setId(document.getString("id"));
		msg.setSender(document.getString("sender"));
		msg.setReciever(document.getString("reciever"));
		msg.setContent(document.getString("content"));
		msg.setDate(document.getString("date"));
		msg.setTime(document.getString("time"));
		msg.setIsGroup(document.getBoolean("isGroup"));
		return msg;
	}

}
