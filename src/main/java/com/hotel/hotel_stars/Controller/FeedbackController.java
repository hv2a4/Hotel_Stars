package com.hotel.hotel_stars.Controller;

import com.hotel.hotel_stars.DTO.FeedbackDto;
import com.hotel.hotel_stars.Entity.Feedback;
import com.hotel.hotel_stars.DTO.StatusResponseDto;
import com.hotel.hotel_stars.Entity.Feedback;
import com.hotel.hotel_stars.Exception.ErrorsService;
import com.hotel.hotel_stars.Models.FeedbackModel;
import com.hotel.hotel_stars.Service.FeedbackService;
import com.hotel.hotel_stars.utils.paramService;
import com.hotel.hotel_stars.Models.MessageModel;
import com.hotel.hotel_stars.Service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @Autowired
    paramService paramServices;
    @Autowired
    ErrorsService errorsServices;
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
    @PostMapping("/postFeedBack")
    public ResponseEntity<?> postFeedback(@Valid @RequestBody FeedbackModel feedbackModel){
        Map<String, String> response = new HashMap<String, String>();
        StatusResponseDto statusResponseDto = errorsServices.errorFeedBack(feedbackModel);
        if (statusResponseDto != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(statusResponseDto);
        }
        System.out.println(feedbackModel.getIdInvoice());
        Feedback feedback = feedbackService.postFeedback(feedbackModel);
        if(feedback != null){
            response = paramServices.messageSuccessApi(201, "success", "Đánh giá thành công");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }else {
            response = paramServices.messageSuccessApi(400, "error", "Đánh giá thất bại");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); // 400 Bad Request for failure
        }
    }
}
