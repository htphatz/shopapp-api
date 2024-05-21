package com.example.shopapp.controllers;

import com.example.shopapp.dtos.VoucherDTO;
import com.example.shopapp.models.Voucher;
import com.example.shopapp.responses.ResponseCustom;
import com.example.shopapp.services.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseCustom<?> createVoucher(
            @RequestBody VoucherDTO voucherDTO,
            BindingResult result)
    {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return new ResponseCustom<>(HttpStatus.BAD_REQUEST.value(), "Create voucher failed");
            }
            Voucher newVoucher = voucherService.createVoucher(voucherDTO);
            return new ResponseCustom<>(HttpStatus.CREATED.value(), "Create voucher successfully", newVoucher);
        } catch (Exception e) {
            return new ResponseCustom<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping
    public ResponseCustom<?> getAllVoucher()
    {
        try {
            List<Voucher> vouchers = voucherService.getAllVouchers();
            return new ResponseCustom<>(HttpStatus.OK.value(), "Get all vouchers successfully", vouchers);
        } catch (Exception e) {
            return new ResponseCustom<>(HttpStatus.BAD_REQUEST.value(), "Cannot get all vouchers");
        }
    }

    @GetMapping("/{code}")
    public ResponseCustom<?> getVoucherByCode(@PathVariable String code) {
        try {
            Voucher existingVoucher = voucherService.getVoucherByCode(code);
            return new ResponseCustom<>(HttpStatus.OK.value(), "Get voucher with code " + code + " successfully", existingVoucher);
        } catch (Exception e) {
            return new ResponseCustom<>(HttpStatus.BAD_REQUEST.value(), "Cannot get voucher with code " + code);
        }
    }

    @PutMapping("/{id}")
    public ResponseCustom<?> updateProducts(
            @PathVariable("id") Long id,
            @RequestBody VoucherDTO voucherDTO) {
        try {
            voucherService.updateVoucher(id, voucherDTO);
            return new ResponseCustom<>(HttpStatus.ACCEPTED.value(), "Update voucher with id " + id + " successfully");
        } catch (Exception e) {
            return new ResponseCustom<>(HttpStatus.BAD_REQUEST.value(), "Cannot update voucher with id " + id);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseCustom<?> deleteProduct(@PathVariable("id") Long id) {
        voucherService.deleteVoucher(id);
        return new ResponseCustom<>(HttpStatus.NO_CONTENT.value(), "Delete voucher with id " + id + " successfully");
    }
}
