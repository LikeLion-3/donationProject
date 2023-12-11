import React, { useState, useEffect, useRef } from "react";
import "./ChatSide.css"; // Import your CSS file for styling
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

const ChatSide = () => {
  const [isChatOpen, setChatOpen] = useState(false);

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

  const toggleChat = () => {
    const postData = {
      articleId: "2",
      donorId: "11",
      recipientId: "9",
    };

    axios
      .post("http://localhost:9000/api/chatRoom", postData)
      .then((response) => {
        console.log("포스트 요청 성공:", response.data);

        // 응답에 상태를 포함하여 채팅방의 업데이트된 세부 정보를 포함한다고 가정
        const updatedChatRoom = response.data;

        // 업데이트된 채팅방 목록을 가져오거나 상태를 업데이트해야하는 경우

        // 다른 요청을 보내 상태를 업데이트합니다
        return axios.get("http://localhost:9000/api/chatRoom/", updateStatusData);
      })
      .then((updateStatusResponse) => {
        console.log("상태 업데이트 성공:", updateStatusResponse.data);

        // 최종적으로 채팅방 목록을 가져오거나 상태를 업데이트합니다

        setChatOpen(!isChatOpen);
      })
      .catch((error) => {
        console.error("포스트 요청 중 오류 발생:", error);
      });
  };

  const closeChat = () => {
    setChatOpen(false);
    // Optionally, you can also reset other state values or perform other cleanup here
  };

  return (
    <div>
      <div
        style={{
          height: "600px",
          position: "relative",
        }}
      >
        <button id="chat-button" onClick={toggleChat}>
          Chat
        </button>
        {isChatOpen && (
          <MainContainer responsive style={{ width: "600px", right: "0", position: "fixed", bottom: "0" }}>
            <button className="close-button" onClick={closeChat} style={{ marginTop: "70PX", height: "30px" }}>
              X
            </button>
            <Sidebar position="left" scrollable={false}>
              <Search placeholder="Search..." />
              <ConversationList>
                {conversations.map((conversation) => {
                  return (
                    <Conversation
                      key={conversation.id}
                      name={conversation.articleTitle}
                      lastSenderName={conversation.recipientName}
                      info={conversation.lastMessage}
                      onClick={() => handleConversationClick(conversation.id)}
                    >
                      <Avatar src={require("../../assets/images/p.png")} name="Lilly" />
                    </Conversation>
                  );
                })}
              </ConversationList>
            </Sidebar>
            <ChatContainer>
              <ConversationHeader>
                <ConversationHeader.Back />
                <Avatar src={require("../../assets/images/p.png")} name="Zoe" />
                <ConversationHeader.Content userName="Zoe" info="Active 10 mins ago" />
                <ConversationHeader.Actions>
                  <VoiceCallButton />
                  <VideoCallButton />
                  <InfoButton />
                </ConversationHeader.Actions>
              </ConversationHeader>
              <MessageList>
                <Message key="placeholder" model={{ message: "채팅방을 선택해주세요" }} />

                {messages.map((msg, index) => (
                  <Message
                    key={msg.id} // 각 메시지에 대해 고유한 키를 제공
                    order={index}
                    model={{
                      message: msg.content,
                      sentTime: msg.timestamp,
                      sender: msg.sender,
                      direction: msg.sender === "서예린" ? "outgoing" : "incoming",
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
        )}
      </div>
    </div>
  );
};

export default ChatSide;
