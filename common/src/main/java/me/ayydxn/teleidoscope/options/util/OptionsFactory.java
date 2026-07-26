package me.ayydxn.teleidoscope.options.util;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.controller.*;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class OptionsFactory
{
    private OptionsFactory()
    {
    }

    public static Option<Boolean> toggle(String translationKey, boolean defaultValue, Supplier<Boolean> getter, Consumer<Boolean> setter)
    {
        return Option.<Boolean>createBuilder()
                .name(Component.translatable(translationKey))
                .description(OptionDescription.of(Component.translatable(translationKey + ".description")))
                .binding(defaultValue, getter, setter)
                .controller(BooleanControllerBuilder::create)
                .build();
    }

    public static Option<Integer> intSlider(String translationKey, int defaultValue, Supplier<Integer> getter, Consumer<Integer> setter, int min, int max, int step,
                                            @Nullable ValueFormatter<Integer> formatter)
    {
        return Option.<Integer>createBuilder()
                .name(Component.translatable(translationKey))
                .description(OptionDescription.of(Component.translatable(translationKey + ".description")))
                .binding(defaultValue, getter, setter)
                .controller(option ->
                {
                    IntegerSliderControllerBuilder builder = IntegerSliderControllerBuilder.create(option)
                            .range(min, max)
                            .step(step);

                    if (formatter != null)
                        builder.formatValue(formatter);

                    return builder;
                })
                .build();
    }

    public static Option<Integer> intSlider(String translationKey, int defaultValue, Supplier<Integer> getter, Consumer<Integer> setter, int min, int max, int step)
    {
        return intSlider(translationKey, defaultValue, getter, setter, min, max, step, null);
    }

    public static <E extends Enum<E>> Option<E> enumCycle(String translationKey, Class<E> enumType, E defaultValue, Supplier<E> getter, Consumer<E> setter)
    {
        return Option.<E>createBuilder()
                .name(Component.translatable(translationKey))
                .description(OptionDescription.of(Component.translatable(translationKey + ".description")))
                .binding(defaultValue, getter, setter)
                .controller(option -> EnumControllerBuilder.create(option).enumClass(enumType))
                .build();
    }
}
