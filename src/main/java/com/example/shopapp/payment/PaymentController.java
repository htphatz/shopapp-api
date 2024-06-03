package com.example.shopapp.payment;

import com.example.shopapp.dtos.OrderDTO;
import com.example.shopapp.responses.ResponseCustom;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/vn-pay")
    public ResponseCustom<?> pay(@Valid @RequestBody OrderDTO orderDTO) {
        return new ResponseCustom<>(HttpStatus.OK.value(), "Create payment url with VNPay successfully", paymentService.createVnPayPayment(orderDTO));
    }
    @GetMapping("/vn-pay-callback")
    public ResponseEntity<PaymentCallbackDTO> payCallbackHandler(HttpServletRequest request) {
        String status = request.getParameter("vnp_ResponseCode");
        if (status.equals("00")) {
            return ResponseEntity.ok(PaymentCallbackDTO.builder().code("OK").message("Pay with VNPay successfully").build());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(PaymentCallbackDTO.builder().code("NOT_OK").message("Cannot pay with VNPay").build());
        }
    }
}
