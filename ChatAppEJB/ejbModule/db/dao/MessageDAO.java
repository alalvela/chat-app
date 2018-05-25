package db.dao;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.or;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import db.MongoClientProvider;
import model.Message;


@Stateless
@LocalBean
public class MessageDAO {

	private final String dbName = "chatapp";
	private final String messageCollection = "messages";
	private MongoClient mongoClient;
		
	@EJB
	private MongoClientProvider mongoClientProvider;

	@PostConstruct
	private void init() {
		mongoClient = mongoClientProvider.getMongoClient();
	}
	
	public Message insertMessage(Message msg) {
		msg.setDate(LocalDate.now().toString());
		msg.setTime(LocalTime.now().toString());
		Document doc = messageToDocument(msg);
		getCollection(messageCollection).insertOne(doc);
		ObjectId id = (ObjectId)doc.get("_id");
		
		getCollection(messageCollection).updateOne(
				new BasicDBObject("_id", id),
				new BasicDBObject("$set", new BasicDBObject("id", id.toHexString()))
		);

		Message ret = documentToMessage(doc);
		ret.setId(id.toHexString());
		return ret;
	}
	
	
	public List<Message> getAllMessagesFromUserToUser(String sender, String reciever) {
		List<Message> msgs = new ArrayList<>();
		Bson crit1 = and(eq("sender", sender), eq("reciever", reciever), eq("isGroup", false));
		Bson crit2 = and(eq("sender", reciever), eq("reciever", sender), eq("isGroup", false));
		
		MongoCursor<Document> cursor = getCollection(messageCollection).find(or(crit1, crit2)).iterator();
		try {
		    while (cursor.hasNext()) {	//cursor.next() je Document
		        msgs.add(documentToMessage(cursor.next()));
		    }
		} finally {
		    cursor.close();
		}
		return msgs;
	}
	
	
	private MongoCollection<Document> getCollection(String collection) {
		return this.mongoClient.getDatabase(dbName).getCollection(collection);
	}
	
	private Message documentToMessage(Document document) {
		Message msg = new Message();
		msg.setSender(document.getString("sender"));
		msg.setReciever(document.getString("reciever"));
		msg.setContent(document.getString("content"));
		msg.setDate(document.getString("date"));
		msg.setTime(document.getString("time"));
		msg.setIsGroup(document.getBoolean("isGroup"));
		return msg;
	}
	
	private Document messageToDocument(Message message) {
		Document document = new Document();
		document.append("id", message.getId());
		document.append("sender", message.getSender());
		document.append("reciever", message.getReciever());
		document.append("date", message.getDate());
		document.append("time", message.getTime());
		document.append("content", message.getContent());
		document.append("isGroup", message.getIsGroup());
		return document;
	}

}
