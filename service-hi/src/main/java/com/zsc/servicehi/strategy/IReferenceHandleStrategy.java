package com.zsc.servicehi.strategy;

import model.weather.Reference;

/**
 * 参考处理策略接口
 */
public interface IReferenceHandleStrategy {
    Reference handleReference(String quality);
}
