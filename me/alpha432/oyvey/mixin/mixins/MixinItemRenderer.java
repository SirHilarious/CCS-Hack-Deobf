//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.mixin.mixins;

import net.minecraft.client.*;
import net.minecraft.item.*;
import net.minecraft.client.entity.*;
import net.minecraft.client.renderer.texture.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import org.spongepowered.asm.mixin.injection.*;
import com.google.common.base.*;
import net.minecraft.init.*;
import net.minecraft.client.renderer.*;
import org.spongepowered.asm.mixin.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import org.lwjgl.opengl.*;
import me.alpha432.oyvey.features.modules.render.*;
import me.alpha432.oyvey.features.modules.client.*;
import me.alpha432.oyvey.util.*;

@Mixin({ ItemRenderer.class })
public abstract class MixinItemRenderer
{
    @Shadow
    @Final
    public Minecraft mc;
    private boolean injection;
    @Shadow
    private float equippedProgressMainHand;
    @Shadow
    private float prevEquippedProgressMainHand;
    @Shadow
    private float equippedProgressOffHand;
    @Shadow
    private float prevEquippedProgressOffHand;
    @Shadow
    private ItemStack itemStackMainHand;
    @Shadow
    private ItemStack itemStackOffHand;
    
    public MixinItemRenderer() {
        this.injection = true;
        this.itemStackMainHand = ItemStack.EMPTY;
        this.itemStackOffHand = ItemStack.EMPTY;
    }
    
    @Shadow
    public abstract void renderItemInFirstPerson(final AbstractClientPlayer p0, final float p1, final float p2, final EnumHand p3, final float p4, final ItemStack p5, final float p6);
    
    @Inject(method = { "renderSuffocationOverlay" }, at = { @At("HEAD") }, cancellable = true)
    private void renderSuffocationOverlay(final TextureAtlasSprite sprite, final CallbackInfo ci) {
        ci.cancel();
    }
    
