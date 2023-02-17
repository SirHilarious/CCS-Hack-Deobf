//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.features.setting.*;

public class ViewModel extends Module
{
    public static ViewModel INSTANCE;
    public Setting<Float> posX;
    public Setting<Float> posY;
    public Setting<Float> posZ;
    public Setting<Float> sizex;
    public Setting<Float> sizey;
    public Setting<Float> sizez;
    public Setting<Float> x;
    public Setting<Float> y;
    public Setting<Float> z;
    public Setting<Boolean> doSwing;
    public Setting<Float> swing;
    
    public static ViewModel getInstance() {
        if (ViewModel.INSTANCE == null) {
            ViewModel.INSTANCE = new ViewModel();
        }
        return ViewModel.INSTANCE;
    }
    
    public ViewModel() {
        super("ViewModel", "Changes your ViewModel", Module.Category.RENDER, true, false, false);
        this.posX = (Setting<Float>)this.register(new Setting("Offset X", (T)0.0f, (T)(-4.0f), (T)4.0f));
        this.posY = (Setting<Float>)this.register(new Setting("Offset Y", (T)0.0f, (T)(-4.0f), (T)4.0f));
        this.posZ = (Setting<Float>)this.register(new Setting("Offset Z", (T)0.0f, (T)(-4.0f), (T)4.0f));
        this.sizex = (Setting<Float>)this.register(new Setting("Size X", (T)1.0f, (T)(-4.0f), (T)4.0f));
        this.sizey = (Setting<Float>)this.register(new Setting("Size Y", (T)1.0f, (T)(-4.0f), (T)4.0f));
        this.sizez = (Setting<Float>)this.register(new Setting("Size Z", (T)1.0f, (T)(-4.0f), (T)4.0f));
        this.x = (Setting<Float>)this.register(new Setting("Rotation X", (T)1.0f, (T)0.0f, (T)1.0f));
        this.y = (Setting<Float>)this.register(new Setting("Rotation Y", (T)1.0f, (T)0.0f, (T)1.0f));
        this.z = (Setting<Float>)this.register(new Setting("Rotation Z", (T)1.0f, (T)0.0f, (T)1.0f));
        this.doSwing = (Setting<Boolean>)this.register(new Setting("Modify Swing", (T)true));
        this.swing = (Setting<Float>)this.register(new Setting("Swing Amount", (T)1.0f, (T)0.0f, (T)1.0f));
        ViewModel.INSTANCE = this;
    }
}
