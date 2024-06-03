package com.example.shopapp.payment;

import com.example.shopapp.configurations.VNPayConfig;
import com.example.shopapp.dtos.OrderDTO;
import com.example.shopapp.utils.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final VNPayConfig vnPayConfig;
    public PaymentDTO createVnPayPayment(OrderDTO orderDTO) {
        long amount = Math.round(orderDTO.getTotalMoney() * 100L);
//        long amount = Integer.parseInt(request.getParameter("amount")) * 100L;
//        String bankCode = request.getParameter("bankCode");
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
//        vnpParamsMap.put("vnp_OrderType", String.valueOf(orderDTO));
//        vnpParamsMap.put("vnp_OrderDetail", String.valueOf(orderDTO));
//        if (bankCode != null && !bankCode.isEmpty()) {
//            vnpParamsMap.put("vnp_BankCode", bankCode);
//        }
//        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));
        //build query url
        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
        return PaymentDTO.builder()
                .paymentUrl(paymentUrl)
                .build();
    }
}
