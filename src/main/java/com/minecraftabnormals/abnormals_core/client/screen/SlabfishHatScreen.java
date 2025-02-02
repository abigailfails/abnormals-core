package com.minecraftabnormals.abnormals_core.client.screen;

import com.minecraftabnormals.abnormals_core.client.RewardHandler;
import com.minecraftabnormals.abnormals_core.core.util.NetworkUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Locale;

/**
 * @author Jackson
 */
public class SlabfishHatScreen extends Screen {
	public static final String SLABFISH_SCREEN_KEY = "abnormals_core.screen.slabfish_settings";
	private final Screen parent;

	public SlabfishHatScreen(Screen parent) {
		super(new TranslationTextComponent(SLABFISH_SCREEN_KEY + ".title"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		int i = 0;

		for (RewardHandler.SlabfishSetting setting : RewardHandler.SlabfishSetting.values()) {
			ForgeConfigSpec.ConfigValue<Boolean> configValue = setting.getConfigValue();

			this.addButton(new Button(this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 24 * (i >> 1), 150, 20, this.getOptionName(setting, configValue.get()), (button) -> {
				boolean enabled = !configValue.get();
				configValue.set(enabled);
				button.setMessage(this.getOptionName(setting, enabled));
				NetworkUtil.updateSlabfish(RewardHandler.SlabfishSetting.getConfig());
			}, (button, stack, mouseX, mouseY) -> this.renderTooltip(stack, this.font.split(new TranslationTextComponent("abnormals_core.config.slabfish_hat." + setting.name().toLowerCase(Locale.ROOT) + ".tooltip"), 200), mouseX, mouseY)));
			++i;
		}

		if (i % 2 == 1) {
			++i;
		}

		this.addButton(new Button(this.width / 2 - 100, this.height / 6 + 24 * (i >> 1), 200, 20, DialogTexts.GUI_DONE, (button) -> this.getMinecraft().setScreen(this.parent)));
	}

	@Override
	public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(stack);
		drawCenteredString(stack, this.font, this.title, this.width / 2, 20, 16777215);
		super.render(stack, mouseX, mouseY, partialTicks);
	}

	@Override
	public void onClose() {
		this.getMinecraft().setScreen(this.parent);
	}

	private ITextComponent getOptionName(RewardHandler.SlabfishSetting setting, boolean enabled) {
		return DialogTexts.optionStatus(new TranslationTextComponent("abnormals_core.config.slabfish_hat." + setting.name().toLowerCase(Locale.ROOT)), enabled);
	}
}
