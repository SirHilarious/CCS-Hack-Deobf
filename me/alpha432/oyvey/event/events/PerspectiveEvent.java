//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.event.events;

import me.alpha432.oyvey.event.*;

public class PerspectiveEvent extends EventStage
{
    private float aspect;
    
    public PerspectiveEvent(final float aspect) {
        this.aspect = aspect;
    }
    
    public float getAspect() {
        return this.aspect;
    }
    
    public void setAspect(final float aspect) {
        this.aspect = aspect;
    }
}
