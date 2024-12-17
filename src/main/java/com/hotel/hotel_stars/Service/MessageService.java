package com.hotel.hotel_stars.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.hotel_stars.Models.MessageModel;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    private static final String FILE_PATH = "src/main/resources/data/feedback.json";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final File file = new File(FILE_PATH);

    // Đảm bảo file tồn tại
    private void ensureFileExists() {
        if (!file.exists()) {
            try {
                file.createNewFile(); // Tạo file nếu chưa tồn tại
            } catch (IOException e) {
                e.printStackTrace(); // Log lỗi nếu không thể tạo file
            }
        }
    }

    private List<MessageModel> readMessages() {
        if (file.length() == 0) {  // Kiểm tra nếu file trống
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(file, new TypeReference<List<MessageModel>>() {});
        } catch (IOException e) {
            e.printStackTrace(); // Log lỗi nếu có khi đọc file
            return new ArrayList<>(); // Trả về danh sách rỗng nếu có lỗi
        }
    }


    // Ghi danh sách tin nhắn vào file JSON
    private void writeMessages(List<MessageModel> messageModels) {
        try {
            objectMapper.writeValue(file, messageModels);
        } catch (IOException e) {
            e.printStackTrace(); // Log lỗi nếu có khi ghi file
        }
    }

    // Optional: Trả về danh sách tin nhắn, nếu có lỗi sẽ trả về Optional.empty
    public Optional<List<MessageModel>> getMessages() {
        return Optional.ofNullable(readMessages());
    }

    // Tìm tin nhắn theo id
    public MessageModel getMessageById(Integer id) {
        List<MessageModel> messageModels = readMessages();
        return messageModels.stream()
                .filter(messageModel -> messageModel.getId().equals(id))
                .findFirst()
                .orElse(null); // Trả về null nếu không tìm thấy
    }


    // Lưu tin nhắn mới
    public void saveMessage(MessageModel messageModel) {
        List<MessageModel> messageModels = readMessages();
        messageModels.add(messageModel);
        writeMessages(messageModels);
    }
}
