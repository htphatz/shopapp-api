package com.example.shopapp.controllers;

import com.example.shopapp.dtos.VoucherDTO;
import com.example.shopapp.models.Voucher;
import com.example.shopapp.services.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/vouchers")
@RequiredArgsConstructor
public class VoucherController {
    private final VoucherService voucherService;

    @PostMapping
    public ResponseEntity<?> createVoucher(
            VoucherDTO voucherDTO,
            BindingResult result)
    {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            Voucher newVoucher = voucherService.createVoucher(voucherDTO);
            return ResponseEntity.ok(newVoucher);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllVoucher()
    {
        try {
            List<Voucher> vouchers = voucherService.getAllVouchers();
            return ResponseEntity.ok(vouchers);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getVoucherByCode(@PathVariable String code) {
        try {
            Voucher existingVoucher = voucherService.getVoucherByCode(code);
            return ResponseEntity.ok(existingVoucher);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProducts(
            @PathVariable("id") Long id,
            VoucherDTO voucherDTO) {
        try {
            Voucher updatedVoucher = voucherService.updateVoucher(id, voucherDTO);
            return ResponseEntity.ok(updatedVoucher);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long id) {
        try {
            voucherService.deleteVoucher(id);
            return ResponseEntity.ok("Voucher with id " + id + " deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
