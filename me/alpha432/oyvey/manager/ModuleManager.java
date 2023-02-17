//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey.manager;

import me.alpha432.oyvey.features.*;
import me.alpha432.oyvey.features.modules.*;
import me.alpha432.oyvey.features.modules.client.*;
import me.alpha432.oyvey.features.modules.render.*;
import me.alpha432.oyvey.features.modules.misc.*;
import me.alpha432.oyvey.features.modules.movement.*;
import me.alpha432.oyvey.features.modules.player.*;
import me.alpha432.oyvey.features.modules.combat.*;
import me.alpha432.oyvey.features.modules.exploit.*;
import net.minecraftforge.common.*;
import java.util.function.*;
import me.alpha432.oyvey.event.events.*;
import java.util.stream.*;
import java.util.*;
import org.lwjgl.input.*;
import me.alpha432.oyvey.features.gui.*;
import com.mojang.realmsclient.gui.*;
import me.alpha432.oyvey.util.*;
import me.alpha432.oyvey.*;
import java.util.concurrent.*;

public class ModuleManager extends Feature
{
    public ArrayList<Module> modules;
    public List<Module> sortedModules;
    public List<String> sortedModulesABC;
    public Animation animationThread;
    
    public ModuleManager() {
        this.modules = new ArrayList<Module>();
        this.sortedModules = new ArrayList<Module>();
        this.sortedModulesABC = new ArrayList<String>();
    }
    
    public void init() {
        this.modules.add((Module)new ClickGui());
        this.modules.add((Module)new FontMod());
        this.modules.add((Module)new ExtraTab());
        this.modules.add((Module)new HUD());
        this.modules.add((Module)new BlockHighlight());
        this.modules.add((Module)new HoleESP());
        this.modules.add((Module)new Skeleton());
        this.modules.add((Module)new Wireframe());
        this.modules.add((Module)new Replenish());
        this.modules.add((Module)new SmallShield());
        this.modules.add((Module)new HandChams());
        this.modules.add((Module)new Trajectories());
        this.modules.add((Module)new FakePlayer());
        this.modules.add((Module)new TpsSync());
        this.modules.add((Module)new MultiTask());
        this.modules.add((Module)new MCP());
        this.modules.add((Module)new LiquidInteract());
        this.modules.add((Module)new Speedmine());
        this.modules.add((Module)new ReverseStep());
        this.modules.add((Module)new NoVoid());
        this.modules.add((Module)new NoHandShake());
        this.modules.add((Module)new BuildHeight());
        this.modules.add((Module)new ChatModifier());
        this.modules.add((Module)new MCF());
        this.modules.add((Module)new PearlNotify());
        this.modules.add((Module)new AutoGG());
        this.modules.add((Module)new ToolTips());
        this.modules.add((Module)new Tracker());
        this.modules.add((Module)new PopCounter());
        this.modules.add((Module)new Offhand());
        this.modules.add((Module)new Surround());
        this.modules.add((Module)new AutoTrap());
        this.modules.add((Module)new AutoWeb());
        this.modules.add((Module)new AutoCrystal());
        this.modules.add((Module)new Killaura());
        this.modules.add((Module)new Criticals());
        this.modules.add((Module)new HoleFiller());
        this.modules.add((Module)new AutoArmor());
        this.modules.add((Module)new Speed());
        this.modules.add((Module)new Step());
        this.modules.add((Module)new Flight());
        this.modules.add((Module)new Scaffold());
        this.modules.add((Module)new PacketFly());
        this.modules.add((Module)new FastPlace());
        this.modules.add((Module)new ESP());
        this.modules.add((Module)new Selftrap());
        this.modules.add((Module)new NoHitBox());
        this.modules.add((Module)new SelfFill());
        this.modules.add((Module)new ArrowESP());
        this.modules.add((Module)new InstantMine());
        this.modules.add((Module)new CityESP());
        this.modules.add((Module)new AutoMiner());
        this.modules.add((Module)new Nametags());
        this.modules.add((Module)new SkyColour());
        this.modules.add((Module)new AntiWeb());
        this.modules.add((Module)new CrystalScale());
        this.modules.add((Module)new Quiver());
        this.modules.add((Module)new PacketPhase());
        this.modules.add((Module)new Offhand2());
        this.modules.add((Module)new Offhand3());
        this.modules.add((Module)new ViewModel());
        this.modules.add((Module)new PenisESP());
        this.modules.add((Module)new SelfWeb());
        this.modules.add((Module)new Animations());
        this.modules.add((Module)new ElytraFly());
        this.modules.add((Module)new ChestSwap());
        this.modules.add((Module)new Velocity());
        this.modules.add((Module)new ShulkerAura());
        this.modules.add((Module)new BoatPhase());
        this.modules.add((Module)new LogOutSpots());
        this.modules.add((Module)new PotionDetect());
        this.modules.add((Module)new InstantEXP());
        this.modules.add((Module)new AutoToxic());
        this.modules.add((Module)new AntiMaps());
        this.modules.add((Module)new ElytraFly2());
        this.modules.add((Module)new Inspector());
        this.modules.add((Module)new Phase());
        this.modules.add((Module)new Debug());
        this.modules.add((Module)new NoClip());
        this.modules.add((Module)new SkullBurrow());
        this.modules.add((Module)new WallClip());
        this.modules.add((Module)new AspectRatio());
        this.modules.add((Module)new GhastModule());
        this.modules.add((Module)new TeleportTracer());
        this.modules.add((Module)new AutoBuilder());
        this.modules.add((Module)new AutoWither());
        this.modules.add((Module)new VisualRange());
        this.modules.add((Module)new NewChunks());
        this.modules.add((Module)new PingSpoof());
        this.modules.add((Module)new AntiHunger());
        this.modules.add((Module)new AutoHeadCrystal());
        this.modules.add((Module)new AutoCrystalC());
        this.modules.add((Module)new EntityMigration());
        this.modules.add((Module)new ChrousPostpone());
        this.modules.add((Module)new Tracers());
        this.modules.add((Module)new DurabilityAlert());
        this.modules.add((Module)new MCXP());
        this.modules.add((Module)new Crasher());
        this.modules.add((Module)new EnchantColour());
        this.modules.add((Module)new PlayerFinder());
        this.modules.add((Module)new ReckFinder());
        this.modules.add((Module)new GappleFinder());
        this.modules.add((Module)new FakeKick());
        this.modules.add((Module)new PacketFlyNew());
        this.modules.add((Module)new Freecam());
        this.modules.add((Module)new TemporalOffloader());
        this.modules.add((Module)new TemporalOverload());
        this.modules.add((Module)new PhobosCA());
        this.modules.add((Module)new ChatReset());
        this.modules.add((Module)new SoundLag());
        this.modules.add((Module)new Surround2());
        this.modules.add((Module)new TickManipulation());
    }
    
