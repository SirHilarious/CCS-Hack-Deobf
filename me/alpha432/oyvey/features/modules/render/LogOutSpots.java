//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.*;
import net.minecraft.entity.player.*;
import me.alpha432.oyvey.features.setting.*;
import com.google.common.collect.*;
import java.util.*;
import me.alpha432.oyvey.util.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.*;
import net.minecraft.entity.*;
import me.alpha432.oyvey.*;
import java.awt.*;
import me.alpha432.oyvey.features.modules.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.network.*;
import java.util.function.*;
import java.net.*;
import java.nio.charset.*;
import org.apache.commons.io.*;
import org.json.simple.*;
import me.alpha432.oyvey.features.command.*;
import java.io.*;
import org.json.simple.parser.*;
import me.alpha432.oyvey.event.events.*;
import net.minecraft.network.play.server.*;
import net.minecraft.util.math.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import net.minecraftforge.fml.common.eventhandler.*;
import com.mojang.authlib.*;

public class LogOutSpots extends Module
{
    private final Map<String, EntityPlayer> playerCache;
    private final Map<String, PlayerData> logoutCache;
    private final Map<String, String> uuidNameCache;
    private Map<Integer, Boolean> glCapMap;
    private Setting<Float> scale;
    private Setting<Float> height;
    private Setting<Integer> removeDistance;
    
    public LogOutSpots() {
        super("LogOutSpots", "Shows where a player logged out", Module.Category.RENDER, true, false, false);
        this.playerCache = (Map<String, EntityPlayer>)Maps.newConcurrentMap();
        this.logoutCache = (Map<String, PlayerData>)Maps.newConcurrentMap();
        this.uuidNameCache = (Map<String, String>)Maps.newConcurrentMap();
        this.glCapMap = new HashMap<Integer, Boolean>();
        this.scale = (Setting<Float>)this.register(new Setting("Scale", (T)5.0f, (T)1.0f, (T)9.0f));
        this.height = (Setting<Float>)this.register(new Setting("Height", (T)2.5f, (T)0.5f, (T)5.0f));
        this.removeDistance = (Setting<Integer>)this.register(new Setting("RemoveDist", (T)200, (T)10, (T)2000));
        this.glCapMap = new HashMap<Integer, Boolean>();
    }
    
    public void unload() {
        this.uuidNameCache.clear();
    }
    
    public void onToggle() {
        super.onToggle();
        this.glCapMap = new HashMap<Integer, Boolean>();
        this.playerCache.clear();
        this.logoutCache.clear();
    }
    
    public void onUpdate() {
        if (LogOutSpots.mc.player == null) {
            return;
        }
        for (final EntityPlayer player : LogOutSpots.mc.world.playerEntities) {
            if (player != null) {
                if (player.equals((Object)LogOutSpots.mc.player)) {
                    continue;
                }
                this.updatePlayerCache(player.getGameProfile().getId().toString(), player);
            }
        }
    }
    
    public void onRender3D(final Render3DEvent event) {
        for (final String uuid : this.logoutCache.keySet()) {
            final PlayerData data = this.logoutCache.get(uuid);
            if (this.isOutOfRange(data)) {
                this.logoutCache.remove(uuid);
            }
            else {
                data.ghost.prevLimbSwingAmount = 0.0f;
                data.ghost.limbSwing = 0.0f;
                data.ghost.limbSwingAmount = 0.0f;
                data.ghost.hurtTime = 0;
                final Vec3d interp = EntityUtil.getInterpolatedRenderPos((Entity)data.ghost, LogOutSpots.mc.getRenderPartialTicks());
                final AxisAlignedBB bb = new AxisAlignedBB(data.ghost.getEntityBoundingBox().minX - 0.05 - data.ghost.posX + interp.x, data.ghost.getEntityBoundingBox().minY - 0.0 - data.ghost.posY + interp.y, data.ghost.getEntityBoundingBox().minZ - 0.05 - data.ghost.posZ + interp.z, data.ghost.getEntityBoundingBox().maxX + 0.05 - data.ghost.posX + interp.x, data.ghost.getEntityBoundingBox().maxY + 0.1 - data.ghost.posY + interp.y, data.ghost.getEntityBoundingBox().maxZ + 0.05 - data.ghost.posZ + interp.z);
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.disableDepth();
                GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
                GlStateManager.disableTexture2D();
                GlStateManager.depthMask(false);
                GL11.glEnable(2848);
                GL11.glHint(3154, 4354);
                GL11.glLineWidth(1.0f);
                RenderGlobal.renderFilledBox(bb, 0.16470589f, 0.16470589f, 0.16470589f, 0.16470589f);
                GL11.glDisable(2848);
                GlStateManager.depthMask(true);
                GlStateManager.enableDepth();
                GlStateManager.enableTexture2D();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
                GlStateManager.pushMatrix();
                final double pX = data.position.x + (data.position.x - data.position.x) * LogOutSpots.mc.timer.renderPartialTicks - LogOutSpots.mc.renderManager.renderPosX;
                final double pY = data.position.y + (data.position.y - data.position.y) * LogOutSpots.mc.timer.renderPartialTicks - LogOutSpots.mc.renderManager.renderPosY;
                final double pZ = data.position.z + (data.position.z - data.position.z) * LogOutSpots.mc.timer.renderPartialTicks - LogOutSpots.mc.renderManager.renderPosZ;
                this.renderNametag(data.ghost, pX, pY, pZ);
                GlStateManager.popMatrix();
            }
        }
    }
    
