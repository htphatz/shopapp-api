package com.example.shopapp.services;

import com.example.shopapp.dtos.BannerDTO;
import com.example.shopapp.models.Banner;

import java.util.List;

public interface IBannerService {
    Banner createBanner(BannerDTO bannerDTO);
    Banner getBannerById(Long id);
    List<Banner> getAllBanners();
    Banner updateBanner(Long id, BannerDTO bannerDTO) ;
    void deleteBanner(Long id);
}
