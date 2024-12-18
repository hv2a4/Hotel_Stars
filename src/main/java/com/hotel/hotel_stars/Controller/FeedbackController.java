package com.hotel.hotel_stars.Controller;

import com.hotel.hotel_stars.DTO.FeedbackDto;
import com.hotel.hotel_stars.Entity.Feedback;
import com.hotel.hotel_stars.Models.MessageModel;
import com.hotel.hotel_stars.Service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
