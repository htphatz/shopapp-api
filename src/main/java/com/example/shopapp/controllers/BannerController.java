package com.example.shopapp.controllers;

import com.example.shopapp.dtos.BannerDTO;
import com.example.shopapp.entities.Banner;
import com.example.shopapp.responses.ArrayDataResponse;
import com.example.shopapp.responses.ResponseCustom;
import com.example.shopapp.services.BannerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/banners")
@RequiredArgsConstructor
public class BannerController {
    private final BannerService bannerService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ArrayDataResponse<Banner> createBanner(@Valid BannerDTO bannerDTO){
        Banner newBanner = bannerService.createBanner(bannerDTO);
        return new ArrayDataResponse<>(HttpStatus.OK.value(), "Create banner successfully", List.of(newBanner));
    }

    @GetMapping
    public ResponseCustom<List<Banner>> getBanners() {
        List<Banner> banners = bannerService.getAllBanners();
        return new ResponseCustom<>(HttpStatus.OK.value(), "Get all banners successfully", banners);
    }

    @GetMapping("/{id}")
    public ResponseCustom<?> getBannerById(@Valid @PathVariable("id") Long id) {
        Banner existingBanner = bannerService.getBannerById(id);
        return new ResponseCustom<>(HttpStatus.OK.value(), "Get banner with id " + id + " successfully", existingBanner);
    }

    @PutMapping("/{id}")
    public ResponseCustom<?> updateBanner(@Valid @PathVariable("id") Long id, BannerDTO bannerDTO) {
        bannerService.updateBanner(id, bannerDTO);
        return new ResponseCustom<>(HttpStatus.ACCEPTED.value(), "Update banner with id " + id + " successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseCustom<?> deleteBanner(@Valid @PathVariable("id") Long id) {
        bannerService.deleteBanner(id);
        return new ResponseCustom<>(HttpStatus.NO_CONTENT.value(), "Delete banner with id " + id + " successfully");
    }
}
