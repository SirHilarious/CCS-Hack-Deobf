//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.event.events;

import me.alpha432.oyvey.event.*;
import net.minecraftforge.fml.common.eventhandler.*;

@Cancelable
public class PlayerTravelEvent extends EventStage
{
    public float Strafe;
    public float Vertical;
    public float Forward;
    
    public PlayerTravelEvent(final float Strafe, final float Vertical, final float Forward) {
        this.Strafe = Strafe;
        this.Vertical = Vertical;
        this.Forward = Forward;
    }
}
