package ws;

import model.Host;
import model.request.RequestMessage;
import model.request.ResponseMessage;

public class JMSRequestStrategy implements RequestSendingStrategyI {
	
	//ovdje treba nekako da se injektuje onaj jms sender

	@Override
	public ResponseMessage sendLoginRequest(RequestMessage rm, Host host) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseMessage sendLogoutRequest(RequestMessage rm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseMessage sendRegisterRequest(RequestMessage rm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseMessage fetchChatData(RequestMessage rm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseMessage fetchConversationData(RequestMessage rm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseMessage fetchGroupConversationData(RequestMessage rm) {
		// TODO Auto-generated method stub
		return null;
	}

}
