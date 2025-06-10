package com.zenith.zenithrpc.fault.tolerant;

import com.zenith.zenithrpc.serializer.SpiLoader;

public class TolerantStrategyFactory {
    static {
        SpiLoader.load(TolerantStrategy.class);
    }

    public static TolerantStrategy getInstance(String key)
    {
        return SpiLoader.getInstance(TolerantStrategy.class, key);
    }
}
