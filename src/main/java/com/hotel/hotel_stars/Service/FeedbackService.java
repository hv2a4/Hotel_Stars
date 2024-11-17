package com.hotel.hotel_stars.Service;

import com.hotel.hotel_stars.DTO.*;
import com.hotel.hotel_stars.Entity.*;
import com.hotel.hotel_stars.Repository.FeedBackRepository;
import com.hotel.hotel_stars.Repository.InvoiceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