    @Overwrite
    public void renderItemInFirstPerson(final float partialTicks) {
        final AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer)this.mc.player;
        float f;
        if (ViewModel.getInstance().isOn() && (boolean)ViewModel.getInstance().doSwing.getValue()) {
            f = (float)ViewModel.getInstance().swing.getValue();
        }
        else {
            f = abstractclientplayer.getSwingProgress(partialTicks);
        }
        final EnumHand enumhand = (EnumHand)MoreObjects.firstNonNull((Object)abstractclientplayer.swingingHand, (Object)EnumHand.MAIN_HAND);
        final float f2 = abstractclientplayer.prevRotationPitch + (abstractclientplayer.rotationPitch - abstractclientplayer.prevRotationPitch) * partialTicks;
        final float f3 = abstractclientplayer.prevRotationYaw + (abstractclientplayer.rotationYaw - abstractclientplayer.prevRotationYaw) * partialTicks;
        boolean flag = true;
        boolean flag2 = true;
        if (abstractclientplayer.isHandActive()) {
            final ItemStack itemstack = abstractclientplayer.getActiveItemStack();
            if (itemstack.getItem() == Items.BOW) {
                final EnumHand enumhand2 = abstractclientplayer.getActiveHand();
                flag = (enumhand2 == EnumHand.MAIN_HAND);
                flag2 = !flag;
            }
        }
        this.mc.itemRenderer.rotateArroundXAndY(f2, f3);
        this.mc.itemRenderer.setLightmap();
        this.mc.itemRenderer.rotateArm(partialTicks);
        GlStateManager.enableRescaleNormal();
        if (ViewModel.getInstance().isOn()) {
            GlStateManager.scale((float)ViewModel.getInstance().sizex.getValue(), (float)ViewModel.getInstance().sizey.getValue(), (float)ViewModel.getInstance().sizez.getValue());
            GlStateManager.rotate((float)ViewModel.getInstance().x.getValue() * 360.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate((float)ViewModel.getInstance().y.getValue() * 360.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate((float)ViewModel.getInstance().z.getValue() * 360.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.translate((float)ViewModel.getInstance().posX.getValue(), (float)ViewModel.getInstance().posY.getValue(), (float)ViewModel.getInstance().posZ.getValue());
        }
        if (flag) {
            final float f4 = (enumhand == EnumHand.MAIN_HAND) ? f : 0.0f;
            final float f5 = 1.0f - (this.prevEquippedProgressMainHand + (this.equippedProgressMainHand - this.prevEquippedProgressMainHand) * partialTicks);
            this.renderItemInFirstPerson(abstractclientplayer, partialTicks, f2, EnumHand.MAIN_HAND, f4, this.itemStackMainHand, f5);
        }
        if (flag2) {
            final float f6 = (enumhand == EnumHand.OFF_HAND) ? f : 0.0f;
            final float f7 = 1.0f - (this.prevEquippedProgressOffHand + (this.equippedProgressOffHand - this.prevEquippedProgressOffHand) * partialTicks);
            this.renderItemInFirstPerson(abstractclientplayer, partialTicks, f2, EnumHand.OFF_HAND, f6, this.itemStackOffHand, f7);
        }
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
    }
    
    @Inject(method = { "transformSideFirstPerson(Lnet/minecraft/util/EnumHandSide;F)V" }, at = { @At("HEAD") })
    private void transformSideFirstPerson(final EnumHandSide hand, final float p_187459_2_, final CallbackInfo ci) {
        if (this.mc != null && this.mc.player != null) {
            final int i = (hand == EnumHandSide.RIGHT) ? 1 : -1;
            if (Animations.getInstance().isOn() && hand == EnumHandSide.RIGHT && this.mc.player.isSwingInProgress) {
                final float swingprogress = this.mc.player.swingProgress;
                final float var15 = MathHelper.sin(swingprogress * swingprogress * 3.1415927f);
                GlStateManager.rotate(-var15 * 55.0f / 2.0f, -8.0f, -0.0f, 9.0f);
                GlStateManager.rotate(-var15 * 45.0f, 1.0f, var15 / 2.0f, -0.0f);
                GL11.glTranslated(1.2, 0.3, 0.5);
                GL11.glTranslatef(-1.0f, this.mc.player.isSneaking() ? -0.1f : -0.2f, 0.2f);
            }
        }
    }
    
    @Inject(method = { "renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V" }, at = { @At("HEAD") }, cancellable = true)
    public void renderItemInFirstPersonHook(final AbstractClientPlayer player, final float p_187457_2_, final float p_187457_3_, final EnumHand hand, final float p_187457_5_, final ItemStack stack, final float p_187457_7_, final CallbackInfo info) {
        if (this.injection) {
            info.cancel();
            final SmallShield offset = SmallShield.getINSTANCE();
            final float xOffset = 0.0f;
            final float yOffset = 0.0f;
            this.injection = false;
            if (HandChams.getINSTANCE().isOn() && hand == EnumHand.MAIN_HAND && stack.isEmpty()) {
                if (((HandChams.RenderMode)HandChams.getINSTANCE().mode.getValue()).equals((Object)HandChams.RenderMode.WIREFRAME)) {
                    this.renderItemInFirstPerson(player, p_187457_2_, p_187457_3_, hand, p_187457_5_ + xOffset, stack, p_187457_7_ + yOffset);
                }
                GlStateManager.pushMatrix();
                if (((HandChams.RenderMode)HandChams.getINSTANCE().mode.getValue()).equals((Object)HandChams.RenderMode.WIREFRAME)) {
                    GL11.glPushAttrib(1048575);
                }
                else {
                    GlStateManager.pushAttrib();
                }
                if (((HandChams.RenderMode)HandChams.getINSTANCE().mode.getValue()).equals((Object)HandChams.RenderMode.WIREFRAME)) {
                    GL11.glPolygonMode(1032, 6913);
                }
                GL11.glDisable(3553);
                GL11.glDisable(2896);
                if (((HandChams.RenderMode)HandChams.getINSTANCE().mode.getValue()).equals((Object)HandChams.RenderMode.WIREFRAME)) {
                    GL11.glEnable(2848);
                    GL11.glEnable(3042);
                }
                GL11.glColor4f(((boolean)ClickGui.getInstance().rainbow.getValue()) ? (ColorUtil.rainbow((int)ClickGui.getInstance().rainbowHue.getValue()).getRed() / 255.0f) : ((int)HandChams.getINSTANCE().red.getValue() / 255.0f), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? (ColorUtil.rainbow((int)ClickGui.getInstance().rainbowHue.getValue()).getGreen() / 255.0f) : ((int)HandChams.getINSTANCE().green.getValue() / 255.0f), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? (ColorUtil.rainbow((int)ClickGui.getInstance().rainbowHue.getValue()).getBlue() / 255.0f) : ((int)HandChams.getINSTANCE().blue.getValue() / 255.0f), (int)HandChams.getINSTANCE().alpha.getValue() / 255.0f);
                this.renderItemInFirstPerson(player, p_187457_2_, p_187457_3_, hand, p_187457_5_ + xOffset, stack, p_187457_7_ + yOffset);
                GlStateManager.popAttrib();
                GlStateManager.popMatrix();
            }
            if (SmallShield.getINSTANCE().isOn() && (!stack.isEmpty || HandChams.getINSTANCE().isOff())) {
                this.renderItemInFirstPerson(player, p_187457_2_, p_187457_3_, hand, p_187457_5_ + xOffset, stack, p_187457_7_ + yOffset);
            }
            else if (!stack.isEmpty || HandChams.getINSTANCE().isOff()) {
                this.renderItemInFirstPerson(player, p_187457_2_, p_187457_3_, hand, p_187457_5_, stack, p_187457_7_);
            }
            this.injection = true;
        }
    }
}
