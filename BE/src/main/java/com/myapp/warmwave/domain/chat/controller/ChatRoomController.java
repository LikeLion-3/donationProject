package com.myapp.warmwave.domain.chat.controller;

import com.myapp.warmwave.domain.chat.dto.ChatRoomDto;
import com.myapp.warmwave.domain.chat.dto.ResponseChatRoomDto;
import com.myapp.warmwave.domain.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatRoomController {

    private final ChatRoomService chatroomService;

    @PostMapping("/chatRoom")
    public ResponseEntity<ResponseChatRoomDto> createRoom(@RequestBody ChatRoomDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(chatroomService.createChatRoom(requestDto));
    }

    @GetMapping("/chatRoom")
    public ResponseEntity<List<ResponseChatRoomDto>> getChatRoomList() {
        return ResponseEntity.status(HttpStatus.OK).body(chatroomService.selectChatRoomList());
    }

    @DeleteMapping("/chatRoom/{roomId}")
    public void deleteRoom(@PathVariable("roomId") Long roomId) {
        chatroomService.deleteChatRoom(roomId);
    }
}
