import React, { useState, useEffect, useRef } from "react";
import styles from "@chatscope/chat-ui-kit-styles/dist/default/styles.min.css";
import axios from "axios";
import Stomp from "stompjs";
import SockJS from "sockjs-client";
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

  const subscribeToTopic = (id) => {
    if (stompClient) {
      stompClient.unsubscribe(`/topic/messages/${selectedConversationId}`);
      stompClient.subscribe(`/topic/messages/${id}`, (message) => {
        const receivedMessage = JSON.parse(message.body);
        setMessages((prevMessages) => [...prevMessages, receivedMessage]);
      });
      setSelectedConversationId(id);
    }
  };

  useEffect(() => {
    // Fetch data using Axios when the component mounts
    axios
      .get("http://localhost:9000/api/chatRoom")
      .then((response) => {
        setConversations(response.data);
      })
      .catch((error) => {
        console.error("Error fetching data:", error);
      });

    // Connect to WebSocket if not already connected
    if (!stompRef.current) {
      const socket = new SockJS("http://localhost:9000/ws");
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
    // Set the selected conversation ID, subscribe to the corresponding topic, and send a message
    subscribeToTopic(id);
    sendMessage(id);
  };

  const sendMessage = (id) => {
    if (stompClient) {
      const messageObject = {
        content: messageInputValue,
        roomId: id, // Use the clicked conversation's id as the roomId
      };

      stompClient.send("/app/chat", {}, JSON.stringify(messageObject));
      setMessageInputValue("");
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
              {conversations.map((conversation) => {
                return (
                  <Conversation key={conversation.id} name={conversation.recipientName} onClick={() => handleConversationClick(conversation.id)}>
                    <Avatar src={require("../assets/images/ram.png")} name="Lilly" status="available" />
                  </Conversation>
                );
              })}
            </ConversationList>
          </Sidebar>

          <ChatContainer>
            <ConversationHeader>
              <ConversationHeader.Back />
              <Avatar src={require("../assets/images/ram.png")} name="Zoe" />
              <ConversationHeader.Content userName="Zoe" info="Active 10 mins ago" />
              <ConversationHeader.Actions>
                <VoiceCallButton />
                <VideoCallButton />
                <InfoButton />
              </ConversationHeader.Actions>
            </ConversationHeader>
            <MessageList>
              {messages.map((msg, index) => (
                <Message
                  key={index}
                  model={{
                    message: msg.content,
                    sentTime: "just now",
                    sender: msg.sender,
                    direction: msg.sender === "current_user" ? "outgoing" : "incoming",
                    position: "single",
                  }}
                >
                  <Avatar src={require("../assets/images/ram.png")} name={msg.sender} />
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
