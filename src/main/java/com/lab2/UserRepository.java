package com.lab2;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class UserRepository {
	private static Set<User> users = new HashSet<User>();
	
	public static boolean addUser(String username)
	{
		return users.add(new User(username));
	}
	
	public static void printUsers()
	{
		for (User u:users)
		{
			System.out.println(u);
		}
	}
	
	public static void deleteUser(String username)
	{
		Optional<User> userOpt = users.stream().filter(u -> u.getUsername().equals(username) ).findFirst();
	
		users.remove(userOpt.get());
	}
	
	public static Collection<User> getAllUsers()
	{
		return users;
	}
	
}
