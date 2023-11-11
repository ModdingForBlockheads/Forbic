package net.blay09.mods.balm.neoforge.client;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.BalmClientRuntime;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.api.client.rendering.BalmTextures;
import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.blay09.mods.balm.neoforge.event.NeoForgeBalmEvents;
import net.blay09.mods.balm.neoforge.client.keymappings.NeoForgeBalmKeyMappings;
import net.blay09.mods.balm.neoforge.client.rendering.NeoForgeBalmModels;
import net.blay09.mods.balm.neoforge.client.rendering.NeoForgeBalmRenderers;
import net.blay09.mods.balm.neoforge.client.rendering.NeoForgeBalmTextures;
import net.blay09.mods.balm.neoforge.client.screen.NeoForgeBalmScreens;
import net.blay09.mods.balm.neoforge.event.NeoForgeBalmClientEvents;

public class NeoForgeBalmClientRuntime implements BalmClientRuntime {

    private final BalmRenderers renderers = new NeoForgeBalmRenderers();
    private final BalmTextures textures = new NeoForgeBalmTextures();
    private final BalmScreens screens = new NeoForgeBalmScreens();
    private final BalmKeyMappings keyMappings = new NeoForgeBalmKeyMappings();
    private final BalmModels models = new NeoForgeBalmModels();

    public NeoForgeBalmClientRuntime() {
        NeoForgeBalmClientEvents.registerEvents(((NeoForgeBalmEvents) Balm.getEvents()));
    }

    @Override
    public BalmRenderers getRenderers() {
        return renderers;
    }

    @Override
    public BalmTextures getTextures() {
        return textures;
    }

    @Override
    public BalmScreens getScreens() {
        return screens;
    }

    @Override
    public BalmModels getModels() {
        return models;
    }

    @Override
    public BalmKeyMappings getKeyMappings() {
        return keyMappings;
    }

    @Override
    public void initialize(String modId, Runnable initializer) {
        ((NeoForgeBalmRenderers) renderers).register();
        ((NeoForgeBalmScreens) screens).register();
        ((NeoForgeBalmModels) models).register();
        ((NeoForgeBalmKeyMappings) keyMappings).register();

        initializer.run();
    }
}
