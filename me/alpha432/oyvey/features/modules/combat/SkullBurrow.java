//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.*;
import net.minecraft.item.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.util.math.*;
import net.minecraft.util.*;
import me.alpha432.oyvey.util.*;

public class SkullBurrow extends Module
{
    public SkullBurrow() {
        super("SelfSkull", "Places skulls inside yourself", Category.COMBAT, false, false, false);
    }
    
    @Override
    public void onUpdate() {
        final int slot = InventoryUtil.findHotbarBlock(ItemSkull.class);
        if (slot == -1) {
            this.disable();
            return;
        }
        int lastSlot = -1;
        if (SkullBurrow.mc.player.getHeldItemMainhand().getItem().getClass() != ItemSkull.class) {
            lastSlot = SkullBurrow.mc.player.inventory.currentItem;
            SkullBurrow.mc.getConnection().sendPacket((Packet)new CPacketHeldItemChange(slot));
        }
        BlockUtil.placeBlock(new BlockPos(SkullBurrow.mc.player.getPositionVector()), EnumHand.MAIN_HAND, true, true, SkullBurrow.mc.player.isSneaking());
        if (lastSlot != -1) {
            SkullBurrow.mc.getConnection().sendPacket((Packet)new CPacketHeldItemChange(lastSlot));
        }
        this.disable();
    }
}
