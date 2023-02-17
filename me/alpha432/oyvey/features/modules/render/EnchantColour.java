//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.features.setting.*;

public class EnchantColour extends Module
{
    private Setting<Integer> r;
    private Setting<Integer> g;
    private Setting<Integer> b;
    public static EnchantColour INSTANCE;
    
    public EnchantColour() {
        super("EnchantColour", "Changes the color of your enchtants", Module.Category.RENDER, false, false, false);
        this.r = (Setting<Integer>)this.register(new Setting("Red", (T)255, (T)0, (T)255));
        this.g = (Setting<Integer>)this.register(new Setting("Green", (T)0, (T)0, (T)255));
        this.b = (Setting<Integer>)this.register(new Setting("Blue", (T)0, (T)0, (T)255));
        EnchantColour.INSTANCE = this;
    }
    
    public Setting<Integer> getR() {
        return this.r;
    }
    
    public void setR(final Setting<Integer> r) {
        this.r = r;
    }
    
    public Setting<Integer> getG() {
        return this.g;
    }
    
    public void setG(final Setting<Integer> g) {
        this.g = g;
    }
    
    public Setting<Integer> getB() {
        return this.b;
    }
    
    public void setB(final Setting<Integer> b) {
        this.b = b;
    }
}
