//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.features.command.*;

public class Debug extends Module
{
    public Debug() {
        super("Debug", "Shows debug info.", Category.MISC, true, false, false);
    }
    
    @Override
    public void onUpdate() {
        Command.sendMessage("[NoClip: " + Debug.mc.player.noClip + "] [ColliedH: " + Debug.mc.player.collidedHorizontally + "] [ColliedV: " + Debug.mc.player.collidedVertically + "]");
    }
}
