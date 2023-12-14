import React, { useState, useEffect, useRef } from "react";
import styles from "@chatscope/chat-ui-kit-styles/dist/default/styles.min.css";
import axios from "axios";
import Stomp from "stompjs";
import SockJS from "sockjs-client";
import jwtAxios from "../util/jwtUtil";
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

  const parseUserCookie = () => {
    try {
      const cookieValue = Cookies.get("user");
      return cookieValue ? JSON.parse(decodeURIComponent(cookieValue)) : {};
    } catch (error) {
      console.error("Error parsing user cookie:", error);
      return {};
    }
  };

  const subscribeToTopic = (id) => {
    if (stompClient) {
      const userCookie = parseUserCookie();
      const accessToken = userCookie.accessToken;

      if (accessToken) {
        const headers = {
          Authorization: `Bearer ${accessToken}`,
        };

        stompClient.subscribe(
          `/topic/messages/${id}`,
          (message) => {
            console.log(message.body);
            const receivedMessage = JSON.parse(message.body);
            setMessages((prevMessages) => [...prevMessages, receivedMessage]);
          },
          headers
        );

        setSelectedConversationId(id);
      } else {
        console.error("Access token not found or undefined in the user cookie.");
      }
    }
  };
  useEffect(() => {
    // Fetch data using Axios when the component mounts
    jwtAxios
      .get("http://localhost:8080/api/chatRoom", {})
      .then((response) => {
        setConversations(response.data);
      })
      .catch((error) => {
        console.error("Error fetching data:", error);
      });

    // Connect to WebSocket if not already connected
    if (!stompRef.current) {
      const socket = new SockJS("http://localhost:8080/ws");
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
    subscribeToTopic(id);

    jwtAxios
      .get(`http://localhost:8080/api/chatMessages/${id}`, {})
      .then((response) => {
        setMessages(response.data);
      })
      .catch((error) => {
        console.error("Error fetching chat messages:", error);
      });

    if (messageInputValue.trim() !== "") {
      sendMessage(id);
    }
  };

  const sendMessage = (id) => {
    if (stompClient) {
      const userCookie = parseUserCookie();
      const accessToken = userCookie.accessToken;

      if (accessToken) {
        const headers = {
          Authorization: `Bearer ${accessToken}`,
        };

        const messageObject = {
          content: messageInputValue,
          roomId: id,
        };

        stompClient.send(`/app/chat/${id}`, headers, JSON.stringify(messageObject));
        setMessageInputValue("");
      } else {
        console.error("Access token not found or undefined in the user cookie.");
      }
    } else {
      console.error("WebSocket connection not yet established.");
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
              {conversations.map((conversation) => (
                <Conversation key={conversation.id} name={conversation.recipientName} onClick={() => handleConversationClick(conversation.id)}>
                  <Avatar src={require("../../assets/images/p.png")} name="Lilly" status="available" />
                </Conversation>
              ))}
            </ConversationList>
          </Sidebar>

          <ChatContainer>
            <ConversationHeader>{/* ... */}</ConversationHeader>
            <MessageList>
              {messages.map((msg, index) => (
                <Message
                  key={msg.id}
                  order={index}
                  model={{
                    message: msg.content,
                    sentTime: msg.timestamp,
                    sender: msg.sender,
                    direction: msg.sender === "current_user" ? "outgoing" : "incoming",
                    position: "single",
                  }}
                >
                  <Avatar src={require("../../assets/images/p.png")} name={msg.nickName} />
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