    public float getNametagSize(final EntityLivingBase player) {
        final ScaledResolution scaledRes = new ScaledResolution(LogOutSpots.mc);
        final double twoDscale = scaledRes.getScaleFactor() / Math.pow(scaledRes.getScaleFactor(), 2.0);
        return (float)twoDscale + LogOutSpots.mc.player.getDistance((Entity)player) / 7.0f;
    }
    
    public void renderNametag(final EntityPlayer player, final double x, final double y, final double z) {
        final int l4 = 0;
        GlStateManager.pushMatrix();
        final FontRenderer var13 = LogOutSpots.mc.fontRenderer;
        final NetworkPlayerInfo npi = LogOutSpots.mc.player.connection.getPlayerInfo(player.getGameProfile().getId());
        final String name = player.getName().replace(".0", "");
        final float distance = LogOutSpots.mc.player.getDistance((Entity)player);
        float var14 = ((distance / 5.0f <= 2.0f) ? 2.0f : (distance / 5.0f * (this.scale.getValue() / 100.0f * 10.0f + 1.0f))) * 2.5f * (this.scale.getValue() / 100.0f / 10.0f);
        if (distance <= 8.0) {
            var14 = 0.0245f;
        }
        final float var15 = this.scale.getValue() / 100.0f * this.getNametagSize((EntityLivingBase)player);
        GL11.glTranslated((double)(float)x, (float)y + this.height.getValue() - (player.isSneaking() ? 0.4 : 0.0) + ((distance / 5.0f > 2.0f) ? (distance / 12.0f - 0.7) : 0.0), (double)(float)z);
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-LogOutSpots.mc.renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(LogOutSpots.mc.renderManager.playerViewX, (LogOutSpots.mc.gameSettings.thirdPersonView == 2) ? -1.0f : 1.0f, 0.0f, 0.0f);
        GL11.glScalef(-var14, -var14, var14);
        this.disableGlCap(2896, 2929);
        this.enableGlCap(3042);
        GL11.glBlendFunc(770, 771);
        final int width = OyVey.textManager.getStringWidth(name) / 2 + 1;
        final int color = new Color(45, 46, 46).getRGB();
        Gui.drawRect(-width - 2, 7, width + 1, 19, changeAlpha(color, 120));
        if (FontMod.getInstance().isOn()) {
            OyVey.textManager.drawStringWithShadow(name, (float)(-width), 8.2f, -1);
        }
        else {
            LogOutSpots.mc.fontRenderer.drawStringWithShadow(name, (float)(-width), 9.2f, -1);
        }
        this.resetCaps();
        GlStateManager.resetColor();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }
    
    public void enableGlCap(final int cap) {
        this.setGlCap(cap, true);
    }
    
    public void enableGlCap(final int... caps) {
        for (final int cap : caps) {
            this.setGlCap(cap, true);
        }
    }
    
    public void disableGlCap(final int cap) {
        this.setGlCap(cap, false);
    }
    
    public void disableGlCap(final int... caps) {
        for (final int cap : caps) {
            this.setGlCap(cap, false);
        }
    }
    
    public void setGlCap(final int cap, final boolean state) {
        this.glCapMap.put(cap, GL11.glGetBoolean(cap));
        this.setGlState(cap, state);
    }
    
    public void setGlState(final int cap, final boolean state) {
        if (state) {
            GL11.glEnable(cap);
        }
        else {
            GL11.glDisable(cap);
        }
    }
    
    public void resetCaps() {
        this.glCapMap.forEach(this::setGlState);
    }
    
    public static final int changeAlpha(int origColor, final int userInputedAlpha) {
        origColor &= 0xFFFFFF;
        return userInputedAlpha << 24 | origColor;
    }
    
