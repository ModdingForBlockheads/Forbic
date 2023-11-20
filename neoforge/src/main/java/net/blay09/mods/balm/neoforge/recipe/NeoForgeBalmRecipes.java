package net.blay09.mods.balm.neoforge.recipe;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.recipe.BalmRecipes;
import net.blay09.mods.balm.neoforge.DeferredRegisters;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.function.Supplier;

public class NeoForgeBalmRecipes implements BalmRecipes {
    @Override
    public <T extends Recipe<?>> DeferredObject<RecipeType<T>> registerRecipeType(Supplier<RecipeType<T>> typeSupplier, Supplier<RecipeSerializer<T>> serializerSupplier, ResourceLocation identifier) {
        final var register = DeferredRegisters.get(Registries.RECIPE_TYPE, identifier.getNamespace());
        final var registryObject = register.register(identifier.getPath(), typeSupplier);

        final var serializerRegister = DeferredRegisters.get(Registries.RECIPE_SERIALIZER, identifier.getNamespace());
        final var serializerRegistryObject = serializerRegister.register(identifier.getPath(), serializerSupplier);

        return new DeferredObject<>(identifier, registryObject, () -> registryObject.isBound() && serializerRegistryObject.isBound());
    }

}
