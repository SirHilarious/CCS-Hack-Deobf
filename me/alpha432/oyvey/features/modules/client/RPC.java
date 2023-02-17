//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.client;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.features.setting.*;
import me.alpha432.oyvey.*;

public class RPC extends Module
{
    public static RPC INSTANCE;
    public Setting<Boolean> gang;
    
    public RPC() {
        super("RPC", "Discord rich presence", Category.CLIENT, false, false, false);
        this.gang = (Setting<Boolean>)this.register(new Setting("hvh", (T)true));
        RPC.INSTANCE = this;
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        Discord.start();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        Discord.stop();
    }
    
    @Override
    public void onLoad() {
        super.onLoad();
        Discord.start();
    }
}
