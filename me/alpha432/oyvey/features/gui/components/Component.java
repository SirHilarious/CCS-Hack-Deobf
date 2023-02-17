//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.features.gui.components;

import me.alpha432.oyvey.features.*;
import me.alpha432.oyvey.features.gui.components.items.*;
import me.alpha432.oyvey.features.modules.client.*;
import net.minecraft.client.gui.*;
import me.alpha432.oyvey.util.*;
import me.alpha432.oyvey.*;
import me.alpha432.oyvey.features.gui.*;
import java.util.*;
import net.minecraft.init.*;
import net.minecraft.client.audio.*;
import me.alpha432.oyvey.features.gui.components.items.buttons.*;

public class Component extends Feature
{
    public static int[] counter1;
    private final ArrayList<Item> items;
    public boolean drag;
    private int x;
    private int y;
    private int x2;
    private int y2;
    private int width;
    private int height;
    private boolean open;
    private boolean hidden;
    
    public Component(final String name, final int x, final int y, final boolean open) {
        super(name);
        this.items = new ArrayList<Item>();
        this.hidden = false;
        this.x = x;
        this.y = y;
        this.width = 100;
        this.height = 18;
        this.open = open;
        this.setupItems();
    }
    
    public void setupItems() {
    }
    
    private void drag(final int mouseX, final int mouseY) {
        if (!this.drag) {
            return;
        }
        this.x = this.x2 + mouseX;
        this.y = this.y2 + mouseY;
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drag(mouseX, mouseY);
        Component.counter1 = new int[] { 1 };
        final float totalItemHeight = this.open ? (this.getTotalItemHeight() - 2.0f) : 0.0f;
        final int color = ColorUtil.toARGB(ClickGui.getInstance().topRed.getValue(), ClickGui.getInstance().topGreen.getValue(), ClickGui.getInstance().topBlue.getValue(), 255);
        Gui.drawRect(this.x, this.y - 1, this.x + this.width, this.y + this.height - 6, ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB() : color);
        if (this.open) {
            RenderUtil.drawLine((float)(this.x + this.width - 25), this.y + 8.0f, (float)(this.x + this.width - 15), this.y + 4.0f, 2.5f, -16777216);
            RenderUtil.drawLine((float)(this.x + this.width - 15), this.y + 4.0f, (float)(this.x + this.width - 5), this.y + 8.0f, 2.5f, -16777216);
            RenderUtil.drawRect((float)this.x, this.y + 12.5f, (float)(this.x + this.width), this.y + this.height + 1 + totalItemHeight, -2013265920);
        }
        else {
            RenderUtil.drawLine((float)(this.x + this.width - 25), this.y + 4.0f, (float)(this.x + this.width - 15), this.y + 8.0f, 2.5f, -16777216);
            RenderUtil.drawLine((float)(this.x + this.width - 15), this.y + 8.0f, (float)(this.x + this.width - 5), this.y + 4.0f, 2.5f, -16777216);
        }
        OyVey.textManager.drawStringWithShadow(this.getName(), this.x + 3.0f, this.y - 4.0f - OyVeyGui.getClickGui().getTextOffset(), -1);
        if (this.open) {
            float y = this.getY() + this.getHeight() - 3.0f;
            for (final Item item : this.getItems()) {
                ++Component.counter1[0];
                if (item.isHidden()) {
                    continue;
                }
                item.setLocation(this.x + 2.0f, y);
                item.setWidth(this.getWidth() - 4);
                item.drawScreen(mouseX, mouseY, partialTicks);
                y += item.getHeight() + 1.5f;
            }
        }
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
            this.x2 = this.x - mouseX;
            this.y2 = this.y - mouseY;
            OyVeyGui.getClickGui().getComponents().forEach(component -> {
                if (component.drag) {
                    component.drag = false;
                }
                return;
            });
            this.drag = true;
            return;
        }
        if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
            this.open = !this.open;
            Component.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            return;
        }
        if (!this.open) {
            return;
        }
        this.getItems().forEach(item -> item.mouseClicked(mouseX, mouseY, mouseButton));
    }
    
    public void mouseReleased(final int mouseX, final int mouseY, final int releaseButton) {
        if (releaseButton == 0) {
            this.drag = false;
        }
        if (!this.open) {
            return;
        }
        this.getItems().forEach(item -> item.mouseReleased(mouseX, mouseY, releaseButton));
    }
    
    public void onKeyTyped(final char typedChar, final int keyCode) {
        if (!this.open) {
            return;
        }
        this.getItems().forEach(item -> item.onKeyTyped(typedChar, keyCode));
    }
    
    public void addButton(final Button button) {
        this.items.add(button);
    }
    
    public int getX() {
        return this.x;
    }
    
    public void setX(final int x) {
        this.x = x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public void setY(final int y) {
        this.y = y;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public void setWidth(final int width) {
        this.width = width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public void setHeight(final int height) {
        this.height = height;
    }
    
    public boolean isHidden() {
        return this.hidden;
    }
    
    public void setHidden(final boolean hidden) {
        this.hidden = hidden;
    }
    
    public boolean isOpen() {
        return this.open;
    }
    
    public final ArrayList<Item> getItems() {
        return this.items;
    }
    
    private boolean isHovering(final int mouseX, final int mouseY) {
        return mouseX >= this.getX() && mouseX <= this.getX() + this.getWidth() && mouseY >= this.getY() && mouseY <= this.getY() + this.getHeight() - (this.open ? 2 : 0);
    }
    
    private float getTotalItemHeight() {
        float height = 0.0f;
        for (final Item item : this.getItems()) {
            height += item.getHeight() + 1.5f;
        }
        return height;
    }
    
    static {
        Component.counter1 = new int[] { 1 };
    }
}
