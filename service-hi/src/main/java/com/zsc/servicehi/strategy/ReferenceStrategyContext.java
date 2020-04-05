package com.zsc.servicehi.strategy;

import model.weather.Reference;

/**
 * 上下文持有策略接口
 */
public class ReferenceStrategyContext {
    private IReferenceHandleStrategy referenceHandleStrategy;

    /**
     * 设置策略接口
     * @param referenceHandleStrategy
     */
    public void setReferenceStrategyContext(IReferenceHandleStrategy referenceHandleStrategy) {
        this.referenceHandleStrategy = referenceHandleStrategy;
    }

    public Reference handleReference(String quality){
        Reference reference = new Reference();
        if (referenceHandleStrategy != null) {
            reference = referenceHandleStrategy.handleReference(quality);
        }
        return reference;
    }
}
