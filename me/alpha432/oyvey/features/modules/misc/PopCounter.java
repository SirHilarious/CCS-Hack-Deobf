//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.*;
import java.util.*;
import me.alpha432.oyvey.features.setting.*;
import net.minecraft.entity.player.*;
import com.mojang.realmsclient.gui.*;
import me.alpha432.oyvey.features.command.*;

public class PopCounter extends Module
{
    public static HashMap<String, Integer> TotemPopContainer;
    private static PopCounter INSTANCE;
    private Setting<Boolean> showOwn;
    
    public PopCounter() {
        super("PopCounter", "Counts other players totem pops.", Category.MISC, true, false, true);
        this.showOwn = (Setting<Boolean>)this.register(new Setting("Show Own", (T)true));
        this.setInstance();
    }
    
    public static PopCounter getInstance() {
        if (PopCounter.INSTANCE == null) {
            PopCounter.INSTANCE = new PopCounter();
        }
        return PopCounter.INSTANCE;
    }
    
    private void setInstance() {
        PopCounter.INSTANCE = this;
    }
    
    @Override
    public void onEnable() {
        PopCounter.TotemPopContainer.clear();
    }
    
    public void onDeath(final EntityPlayer player) {
        if (PopCounter.TotemPopContainer.containsKey(player.getName())) {
            final int l_Count = PopCounter.TotemPopContainer.get(player.getName());
            PopCounter.TotemPopContainer.remove(player.getName());
            if (this.isOn()) {
                Command.sendTempMessageID("§d" + player.getName() + ChatFormatting.AQUA + " died after popping their " + ChatFormatting.GREEN + l_Count + this.getPopString(l_Count) + ChatFormatting.AQUA + " Totem!", -42069);
            }
        }
    }
    
    public void onTotemPop(final EntityPlayer player) {
        if (fullNullCheck()) {
            return;
        }
        if (PopCounter.mc.player.equals((Object)player) && !this.showOwn.getValue()) {
            return;
        }
        int l_Count = 1;
        if (PopCounter.TotemPopContainer.containsKey(player.getName())) {
            l_Count = PopCounter.TotemPopContainer.get(player.getName());
            PopCounter.TotemPopContainer.put(player.getName(), ++l_Count);
        }
        else {
            PopCounter.TotemPopContainer.put(player.getName(), l_Count);
        }
        if (this.isOn()) {
            Command.sendTempMessageID("§d" + player.getName() + ChatFormatting.AQUA + " popped their " + ChatFormatting.RED + l_Count + this.getPopString(l_Count) + ChatFormatting.AQUA + " Totem.", -1337);
        }
    }
    
    public String getPopString(final int pops) {
        if (pops == 1) {
            return "st";
        }
        if (pops == 2) {
            return "nd";
        }
        if (pops == 3) {
            return "rd";
        }
        if (pops >= 4 && pops < 21) {
            return "th";
        }
        final int lastDigit = pops % 10;
        if (lastDigit == 1) {
            return "st";
        }
        if (lastDigit == 2) {
            return "nd";
        }
        if (lastDigit == 3) {
            return "rd";
        }
        return "th";
    }
    
    static {
        PopCounter.TotemPopContainer = new HashMap<String, Integer>();
        PopCounter.INSTANCE = new PopCounter();
    }
}
