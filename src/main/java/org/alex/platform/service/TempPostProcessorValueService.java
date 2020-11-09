package org.alex.platform.service;

import java.util.Map;

public interface TempPostProcessorValueService {
    Map<Object, Object> findAllTempValue();

    Map<Object, Object> findTempValueByValue(String hashKey);

    void removeTempValue(String hashKey);

    void removeAllTempValue();
}
