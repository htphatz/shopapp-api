package com.example.shopapp.services;

import com.example.shopapp.dtos.VoucherDTO;
import com.example.shopapp.exceptions.ResourceNotFoundException;
import com.example.shopapp.entities.Voucher;
import com.example.shopapp.repositories.VoucherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VoucherService implements IVoucherService {
    private final VoucherRepository voucherRepository;

    @Override
    public Voucher createVoucher(VoucherDTO voucherDTO) {
        Voucher newVoucher = Voucher.builder()
                .code(voucherDTO.getCode())
                .discountType(voucherDTO.getDiscountType())
                .discountValue(voucherDTO.getDiscountValue())
                .build();
        return voucherRepository.save(newVoucher);
    }

    @Override
    public Voucher getVoucherById(Long id) {
        return voucherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find voucher with id: " + id));
    }

    @Override
    public Voucher getVoucherByCode(String code) {
        return voucherRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find voucher with code: " + code));
    }

    @Override
    @Transactional
    public Voucher updateVoucher(Long id, VoucherDTO voucherDTO) {
        Voucher existingVoucher = voucherRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Voucher with id " + id + " not found"));
        existingVoucher.setCode(voucherDTO.getCode());
        existingVoucher.setDiscountType(voucherDTO.getDiscountType());
        existingVoucher.setDiscountValue(voucherDTO.getDiscountValue());
        return voucherRepository.save(existingVoucher);
    }

    @Override
    @Transactional
    public void deleteVoucher(Long id) {
        voucherRepository.deleteById(id);
    }

    @Override
    public List<Voucher> getAllVouchers() {
        return voucherRepository.findAll();
    }
}
