//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.features.setting.*;
import net.minecraft.entity.*;
import me.alpha432.oyvey.*;
import net.minecraft.network.play.server.*;
import net.minecraft.init.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.alpha432.oyvey.event.events.*;
import java.awt.*;
import com.mojang.realmsclient.gui.*;
import net.minecraft.entity.item.*;
import java.util.*;
import net.minecraft.network.*;
import net.minecraft.block.*;
import me.alpha432.oyvey.features.command.*;
import me.alpha432.oyvey.util.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.network.play.client.*;

public class Surround extends Module
{
    public static boolean isPlacing;
    private final Setting<Integer> blocksPerTick;
    private final Setting<Integer> delay;
    private final Setting<Boolean> noGhost;
    private final Setting<Boolean> center;
    private final Setting<Boolean> rotate;
    private final Setting<Boolean> attack;
    private final Setting<Boolean> blockChange;
    private final Setting<Boolean> render;
    private final Setting<Integer> red;
    private final Setting<Integer> green;
    private final Setting<Integer> blue;
    private final Setting<Integer> alpha;
    private final Timer timer;
    private int isSafe;
    private BlockPos startPos;
    private boolean didPlace;
    private int lastHotbarSlot;
    private boolean isSneaking;
    private int placements;
    private int obbySlot;
    private boolean offHand;
    public static Surround INSTANCE;
    List<Vec3d> offsets;
    List<BlockPos> ghostPlace;
    private BlockPos renderPos;
    
    public static Surround getInstance() {
        if (Surround.INSTANCE == null) {
            Surround.INSTANCE = new Surround();
        }
        return Surround.INSTANCE;
    }
    
    @Override
    public void onToggle() {
        this.renderPos = null;
    }
    
