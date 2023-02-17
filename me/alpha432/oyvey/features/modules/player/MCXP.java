//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.*;
import org.lwjgl.input.*;
import net.minecraft.item.*;
import me.alpha432.oyvey.util.*;
import net.minecraft.network.*;
import net.minecraft.util.*;
import net.minecraft.network.play.client.*;

public class MCXP extends Module
{
    private int oldslot;
    
    public MCXP() {
        super("MCXP", "Throws XP Using Magic", Module.Category.PLAYER, false, false, false);
        this.oldslot = -1;
    }
    
    public void onEnable() {
        if (fullNullCheck()) {
            this.disable();
        }
    }
    
    public void onTick() {
        try {
            this.oldslot = MCXP.mc.player.inventory.currentItem;
        }
        catch (Exception e) {
            return;
        }
        if (Mouse.isButtonDown(2)) {
            this.throwXP();
        }
    }
    
    private void throwXP() {
        final int xpSlot = InventoryUtil.findHotbarBlock(ItemExpBottle.class);
        if (xpSlot != -1) {
            MCXP.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(xpSlot));
            MCXP.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            MCXP.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.oldslot));
        }
    }
}