    public static void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }
    
    public static void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }
    
    public String resolveName(String uuid) {
        uuid = uuid.replace("-", "");
        if (this.uuidNameCache.containsKey(uuid)) {
            return this.uuidNameCache.get(uuid);
        }
        final String url = "https://api.mojang.com/user/profiles/" + uuid + "/names";
        try {
            final String nameJson = IOUtils.toString(new URL(url), StandardCharsets.UTF_8);
            if (nameJson != null && nameJson.length() > 0) {
                final JSONArray jsonArray = (JSONArray)JSONValue.parseWithException(nameJson);
                if (jsonArray != null) {
                    final JSONObject latestName = (JSONObject)jsonArray.get(jsonArray.size() - 1);
                    if (latestName != null) {
                        return latestName.get((Object)"name").toString();
                    }
                }
            }
        }
        catch (IOException | ParseException ex2) {
            final Exception ex;
            final Exception e = ex;
            Command.sendMessage("Failed to resolve " + url);
            e.printStackTrace();
        }
        return null;
    }
    
    @SubscribeEvent
    public void onPacketRecieved(final PacketEvent.Receive event) {
        if (event.getStage() == 0 && event.getPacket() instanceof SPacketPlayerListItem) {
            final SPacketPlayerListItem packet = (SPacketPlayerListItem)event.getPacket();
            if (LogOutSpots.mc.player != null && LogOutSpots.mc.player.ticksExisted >= 100) {
                if (packet.getAction() == SPacketPlayerListItem.Action.ADD_PLAYER) {
                    for (final SPacketPlayerListItem.AddPlayerData playerData : packet.getEntries()) {
                        if (playerData.getProfile().getId() != LogOutSpots.mc.session.getProfile().getId()) {
                            final SPacketPlayerListItem.AddPlayerData addPlayerData;
                            final String name;
                            final Iterator<String> iterator2;
                            String uuid;
                            new Thread(() -> {
                                name = this.resolveName(addPlayerData.getProfile().getId().toString());
                                if (name != null) {
                                    this.logoutCache.keySet().iterator();
                                    while (iterator2.hasNext()) {
                                        uuid = iterator2.next();
                                        if (!uuid.equals(addPlayerData.getProfile().getId().toString())) {
                                            continue;
                                        }
                                        else {
                                            this.logoutCache.remove(uuid);
                                            Command.sendMessage(name + " Just Logged Back In");
                                            LogOutSpots.mc.world.playSound((EntityPlayer)LogOutSpots.mc.player, new BlockPos(LogOutSpots.mc.player.posX, LogOutSpots.mc.player.posY, LogOutSpots.mc.player.posZ), SoundEvents.BLOCK_NOTE_CHIME, SoundCategory.BLOCKS, 1.0f, 0.5f);
                                        }
                                    }
                                    this.playerCache.clear();
                                }
                                return;
                            }).start();
                        }
                    }
                }
                if (packet.getAction() == SPacketPlayerListItem.Action.REMOVE_PLAYER) {
                    for (final SPacketPlayerListItem.AddPlayerData playerData : packet.getEntries()) {
                        if (playerData.getProfile().getId() != LogOutSpots.mc.session.getProfile().getId()) {
                            final SPacketPlayerListItem.AddPlayerData addPlayerData2;
                            final String name2;
                            final Iterator<String> iterator4;
                            String uuid2;
                            EntityPlayer player;
                            PlayerData data;
                            new Thread(() -> {
                                name2 = this.resolveName(addPlayerData2.getProfile().getId().toString());
                                if (name2 != null) {
                                    this.playerCache.keySet().iterator();
                                    while (iterator4.hasNext()) {
                                        uuid2 = iterator4.next();
                                        if (!uuid2.equals(addPlayerData2.getProfile().getId().toString())) {
                                            continue;
                                        }
                                        else {
                                            player = this.playerCache.get(uuid2);
                                            data = new PlayerData(player.getPositionVector(), player.getGameProfile(), player);
                                            if (!this.hasPlayerLogged(uuid2)) {
                                                this.logoutCache.put(uuid2, data);
                                            }
                                            else {
                                                continue;
                                            }
                                        }
                                    }
                                    this.playerCache.clear();
                                }
                            }).start();
                        }
                    }
                }
            }
        }
    }
    
    private void cleanLogoutCache(final String uuid) {
        this.logoutCache.remove(uuid);
    }
    
    private void updatePlayerCache(final String uuid, final EntityPlayer player) {
        this.playerCache.put(uuid, player);
    }
    
    private boolean hasPlayerLogged(final String uuid) {
        return this.logoutCache.containsKey(uuid);
    }
    
    private boolean isOutOfRange(final PlayerData data) {
        final Vec3d position = data.position;
        return LogOutSpots.mc.player.getDistance(position.x, position.y, position.z) > this.removeDistance.getValue();
    }
    
    public Map<String, EntityPlayer> getPlayerCache() {
        return this.playerCache;
    }
    
    public Map<String, PlayerData> getLogoutCache() {
        return this.logoutCache;
    }
    
    private class PlayerData
    {
        Vec3d position;
        GameProfile profile;
        EntityPlayer ghost;
        
        public PlayerData(final Vec3d position, final GameProfile profile, final EntityPlayer ghost) {
            this.position = position;
            this.profile = profile;
            this.ghost = ghost;
        }
    }
}
