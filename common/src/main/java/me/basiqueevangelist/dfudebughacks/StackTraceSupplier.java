package me.basiqueevangelist.dfudebughacks;

import java.util.function.Supplier;

public record StackTraceSupplier(StackTraceElement[] stackTrace, Supplier<String> message) implements Supplier<String> {
    @Override
    public String get() {
        return message.get();
    }
}