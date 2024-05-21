package com.example.shopapp.services;

import com.example.shopapp.dtos.BannerDTO;
import com.example.shopapp.dtos.VoucherDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.Banner;
import com.example.shopapp.models.Voucher;

import java.util.List;

public interface IBannerService {
    Banner createBanner(BannerDTO bannerDTO) throws Exception;
    Banner getBannerById(Long id);
    List<Banner> getAllBanners();
    Banner updateBanner(Long id, BannerDTO bannerDTO) throws DataNotFoundException;
    void deleteBanner(Long id);
}
