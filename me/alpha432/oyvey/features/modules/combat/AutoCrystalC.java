//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.features.setting.*;
import net.minecraft.entity.item.*;
import me.alpha432.oyvey.features.modules.client.*;
import java.awt.*;
import me.alpha432.oyvey.event.events.*;
import net.minecraft.network.*;
import me.alpha432.oyvey.features.command.*;
import java.util.function.*;
import me.alpha432.oyvey.util.*;
import net.minecraft.item.*;
import me.alpha432.oyvey.*;
import me.alpha432.oyvey.features.modules.misc.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.entity.player.*;
import java.util.stream.*;
import java.util.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import net.minecraft.enchantment.*;
import net.minecraft.util.math.*;
import net.minecraft.init.*;

public class AutoCrystalC extends Module
{
    public static AutoCrystalC INSTANCE;
    private final Timer placeTimer;
    private final Timer breakTimer;
    private final Timer swapTimer;
    private final Timer syncTimer;
    public Setting<Boolean> debug;
    public Setting<Boolean> place;
    public Setting<Boolean> raytrace;
    public Setting<Float> placeRange;
    public Setting<Float> placeDelay;
    public Setting<Float> minDamage;
    public Setting<Float> maxSelfDamage;
    public Setting<Float> facePlace;
    public Setting<Float> minArmor;
    public Setting<Float> targetRange;
    public Setting<Boolean> explode;
    public Setting<Float> breakDelay;
    public Setting<Float> breakRange;
    public Setting<Float> breakWallRange;
    public Setting<Boolean> predict;
    public Setting<Integer> ticksExisted;
    public Setting<Boolean> autoswitch;
    public Setting<Integer> swapCooldown;
    public Setting<Boolean> sync;
    public Setting<DamageSync> damageSync;
    public Setting<Integer> damageSyncTime;
    public Setting<Integer> confirm;
    public Setting<Boolean> render;
    public Setting<Boolean> renderDmg;
    public Setting<Boolean> box;
    public Setting<Boolean> outline;
    private final Setting<Integer> red;
    private final Setting<Integer> green;
    private final Setting<Integer> blue;
    private final Setting<Integer> boxAlpha;
    private final Setting<Float> lineWidth;
    private EntityLivingBase target;
    public BlockPos pos;
    public BlockPos calcPos;
    EntityEnderCrystal crystal;
    private int hotBarSlot;
    private boolean armor;
    private boolean armorTarget;
    private int crystalCount;
    private boolean swapped;
    private float yaw;
    private double damage;
    private EntityLivingBase realTarget;
    private float pitch;
    private boolean exploded;
    private boolean posConfirmed;
    private boolean togglePitch;
    private EnumFacing enumFacing;
    public static ArrayList<BlockPos> placedPos;
    public static ArrayList<BlockPos> brokenPos;
    
