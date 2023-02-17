//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.features.setting.*;
import me.alpha432.oyvey.util.*;
import net.minecraft.inventory.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.init.*;

public class Offhand2 extends Module
{
    public Setting<Float> ToggleHealth;
    public Setting<Boolean> HotbarFirst;
    
    public Offhand2() {
        super("OffhandGapple", "Very original offhand", Category.COMBAT, true, false, false);
        this.ToggleHealth = (Setting<Float>)this.register(new Setting("Toggle Health", (T)13.0f, (T)0.1f, (T)36.0f));
        this.HotbarFirst = (Setting<Boolean>)this.register(new Setting("Horbar Prioritize", (T)true));
    }
    
    private void SwitchOffHandIfNeed() {
        final Item l_Item = this.GetItemFromModeVal();
        if (Offhand2.mc.player.getHeldItemOffhand().getItem() != l_Item) {
            final int l_Slot = this.HotbarFirst.getValue() ? PlayerUtil.GetRecursiveItemSlot(l_Item) : PlayerUtil.GetItemSlot(l_Item);
            if (l_Slot != -1) {
                Offhand2.mc.playerController.windowClick(Offhand2.mc.player.inventoryContainer.windowId, l_Slot, 0, ClickType.PICKUP, (EntityPlayer)Offhand2.mc.player);
                Offhand2.mc.playerController.windowClick(Offhand2.mc.player.inventoryContainer.windowId, 45, 0, ClickType.PICKUP, (EntityPlayer)Offhand2.mc.player);
                Offhand2.mc.playerController.windowClick(Offhand2.mc.player.inventoryContainer.windowId, l_Slot, 0, ClickType.PICKUP, (EntityPlayer)Offhand2.mc.player);
                Offhand2.mc.playerController.updateController();
            }
        }
    }
    
    @Override
    public void onUpdate() {
        if (Offhand2.mc.currentScreen != null && !(Offhand2.mc.currentScreen instanceof GuiInventory)) {
            return;
        }
        this.SwitchOffHandIfNeed();
    }
    
    public Item GetItemFromModeVal() {
        if (PlayerUtil.GetHealthWithAbsorption() < this.ToggleHealth.getValue()) {
            return Items.TOTEM_OF_UNDYING;
        }
        return Items.GOLDEN_APPLE;
    }
}
