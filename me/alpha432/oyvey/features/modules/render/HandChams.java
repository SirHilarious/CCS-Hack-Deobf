//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.features.setting.*;

public class HandChams extends Module
{
    private static HandChams INSTANCE;
    public Setting<RenderMode> mode;
    public Setting<Integer> red;
    public Setting<Integer> green;
    public Setting<Integer> blue;
    public Setting<Integer> alpha;
    
    public HandChams() {
        super("HandChams", "Changes your hand color.", Module.Category.RENDER, false, false, false);
        this.mode = (Setting<RenderMode>)this.register(new Setting("Mode", (T)RenderMode.SOLID));
        this.red = (Setting<Integer>)this.register(new Setting("Red", (T)255, (T)0, (T)255));
        this.green = (Setting<Integer>)this.register(new Setting("Green", (T)0, (T)0, (T)255));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", (T)0, (T)0, (T)255));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", (T)240, (T)0, (T)255));
        this.setInstance();
    }
    
    public static HandChams getINSTANCE() {
        if (HandChams.INSTANCE == null) {
            HandChams.INSTANCE = new HandChams();
        }
        return HandChams.INSTANCE;
    }
    
    private void setInstance() {
        HandChams.INSTANCE = this;
    }
    
    static {
        HandChams.INSTANCE = new HandChams();
    }
    
    public enum RenderMode
    {
        SOLID, 
        WIREFRAME;
    }
}
