//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.features.setting.*;

public class Inspector extends Module
{
    public static Inspector Instance;
    public Setting<Boolean> send;
    public Setting<Boolean> recv;
    
    public static Inspector getInstance() {
        if (Inspector.Instance == null) {
            Inspector.Instance = new Inspector();
        }
        return Inspector.Instance;
    }
    
    public void setInstance() {
        Inspector.Instance = this;
    }
    
    public Inspector() {
        super("Inspector", "Does something", Category.MISC, true, false, false);
        this.send = (Setting<Boolean>)this.register(new Setting("Send", (T)false));
        this.recv = (Setting<Boolean>)this.register(new Setting("Recv", (T)false));
        this.setInstance();
    }
}
