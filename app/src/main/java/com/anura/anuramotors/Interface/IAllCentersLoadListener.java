package com.anura.anuramotors.Interface;

import java.util.List;

public interface IAllCentersLoadListener {
    void onAllCenterLoadSuccess(List<String> areaNameList);
    void onAllCenterLoadFailed(String message);
}
