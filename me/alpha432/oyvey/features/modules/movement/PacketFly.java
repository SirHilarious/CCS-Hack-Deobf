//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.features.setting.*;
import com.mojang.realmsclient.gui.*;
import java.util.*;
import net.minecraft.network.*;
import net.minecraft.entity.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.alpha432.oyvey.features.command.*;
import net.minecraft.client.gui.*;
import me.alpha432.oyvey.util.*;
import me.alpha432.oyvey.event.events.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.*;

public class PacketFly extends Module
{
    public static PacketFly INSTANCE;
    private final Setting<Boolean> debug;
    private final Setting<modes> mode;
    private final Setting<Integer> factorAmount;
    private final Setting<types> type;
    private final Setting<Boolean> limit;
    private final Setting<Boolean> frequency;
    private final Setting<Boolean> fluctuate;
    private final Setting<Integer> frequencyAmount;
    private final Setting<Integer> limitAmount;
    private final Setting<Boolean> factorize;
    private final Setting<Integer> fluctuateMin;
    private final Setting<Integer> fluctuateMax;
    private final Setting<Float> additional;
    private final List<CPacketPlayer> packets;
    private int teleportID;
    private int clock;
    private int flightCounter;
    private float factorAmt;
    private float limitAmt;
    private int discrim;
    private boolean countingUp;
    
    public PacketFly() {
        super("PacketFlyOld", "Uses packets to allow you to phase and fly", Module.Category.MOVEMENT, true, false, false);
        this.debug = (Setting<Boolean>)this.register(new Setting("Debug", (T)false));
        this.mode = (Setting<modes>)this.register(new Setting("Mode", (T)modes.FAST));
        this.factorAmount = (Setting<Integer>)this.register(new Setting("Factor", (T)3, (T)1, (T)15));
        this.type = (Setting<types>)this.register(new Setting("Type", (T)types.DIFFER));
        this.limit = (Setting<Boolean>)this.register(new Setting("Limit", (T)true));
        this.frequency = (Setting<Boolean>)this.register(new Setting("Frequency", (T)true));
        this.fluctuate = (Setting<Boolean>)this.register(new Setting("Fluctuate", (T)false));
        this.frequencyAmount = (Setting<Integer>)this.register(new Setting("Frequency Amount", (T)10, (T)1, (T)10, v -> this.frequency.getValue()));
        this.limitAmount = (Setting<Integer>)this.register(new Setting("Limit Amount", (T)10, (T)1, (T)10, v -> this.limit.getValue() && !this.fluctuate.getValue()));
        this.factorize = (Setting<Boolean>)this.register(new Setting("Factorize", (T)true));
        this.fluctuateMin = (Setting<Integer>)this.register(new Setting("Factor Min", (T)1, (T)1, (T)15, v -> this.factorize.getValue()));
        this.fluctuateMax = (Setting<Integer>)this.register(new Setting("Factor Max", (T)4, (T)1, (T)15, v -> this.factorize.getValue()));
        this.additional = (Setting<Float>)this.register(new Setting("Inheritance", (T)0.0f, (T)(-0.5f), (T)1.0f));
        this.packets = new ArrayList<CPacketPlayer>();
        this.teleportID = 0;
        this.clock = 0;
        this.flightCounter = 0;
        this.factorAmt = 0.0f;
        this.limitAmt = 0.0f;
        this.discrim = 0;
        this.countingUp = true;
    }
    
    public static PacketFly getInstance() {
        if (PacketFly.INSTANCE == null) {
            PacketFly.INSTANCE = new PacketFly();
        }
        return PacketFly.INSTANCE;
    }
    
    public String getDisplayInfo() {
        if (this.mode.getValue() == modes.FACTOR) {
            return this.mode.getValue() + ", F: " + this.factorAmt + ", " + (this.limit.getValue() ? (" L: " + this.limitAmt + ", ") : "") + this.discrim + ", " + (this.checkHitBoxes() ? (ChatFormatting.GREEN + "Phase") : (ChatFormatting.RED + "Non-Phase"));
        }
        return this.mode.getValue() + (this.limit.getValue() ? (", L: " + this.limitAmt + ", ") : ", ") + this.discrim + ", " + (this.checkHitBoxes() ? (ChatFormatting.GREEN + " Phase") : (ChatFormatting.DARK_RED + " Non-Phase"));
    }
    
