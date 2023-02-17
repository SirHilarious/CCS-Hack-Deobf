//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.*;
import net.minecraft.entity.player.*;
import me.alpha432.oyvey.event.events.*;
import net.minecraft.network.play.server.*;
import me.alpha432.oyvey.features.command.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.init.*;
import com.mojang.realmsclient.gui.*;
import java.util.*;

public class PotionDetect extends Module
{
    private Set<EntityPlayer> str;
    
    public PotionDetect() {
        super("PotionDetect", "Detects the potions someone has", Category.MISC, true, false, false);
        this.str = Collections.newSetFromMap(new WeakHashMap<EntityPlayer, Boolean>());
    }
    
    @Override
    public void onToggle() {
        this.str = Collections.newSetFromMap(new WeakHashMap<EntityPlayer, Boolean>());
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketEntityEffect) {
            final SPacketEntityEffect sPacketEntityEffect = (SPacketEntityEffect)event.getPacket();
            Command.sendMessage("Potion ID: " + sPacketEntityEffect.getEntityId());
        }
    }
    
    @Override
    public void onUpdate() {
        for (final EntityPlayer player : PotionDetect.mc.world.playerEntities) {
            if (player.equals((Object)PotionDetect.mc.player)) {
                continue;
            }
            if (player.isPotionActive(MobEffects.STRENGTH) && !this.str.contains(player)) {
                Command.sendMessage("§d" + player.getName() + ChatFormatting.AQUA + " has strength");
                this.str.add(player);
            }
            if (!this.str.contains(player)) {
                continue;
            }
            if (player.isPotionActive(MobEffects.STRENGTH)) {
                continue;
            }
            Command.sendMessage("§d" + player.getName() + ChatFormatting.AQUA + " no longer has strength!");
            this.str.remove(player);
        }
    }
}
