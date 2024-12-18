package com.hotel.hotel_stars.Controller;

import com.hotel.hotel_stars.Models.MessageModel;
import com.hotel.hotel_stars.Models.SendEmailFeedback;
import com.hotel.hotel_stars.Service.MessageService;
import com.hotel.hotel_stars.utils.paramService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/messages")
public class MessagesController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private paramService paramServices;

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
    public ResponseEntity<?> saveMessage(@Valid @RequestBody MessageModel messageModel) {
        messageService.saveMessage(messageModel);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 201);
        response.put("message", "Phản hồi thành công");
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/send-email-feedback")
    public ResponseEntity<?> sendEmail(@RequestBody SendEmailFeedback sendEmailFeedback) {
        String message =
                "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Email Response</title>\n" +
                "</head>\n" +
                "<body style=\"font-family: Arial, sans-serif; line-height: 1.6; color: #333;\">\n" +
                "\n" +
                "    <h3>Chủ đề: Phản hồi đánh giá</h3>\n" +
                "\n" +
                "    <p>Kính gửi <strong> " + sendEmailFeedback.getFullname() + "</strong>,</p>\n" +
                "\n" +
                "    <p>Cảm ơn bạn đã dành thời gian để chia sẻ ý kiến và đánh giá về dịch vụ của khách sạn Stars của chúng tôi. Dưới đây là câu trả lời của chúng tôi:</p>\n" +
                "\n" +
                "    <p>"+sendEmailFeedback.getMessage()+"</p>\n" +
                "\n" +
                "    <p>Nếu bạn có thêm bất kỳ câu hỏi hoặc đề xuất nào, xin vui lòng liên hệ với chúng tôi. Chúng tôi rất mong được phục vụ bạn trong tương lai.</p>\n" +
                "\n" +
                "    <p>Trân trọng,</p>\n" +
                "    <p><strong>Hotel Stars</strong><br>\n" +
                "\n" +
                "</body>\n" +
                "</html>\n";
        paramServices.sendEmails(sendEmailFeedback.getEmail(), "Phản hồi đánh giá", message);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 201);
        response.put("message", "Gửi email thành công");
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }
}