    public AutoCrystalC() {
        super("AutoCrystal-C", "I'm going to cry if I die on const again", Category.COMBAT, true, false, false);
        this.placeTimer = new Timer();
        this.breakTimer = new Timer();
        this.swapTimer = new Timer();
        this.syncTimer = new Timer();
        this.debug = (Setting<Boolean>)this.register(new Setting("Debug", (T)false));
        this.place = (Setting<Boolean>)this.register(new Setting("Place", (T)true));
        this.raytrace = (Setting<Boolean>)this.register(new Setting("Raytrace", (T)true));
        this.placeRange = (Setting<Float>)this.register(new Setting("PlaceRange", (T)4.2f, (T)0.1f, (T)7.0f, p -> this.place.getValue()));
        this.placeDelay = (Setting<Float>)this.register(new Setting("PlaceDelay", (T)12.0f, (T)0.0f, (T)300.0f, p -> this.place.getValue()));
        this.minDamage = (Setting<Float>)this.register(new Setting("MinDamage", (T)7.0f, (T)0.1f, (T)20.0f));
        this.maxSelfDamage = (Setting<Float>)this.register(new Setting("MaxSelfDamage", (T)10.0f, (T)0.0f, (T)20.0f));
        this.facePlace = (Setting<Float>)this.register(new Setting("FacePlaceHP", (T)4.0f, (T)0.0f, (T)36.0f));
        this.minArmor = (Setting<Float>)this.register(new Setting("MinArmor", (T)4.0f, (T)0.1f, (T)80.0f));
        this.targetRange = (Setting<Float>)this.register(new Setting("TargetRange", (T)7.0f, (T)1.0f, (T)12.0f));
        this.explode = (Setting<Boolean>)this.register(new Setting("Break", (T)true));
        this.breakDelay = (Setting<Float>)this.register(new Setting("BreakDelay", (T)24.0f, (T)0.0f, (T)300.0f));
        this.breakRange = (Setting<Float>)this.register(new Setting("BreakRange", (T)4.2f, (T)0.1f, (T)7.0f));
        this.breakWallRange = (Setting<Float>)this.register(new Setting("BreakWallRange", (T)3.0f, (T)0.1f, (T)7.0f));
        this.predict = (Setting<Boolean>)this.register(new Setting("Predict", (T)true));
        this.ticksExisted = (Setting<Integer>)this.register(new Setting("Ticks Existed", (T)0, (T)0, (T)5, v -> !this.predict.getValue()));
        this.autoswitch = (Setting<Boolean>)this.register(new Setting("AutoSwitch", (T)true));
        this.swapCooldown = (Setting<Integer>)this.register(new Setting("AutoSwitchCooldown", (T)150, (T)0, (T)200, p -> this.autoswitch.getValue()));
        this.sync = (Setting<Boolean>)this.register(new Setting("Sync", (T)true));
        this.damageSync = (Setting<DamageSync>)this.register(new Setting("DamageSync", (T)DamageSync.NONE));
        this.damageSyncTime = (Setting<Integer>)this.register(new Setting("SyncDelay", (T)500, (T)0, (T)1000, v -> this.damageSync.getValue() != DamageSync.NONE));
        this.confirm = (Setting<Integer>)this.register(new Setting("Confirm", (T)250, (T)0, (T)1000, v -> this.damageSync.getValue() != DamageSync.NONE));
        this.render = (Setting<Boolean>)this.register(new Setting("Render", (T)true));
        this.renderDmg = (Setting<Boolean>)this.register(new Setting("RenderDmg", (T)true));
        this.box = (Setting<Boolean>)this.register(new Setting("Box", (T)true));
        this.outline = (Setting<Boolean>)this.register(new Setting("Outline", (T)true));
        this.red = (Setting<Integer>)this.register(new Setting("Red", (T)0, (T)0, (T)255));
        this.green = (Setting<Integer>)this.register(new Setting("Green", (T)255, (T)0, (T)255));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", (T)0, (T)0, (T)255));
        this.boxAlpha = (Setting<Integer>)this.register(new Setting("BoxAlpha", (T)125, (T)0, (T)255));
        this.lineWidth = (Setting<Float>)this.register(new Setting("LineWidth", (T)1.0f, (T)0.1f, (T)5.0f));
        this.yaw = 0.0f;
        this.damage = 0.5;
        this.pitch = 0.0f;
        this.exploded = false;
        this.posConfirmed = false;
        this.togglePitch = false;
        AutoCrystalC.INSTANCE = this;
    }
    
    public static AutoCrystalC getInstance() {
        if (AutoCrystalC.INSTANCE == null) {
            AutoCrystalC.INSTANCE = new AutoCrystalC();
        }
        return AutoCrystalC.INSTANCE;
    }
    
    public static List<BlockPos> getSphere(final BlockPos loc, final float r, final int h, final boolean hollow, final boolean sphere, final int plus_y) {
        final ArrayList<BlockPos> circleblocks = new ArrayList<BlockPos>();
        final int cx = loc.getX();
        final int cy = loc.getY();
        final int cz = loc.getZ();
        for (int x = cx - (int)r; x <= cx + r; ++x) {
            for (int z = cz - (int)r; z <= cz + r; ++z) {
                int y = sphere ? (cy - (int)r) : cy;
                while (true) {
                    final float f2;
                    final float f = f2 = (sphere ? (cy + r) : ((float)(cy + h)));
                    if (y >= f) {
                        break;
                    }
                    final double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0);
                    if (dist < r * r && (!hollow || dist >= (r - 1.0f) * (r - 1.0f))) {
                        final BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                    ++y;
                }
            }
        }
        return circleblocks;
    }
    
