//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.features.setting.*;

public class CrystalScale extends Module
{
    public Setting<Float> scale;
    public static CrystalScale INSTANCE;
    
    public CrystalScale() {
        super("CrystalScale", "", Module.Category.RENDER, true, false, false);
        this.scale = (Setting<Float>)this.register(new Setting("Scale", (T)6.25f, (T)0.01f, (T)10.0f));
        CrystalScale.INSTANCE = this;
    }
    
    public void onEnable() {
        CrystalScale.INSTANCE = this;
    }
    
    public static CrystalScale getInstance() {
        if (CrystalScale.INSTANCE == null) {
            CrystalScale.INSTANCE = new CrystalScale();
        }
        return CrystalScale.INSTANCE;
    }
    
    public float getScale() {
        return this.scale.getValue() / 10.0f;
    }
}
