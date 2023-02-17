//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.features.setting.*;
import net.minecraft.entity.player.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.*;
import net.minecraft.network.*;
import com.mojang.realmsclient.gui.*;
import me.alpha432.oyvey.features.command.*;
import me.alpha432.oyvey.event.events.*;
import me.alpha432.oyvey.features.modules.client.*;
import java.awt.*;
import me.alpha432.oyvey.*;
import net.minecraft.entity.item.*;
import net.minecraft.util.math.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.network.play.client.*;
import net.minecraft.item.*;
import net.minecraft.block.*;
import me.alpha432.oyvey.util.*;
import net.minecraft.init.*;
import java.util.*;

public class AutoTrap extends Module
{
    public static final List blackList;
    public static final List shulkerList;
    private final Setting<Float> range;
    private final Setting<Integer> blocksPerTick;
    private final Setting<Integer> tickDelay;
    private final Setting<Cage> cage;
    private final Setting<Boolean> rotate;
    private final Setting<Boolean> noGlitchBlocks;
    private final Setting<Boolean> activeInFreecam;
    private final Setting<Boolean> announceUsage;
    private final Setting<Boolean> toggleoff;
    private final Setting<Boolean> turnOffCauras;
    private final Setting<Boolean> esp;
    private final Setting<ESPMode> mode;
    private final Setting<Integer> red;
    private final Setting<Integer> green;
    private final Setting<Integer> blue;
    private final Setting<Integer> alpha;
    private final Setting<Integer> oalpha;
    int cDelay;
    String caura;
    boolean isDisabling;
    boolean hasDisabled;
    private ArrayList<String> options;
    private EntityPlayer closestTarget;
    private String lastTickTargetName;
    private int playerHotbarSlot;
    private int lastHotbarSlot;
    private int delayStep;
    private boolean isSneaking;
    private int offsetStep;
    private boolean firstRun;
    private int test;
    private Set<BlockPos> placeList;
    
