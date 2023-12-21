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
  const socket = useRef(null);
  const stompClient = useRef(null);
  const [selectedConversation, setSelectedConversation] = useState({});
  const [articleId, setArticleId] = useState([]);
  const cookieValue = Cookies.get("user");
  const userObject = JSON.parse(cookieValue);
  const userId = userObject.id;
  const userName = userObject.name;

  const connectToWebSocket = () => {
    if (!stompClient.current) {
      socket.current = new SockJS(`${API_SERVER_HOST}/ws`);
      stompClient.current = Stomp.over(socket.current);

      stompClient.current.connect({}, () => {
        // 연결 후 추가 로직이 필요한 경우에 추가합니다.
      });
    }
  };

  const subscribeToTopic = (id, articleId) => {
    if (stompClient.current) {
      if (userId) {
        stompClient.current.subscribe(`/topic/messages/${id}`, (message) => {
          console.log(message.body);
          const receivedMessage = JSON.parse(message.body);
          setMessages((prevMessages) => [...prevMessages, receivedMessage]);
        });

        // Update selectedConversation with both id and articleId
        setSelectedConversation({ ...selectedConversation, id, articleId });
      } else {
        console.error("Access token not found or undefined in the user cookie.");
      }
    }
  };

  useEffect(() => {
    // Axios를 사용하여 데이터를 가져옵니다.
    jwtAxios
      .get(`${API_SERVER_HOST}/api/chatRoom`)
      .then((response) => {
        setConversations(response.data);
        setArticleId(response.data);
      })
      .catch((error) => {
        console.error("Error fetching data:", error);
      });

    // WebSocket에 연결합니다.
    connectToWebSocket();

    return () => {
      // 언마운트 시 연결을 끊습니다.
      if (stompClient.current && stompClient.current.connected) {
        stompClient.current.disconnect();
      }
    };
  }, []);

  const handleConversationClick = (conversation) => {
    if (conversation.id === selectedConversation.id) {
      return; // 이미 선택된 채팅방인 경우 처리 중단
    }

    setSelectedConversation(conversation);

    // Pass the articleId to subscribeToTopic function
    subscribeToTopic(conversation.id, conversation.articleId);

    jwtAxios
      .get(`${API_SERVER_HOST}/api/chatMessages/${conversation.id}`, {})
      .then((response) => {
        setMessages(response.data);
      })
      .catch((error) => {
        console.error("Error fetching chat messages:", error);
      });

    if (messageInputValue.trim() !== "") {
      sendMessage(conversation.id);
    }
  };
  const sendMessage = (id) => {
    if (stompClient.current && userId) {
      const messageObject = {
        roomId: id,
        userId: userId,
        sender: userName,
        content: messageInputValue,
      };

      stompClient.current.send(`/app/chat/${id}`, {}, JSON.stringify(messageObject));

      setMessageInputValue("");
    } else {
      console.error("WebSocket connection not yet established or user ID is missing.");
    }
  };

  const handleArticleStatusButtonClick = async (articleStatus, articleId) => {
    try {
      console.log("Sending request with payload:", { articleStatus, articleId });

      if (articleId) {
        const response = await jwtAxios.put(`${API_SERVER_HOST}/api/articles/status/${articleId}?articleStatus=${articleStatus}`, {
          articleStatus: articleStatus,
        });

        console.log("API Response:", response.data);
      } else {
        console.error("No conversationId available.");
      }
    } catch (error) {
      console.error("API Error:", error);
    }
  };

  return (
    <div>
      <div
        style={{
          height: "650px",
          position: "relative",
        }}
      >
        <MainContainer responsive>
          <Sidebar position="left" scrollable={false}>
            <Search placeholder="Search..." />
            <ConversationList>
              {conversations.map((conversation) => (
                <Conversation key={conversation.id} name={conversation.articleTitle} info={conversation.lastMessage} onClick={() => handleConversationClick(conversation)}>
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
                  index={index}
                  model={{
                    message: msg.content,
                    sentTime: msg.timestamp,
                    sender: msg.sender,
                    direction: msg.sender === userName ? "outgoing" : "incoming",
                    position: "single",
                  }}
                >
                  {msg.sender !== userName && <Avatar src={require("../../assets/images/p.png")} name={msg.sender} />}
                </Message>
              ))}
              <div>
                <button
                  className="btn btn-secondary me-2"
                  type="button"
                  onClick={() => handleArticleStatusButtonClick("완료", selectedConversation.articleId)}
                  style={{
                    backgroundColor: "#ffffff",
                    borderColor: "#999999",
                    color: "#999999",
                  }}
                >
                  기부완료
                </button>
                <button
                  className="btn btn-secondary me-2"
                  type="button"
                  onClick={() => handleArticleStatusButtonClick("기본", selectedConversation.articleId)}
                  style={{
                    backgroundColor: "#ffffff",
                    borderColor: "#999999",
                    color: "#999999",
                  }}
                >
                  기부중단
                </button>
              </div>
            </MessageList>

            <MessageInput
              placeholder="Type message here"
              value={messageInputValue}
              onChange={(val) => setMessageInputValue(val)}
              onSend={() => selectedConversation?.id && sendMessage(selectedConversation.id)}
            />
          </ChatContainer>
        </MainContainer>
      </div>
    </div>
  );
}

export default ChatMain;