    private double[] getBounds(final double motionX, final double motionY, final double motionZ) {
        switch (this.type.getValue()) {
            case UP: {
                return new double[] { PacketFly.mc.player.posX + motionX, PacketFly.mc.player.posY + motionY + 1337.69, PacketFly.mc.player.posZ + motionZ };
            }
            case DOWN: {
                return new double[] { PacketFly.mc.player.posX + motionX, PacketFly.mc.player.posY + motionY - 42069.0, PacketFly.mc.player.posZ + motionZ };
            }
            case PRESERVE: {
                return new double[] { PacketFly.mc.player.posX + motionX + new Random().nextInt(60000000) - 3.0E7, PacketFly.mc.player.posY + motionY, PacketFly.mc.player.posZ + motionZ + new Random().nextInt(60000000) - 3.0E7 };
            }
            case LIMITJITTER: {
                return new double[] { PacketFly.mc.player.posX + motionX + new Random().nextInt(100) - 50.0, PacketFly.mc.player.posY + motionY + new Random().nextInt(840) - 420.0, PacketFly.mc.player.posZ + motionZ + new Random().nextInt(100) - 50.0 };
            }
            case DIFFER: {
                switch (this.discrim) {
                    case 0: {
                        return new double[] { PacketFly.mc.player.posX + motionX, PacketFly.mc.player.posY + motionY - 256.0, PacketFly.mc.player.posZ + motionZ };
                    }
                    case 1: {
                        return new double[] { PacketFly.mc.player.posX + motionX, PacketFly.mc.player.posY + motionY + 256.0, PacketFly.mc.player.posZ + motionZ };
                    }
                    case 2: {
                        return new double[] { PacketFly.mc.player.posX + motionX + 256.0, PacketFly.mc.player.posY + motionY, PacketFly.mc.player.posZ + motionZ - 256.0 };
                    }
                    case 3: {
                        return new double[] { PacketFly.mc.player.posX + motionX, PacketFly.mc.player.posY + motionY - 42069.0, PacketFly.mc.player.posZ + motionZ };
                    }
                    default: {
                        return new double[] { PacketFly.mc.player.posX + motionX, 256.0, PacketFly.mc.player.posZ + motionZ };
                    }
                }
                break;
            }
            case BOUNDED: {
                return new double[] { PacketFly.mc.player.posX + motionX, 256.0, PacketFly.mc.player.posZ + motionZ };
            }
            case SPECIAL: {
                return new double[] { -8043809.0, 1337.69, -203912.0 };
            }
            case CONCEAL: {
                return new double[] { PacketFly.mc.player.posX + motionX + new Random().nextInt(2000000) - 1000000.0, PacketFly.mc.player.posY + motionY + 256.0, PacketFly.mc.player.posZ + motionZ + new Random().nextInt(2000000) - 1000000.0 };
            }
            default: {
                return new double[] { PacketFly.mc.player.posX + motionX, PacketFly.mc.player.posY + motionY + 1337.69, PacketFly.mc.player.posZ + motionZ };
            }
        }
    }
    
    private float getFactorAmount() {
        if (this.mode.getValue() != modes.FACTOR) {
            return 1.0f;
        }
        if (this.factorize.getValue()) {
            return (float)(new Random().nextInt(this.fluctuateMax.getValue() - this.fluctuateMin.getValue() + 1) + this.fluctuateMin.getValue());
        }
        return this.factorAmount.getValue();
    }
    
    private float getLimitAmount() {
        if (this.fluctuate.getValue()) {
            return (float)(new Random().nextInt(10) + 1);
        }
        return this.limitAmount.getValue();
    }
    
    public void onDisable() {
        PacketFly.mc.player.noClip = false;
        PacketFly.mc.player.collidedHorizontally = false;
        PacketFly.mc.player.collidedVertically = false;
        final double[] bounds = this.getBounds(0.0, 0.0, 0.0);
        final CPacketPlayer.PositionRotation outOfBoundsPosition = new CPacketPlayer.PositionRotation(bounds[0], bounds[1], bounds[2], PacketFly.mc.player.rotationYaw, PacketFly.mc.player.rotationPitch, PacketFly.mc.player.onGround);
        this.packets.add((CPacketPlayer)outOfBoundsPosition);
        PacketFly.mc.player.connection.sendPacket((Packet)outOfBoundsPosition);
    }
    
    public void onEnable() {
        if (PacketFly.mc.player != null) {
            this.teleportID = 0;
            this.clock = 0;
            this.factorAmt = 0.0f;
            this.limitAmt = 0.0f;
            final double[] bounds = this.getBounds(0.0, 0.0, 0.0);
            final CPacketPlayer.PositionRotation outOfBoundsPosition = new CPacketPlayer.PositionRotation(bounds[0], bounds[1], bounds[2], PacketFly.mc.player.rotationYaw, PacketFly.mc.player.rotationPitch, PacketFly.mc.player.onGround);
            this.packets.add((CPacketPlayer)outOfBoundsPosition);
            PacketFly.mc.player.connection.sendPacket((Packet)outOfBoundsPosition);
        }
    }
    
