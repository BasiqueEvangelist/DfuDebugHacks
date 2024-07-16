package me.basiqueevangelist.dfudebughacks;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.WeakHashMap;

public class CodecNaming {
    private static final WeakHashMap<Codec<?>, String> NAMES = new WeakHashMap<>();

    public static @Nullable String getName(Codec<?> codec) {
        return NAMES.get(codec);
    }

    @ApiStatus.Internal
    @HookMethod
    public static <T> Codec<T> nameCodec(Codec<T> codec, String name) {
        NAMES.put(codec, name);

        return codec;
    }
}
