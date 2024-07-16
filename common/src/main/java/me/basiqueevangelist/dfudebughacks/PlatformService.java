package me.basiqueevangelist.dfudebughacks;

import java.util.ServiceLoader;

public interface PlatformService {
    PlatformService INSTANCE = ServiceLoader.load(PlatformService.class).findFirst().orElseThrow();

    void blessClass(String className);
}