    private boolean checkHitBoxes() {
        return !PacketFly.mc.world.getCollisionBoxes((Entity)PacketFly.mc.player, PacketFly.mc.player.getEntityBoundingBox().expand(-0.0625, -0.0625, -0.0625)).isEmpty();
    }
    
    @SubscribeEvent
    public void onMove(final MoveEvent event) {
        event.setCanceled(true);
    }
    
    private boolean resetCounter(final int counter) {
        if (++this.flightCounter >= counter) {
            if (this.debug.getValue()) {
                Command.sendMessage(counter + " Exceeded");
            }
            this.flightCounter = 0;
            return true;
        }
        return false;
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 1) {
            return;
        }
        if (PacketFly.mc.player == null || PacketFly.mc.world == null) {
            this.disable();
            return;
        }
        if (PacketFly.mc.currentScreen instanceof GuiDownloadTerrain) {
            PacketFly.mc.currentScreen = null;
            return;
        }
        PacketFly.mc.player.noClip = false;
        PacketFly.mc.player.motionX = 0.0;
        PacketFly.mc.player.motionY = 0.0;
        PacketFly.mc.player.motionZ = 0.0;
        PacketFly.mc.player.setVelocity(0.0, 0.0, 0.0);
        double factorizes;
        if (this.checkHitBoxes()) {
            if (PacketFly.mc.player.movementInput.jump || PacketFly.mc.player.movementInput.sneak) {
                factorizes = 0.032;
            }
            else {
                factorizes = 0.062 + this.additional.getValue() / 10.0f;
            }
        }
        else if (PacketFly.mc.player.movementInput.jump || PacketFly.mc.player.movementInput.sneak) {
            factorizes = (this.resetCounter(20) ? -0.001 : 0.032);
        }
        else {
            factorizes = (this.resetCounter(20) ? 0.016 : (0.031 + this.additional.getValue() / 10.0f));
        }
        final double[] strafing = MathUtil.directionSpeed(factorizes);
        if (this.checkHitBoxes()) {
            PacketFly.mc.player.noClip = true;
        }
        final float speed = (float)((!PacketFly.mc.player.movementInput.jump || (!this.checkHitBoxes() && EntityUtil.isMoving())) ? (PacketFly.mc.player.movementInput.sneak ? -0.062 : (this.checkHitBoxes() ? 0.0 : (this.resetCounter(4) ? -0.01 : 0.0))) : (this.checkHitBoxes() ? 0.062 : (this.resetCounter(20) ? -0.032 : 0.062)));
        if (speed == 0.0f && strafing[0] == 0.0 && strafing[1] == 0.0) {
            return;
        }
        this.factorAmt = this.getFactorAmount();
        this.limitAmt = this.getLimitAmount();
        if (this.discrim >= 3) {
            this.countingUp = false;
        }
        else if (this.discrim <= 0) {
            this.countingUp = true;
        }
        if (this.countingUp) {
            ++this.discrim;
        }
        else {
            --this.discrim;
        }
        this.clock = 0;
        int multiplier = 1;
        while (multiplier <= ((this.mode.getValue() == modes.FACTOR) ? this.factorAmt : 1.0f)) {
            final double motionX = strafing[0] * multiplier;
            final double motionY = speed * multiplier;
            final double motionZ = strafing[1] * multiplier;
            if (this.limit.getValue()) {
                if (this.clock++ <= this.limitAmt) {
                    continue;
                }
                final CPacketPlayer.PositionRotation newPosition = new CPacketPlayer.PositionRotation(PacketFly.mc.player.posX + motionX, PacketFly.mc.player.posY + motionY, PacketFly.mc.player.posZ + motionZ, PacketFly.mc.player.rotationYaw, PacketFly.mc.player.rotationPitch, PacketFly.mc.player.onGround);
                this.packets.add((CPacketPlayer)newPosition);
                PacketFly.mc.player.connection.sendPacket((Packet)newPosition);
                if (this.frequency.getValue()) {
                    for (int f = 0; f < this.frequencyAmount.getValue(); ++f) {
                        final double[] bounds = this.getBounds(motionX, motionY, motionZ);
                        final CPacketPlayer.PositionRotation outOfBoundsPosition = new CPacketPlayer.PositionRotation(bounds[0], bounds[1], bounds[2], PacketFly.mc.player.rotationYaw, PacketFly.mc.player.rotationPitch, PacketFly.mc.player.onGround);
                        this.packets.add((CPacketPlayer)outOfBoundsPosition);
                        PacketFly.mc.player.connection.sendPacket((Packet)outOfBoundsPosition);
                    }
                }
                else {
                    final double[] bounds2 = this.getBounds(motionX, motionY, motionZ);
                    final CPacketPlayer.PositionRotation outOfBoundsPosition2 = new CPacketPlayer.PositionRotation(bounds2[0], bounds2[1], bounds2[2], PacketFly.mc.player.rotationYaw, PacketFly.mc.player.rotationPitch, PacketFly.mc.player.onGround);
                    this.packets.add((CPacketPlayer)outOfBoundsPosition2);
                    PacketFly.mc.player.connection.sendPacket((Packet)outOfBoundsPosition2);
                }
                ++multiplier;
            }
            else {
                final CPacketPlayer.PositionRotation newPosition = new CPacketPlayer.PositionRotation(PacketFly.mc.player.posX + motionX, PacketFly.mc.player.posY + motionY, PacketFly.mc.player.posZ + motionZ, PacketFly.mc.player.rotationYaw, PacketFly.mc.player.rotationPitch, PacketFly.mc.player.onGround);
                this.packets.add((CPacketPlayer)newPosition);
                PacketFly.mc.player.connection.sendPacket((Packet)newPosition);
                if (this.frequency.getValue()) {
                    for (int f = 0; f < this.frequencyAmount.getValue(); ++f) {
                        final double[] bounds = this.getBounds(motionX, motionY, motionZ);
                        final CPacketPlayer.PositionRotation outOfBoundsPosition = new CPacketPlayer.PositionRotation(bounds[0], bounds[1], bounds[2], PacketFly.mc.player.rotationYaw, PacketFly.mc.player.rotationPitch, PacketFly.mc.player.onGround);
                        this.packets.add((CPacketPlayer)outOfBoundsPosition);
                        PacketFly.mc.player.connection.sendPacket((Packet)outOfBoundsPosition);
                    }
                }
                else {
                    final double[] bounds2 = this.getBounds(motionX, motionY, motionZ);
                    final CPacketPlayer.PositionRotation outOfBoundsPosition2 = new CPacketPlayer.PositionRotation(bounds2[0], bounds2[1], bounds2[2], PacketFly.mc.player.rotationYaw, PacketFly.mc.player.rotationPitch, PacketFly.mc.player.onGround);
                    this.packets.add((CPacketPlayer)outOfBoundsPosition2);
                    PacketFly.mc.player.connection.sendPacket((Packet)outOfBoundsPosition2);
                }
                ++multiplier;
            }
        }
    }
    
    @SubscribeEvent
    public void onPushOutOfBlocks(final PushEvent event) {
        if (event.getStage() == 1) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayer.Position || event.getPacket() instanceof CPacketPlayer.Rotation) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketPlayer) {
            final CPacketPlayer packetPlayer = (CPacketPlayer)event.getPacket();
            if (this.packets.contains(packetPlayer)) {
                if (this.debug.getValue()) {
                    Command.sendMessage("Sending: " + packetPlayer.x + " " + packetPlayer.y + " " + packetPlayer.z);
                }
                this.packets.remove(packetPlayer);
            }
            else {
                event.setCanceled(true);
            }
        }
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (PacketFly.mc.player == null || PacketFly.mc.world == null) {
            return;
        }
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            final SPacketPlayerPosLook pak = (SPacketPlayerPosLook)event.getPacket();
            if (this.debug.getValue()) {
                Command.sendMessage("LagBack Detected: " + pak.getX() + " " + pak.getY() + " " + pak.getZ() + " L" + this.limitAmt + " F" + this.factorAmt + " D" + this.discrim);
            }
            this.teleportID = pak.getTeleportId();
            PacketFly.mc.player.setPosition(pak.getX(), pak.getY(), pak.getZ());
            PacketFly.mc.player.connection.sendPacket((Packet)new CPacketConfirmTeleport(this.teleportID++));
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketEntityVelocity) {
            final SPacketEntityVelocity packet = (SPacketEntityVelocity)event.getPacket();
            if (packet.entityID == PacketFly.mc.player.entityId) {
                packet.motionX = 0;
                packet.motionY = 0;
                packet.motionZ = 0;
            }
        }
    }
    
    private enum modes
    {
        FAST, 
        FACTOR;
    }
    
    private enum types
    {
        UP, 
        DOWN, 
        PRESERVE, 
        LIMITJITTER, 
        DIFFER, 
        BOUNDED, 
        SPECIAL, 
        CONCEAL;
    }
}