    @SubscribeEvent
    @Override
    public void onRender3D(final Render3DEvent event) {
        if (this.pos != null && this.render.getValue() && this.target != null) {
            RenderUtil.drawBoxESP(this.pos, ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), 75), this.outline.getValue(), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), 255), this.lineWidth.getValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), true);
            if (this.renderDmg.getValue()) {
                final double renderDamage = this.calculateDamage(this.pos.getX() + 0.5, this.pos.getY() + 1.0, this.pos.getZ() + 0.5, (Entity)this.target);
                RenderUtil.drawText(this.pos, ((Math.floor(renderDamage) == renderDamage) ? Integer.valueOf((int)renderDamage) : String.format("%.1f", renderDamage)) + "");
            }
        }
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (event.getStage() != 0) {
            return;
        }
        this.onCrystal();
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (event.getStage() == 0 && event.getPacket() instanceof CPacketUseEntity) {
            final CPacketUseEntity cPacketUseEntity = (CPacketUseEntity)event.getPacket();
            if (cPacketUseEntity.getAction() == CPacketUseEntity.Action.ATTACK && cPacketUseEntity.getEntityFromWorld((World)AutoCrystalC.mc.world) instanceof EntityEnderCrystal) {
                if (AutoCrystalC.mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
                    int crystalSlot = (AutoCrystalC.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) ? AutoCrystalC.mc.player.inventory.currentItem : -1;
                    if (crystalSlot == -1) {
                        for (int l = 0; l < 9; ++l) {
                            if (AutoCrystalC.mc.player.inventory.getStackInSlot(l).getItem() == Items.END_CRYSTAL) {
                                crystalSlot = l;
                                this.hotBarSlot = l;
                                break;
                            }
                        }
                    }
                    if (crystalSlot == -1) {
                        this.pos = null;
                        this.calcPos = null;
                        this.target = null;
                        this.realTarget = null;
                        return;
                    }
                }
                if (this.pos != null && AutoCrystalC.mc.player.onGround) {
                    final RayTraceResult result = AutoCrystalC.mc.world.rayTraceBlocks(new Vec3d(AutoCrystalC.mc.player.posX, AutoCrystalC.mc.player.posY + AutoCrystalC.mc.player.getEyeHeight(), AutoCrystalC.mc.player.posZ), new Vec3d(this.pos.x + 0.5, this.pos.y + 1.0, this.pos.z + 0.5));
                    final EnumFacing f = (result == null || result.sideHit == null) ? EnumFacing.UP : result.sideHit;
                    this.rotateToPos(this.pos, f);
                    AutoCrystalC.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(this.pos, f, (AutoCrystalC.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, (float)this.pos.getX(), (float)this.pos.getY(), (float)this.pos.getZ()));
                }
            }
        }
        if (event.getStage() == 0 && event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock && this.pos != null) {
            final CPacketPlayerTryUseItemOnBlock cPacketPlayerTryUseItemOnBlock = (CPacketPlayerTryUseItemOnBlock)event.getPacket();
            cPacketPlayerTryUseItemOnBlock.facingX = (float)this.pos.getX();
            cPacketPlayerTryUseItemOnBlock.facingY = (float)this.pos.getY();
            cPacketPlayerTryUseItemOnBlock.facingZ = (float)this.pos.getZ();
        }
        if (event.getStage() == 0 && event.getPacket() instanceof CPacketPlayer && this.pos != null) {
            final CPacketPlayer packet = (CPacketPlayer)event.getPacket();
            packet.yaw = this.yaw;
            packet.pitch = this.pitch;
        }
    }
    
    private void holdRotation() {
        if (this.pos != null && this.enumFacing != null) {
            this.rotateToPos(this.pos, this.enumFacing);
        }
    }
    
    private void rotateTo(final Entity entity) {
        final float[] angle = MathUtil.calcAngle(AutoCrystalC.mc.player.getPositionEyes(AutoCrystalC.mc.getRenderPartialTicks()), entity.getPositionVector());
        this.yaw = angle[0];
        this.pitch = angle[1];
        AutoCrystalC.mc.player.rotationYaw = angle[0];
        AutoCrystalC.mc.player.rotationYawHead = angle[0];
        AutoCrystalC.mc.player.rotationPitch = angle[1];
    }
    
    private void rotateToPos(final BlockPos pos, final EnumFacing f) {
        float[] angle = new float[0];
        if (f == EnumFacing.UP) {
            final Vec3d vec = new Vec3d((float)pos.up().getX() - 0.5, (double)(pos.up().getY() - 1.0f), (float)pos.up().getZ() + 0.5);
            angle = MathUtil.calcAngle(AutoCrystalC.mc.player.getPositionVector(), vec);
        }
        else if (f == EnumFacing.DOWN) {
            final Vec3d vec = new Vec3d((float)pos.down().getX() - 0.5, (double)(pos.down().getY() - 1.0f), (float)pos.down().getZ() - 0.5);
            angle = MathUtil.calcAngle(AutoCrystalC.mc.player.getPositionVector(), vec);
        }
        else if (f == EnumFacing.WEST) {
            final Vec3d vec = new Vec3d((float)pos.west().getX() - 0.5, (double)(pos.west().getY() - 1.0f), (float)pos.west().getZ() - 0.5);
            angle = MathUtil.calcAngle(AutoCrystalC.mc.player.getPositionVector(), vec);
        }
        else if (f == EnumFacing.EAST) {
            final Vec3d vec = new Vec3d((float)pos.east().getX() - 0.5, (double)(pos.east().getY() - 1.0f), (float)pos.east().getZ() - 0.5);
            angle = MathUtil.calcAngle(AutoCrystalC.mc.player.getPositionVector(), vec);
        }
        else if (f == EnumFacing.NORTH) {
            final Vec3d vec = new Vec3d((float)pos.north().getX() - 0.5, (double)(pos.north().getY() - 1.0f), (float)pos.north().getZ() - 0.5);
            angle = MathUtil.calcAngle(AutoCrystalC.mc.player.getPositionVector(), vec);
        }
        else if (f == EnumFacing.SOUTH) {
            final Vec3d vec = new Vec3d((float)pos.south().getX() - 0.5, (double)(pos.south().getY() - 1.0f), (float)pos.south().getZ() - 0.5);
            angle = MathUtil.calcAngle(AutoCrystalC.mc.player.getPositionVector(), vec);
        }
        this.yaw = angle[0];
        this.pitch = angle[1];
        if (this.debug.getValue()) {
            Command.sendMessage("[AutoCrystal-C Debug] Block Resolved @ [Facing: " + f + " | Yaw: " + this.yaw + " | Pitch: " + this.pitch + "]");
        }
        AutoCrystalC.mc.player.rotationYaw = angle[0];
        AutoCrystalC.mc.player.rotationYawHead = angle[0];
        AutoCrystalC.mc.player.rotationPitch = angle[1];
    }
    
    @Override
    public void onEnable() {
        this.placeTimer.reset();
        this.breakTimer.reset();
        this.swapTimer.reset();
        this.hotBarSlot = -1;
        this.damage = 0.5;
        this.pos = null;
        this.calcPos = null;
        this.crystal = null;
        this.target = null;
        this.realTarget = null;
        this.armor = false;
        this.armorTarget = false;
        this.exploded = false;
        this.swapped = false;
        this.posConfirmed = false;
    }
    
    @Override
    public String getDisplayInfo() {
        if (this.realTarget != null) {
            return this.realTarget.getName();
        }
        return null;
    }
    
    public void onCrystal() {
        if (AutoCrystalC.mc.world == null || AutoCrystalC.mc.player == null) {
            return;
        }
        if (!this.posConfirmed && this.damageSync.getValue() != DamageSync.NONE && this.syncTimer.passedMs(this.confirm.getValue())) {
            this.syncTimer.setMs(this.damageSyncTime.getValue() + 1);
        }
        this.crystalCount = 0;
        this.holdRotation();
        for (final Entity crystal : AutoCrystalC.mc.world.loadedEntityList) {
            if (crystal instanceof EntityEnderCrystal) {
                if (!this.IsValidCrystal(crystal)) {
                    continue;
                }
                boolean count = false;
                final double damage = this.calculateDamage(this.target.getPosition().getX() + 0.5, this.target.getPosition().getY() + 1.0, this.target.getPosition().getZ() + 0.5, (Entity)this.target);
                if (damage >= this.minDamage.getValue()) {
                    count = true;
                }
                if (!count) {
                    continue;
                }
                ++this.crystalCount;
            }
        }
        this.hotBarSlot = -1;
        if (AutoCrystalC.mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
            int crystalSlot = (AutoCrystalC.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) ? AutoCrystalC.mc.player.inventory.currentItem : -1;
            if (crystalSlot == -1) {
                for (int l = 0; l < 9; ++l) {
                    if (AutoCrystalC.mc.player.inventory.getStackInSlot(l).getItem() == Items.END_CRYSTAL) {
                        crystalSlot = l;
                        this.hotBarSlot = l;
                        break;
                    }
                }
            }
            if (crystalSlot == -1) {
                this.pos = null;
                this.calcPos = null;
                this.target = null;
                this.realTarget = null;
                return;
            }
        }
        if (this.target == null) {
            this.target = (EntityLivingBase)this.getTarget();
        }
        if (this.target == null) {
            this.crystal = null;
            return;
        }
        if (this.target.getDistance((Entity)AutoCrystalC.mc.player) > 12.0f) {
            this.crystal = null;
            this.target = null;
            this.realTarget = null;
        }
        this.crystal = (EntityEnderCrystal)AutoCrystalC.mc.world.loadedEntityList.stream().filter(this::IsValidCrystal).map(p_Entity -> p_Entity).min(Comparator.comparing(p_Entity -> this.target.getDistance(p_Entity))).orElse(null);
        if (this.crystal != null && this.explode.getValue()) {
            if (this.crystal.ticksExisted < this.ticksExisted.getValue()) {
                return;
            }
            if (this.breakTimer.passedMs(this.breakDelay.getValue().longValue())) {
                this.breakTimer.reset();
                this.rotateTo((Entity)this.crystal);
                AutoCrystalC.mc.player.swingArm(EnumHand.MAIN_HAND);
                EntityUtil.attackEntity((Entity)this.crystal, this.sync.getValue(), true);
                this.exploded = true;
                return;
            }
        }
        if (!this.place.getValue()) {
            return;
        }
        this.doCalculations();
        if (this.placeTimer.passedMs(this.placeDelay.getValue().longValue())) {
            this.placeTimer.reset();
            if (this.debug.getValue()) {
                Command.sendMessage("1");
            }
            this.pos = this.calcPos;
            if (this.debug.getValue()) {
                Command.sendMessage("2");
            }
            if (this.pos != null && !this.swapped && AutoCrystalC.mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL && AutoCrystalC.mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
                if (this.autoswitch.getValue()) {
                    AutoCrystalC.mc.player.inventory.currentItem = this.hotBarSlot;
                    this.swapped = true;
                }
                return;
            }
            if (this.debug.getValue()) {
                Command.sendMessage("3");
            }
            if (this.swapped) {
                if (!this.swapTimer.passedMs(this.swapCooldown.getValue())) {
                    return;
                }
                this.swapped = false;
                this.swapTimer.reset();
            }
            if (this.debug.getValue()) {
                Command.sendMessage("4");
            }
            if (AutoCrystalC.mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL && AutoCrystalC.mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
                this.pos = null;
                this.calcPos = null;
                this.target = null;
                this.realTarget = null;
                return;
            }
            if (this.debug.getValue()) {
                Command.sendMessage("5");
            }
            int crystalLimit = 2;
            if (this.debug.getValue()) {
                Command.sendMessage("6");
            }
            if (this.crystalCount >= crystalLimit) {
                return;
            }
            if (this.debug.getValue()) {
                Command.sendMessage("7");
            }
            if (this.damage < this.minDamage.getValue()) {
                crystalLimit = 1;
            }
            if (this.debug.getValue()) {
                Command.sendMessage("8");
            }
            if (this.pos != null) {
                if (this.debug.getValue()) {
                    Command.sendMessage("9");
                }
                if (this.crystalCount < crystalLimit || this.syncTimer.passedMs(this.damageSyncTime.getValue()) || this.damageSync.getValue() == DamageSync.NONE) {
                    if (!this.exploded) {
                        if (this.debug.getValue()) {
                            Command.sendMessage("10");
                        }
                        final RayTraceResult result = AutoCrystalC.mc.world.rayTraceBlocks(new Vec3d(AutoCrystalC.mc.player.posX, AutoCrystalC.mc.player.posY + AutoCrystalC.mc.player.getEyeHeight(), AutoCrystalC.mc.player.posZ), new Vec3d(this.pos.x + 0.5, this.pos.y + 1.0, this.pos.z + 0.5));
                        final EnumFacing f = (result == null || result.sideHit == null) ? EnumFacing.UP : result.sideHit;
                        this.enumFacing = f;
                        this.rotateToPos(this.pos, f);
                        AutoCrystalC.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(this.pos, f, (AutoCrystalC.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, (float)this.pos.getX(), (float)this.pos.getY(), (float)this.pos.getZ()));
                        AutoCrystalC.placedPos.add(this.pos);
                        this.posConfirmed = false;
                        if (this.syncTimer.passedMs(this.damageSyncTime.getValue())) {
                            this.syncTimer.reset();
                        }
                        if (this.togglePitch) {
                            AutoCrystalC.mc.player.rotationPitch += (float)4.0E-4;
                            this.togglePitch = false;
                        }
                        else {
                            AutoCrystalC.mc.player.rotationPitch -= (float)4.0E-4;
                            this.togglePitch = true;
                        }
                    }
                    else {
                        this.exploded = false;
                    }
                }
                if (this.debug.getValue()) {
                    Command.sendMessage("11");
                }
            }
            if (this.debug.getValue()) {
                Command.sendMessage("12");
            }
        }
    }
    
    private void doCalculations() {
        if (this.target == null) {
            this.target = (EntityLivingBase)this.getTarget();
        }
        if (this.target == null) {
            return;
        }
        this.damage = 0.5;
        for (final BlockPos blockPos : this.placePostions(this.placeRange.getValue())) {
            if (this.debug.getValue()) {
                Command.sendMessage("Calc-1");
            }
            if (blockPos != null && this.target != null && AutoCrystalC.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(blockPos)).isEmpty() && this.target.getDistance((double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ()) <= this.targetRange.getValue() && !this.target.isDead) {
                if (this.target.getHealth() + this.target.getAbsorptionAmount() <= 0.0f) {
                    continue;
                }
                if (!BlockUtil.canBlockBeSeen(blockPos) && this.raytrace.getValue()) {
                    if (!this.debug.getValue()) {
                        continue;
                    }
                    Command.sendMessage("Calc-Raytrace: failed to resolve [ " + AutoCrystalC.mc.player.getPositionVector() + " | " + blockPos + "]");
                }
                else {
                    if (this.debug.getValue()) {
                        Command.sendMessage("Calc-2");
                    }
                    final double targetDmg = this.calculateDamage(blockPos.getX() + 0.5, blockPos.getY() + 1.0, blockPos.getZ() + 0.5, (Entity)this.target);
                    final double localDmg = this.calculateDamage(blockPos.getX() + 0.5, blockPos.getY() + 1.0, blockPos.getZ() + 0.5, (Entity)AutoCrystalC.mc.player);
                    this.armor = false;
                    if (this.debug.getValue()) {
                        Command.sendMessage("Calc-2 Debug: Target: " + targetDmg + " | Local: " + localDmg);
                    }
                    if (localDmg > this.maxSelfDamage.getValue()) {
                        continue;
                    }
                    if (this.debug.getValue()) {
                        Command.sendMessage("Calc-3");
                    }
                    for (final ItemStack is : this.target.getArmorInventoryList()) {
                        if (is.isEmpty) {
                            this.armor = true;
                        }
                        final float green = (is.getMaxDamage() - (float)is.getItemDamage()) / is.getMaxDamage();
                        final float red = 1.0f - green;
                        final int dmg = 100 - (int)(red * 100.0f);
                        if (dmg > this.minArmor.getValue()) {
                            continue;
                        }
                        this.armor = true;
                    }
                    if (this.debug.getValue()) {
                        Command.sendMessage("Calc-4: " + this.armor);
                    }
                    if (targetDmg < this.minDamage.getValue() && (this.target.getAbsorptionAmount() + this.target.getHealth() > this.facePlace.getValue() || this.target.getAbsorptionAmount() + this.target.getHealth() > this.facePlace.getValue()) && !this.armor) {
                        if (!this.debug.getValue()) {
                            continue;
                        }
                        Command.sendMessage("Calc-5");
                    }
                    else {
                        final double selfDmg = this.calculateDamage(blockPos.getX() + 0.5, blockPos.getY() + 1.0, blockPos.getZ() + 0.5, (Entity)AutoCrystalC.mc.player) + 4.0;
                        if ((selfDmg >= AutoCrystalC.mc.player.getHealth() + AutoCrystalC.mc.player.getAbsorptionAmount() && selfDmg >= targetDmg && targetDmg < this.target.getHealth() + this.target.getAbsorptionAmount()) || targetDmg < this.damage) {
                            if (!this.debug.getValue()) {
                                continue;
                            }
                            Command.sendMessage("Calc-6 [" + (selfDmg >= AutoCrystalC.mc.player.getHealth() + AutoCrystalC.mc.player.getAbsorptionAmount()) + " | " + (selfDmg >= targetDmg) + " | " + (targetDmg < this.target.getHealth() + this.target.getAbsorptionAmount()));
                        }
                        else {
                            if (this.debug.getValue()) {
                                Command.sendMessage("Calc-7");
                            }
                            this.calcPos = blockPos;
                            this.damage = targetDmg;
                        }
                    }
                }
            }
        }
        if (this.damage == 0.5) {
            if (this.debug.getValue()) {
                Command.sendMessage("Calc-8: RESET " + this.damage);
            }
            this.pos = null;
            this.calcPos = null;
            this.target = null;
            this.realTarget = null;
            return;
        }
        this.realTarget = this.target;
        if (AutoGG.getINSTANCE().isOn()) {
            final AutoGG autoGG = (AutoGG)OyVey.moduleManager.getModuleByName("AutoGG");
            autoGG.addTargetedPlayer(this.target.getName());
        }
        if (AutoToxic.getINSTANCE().isOn()) {
            final AutoToxic autoToxic = (AutoToxic)OyVey.moduleManager.getModuleByName("AutoToxic");
            autoToxic.addTargetedPlayer(this.target.getName());
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public void onPacketReceive(final PacketEvent.Receive event) {
        final SPacketSpawnObject packet;
        if (event.getPacket() instanceof SPacketSpawnObject && (packet = (SPacketSpawnObject)event.getPacket()).getType() == 51 && this.explode.getValue() && this.target != null && this.predict.getValue()) {
            if (!this.isPredicting(packet)) {
                return;
            }
            final CPacketUseEntity predict = new CPacketUseEntity();
            predict.entityId = packet.getEntityID();
            predict.action = CPacketUseEntity.Action.ATTACK;
            AutoCrystalC.mc.player.connection.sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
            AutoCrystalC.mc.player.connection.sendPacket((Packet)predict);
        }
        if (event.getPacket() instanceof SPacketSoundEffect) {
            try {
                final SPacketSoundEffect sPacketSoundEffect = (SPacketSoundEffect)event.getPacket();
                if (sPacketSoundEffect.getCategory() == SoundCategory.BLOCKS && sPacketSoundEffect.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                    for (final Entity e : AutoCrystalC.mc.world.loadedEntityList) {
                        if (e instanceof EntityEnderCrystal && e.getDistance(sPacketSoundEffect.getX(), sPacketSoundEffect.getY(), sPacketSoundEffect.getZ()) <= this.breakRange.getValue()) {
                            Objects.requireNonNull(AutoCrystalC.mc.world.getEntityByID(e.getEntityId())).setDead();
                            AutoCrystalC.mc.world.removeEntityFromWorld(e.entityId);
                        }
                    }
                    if (this.pos != null && AutoCrystalC.mc.player.onGround) {
                        final RayTraceResult result = AutoCrystalC.mc.world.rayTraceBlocks(new Vec3d(AutoCrystalC.mc.player.posX, AutoCrystalC.mc.player.posY + AutoCrystalC.mc.player.getEyeHeight(), AutoCrystalC.mc.player.posZ), new Vec3d(this.pos.x + 0.5, this.pos.y + 1.0, this.pos.z + 0.5));
                        final EnumFacing f = (result == null || result.sideHit == null) ? EnumFacing.UP : result.sideHit;
                        AutoCrystalC.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(this.pos, f, (AutoCrystalC.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                    }
                }
            }
            catch (Exception ex) {}
        }
        if (event.getPacket() instanceof SPacketExplosion) {
            try {
                final SPacketExplosion sPacketExplosion = (SPacketExplosion)event.getPacket();
                final BlockPos pos = new BlockPos(sPacketExplosion.getX(), sPacketExplosion.getY(), sPacketExplosion.getZ()).down();
                if (this.damageSync.getValue() == DamageSync.PLACE) {
                    if (AutoCrystalC.placedPos.contains(pos)) {
                        AutoCrystalC.placedPos.remove(pos);
                        this.posConfirmed = true;
                    }
                }
                else if (this.damageSync.getValue() == DamageSync.BREAK && AutoCrystalC.brokenPos.contains(pos)) {
                    AutoCrystalC.brokenPos.remove(pos);
                    this.posConfirmed = true;
                }
                for (final Entity e2 : AutoCrystalC.mc.world.loadedEntityList) {
                    if (e2 instanceof EntityEnderCrystal && e2.getDistance(sPacketExplosion.getX(), sPacketExplosion.getY(), sPacketExplosion.getZ()) <= this.breakRange.getValue()) {
                        Objects.requireNonNull(AutoCrystalC.mc.world.getEntityByID(e2.getEntityId())).setDead();
                        AutoCrystalC.mc.world.removeEntityFromWorld(e2.entityId);
                    }
                }
                if (this.pos != null && AutoCrystalC.mc.player.onGround) {
                    final RayTraceResult result2 = AutoCrystalC.mc.world.rayTraceBlocks(new Vec3d(AutoCrystalC.mc.player.posX, AutoCrystalC.mc.player.posY + AutoCrystalC.mc.player.getEyeHeight(), AutoCrystalC.mc.player.posZ), new Vec3d(pos.x + 0.5, pos.y + 1.0, pos.z + 0.5));
                    final EnumFacing f2 = (result2 == null || result2.sideHit == null) ? EnumFacing.UP : result2.sideHit;
                    AutoCrystalC.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(this.pos, f2, (AutoCrystalC.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                }
            }
            catch (Exception ex2) {}
        }
        if (event.getPacket() instanceof SPacketDestroyEntities) {
            final SPacketDestroyEntities packet2 = (SPacketDestroyEntities)event.getPacket();
            for (final int id : packet2.getEntityIDs()) {
                final Entity entity = AutoCrystalC.mc.world.getEntityByID(id);
                if (entity instanceof EntityEnderCrystal) {
                    AutoCrystalC.brokenPos.remove(new BlockPos(entity.getPositionVector()).down());
                    AutoCrystalC.placedPos.remove(new BlockPos(entity.getPositionVector()).down());
                }
            }
        }
    }
    
    private boolean isPredicting(final SPacketSpawnObject packet) {
        try {
            final BlockPos packPos = new BlockPos(packet.getX(), packet.getY(), packet.getZ());
            if (AutoCrystalC.mc.player.getDistance(packet.getX(), packet.getY(), packet.getZ()) > this.breakRange.getValue()) {
                return false;
            }
            if (!this.canSeePos(packPos) && AutoCrystalC.mc.player.getDistance(packet.getX(), packet.getY(), packet.getZ()) > this.breakWallRange.getValue()) {
                return false;
            }
            final double targetDmg = this.calculateDamage(packet.getX() + 0.5, packet.getY() + 1.0, packet.getZ() + 0.5, (Entity)this.target);
            if (EntityUtil.isInHole((Entity)AutoCrystalC.mc.player) && targetDmg >= 1.0) {
                return true;
            }
            final double selfDmg = this.calculateDamage(packet.getX() + 0.5, packet.getY() + 1.0, packet.getZ() + 0.5, (Entity)AutoCrystalC.mc.player);
            return selfDmg + 4.0 >= AutoCrystalC.mc.player.getHealth() + AutoCrystalC.mc.player.getAbsorptionAmount() || true;
        }
        catch (Exception e) {
            return false;
        }
    }
    
    private boolean IsValidCrystal(final Entity p_Entity) {
        return p_Entity != null && p_Entity instanceof EntityEnderCrystal && this.target != null && p_Entity.getDistance((Entity)AutoCrystalC.mc.player) <= this.breakRange.getValue() && (AutoCrystalC.mc.player.canEntityBeSeen(p_Entity) || p_Entity.getDistance((Entity)AutoCrystalC.mc.player) <= this.breakWallRange.getValue()) && !p_Entity.isDead && !this.target.isDead && this.target.getHealth() + this.target.getAbsorptionAmount() > 0.0f;
    }
    
    private EntityPlayer getTarget() {
        EntityPlayer closestPlayer = null;
        try {
            for (final EntityPlayer entity : AutoCrystalC.mc.world.playerEntities) {
                if (AutoCrystalC.mc.player != null && !AutoCrystalC.mc.player.isDead && !entity.isDead && entity != AutoCrystalC.mc.player && !OyVey.friendManager.isFriend(entity.getName())) {
                    if (entity.getDistance((Entity)AutoCrystalC.mc.player) > 12.0f) {
                        continue;
                    }
                    this.armorTarget = false;
                    for (final ItemStack is : entity.getArmorInventoryList()) {
                        if (is.isEmpty) {
                            this.armorTarget = true;
                        }
                        final float green = (is.getMaxDamage() - (float)is.getItemDamage()) / is.getMaxDamage();
                        final float red = 1.0f - green;
                        final int dmg = 100 - (int)(red * 100.0f);
                        if (dmg > this.minArmor.getValue()) {
                            continue;
                        }
                        this.armorTarget = true;
                    }
                    if (EntityUtil.isInHole((Entity)entity) && entity.getAbsorptionAmount() + entity.getHealth() > this.facePlace.getValue() && !this.armorTarget && this.minDamage.getValue() > 2.2f) {
                        continue;
                    }
                    if (closestPlayer == null) {
                        closestPlayer = entity;
                    }
                    else {
                        if (closestPlayer.getDistance((Entity)AutoCrystalC.mc.player) <= entity.getDistance((Entity)AutoCrystalC.mc.player)) {
                            continue;
                        }
                        closestPlayer = entity;
                    }
                }
            }
        }
        catch (Exception ex) {}
        return closestPlayer;
    }
    
    private boolean canSeePos(final BlockPos pos) {
        return BlockUtil.canBlockBeSeen(pos);
    }
    
    private NonNullList<BlockPos> placePostions(final float placeRange) {
        final NonNullList positions = NonNullList.create();
        positions.addAll((Collection)getSphere(new BlockPos(Math.floor(AutoCrystalC.mc.player.posX), Math.floor(AutoCrystalC.mc.player.posY), Math.floor(AutoCrystalC.mc.player.posZ)), placeRange, (int)placeRange, false, true, 0).stream().filter(pos -> this.canPlaceCrystal(pos, true)).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
        return (NonNullList<BlockPos>)positions;
    }
    
    private boolean canPlaceCrystal(final BlockPos blockPos, final boolean specialEntityCheck) {
        final BlockPos boost = blockPos.add(0, 1, 0);
        final BlockPos boost2 = blockPos.add(0, 2, 0);
        try {
            if (AutoCrystalC.mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && AutoCrystalC.mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN) {
                return false;
            }
            if (AutoCrystalC.mc.world.getBlockState(boost).getBlock() != Blocks.AIR || AutoCrystalC.mc.world.getBlockState(boost2).getBlock() != Blocks.AIR) {
                return false;
            }
            if (!specialEntityCheck) {
                return AutoCrystalC.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost)).isEmpty() && AutoCrystalC.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost2)).isEmpty();
            }
            for (final Entity entity : AutoCrystalC.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost))) {
                if (entity instanceof EntityEnderCrystal) {
                    continue;
                }
                return false;
            }
            for (final Entity entity : AutoCrystalC.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost2))) {
                if (entity instanceof EntityEnderCrystal) {
                    continue;
                }
                return false;
            }
        }
        catch (Exception ignored) {
            return false;
        }
        return true;
    }
    
    private float calculateDamage(final double posX, final double posY, final double posZ, final Entity entity) {
        final float doubleExplosionSize = 12.0f;
        final double distancedsize = entity.getDistance(posX, posY, posZ) / 12.0;
        final Vec3d vec3d = new Vec3d(posX, posY, posZ);
        double blockDensity = 0.0;
        try {
            blockDensity = AutoCrystalC.mc.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        }
        catch (Exception ex) {}
        final double v = (1.0 - distancedsize) * blockDensity;
        final float damage = (float)(int)((v * v + v) / 2.0 * 7.0 * 12.0 + 1.0);
        double finald = 1.0;
        if (entity instanceof EntityLivingBase) {
            finald = this.getBlastReduction((EntityLivingBase)entity, this.getDamageMultiplied(damage), new Explosion((World)AutoCrystalC.mc.world, (Entity)null, posX, posY, posZ, 6.0f, false, true));
        }
        return (float)finald;
    }
    
    private float getBlastReduction(final EntityLivingBase entity, final float damageI, final Explosion explosion) {
        float damage = damageI;
        if (entity instanceof EntityPlayer) {
            final EntityPlayer ep = (EntityPlayer)entity;
            final DamageSource ds = DamageSource.causeExplosionDamage(explosion);
            damage = CombatRules.getDamageAfterAbsorb(damage, (float)ep.getTotalArmorValue(), (float)ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            int k = 0;
            try {
                k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
            }
            catch (Exception ex) {}
            final float f = MathHelper.clamp((float)k, 0.0f, 20.0f);
            damage *= 1.0f - f / 25.0f;
            if (entity.isPotionActive(MobEffects.RESISTANCE)) {
                damage -= damage / 4.0f;
            }
            damage = Math.max(damage, 0.0f);
            return damage;
        }
        damage = CombatRules.getDamageAfterAbsorb(damage, (float)entity.getTotalArmorValue(), (float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        return damage;
    }
    
    private float getDamageMultiplied(final float damage) {
        final int diff = AutoCrystalC.mc.world.getDifficulty().getId();
        return damage * ((diff == 0) ? 0.0f : ((diff == 2) ? 1.0f : ((diff == 1) ? 0.5f : 1.5f)));
    }
    
    static {
        AutoCrystalC.placedPos = new ArrayList<BlockPos>();
        AutoCrystalC.brokenPos = new ArrayList<BlockPos>();
    }
    
    public enum DamageSync
    {
        NONE, 
        PLACE, 
        BREAK;
    }
}
