package com.lab2;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.lab2.UserMessage.UserMessageType;

@Controller
public class MainController {

	 @Autowired
	    private SimpMessageSendingOperations messagingTemplate;
	 
	@RequestMapping("/")
	public String index(HttpServletRequest request, Model model) {
		String username = (String) request.getSession().getAttribute("username");

		if (username == null || username.isEmpty()) {
	
			return "redirect:/login";
		}
		model.addAttribute("username", username);
		
		List<User> users = UserRepository.getAllUsers().stream()
				.filter(u->u.getUsername()!=username)
				.sorted()
				
				.collect(Collectors.toList());
		Optional<User> user = UserRepository.getAllUsers().stream().filter(u->u.getUsername()==username)
				.findFirst();
		
		users.add(0, user.get());
		model.addAttribute("users",users);
		return "chat";
	}

	@RequestMapping(path = "/login", method = RequestMethod.GET)
	public String showLoginPage(Model model) {
		return "login.html";
	}

	@RequestMapping(path = "/login", method = RequestMethod.POST)
	public String doLogin(HttpServletRequest request, @RequestParam(defaultValue = "") String username,Model model) {
		username = username.trim();

		if (username.isEmpty() || UserRepository.addUser(username)==false) {
			model.addAttribute("error","User already exists. Please choose different username.");
			return "login";
		}
		request.getSession().setAttribute("username", username);
		
		UserMessage userMessage= new UserMessage();
        userMessage.setUsername(username);
        userMessage.setType(UserMessageType.JOIN);
        
        messagingTemplate.convertAndSend("/topic/usersObserver", userMessage);
        
		UserRepository.printUsers();
		
		return "redirect:/";
	}

	@RequestMapping(path = "/logout")
	    public String logout(HttpServletRequest request) {
	        request.getSession(true).invalidate();
			
	        return "redirect:/login";
	    }
	
/*	@RequestMapping("/check")     

	public String check(HttpServletRequest request, HttpServletResponse response, Model model) {
	    
	    return "hallo";
	}*/
	
}
