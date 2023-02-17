//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.event.events;

import me.alpha432.oyvey.event.*;

public class UpdateWalkingPlayerEvent extends EventStage
{
    public float rotationYaw;
    public float rotationPitch;
    public double posX;
    public double posY;
    public double posZ;
    public boolean onGround;
    
    public UpdateWalkingPlayerEvent(final int stage, final float rotationYaw, final float rotationPitch, final double posX, final double posY, final double posZ, final boolean onGround) {
        super(stage);
        this.rotationYaw = rotationYaw;
        this.rotationPitch = rotationPitch;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.onGround = onGround;
    }
}
