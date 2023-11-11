package net.blay09.mods.balm.api.block.entity;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mojang.datafixers.util.Pair;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.energy.EnergyStorage;
import net.blay09.mods.balm.api.fluid.FluidTank;
import net.blay09.mods.balm.api.provider.BalmProvider;
import net.blay09.mods.balm.api.provider.BalmProviderHolder;
import net.blay09.mods.balm.neoforge.container.BalmInvWrapper;
import net.blay09.mods.balm.neoforge.energy.NeoForgeEnergyStorage;
import net.blay09.mods.balm.neoforge.fluid.NeoForgeFluidTank;
import net.blay09.mods.balm.neoforge.provider.NeoForgeBalmProviders;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.capabilities.Capabilities;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BalmBlockEntityBase extends BlockEntity {

    private final Map<Capability<?>, LazyOptional<?>> capabilities = new HashMap<>();
    private final Table<Capability<?>, Direction, LazyOptional<?>> sidedCapabilities = HashBasedTable.create();
    private boolean capabilitiesInitialized;

    public BalmBlockEntityBase(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    private void addCapabilities(BalmProvider<?> provider, Map<Capability<?>, LazyOptional<?>> capabilities) {
        NeoForgeBalmProviders forgeProviders = (NeoForgeBalmProviders) Balm.getProviders();
        Capability<?> capability = forgeProviders.getCapability(provider.getProviderClass());
        capabilities.put(capability, LazyOptional.of(provider::getInstance));

        if (provider.getProviderClass() == Container.class) {
            capabilities.put(Capabilities.ITEM_HANDLER, LazyOptional.of(() -> new BalmInvWrapper((Container) provider.getInstance())));
        } else if (provider.getProviderClass() == FluidTank.class) {
            capabilities.put(Capabilities.FLUID_HANDLER, LazyOptional.of(() -> new NeoForgeFluidTank((FluidTank) provider.getInstance())));
        } else if (provider.getProviderClass() == EnergyStorage.class) {
            capabilities.put(Capabilities.ENERGY, LazyOptional.of(() -> new NeoForgeEnergyStorage((EnergyStorage) provider.getInstance())));
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getProvider(Class<T> clazz) {
        NeoForgeBalmProviders forgeProviders = (NeoForgeBalmProviders) Balm.getProviders();
        Capability<?> capability = forgeProviders.getCapability(clazz);
        return (T) getCapability(capability).resolve().orElse(null);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (!capabilitiesInitialized) {
            List<Object> providers = new ArrayList<>();
            buildProviders(providers);

            for (Object holder : providers) {
                BalmProviderHolder providerHolder = (BalmProviderHolder) holder;
                for (BalmProvider<?> provider : providerHolder.getProviders()) {
                    addCapabilities(provider, capabilities);
                }

                for (Pair<Direction, BalmProvider<?>> pair : providerHolder.getSidedProviders()) {
                    Direction direction = pair.getFirst();
                    BalmProvider<?> provider = pair.getSecond();
                    Map<Capability<?>, LazyOptional<?>> sidedCapabilities = this.sidedCapabilities.column(direction);
                    addCapabilities(provider, sidedCapabilities);
                }
            }
            capabilitiesInitialized = true;
        }

        LazyOptional<?> result = null;
        if (side != null) {
            result = sidedCapabilities.get(cap, side);
        }
        if (result == null) {
            result = capabilities.get(cap);
        }
        return result != null ? result.cast() : super.getCapability(cap, side);
    }

    protected abstract void buildProviders(List<Object> providers);

}
