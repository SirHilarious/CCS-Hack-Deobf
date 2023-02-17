//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.features.setting.*;
import net.minecraft.client.renderer.culling.*;
import net.minecraft.entity.player.*;
import me.alpha432.oyvey.*;
import java.util.stream.*;
import net.minecraft.entity.*;
import net.minecraft.init.*;
import java.util.*;
import me.alpha432.oyvey.event.events.*;
import net.minecraft.util.math.*;
import net.minecraft.client.renderer.*;
import org.lwjgl.opengl.*;
import java.awt.*;
import me.alpha432.oyvey.util.*;
import net.minecraft.block.*;

public class CityESP extends Module
{
    public Setting<Boolean> self;
    private final Setting<Integer> red;
    private final Setting<Integer> green;
    private final Setting<Integer> blue;
    private final Setting<Integer> alpha;
    private ICamera camera;
    ArrayList<BlockPos> positions;
    private static final BlockPos[] surroundOffset;
    
    public CityESP() {
        super("CityESP", "Shows spots that can be city'd", Module.Category.RENDER, true, false, false);
        this.self = (Setting<Boolean>)this.register(new Setting("Self", (T)false));
        this.red = (Setting<Integer>)this.register(new Setting("Red", (T)255, (T)0, (T)255));
        this.green = (Setting<Integer>)this.register(new Setting("Green", (T)0, (T)0, (T)255));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", (T)0, (T)0, (T)255));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", (T)255, (T)0, (T)255));
        this.camera = (ICamera)new Frustum();
        this.positions = new ArrayList<BlockPos>();
    }
    
    public ArrayList<Pair<EntityPlayer, ArrayList<BlockPos>>> GetPlayersReadyToBeCitied() {
        final ArrayList<Pair<EntityPlayer, ArrayList<BlockPos>>> players = new ArrayList<Pair<EntityPlayer, ArrayList<BlockPos>>>();
        for (final Entity entity : (List)CityESP.mc.world.playerEntities.stream().filter(entityPlayer -> !OyVey.friendManager.isFriend(entityPlayer.getName())).collect(Collectors.toList())) {
            if (!this.self.getValue() && entity == CityESP.mc.player) {
                continue;
            }
            if (EntityUtil.isBurrow(entity)) {
                continue;
            }
            this.positions = new ArrayList<BlockPos>();
            for (int i = 0; i < 4; ++i) {
                final BlockPos o = EntityUtil.GetPositionVectorBlockPos(entity, CityESP.surroundOffset[i]);
                if (CityESP.mc.world.getBlockState(o).getBlock() == Blocks.OBSIDIAN || CityESP.mc.world.getBlockState(o).getBlock() == Blocks.ENDER_CHEST) {
                    boolean passCheck = false;
                    switch (i) {
                        case 0: {
                            passCheck = canPlaceCrystal(o.north(1).down());
                            break;
                        }
                        case 1: {
                            passCheck = canPlaceCrystal(o.east(1).down());
                            break;
                        }
                        case 2: {
                            passCheck = canPlaceCrystal(o.south(1).down());
                            break;
                        }
                        case 3: {
                            passCheck = canPlaceCrystal(o.west(1).down());
                            break;
                        }
                    }
                    if (passCheck) {
                        this.positions.add(o);
                    }
                }
            }
            if (this.positions.isEmpty()) {
                continue;
            }
            players.add(new Pair<EntityPlayer, ArrayList<BlockPos>>((EntityPlayer)entity, this.positions));
        }
        return players;
    }
    
    public void onRender3D(final Render3DEvent event) {
        if (CityESP.mc.getRenderManager() == null || CityESP.mc.getRenderManager().options == null) {
            return;
        }
        final AxisAlignedBB bb;
        this.GetPlayersReadyToBeCitied().forEach(pair -> pair.getValue().forEach(o -> {
            bb = new AxisAlignedBB(o.getX() - CityESP.mc.getRenderManager().viewerPosX, o.getY() - CityESP.mc.getRenderManager().viewerPosY, o.getZ() - CityESP.mc.getRenderManager().viewerPosZ, o.getX() + 1 - CityESP.mc.getRenderManager().viewerPosX, o.getY() + 1 - CityESP.mc.getRenderManager().viewerPosY, o.getZ() + 1 - CityESP.mc.getRenderManager().viewerPosZ);
            this.camera.setPosition(CityESP.mc.getRenderViewEntity().posX, CityESP.mc.getRenderViewEntity().posY, CityESP.mc.getRenderViewEntity().posZ);
            if (this.camera.isBoundingBoxInFrustum(new AxisAlignedBB(bb.minX + CityESP.mc.getRenderManager().viewerPosX, bb.minY + CityESP.mc.getRenderManager().viewerPosY, bb.minZ + CityESP.mc.getRenderManager().viewerPosZ, bb.maxX + CityESP.mc.getRenderManager().viewerPosX, bb.maxY + CityESP.mc.getRenderManager().viewerPosY, bb.maxZ + CityESP.mc.getRenderManager().viewerPosZ))) {
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.disableDepth();
                GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
                GlStateManager.disableTexture2D();
                GlStateManager.depthMask((boolean)(0 != 0));
                GL11.glEnable(2848);
                GL11.glHint(3154, 4354);
                GL11.glLineWidth(1.5f);
                this.drawBox(o, this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue());
                GL11.glDisable(2848);
                GlStateManager.depthMask((boolean)(1 != 0));
                GlStateManager.enableDepth();
                GlStateManager.enableTexture2D();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        }));
    }
    
    private void drawBox(final BlockPos blockPos, final int r, final int g, final int b, final int a) {
        final Color color = new Color(r, g, b, a);
        RenderUtil.drawBoxESP(blockPos, color, false, color, 1.2f, true, true, 120, false);
    }
    
    public static boolean canPlaceCrystal(final BlockPos pos) {
        final Block block = CityESP.mc.world.getBlockState(pos).getBlock();
        if (block == Blocks.OBSIDIAN || block == Blocks.BEDROCK) {
            final Block floor = CityESP.mc.world.getBlockState(pos.add(0, 1, 0)).getBlock();
            final Block ceil = CityESP.mc.world.getBlockState(pos.add(0, 2, 0)).getBlock();
            if (floor == Blocks.AIR && ceil == Blocks.AIR && CityESP.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(pos.add(0, 1, 0))).isEmpty()) {
                return true;
            }
        }
        return false;
    }
    
    static {
        surroundOffset = new BlockPos[] { new BlockPos(0, 0, -1), new BlockPos(1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(-1, 0, 0) };
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
