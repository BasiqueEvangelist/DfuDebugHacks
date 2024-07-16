package me.basiqueevangelist.dfudebughacks.mixin;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.Lifecycle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = DataResult.Success.class, remap = false)
public class DataResultSuccessMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void checkNullability(Object value, Lifecycle lifecycle, CallbackInfo ci) {
        if (value == null)
            throw new IllegalArgumentException("value must be not null");
    }
}
