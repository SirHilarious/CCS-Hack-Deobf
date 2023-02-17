//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.features.setting.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import me.alpha432.oyvey.*;
import me.alpha432.oyvey.util.*;
import me.alpha432.oyvey.features.modules.client.*;
import com.mojang.realmsclient.gui.*;
import net.minecraft.init.*;
import java.util.*;
import me.alpha432.oyvey.features.command.*;

public class VisualRange extends Module
{
    private Setting<Boolean> sound;
    private List<String> knownPlayers;
    private Set<EntityPlayer> str;
    
    public VisualRange() {
        super("VisualRange", "Tells you when a player enters your render distance", Category.MISC, false, false, false);
        this.sound = (Setting<Boolean>)this.register(new Setting("Sound", (T)false));
        this.knownPlayers = new ArrayList<String>();
        this.str = Collections.newSetFromMap(new WeakHashMap<EntityPlayer, Boolean>());
    }
    
    @Override
    public void onToggle() {
        this.knownPlayers = new ArrayList<String>();
    }
    
    @Override
    public void onTick() {
        final List<String> tickPlayerList = new ArrayList<String>();
        try {
            for (final Entity entity : VisualRange.mc.world.getLoadedEntityList()) {
                if (entity instanceof EntityPlayer) {
                    tickPlayerList.add(entity.getName());
                }
            }
        }
        catch (Exception ex) {}
        if (tickPlayerList.size() > 0) {
            for (final String playerName : tickPlayerList) {
                if (playerName.equals(VisualRange.mc.player.getName())) {
                    continue;
                }
                if (!this.knownPlayers.contains(playerName)) {
                    this.knownPlayers.add(playerName);
                    if (OyVey.friendManager.isFriend(playerName)) {
                        this.sendNotification("§b" + playerName + TextUtil.coloredString(" Has entered your Visual Range!", HUD.getInstance().commandColor.getValue()));
                    }
                    else {
                        this.sendNotification(ChatFormatting.RED.toString() + playerName + TextUtil.coloredString(" Has entered your Visual Range!", HUD.getInstance().commandColor.getValue()));
                    }
                    if (this.sound.getValue()) {
                        VisualRange.mc.player.playSound(SoundEvents.BLOCK_NOTE_CHIME, 0.5f, 1.0f);
                    }
                    return;
                }
            }
        }
        if (this.knownPlayers.size() > 0) {
            for (final String playerName : this.knownPlayers) {
                if (!tickPlayerList.contains(playerName)) {
                    this.knownPlayers.remove(playerName);
                }
            }
        }
    }
    
    private void sendNotification(final String s) {
        Command.sendMessage(s);
    }
}
