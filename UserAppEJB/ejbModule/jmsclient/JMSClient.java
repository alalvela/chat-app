package jmsclient;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.google.gson.Gson;

import model.jms.ActiveUserMessageJMS;

/**
 * Session Bean implementation class JMSClient
 */
@Stateless
@LocalBean
public class JMSClient implements MessageListener {
	
	private final String ACTIVE_USERS_QUEUE_URI = "jms/queue/TestQueue";
	
	private ConnectionFactory cf;
	private Connection connection;
	private Queue activeUsersQueue;
	private Session session;
	
	
	@PostConstruct
	private void init() throws NamingException, JMSException {
		Context context = new InitialContext();
		cf = (ConnectionFactory) context.lookup("jms/RemoteConnectionFactory");
		activeUsersQueue = (Queue) context.lookup(ACTIVE_USERS_QUEUE_URI);
		// ... dodaj jos queueova ako treba ovdje
		
		context.close();
		connection = cf.createConnection();
		session = connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);
		connection.start();
	}
	
	@PreDestroy
	private void destroy() throws JMSException {
		connection.stop();
		session.close();
	}
	
	public void sendModifyActiveUserMessage(ActiveUserMessageJMS message) throws JMSException {
		//generate message to chatapp
		String userJson = new Gson().toJson(message.getUser());
		
		MessageConsumer consumer = session.createConsumer(activeUsersQueue);
		consumer.setMessageListener(this);
		
	    TextMessage msg = session.createTextMessage();
	    msg.setStringProperty("action", message.getAction());
	    msg.setStringProperty("user", userJson);
	    
	    MessageProducer producer = session.createProducer(activeUsersQueue);
		producer.send(msg);
		System.out.println("Message published. Please check application server's console to see the response from MDB.");

		producer.close();
		consumer.close();
	}

   
	public void sendAddActiveUserMessage(ActiveUserMessageJMS message) throws JMSException {
		//generate message to chatapp
		String userJson = new Gson().toJson(message.getUser());
		
		MessageConsumer consumer = session.createConsumer(activeUsersQueue);
		consumer.setMessageListener(this);
		
	    TextMessage msg = session.createTextMessage();
	    msg.setStringProperty("action", message.getAction());
	    msg.setStringProperty("user", userJson);
	    
	    MessageProducer producer = session.createProducer(activeUsersQueue);
		producer.send(msg);
		System.out.println("Message published. Please check application server's console to see the response from MDB.");

		producer.close();
		consumer.close();
	}
	
	public void sendRemoveActiveUserMessage(ActiveUserMessageJMS message) throws JMSException {
		MessageConsumer consumer = session.createConsumer(activeUsersQueue);
		consumer.setMessageListener(this);

	    TextMessage msg = session.createTextMessage("Queue message : " + message);
	  
	    MessageProducer producer = session.createProducer(activeUsersQueue);
		producer.send(msg);
		System.out.println("Message published. Please check application server's console to see the response from MDB.");

		producer.close();
		consumer.close();
	}
	
	@Override
	public void onMessage(Message arg0) {
		// TODO Auto-generated method stub
		
	}

}
