//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.features.setting.*;
import net.minecraft.entity.item.*;
import net.minecraft.network.*;
import java.util.function.*;
import me.alpha432.oyvey.features.command.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import me.alpha432.oyvey.*;
import me.alpha432.oyvey.features.modules.misc.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.alpha432.oyvey.event.events.*;
import me.alpha432.oyvey.features.modules.client.*;
import java.awt.*;
import me.alpha432.oyvey.util.*;
import java.util.stream.*;
import java.util.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import net.minecraft.enchantment.*;
import net.minecraft.util.math.*;
import net.minecraft.init.*;
import net.minecraft.world.*;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.state.*;
import java.util.concurrent.*;

public final class AutoCrystal extends Module
{
    private Timer placeTimer;
    private Timer breakTimer;
    private Timer predictTimer;
    private Timer swapTimer;
    public final Setting<Page> page;
    public final Setting<Boolean> debug;
    public final Setting<RotateMode> rotationMode;
    public final Setting<Boolean> place;
    public final Setting<Boolean> rayTrace;
    public final Setting<Float> placeRange;
    public final Setting<Float> placeWallRange;
    public final Setting<Float> placeDelay;
    public final Setting<Float> minDamage;
    public final Setting<Float> maxSelfDamage;
    public final Setting<Float> facePlace;
    public final Setting<Float> minArmor;
    public final Setting<Float> targetRange;
    public final Setting<Boolean> predictMotion;
    public final Setting<Integer> motionTicks;
    public final Setting<Boolean> opPlace;
    public final Setting<Boolean> ccMode;
    public final Setting<Boolean> explode;
    public final Setting<Float> breakDelay;
    public final Setting<Float> breakRange;
    public final Setting<Float> breakWallRange;
    public final Setting<Boolean> packetBreak;
    public final Setting<Boolean> predicts;
    public final Setting<Integer> attackFactor;
    public final Setting<Boolean> remove;
    public final Setting<Integer> ticksExisted;
    public final Setting<Boolean> await;
    public final Setting<AntiWeakness> antiWeakness;
    public final Setting<SwapMode> swapType;
    public final Setting<Integer> autoSwitchCooldown;
    public final Setting<Boolean> ignoreUseAmount;
    public final Setting<Integer> wasteAmount;
    public final Setting<SwingMode> swingMode;
    public final Setting<Boolean> render;
    public final Setting<Boolean> renderDmg;
    public final Setting<Boolean> box;
    public final Setting<Integer> red;
    public final Setting<Integer> green;
    public final Setting<Integer> blue;
    public final Setting<Integer> boxAlpha;
    public final Setting<Float> lineWidth;
    public final Setting<Boolean> outline;
    public final Setting<Boolean> fadeSlower;
    private ConcurrentHashMap<BlockPos, Integer> renderSpots;
    EntityEnderCrystal crystal;
    private EntityLivingBase target;
    public BlockPos pos;
    public BlockPos calcPos;
    private int hotBarSlot;
    private boolean armor;
    private boolean armorTarget;
    private int crystalCount;
    private float yaw;
    private double damage;
    private EntityLivingBase realTarget;
    private float pitch;
    private boolean exploded;
    private boolean confirmed;
    private CalculationThread calculationThread;
    public BlockPos position;
    public EnumHand hand;
    public static AutoCrystal INSTANCE;
    
    public static AutoCrystal getInstance() {
        if (AutoCrystal.INSTANCE == null) {
            AutoCrystal.INSTANCE = new AutoCrystal();
        }
        return AutoCrystal.INSTANCE;
    }
    
