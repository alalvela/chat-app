package ws;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import model.request.RequestMessage;
import model.request.RequestType;


@Stateless
@LocalBean
public class WSMessageParser {
	
    private JsonParser parser = new JsonParser();
    
    public WSMessageParser() {}
    
    
    public RequestMessage parseMessage(String message) {
    	JsonObject jsonObject = parser.parse(message).getAsJsonObject();
    	return new RequestMessage(getRequestType(jsonObject), getLoggedInUsername(jsonObject), getObject(jsonObject));
    }
    
    private String getLoggedInUsername(JsonObject obj) {
    	return obj.get("username").getAsString();
    }
    
    private JsonObject getObject(JsonObject obj) {
		return obj.get("data").getAsJsonObject();
	}

	private RequestType getRequestType(JsonObject obj) {
    	String action = obj.get("action").getAsString();
    	switch(action) {
	    	case "login": return RequestType.LOGIN;
	    	case "logout": return RequestType.LOGOUT;
	    	case "register" : return RequestType.REGISTER;
	    	case "fetchChatData" : return RequestType.CHAT_DATA;
	    	case "fetchConversation" : return RequestType.CONVERSATION_DATA;
	    	case "sendMessage" : return RequestType.SEND_MESSAGE;
	    	case "fetchGroupConversation" : return RequestType.GROUP_CONVERSATION_DATA;
	    	default: return RequestType.UNKNOWN;
    	}
    }
}