    public AutoTrap() {
        super("AutoTrap", "Automatically traps people", Category.COMBAT, true, false, false);
        this.lastTickTargetName = "";
        this.range = (Setting<Float>)this.register(new Setting("Range", (T)4.5f, (T)3.5f, (T)32.0f));
        this.blocksPerTick = (Setting<Integer>)this.register(new Setting("BlocksPerTick", (T)2, (T)1, (T)23));
        this.tickDelay = (Setting<Integer>)this.register(new Setting("TickDelay", (T)2, (T)0, (T)10));
        this.cage = (Setting<Cage>)this.register(new Setting("Cage", (T)Cage.TRAP));
        this.rotate = (Setting<Boolean>)this.register(new Setting("Rotate", (T)false));
        this.noGlitchBlocks = (Setting<Boolean>)this.register(new Setting("NoGlitchBlocks", (T)true));
        this.activeInFreecam = (Setting<Boolean>)this.register(new Setting("ActiveInFreecam", (T)true));
        this.announceUsage = (Setting<Boolean>)this.register(new Setting("AnnounceUsage", (T)true));
        this.toggleoff = (Setting<Boolean>)this.register(new Setting("Toggle Off", (T)false));
        this.turnOffCauras = (Setting<Boolean>)this.register(new Setting("Toggle Other Cauras", (T)false));
        this.esp = (Setting<Boolean>)this.register(new Setting("Show esp", (T)false));
        this.mode = (Setting<ESPMode>)this.register(new Setting("Esp Mode", (T)ESPMode.Full));
        this.red = (Setting<Integer>)this.register(new Setting("Red", (T)0, (T)0, (T)255));
        this.green = (Setting<Integer>)this.register(new Setting("Green", (T)255, (T)0, (T)255));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", (T)255, (T)0, (T)255));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", (T)70, (T)0, (T)255));
        this.oalpha = (Setting<Integer>)this.register(new Setting("Outline Alpha", (T)70, (T)0, (T)255));
        this.placeList = new HashSet<BlockPos>();
        this.cDelay = 0;
        this.playerHotbarSlot = -1;
        this.lastHotbarSlot = -1;
        this.delayStep = 0;
        this.isSneaking = false;
        this.offsetStep = 0;
    }
    
    public static EnumFacing getPlaceableSide(final BlockPos pos) {
        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbour = pos.offset(side);
            if (AutoTrap.mc.world.getBlockState(neighbour).getBlock().canCollideCheck(AutoTrap.mc.world.getBlockState(neighbour), false)) {
                final IBlockState blockState = AutoTrap.mc.world.getBlockState(neighbour);
                if (!blockState.getMaterial().isReplaceable()) {
                    return side;
                }
            }
        }
        return null;
    }
    
    private static Block getBlock(final BlockPos pos) {
        return getState(pos).getBlock();
    }
    
    private static IBlockState getState(final BlockPos pos) {
        return AutoTrap.mc.world.getBlockState(pos);
    }
    
    public static boolean canBeClicked(final BlockPos pos) {
        return getBlock(pos).canCollideCheck(getState(pos), false);
    }
    
    @Override
    public void onEnable() {
        this.test = 0;
        if (AutoTrap.mc.player == null) {
            this.disable();
            return;
        }
        this.hasDisabled = false;
        this.firstRun = true;
        this.playerHotbarSlot = AutoTrap.mc.player.inventory.currentItem;
        this.lastHotbarSlot = -1;
    }
    
    @Override
    public void onDisable() {
        if (AutoTrap.mc.player == null) {
            return;
        }
        if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
            AutoTrap.mc.player.inventory.currentItem = this.playerHotbarSlot;
        }
        if (this.isSneaking) {
            AutoTrap.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)AutoTrap.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            this.isSneaking = false;
        }
        this.playerHotbarSlot = -1;
        this.lastHotbarSlot = -1;
        this.placeList.clear();
        if (this.announceUsage.getValue()) {
            this.sendDebugMessage(ChatFormatting.RED.toString() + "Disabled!");
        }
    }
    
    protected void sendDebugMessage(final String text) {
        Command.sendMessage(text);
    }
    
    @Override
    public void onRender3D(final Render3DEvent event) {
        if (this.esp.getValue()) {
            final int color1 = this.red.getValue();
            final int color2 = this.green.getValue();
            final int color3 = this.blue.getValue();
            for (final BlockPos pos : this.placeList) {
                if (this.mode.getValue() == ESPMode.Solid) {
                    RenderUtil.drawBoxESP(pos, ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), true, ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), 1.2f, false, true, this.alpha.getValue(), true);
                }
                else if (this.mode.getValue() == ESPMode.Outline) {
                    final IBlockState iBlockState2 = AutoTrap.mc.world.getBlockState(pos);
                    final Vec3d interp2 = MathUtil.interpolateEntity((Entity)AutoTrap.mc.player, AutoTrap.mc.getRenderPartialTicks());
                    RenderUtil.drawBoxESP(pos, ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), true, ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), 1.2f, true, false, this.alpha.getValue(), true);
                }
                else {
                    if (this.mode.getValue() != ESPMode.Full) {
                        continue;
                    }
                    final IBlockState iBlockState3 = AutoTrap.mc.world.getBlockState(pos);
                    final Vec3d interp3 = MathUtil.interpolateEntity((Entity)AutoTrap.mc.player, AutoTrap.mc.getRenderPartialTicks());
                    RenderUtil.drawBoxESP(pos, ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), true, ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), 1.2f, true, true, this.alpha.getValue(), true);
                }
            }
        }
    }
    
    @Override
    public void onUpdate() {
        if (this.cDelay > 0) {
            --this.cDelay;
        }
        if (this.cDelay == 0 && this.isDisabling && OyVey.moduleManager.getModuleByName(this.caura) != null) {
            OyVey.moduleManager.getModuleByName(this.caura).toggle();
            this.isDisabling = false;
            this.hasDisabled = true;
        }
        if (OyVey.moduleManager.getModuleByName("AutoCrystal") != null && OyVey.moduleManager.getModuleByName("AutoCrystal").isOn() && this.turnOffCauras.getValue() && !this.hasDisabled) {
            this.caura = "AutoCrystal";
            this.cDelay = 19;
            this.isDisabling = true;
            OyVey.moduleManager.getModuleByName(this.caura).toggle();
        }
        if (this.toggleoff.getValue()) {
            ++this.test;
            if (this.test == 20) {
                super.toggle();
            }
        }
        if (AutoTrap.mc.player == null) {
            return;
        }
        if (!this.activeInFreecam.getValue() && OyVey.moduleManager.isModuleEnabled("Freecam")) {
            return;
        }
        if (!this.firstRun) {
            if (this.delayStep < this.tickDelay.getValue()) {
                ++this.delayStep;
                return;
            }
            this.delayStep = 0;
        }
        this.findClosestTarget();
        if (this.closestTarget == null) {
            if (this.firstRun) {
                this.firstRun = false;
                if (this.announceUsage.getValue()) {
                    this.sendDebugMessage(ChatFormatting.GREEN + "enabled" + ChatFormatting.RESET + ", waiting for target.");
                }
            }
            return;
        }
        if (this.firstRun) {
            this.firstRun = false;
            this.lastTickTargetName = this.closestTarget.getName();
            if (this.announceUsage.getValue()) {
                this.sendDebugMessage(ChatFormatting.GREEN + "enabled" + ChatFormatting.RESET + ", target: " + this.lastTickTargetName);
            }
        }
        else if (!this.lastTickTargetName.equals(this.closestTarget.getName())) {
            this.lastTickTargetName = this.closestTarget.getName();
            this.offsetStep = 0;
            if (this.announceUsage.getValue()) {
                this.sendDebugMessage("New target: " + this.lastTickTargetName);
            }
        }
        final List<Vec3d> placeTargets = new ArrayList<Vec3d>();
        if (this.cage.getValue() == Cage.TRAP) {
            Collections.addAll(placeTargets, Offsets.TRAP);
        }
        if (this.cage.getValue() == Cage.TRAPTOP) {
            Collections.addAll(placeTargets, Offsets.TRAPTOP);
        }
        if (this.cage.getValue() == Cage.TRAPFULLROOF) {
            Collections.addAll(placeTargets, Offsets.TRAPFULLROOF);
        }
        if (this.cage.getValue() == Cage.TRAPFULLROOFTOP) {
            Collections.addAll(placeTargets, Offsets.TRAPFULLROOFTOP);
        }
        if (this.cage.getValue() == Cage.CRYSTALEXA) {
            Collections.addAll(placeTargets, Offsets.CRYSTALEXA);
        }
        if (this.cage.getValue() == Cage.CRYSTAL) {
            Collections.addAll(placeTargets, Offsets.CRYSTAL);
        }
        if (this.cage.getValue() == Cage.CRYSTALFULLROOF) {
            Collections.addAll(placeTargets, Offsets.CRYSTALFULLROOF);
        }
        int blocksPlaced = 0;
        while (blocksPlaced < this.blocksPerTick.getValue()) {
            if (this.offsetStep >= placeTargets.size()) {
                this.offsetStep = 0;
                break;
            }
            final BlockPos offsetPos = new BlockPos((Vec3d)placeTargets.get(this.offsetStep));
            final BlockPos targetPos = new BlockPos(this.closestTarget.getPositionVector()).down().add(offsetPos.x, offsetPos.y, offsetPos.z);
            this.placeList.add(targetPos);
            if (this.placeBlockInRange(targetPos, this.range.getValue())) {
                ++blocksPlaced;
            }
            ++this.offsetStep;
        }
        if (blocksPlaced > 0) {
            if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
                AutoTrap.mc.player.inventory.currentItem = this.playerHotbarSlot;
                this.lastHotbarSlot = this.playerHotbarSlot;
            }
            if (this.isSneaking) {
                AutoTrap.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)AutoTrap.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                this.isSneaking = false;
            }
        }
    }
    
    private boolean placeBlockInRange(final BlockPos pos, final double range) {
        final Block block = AutoTrap.mc.world.getBlockState(pos).getBlock();
        if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid)) {
            this.placeList.remove(pos);
            return false;
        }
        for (final Entity entity : AutoTrap.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(pos))) {
            if (!(entity instanceof EntityItem) && !(entity instanceof EntityXPOrb)) {
                return false;
            }
        }
        final EnumFacing side = getPlaceableSide(pos);
        if (side == null) {
            return false;
        }
        final BlockPos neighbour = pos.offset(side);
        final EnumFacing opposite = side.getOpposite();
        if (!canBeClicked(neighbour)) {
            return false;
        }
        final Vec3d hitVec = new Vec3d((Vec3i)neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        final Block neighbourBlock = AutoTrap.mc.world.getBlockState(neighbour).getBlock();
        if (AutoTrap.mc.player.getPositionVector().distanceTo(hitVec) > range) {
            return false;
        }
        final int obiSlot = this.findObiInHotbar();
        if (obiSlot == -1) {
            this.disable();
        }
        if (this.lastHotbarSlot != obiSlot) {
            AutoTrap.mc.player.inventory.currentItem = obiSlot;
            this.lastHotbarSlot = obiSlot;
        }
        if ((!this.isSneaking && AutoTrap.blackList.contains(neighbourBlock)) || AutoTrap.shulkerList.contains(neighbourBlock)) {
            AutoTrap.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)AutoTrap.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            this.isSneaking = true;
        }
        if (this.rotate.getValue()) {
            BlockUtil.faceVectorPacketInstant(hitVec);
        }
        AutoTrap.mc.playerController.processRightClickBlock(AutoTrap.mc.player, AutoTrap.mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
        AutoTrap.mc.player.swingArm(EnumHand.MAIN_HAND);
        AutoTrap.mc.rightClickDelayTimer = 4;
        if (this.noGlitchBlocks.getValue() && !AutoTrap.mc.playerController.getCurrentGameType().equals((Object)GameType.CREATIVE)) {
            AutoTrap.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, neighbour, opposite));
        }
        return true;
    }
    
    private int findObiInHotbar() {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = AutoTrap.mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock) {
                final Block block = ((ItemBlock)stack.getItem()).getBlock();
                if (block instanceof BlockObsidian) {
                    slot = i;
                    break;
                }
            }
        }
        return slot;
    }
    
    private void findClosestTarget() {
        final List<EntityPlayer> playerList = (List<EntityPlayer>)AutoTrap.mc.world.playerEntities;
        this.closestTarget = null;
        for (final EntityPlayer target : playerList) {
            if (target == AutoTrap.mc.player) {
                continue;
            }
            if (OyVey.friendManager.isFriend(target.getName())) {
                continue;
            }
            if (!EntityUtil.isLiving((Entity)target)) {
                continue;
            }
            if (target.getDistance((Entity)AutoTrap.mc.player) > 7.0f) {
                continue;
            }
            if (target.getHealth() <= 0.0f) {
                continue;
            }
            if (this.closestTarget == null) {
                this.closestTarget = target;
            }
            else {
                if (AutoTrap.mc.player.getDistance((Entity)target) >= AutoTrap.mc.player.getDistance((Entity)this.closestTarget)) {
                    continue;
                }
                this.closestTarget = target;
            }
        }
    }
    
    @Override
    public String getDisplayInfo() {
        if (this.closestTarget != null) {
            return this.closestTarget.getName().toUpperCase();
        }
        return null;
    }
    
    static {
        blackList = Arrays.asList(Blocks.ENDER_CHEST, (Block)Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.CRAFTING_TABLE, Blocks.ANVIL, Blocks.BREWING_STAND, (Block)Blocks.HOPPER, Blocks.DROPPER, Blocks.DISPENSER);
        shulkerList = Arrays.asList(Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.SILVER_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX);
    }
    
    private enum ESPMode
    {
        Solid, 
        Outline, 
        Full, 
        None;
    }
    
    private enum Cage
    {
        TRAP, 
        TRAPTOP, 
        TRAPFULLROOF, 
        TRAPFULLROOFTOP, 
        CRYSTALEXA, 
        CRYSTAL, 
        CRYSTALFULLROOF;
    }
    
    private static class Offsets
    {
        private static final Vec3d[] TRAP;
        private static final Vec3d[] TRAPTOP;
        private static final Vec3d[] TRAPFULLROOF;
        private static final Vec3d[] TRAPFULLROOFTOP;
        private static final Vec3d[] CRYSTALEXA;
        private static final Vec3d[] CRYSTAL;
        private static final Vec3d[] CRYSTALFULLROOF;
        
        static {
            TRAP = new Vec3d[] { new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 2.0, -1.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(-1.0, 2.0, 0.0), new Vec3d(0.0, 3.0, -1.0), new Vec3d(0.0, 3.0, 0.0) };
            TRAPTOP = new Vec3d[] { new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 2.0, -1.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(-1.0, 2.0, 0.0), new Vec3d(0.0, 3.0, -1.0), new Vec3d(0.0, 3.0, 0.0), new Vec3d(0.0, 4.0, 0.0) };
            TRAPFULLROOF = new Vec3d[] { new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 2.0, -1.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(-1.0, 2.0, 0.0), new Vec3d(0.0, 3.0, -1.0), new Vec3d(1.0, 3.0, 0.0), new Vec3d(0.0, 3.0, 1.0), new Vec3d(-1.0, 3.0, 0.0), new Vec3d(0.0, 3.0, 0.0) };
            TRAPFULLROOFTOP = new Vec3d[] { new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 2.0, -1.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(-1.0, 2.0, 0.0), new Vec3d(0.0, 3.0, -1.0), new Vec3d(1.0, 3.0, 0.0), new Vec3d(0.0, 3.0, 1.0), new Vec3d(-1.0, 3.0, 0.0), new Vec3d(0.0, 3.0, 0.0), new Vec3d(0.0, 4.0, 0.0) };
            CRYSTALEXA = new Vec3d[] { new Vec3d(0.0, 0.0, -1.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(0.0, 2.0, -1.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(-1.0, 2.0, 0.0), new Vec3d(-1.0, 2.0, -1.0), new Vec3d(1.0, 2.0, 1.0), new Vec3d(1.0, 2.0, -1.0), new Vec3d(-1.0, 2.0, 1.0), new Vec3d(0.0, 3.0, -1.0), new Vec3d(0.0, 3.0, 0.0) };
            CRYSTAL = new Vec3d[] { new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(-1.0, 0.0, 1.0), new Vec3d(1.0, 0.0, -1.0), new Vec3d(-1.0, 0.0, -1.0), new Vec3d(1.0, 0.0, 1.0), new Vec3d(-1.0, 1.0, -1.0), new Vec3d(1.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, 1.0), new Vec3d(1.0, 1.0, -1.0), new Vec3d(0.0, 2.0, -1.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(-1.0, 2.0, 0.0), new Vec3d(-1.0, 2.0, 1.0), new Vec3d(1.0, 2.0, -1.0), new Vec3d(0.0, 3.0, -1.0), new Vec3d(0.0, 3.0, 0.0) };
            CRYSTALFULLROOF = new Vec3d[] { new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(-1.0, 0.0, 1.0), new Vec3d(1.0, 0.0, -1.0), new Vec3d(-1.0, 0.0, -1.0), new Vec3d(1.0, 0.0, 1.0), new Vec3d(-1.0, 1.0, -1.0), new Vec3d(1.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, 1.0), new Vec3d(1.0, 1.0, -1.0), new Vec3d(0.0, 2.0, -1.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(-1.0, 2.0, 0.0), new Vec3d(-1.0, 2.0, 1.0), new Vec3d(1.0, 2.0, -1.0), new Vec3d(0.0, 3.0, -1.0), new Vec3d(1.0, 3.0, 0.0), new Vec3d(0.0, 3.0, 1.0), new Vec3d(-1.0, 3.0, 0.0), new Vec3d(0.0, 3.0, 0.0) };
        }
    }
}