    public AutoCrystal() {
        super("AutoCrystal", "My CrystalAura Places Crystals!!!!", Category.COMBAT, true, false, false);
        this.placeTimer = new Timer();
        this.breakTimer = new Timer();
        this.predictTimer = new Timer();
        this.swapTimer = new Timer();
        this.page = (Setting<Page>)this.register(new Setting("Page", (T)Page.Place));
        this.debug = (Setting<Boolean>)this.register(new Setting("Debug", (T)false, v -> this.page.getValue() == Page.Misc));
        this.rotationMode = (Setting<RotateMode>)this.register(new Setting("Rotations", (T)RotateMode.Off, v -> this.page.getValue() == Page.Misc));
        this.place = (Setting<Boolean>)this.register(new Setting("Place", (T)true, v -> this.page.getValue() == Page.Place));
        this.rayTrace = (Setting<Boolean>)this.register(new Setting("RayTrace", (T)false, v -> this.page.getValue() == Page.Place));
        this.placeRange = (Setting<Float>)this.register(new Setting("PlaceRange", (T)4.0f, (T)0.1f, (T)7.0f, p -> this.place.getValue() && this.page.getValue() == Page.Place));
        this.placeWallRange = (Setting<Float>)this.register(new Setting("PlaceWallRange", (T)3.0f, (T)0.1f, (T)7.0f, p -> this.place.getValue() && this.page.getValue() == Page.Place));
        this.placeDelay = (Setting<Float>)this.register(new Setting("PlaceDelay", (T)0.0f, (T)0.0f, (T)300.0f, p -> this.place.getValue() && this.page.getValue() == Page.Place));
        this.minDamage = (Setting<Float>)this.register(new Setting("MinDamage", (T)4.0f, (T)0.1f, (T)20.0f, v -> this.page.getValue() == Page.Place));
        this.maxSelfDamage = (Setting<Float>)this.register(new Setting("MaxSelfDamage", (T)10.0f, (T)0.0f, (T)20.0f, v -> this.page.getValue() == Page.Place));
        this.facePlace = (Setting<Float>)this.register(new Setting("FacePlaceHP", (T)4.0f, (T)0.0f, (T)36.0f, v -> this.page.getValue() == Page.Place));
        this.minArmor = (Setting<Float>)this.register(new Setting("MinArmor", (T)4.0f, (T)0.1f, (T)80.0f, v -> this.page.getValue() == Page.Place));
        this.targetRange = (Setting<Float>)this.register(new Setting("TargetRange", (T)4.0f, (T)1.0f, (T)12.0f, v -> this.page.getValue() == Page.Place));
        this.predictMotion = (Setting<Boolean>)this.register(new Setting("PredictMotion", (T)true, v -> this.page.getValue() == Page.Place));
        this.motionTicks = (Setting<Integer>)this.register(new Setting("MotionTicks", (T)2, (T)1, (T)10, v -> this.predictMotion.getValue() && this.page.getValue() == Page.Place));
        this.opPlace = (Setting<Boolean>)this.register(new Setting("1.13 Place", (T)false, v -> this.page.getValue() == Page.Place));
        this.ccMode = (Setting<Boolean>)this.register(new Setting("CC Mode", (T)true, v -> this.page.getValue() == Page.Place));
        this.explode = (Setting<Boolean>)this.register(new Setting("Break", (T)true, v -> this.page.getValue() == Page.Break));
        this.breakDelay = (Setting<Float>)this.register(new Setting("BreakDelay", (T)10.0f, (T)0.0f, (T)300.0f, v -> this.page.getValue() == Page.Break));
        this.breakRange = (Setting<Float>)this.register(new Setting("BreakRange", (T)4.0f, (T)0.1f, (T)7.0f, v -> this.page.getValue() == Page.Break));
        this.breakWallRange = (Setting<Float>)this.register(new Setting("BreakWallRange", (T)4.0f, (T)0.1f, (T)7.0f, v -> this.page.getValue() == Page.Break));
        this.packetBreak = (Setting<Boolean>)this.register(new Setting("PacketBreak", (T)true, v -> this.page.getValue() == Page.Break));
        this.predicts = (Setting<Boolean>)this.register(new Setting("Predict", (T)true, v -> this.page.getValue() == Page.Break));
        this.attackFactor = (Setting<Integer>)this.register(new Setting("PredictDelay", (T)0, (T)0, (T)200, p -> this.predicts.getValue() && this.page.getValue() == Page.Break));
        this.remove = (Setting<Boolean>)this.register(new Setting("Remove", (T)true, v -> this.page.getValue() == Page.Break));
        this.ticksExisted = (Setting<Integer>)this.register(new Setting("TicksExisted", (T)0, (T)0, (T)5, p -> this.predicts.getValue() && this.page.getValue() == Page.Break));
        this.await = (Setting<Boolean>)this.register(new Setting("Await", (T)false, v -> this.page.getValue() == Page.Misc));
        this.antiWeakness = (Setting<AntiWeakness>)this.register(new Setting("AntiWeakness", (T)AntiWeakness.Off, v -> this.page.getValue() == Page.Misc));
        this.swapType = (Setting<SwapMode>)this.register(new Setting("Switch", (T)SwapMode.Off, v -> this.page.getValue() == Page.Misc));
        this.autoSwitchCooldown = (Setting<Integer>)this.register(new Setting("AutoSwitchCooldown", (T)50, (T)0, (T)200, p -> this.swapType.getValue() == SwapMode.Normal && this.page.getValue() == Page.Misc));
        this.ignoreUseAmount = (Setting<Boolean>)this.register(new Setting("IgnoreUseAmount", (T)true, v -> this.page.getValue() == Page.Misc));
        this.wasteAmount = (Setting<Integer>)this.register(new Setting("UseAmount", (T)4, (T)1, (T)5, v -> this.page.getValue() == Page.Misc));
        this.swingMode = (Setting<SwingMode>)this.register(new Setting("Swing", (T)SwingMode.MainHand, v -> this.page.getValue() == Page.Misc));
        this.render = (Setting<Boolean>)this.register(new Setting("Render", (T)true, v -> this.page.getValue() == Page.Render));
        this.renderDmg = (Setting<Boolean>)this.register(new Setting("RenderDmg", (T)true, v -> this.page.getValue() == Page.Render));
        this.box = (Setting<Boolean>)this.register(new Setting("Box", (T)true, v -> this.page.getValue() == Page.Render));
        this.red = (Setting<Integer>)this.register(new Setting("Red", (T)0, (T)0, (T)255, v -> this.page.getValue() == Page.Render));
        this.green = (Setting<Integer>)this.register(new Setting("Green", (T)255, (T)0, (T)255, v -> this.page.getValue() == Page.Render));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", (T)0, (T)0, (T)255, v -> this.page.getValue() == Page.Render));
        this.boxAlpha = (Setting<Integer>)this.register(new Setting("BoxAlpha", (T)125, (T)0, (T)255, v -> this.page.getValue() == Page.Render));
        this.lineWidth = (Setting<Float>)this.register(new Setting("LineWidth", (T)1.0f, (T)0.1f, (T)5.0f, v -> this.page.getValue() == Page.Render));
        this.outline = (Setting<Boolean>)this.register(new Setting("Outline", (T)true, v -> this.page.getValue() == Page.Render));
        this.fadeSlower = (Setting<Boolean>)this.register(new Setting("FadeSlower", (T)false, v -> this.page.getValue() == Page.Render));
        this.yaw = 0.0f;
        this.damage = 0.5;
        this.pitch = 0.0f;
        this.exploded = false;
        this.confirmed = true;
        AutoCrystal.INSTANCE = this;
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
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (this.rotationMode.getValue() != RotateMode.Off && event.getStage() != 0) {
            return;
        }
        AutoCrystal.mc.addScheduledTask(() -> this.onCrystal());
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (SelfFill.getInstance().isOn()) {
            return;
        }
        if (event.getStage() == 0 && event.getPacket() instanceof CPacketUseEntity) {
            final CPacketUseEntity cPacketUseEntity = (CPacketUseEntity)event.getPacket();
            if (cPacketUseEntity.getAction() == CPacketUseEntity.Action.ATTACK && cPacketUseEntity.getEntityFromWorld((World)AutoCrystal.mc.world) instanceof EntityEnderCrystal) {
                if (this.remove.getValue()) {
                    Objects.requireNonNull(cPacketUseEntity.getEntityFromWorld((World)AutoCrystal.mc.world)).setDead();
                    AutoCrystal.mc.world.removeEntityFromWorld(cPacketUseEntity.entityId);
                    if (this.await.getValue()) {
                        this.confirmed = true;
                    }
                }
                if (AutoCrystal.mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
                    int crystalSlot = (AutoCrystal.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) ? AutoCrystal.mc.player.inventory.currentItem : -1;
                    if (crystalSlot == -1) {
                        for (int l = 0; l < 9; ++l) {
                            if (AutoCrystal.mc.player.inventory.getStackInSlot(l).getItem() == Items.END_CRYSTAL) {
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
                if (this.swapType.getValue() == SwapMode.Silent) {
                    return;
                }
                if (this.pos != null && AutoCrystal.mc.player.onGround) {
                    final RayTraceResult result = AutoCrystal.mc.world.rayTraceBlocks(new Vec3d(AutoCrystal.mc.player.posX, AutoCrystal.mc.player.posY + AutoCrystal.mc.player.getEyeHeight(), AutoCrystal.mc.player.posZ), new Vec3d(this.pos.x + 0.5, this.pos.y + 1.0, this.pos.z + 0.5));
                    final EnumFacing f = (result == null || result.sideHit == null) ? EnumFacing.UP : result.sideHit;
                    AutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(this.pos, f, (AutoCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, (float)this.pos.x, (float)this.pos.y, (float)this.pos.z));
                    AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                }
            }
        }
    }
    
    private void holdRotation() {
        if (this.crystal != null && this.rotationMode.getValue() == RotateMode.Full) {
            final float[] angle = MathUtil.calcAngle(AutoCrystal.mc.player.getPositionEyes(AutoCrystal.mc.getRenderPartialTicks()), this.crystal.getPositionVector());
            this.yaw = angle[0];
            this.pitch = angle[1];
            AutoCrystal.mc.player.rotationYaw = this.yaw;
            AutoCrystal.mc.player.rotationYawHead = this.yaw;
            AutoCrystal.mc.player.rotationPitch = this.pitch;
        }
        else if (this.pos != null) {
            final float[] angle = MathUtil.calcAngle(AutoCrystal.mc.player.getPositionEyes(AutoCrystal.mc.getRenderPartialTicks()), new Vec3d((double)(this.pos.getX() + 0.5f), (double)(this.pos.getY() - 0.5f), (double)(this.pos.getZ() + 0.5f)));
            this.yaw = angle[0];
            this.pitch = angle[1];
            AutoCrystal.mc.player.rotationYaw = this.yaw;
            AutoCrystal.mc.player.rotationYawHead = this.yaw;
            AutoCrystal.mc.player.rotationPitch = this.pitch;
        }
    }
    
    private void rotateTo(final Entity entity) {
        if (this.rotationMode.getValue() == RotateMode.Full) {
            final float[] angle = MathUtil.calcAngle(AutoCrystal.mc.player.getPositionEyes(AutoCrystal.mc.getRenderPartialTicks()), entity.getPositionVector());
            this.yaw = angle[0];
            this.pitch = angle[1];
            AutoCrystal.mc.player.rotationYaw = angle[0];
            AutoCrystal.mc.player.rotationYawHead = angle[0];
            AutoCrystal.mc.player.rotationPitch = angle[1];
        }
    }
    
    private void rotateToPos(final BlockPos pos) {
        if (this.rotationMode.getValue() != RotateMode.Off) {
            final float[] angle = MathUtil.calcAngle(AutoCrystal.mc.player.getPositionEyes(AutoCrystal.mc.getRenderPartialTicks()), new Vec3d((double)(pos.getX() + 0.5f), (double)(pos.getY() - 0.5f), (double)(pos.getZ() + 0.5f)));
            this.yaw = angle[0];
            this.pitch = angle[1];
            AutoCrystal.mc.player.rotationYaw = angle[0];
            AutoCrystal.mc.player.rotationYawHead = angle[0];
            AutoCrystal.mc.player.rotationPitch = angle[1];
        }
    }
    
    @Override
    public void onEnable() {
        this.renderSpots = new ConcurrentHashMap<BlockPos, Integer>();
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
        this.confirmed = true;
        (this.calculationThread = new CalculationThread()).start();
    }
    
    @Override
    public void onTick() {
        if (this.rotationMode.getValue() == RotateMode.Off) {
            AutoCrystal.mc.addScheduledTask(() -> this.onCrystal());
        }
    }
    
    @Override
    public String getDisplayInfo() {
        if (this.realTarget != null) {
            return this.realTarget.getName();
        }
        return null;
    }
    
    public void onCrystal() {
        if (AutoCrystal.mc.world == null || AutoCrystal.mc.player == null) {
            return;
        }
        if (SelfFill.getInstance().isOn()) {
            return;
        }
        this.crystalCount = 0;
        if (this.rotationMode.getValue() != RotateMode.Off) {
            this.holdRotation();
        }
        if (!this.ignoreUseAmount.getValue()) {
            for (final Entity crystal : AutoCrystal.mc.world.loadedEntityList) {
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
        }
        this.hotBarSlot = -1;
        if (AutoCrystal.mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
            int crystalSlot = (AutoCrystal.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) ? AutoCrystal.mc.player.inventory.currentItem : -1;
            if (crystalSlot == -1) {
                for (int l = 0; l < 9; ++l) {
                    if (AutoCrystal.mc.player.inventory.getStackInSlot(l).getItem() == Items.END_CRYSTAL) {
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
        if (this.target.getDistance((Entity)AutoCrystal.mc.player) > 12.0f) {
            this.crystal = null;
            this.target = null;
            this.realTarget = null;
        }
        this.crystal = (EntityEnderCrystal)AutoCrystal.mc.world.loadedEntityList.stream().filter(this::IsValidCrystal).map(p_Entity -> p_Entity).min(Comparator.comparing(p_Entity -> this.target.getDistance(p_Entity))).orElse(null);
        if (this.crystal != null && this.explode.getValue()) {
            if (this.crystal.ticksExisted < this.ticksExisted.getValue()) {
                return;
            }
            if (this.breakTimer.passedMs(this.breakDelay.getValue().longValue())) {
                this.breakTimer.reset();
                this.rotateTo((Entity)this.crystal);
                if (this.debug.getValue()) {
                    Command.sendMessage("Attacking");
                }
                if (this.swingMode.getValue() == SwingMode.MainHand) {
                    AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                }
                else if (this.swingMode.getValue() == SwingMode.OffHand) {
                    AutoCrystal.mc.player.swingArm(EnumHand.OFF_HAND);
                }
                final int oldSlot = AutoCrystal.mc.player.inventory.currentItem;
                int swordSlot = (AutoCrystal.mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD) ? AutoCrystal.mc.player.inventory.currentItem : -1;
                if (swordSlot == -1) {
                    for (int i = 0; i < 9; ++i) {
                        if (AutoCrystal.mc.player.inventory.getStackInSlot(i).getItem() == Items.DIAMOND_SWORD) {
                            swordSlot = i;
                            break;
                        }
                    }
                }
                if (this.antiWeakness.getValue() == AntiWeakness.Silent && AutoCrystal.mc.player.getHeldItemMainhand().getItem() != Items.DIAMOND_SWORD && swordSlot != -1) {
                    AutoCrystal.mc.player.inventory.currentItem = swordSlot;
                    AutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(swordSlot));
                }
                if (this.antiWeakness.getValue() == AntiWeakness.Normal && AutoCrystal.mc.player.getHeldItemMainhand().getItem() != Items.DIAMOND_SWORD && swordSlot != -1) {
                    AutoCrystal.mc.player.inventory.currentItem = swordSlot;
                    AutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(swordSlot));
                    return;
                }
                if (this.packetBreak.getValue()) {
                    AutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketUseEntity((Entity)this.crystal));
                }
                else {
                    AutoCrystal.mc.playerController.attackEntity((EntityPlayer)AutoCrystal.mc.player, (Entity)this.crystal);
                }
                this.exploded = true;
                if (this.antiWeakness.getValue() != AntiWeakness.Off) {
                    AutoCrystal.mc.player.inventory.currentItem = oldSlot;
                    AutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(oldSlot));
                    return;
                }
            }
        }
        if (this.placeTimer.passedMs(this.placeDelay.getValue().longValue()) && this.place.getValue()) {
            this.placeTimer.reset();
            if (this.await.getValue() && !this.confirmed) {
                return;
            }
            this.pos = this.calcPos;
            final int oldSlot = AutoCrystal.mc.player.inventory.currentItem;
            if (this.pos == null) {
                return;
            }
            if (this.swapType.getValue() == SwapMode.Normal && AutoCrystal.mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL && AutoCrystal.mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
                AutoCrystal.mc.player.inventory.currentItem = this.hotBarSlot;
                return;
            }
            if (this.swapType.getValue() == SwapMode.Silent && AutoCrystal.mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL) {
                AutoCrystal.mc.player.inventory.currentItem = this.hotBarSlot;
                AutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.hotBarSlot));
            }
            if (this.swapType.getValue() == SwapMode.Normal) {
                if (!this.swapTimer.passedMs(this.autoSwitchCooldown.getValue())) {
                    return;
                }
                this.swapTimer.reset();
            }
            if (this.swapType.getValue() != SwapMode.Silent && AutoCrystal.mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL && AutoCrystal.mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
                this.pos = null;
                this.calcPos = null;
                this.target = null;
                this.realTarget = null;
                return;
            }
            if (!this.ignoreUseAmount.getValue()) {
                int crystalLimit = this.wasteAmount.getValue();
                if (this.crystalCount >= crystalLimit) {
                    return;
                }
                if (this.damage < this.minDamage.getValue()) {
                    crystalLimit = 1;
                }
                if (this.crystalCount < crystalLimit && this.pos != null) {
                    if (!this.exploded) {
                        this.rotateToPos(this.pos);
                        if (this.debug.getValue()) {
                            Command.sendMessage("Placing");
                        }
                        final RayTraceResult result = AutoCrystal.mc.world.rayTraceBlocks(new Vec3d(AutoCrystal.mc.player.posX, AutoCrystal.mc.player.posY + AutoCrystal.mc.player.getEyeHeight(), AutoCrystal.mc.player.posZ), new Vec3d(this.pos.x + 0.5, this.pos.y + 1.0, this.pos.z + 0.5));
                        final EnumFacing f = (result == null || result.sideHit == null) ? EnumFacing.UP : result.sideHit;
                        AutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(this.pos, f, (AutoCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, (float)this.pos.x, (float)this.pos.y, (float)this.pos.z));
                        AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                        this.renderSpots.put(this.pos, this.boxAlpha.getValue());
                    }
                    else {
                        this.exploded = false;
                    }
                }
            }
            else if (this.pos != null) {
                if (!this.exploded) {
                    this.rotateToPos(this.pos);
                    if (this.debug.getValue()) {
                        Command.sendMessage("Placing");
                    }
                    final RayTraceResult result2 = AutoCrystal.mc.world.rayTraceBlocks(new Vec3d(AutoCrystal.mc.player.posX, AutoCrystal.mc.player.posY + AutoCrystal.mc.player.getEyeHeight(), AutoCrystal.mc.player.posZ), new Vec3d(this.pos.x + 0.5, this.pos.y + 1.0, this.pos.z + 0.5));
                    final EnumFacing f2 = (result2 == null || result2.sideHit == null) ? EnumFacing.UP : result2.sideHit;
                    AutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(this.pos, f2, (AutoCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, (float)this.pos.x, (float)this.pos.y, (float)this.pos.z));
                    AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                    this.renderSpots.put(this.pos, this.boxAlpha.getValue());
                }
                else {
                    this.exploded = false;
                }
            }
            if (this.swapType.getValue() == SwapMode.Silent) {
                AutoCrystal.mc.player.inventory.currentItem = oldSlot;
                AutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(oldSlot));
            }
            this.confirmed = false;
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
            if (blockPos != null && this.target != null && AutoCrystal.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(blockPos)).isEmpty() && this.target.getDistance((double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ()) <= this.targetRange.getValue() && !this.target.isDead) {
                if (this.target.getHealth() + this.target.getAbsorptionAmount() <= 0.0f) {
                    continue;
                }
                final double targetDmg = this.calculateDamage(blockPos.getX() + 0.5, blockPos.getY() + 1.0, blockPos.getZ() + 0.5, (Entity)this.target);
                final double localDmg = this.calculateDamage(blockPos.getX() + 0.5, blockPos.getY() + 1.0, blockPos.getZ() + 0.5, (Entity)AutoCrystal.mc.player);
                this.armor = false;
                if (localDmg > this.maxSelfDamage.getValue()) {
                    continue;
                }
                try {
                    for (final ItemStack is : this.target.getArmorInventoryList()) {
                        final float green = (is.getMaxDamage() - (float)is.getItemDamage()) / is.getMaxDamage();
                        final float red = 1.0f - green;
                        final int dmg = 100 - (int)(red * 100.0f);
                        if (dmg > this.minArmor.getValue()) {
                            continue;
                        }
                        this.armor = true;
                    }
                }
                catch (Exception ex) {}
                final double selfDmg;
                if ((targetDmg < this.minDamage.getValue() && this.target.getHealth() + this.target.getAbsorptionAmount() > this.facePlace.getValue() && !this.armor) || ((selfDmg = this.calculateDamage(blockPos.getX() + 0.5, blockPos.getY() + 1.0, blockPos.getZ() + 0.5, (Entity)AutoCrystal.mc.player) + 4.0) >= AutoCrystal.mc.player.getHealth() + AutoCrystal.mc.player.getAbsorptionAmount() && selfDmg >= targetDmg)) {
                    continue;
                }
                if (this.damage > targetDmg) {
                    continue;
                }
                this.calcPos = blockPos;
                this.damage = targetDmg;
            }
        }
        if (this.damage == 0.5) {
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
        if (SelfFill.getInstance().isOn()) {
            return;
        }
        final SPacketSpawnObject packet;
        if (event.getPacket() instanceof SPacketSpawnObject && (packet = (SPacketSpawnObject)event.getPacket()).getType() == 51 && this.predicts.getValue() && this.predictTimer.passedMs(this.attackFactor.getValue()) && this.predicts.getValue() && this.explode.getValue() && this.packetBreak.getValue() && this.target != null) {
            if (!this.isPredicting(packet)) {
                return;
            }
            final CPacketUseEntity predict = new CPacketUseEntity();
            predict.entityId = packet.getEntityID();
            predict.action = CPacketUseEntity.Action.ATTACK;
            AutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
            AutoCrystal.mc.player.connection.sendPacket((Packet)predict);
        }
        if (event.getPacket() instanceof SPacketSoundEffect) {
            try {
                final SPacketSoundEffect sPacketSoundEffect = (SPacketSoundEffect)event.getPacket();
                if (sPacketSoundEffect.getCategory() == SoundCategory.BLOCKS && sPacketSoundEffect.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                    for (final Entity e : AutoCrystal.mc.world.loadedEntityList) {
                        if (e instanceof EntityEnderCrystal && e.getDistance(sPacketSoundEffect.getX(), sPacketSoundEffect.getY(), sPacketSoundEffect.getZ()) <= this.breakRange.getValue()) {
                            Objects.requireNonNull(AutoCrystal.mc.world.getEntityByID(e.getEntityId())).setDead();
                            AutoCrystal.mc.world.removeEntityFromWorld(e.entityId);
                            if (!this.await.getValue()) {
                                continue;
                            }
                            this.confirmed = true;
                        }
                    }
                }
            }
            catch (Exception ex) {}
        }
        if (event.getPacket() instanceof SPacketExplosion) {
            try {
                final SPacketExplosion sPacketExplosion = (SPacketExplosion)event.getPacket();
                for (final Entity e : AutoCrystal.mc.world.loadedEntityList) {
                    if (e instanceof EntityEnderCrystal && e.getDistance(sPacketExplosion.getX(), sPacketExplosion.getY(), sPacketExplosion.getZ()) <= this.breakRange.getValue()) {
                        Objects.requireNonNull(AutoCrystal.mc.world.getEntityByID(e.getEntityId())).setDead();
                        AutoCrystal.mc.world.removeEntityFromWorld(e.entityId);
                        if (!this.await.getValue()) {
                            continue;
                        }
                        this.confirmed = true;
                    }
                }
            }
            catch (Exception ex2) {}
        }
    }
    
    @Override
    public void onRender3D(final Render3DEvent event) {
        if (this.renderSpots != null && !this.renderSpots.isEmpty()) {
            for (final Map.Entry<BlockPos, Integer> entry : this.renderSpots.entrySet()) {
                final BlockPos blockPos = entry.getKey();
                Integer alpha = entry.getValue();
                if (this.fadeSlower.getValue()) {
                    --alpha;
                }
                else {
                    alpha -= 2;
                }
                if (alpha <= 0) {
                    this.renderSpots.remove(blockPos);
                }
                else {
                    this.renderSpots.replace(blockPos, alpha);
                    RenderUtil.drawBoxESP(blockPos, ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), alpha), this.outline.getValue(), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), alpha), this.lineWidth.getValue(), this.outline.getValue(), this.box.getValue(), alpha, true);
                }
            }
        }
        if (this.pos != null && this.render.getValue() && this.target != null && this.renderDmg.getValue()) {
            try {
                final double renderDamage = this.calculateDamage(this.pos.getX() + 0.5, this.pos.getY() + 1.0, this.pos.getZ() + 0.5, (Entity)this.target);
                RenderUtil.drawText(this.pos, ((Math.floor(renderDamage) == renderDamage) ? Integer.valueOf((int)renderDamage) : String.format("%.1f", renderDamage)) + "");
            }
            catch (Exception ex) {}
        }
    }
    
    private boolean isPredicting(final SPacketSpawnObject packet) {
        try {
            final BlockPos packPos = new BlockPos(packet.getX(), packet.getY(), packet.getZ());
            return AutoCrystal.mc.player.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= this.breakRange.getValue() && (BlockUtil.rayTracePlaceCheck(packPos) || AutoCrystal.mc.player.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= this.breakWallRange.getValue()) && (this.ignoreUseAmount.getValue() || !this.target.isDead) && this.target.getHealth() + this.target.getAbsorptionAmount() > 0.0f;
        }
        catch (Exception e) {
            return false;
        }
    }
    
    private boolean IsValidCrystal(final Entity p_Entity) {
        try {
            if (p_Entity == null) {
                return false;
            }
            if (!(p_Entity instanceof EntityEnderCrystal)) {
                return false;
            }
            if (this.target == null) {
                return false;
            }
            if (p_Entity.getDistance((Entity)AutoCrystal.mc.player) > this.breakRange.getValue()) {
                return false;
            }
            if (!AutoCrystal.mc.player.canEntityBeSeen(p_Entity) && p_Entity.getDistance((Entity)AutoCrystal.mc.player) > this.breakWallRange.getValue()) {
                return false;
            }
            if (p_Entity.isDead) {
                return false;
            }
            if ((!this.ignoreUseAmount.getValue() && this.target.isDead) || this.target.getHealth() + this.target.getAbsorptionAmount() <= 0.0f) {
                return false;
            }
        }
        catch (Exception ex) {}
        return true;
    }
    
    private EntityPlayer getTarget() {
        EntityPlayer closestPlayer = null;
        try {
            for (final EntityPlayer entity : AutoCrystal.mc.world.playerEntities) {
                if (AutoCrystal.mc.player != null && entity.entityId != -1337420 && !AutoCrystal.mc.player.isDead && !entity.isDead && entity != AutoCrystal.mc.player && !OyVey.friendManager.isFriend(entity.getName())) {
                    if (entity.getDistance((Entity)AutoCrystal.mc.player) > 12.0f) {
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
                        if (closestPlayer.getDistance((Entity)AutoCrystal.mc.player) <= entity.getDistance((Entity)AutoCrystal.mc.player)) {
                            continue;
                        }
                        closestPlayer = entity;
                    }
                }
            }
        }
        catch (Exception ex) {}
        if (closestPlayer != null && this.predictMotion.getValue()) {
            final float f = closestPlayer.width / 2.0f;
            final float f2 = closestPlayer.height;
            closestPlayer.setEntityBoundingBox(new AxisAlignedBB(closestPlayer.posX - f, closestPlayer.posY, closestPlayer.posZ - f, closestPlayer.posX + f, closestPlayer.posY + f2, closestPlayer.posZ + f));
            final Entity y = getPredictedPosition((Entity)closestPlayer, this.motionTicks.getValue());
            closestPlayer.setEntityBoundingBox(y.getEntityBoundingBox());
        }
        return closestPlayer;
    }
    
    public static boolean canSeePos(final BlockPos pos) {
        return AutoCrystal.mc.world.rayTraceBlocks(new Vec3d(AutoCrystal.mc.player.posX, AutoCrystal.mc.player.posY + AutoCrystal.mc.player.getEyeHeight(), AutoCrystal.mc.player.posZ), new Vec3d((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()), false, true, false) == null;
    }
    
    private NonNullList<BlockPos> placePostions(final float placeRange) {
        final NonNullList positions = NonNullList.create();
        positions.addAll((Collection)getSphere(new BlockPos(Math.floor(AutoCrystal.mc.player.posX), Math.floor(AutoCrystal.mc.player.posY), Math.floor(AutoCrystal.mc.player.posZ)), placeRange, (int)placeRange, false, true, 0).stream().filter(pos -> this.canPlaceCrystal(pos, true)).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
        return (NonNullList<BlockPos>)positions;
    }
    
    public boolean rayTracePlaceCheck(final BlockPos pos, final boolean shouldCheck, final float height) {
        return !shouldCheck || AutoCrystal.mc.world.rayTraceBlocks(new Vec3d(AutoCrystal.mc.player.posX, AutoCrystal.mc.player.posY + AutoCrystal.mc.player.getEyeHeight(), AutoCrystal.mc.player.posZ), new Vec3d((double)pos.getX(), (double)(pos.getY() + height), (double)pos.getZ()), false, true, false) == null;
    }
    
    public boolean rayTracePlaceCheck(final BlockPos pos, final boolean shouldCheck) {
        return this.rayTracePlaceCheck(pos, shouldCheck, 1.0f);
    }
    
    private boolean canPlaceCrystal(final BlockPos blockPos, final boolean specialEntityCheck) {
        final BlockPos boost = blockPos.add(0, 1, 0);
        final BlockPos boost2 = blockPos.add(0, 2, 0);
        if (this.rayTrace.getValue() && !this.rayTracePlaceCheck(blockPos, true)) {
            return false;
        }
        if (!BlockUtil.rayTracePlaceCheck(blockPos)) {
            if (AutoCrystal.mc.player.getDistanceSq(blockPos) > MathUtil.square(this.placeWallRange.getValue())) {
                return false;
            }
        }
        else if (AutoCrystal.mc.player.getDistanceSq(blockPos) > MathUtil.square(this.placeRange.getValue())) {
            return false;
        }
        try {
            if (this.opPlace.getValue()) {
                if (AutoCrystal.mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && AutoCrystal.mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN) {
                    return false;
                }
                if (AutoCrystal.mc.world.getBlockState(boost).getBlock() != Blocks.AIR) {
                    return false;
                }
                if (!specialEntityCheck) {
                    return AutoCrystal.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost)).isEmpty();
                }
                for (final Entity entity : AutoCrystal.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost))) {
                    if (entity instanceof EntityEnderCrystal) {
                        continue;
                    }
                    return false;
                }
            }
            else if (this.ccMode.getValue()) {
                if (AutoCrystal.mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && AutoCrystal.mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN) {
                    return false;
                }
                if (AutoCrystal.mc.world.getBlockState(boost).getBlock() != Blocks.AIR) {
                    return false;
                }
                if (AutoCrystal.mc.world.getBlockState(boost2).getBlock() != Blocks.AIR) {
                    return false;
                }
                if (!specialEntityCheck) {
                    return AutoCrystal.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost)).isEmpty();
                }
                for (final Entity entity : AutoCrystal.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost))) {
                    if (entity instanceof EntityEnderCrystal) {
                        continue;
                    }
                    return false;
                }
            }
            else {
                if (AutoCrystal.mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && AutoCrystal.mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN) {
                    return false;
                }
                if (AutoCrystal.mc.world.getBlockState(boost).getBlock() != Blocks.AIR || AutoCrystal.mc.world.getBlockState(boost2).getBlock() != Blocks.AIR) {
                    return false;
                }
                if (!specialEntityCheck) {
                    return AutoCrystal.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost)).isEmpty() && AutoCrystal.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost2)).isEmpty();
                }
                for (final Entity entity : AutoCrystal.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost))) {
                    if (entity instanceof EntityEnderCrystal) {
                        continue;
                    }
                    return false;
                }
                for (final Entity entity : AutoCrystal.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost2))) {
                    if (entity instanceof EntityEnderCrystal) {
                        continue;
                    }
                    return false;
                }
            }
        }
        catch (Exception ignored) {
            return false;
        }
        return true;
    }
    
    private float calculateDamage(final double posX, final double posY, final double posZ, final Entity entity) {
        final float doubleExplosionSize = 12.0f;
        final double distancedsize = entity.getDistance(posX, posY, posZ) / doubleExplosionSize;
        final Vec3d vec3d = new Vec3d(posX, posY, posZ);
        double blockDensity = 0.0;
        try {
            blockDensity = this.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        }
        catch (Exception ex) {}
        final double v = (1.0 - distancedsize) * blockDensity;
        final float damage = (float)(int)((v * v + v) / 2.0 * 7.0 * doubleExplosionSize + 1.0);
        double finald = 1.0;
        if (entity instanceof EntityLivingBase) {
            try {
                finald = this.getBlastReduction((EntityLivingBase)entity, this.getDamageMultiplied(damage), new Explosion((World)AutoCrystal.mc.world, (Entity)null, posX, posY, posZ, 6.0f, false, true));
            }
            catch (Exception ex2) {}
        }
        return (float)finald;
    }
    
    public float getBlockDensity(final Vec3d vec, final AxisAlignedBB bb) {
        final double d0 = 1.0 / ((bb.maxX - bb.minX) * 2.0 + 1.0);
        final double d2 = 1.0 / ((bb.maxY - bb.minY) * 2.0 + 1.0);
        final double d3 = 1.0 / ((bb.maxZ - bb.minZ) * 2.0 + 1.0);
        final double d4 = (1.0 - Math.floor(1.0 / d0) * d0) / 2.0;
        final double d5 = (1.0 - Math.floor(1.0 / d3) * d3) / 2.0;
        if (d0 >= 0.0 && d2 >= 0.0 && d3 >= 0.0) {
            int j2 = 0;
            int k2 = 0;
            for (float f = 0.0f; f <= 1.0f; f += (float)d0) {
                for (float f2 = 0.0f; f2 <= 1.0f; f2 += (float)d2) {
                    for (float f3 = 0.0f; f3 <= 1.0f; f3 += (float)d3) {
                        final double d6 = bb.minX + (bb.maxX - bb.minX) * f;
                        final double d7 = bb.minY + (bb.maxY - bb.minY) * f2;
                        final double d8 = bb.minZ + (bb.maxZ - bb.minZ) * f3;
                        if (rayTraceBlocks(new Vec3d(d6 + d4, d7, d8 + d5), vec, false, false, false, true) == null) {
                            ++j2;
                        }
                        ++k2;
                    }
                }
            }
            return j2 / (float)k2;
        }
        return 0.0f;
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
    
    public static RayTraceResult rayTraceBlocks(Vec3d vec31, final Vec3d vec32, final boolean stopOnLiquid, final boolean ignoreNoBox, final boolean returnLastUncollidableBlock, final boolean ignoreWebs) {
        if (Double.isNaN(vec31.x) || Double.isNaN(vec31.y) || Double.isNaN(vec31.z)) {
            return null;
        }
        if (!Double.isNaN(vec32.x) && !Double.isNaN(vec32.y) && !Double.isNaN(vec32.z)) {
            int x1 = MathHelper.floor(vec31.x);
            int y1 = MathHelper.floor(vec31.y);
            int z1 = MathHelper.floor(vec31.z);
            final int x2 = MathHelper.floor(vec32.x);
            final int y2 = MathHelper.floor(vec32.y);
            final int z2 = MathHelper.floor(vec32.z);
            BlockPos pos = new BlockPos(x1, y1, z1);
            final IBlockState state = AutoCrystal.mc.world.getBlockState(pos);
            final Block block = state.getBlock();
            if ((!ignoreNoBox || state.getCollisionBoundingBox((IBlockAccess)AutoCrystal.mc.world, pos) != Block.NULL_AABB) && block.canCollideCheck(state, stopOnLiquid) && (!ignoreWebs || !(block instanceof BlockWeb))) {
                final RayTraceResult raytraceresult = state.collisionRayTrace((World)AutoCrystal.mc.world, pos, vec31, vec32);
                if (raytraceresult != null) {
                    return raytraceresult;
                }
            }
            RayTraceResult raytraceresult2 = null;
            int k1 = 200;
            while (k1-- >= 0) {
                if (Double.isNaN(vec31.x) || Double.isNaN(vec31.y) || Double.isNaN(vec31.z)) {
                    return null;
                }
                if (x1 == x2 && y1 == y2 && z1 == z2) {
                    return returnLastUncollidableBlock ? raytraceresult2 : null;
                }
                boolean flag2 = true;
                boolean flag3 = true;
                boolean flag4 = true;
                double d0 = 999.0;
                double d2 = 999.0;
                double d3 = 999.0;
                if (x2 > x1) {
                    d0 = x1 + 1.0;
                }
                else if (x2 < x1) {
                    d0 = x1 + 0.0;
                }
                else {
                    flag2 = false;
                }
                if (y2 > y1) {
                    d2 = y1 + 1.0;
                }
                else if (y2 < y1) {
                    d2 = y1 + 0.0;
                }
                else {
                    flag3 = false;
                }
                if (z2 > z1) {
                    d3 = z1 + 1.0;
                }
                else if (z2 < z1) {
                    d3 = z1 + 0.0;
                }
                else {
                    flag4 = false;
                }
                double d4 = 999.0;
                double d5 = 999.0;
                double d6 = 999.0;
                final double d7 = vec32.x - vec31.x;
                final double d8 = vec32.y - vec31.y;
                final double d9 = vec32.z - vec31.z;
                if (flag2) {
                    d4 = (d0 - vec31.x) / d7;
                }
                if (flag3) {
                    d5 = (d2 - vec31.y) / d8;
                }
                if (flag4) {
                    d6 = (d3 - vec31.z) / d9;
                }
                if (d4 == -0.0) {
                    d4 = -1.0E-4;
                }
                if (d5 == -0.0) {
                    d5 = -1.0E-4;
                }
                if (d6 == -0.0) {
                    d6 = -1.0E-4;
                }
                EnumFacing enumfacing;
                if (d4 < d5 && d4 < d6) {
                    enumfacing = ((x2 > x1) ? EnumFacing.WEST : EnumFacing.EAST);
                    vec31 = new Vec3d(d0, vec31.y + d8 * d4, vec31.z + d9 * d4);
                }
                else if (d5 < d6) {
                    enumfacing = ((y2 > y1) ? EnumFacing.DOWN : EnumFacing.UP);
                    vec31 = new Vec3d(vec31.x + d7 * d5, d2, vec31.z + d9 * d5);
                }
                else {
                    enumfacing = ((z2 > z1) ? EnumFacing.NORTH : EnumFacing.SOUTH);
                    vec31 = new Vec3d(vec31.x + d7 * d6, vec31.y + d8 * d6, d3);
                }
                x1 = MathHelper.floor(vec31.x) - ((enumfacing == EnumFacing.EAST) ? 1 : 0);
                y1 = MathHelper.floor(vec31.y) - ((enumfacing == EnumFacing.UP) ? 1 : 0);
                z1 = MathHelper.floor(vec31.z) - ((enumfacing == EnumFacing.SOUTH) ? 1 : 0);
                pos = new BlockPos(x1, y1, z1);
                final IBlockState state2 = AutoCrystal.mc.world.getBlockState(pos);
                final Block block2 = state2.getBlock();
                if (ignoreNoBox && state2.getMaterial() != Material.PORTAL && state2.getCollisionBoundingBox((IBlockAccess)AutoCrystal.mc.world, pos) == Block.NULL_AABB) {
                    continue;
                }
                if (block2.canCollideCheck(state2, stopOnLiquid) && (!ignoreWebs || !(block2 instanceof BlockWeb))) {
                    final RayTraceResult raytraceresult3 = state2.collisionRayTrace((World)AutoCrystal.mc.world, pos, vec31, vec32);
                    if (raytraceresult3 != null) {
                        return raytraceresult3;
                    }
                    continue;
                }
                else {
                    raytraceresult2 = new RayTraceResult(RayTraceResult.Type.MISS, vec31, enumfacing, pos);
                }
            }
            return returnLastUncollidableBlock ? raytraceresult2 : null;
        }
        return null;
    }
    
    public static Entity getPredictedPosition(final Entity entity, final double x) {
        if (x == 0.0) {
            return entity;
        }
        EntityPlayer e = null;
        final double motionX = entity.posX - entity.lastTickPosX;
        double motionY = entity.posY - entity.lastTickPosY;
        final double motionZ = entity.posZ - entity.lastTickPosZ;
        boolean shouldPredict = false;
        boolean shouldStrafe = false;
        final double motion = Math.sqrt(Math.pow(motionX, 2.0) + Math.pow(motionZ, 2.0) + Math.pow(motionY, 2.0));
        if (motion > 0.1) {
            shouldPredict = true;
        }
        if (!shouldPredict) {
            return entity;
        }
        if (motion > 0.31) {
            shouldStrafe = true;
        }
        for (int i = 0; i < x; ++i) {
            if (e == null) {
                if (isOnGround(0.0, 0.0, 0.0, entity)) {
                    motionY = (shouldStrafe ? 0.4 : -0.07840015258789);
                }
                else {
                    motionY -= 0.08;
                    motionY *= 0.9800000190734863;
                }
                e = placeValue(motionX, motionY, motionZ, (EntityPlayer)entity);
            }
            else {
                if (isOnGround(0.0, 0.0, 0.0, (Entity)e)) {
                    motionY = (shouldStrafe ? 0.4 : -0.07840015258789);
                }
                else {
                    motionY -= 0.08;
                    motionY *= 0.9800000190734863;
                }
                e = placeValue(motionX, motionY, motionZ, e);
            }
        }
        return (Entity)e;
    }
    
    public static boolean isOnGround(final double x, double y, final double z, final Entity entity) {
        try {
            final double d3 = y;
            final List<AxisAlignedBB> list1 = (List<AxisAlignedBB>)AutoCrystal.mc.world.getCollisionBoxes(entity, entity.getEntityBoundingBox().expand(x, y, z));
            if (y != 0.0) {
                for (int k = 0, l = list1.size(); k < l; ++k) {
                    y = list1.get(k).calculateYOffset(entity.getEntityBoundingBox(), y);
                }
            }
            return d3 != y && d3 < 0.0;
        }
        catch (Exception ignored) {
            return false;
        }
    }
    
    public static EntityPlayer placeValue(double x, double y, double z, final EntityPlayer entity) {
        final List<AxisAlignedBB> list1 = (List<AxisAlignedBB>)AutoCrystal.mc.world.getCollisionBoxes((Entity)entity, entity.getEntityBoundingBox().expand(x, y, z));
        if (y != 0.0) {
            for (int k = 0, l = list1.size(); k < l; ++k) {
                y = list1.get(k).calculateYOffset(entity.getEntityBoundingBox(), y);
            }
            if (y != 0.0) {
                entity.setEntityBoundingBox(entity.getEntityBoundingBox().offset(0.0, y, 0.0));
            }
        }
        if (x != 0.0) {
            for (int j5 = 0, l2 = list1.size(); j5 < l2; ++j5) {
                x = calculateXOffset(entity.getEntityBoundingBox(), x, list1.get(j5));
            }
            if (x != 0.0) {
                entity.setEntityBoundingBox(entity.getEntityBoundingBox().offset(x, 0.0, 0.0));
            }
        }
        if (z != 0.0) {
            for (int k2 = 0, i6 = list1.size(); k2 < i6; ++k2) {
                z = calculateZOffset(entity.getEntityBoundingBox(), z, list1.get(k2));
            }
            if (z != 0.0) {
                entity.setEntityBoundingBox(entity.getEntityBoundingBox().offset(0.0, 0.0, z));
            }
        }
        return entity;
    }
    
    public static double calculateXOffset(final AxisAlignedBB other, double OffsetX, final AxisAlignedBB this1) {
        if (other.maxY > this1.minY && other.minY < this1.maxY && other.maxZ > this1.minZ && other.minZ < this1.maxZ) {
            if (OffsetX > 0.0 && other.maxX <= this1.minX) {
                final double d1 = this1.minX - 0.3 - other.maxX;
                if (d1 < OffsetX) {
                    OffsetX = d1;
                }
            }
            else if (OffsetX < 0.0 && other.minX >= this1.maxX) {
                final double d2 = this1.maxX + 0.3 - other.minX;
                if (d2 > OffsetX) {
                    OffsetX = d2;
                }
            }
        }
        return OffsetX;
    }
    
    public static double calculateZOffset(final AxisAlignedBB other, double OffsetZ, final AxisAlignedBB this1) {
        if (other.maxX > this1.minX && other.minX < this1.maxX && other.maxY > this1.minY && other.minY < this1.maxY) {
            if (OffsetZ > 0.0 && other.maxZ <= this1.minZ) {
                final double d1 = this1.minZ - 0.3 - other.maxZ;
                if (d1 < OffsetZ) {
                    OffsetZ = d1;
                }
            }
            else if (OffsetZ < 0.0 && other.minZ >= this1.maxZ) {
                final double d2 = this1.maxZ + 0.3 - other.minZ;
                if (d2 > OffsetZ) {
                    OffsetZ = d2;
                }
            }
        }
        return OffsetZ;
    }
    
    private float getDamageMultiplied(final float damage) {
        final int diff = AutoCrystal.mc.world.getDifficulty().getId();
        return damage * ((diff == 0) ? 0.0f : ((diff == 2) ? 1.0f : ((diff == 1) ? 0.5f : 1.5f)));
    }
    
    private enum Page
    {
        Place, 
        Break, 
        Misc, 
        Render;
    }
    
    private enum AntiWeakness
    {
        Off, 
        Normal, 
        Silent;
    }
    
    public enum SwingMode
    {
        MainHand, 
        OffHand, 
        None;
    }
    
    public enum RotateMode
    {
        Off, 
        Full, 
        Semi;
    }
    
    private enum SwapMode
    {
        Off, 
        Normal, 
        Silent;
    }
    
    public static class CalculationThread extends Thread
    {
        @Override
        public void run() {
            while (AutoCrystal.getInstance().isOn()) {
                try {
                    AutoCrystal.getInstance().doCalculations();
                    TimeUnit.MILLISECONDS.sleep(50L);
                }
                catch (Exception ex) {}
            }
        }
    }
}
