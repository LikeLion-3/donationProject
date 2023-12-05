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

  // Move subscribeToTopic outside of useEffect
  const subscribeToTopic = (id) => {
    if (stompClient) {
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
      .get("http://localhost:9000/api/chatRoom", {
        headers: {
          // Authorization:
          //   "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBY2Nlc3NUb2tlbiIsImJvZHkiOnsiZW1haWwiOiJhc2pzMTIzNEBuYXZlci5jb20ifSwiZXhwIjoxNzAxNzQ0MDk4fQ.cmYEt3t0dM0Dl8zOma4EtAoxtDDJdxOaohYg0LoBApWjRizxjAR5nuqYDVJHC8z0WLzR8Vs2ah3f_lLIUaxdMQ",
        },
      })
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
    // 선택한 대화 ID를 설정하고 해당 토픽을 구독합니다.
    // 그리고 선택한 대화에 대한 채팅 메시지를 가져옵니다.
    subscribeToTopic(id);

    axios
      .get(`http://localhost:9000/api/chatMessages/${id}`, {
        headers: {
          // 필요한 경우 헤더를 추가합니다.
        },
      })
      .then((response) => {
        // 가져온 메시지로 messages 상태를 업데이트합니다.
        setMessages(response.data);
      })
      .catch((error) => {
        console.error("채팅 메시지를 가져오는 중 오류 발생:", error);
      });

    // 메시지 입력값이 비어있지 않은 경우에만 메시지를 보냅니다.
    if (messageInputValue.trim() !== "") {
      sendMessage(id);
    }
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
              {messages.map((msg) => (
                <Message
                  key={msg.id} // Use the 'id' property as the key
                  model={{
                    message: msg.message,
                    sentTime: msg.timestamp,
                    sender: msg.nickName,
                    direction: "incoming",
                    position: "single",
                  }}
                >
                  <Avatar src={require("../assets/images/ram.png")} name={msg.nickName} />
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
