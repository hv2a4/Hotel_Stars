package com.hotel.hotel_stars.Controller;

import com.hotel.hotel_stars.DTO.FeedbackDto;
import com.hotel.hotel_stars.Entity.Feedback;
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

    @GetMapping("get-all-use")
    public ResponseEntity<?> getAllFeedback(){
        return ResponseEntity.ok(feedbackService.getListUser());
    }

    @GetMapping("getAllDC")
    public ResponseEntity<?> getAllDC(){
        return ResponseEntity.ok(feedbackService.getAllFeedbackDC());
    }

    @GetMapping("getAllDPH")
    public ResponseEntity<?> getAllDPH(){
        return ResponseEntity.ok(feedbackService.getAllFeedbackDPH());
    }
}
