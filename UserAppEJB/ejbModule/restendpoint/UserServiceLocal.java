package restendpoint;

import java.util.List;

import dto.Friendship;
import model.User;

public interface UserServiceLocal {
	
	public List<User> getActiveUsers();
		
	public User register(User user);
	
	public User login(User user);
	
	public boolean logout(User user);
	
	public void addFriendship(Friendship friendship);
	
	public void removeFriendship(Friendship friendship);

}
