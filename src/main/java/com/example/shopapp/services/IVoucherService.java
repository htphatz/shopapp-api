package com.example.shopapp.services;

import com.example.shopapp.dtos.VoucherDTO;
import com.example.shopapp.models.Voucher;

import java.util.List;

public interface IVoucherService {
    Voucher createVoucher(VoucherDTO voucherDTO);
    Voucher getVoucherById(Long id);
    Voucher getVoucherByCode(String code);
    List<Voucher> getAllVouchers();
    Voucher updateVoucher(Long id, VoucherDTO voucherDTO);
    void deleteVoucher(Long id);
}
