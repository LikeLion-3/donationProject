import React, { useState, useEffect } from "react";
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

function ChatMain(params) {
  const [messageInputValue, setMessageInputValue] = useState("");
  const [conversations, setConversations] = useState([]);

  const [messages, setMessages] = useState([]);
  const [message, setMessage] = useState("");
  const [nickname, setnickname] = useState("");
  const [stompClient, setStompClient] = useState(null);

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
  }, []);
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
            <ConversationList conversations={conversations}>
              {conversations.map((conversation) => {
                return (
                  <Conversation key={conversation.id} name={conversation.recipientName} info={` 안녕하세요`}>
                    <Avatar src={require("../assets/images/ram.png")} name="Lilly" status="available" />
                  </Conversation>
                );
              })}

              {/* <Conversation name="핫팩 기부" lastSenderName="서예린" info="안녕하세요ㅎㅎ" style={{ justifyContent: "start" }}>
                <Avatar src={require("../assets/images/ram.png")} name="Lilly" status="available" />
              </Conversation>
              <Conversation name="목도리 기부" lastSenderName="홍길동" info="안녕하세요!">
                <Avatar src={require("../assets/images/ram.png")} name="Joe" status="dnd" />
              </Conversation> */}
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
              <Message
                model={{
                  message: "안녕하세요 기부 받고 싶습니다",
                  sentTime: "15 mins ago",
                  sender: "Zoe",
                  direction: "incoming",
                  position: "single",
                }}
              >
                <Avatar src={require("../assets/images/ram.png")} name="Zoe" />
              </Message>

              <Message
                model={{
                  message: "안녕하세요",
                  sentTime: "15 mins ago",
                  sender: "Patrik",
                  direction: "outgoing",
                  position: "single",
                }}
                avatarSpacer
              />
            </MessageList>
            <MessageInput placeholder="Type message here" value={messageInputValue} onChange={(val) => setMessageInputValue(val)} onSend={() => setMessageInputValue("")} />
          </ChatContainer>
        </MainContainer>
      </div>
    </div>
  );
}

export default ChatMain;
