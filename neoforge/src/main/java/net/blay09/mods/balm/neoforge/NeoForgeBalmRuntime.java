package net.blay09.mods.balm.neoforge;

import net.blay09.mods.balm.api.BalmHooks;
import net.blay09.mods.balm.api.BalmRegistries;
import net.blay09.mods.balm.api.BalmRuntime;
import net.blay09.mods.balm.api.block.BalmBlockEntities;
import net.blay09.mods.balm.api.block.BalmBlocks;
import net.blay09.mods.balm.api.command.BalmCommands;
import net.blay09.mods.balm.api.config.BalmConfig;
import net.blay09.mods.balm.api.entity.BalmEntities;
import net.blay09.mods.balm.api.event.BalmEvents;
import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.balm.api.loot.BalmLootTables;
import net.blay09.mods.balm.api.menu.BalmMenus;
import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.balm.api.provider.BalmProviders;
import net.blay09.mods.balm.api.proxy.ProxyResolutionException;
import net.blay09.mods.balm.api.proxy.SidedProxy;
import net.blay09.mods.balm.api.sound.BalmSounds;
import net.blay09.mods.balm.api.stats.BalmStats;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.blay09.mods.balm.common.CommonBalmLootTables;
import net.blay09.mods.balm.neoforge.block.NeoForgeBalmBlocks;
import net.blay09.mods.balm.neoforge.block.entity.NeoForgeBalmBlockEntities;
import net.blay09.mods.balm.neoforge.command.NeoForgeBalmCommands;
import net.blay09.mods.balm.neoforge.config.NeoForgeBalmConfig;
import net.blay09.mods.balm.neoforge.entity.NeoForgeBalmEntities;
import net.blay09.mods.balm.neoforge.event.NeoForgeBalmCommonEvents;
import net.blay09.mods.balm.neoforge.event.NeoForgeBalmEvents;
import net.blay09.mods.balm.neoforge.item.NeoForgeBalmItems;
import net.blay09.mods.balm.neoforge.menu.NeoForgeBalmMenus;
import net.blay09.mods.balm.neoforge.network.NeoForgeBalmNetworking;
import net.blay09.mods.balm.neoforge.provider.NeoForgeBalmProviders;
import net.blay09.mods.balm.neoforge.sound.NeoForgeBalmSounds;
import net.blay09.mods.balm.neoforge.stats.NeoForgeBalmStats;
import net.blay09.mods.balm.neoforge.world.NeoForgeBalmWorldGen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class NeoForgeBalmRuntime implements BalmRuntime {
    private final BalmWorldGen worldGen = new NeoForgeBalmWorldGen();
    private final BalmBlocks blocks = new NeoForgeBalmBlocks();
    private final BalmBlockEntities blockEntities = new NeoForgeBalmBlockEntities();
    private final NeoForgeBalmEvents events = new NeoForgeBalmEvents();
    private final BalmItems items = new NeoForgeBalmItems();
    private final BalmMenus menus = new NeoForgeBalmMenus();
    private final BalmNetworking networking = new NeoForgeBalmNetworking();
    private final BalmConfig config = new NeoForgeBalmConfig();
    private final BalmHooks hooks = new NeoForgeBalmHooks();
    private final BalmRegistries registries = new NeoForgeBalmRegistries();
    private final BalmSounds sounds = new NeoForgeBalmSounds();
    private final BalmEntities entities = new NeoForgeBalmEntities();
    private final BalmProviders providers = new NeoForgeBalmProviders();
    private final BalmCommands commands = new NeoForgeBalmCommands();
    private final BalmLootTables lootTables = new CommonBalmLootTables();
    private final BalmStats stats = new NeoForgeBalmStats();

    private final List<String> addonClasses = new ArrayList<>();

    public NeoForgeBalmRuntime() {
        NeoForgeBalmCommonEvents.registerEvents(events);
    }

    @Override
    public BalmConfig getConfig() {
        return config;
    }

    @Override
    public BalmEvents getEvents() {
        return events;
    }

    @Override
    public BalmWorldGen getWorldGen() {
        return worldGen;
    }

    @Override
    public BalmBlocks getBlocks() {
        return blocks;
    }

    @Override
    public BalmBlockEntities getBlockEntities() {
        return blockEntities;
    }

    @Override
    public BalmItems getItems() {
        return items;
    }

    @Override
    public BalmMenus getMenus() {
        return menus;
    }

    @Override
    public BalmNetworking getNetworking() {
        return networking;
    }

    @Override
    public BalmHooks getHooks() {
        return hooks;
    }

    @Override
    public BalmRegistries getRegistries() {
        return registries;
    }

    @Override
    public BalmSounds getSounds() {
        return sounds;
    }

    @Override
    public BalmEntities getEntities() {
        return entities;
    }

    @Override
    public BalmProviders getProviders() {
        return providers;
    }

    @Override
    public BalmCommands getCommands() {
        return commands;
    }

    @Override
    public BalmLootTables getLootTables() {
        return lootTables;
    }

    @Override
    public BalmStats getStats() {
        return stats;
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public String getModName(String modId) {
        return ModList.get().getModContainerById(modId).map(it -> it.getModInfo().getDisplayName()).orElse(modId);
    }

    @Override
    public void initialize(String modId, Runnable initializer) {
        ((NeoForgeBalmItems) items).register();
        ((NeoForgeBalmEntities) entities).register();
        ((NeoForgeBalmWorldGen) worldGen).register();
        ((NeoForgeBalmStats) stats).register();

        initializer.run();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener((FMLLoadCompleteEvent event) -> initializeAddons());
        DeferredRegisters.register(modId, modEventBus);
    }

    @Override
    public void initializeIfLoaded(String modId, String className) {
        if (isModLoaded(modId)) {
            addonClasses.add(className);
        }
    }

    @Override
    public <T> SidedProxy<T> sidedProxy(String commonName, String clientName) {
        SidedProxy<T> proxy = new SidedProxy<>(commonName, clientName);
        try {
            if (FMLEnvironment.dist.isClient()) {

                proxy.resolveClient();
            } else {
                proxy.resolveCommon();
            }
        } catch (ProxyResolutionException e) {
            throw new RuntimeException(e);
        }

        return proxy;
    }

    private void initializeAddons() {
        for (String addonClass : addonClasses) {
            try {
                Class.forName(addonClass).getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addServerReloadListener(ResourceLocation identifier, PreparableReloadListener reloadListener) {
        NeoForge.EVENT_BUS.addListener((AddReloadListenerEvent event) -> event.addListener(reloadListener));
    }

    @Override
    public void addServerReloadListener(ResourceLocation identifier, Consumer<ResourceManager> reloadListener) {
        NeoForge.EVENT_BUS.addListener((AddReloadListenerEvent event) -> event.addListener((ResourceManagerReloadListener) reloadListener::accept));
    }

}