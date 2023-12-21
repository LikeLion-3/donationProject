import React, { useState, useEffect, useRef } from "react";
import styles from "@chatscope/chat-ui-kit-styles/dist/default/styles.min.css";
import Stomp from "stompjs";
import SockJS from "sockjs-client";
import jwtAxios, { API_SERVER_HOST } from "../util/jwtUtil";
import Cookies from "js-cookie";
import {
  MainContainer,
  Sidebar,
  Search,
  ConversationList,
  Conversation,
  Avatar,
  ChatContainer,
  ConversationHeader,
  VoiceCallButton,
  Message,
  MessageInput,
  VideoCallButton,
  InfoButton,
  MessageList,
} from "@chatscope/chat-ui-kit-react";

function ChatMain() {
  const [messageInputValue, setMessageInputValue] = useState("");
  const [conversations, setConversations] = useState([]);
  const [messages, setMessages] = useState([]);
  const [stompClient, setStompClient] = useState(null);
  const [selectedConversationId, setSelectedConversationId] = useState(null);

  const stompRef = useRef(null);

  const cookieValue = Cookies.get("user");
  const userObject = JSON.parse(cookieValue);
  const userId = userObject.id;
  const userName = userObject.name;

  const subscribeToTopic = (id) => {
    if (stompClient) {
      if (userId) {
        stompClient.subscribe(`/topic/messages/${id}`, (message) => {
          console.log(message.body);
          const receivedMessage = JSON.parse(message.body);
          setMessages((prevMessages) => [...prevMessages, receivedMessage]);
        });

        setSelectedConversationId(id);
      } else {
        console.error("Access token not found or undefined in the user cookie.");
      }
    }
  };
  useEffect(() => {
    // Fetch data using Axios when the component mounts
    jwtAxios
      .get(`${API_SERVER_HOST}/api/chatRoom`)
      .then((response) => {
        setConversations(response.data);
      })
      .catch((error) => {
        console.error("Error fetching data:", error);
      });

    // Connect to WebSocket if not already connected
    if (!stompRef.current) {
      const socket = new SockJS(`${API_SERVER_HOST}/ws`);
      const stomp = Stomp.over(socket);

      stomp.connect({}, () => {
        setStompClient(stomp);
      });

      stompRef.current = stomp; // Store the stomp instance in the ref
    }

    return () => {
      // Disconnect on unmount
      if (stompRef.current && stompRef.current.connected) {
        stompRef.current.disconnect();
      }
    };
  }, []);

  const handleConversationClick = (id) => {
    // Set the selected conversation ID
    setSelectedConversationId(id);

    // Subscribe to the WebSocket topic for real-time updates
    subscribeToTopic(id);

    // Fetch chat messages for the selected conversation
    jwtAxios
      .get(`${API_SERVER_HOST}/api/chatMessages/${id}`, {})
      .then((response) => {
        setMessages(response.data);
      })
      .catch((error) => {
        console.error("Error fetching chat messages:", error);
      });

    // If there is a message in the input, send it
    if (messageInputValue.trim() !== "") {
      sendMessage(id);
    }
  };

  const sendMessage = (id) => {
    if (stompClient && userId) {
      const messageObject = {
        roomId: id,
        userId: userId,
        sender: userName,
        content: messageInputValue,
      };

      // Send the message to the server
      stompClient.send(`/app/chat/${id}`, {}, JSON.stringify(messageObject));

      // Update the state only after the message has been sent
      setMessageInputValue("");
    } else {
      console.error("WebSocket connection not yet established or user ID is missing.");
    }
  };

  return (
    <div>
      <div
        style={{
          height: "600px",
          position: "relative",
        }}
      >
        <MainContainer responsive>
          <Sidebar position="left" scrollable={false}>
            <Search placeholder="Search..." />
            <ConversationList>
              {conversations.map((conversation) => {
                // Determine the name to display based on the condition
                const displayUserName = userName !== conversation.donorName ? conversation.donorName : conversation.recipientName;

                // Render Conversation with the determined displayUserName
                return (
                  <Conversation key={conversation.id} name={conversation.articleTitle} info={conversation.lastMessage} onClick={() => handleConversationClick(conversation.id)}>
                    <Avatar src={require("../../assets/images/p.png")} name="Lilly" status="available" />
                  </Conversation>
                );
              })}
            </ConversationList>
          </Sidebar>

          <ChatContainer>
            <ConversationHeader>{/* ... */}</ConversationHeader>
            <MessageList>
              {messages.map((msg, index) => (
                <Message
                  key={msg.id}
                  index={index}
                  model={{
                    message: msg.content,
                    sentTime: msg.timestamp,
                    sender: msg.sender,
                    direction: msg.sender === userName ? "outgoing" : "incoming",
                    position: "single",
                  }}
                >
                  {msg.sender !== userName && <Avatar src={require("../../assets/images/p.png")} name={msg.nickName} />}
                </Message>
              ))}
            </MessageList>
            <MessageInput placeholder="Type message here" value={messageInputValue} onChange={(val) => setMessageInputValue(val)} onSend={() => sendMessage(selectedConversationId)} />
          </ChatContainer>
        </MainContainer>
      </div>
    </div>
  );
}
export default ChatMain;
