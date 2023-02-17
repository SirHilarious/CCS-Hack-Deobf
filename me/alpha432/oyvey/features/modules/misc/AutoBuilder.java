//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.features.setting.*;
import net.minecraft.client.renderer.culling.*;
import net.minecraft.entity.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import net.minecraft.network.*;
import net.minecraft.network.play.client.*;
import java.util.*;
import net.minecraft.util.math.*;
import net.minecraft.client.entity.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.alpha432.oyvey.event.events.*;
import net.minecraft.client.renderer.*;
import org.lwjgl.opengl.*;
import me.alpha432.oyvey.util.*;
import net.minecraft.block.state.*;
import net.minecraft.item.*;
import net.minecraft.client.*;
import net.minecraft.block.*;

public class AutoBuilder extends Module
{
    public final Setting<Modes> Mode;
    public final Setting<BuildingModes> BuildingMode;
    public final Setting<Integer> BlocksPerTick;
    public final Setting<Float> Delay;
    public final Setting<Boolean> Visualize;
    private Vec3d Center;
    private ICamera camera;
    private Timer timer;
    private Timer NetherPortalTimer;
    private BlockPos SourceBlock;
    ArrayList<BlockPos> BlockArray;
    private float PitchHead;
    private boolean SentPacket;
    
    public AutoBuilder() {
        super("AutoBuilder", "Builds stuff", Category.MISC, true, false, false);
        this.Mode = (Setting<Modes>)this.register(new Setting("Mode", (T)Modes.Highway));
        this.BuildingMode = (Setting<BuildingModes>)this.register(new Setting("BuildingMode", (T)BuildingModes.Dynamic));
        this.BlocksPerTick = (Setting<Integer>)this.register(new Setting("BlocksPerTick", (T)4, (T)1, (T)10));
        this.Delay = (Setting<Float>)this.register(new Setting("Delay", (T)0.0f, (T)0.1f, (T)1.0f));
        this.Visualize = (Setting<Boolean>)this.register(new Setting("Visualize", (T)true));
        this.Center = Vec3d.ZERO;
        this.camera = (ICamera)new Frustum();
        this.timer = new Timer();
        this.NetherPortalTimer = new Timer();
        this.SourceBlock = null;
        this.BlockArray = new ArrayList<BlockPos>();
        this.PitchHead = 0.0f;
        this.SentPacket = false;
    }
    
    @Override
    public void onEnable() {
        if (AutoBuilder.mc.player == null) {
            this.toggle();
            return;
        }
        this.timer.reset();
        this.SourceBlock = null;
        this.BlockArray.clear();
    }
    
