package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.DTO.*;
import com.hotel.hotel_stars.DTO.Select.FeedbackResponseDTO;
import com.hotel.hotel_stars.Entity.*;
import com.hotel.hotel_stars.Models.FeedbackModel;
import com.hotel.hotel_stars.Repository.FeedBackRepository;
import com.hotel.hotel_stars.Repository.InvoiceRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FeedbackService {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    FeedBackRepository feedBackRepository;

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    MessageService messageService;

    public FeedbackDto convertDTO(Feedback feedback) {
        // Chuyển đổi Feedback sang FeedbackDto
        FeedbackDto feedbackDto = modelMapper.map(feedback, FeedbackDto.class);

        // Chuyển đổi Account sang AccountDto trước khi gán vào BookingDto
        AccountDto accountDto = modelMapper.map(feedback.getInvoice().getBooking().getAccount(), AccountDto.class);

        // Gán InvoiceDto vào FeedbackDto
        // feedbackDto.setInvoiceDto(invoiceDto);

        return feedbackDto;
    }

    public List<FeedbackDto> convertListDTO() {
        List<Feedback> feedbackList = feedBackRepository.findAll();
        return feedbackList.stream().map(this::convertDTO).toList();
    }

    public List<FeedbackResponseDTO> getListUser() {
        final String defaultAvatar = "https://i.pinimg.com/236x/5e/e0/82/5ee082781b8c41406a2a50a0f32d6aa6.jpg";
        List<Object[]> result = feedBackRepository.findFeedbacksByRoleIdNative();
        List<FeedbackResponseDTO> feedbackResponseDTOList = new ArrayList<>();
        result.forEach(row -> {
            String fullName = (String) row[0];
            String Content = (String) row[1];
            String avatar = (String) row[2];
            if (!isValidUrl(avatar)) {
                avatar = defaultAvatar;
            }
            FeedbackResponseDTO responseDTO = new FeedbackResponseDTO();
            responseDTO.setFullName(fullName);
            responseDTO.setContent(Content);
            responseDTO.setAvatar(avatar);
            feedbackResponseDTOList.add(responseDTO);
        });
        return feedbackResponseDTOList;
    }

    public Boolean isValidUrl(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            int responseCode = connection.getResponseCode();
            // Trả về true nếu mã phản hồi là 200 (OK)
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            // URL không hợp lệ hoặc gặp lỗi
            e.printStackTrace();
            return false;
        }
    }

    public List<Object[]> getAllFeedbackDC() {
        List<Object[]> feedbackList = feedBackRepository.getAll();
        List<Object[]> feedbacks = new ArrayList<>();
        for (Object[] row : feedbackList) {
            if (messageService.getMessageById(Integer.parseInt(String.valueOf(row[0]))) == null) {
                feedbacks.add(row);
            }
        }
        return feedbacks;
    }

    public List<Object[]> getAllFeedbackDPH() {
        List<Object[]> feedbackList = feedBackRepository.getAll();
        List<Object[]> feedbacks = new ArrayList<>();
        for (Object[] row : feedbackList) {
            if (messageService.getMessageById(Integer.parseInt(String.valueOf(row[0]))) != null) {
                feedbacks.add(row);
            }
        }
        return feedbacks;
    }

    public Feedback postFeedback(FeedbackModel feedbackModel) {
        System.out.println(feedbackModel.getIdInvoice());
        Feedback feedback = new Feedback();
        Invoice invoice = invoiceRepository.findById(feedbackModel.getIdInvoice()).get();
        System.out.println(invoice.getId());
        feedback.setContent(feedbackModel.getContent());
        feedback.setStars(feedbackModel.getStars());
        feedback.setRatingStatus(true);
        feedback.setCreateAt(Instant.now());
        feedback.setInvoice(invoice);
        try {
            feedBackRepository.save(feedback);
            return feedback;
        } catch (Exception e) {
            return null;
        }

    }
}
