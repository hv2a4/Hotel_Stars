package com.hotel.hotel_stars.Controller;

import com.hotel.hotel_stars.DTO.FeedbackDto;
import com.hotel.hotel_stars.Service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("api/feedback")
public class FeedbackController {
    @Autowired
    FeedbackService feedbackService;

    @GetMapping("getAll")
    public ResponseEntity<List<FeedbackDto>> getAll(){
        return ResponseEntity.ok(feedbackService.convertListDTO());
    }
}
