package com.example.shopapp.controllers;

import com.example.shopapp.dtos.VoucherDTO;
import com.example.shopapp.models.Voucher;
import com.example.shopapp.responses.ArrayDataResponse;
import com.example.shopapp.responses.ResponseCustom;
import com.example.shopapp.services.VoucherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/vouchers")
@RequiredArgsConstructor
public class VoucherController {
    private final VoucherService voucherService;

    @PostMapping
    public ArrayDataResponse<Voucher> createVoucher(@Valid @RequestBody VoucherDTO voucherDTO) {
        Voucher newVoucher = voucherService.createVoucher(voucherDTO);
        return new ArrayDataResponse<>(HttpStatus.OK.value(), "Create voucher successfully", List.of(newVoucher));
    }

    @GetMapping
    public ResponseCustom<?> getAllVoucher() {
        List<Voucher> vouchers = voucherService.getAllVouchers();
        return new ResponseCustom<>(HttpStatus.OK.value(), "Get all vouchers successfully", vouchers);
    }

    @GetMapping("/{code}")
    public ResponseCustom<?> getVoucherByCode(@Valid @PathVariable("code") String code) {
        Voucher existingVoucher = voucherService.getVoucherByCode(code);
        return new ResponseCustom<>(HttpStatus.OK.value(), "Get voucher with code " + code + " successfully", existingVoucher);
    }

    @PutMapping("/{id}")
    public ResponseCustom<?> updateProducts(@Valid @PathVariable("id") Long id, @Valid @RequestBody VoucherDTO voucherDTO) {
        voucherService.updateVoucher(id, voucherDTO);
        return new ResponseCustom<>(HttpStatus.ACCEPTED.value(), "Update voucher with id " + id + " successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseCustom<?> deleteProduct(@PathVariable("id") Long id) {
        voucherService.deleteVoucher(id);
        return new ResponseCustom<>(HttpStatus.NO_CONTENT.value(), "Delete voucher with id " + id + " successfully");
    }
}
