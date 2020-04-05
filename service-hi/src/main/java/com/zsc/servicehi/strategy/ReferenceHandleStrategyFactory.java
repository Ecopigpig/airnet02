package com.zsc.servicehi.strategy;

import com.zsc.servicehi.strategy.Impl.*;
import org.apache.commons.lang.StringUtils;

public class ReferenceHandleStrategyFactory {
    private ReferenceHandleStrategyFactory(){}

    public static IReferenceHandleStrategy getReferenceHandleStrategy(String quality){
        IReferenceHandleStrategy referenceHandleStrategy = null;
        if (StringUtils.equals("优质",quality)) {
            referenceHandleStrategy = new HighReferenceHandleStrategy();
        } else if (StringUtils.equals("良好",quality)) {
            referenceHandleStrategy = new FineReferenceHandleStategy();
        }else if (StringUtils.equals("轻度污染",quality)) {
            referenceHandleStrategy = new LightReferenceHandleStrategy();
        }else if (StringUtils.equals("中度污染",quality)) {
            referenceHandleStrategy = new MiddleReferenceHandleStrategy();
        }else if (StringUtils.equals("重度污染",quality)) {
            referenceHandleStrategy = new HeavyReferenceHandleStrategy();
        }else if (StringUtils.equals("严重污染",quality)) {
            referenceHandleStrategy = new SeriousReferenceHandleStrategy();
        }
        return referenceHandleStrategy;
    }
}
