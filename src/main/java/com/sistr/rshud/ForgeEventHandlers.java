package com.sistr.rshud;

import com.sistr.rshud.client.RSHUDRenderer;
import com.sistr.rshud.datagen.RSHUDTags;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForgeEventHandlers {
    private static final RSHUDRenderer RSHUD_RENDERER = new RSHUDRenderer();

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onDisplayRender(RenderGameOverlayEvent.Pre event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
            return;
        }
        PlayerEntity player = RSHUDMod.proxy.getClientPlayer();
        ItemStack stack = player.getItemStackFromSlot(EquipmentSlotType.HEAD);
        if (!Config.RSHUD_ALWAYS.get()) {
            if (stack.isEmpty()) {
                return;
            }
            CompoundNBT tag = stack.getOrCreateTag();
            if (!tag.getBoolean(RSHUDMod.MODID + "CanRender") && !(RSHUDTags.Items.RSHUD_MOUNTED.contains(stack.getItem()))) {
                return;
            }
        }
        RSHUD_RENDERER.render(player, event.getWindow().getScaledWidth(), event.getWindow().getScaledHeight());
    }

    @SubscribeEvent
    public static void onGetToolTip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        CompoundNBT tag = stack.getOrCreateTag();
        if (tag.getBoolean(RSHUDMod.MODID + "CanRender")) {
            event.getToolTip().add(new TranslationTextComponent(RSHUDMod.MODID + ".modified.tooltip"));
        }
    }


}
