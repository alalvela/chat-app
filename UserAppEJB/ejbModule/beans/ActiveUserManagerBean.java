package beans;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;

import com.google.gson.Gson;

import model.Host;
import model.User;

/**
 * Session Bean implementation class ActiveUserManagerBean
 */
@Singleton
@LocalBean
public class ActiveUserManagerBean implements ActiveUserManagerLocal {
	
    private List<User> activeUsers;
    
    @PostConstruct
    private void init() {
    	activeUsers = new ArrayList<>();
    }
	
    @Lock(LockType.READ)
	public List<User> getActiveUsers() {
		return activeUsers;
	}
	
	
	@Lock(LockType.READ)
	public boolean checkIfActive(User user) {
		Iterator<User> it = activeUsers.iterator();
		while(it.hasNext()) {
			if (user.getUsername().equals(it.next().getUsername())) {
				return true;
			}
		}
		return false;
	}
	
	@Lock(LockType.WRITE)
	public boolean addUserToActive(User user) {		
		activeUsers.add(user);
		System.out.println(new Gson().toJson(activeUsers));
		//neki exceptioni?
		return true;
	}

	@Lock(LockType.WRITE)
	public boolean removeUserFromActive(User user) {
		Iterator<User> it = activeUsers.iterator();
		while(it.hasNext()) {
			if (user.getUsername().equals(it.next().getUsername())) {
				it.remove();
				System.out.println(new Gson().toJson(activeUsers));
				return true;
			}
		}
		return false;
	}


}
