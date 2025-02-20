// Copied from https://github.com/wisp-forest/owo-lib/blob/1.21/src/main/java/io/wispforest/owo/mixin/DataResultMixin.java

package me.basiqueevangelist.dfudebughacks.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.serialization.DataResult;
import me.basiqueevangelist.dfudebughacks.StackTraceSupplier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@Mixin(value = DataResult.class, remap = false)
public interface DataResultMixin {

    @Inject(
        method = {
            "error(Ljava/util/function/Supplier;)Lcom/mojang/serialization/DataResult;",
            "error(Ljava/util/function/Supplier;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;",
            "error(Ljava/util/function/Supplier;Lcom/mojang/serialization/Lifecycle;)Lcom/mojang/serialization/DataResult;",
            "error(Ljava/util/function/Supplier;Ljava/lang/Object;Lcom/mojang/serialization/Lifecycle;)Lcom/mojang/serialization/DataResult;"
        },
        at = @At(value = "HEAD"),
        remap = false
    )
    private static <R> void wrapMessageWithStacktrace(CallbackInfoReturnable<Optional<DataResult.Error<R>>> cir, @Local(argsOnly = true) LocalRef<Supplier<String>> messageSupplier) {
        var ogSupplier = messageSupplier.get();
        var ogClass = ogSupplier.getClass();
        if (ogSupplier instanceof StackTraceSupplier) return;

        var stackTrace = Thread.currentThread().getStackTrace();

        if (ogClass.isSynthetic()) {
            try {
                for (var field : ogClass.getDeclaredFields()) {
                    if (!Throwable.class.isAssignableFrom(field.getType())) continue;

                    field.setAccessible(true);
                    if (field.get(ogSupplier) instanceof Throwable e) stackTrace = e.getStackTrace().clone();
                    break;
                }
            } catch (IllegalArgumentException | IllegalAccessException ignore) {}
        }

        messageSupplier.set(new StackTraceSupplier(stackTrace, ogSupplier));
    }

    @Mixin(value = DataResult.Error.class, remap = false)
    abstract class DataResultErrorMixin<R> {
        @Shadow(remap = false)
        public abstract Supplier<String> messageSupplier();

        @Inject(method = {"getOrThrow", "getPartialOrThrow"}, at = @At(value = "HEAD"), remap = false)
        private <E extends Throwable> void addStackTraceToException(CallbackInfoReturnable<R> cir, @Local(argsOnly = true) LocalRef<Function<String, E>> exceptionSupplier) {
            final var funcToWrap = exceptionSupplier.get();

            exceptionSupplier.set(s -> {
                var exception = funcToWrap.apply(s);
                if (this.messageSupplier() instanceof StackTraceSupplier stackTraceSupplier) {
                    exception.setStackTrace(stackTraceSupplier.stackTrace());
                }

                return exception;
            });
        }
    }
}