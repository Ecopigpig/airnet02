package com.zsc.servicehi.strategy;

import com.zsc.servicehi.strategy.Impl.*;

import java.util.HashMap;
import java.util.Map;

public class ReferenceHandleStrategyFactory {

    private static Map<String,IReferenceHandleStrategy> referenceHandleStrategyMap;

    private ReferenceHandleStrategyFactory(){
        this.referenceHandleStrategyMap = new HashMap<>();
        this.referenceHandleStrategyMap.put("优质",new HighReferenceHandleStrategy());
        this.referenceHandleStrategyMap.put("良好",new FineReferenceHandleStrategy());
        this.referenceHandleStrategyMap.put("轻度污染",new LightReferenceHandleStrategy());
        this.referenceHandleStrategyMap.put("中度污染",new MiddleReferenceHandleStrategy());
        this.referenceHandleStrategyMap.put("重度污染",new HeavyReferenceHandleStrategy());
        this.referenceHandleStrategyMap.put("严重污染",new SeriousReferenceHandleStrategy());
    }

    public static IReferenceHandleStrategy getReceiptHandleStrategy(String quality){
        return referenceHandleStrategyMap.get(quality);
    }
}
