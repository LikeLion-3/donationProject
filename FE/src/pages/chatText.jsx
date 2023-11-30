import React, { useState, useEffect } from "react";
import Stomp from "stompjs";
import SockJS from "sockjs-client";
import { Button, ListGroup, ListGroupItem, Image, Form, Row, Col } from "react-bootstrap";

function ChatText(params) {
  const [messages, setMessages] = useState([]);
  const [message, setMessage] = useState("");
  const [nickname, setNickname] = useState("");
  const [stompClient, setStompClient] = useState(null);

  useEffect(() => {
    const socket = new SockJS("http://localhost:9000/ws");
    const client = Stomp.over(socket);

    client.connect([], () => {
      if (stompClient) {
        console.log("Already connected");
      }

      client.subscribe("/topic/messages", (message) => {
        const receiverMessage = JSON.parse(message.body);
        setMessages((prevMessages) => [...prevMessages, receiverMessage]);
      });

      setStompClient(client);
    });

    return () => {
      console.log("Disconnecting");
      client.disconnect();
    };
  }, [stompClient]);

  const handleNicknameChange = (e) => {
    setNickname(e.target.value);
  };

  const handleMessageChange = (e) => {
    setMessage(e.target.value);
  };

  const sendMessage = () => {
    if (message.trim() && stompClient && stompClient.connected) {
      const chatMessage = {
        nickname,
        content: message,
      };

      stompClient.send("/app/chat", {}, JSON.stringify(chatMessage));
      setMessage("");
    } else {
      console.error("WebSocket connection not open.");
    }
  };

  return (
    <div>
      <ListGroup>
        {messages.map((msg, index) => (
          <ListGroupItem key={index}>
            <Image src={`https://via.placeholder.com/40`} roundedCircle /> {/* Replace with actual avatar */}
            <div>
              <strong>{msg.nickname || "Unknown"}</strong>
              <p>{msg.content}</p>
            </div>
          </ListGroupItem>
        ))}
      </ListGroup>
      <Row className="mt-3">
        <Col>
          <Form.Control id="nickname" placeholder="Nickname" value={nickname} onChange={handleNicknameChange} />
        </Col>
        <Col>
          <Form.Control id="message" placeholder="Message" value={message} onChange={handleMessageChange} />
        </Col>
        <Col>
          <Button variant="primary" onClick={sendMessage} disabled={!message.trim()}>
            Send
          </Button>
        </Col>
      </Row>
    </div>
  );
}

export default ChatText;
