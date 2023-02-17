//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.features.setting.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.common.*;

public class SkyColour extends Module
{
    private Setting<Integer> Ored;
    private Setting<Integer> Ogreen;
    private Setting<Integer> Oblue;
    
    public SkyColour() {
        super("SkyColour", "Changes the colour of the sky", Module.Category.RENDER, true, false, false);
        this.Ored = (Setting<Integer>)this.register(new Setting("Red", (T)0, (T)0, (T)255));
        this.Ogreen = (Setting<Integer>)this.register(new Setting("Green", (T)0, (T)0, (T)255));
        this.Oblue = (Setting<Integer>)this.register(new Setting("Blue", (T)0, (T)0, (T)255));
    }
    
    @SubscribeEvent
    public void fog_colour(final EntityViewRenderEvent.FogColors event) {
        try {
            event.setRed(this.Ored.getValue() / 255.0f);
            event.setGreen(this.Ogreen.getValue() / 255.0f);
            event.setBlue(this.Oblue.getValue() / 255.0f);
        }
        catch (Exception ex) {}
    }
    
    @SubscribeEvent
    public void fog_density(final EntityViewRenderEvent.FogDensity event) {
        try {
            event.setDensity(0.0f);
            event.setCanceled(true);
        }
        catch (Exception ex) {}
    }
    
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
    
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
}
