package util;

import java.io.InputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import model.Host;


@Singleton
@LocalBean
@Startup
public class ChatAppPropertiesBean {
	
	private Properties props;
	private Host master;
	private Host local;
	private String userAppName;
	private String chatAppName;
	
	@PostConstruct
    private void startup() {
		try {
			InputStream propsStream = getClass().getClassLoader().getResourceAsStream("app.properties");
            props = new Properties();
            props.load(propsStream);
            
            String ipAddress = props.getProperty("ip-address", "localhost");
            String masterPort = props.getProperty("port-master", "8080");
            String portOffset = props.getProperty("port-offset");
            
            int masterPortInt = Integer.valueOf(masterPort);
            int portOffsetInt = Integer.valueOf(portOffset);
            
            String masterAlias = props.getProperty("master-alias");
            String localAlias = props.getProperty("local-alias");
            
            userAppName = props.getProperty("user-app-name", "UserAppWeb");
            chatAppName = props.getProperty("chat-app-name", "ChatAppWeb");
            
            master = new Host(ipAddress + ":" + masterPort, masterAlias);
            local = new Host(ipAddress + ":" + (masterPortInt+portOffsetInt), localAlias);
		} catch(Exception e) {
		    throw new EJBException("ChatAppHostBean initialization error", e);
	    }
		System.out.println("MASTER ADDRESS FROM PROPERTY FILE (or not) : " + master.getAddress());
    }
	
	public Host getMasterHost() {
		return master;
	}
	
	public Host getLocalHost() {
		return local;
	}

	public String getUserAppName() {
		return userAppName;
	}

	public void setUserAppName(String userAppName) {
		this.userAppName = userAppName;
	}

	public String getChatAppName() {
		return chatAppName;
	}

	public void setChatAppName(String chatAppName) {
		this.chatAppName = chatAppName;
	}
	
	

}


