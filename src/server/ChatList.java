package server;

import java.util.HashMap;

public class ChatList {
	HashMap<String,User> chatlist = new HashMap<String,User>();
	public ChatList() {
		chatlist.clear();
	}
	public boolean add(User user) {
		if(chatlist.putIfAbsent(user.toString(), user)!=null) {
			return false;
		}
		return true;
	}
	
}
