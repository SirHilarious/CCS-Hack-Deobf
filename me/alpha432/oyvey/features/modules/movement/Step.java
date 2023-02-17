//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.features.setting.*;
import me.alpha432.oyvey.*;
import me.alpha432.oyvey.event.events.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.entity.*;
import net.minecraft.util.math.*;
import java.util.*;

public class Step extends Module
{
    private final double[] oneblockPositions;
    private final double[] onehalfblockPositions;
    private final double[] twoblockPositions;
    private int packets;
    private Setting<Float> height;
    private Setting<Mode> mode;
    private Setting<Boolean> timer;
    private double[] selectedPositions;
    
    public Step() {
        super("Step", "Step.", Module.Category.MOVEMENT, true, false, false);
        this.oneblockPositions = new double[] { 0.41999998688698, 0.7531999805212 };
        this.onehalfblockPositions = new double[] { 0.41999998688698, 0.7531999805212, 1.00133597911214, 1.16610926093821, 1.24918707874468, 1.1707870772188 };
        this.twoblockPositions = new double[] { 0.42, 0.78, 0.63, 0.51, 0.9, 1.21, 1.45, 1.43 };
        this.height = (Setting<Float>)this.register(new Setting("Height", (T)2.0f, (T)1.0f, (T)2.0f));
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", (T)Mode.Vanilla));
        this.timer = (Setting<Boolean>)this.register(new Setting("UseTimer", (T)false));
        this.selectedPositions = new double[0];
    }
    
    public void onToggle() {
        Step.mc.player.stepHeight = 0.6f;
    }
    
    public void onUpdate() {
        if (OyVey.moduleManager.isModuleEnabled("Speed")) {
            return;
        }
        if (this.mode.getValue() == Mode.Vanilla) {
            Step.mc.player.stepHeight = this.height.getValue();
        }
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (this.mode.getValue() != Mode.Normal) {
            return;
        }
        if (!Step.mc.player.collidedHorizontally) {
            return;
        }
        if (!Step.mc.player.onGround || Step.mc.player.isOnLadder() || Step.mc.player.isInWater() || Step.mc.player.isInLava() || Step.mc.player.movementInput.jump || Step.mc.player.noClip) {
            return;
        }
        if (Step.mc.player.moveForward == 0.0f && Step.mc.player.moveStrafing == 0.0f) {
            return;
        }
        if (Step.mc.player.collidedHorizontally && Step.mc.player.onGround) {
            ++this.packets;
        }
        final double n = this.get_n_normal();
        if (n < 0.0 || n > 2.0) {
            return;
        }
        Step.mc.timer.tickLength = 200.0f;
        if (n == 2.0 && this.packets > this.twoblockPositions.length - 2) {
            for (final double pos : this.twoblockPositions) {
                Step.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Step.mc.player.posX, Step.mc.player.posY + pos, Step.mc.player.posZ, true));
            }
            Step.mc.player.setPosition(Step.mc.player.posX, Step.mc.player.posY + 2.0, Step.mc.player.posZ);
            this.packets = 0;
        }
        if (n == 1.5 && this.packets > this.onehalfblockPositions.length - 2) {
            for (final double pos : this.onehalfblockPositions) {
                Step.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Step.mc.player.posX, Step.mc.player.posY + pos, Step.mc.player.posZ, true));
            }
            Step.mc.player.setPosition(Step.mc.player.posX, Step.mc.player.posY + this.onehalfblockPositions[this.onehalfblockPositions.length - 1], Step.mc.player.posZ);
            this.packets = 0;
        }
        if (n == 1.0 && this.packets > this.oneblockPositions.length - 2) {
            for (final double pos : this.oneblockPositions) {
                Step.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Step.mc.player.posX, Step.mc.player.posY + pos, Step.mc.player.posZ, true));
            }
            Step.mc.player.setPosition(Step.mc.player.posX, Step.mc.player.posY + this.oneblockPositions[this.oneblockPositions.length - 1], Step.mc.player.posZ);
            this.packets = 0;
        }
        Step.mc.timer.tickLength = 50.0f;
    }
    
    public double get_n_normal() {
        Step.mc.player.stepHeight = 0.5f;
        double max_y = -1.0;
        final AxisAlignedBB grow = Step.mc.player.getEntityBoundingBox().offset(0.0, 0.05, 0.0).grow(0.05);
        if (!Step.mc.world.getCollisionBoxes((Entity)Step.mc.player, grow.offset(0.0, 2.0, 0.0)).isEmpty()) {
            return 100.0;
        }
        for (final AxisAlignedBB aabb : Step.mc.world.getCollisionBoxes((Entity)Step.mc.player, grow)) {
            if (aabb.maxY > max_y) {
                max_y = aabb.maxY;
            }
        }
        return max_y - Step.mc.player.posY;
    }
    
    public enum Mode
    {
        Vanilla, 
        Normal;
    }
}
