package me.basiqueevangelist.dfudebughacks;

public class DfuDebugHacks {

    public static final String MOD_ID = "dfudebughacks";

    public static void init() {
        PlatformService.INSTANCE.blessClass("me.basiqueevangelist.dfudebughacks.StackTraceSupplier");
    }
}