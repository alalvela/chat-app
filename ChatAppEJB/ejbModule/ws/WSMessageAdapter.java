package ws;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import model.User;
import model.dto.SendMessage;
import model.request.RequestType;


@SuppressWarnings("rawtypes")
public class WSMessageAdapter {
	
	public static final Map<RequestType, Class> requestParameterMap;
	static {
		Map<RequestType, Class> temp = new HashMap<>();
		temp.put(RequestType.LOGIN, User.class);
		temp.put(RequestType.LOGOUT, User.class);
		temp.put(RequestType.REGISTER, User.class);	
		temp.put(RequestType.CONVERSATION_DATA, User.class);
		temp.put(RequestType.SEND_MESSAGE, SendMessage.class);
		requestParameterMap = Collections.unmodifiableMap(temp);
	}
	
	public static Object getRequestParameter(JsonObject obj, RequestType requestType) {
		return new Gson().fromJson(obj, requestParameterMap.get(requestType));
	}
	
}
