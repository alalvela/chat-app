package ws;

import model.Host;
import model.request.RequestMessage;
import model.request.ResponseMessage;

public interface RequestSendingStrategyI {
	
	public ResponseMessage sendLoginRequest(RequestMessage rm, Host host);
	
	public ResponseMessage sendLogoutRequest(RequestMessage rm);

	public ResponseMessage sendRegisterRequest(RequestMessage rm);
	
	public ResponseMessage fetchChatData(RequestMessage rm);
	
	public ResponseMessage fetchConversationData(RequestMessage rm);
	
	public ResponseMessage fetchGroupConversationData(RequestMessage rm);

}