    public Surround() {
        super("Surround", "Surrounds you with Obsidian", Category.COMBAT, true, false, false);
        this.blocksPerTick = (Setting<Integer>)this.register(new Setting("BlocksPerTick", (T)12, (T)1, (T)20));
        this.delay = (Setting<Integer>)this.register(new Setting("Delay", (T)0, (T)0, (T)250));
        this.noGhost = (Setting<Boolean>)this.register(new Setting("PacketPlace", (T)true));
        this.center = (Setting<Boolean>)this.register(new Setting("TPCenter", (T)false));
        this.rotate = (Setting<Boolean>)this.register(new Setting("Rotate", (T)true));
        this.attack = (Setting<Boolean>)this.register(new Setting("Attack", (T)true));
        this.blockChange = (Setting<Boolean>)this.register(new Setting("BlockChange", (T)true));
        this.render = (Setting<Boolean>)this.register(new Setting("Render", (T)true));
        this.red = (Setting<Integer>)this.register(new Setting("Red", (T)12, (T)0, (T)255));
        this.green = (Setting<Integer>)this.register(new Setting("Green", (T)22, (T)0, (T)255));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", (T)232, (T)0, (T)255));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", (T)154, (T)0, (T)255));
        this.timer = new Timer();
        this.didPlace = false;
        this.placements = 0;
        this.obbySlot = -1;
        this.offHand = false;
        this.offsets = new ArrayList<Vec3d>();
        this.ghostPlace = new ArrayList<BlockPos>();
        Surround.INSTANCE = this;
    }
    
    @Override
    public void onEnable() {
        if (fullNullCheck()) {
            this.disable();
        }
        this.lastHotbarSlot = Surround.mc.player.inventory.currentItem;
        this.startPos = EntityUtil.getRoundedBlockPos((Entity)Surround.mc.player);
        if (this.center.getValue()) {
            OyVey.positionManager.setPositionPacket(this.startPos.getX() + 0.5, this.startPos.getY(), this.startPos.getZ() + 0.5, true, true, true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (nullCheck()) {
            return;
        }
        if (event.getPacket() instanceof SPacketBlockChange && this.blockChange.getValue()) {
            final SPacketBlockChange sPacketBlockChange = (SPacketBlockChange)event.getPacket();
            if (sPacketBlockChange.blockState.getBlock() == Blocks.AIR && Surround.mc.player.getDistance((double)sPacketBlockChange.blockPosition.x, (double)sPacketBlockChange.blockPosition.y, (double)sPacketBlockChange.blockPosition.z) < 1.75) {
                Surround.mc.addScheduledTask(() -> this.doFeetPlace());
            }
        }
    }
    
    @Override
    public void onTick() {
        Surround.mc.addScheduledTask(() -> this.doFeetPlace());
    }
    
    @Override
    public void onRender3D(final Render3DEvent event) {
        if (this.render.getValue() && this.renderPos != null) {
            RenderUtil.drawBox(this.renderPos, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()));
        }
    }
    
    @Override
    public void onDisable() {
        if (nullCheck()) {
            return;
        }
        Surround.isPlacing = false;
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
    }
    
    @Override
    public String getDisplayInfo() {
        switch (this.isSafe) {
            case 0: {
                return ChatFormatting.RED + "Unsafe";
            }
            case 1: {
                return ChatFormatting.YELLOW + "Safe";
            }
            default: {
                return ChatFormatting.GREEN + "Safe";
            }
        }
    }
    
    private boolean isInterceptedByCrystal(final BlockPos pos) {
        for (final Entity entity : Surround.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityEnderCrystal)) {
                continue;
            }
            if (entity.equals((Object)Surround.mc.player)) {
                continue;
            }
            if (entity instanceof EntityItem) {
                continue;
            }
            if (new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isInterceptedByOther(final BlockPos pos) {
        for (final Entity entity : Surround.mc.world.loadedEntityList) {
            if (entity instanceof EntityEnderCrystal) {
                continue;
            }
            if (entity instanceof EntityItem) {
                continue;
            }
            if (new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) {
                return true;
            }
        }
        return false;
    }
    
    private void doFeetPlace() {
        if (this.check()) {
            return;
        }
        this.renderPos = null;
        this.ghostPlace = new ArrayList<BlockPos>();
        this.offsets = new ArrayList<Vec3d>();
        if (this.isChestBelow()) {
            this.offsets.add(Surround.mc.player.getPositionVector().add(1.0, 0.0, 0.0));
            this.offsets.add(Surround.mc.player.getPositionVector().add(-1.0, 0.0, 0.0));
            this.offsets.add(Surround.mc.player.getPositionVector().add(0.0, 0.0, 1.0));
            this.offsets.add(Surround.mc.player.getPositionVector().add(0.0, 0.0, -1.0));
            this.offsets.add(Surround.mc.player.getPositionVector().add(1.0, 1.0, 0.0));
            this.offsets.add(Surround.mc.player.getPositionVector().add(-1.0, 1.0, 0.0));
            this.offsets.add(Surround.mc.player.getPositionVector().add(0.0, 1.0, 1.0));
            this.offsets.add(Surround.mc.player.getPositionVector().add(0.0, 1.0, -1.0));
        }
        else {
            this.offsets.add(Surround.mc.player.getPositionVector().add(0.0, -1.0, 0.0));
            this.offsets.add(Surround.mc.player.getPositionVector().add(1.0, -1.0, 0.0));
            this.offsets.add(Surround.mc.player.getPositionVector().add(-1.0, -1.0, 0.0));
            this.offsets.add(Surround.mc.player.getPositionVector().add(0.0, -1.0, 1.0));
            this.offsets.add(Surround.mc.player.getPositionVector().add(0.0, -1.0, -1.0));
            this.offsets.add(Surround.mc.player.getPositionVector().add(1.0, 0.0, 0.0));
            this.offsets.add(Surround.mc.player.getPositionVector().add(-1.0, 0.0, 0.0));
            this.offsets.add(Surround.mc.player.getPositionVector().add(0.0, 0.0, 1.0));
            this.offsets.add(Surround.mc.player.getPositionVector().add(0.0, 0.0, -1.0));
        }
        final List<BlockPos> blockPosList = new ArrayList<BlockPos>();
        for (final Vec3d vec3d : this.offsets) {
            final BlockPos pos = new BlockPos(vec3d);
            if (Surround.mc.world.getBlockState(pos).getBlock() == Blocks.AIR) {
                blockPosList.add(pos);
            }
        }
        if (blockPosList.isEmpty()) {
            return;
        }
        for (final BlockPos blockPos : blockPosList) {
            if (this.placements > this.blocksPerTick.getValue()) {
                return;
            }
            if (this.isInterceptedByOther(blockPos)) {
                continue;
            }
            if (this.isInterceptedByCrystal(blockPos)) {
                if (!this.attack.getValue()) {
                    continue;
                }
                EntityEnderCrystal crystal = null;
                for (final Entity entity : Surround.mc.world.loadedEntityList) {
                    if (entity == null) {
                        continue;
                    }
                    if (Surround.mc.player.getDistance(entity) > 2.4) {
                        continue;
                    }
                    if (!(entity instanceof EntityEnderCrystal)) {
                        continue;
                    }
                    if (entity.isDead) {
                        continue;
                    }
                    crystal = (EntityEnderCrystal)entity;
                }
                if (crystal != null) {
                    if (this.rotate.getValue()) {
                        RotationUtil.faceEntity((Entity)crystal);
                    }
                    Surround.mc.getConnection().sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
                    Surround.mc.getConnection().sendPacket((Packet)new CPacketUseEntity((Entity)crystal));
                }
            }
            this.placeBlock(this.renderPos = blockPos);
            ++this.placements;
        }
    }
    
    public boolean isBurrow() {
        final Block block = Surround.mc.world.getBlockState(new BlockPos(Surround.mc.player.getPositionVector().add(0.0, 0.2, 0.0))).getBlock();
        return block == Blocks.OBSIDIAN || block == Blocks.ENDER_CHEST;
    }
    
    private boolean isChestBelow() {
        return !this.isBurrow() && EntityUtil.isOnChest((Entity)Surround.mc.player);
    }
    
    private boolean check() {
        if (nullCheck()) {
            return true;
        }
        final int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        final int eChestSot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
        if (obbySlot == -1 && eChestSot == -1) {
            this.toggle();
        }
        this.offHand = InventoryUtil.isBlock(Surround.mc.player.getHeldItemOffhand().getItem(), BlockObsidian.class);
        Surround.isPlacing = false;
        this.didPlace = false;
        this.placements = 0;
        this.obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        final int echestSlot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
        if (this.isOff()) {
            return true;
        }
        if (this.obbySlot == -1 && !this.offHand && echestSlot == -1) {
            Command.sendMessage("<" + this.getDisplayName() + "> " + ChatFormatting.RED + "No blocks in hotbar disabling...");
            this.disable();
            return true;
        }
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
        if (Surround.mc.player.inventory.currentItem != this.lastHotbarSlot && Surround.mc.player.inventory.currentItem != this.obbySlot && Surround.mc.player.inventory.currentItem != echestSlot) {
            this.lastHotbarSlot = Surround.mc.player.inventory.currentItem;
        }
        if (!this.startPos.equals((Object)EntityUtil.getRoundedBlockPos((Entity)Surround.mc.player))) {
            this.disable();
            return true;
        }
        return !this.timer.passedMs(this.delay.getValue());
    }
    
    private void placeBlock(final BlockPos pos) {
        try {
            final int originalSlot = Surround.mc.player.inventory.currentItem;
            final int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
            final int eChestSot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
            if (obbySlot == -1 && eChestSot == -1) {
                this.toggle();
            }
            Surround.isPlacing = true;
            Surround.mc.player.inventory.currentItem = ((obbySlot == -1) ? eChestSot : obbySlot);
            Surround.mc.playerController.updateController();
            this.isSneaking = this.placeBlock(pos, this.offHand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, this.rotate.getValue(), this.noGhost.getValue(), this.isSneaking);
            Surround.mc.player.inventory.currentItem = originalSlot;
            Surround.mc.playerController.updateController();
            this.didPlace = true;
        }
        catch (Exception ex) {}
    }
    
    public boolean placeBlock(final BlockPos pos, final EnumHand hand, final boolean rotate, final boolean packet, final boolean isSneaking) {
        boolean sneaking = false;
        EnumFacing side = BlockUtil.getFirstFacing(pos);
        if (side == null) {
            side = EnumFacing.DOWN;
        }
        final BlockPos neighbour = pos.offset(side);
        final EnumFacing opposite = side.getOpposite();
        final Vec3d hitVec = new Vec3d((Vec3i)neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        final Block neighbourBlock = BlockUtil.mc.world.getBlockState(neighbour).getBlock();
        if (!BlockUtil.mc.player.isSneaking() && (BlockUtil.blackList.contains(neighbourBlock) || BlockUtil.shulkerList.contains(neighbourBlock))) {
            BlockUtil.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BlockUtil.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            BlockUtil.mc.player.setSneaking(true);
            sneaking = true;
        }
        if (rotate) {
            RotationUtil.faceVector(hitVec, true);
        }
        BlockUtil.rightClickBlock(neighbour, hitVec, hand, opposite, packet);
        Surround.mc.player.connection.sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
        BlockUtil.mc.rightClickDelayTimer = 0;
        return sneaking || isSneaking;
    }
    
    static {
        Surround.isPlacing = false;
    }
}
