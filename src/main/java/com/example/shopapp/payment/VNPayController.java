package com.example.shopapp.payment;

import com.example.shopapp.responses.ResponseObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/payment")
@RequiredArgsConstructor
public class VNPayController {
    private final VNPayService vnPayService;

//    @GetMapping("/vn-pay")
//    public ResponseEntity<VNPayResponse> pay(HttpServletRequest request) {
//        return ResponseEntity.ok(VNPayResponse.builder()
//                        .code("200")
//                        .message("Success")
//                        .paymentUrl(vnPayService.createOrder())
//                .build());
//    }
//
//    @GetMapping("/vn-pay-callback")
//    public ResponseEntity<?> payCallback() {
//
//    }
}
