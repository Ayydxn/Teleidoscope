package me.ayydxn.teleidoscope.mixin.client.gui.screens;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Codec;
import me.ayydxn.teleidoscope.client.gui.screens.TeleidoscopeOptionsScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.OnlineOptionsScreen;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.SimpleOptionsSubScreen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@Mixin(OnlineOptionsScreen.class)
public class OnlineOptionsScreenMixin extends SimpleOptionsSubScreen
{
    public OnlineOptionsScreenMixin(Screen lastScreen, Options options, Component title, OptionInstance<?>[] smallOptions)
    {
        super(lastScreen, options, title, smallOptions);
    }

    @Inject(method = "createOnlineOptionsScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/Optionull;map(Ljava/lang/Object;Ljava/util/function/Function;)Ljava/lang/Object;"))
    private static void addTeleidoscopeOptionsButton(Minecraft minecraft, Screen lastScreen, Options smallOptions,
                                                     CallbackInfoReturnable<OnlineOptionsScreen> cir, @Local(name = "list") List<OptionInstance<?>> list)
    {
        OpenOptionsScreenValueSet valueSet = new OpenOptionsScreenValueSet(Component.translatable("teleidoscope.gui.options"), cir.getReturnValue());

        OptionInstance<Void> optionScreenDummyOption = new OptionInstance<>("", OptionInstance.noTooltip(),
                (text, value) -> Component.empty(), valueSet, null, value -> {});

        list.add(optionScreenDummyOption);
    }

    // Custom value set so we can replicate YACL's ButtonOption but while using the vanilla API.
    private record OpenOptionsScreenValueSet(Component text, Screen lastScreen) implements OptionInstance.ValueSet<Void>
    {
        @Override
        public @NotNull Function<OptionInstance<Void>, AbstractWidget> createButton(OptionInstance.TooltipSupplier<Void> tooltipSupplier, Options options,
                                                                                    int x, int y, int width, Consumer<Void> onValueChanged)
        {
            Screen teleidoscopeOptionsScreen = new TeleidoscopeOptionsScreen(new OptionsScreen(null, Minecraft.getInstance().options)).getHandle();

            return option -> Button.builder(text, button -> Minecraft.getInstance().setScreen(teleidoscopeOptionsScreen))
                    .bounds(x, y, width, 20)
                    .build();
        }

        @Override
        public @NotNull Optional<Void> validateValue(Void value)
        {
            return Optional.empty();
        }

        @Override
        @SuppressWarnings("NullableProblems")
        public @Nullable Codec<Void> codec()
        {
            return null;
        }
    }
}
