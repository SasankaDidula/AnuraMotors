package com.anura.anuramotors.Interface;



import com.anura.anuramotors.model.Banner;

import java.util.List;

public interface ILookBookLoadListener {
    void onLookBookLoadSuccess(List<Banner> banners);
    void onLookBookLoadFailed(String message);
}
