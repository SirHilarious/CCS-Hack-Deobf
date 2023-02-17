//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.init.*;
import net.minecraft.enchantment.*;
import net.minecraft.item.*;

public class ChestSwap extends Module
{
    public ChestSwap() {
        super("ChestSwap", "Swaps your chest plate with an elytra and vice versa", Module.Category.PLAYER, true, false, false);
    }
    
    public void onEnable() {
        if (ChestSwap.mc.player == null) {
            return;
        }
        final ItemStack l_ChestSlot = ChestSwap.mc.player.inventoryContainer.getSlot(6).getStack();
        if (l_ChestSlot.isEmpty()) {
            final int l_Slot = this.FindChestItem(true);
            if (l_Slot != -1) {
                ChestSwap.mc.playerController.windowClick(ChestSwap.mc.player.inventoryContainer.windowId, l_Slot, 0, ClickType.PICKUP, (EntityPlayer)ChestSwap.mc.player);
                ChestSwap.mc.playerController.windowClick(ChestSwap.mc.player.inventoryContainer.windowId, 6, 0, ClickType.PICKUP, (EntityPlayer)ChestSwap.mc.player);
                ChestSwap.mc.playerController.windowClick(ChestSwap.mc.player.inventoryContainer.windowId, l_Slot, 0, ClickType.PICKUP, (EntityPlayer)ChestSwap.mc.player);
                ChestSwap.mc.playerController.updateController();
            }
            this.toggle();
            return;
        }
        final int l_Slot = this.FindChestItem(l_ChestSlot.getItem() instanceof ItemArmor);
        if (l_Slot != -1) {
            ChestSwap.mc.playerController.windowClick(ChestSwap.mc.player.inventoryContainer.windowId, l_Slot, 0, ClickType.PICKUP, (EntityPlayer)ChestSwap.mc.player);
            ChestSwap.mc.playerController.windowClick(ChestSwap.mc.player.inventoryContainer.windowId, 6, 0, ClickType.PICKUP, (EntityPlayer)ChestSwap.mc.player);
            ChestSwap.mc.playerController.windowClick(ChestSwap.mc.player.inventoryContainer.windowId, l_Slot, 0, ClickType.PICKUP, (EntityPlayer)ChestSwap.mc.player);
            ChestSwap.mc.playerController.updateController();
        }
        this.toggle();
    }
    
    private int FindChestItem(final boolean p_Elytra) {
        int slot = -1;
        float damage = 0.0f;
        for (int i = 0; i < ChestSwap.mc.player.inventoryContainer.getInventory().size(); ++i) {
            if (i != 0 && i != 5 && i != 6 && i != 7) {
                if (i != 8) {
                    final ItemStack s = (ItemStack)ChestSwap.mc.player.inventoryContainer.getInventory().get(i);
                    if (s != null && s.getItem() != Items.AIR) {
                        if (s.getItem() instanceof ItemArmor) {
                            final ItemArmor armor = (ItemArmor)s.getItem();
                            if (armor.armorType == EntityEquipmentSlot.CHEST) {
                                final float currentDamage = (float)(armor.damageReduceAmount + EnchantmentHelper.getEnchantmentLevel(Enchantments.PROTECTION, s));
                                final boolean cursed = EnchantmentHelper.hasBindingCurse(s);
                                if (currentDamage > damage && !cursed) {
                                    damage = currentDamage;
                                    slot = i;
                                }
                            }
                        }
                        else if (p_Elytra && s.getItem() instanceof ItemElytra) {
                            return i;
                        }
                    }
                }
            }
        }
        return slot;
    }
}