    @Override
    public String getDisplayInfo() {
        return this.Mode.getValue().toString() + " - " + this.BuildingMode.getValue().toString();
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 1) {
            return;
        }
        if (!this.timer.passedMs((long)(this.Delay.getValue() * 1000.0f))) {
            return;
        }
        this.timer.reset();
        final Vec3d pos = MathUtil.interpolateEntity((Entity)AutoBuilder.mc.player, AutoBuilder.mc.getRenderPartialTicks());
        BlockPos orignPos = new BlockPos(pos.x, pos.y + 0.5, pos.z);
        final Pair<Integer, Block> l_Pair = this.findStackHotbar();
        int slot = -1;
        final double l_Offset = pos.y - orignPos.getY();
        if (l_Pair != null) {
            slot = l_Pair.getKey();
            if (l_Pair.getValue() instanceof BlockSlab && l_Offset == 0.5) {
                orignPos = new BlockPos(pos.x, pos.y + 0.5, pos.z);
            }
        }
        if (this.BuildingMode.getValue() == BuildingModes.Dynamic) {
            this.BlockArray.clear();
        }
        if (this.BlockArray.isEmpty()) {
            this.FillBlockArrayAsNeeded(pos, orignPos, l_Pair);
        }
        boolean l_NeedPlace = false;
        float[] rotations = null;
        if (slot != -1 && AutoBuilder.mc.player.onGround) {
            final int lastSlot = AutoBuilder.mc.player.inventory.currentItem;
            AutoBuilder.mc.player.inventory.currentItem = slot;
            AutoBuilder.mc.playerController.updateController();
            int l_BlocksPerTick = this.BlocksPerTick.getValue();
            for (final BlockPos l_Pos : this.BlockArray) {
                final BlockUtil.PlaceResult l_Place = BlockUtil.place(l_Pos, 5.0f, false, l_Offset == -0.5);
                if (l_Place != BlockUtil.PlaceResult.Placed) {
                    continue;
                }
                l_NeedPlace = true;
                rotations = RotationUtil.getLegitRotations(new Vec3d((double)l_Pos.getX(), (double)l_Pos.getY(), (double)l_Pos.getZ()));
                if (--l_BlocksPerTick <= 0) {
                    break;
                }
            }
            if (!this.slotEqualsBlock(lastSlot, l_Pair.getValue())) {
                AutoBuilder.mc.player.inventory.currentItem = lastSlot;
            }
            AutoBuilder.mc.playerController.updateController();
        }
        if (!l_NeedPlace && this.Mode.getValue() == Modes.Portal) {
            if (AutoBuilder.mc.world.getBlockState(this.BlockArray.get(0).up()).getBlock() == Blocks.PORTAL || !this.VerifyPortalFrame(this.BlockArray)) {
                return;
            }
            if (AutoBuilder.mc.player.getHeldItemMainhand().getItem() != Items.FLINT_AND_STEEL) {
                for (int l_I = 0; l_I < 9; ++l_I) {
                    final ItemStack l_Stack = AutoBuilder.mc.player.inventory.getStackInSlot(l_I);
                    if (!l_Stack.isEmpty()) {
                        if (l_Stack.getItem() == Items.FLINT_AND_STEEL) {
                            AutoBuilder.mc.player.inventory.currentItem = l_I;
                            AutoBuilder.mc.playerController.updateController();
                            this.NetherPortalTimer.reset();
                            break;
                        }
                    }
                }
            }
            if (this.NetherPortalTimer.passedMs(500L)) {
                return;
            }
            if (this.SentPacket) {
                AutoBuilder.mc.player.swingArm(EnumHand.MAIN_HAND);
                AutoBuilder.mc.getConnection().sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock((BlockPos)this.BlockArray.get(0), EnumFacing.UP, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
            }
            rotations = RotationUtil.getLegitRotations(new Vec3d((double)this.BlockArray.get(0).getX(), (double)(this.BlockArray.get(0).getY() + 0.5f), (double)this.BlockArray.get(0).getZ()));
            l_NeedPlace = true;
        }
        else if (l_NeedPlace && this.Mode.getValue() == Modes.Portal) {
            this.NetherPortalTimer.reset();
        }
        if (!l_NeedPlace || rotations == null) {
            this.PitchHead = -420.0f;
            this.SentPacket = false;
            return;
        }
        final boolean l_IsSprinting = AutoBuilder.mc.player.isSprinting();
        if (l_IsSprinting != AutoBuilder.mc.player.serverSprintState) {
            if (l_IsSprinting) {
                AutoBuilder.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)AutoBuilder.mc.player, CPacketEntityAction.Action.START_SPRINTING));
            }
            else {
                AutoBuilder.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)AutoBuilder.mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
            }
            AutoBuilder.mc.player.serverSprintState = l_IsSprinting;
        }
        final boolean l_IsSneaking = AutoBuilder.mc.player.isSneaking();
        if (l_IsSneaking != AutoBuilder.mc.player.serverSneakState) {
            if (l_IsSneaking) {
                AutoBuilder.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)AutoBuilder.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            }
            else {
                AutoBuilder.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)AutoBuilder.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            }
            AutoBuilder.mc.player.serverSneakState = l_IsSneaking;
        }
        if (PlayerUtil.isCurrentViewEntity()) {
            final float l_Pitch = rotations[1];
            final float l_Yaw = rotations[0];
            AutoBuilder.mc.player.rotationYawHead = l_Yaw;
            this.PitchHead = l_Pitch;
            final AxisAlignedBB axisalignedbb = AutoBuilder.mc.player.getEntityBoundingBox();
            final double l_PosXDifference = AutoBuilder.mc.player.posX - AutoBuilder.mc.player.lastReportedPosX;
            final double l_PosYDifference = axisalignedbb.minY - AutoBuilder.mc.player.lastReportedPosY;
            final double l_PosZDifference = AutoBuilder.mc.player.posZ - AutoBuilder.mc.player.lastReportedPosZ;
            final double l_YawDifference = l_Yaw - AutoBuilder.mc.player.lastReportedYaw;
            final double l_RotationDifference = l_Pitch - AutoBuilder.mc.player.lastReportedPitch;
            final EntityPlayerSP player = AutoBuilder.mc.player;
            ++player.positionUpdateTicks;
            boolean l_MovedXYZ = l_PosXDifference * l_PosXDifference + l_PosYDifference * l_PosYDifference + l_PosZDifference * l_PosZDifference > 9.0E-4 || AutoBuilder.mc.player.positionUpdateTicks >= 20;
            final boolean l_MovedRotation = l_YawDifference != 0.0 || l_RotationDifference != 0.0;
            if (AutoBuilder.mc.player.isRiding()) {
                AutoBuilder.mc.player.connection.sendPacket((Packet)new CPacketPlayer.PositionRotation(AutoBuilder.mc.player.motionX, -999.0, AutoBuilder.mc.player.motionZ, l_Yaw, l_Pitch, AutoBuilder.mc.player.onGround));
                l_MovedXYZ = false;
            }
            else if (l_MovedXYZ && l_MovedRotation) {
                AutoBuilder.mc.player.connection.sendPacket((Packet)new CPacketPlayer.PositionRotation(AutoBuilder.mc.player.posX, axisalignedbb.minY, AutoBuilder.mc.player.posZ, l_Yaw, l_Pitch, AutoBuilder.mc.player.onGround));
            }
            else if (l_MovedXYZ) {
                AutoBuilder.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(AutoBuilder.mc.player.posX, axisalignedbb.minY, AutoBuilder.mc.player.posZ, AutoBuilder.mc.player.onGround));
            }
            else if (l_MovedRotation) {
                AutoBuilder.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(l_Yaw, l_Pitch, AutoBuilder.mc.player.onGround));
            }
            else if (AutoBuilder.mc.player.prevOnGround != AutoBuilder.mc.player.onGround) {
                AutoBuilder.mc.player.connection.sendPacket((Packet)new CPacketPlayer(AutoBuilder.mc.player.onGround));
            }
            if (l_MovedXYZ) {
                AutoBuilder.mc.player.lastReportedPosX = AutoBuilder.mc.player.posX;
                AutoBuilder.mc.player.lastReportedPosY = axisalignedbb.minY;
                AutoBuilder.mc.player.lastReportedPosZ = AutoBuilder.mc.player.posZ;
                AutoBuilder.mc.player.positionUpdateTicks = 0;
            }
            if (l_MovedRotation) {
                AutoBuilder.mc.player.lastReportedYaw = l_Yaw;
                AutoBuilder.mc.player.lastReportedPitch = l_Pitch;
            }
            this.SentPacket = true;
            AutoBuilder.mc.player.prevOnGround = AutoBuilder.mc.player.onGround;
            AutoBuilder.mc.player.autoJumpEnabled = AutoBuilder.mc.player.mc.gameSettings.autoJump;
        }
    }
    
    @Override
    public void onRender2D(final Render2DEvent event) {
        if (!this.Visualize.getValue()) {
            return;
        }
        for (final BlockPos l_Pos : this.BlockArray) {
            final IBlockState l_State = AutoBuilder.mc.world.getBlockState(l_Pos);
            if (l_State != null && l_State.getBlock() != Blocks.AIR && l_State.getBlock() != Blocks.WATER) {
                continue;
            }
            final AxisAlignedBB bb = new AxisAlignedBB(l_Pos.getX() - AutoBuilder.mc.getRenderManager().viewerPosX, l_Pos.getY() - AutoBuilder.mc.getRenderManager().viewerPosY, l_Pos.getZ() - AutoBuilder.mc.getRenderManager().viewerPosZ, l_Pos.getX() + 1 - AutoBuilder.mc.getRenderManager().viewerPosX, l_Pos.getY() + 1 - AutoBuilder.mc.getRenderManager().viewerPosY, l_Pos.getZ() + 1 - AutoBuilder.mc.getRenderManager().viewerPosZ);
            this.camera.setPosition(AutoBuilder.mc.getRenderViewEntity().posX, AutoBuilder.mc.getRenderViewEntity().posY, AutoBuilder.mc.getRenderViewEntity().posZ);
            if (!this.camera.isBoundingBoxInFrustum(new AxisAlignedBB(bb.minX + AutoBuilder.mc.getRenderManager().viewerPosX, bb.minY + AutoBuilder.mc.getRenderManager().viewerPosY, bb.minZ + AutoBuilder.mc.getRenderManager().viewerPosZ, bb.maxX + AutoBuilder.mc.getRenderManager().viewerPosX, bb.maxY + AutoBuilder.mc.getRenderManager().viewerPosY, bb.maxZ + AutoBuilder.mc.getRenderManager().viewerPosZ))) {
                continue;
            }
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            GL11.glEnable(2848);
            GL11.glHint(3154, 4354);
            GL11.glLineWidth(1.5f);
            final double dist = AutoBuilder.mc.player.getDistance((double)(l_Pos.getX() + 0.5f), (double)(l_Pos.getY() + 0.5f), (double)(l_Pos.getZ() + 0.5f)) * 0.75;
            final float alpha = MathUtil.clamp((float)(dist * 255.0 / 5.0 / 255.0), 0.0f, 0.3f);
            final int l_Color = -1878982657;
            RenderUtil.drawBoundingBox(bb, 1.0f, l_Color);
            RenderUtil.drawFilledBox(bb, l_Color);
            GL11.glDisable(2848);
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }
    
    private boolean slotEqualsBlock(final int slot, final Block type) {
        if (AutoBuilder.mc.player.inventory.getStackInSlot(slot).getItem() instanceof ItemBlock) {
            final ItemBlock block = (ItemBlock)AutoBuilder.mc.player.inventory.getStackInSlot(slot).getItem();
            return block.getBlock() == type;
        }
        return false;
    }
    
    private void FillBlockArrayAsNeeded(final Vec3d pos, final BlockPos orignPos, final Pair<Integer, Block> p_Pair) {
        BlockPos interpPos = null;
        Label_6223: {
            switch (this.Mode.getValue()) {
                case Highway: {
                    switch (PlayerUtil.GetFacing()) {
                        case East: {
                            this.BlockArray.add(orignPos.down());
                            this.BlockArray.add(orignPos.down().east());
                            this.BlockArray.add(orignPos.down().east().north());
                            this.BlockArray.add(orignPos.down().east().south());
                            this.BlockArray.add(orignPos.down().east().north().north());
                            this.BlockArray.add(orignPos.down().east().south().south());
                            this.BlockArray.add(orignPos.down().east().north().north().north());
                            this.BlockArray.add(orignPos.down().east().south().south().south());
                            this.BlockArray.add(orignPos.down().east().north().north().north().up());
                            this.BlockArray.add(orignPos.down().east().south().south().south().up());
                            break Label_6223;
                        }
                        case North: {
                            this.BlockArray.add(orignPos.down());
                            this.BlockArray.add(orignPos.down().north());
                            this.BlockArray.add(orignPos.down().north().east());
                            this.BlockArray.add(orignPos.down().north().west());
                            this.BlockArray.add(orignPos.down().north().east().east());
                            this.BlockArray.add(orignPos.down().north().west().west());
                            this.BlockArray.add(orignPos.down().north().east().east().east());
                            this.BlockArray.add(orignPos.down().north().west().west().west());
                            this.BlockArray.add(orignPos.down().north().east().east().east().up());
                            this.BlockArray.add(orignPos.down().north().west().west().west().up());
                            break Label_6223;
                        }
                        case South: {
                            this.BlockArray.add(orignPos.down());
                            this.BlockArray.add(orignPos.down().south());
                            this.BlockArray.add(orignPos.down().south().east());
                            this.BlockArray.add(orignPos.down().south().west());
                            this.BlockArray.add(orignPos.down().south().east().east());
                            this.BlockArray.add(orignPos.down().south().west().west());
                            this.BlockArray.add(orignPos.down().south().east().east().east());
                            this.BlockArray.add(orignPos.down().south().west().west().west());
                            this.BlockArray.add(orignPos.down().south().east().east().east().up());
                            this.BlockArray.add(orignPos.down().south().west().west().west().up());
                            break Label_6223;
                        }
                        case West: {
                            this.BlockArray.add(orignPos.down());
                            this.BlockArray.add(orignPos.down().west());
                            this.BlockArray.add(orignPos.down().west().north());
                            this.BlockArray.add(orignPos.down().west().south());
                            this.BlockArray.add(orignPos.down().west().north().north());
                            this.BlockArray.add(orignPos.down().west().south().south());
                            this.BlockArray.add(orignPos.down().west().north().north().north());
                            this.BlockArray.add(orignPos.down().west().south().south().south());
                            this.BlockArray.add(orignPos.down().west().north().north().north().up());
                            this.BlockArray.add(orignPos.down().west().south().south().south().up());
                            break Label_6223;
                        }
                        default: {
                            break Label_6223;
                        }
                    }
                    break;
                }
                case HighwayTunnel: {
                    this.BlockArray.add(orignPos.down());
                    this.BlockArray.add(orignPos.down().north());
                    this.BlockArray.add(orignPos.down().north().east());
                    this.BlockArray.add(orignPos.down().north().west());
                    this.BlockArray.add(orignPos.down().north().east().east());
                    this.BlockArray.add(orignPos.down().north().west().west());
                    this.BlockArray.add(orignPos.down().north().east().east().east());
                    this.BlockArray.add(orignPos.down().north().west().west().west());
                    this.BlockArray.add(orignPos.down().north().east().east().east().up());
                    this.BlockArray.add(orignPos.down().north().west().west().west().up());
                    this.BlockArray.add(orignPos.down().north().east().east().east().up().up());
                    this.BlockArray.add(orignPos.down().north().west().west().west().up().up());
                    this.BlockArray.add(orignPos.down().north().east().east().east().up().up().up());
                    this.BlockArray.add(orignPos.down().north().west().west().west().up().up().up());
                    this.BlockArray.add(orignPos.down().north().east().east().east().up().up().up().up());
                    this.BlockArray.add(orignPos.down().north().west().west().west().up().up().up().up());
                    this.BlockArray.add(orignPos.down().north().east().east().east().up().up().up().up().west());
                    this.BlockArray.add(orignPos.down().north().west().west().west().up().up().up().up().east());
                    this.BlockArray.add(orignPos.down().north().east().east().east().up().up().up().up().west().west());
                    this.BlockArray.add(orignPos.down().north().west().west().west().up().up().up().up().east().east());
                    this.BlockArray.add(orignPos.down().north().east().east().east().up().up().up().up().west().west().west());
                    this.BlockArray.add(orignPos.down().north().west().west().west().up().up().up().up().east().east().east());
                    break;
                }
                case Swastika: {
                    switch (PlayerUtil.GetFacing()) {
                        case East: {
                            interpPos = new BlockPos(pos.x, pos.y, pos.z).east().east();
                            this.BlockArray.add(interpPos);
                            this.BlockArray.add(interpPos.north());
                            this.BlockArray.add(interpPos.north().north());
                            this.BlockArray.add(interpPos.up());
                            this.BlockArray.add(interpPos.up().up());
                            this.BlockArray.add(interpPos.up().up().north());
                            this.BlockArray.add(interpPos.up().up().north().north());
                            this.BlockArray.add(interpPos.up().up().north().north().up());
                            this.BlockArray.add(interpPos.up().up().north().north().up().up());
                            this.BlockArray.add(interpPos.up().up().south());
                            this.BlockArray.add(interpPos.up().up().south().south());
                            this.BlockArray.add(interpPos.up().up().south().south().down());
                            this.BlockArray.add(interpPos.up().up().south().south().down().down());
                            this.BlockArray.add(interpPos.up().up().up());
                            this.BlockArray.add(interpPos.up().up().up().up());
                            this.BlockArray.add(interpPos.up().up().up().up().south());
                            this.BlockArray.add(interpPos.up().up().up().up().south().south());
                            break Label_6223;
                        }
                        case North: {
                            interpPos = new BlockPos(pos.x, pos.y, pos.z).north().north();
                            this.BlockArray.add(interpPos);
                            this.BlockArray.add(interpPos.west());
                            this.BlockArray.add(interpPos.west().west());
                            this.BlockArray.add(interpPos.up());
                            this.BlockArray.add(interpPos.up().up());
                            this.BlockArray.add(interpPos.up().up().west());
                            this.BlockArray.add(interpPos.up().up().west().west());
                            this.BlockArray.add(interpPos.up().up().west().west().up());
                            this.BlockArray.add(interpPos.up().up().west().west().up().up());
                            this.BlockArray.add(interpPos.up().up().east());
                            this.BlockArray.add(interpPos.up().up().east().east());
                            this.BlockArray.add(interpPos.up().up().east().east().down());
                            this.BlockArray.add(interpPos.up().up().east().east().down().down());
                            this.BlockArray.add(interpPos.up().up().up());
                            this.BlockArray.add(interpPos.up().up().up().up());
                            this.BlockArray.add(interpPos.up().up().up().up().east());
                            this.BlockArray.add(interpPos.up().up().up().up().east().east());
                            break Label_6223;
                        }
                        case South: {
                            interpPos = new BlockPos(pos.x, pos.y, pos.z).south().south();
                            this.BlockArray.add(interpPos);
                            this.BlockArray.add(interpPos.east());
                            this.BlockArray.add(interpPos.east().east());
                            this.BlockArray.add(interpPos.up());
                            this.BlockArray.add(interpPos.up().up());
                            this.BlockArray.add(interpPos.up().up().east());
                            this.BlockArray.add(interpPos.up().up().east().east());
                            this.BlockArray.add(interpPos.up().up().east().east().up());
                            this.BlockArray.add(interpPos.up().up().east().east().up().up());
                            this.BlockArray.add(interpPos.up().up().west());
                            this.BlockArray.add(interpPos.up().up().west().west());
                            this.BlockArray.add(interpPos.up().up().west().west().down());
                            this.BlockArray.add(interpPos.up().up().west().west().down().down());
                            this.BlockArray.add(interpPos.up().up().up());
                            this.BlockArray.add(interpPos.up().up().up().up());
                            this.BlockArray.add(interpPos.up().up().up().up().west());
                            this.BlockArray.add(interpPos.up().up().up().up().west().west());
                            break Label_6223;
                        }
                        case West: {
                            interpPos = new BlockPos(pos.x, pos.y, pos.z).west().west();
                            this.BlockArray.add(interpPos);
                            this.BlockArray.add(interpPos.south());
                            this.BlockArray.add(interpPos.south().south());
                            this.BlockArray.add(interpPos.up());
                            this.BlockArray.add(interpPos.up().up());
                            this.BlockArray.add(interpPos.up().up().south());
                            this.BlockArray.add(interpPos.up().up().south().south());
                            this.BlockArray.add(interpPos.up().up().south().south().up());
                            this.BlockArray.add(interpPos.up().up().south().south().up().up());
                            this.BlockArray.add(interpPos.up().up().north());
                            this.BlockArray.add(interpPos.up().up().north().north());
                            this.BlockArray.add(interpPos.up().up().north().north().down());
                            this.BlockArray.add(interpPos.up().up().north().north().down().down());
                            this.BlockArray.add(interpPos.up().up().up());
                            this.BlockArray.add(interpPos.up().up().up().up());
                            this.BlockArray.add(interpPos.up().up().up().up().north());
                            this.BlockArray.add(interpPos.up().up().up().up().north().north());
                            break Label_6223;
                        }
                        default: {
                            break Label_6223;
                        }
                    }
                    break;
                }
                case Portal: {
                    switch (PlayerUtil.GetFacing()) {
                        case East: {
                            interpPos = new BlockPos(pos.x, pos.y, pos.z).east().east();
                            this.BlockArray.add(interpPos.south());
                            this.BlockArray.add(interpPos.south().south());
                            this.BlockArray.add(interpPos);
                            this.BlockArray.add(interpPos.south().south().up());
                            this.BlockArray.add(interpPos.south().south().up().up());
                            this.BlockArray.add(interpPos.south().south().up().up().up());
                            this.BlockArray.add(interpPos.south().south().up().up().up().up());
                            this.BlockArray.add(interpPos.south().south().up().up().up().up().north());
                            this.BlockArray.add(interpPos.south().south().up().up().up().up().north().north());
                            this.BlockArray.add(interpPos.south().south().up().up().up().up().north().north().north());
                            this.BlockArray.add(interpPos.south().south().up().up().up().up().north().north().north().down());
                            this.BlockArray.add(interpPos.south().south().up().up().up().up().north().north().north().down().down());
                            this.BlockArray.add(interpPos.south().south().up().up().up().up().north().north().north().down().down().down());
                            this.BlockArray.add(interpPos.south().south().up().up().up().up().north().north().north().down().down().down().down());
                            break Label_6223;
                        }
                        case North: {
                            interpPos = new BlockPos(pos.x, pos.y, pos.z).north().north();
                            this.BlockArray.add(interpPos.east());
                            this.BlockArray.add(interpPos.east().east());
                            this.BlockArray.add(interpPos);
                            this.BlockArray.add(interpPos.east().east().up());
                            this.BlockArray.add(interpPos.east().east().up().up());
                            this.BlockArray.add(interpPos.east().east().up().up().up());
                            this.BlockArray.add(interpPos.east().east().up().up().up().up());
                            this.BlockArray.add(interpPos.east().east().up().up().up().up().west());
                            this.BlockArray.add(interpPos.east().east().up().up().up().up().west().west());
                            this.BlockArray.add(interpPos.east().east().up().up().up().up().west().west().west());
                            this.BlockArray.add(interpPos.east().east().up().up().up().up().west().west().west().down());
                            this.BlockArray.add(interpPos.east().east().up().up().up().up().west().west().west().down().down());
                            this.BlockArray.add(interpPos.east().east().up().up().up().up().west().west().west().down().down().down());
                            this.BlockArray.add(interpPos.east().east().up().up().up().up().west().west().west().down().down().down().down());
                            break Label_6223;
                        }
                        case South: {
                            interpPos = new BlockPos(pos.x, pos.y, pos.z).south().south();
                            this.BlockArray.add(interpPos.east());
                            this.BlockArray.add(interpPos.east().east());
                            this.BlockArray.add(interpPos);
                            this.BlockArray.add(interpPos.east().east().up());
                            this.BlockArray.add(interpPos.east().east().up().up());
                            this.BlockArray.add(interpPos.east().east().up().up().up());
                            this.BlockArray.add(interpPos.east().east().up().up().up().up());
                            this.BlockArray.add(interpPos.east().east().up().up().up().up().west());
                            this.BlockArray.add(interpPos.east().east().up().up().up().up().west().west());
                            this.BlockArray.add(interpPos.east().east().up().up().up().up().west().west().west());
                            this.BlockArray.add(interpPos.east().east().up().up().up().up().west().west().west().down());
                            this.BlockArray.add(interpPos.east().east().up().up().up().up().west().west().west().down().down());
                            this.BlockArray.add(interpPos.east().east().up().up().up().up().west().west().west().down().down().down());
                            this.BlockArray.add(interpPos.east().east().up().up().up().up().west().west().west().down().down().down().down());
                            break Label_6223;
                        }
                        case West: {
                            interpPos = new BlockPos(pos.x, pos.y, pos.z).west().west();
                            this.BlockArray.add(interpPos.south());
                            this.BlockArray.add(interpPos.south().south());
                            this.BlockArray.add(interpPos);
                            this.BlockArray.add(interpPos.south().south().up());
                            this.BlockArray.add(interpPos.south().south().up().up());
                            this.BlockArray.add(interpPos.south().south().up().up().up());
                            this.BlockArray.add(interpPos.south().south().up().up().up().up());
                            this.BlockArray.add(interpPos.south().south().up().up().up().up().north());
                            this.BlockArray.add(interpPos.south().south().up().up().up().up().north().north());
                            this.BlockArray.add(interpPos.south().south().up().up().up().up().north().north().north());
                            this.BlockArray.add(interpPos.south().south().up().up().up().up().north().north().north().down());
                            this.BlockArray.add(interpPos.south().south().up().up().up().up().north().north().north().down().down());
                            this.BlockArray.add(interpPos.south().south().up().up().up().up().north().north().north().down().down().down());
                            this.BlockArray.add(interpPos.south().south().up().up().up().up().north().north().north().down().down().down().down());
                            break Label_6223;
                        }
                        default: {
                            break Label_6223;
                        }
                    }
                    break;
                }
                case Flat: {
                    for (int l_X = -3; l_X <= 3; ++l_X) {
                        for (int l_Y = -3; l_Y <= 3; ++l_Y) {
                            this.BlockArray.add(orignPos.down().add(l_X, 0, l_Y));
                        }
                    }
                    break;
                }
                case Cover: {
                    if (p_Pair == null) {
                        return;
                    }
                    for (int l_X = -3; l_X < 3; ++l_X) {
                        for (int l_Y = -3; l_Y < 3; ++l_Y) {
                            int l_Tries = 5;
                            BlockPos l_Pos = orignPos.down().add(l_X, 0, l_Y);
                            if (AutoBuilder.mc.world.getBlockState(l_Pos).getBlock() != p_Pair.getValue() && AutoBuilder.mc.world.getBlockState(l_Pos.down()).getBlock() != Blocks.AIR) {
                                if (AutoBuilder.mc.world.getBlockState(l_Pos.down()).getBlock() != p_Pair.getValue()) {
                                    while (AutoBuilder.mc.world.getBlockState(l_Pos).getBlock() != Blocks.AIR && AutoBuilder.mc.world.getBlockState(l_Pos).getBlock() != Blocks.FIRE) {
                                        if (AutoBuilder.mc.world.getBlockState(l_Pos).getBlock() == p_Pair.getValue()) {
                                            break;
                                        }
                                        l_Pos = l_Pos.up();
                                        if (--l_Tries <= 0) {
                                            break;
                                        }
                                    }
                                    this.BlockArray.add(l_Pos);
                                }
                            }
                        }
                    }
                    break;
                }
                case Tower: {
                    this.BlockArray.add(orignPos.up());
                    this.BlockArray.add(orignPos);
                    this.BlockArray.add(orignPos.down());
                    break;
                }
                case Wall: {
                    switch (PlayerUtil.GetFacing()) {
                        case East: {
                            interpPos = new BlockPos(pos.x, pos.y, pos.z).east().east();
                            for (int l_X = -3; l_X <= 3; ++l_X) {
                                for (int l_Y = -3; l_Y <= 3; ++l_Y) {
                                    this.BlockArray.add(interpPos.add(0, l_Y, l_X));
                                }
                            }
                            break Label_6223;
                        }
                        case North: {
                            interpPos = new BlockPos(pos.x, pos.y, pos.z).north().north();
                            for (int l_X = -3; l_X <= 3; ++l_X) {
                                for (int l_Y = -3; l_Y <= 3; ++l_Y) {
                                    this.BlockArray.add(interpPos.add(l_X, l_Y, 0));
                                }
                            }
                            break Label_6223;
                        }
                        case South: {
                            interpPos = new BlockPos(pos.x, pos.y, pos.z).south().south();
                            for (int l_X = -3; l_X <= 3; ++l_X) {
                                for (int l_Y = -3; l_Y <= 3; ++l_Y) {
                                    this.BlockArray.add(interpPos.add(l_X, l_Y, 0));
                                }
                            }
                            break Label_6223;
                        }
                        case West: {
                            interpPos = new BlockPos(pos.x, pos.y, pos.z).west().west();
                            for (int l_X = -3; l_X <= 3; ++l_X) {
                                for (int l_Y = -3; l_Y <= 3; ++l_Y) {
                                    this.BlockArray.add(interpPos.add(0, l_Y, l_X));
                                }
                            }
                            break Label_6223;
                        }
                        default: {
                            break Label_6223;
                        }
                    }
                    break;
                }
                case HighwayWall: {
                    switch (PlayerUtil.GetFacing()) {
                        case East: {
                            interpPos = new BlockPos(pos.x, pos.y, pos.z).east().east();
                            for (int l_X = -2; l_X <= 3; ++l_X) {
                                for (int l_Y = 0; l_Y < 3; ++l_Y) {
                                    this.BlockArray.add(interpPos.add(0, l_Y, l_X));
                                }
                            }
                            break Label_6223;
                        }
                        case North: {
                            interpPos = new BlockPos(pos.x, pos.y, pos.z).north().north();
                            for (int l_X = -2; l_X <= 3; ++l_X) {
                                for (int l_Y = 0; l_Y < 3; ++l_Y) {
                                    this.BlockArray.add(interpPos.add(l_X, l_Y, 0));
                                }
                            }
                            break Label_6223;
                        }
                        case South: {
                            interpPos = new BlockPos(pos.x, pos.y, pos.z).south().south();
                            for (int l_X = -2; l_X <= 3; ++l_X) {
                                for (int l_Y = 0; l_Y < 3; ++l_Y) {
                                    this.BlockArray.add(interpPos.add(l_X, l_Y, 0));
                                }
                            }
                            break Label_6223;
                        }
                        case West: {
                            interpPos = new BlockPos(pos.x, pos.y, pos.z).west().west();
                            for (int l_X = -2; l_X <= 3; ++l_X) {
                                for (int l_Y = 0; l_Y < 3; ++l_Y) {
                                    this.BlockArray.add(interpPos.add(0, l_Y, l_X));
                                }
                            }
                            break Label_6223;
                        }
                        default: {
                            break Label_6223;
                        }
                    }
                    break;
                }
                case Stair: {
                    interpPos = orignPos.down();
                    switch (PlayerUtil.GetFacing()) {
                        case East: {
                            this.BlockArray.add(interpPos.east());
                            this.BlockArray.add(interpPos.east().up());
                            break Label_6223;
                        }
                        case North: {
                            this.BlockArray.add(interpPos.north());
                            this.BlockArray.add(interpPos.north().up());
                            break Label_6223;
                        }
                        case South: {
                            this.BlockArray.add(interpPos.south());
                            this.BlockArray.add(interpPos.south().up());
                            break Label_6223;
                        }
                        case West: {
                            this.BlockArray.add(interpPos.west());
                            this.BlockArray.add(interpPos.west().up());
                            break Label_6223;
                        }
                        default: {
                            break Label_6223;
                        }
                    }
                    break;
                }
            }
        }
    }
    
    private Pair<Integer, Block> findStackHotbar() {
        if (AutoBuilder.mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock) {
            return new Pair<Integer, Block>(AutoBuilder.mc.player.inventory.currentItem, ((ItemBlock)AutoBuilder.mc.player.getHeldItemMainhand().getItem()).getBlock());
        }
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = Minecraft.getMinecraft().player.inventory.getStackInSlot(i);
            if (stack.getItem() instanceof ItemBlock) {
                final ItemBlock block = (ItemBlock)stack.getItem();
                return new Pair<Integer, Block>(i, block.getBlock());
            }
        }
        return null;
    }
    
    public Vec3d GetCenter(final double posX, final double posY, final double posZ) {
        final double x = Math.floor(posX) + 0.5;
        final double y = Math.floor(posY);
        final double z = Math.floor(posZ) + 0.5;
        return new Vec3d(x, y, z);
    }
    
    private boolean VerifyPortalFrame(final ArrayList<BlockPos> p_Blocks) {
        for (final BlockPos l_Pos : p_Blocks) {
            final IBlockState l_State = AutoBuilder.mc.world.getBlockState(l_Pos);
            if (l_State == null || !(l_State.getBlock() instanceof BlockObsidian)) {
                return false;
            }
        }
        return true;
    }
    
    public enum Modes
    {
        Highway, 
        Swastika, 
        HighwayTunnel, 
        Portal, 
        Flat, 
        Tower, 
        Cover, 
        Wall, 
        HighwayWall, 
        Stair;
    }
    
    public enum BuildingModes
    {
        Dynamic, 
        Static;
    }
    
    public static class Pair<T, S>
    {
        T key;
        S value;
        
        public Pair(final T key, final S value) {
            this.key = key;
            this.value = value;
        }
        
        public T getKey() {
            return this.key;
        }
        
        public S getValue() {
            return this.value;
        }
        
        public void setKey(final T key) {
            this.key = key;
        }
        
        public void setValue(final S value) {
            this.value = value;
        }
    }
}
