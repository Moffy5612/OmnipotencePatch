package com.moffy5612.omnipotence_patch;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.library.events.MaterialEvent.TraitRegisterEvent;
import xyz.phanta.tconevo.init.TconEvoTraits;

@Mod(
    modid = "omnipotencepatch",
    name = "Omnipotence Patch",
    version = "1.0"
)
public class OmnipotencePatch {
    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        MinecraftForge.EVENT_BUS.register(new Subscribe());
    }
}

class Subscribe{

    public ModifierOmnipotence modifierOmnipotence = new ModifierOmnipotence();

    @SubscribeEvent
    public void onTraitRegistering(TraitRegisterEvent<?> event){
        //replace tconevo.omnipotence
        if(Loader.isModLoaded("tconevo")){
            if(event.trait.getIdentifier().equals("tconevo.omnipotence")){
                event.setCanceled(true);
                for(String typeKey:xyz.phanta.tconevo.material.PartType.MAIN.typeKeys){
                    if(!event.material.hasTrait("tbinding.modifieromnipotence", typeKey)){
                        event.material.addTrait(modifierOmnipotence, typeKey);
                    }
                }
                for(String typeKey:xyz.phanta.tconevo.material.PartType.TOOL.typeKeys){
                    if(!event.material.hasTrait("tconevo.modifierinfinitium", typeKey)){
                        event.material.addTrait(TconEvoTraits.TRAIT_INFINITUM, typeKey);
                    }
                }
            }
        }
    }
}
