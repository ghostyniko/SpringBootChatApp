'use strict';
 
 
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var messageBody = document.querySelector('#body');
var usersBody = document.querySelector('#usersArea')

//var connectingElement = document.querySelector('#connecting');
 
var stompClient = null;
var username = null;
  

//Connect to WebSocket Server.
connect();

function connect() {
    username = document.querySelector('#username').innerText.trim();
      
    //var socket = new SockJS('/ws');
    //var socket = new WebSocket('/ws');
    //stompClient = Stomp.client("ws://localhost:8080/ws");
    
  //  var websocket = "ws://localhost:8080/ws"
    //var stompClient = Stomp.client(url)
 
   // stompClient.connect({}, onConnected, onError);
    
    var socket = new WebSocket('ws://192.168.1.100:8080/ws');
	stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnected, onError);

	
}
 



function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/topic/publicChatRoom', onMessageReceived);
 
    // Subscribe to User Observer
    stompClient.subscribe('/topic/usersObserver', onUserObserverReceived);
    
   
    stompClient.send("/app/chat.addUser",
        {},
        JSON.stringify({sender: username, type: 'JOIN'})
    )
    
    var topic = '/topic/private/';
    topic = topic.concat(username);
    stompClient.subscribe(topic,onMessageReceivedPrivate);
   
     
}
 
 
function onError(error) {
  //  connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
  //  connectingElement.style.color = 'red';
}
 

function onUserObserverReceived(payload){
	
	var message = JSON.parse(payload.body);
	if (message.type === 'LEAVE'){
		var userId = message.username;
		
		var el = document.getElementById(userId);
		el.parentNode.removeChild(el);
	}
	
	else if (message.type == 'JOIN'){
		var userId = message.username;
		var userElement = document.createElement('div');
		userElement.id = userId;
		
		userElement.classList.add('list-group-item');
		userElement.classList.add('btn');
		userElement.classList.add('btn-primary');
		userElement.addEventListener('click',sendMessagePrivate.bind(null,userId),true)
		
		var textElement = document.createElement('h4');
	    var userText = document.createTextNode(userId);
	  
	    textElement.appendChild(userText);
	 
	    userElement.appendChild(textElement);
	 
	    usersBody.appendChild(userElement);
	 
	}

}

function sendMessage(event) {
    var messageContent = messageInput.value.trim();
    if(messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageInput.value,
        };
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}
 
 
function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);
 
    var messageElement = document.createElement('div');
    messageElement.classList.add('list-group-item');
    messageElement.setAttribute('style','max-width:80%');
    
    messageElement.classList.add('m-3');
    messageElement.classList.add('text-light');
    
    if (message.sender===username){
		messageElement.classList.add('align-self-end'); 
    	messageElement.classList.add('bg-success');
	}
	else{
		messageElement.classList.add('align-self-start'); 
    	messageElement.classList.add('bg-primary');
	}
    
    
    messageElement.classList.add('rounded');
    
    var usernameElement = document.createElement('h5');
       
    var usernameText = document.createTextNode(message.sender);
    var usernameText = document.createTextNode(message.sender);
    
    usernameElement.appendChild(usernameText);
    messageElement.appendChild(usernameElement);
    
   
 
    var textElement = document.createElement('h5');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);
 
    messageElement.appendChild(textElement);
 
    messageArea.appendChild(messageElement);
  //  messageArea.scrollTop = messageArea.scrollHeight;
    messageBody.scrollTop = messageBody.scrollHeight;
}
  
function onMessageReceivedPrivate(payload) {
    var message = JSON.parse(payload.body);
    
    var messageElement = document.createElement('div');
    messageElement.classList.add('list-group-item');
     
    var usernameElement = document.createElement('h2');
       
    var usernameText = document.createTextNode(message.sender.concat('->').concat(message.receiver));
    //var usernameText = document.createTextNode(message.sender);
    usernameElement.appendChild(usernameText);
    messageElement.appendChild(usernameElement);
    
   
 
    var textElement = document.createElement('h5');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);
 
    messageElement.appendChild(textElement);
 
    messageArea.appendChild(messageElement);
  //  messageArea.scrollTop = messageArea.scrollHeight;
    messageBody.scrollTop = messageBody.scrollHeight;
}

function sendMessagePrivate(name){
	var messageContent = messageInput.value.trim();
	//var userToSend = event.target.myParam;
	alert(name);
    if(messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageInput.value,
            receiver:name,
        };
        stompClient.send("/app/chat.sendMessagePrivate", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    //event.preventDefault();
}
  
messageForm.addEventListener('submit', sendMessage, true);


var usersList = document.getElementById('usersArea').children;
var s = "";
//alert(usersList.length)
for (var i = 0; i < usersList.length; i++) {
	
	if (usersList[i].id==username){
		usersList[i].classList.add('btn');
		usersList[i].classList.add('btn-success');
	}
	else{
		usersList[i].classList.add('btn');
		usersList[i].classList.add('btn-primary');
		usersList[i].addEventListener('click',sendMessagePrivate.bind(null,usersList[i].id),true)
		usersList[i].myParam = usersList[i].id;
	}
	

}



