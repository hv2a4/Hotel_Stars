package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.DTO.*;
import com.hotel.hotel_stars.Entity.*;
import com.hotel.hotel_stars.Models.FeedbackModel;
import com.hotel.hotel_stars.Repository.FeedBackRepository;
import com.hotel.hotel_stars.Repository.InvoiceRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    FeedBackRepository feedBackRepository;

    @Autowired
    InvoiceRepository invoiceRepository;

    public FeedbackDto convertDTO(Feedback feedback) {
        // Chuyển đổi Feedback sang FeedbackDto
        FeedbackDto feedbackDto = modelMapper.map(feedback, FeedbackDto.class);

        // Chuyển đổi Account sang AccountDto trước khi gán vào BookingDto
        AccountDto accountDto = modelMapper.map(feedback.getInvoice().getBooking().getAccount(), AccountDto.class);


        // Gán InvoiceDto vào FeedbackDto
  //      feedbackDto.setInvoiceDto(invoiceDto);

        return feedbackDto;
    }


    public List<FeedbackDto> convertListDTO() {
        List<Feedback> feedbackList = feedBackRepository.findAll();
        return feedbackList.stream().map(this::convertDTO).toList();
    }

//    public FeedbackDto addFeedback(FeedbackModel feedbackModel) {
//        List<String> errorMessages = new ArrayList<>();
//
////        if (!errorMessages.isEmpty()) {
////            throw new ValidationException(String.join(", ", errorMessages));
////        }
//        try {
//            Feedback feedback = new Feedback();
//            feedback.setId(feedbackModel.getId());
//            feedback.setContent(feedbackModel.getContent());
//            feedback.setStars(feedbackModel.getStars());
//            feedback.setCreateAt(feedbackModel.getCreateAt());
//            feedback.setRatingStatus(feedbackModel.getRatingStatus());
//            Optional<Invoice> invoice = invoiceRepository.findById(feedbackModel.getIdInvoice());
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
}