    public Module getModuleByName(final String name) {
        for (final Module module : this.modules) {
            if (!module.getName().equalsIgnoreCase(name)) {
                continue;
            }
            return module;
        }
        return null;
    }
    
    public <T extends Module> T getModuleByClass(final Class<T> clazz) {
        for (final Module module : this.modules) {
            if (!clazz.isInstance(module)) {
                continue;
            }
            return (T)module;
        }
        return null;
    }
    
    public void enableModule(final Class<Module> clazz) {
        final Module module = this.getModuleByClass(clazz);
        if (module != null) {
            module.enable();
        }
    }
    
    public void disableModule(final Class<Module> clazz) {
        final Module module = this.getModuleByClass(clazz);
        if (module != null) {
            module.disable();
        }
    }
    
    public void enableModule(final String name) {
        final Module module = this.getModuleByName(name);
        if (module != null) {
            module.enable();
        }
    }
    
    public void disableModule(final String name) {
        final Module module = this.getModuleByName(name);
        if (module != null) {
            module.disable();
        }
    }
    
    public boolean isModuleEnabled(final String name) {
        final Module module = this.getModuleByName(name);
        return module != null && module.isOn();
    }
    
    public boolean isModuleEnabled(final Class<Module> clazz) {
        final Module module = this.getModuleByClass(clazz);
        return module != null && module.isOn();
    }
    
    public Module getModuleByDisplayName(final String displayName) {
        for (final Module module : this.modules) {
            if (!module.getDisplayName().equalsIgnoreCase(displayName)) {
                continue;
            }
            return module;
        }
        return null;
    }
    
    public ArrayList<Module> getEnabledModules() {
        final ArrayList<Module> enabledModules = new ArrayList<Module>();
        for (final Module module : this.modules) {
            if (!module.isEnabled()) {
                continue;
            }
            enabledModules.add(module);
        }
        return enabledModules;
    }
    
    public ArrayList<String> getEnabledModulesName() {
        final ArrayList<String> enabledModules = new ArrayList<String>();
        for (final Module module : this.modules) {
            if (module.isEnabled()) {
                if (!module.isDrawn()) {
                    continue;
                }
                enabledModules.add(module.getFullArrayString());
            }
        }
        return enabledModules;
    }
    
    public ArrayList<Module> getModulesByCategory(final Module.Category category) {
        final ArrayList<Module> modulesCategory = new ArrayList<Module>();
        final ArrayList<Module> list;
        this.modules.forEach(module -> {
            if (module.getCategory() == category) {
                list.add(module);
            }
            return;
        });
        return modulesCategory;
    }
    
    public List<Module.Category> getCategories() {
        return Arrays.asList(Module.Category.values());
    }
    
    public void onLoad() {
        this.modules.stream().filter(Module::listening).forEach(MinecraftForge.EVENT_BUS::register);
        this.modules.forEach(Module::onLoad);
    }
    
