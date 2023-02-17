//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.features.setting.*;
import me.alpha432.oyvey.event.events.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class AspectRatio extends Module
{
    private Setting<Double> aspect;
    
    public AspectRatio() {
        super("AspectRatio", "Changes your aspect ratio", Module.Category.RENDER, true, false, false);
        this.aspect = (Setting<Double>)this.register(new Setting("Ratio", (T)(AspectRatio.mc.displayWidth / AspectRatio.mc.displayHeight + 0.0), (T)0.1, (T)3.0));
    }
    
    @SubscribeEvent
    public void onPerspectiveEvent(final PerspectiveEvent event) {
        event.setAspect(this.aspect.getValue().floatValue());
    }
}
