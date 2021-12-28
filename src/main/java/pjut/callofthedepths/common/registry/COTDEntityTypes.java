package pjut.callofthedepths.common.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import pjut.callofthedepths.common.entity.Crawler;
import pjut.callofthedepths.common.entity.projectile.TorchArrow;
import pjut.callofthedepths.common.setup.CallOfTheDepths;

@Mod.EventBusSubscriber(modid = CallOfTheDepths.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class COTDEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, CallOfTheDepths.MOD_ID);

    public static final RegistryObject<EntityType<TorchArrow>> TORCH_ARROW = ENTITY_TYPES.register("torch_arrow",
            () -> EntityType.Builder.<TorchArrow>of(TorchArrow::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("torch_arrow"));
    public static final RegistryObject<EntityType<Crawler>> CRAWLER = ENTITY_TYPES.register("crawler",
            () -> EntityType.Builder.<Crawler>of(Crawler::new, MobCategory.MONSTER).sized(0.5F, 0.5F).clientTrackingRange(4).build("crawler"));

    public static void init() {
        ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @SubscribeEvent
    public static void onCreateEntityAttributes(EntityAttributeCreationEvent evt) {
        System.out.println("registering entity attributes");
        evt.put(CRAWLER.get(), Crawler.createAttributes());
    }
}