    public void onUpdate() {
        this.modules.stream().filter(Feature::isEnabled).forEach(Module::onUpdate);
    }
    
    public void onTick() {
        this.modules.stream().filter(Feature::isEnabled).forEach(Module::onTick);
    }
    
    public void onRender2D(final Render2DEvent event) {
        this.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender2D(event));
    }
    
    public void onRender3D(final Render3DEvent event) {
        this.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender3D(event));
    }
    
    public void sortModules(final boolean reverse) {
        this.sortedModules = this.getEnabledModules().stream().filter(Module::isDrawn).sorted(Comparator.comparing(module -> this.renderer.getStringWidth(module.getFullArrayString()) * (reverse ? -1 : 1))).collect((Collector<? super Object, ?, List<Module>>)Collectors.toList());
    }
    
    public void sortModulesABC() {
        (this.sortedModulesABC = new ArrayList<String>(this.getEnabledModulesName())).sort(String.CASE_INSENSITIVE_ORDER);
    }
    
    public void onLogout() {
        this.modules.forEach(Module::onLogout);
    }
    
    public void onLogin() {
        this.modules.forEach(Module::onLogin);
    }
    
    public void onUnload() {
        this.modules.forEach(MinecraftForge.EVENT_BUS::unregister);
        this.modules.forEach(Module::onUnload);
    }
    
    public void onUnloadPost() {
        for (final Module module : this.modules) {
            module.enabled.setValue((Object)false);
        }
    }
    
    public void onKeyPressed(final int eventKey) {
        if (eventKey == 0 || !Keyboard.getEventKeyState() || ModuleManager.mc.currentScreen instanceof OyVeyGui) {
            return;
        }
        this.modules.forEach(module -> {
            if (module.getBind().getKey() == eventKey) {
                module.toggle();
            }
        });
    }
    
    private class Animation extends Thread
    {
        public Module module;
        public float offset;
        public float vOffset;
        ScheduledExecutorService service;
        
        public Animation() {
            super("Animation");
            this.service = Executors.newSingleThreadScheduledExecutor();
        }
        
        @Override
        public void run() {
            if (HUD.getInstance().renderingMode.getValue() == HUD.RenderingMode.Length) {
                for (final Module module : ModuleManager.this.sortedModules) {
                    final String text = module.getDisplayName() + ChatFormatting.GRAY + ((module.getDisplayInfo() != null) ? (" [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.GRAY + "]") : "");
                    module.offset = ModuleManager.this.renderer.getStringWidth(text) / 1000.0f;
                    module.vOffset = ModuleManager.this.renderer.getFontHeight() / 100.0f;
                    if (module.isEnabled()) {
                        if (module.arrayListOffset <= module.offset) {
                            continue;
                        }
                        if (Util.mc.world == null) {
                            continue;
                        }
                        final Module module3 = module;
                        module3.arrayListOffset -= module.offset;
                        module.sliding = true;
                    }
                    else {
                        if (!module.isDisabled()) {
                            continue;
                        }
                        if (module.arrayListOffset < ModuleManager.this.renderer.getStringWidth(text) && Util.mc.world != null) {
                            final Module module4 = module;
                            module4.arrayListOffset += module.offset;
                            module.sliding = true;
                        }
                        else {
                            module.sliding = false;
                        }
                    }
                }
            }
            else {
                for (final String e : ModuleManager.this.sortedModulesABC) {
                    final Module module2 = OyVey.moduleManager.getModuleByName(e);
                    final String text2 = module2.getDisplayName() + ChatFormatting.GRAY + ((module2.getDisplayInfo() != null) ? (" [" + ChatFormatting.WHITE + module2.getDisplayInfo() + ChatFormatting.GRAY + "]") : "");
                    module2.offset = ModuleManager.this.renderer.getStringWidth(text2) / 1000.0f;
                    module2.vOffset = ModuleManager.this.renderer.getFontHeight() / 100.0f;
                    if (module2.isEnabled()) {
                        if (module2.arrayListOffset <= module2.offset) {
                            continue;
                        }
                        if (Util.mc.world == null) {
                            continue;
                        }
                        final Module module5 = module2;
                        module5.arrayListOffset -= module2.offset;
                        module2.sliding = true;
                    }
                    else {
                        if (!module2.isDisabled()) {
                            continue;
                        }
                        if (module2.arrayListOffset < ModuleManager.this.renderer.getStringWidth(text2) && Util.mc.world != null) {
                            final Module module6 = module2;
                            module6.arrayListOffset += module2.offset;
                            module2.sliding = true;
                        }
                        else {
                            module2.sliding = false;
                        }
                    }
                }
            }
        }
        
        @Override
        public void start() {
            System.out.println("Starting animation thread.");
            this.service.scheduleAtFixedRate(this, 0L, 1L, TimeUnit.MILLISECONDS);
        }
    }
}
