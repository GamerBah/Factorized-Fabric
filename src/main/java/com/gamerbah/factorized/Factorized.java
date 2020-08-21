package com.gamerbah.factorized;
/* Created by GamerBah on 8/18/20 */

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

public class Factorized implements ModInitializer {

	// ITEM GROUPS
	public static final ItemGroup ITEM_GROUP =
			FabricItemGroupBuilder.create(new Identifier("factorized", "general")).icon(() -> new ItemStack(Factorized.COPPER_INGOT))
					.build();

	// ORES
	public static final Block COPPER_ORE   = new Block(FabricBlockSettings.of(Material.METAL).hardness(4.0F)
			                                                   .breakByTool(FabricToolTags.PICKAXES, 1));
	public static final Block TIN_ORE      = new Block(FabricBlockSettings.of(Material.METAL).hardness(3.0F)
			                                                   .breakByTool(FabricToolTags.PICKAXES, 1));
	public static final Block CHROMIUM_ORE = new Block(FabricBlockSettings.of(Material.METAL).hardness(5.0F)
			                                                   .breakByTool(FabricToolTags.PICKAXES, 2));

	// INGOTS
	public static final Item COPPER_INGOT   = new Item(new Item.Settings().group(Factorized.ITEM_GROUP));
	public static final Item TIN_INGOT      = new Item(new Item.Settings().group(Factorized.ITEM_GROUP));
	public static final Item CHROMIUM_INGOT = new Item(new Item.Settings().group(Factorized.ITEM_GROUP));
	public static final Item STEEL_INGOT    = new Item(new Item.Settings().group(Factorized.ITEM_GROUP));
	public static final Item BRONZE_INGOT   = new Item(new Item.Settings().group(Factorized.ITEM_GROUP));

	// TOOLS
	public static final Item WRENCH = new Item(new Item.Settings().group(Factorized.ITEM_GROUP));

	@Override
	public void onInitialize() {
		// ORES
		Registry.register(Registry.BLOCK, new Identifier("factorized", "copper_ore"), COPPER_ORE);
		Registry.register(Registry.ITEM,
		                  new Identifier("factorized", "copper_ore"),
		                  new BlockItem(COPPER_ORE, new Item.Settings().group(Factorized.ITEM_GROUP)));
		Registry.register(Registry.BLOCK, new Identifier("factorized", "tin_ore"), TIN_ORE);
		Registry.register(Registry.ITEM,
		                  new Identifier("factorized", "tin_ore"),
		                  new BlockItem(TIN_ORE, new Item.Settings().group(Factorized.ITEM_GROUP)));
		Registry.register(Registry.BLOCK, new Identifier("factorized", "chromium_ore"), CHROMIUM_ORE);
		Registry.register(Registry.ITEM,
		                  new Identifier("factorized", "chromium_ore"),
		                  new BlockItem(CHROMIUM_ORE, new Item.Settings().group(Factorized.ITEM_GROUP)));

		// INGOTS
		Registry.register(Registry.ITEM, new Identifier("factorized", "copper_ingot"), COPPER_INGOT);
		Registry.register(Registry.ITEM, new Identifier("factorized", "tin_ingot"), TIN_INGOT);
		Registry.register(Registry.ITEM, new Identifier("factorized", "chromium_ingot"), CHROMIUM_INGOT);
		Registry.register(Registry.ITEM, new Identifier("factorized", "steel_ingot"), STEEL_INGOT);
		Registry.register(Registry.ITEM, new Identifier("factorized", "bronze_ingot"), BRONZE_INGOT);

		// TOOLS
		Registry.register(Registry.ITEM, new Identifier("factorized", "wrench"), WRENCH);

		// Add ores to biome generation
		Registry.BIOME.forEach(this::handleBiome);
		RegistryEntryAddedCallback.event(Registry.BIOME).register((i, identifier, biome) -> handleBiome(biome));

		// Event Listeners
		registerEventListeners();
	}

	private void registerEventListeners() {
		UseBlockCallback.EVENT.register((playerEntity, world, hand, blockHitResult) -> {
			if (playerEntity.getStackInHand(hand).getItem() == WRENCH) {
				// Player has wrench in hand
				BlockPos   blockPos   = blockHitResult.getBlockPos();
				BlockState blockState = world.getBlockState(blockPos);
				if (blockState.getBlock() instanceof BlockWithEntity) {
					// Target block is a container
					world.setBlockState(blockPos, blockState.rotate(BlockRotation.CLOCKWISE_90));
					return ActionResult.SUCCESS;
				}
			}
			return ActionResult.PASS;
		});
	}

	private void handleBiome(Biome biome) {
		if (biome.getCategory() != Biome.Category.NETHER && biome.getCategory() != Biome.Category.THEEND) {
			biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES,
			                 Feature.ORE.configure(new OreFeatureConfig(
					                 OreFeatureConfig.Target.NATURAL_STONE,
					                 Factorized.COPPER_ORE.getDefaultState(),
					                 8 //Ore vein size
			                 )).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(
					                 8, //Number of veins per chunk
					                 0, //Bottom Offset
					                 0, //Min y level
					                 64 //Max y level
			                 ))));
			biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES,
			                 Feature.ORE.configure(new OreFeatureConfig(
					                 OreFeatureConfig.Target.NATURAL_STONE,
					                 Factorized.TIN_ORE.getDefaultState(),
					                 8 //Ore vein size
			                 )).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(
					                 8, //Number of veins per chunk
					                 0, //Bottom Offset
					                 0, //Min y level
					                 64 //Max y level
			                 ))));
			biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES,
			                 Feature.ORE.configure(new OreFeatureConfig(
					                 OreFeatureConfig.Target.NATURAL_STONE,
					                 Factorized.CHROMIUM_ORE.getDefaultState(),
					                 7 //Ore vein size
			                 )).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(
					                 7, //Number of veins per chunk
					                 0, //Bottom Offset
					                 0, //Min y level
					                 48 //Max y level
			                 ))));
		}
	}

}
