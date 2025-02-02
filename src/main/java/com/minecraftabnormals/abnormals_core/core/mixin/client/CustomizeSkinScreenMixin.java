package com.minecraftabnormals.abnormals_core.core.mixin.client;

import com.minecraftabnormals.abnormals_core.client.screen.SlabfishHatScreen;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.CustomizeSkinScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SettingsScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(CustomizeSkinScreen.class)
public final class CustomizeSkinScreenMixin extends SettingsScreen {

	private CustomizeSkinScreenMixin(Screen previousScreen, GameSettings gameSettingsObj, ITextComponent textComponent) {
		super(previousScreen, gameSettingsObj, textComponent);
	}

	@ModifyVariable(method = "init", ordinal = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/CustomizeSkinScreen;addButton(Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;", ordinal = 1, shift = At.Shift.AFTER))
	public int init(int i) {
		Minecraft minecraft = this.getMinecraft();
		++i;
		this.addButton(new Button(this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), 150, 20, new TranslationTextComponent(SlabfishHatScreen.SLABFISH_SCREEN_KEY), (button) -> minecraft.setScreen(new SlabfishHatScreen(this)), (button, stack, mouseX, mouseY) -> this.renderTooltip(stack, minecraft.font.split(new TranslationTextComponent(SlabfishHatScreen.SLABFISH_SCREEN_KEY + ".tooltip", new StringTextComponent("patreon.com/teamabnormals").withStyle(style -> style.withColor(Color.parseColor("#FF424D")))), 200), mouseX, mouseY)));
		return i;
	}

}
