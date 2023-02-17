//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.*;

public class Animations extends Module
{
    public static Animations INSTANCE;
    
    public Animations() {
        super("Animations", "Modify Animations", Module.Category.RENDER, false, false, false);
        Animations.INSTANCE = this;
    }
    
    public static Animations getInstance() {
        if (Animations.INSTANCE == null) {
            Animations.INSTANCE = new Animations();
        }
        return Animations.INSTANCE;
    }
}
