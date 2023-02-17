//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Program Files\Minecraft Deobfuscator\Mappings\1.12.2"!

//Decompiled by Procyon!

package me.alpha432.oyvey;

import net.minecraftforge.fml.common.*;
import me.alpha432.oyvey.manager.*;
import net.minecraftforge.fml.common.event.*;
import org.lwjgl.opengl.*;
import org.apache.logging.log4j.*;

@Mod(modid = "ccshack", name = "CCSHack", version = "1.0.2Beta")
public class OyVey
{
    public static final String MODID = "ccshack";
    public static final String MODNAME = "CCSHack";
    public static final String MODVER = "1.0.2Beta";
    public static final Logger LOGGER;
    public static CommandManager commandManager;
    public static FriendManager friendManager;
    public static ModuleManager moduleManager;
    public static PacketManager packetManager;
    public static ColorManager colorManager;
    public static HoleManager holeManager;
    public static InventoryManager inventoryManager;
    public static PotionManager potionManager;
    public static RotationManager rotationManager;
    public static PositionManager positionManager;
    public static SpeedManager speedManager;
    public static ReloadManager reloadManager;
    public static FileManager fileManager;
    public static ConfigManager configManager;
    public static ServerManager serverManager;
    public static EventManager eventManager;
    public static TextManager textManager;
    public static SafetyManager safetyManager;
    @Mod.Instance
    public static OyVey INSTANCE;
    private static boolean unloaded;
    
    public static void load() {
        OyVey.LOGGER.info("\n\nLoading CCSHack...");
        OyVey.unloaded = false;
        if (OyVey.reloadManager != null) {
            OyVey.reloadManager.unload();
            OyVey.reloadManager = null;
        }
        OyVey.textManager = new TextManager();
        OyVey.commandManager = new CommandManager();
        OyVey.friendManager = new FriendManager();
        OyVey.moduleManager = new ModuleManager();
        OyVey.rotationManager = new RotationManager();
        OyVey.packetManager = new PacketManager();
        OyVey.eventManager = new EventManager();
        OyVey.speedManager = new SpeedManager();
        OyVey.potionManager = new PotionManager();
        OyVey.inventoryManager = new InventoryManager();
        OyVey.serverManager = new ServerManager();
        OyVey.fileManager = new FileManager();
        OyVey.colorManager = new ColorManager();
        OyVey.positionManager = new PositionManager();
        OyVey.configManager = new ConfigManager();
        OyVey.holeManager = new HoleManager();
        OyVey.safetyManager = new SafetyManager();
        OyVey.LOGGER.info("Managers loaded.");
        OyVey.moduleManager.init();
        OyVey.LOGGER.info("Modules loaded.");
        OyVey.configManager.init();
        OyVey.eventManager.init();
        OyVey.LOGGER.info("EventManager loaded.");
        OyVey.textManager.init(true);
        OyVey.moduleManager.onLoad();
        OyVey.LOGGER.info("CCSHack successfully loaded!\n");
    }
    
    public static void unload(final boolean unload) {
        OyVey.LOGGER.info("\n\nUnloading CCSHack...");
        if (unload) {
            (OyVey.reloadManager = new ReloadManager()).init((OyVey.commandManager != null) ? OyVey.commandManager.getPrefix() : ".");
        }
        onUnload();
        OyVey.eventManager = null;
        OyVey.friendManager = null;
        OyVey.speedManager = null;
        OyVey.holeManager = null;
        OyVey.positionManager = null;
        OyVey.rotationManager = null;
        OyVey.configManager = null;
        OyVey.commandManager = null;
        OyVey.colorManager = null;
        OyVey.serverManager = null;
        OyVey.fileManager = null;
        OyVey.potionManager = null;
        OyVey.inventoryManager = null;
        OyVey.moduleManager = null;
        OyVey.textManager = null;
        OyVey.LOGGER.info("CCSHack unloaded!\n");
    }
    
    public static void reload() {
        unload(false);
        load();
    }
    
    public static void onUnload() {
        if (!OyVey.unloaded) {
            OyVey.eventManager.onUnload();
            OyVey.moduleManager.onUnload();
            OyVey.configManager.saveConfig(OyVey.configManager.config.replaceFirst("ccshack/", ""));
            OyVey.moduleManager.onUnloadPost();
            OyVey.unloaded = true;
        }
    }
    
    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        OyVey.LOGGER.info("CCSHack is the best hack ever");
    }
    
    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        Display.setTitle("CCSHack v1.0.2Beta");
        load();
    }
    
    static {
        LOGGER = LogManager.getLogger("CCSHack");
        OyVey.unloaded = false;
    }
}
