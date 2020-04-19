package com.anura.anuramotors.Interface;



import com.anura.anuramotors.model.Center;

import java.util.List;

public interface IBranchLoadListener {
    void onBranchLoadSuccess(List<Center> centerList);
    void onBranchnLoadFailed(String message);
}
