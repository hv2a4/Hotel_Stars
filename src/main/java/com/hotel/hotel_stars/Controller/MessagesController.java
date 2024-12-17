package com.hotel.hotel_stars.Controller;

import com.hotel.hotel_stars.Models.MessageModel;
import com.hotel.hotel_stars.Service.MessageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/messages")
public class MessagesController {

    @Autowired
    private MessageService messageService;

    // Lấy tất cả tin nhắn
    @GetMapping("getAll")
    public ResponseEntity<List<MessageModel>> getAllMessages() {
        Optional<List<MessageModel>> messages = messageService.getMessages();
        return messages.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    // Lấy tin nhắn theo ID
    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<MessageModel> getMessageById(@PathVariable Integer id) {
        MessageModel message = messageService.getMessageById(id);
        if (message != null) {
            return ResponseEntity.ok(message); // Trả về 200 OK nếu tìm thấy tin nhắn
        } else {
            return ResponseEntity.notFound().build(); // Trả về 404 Not Found nếu không tìm thấy
        }
    }


    // Lưu tin nhắn mới
    @PostMapping("/add")
    public ResponseEntity<Void> saveMessage(@Valid @RequestBody MessageModel messageModel) {
        messageService.saveMessage(messageModel);
        return ResponseEntity.status(201).build(); // Status 201 - Created
    }
}
