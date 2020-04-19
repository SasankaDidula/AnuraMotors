package com.anura.anuramotors.Interface;

import com.anura.anuramotors.model.Banner;
import java.util.List;

public interface IBannerLoadListener {
    void onBannerLoadSuccess(List<Banner> banners);
    void onBannerLoadFailed(String message);
}
