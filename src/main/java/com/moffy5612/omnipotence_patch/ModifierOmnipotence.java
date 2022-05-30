package com.moffy5612.omnipotence_patch;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.TinkerUtil;

import javax.annotation.Nullable;

public class ModifierOmnipotence extends AbstractTrait {
    @Nullable
    private EntityLivingBase hitEntity = null; // probably doesn't need to be thread-safe

    public ModifierOmnipotence() {
        super("omnipotencepatch.modifieromnipotence", 0xfd6785);
        MinecraftForge.EVENT_BUS.register(this);
    }
    

    @Override
    public void onHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damage, boolean isCritical) {
        if (!target.world.isRemote) {
            hitEntity = target;    
        }
    }

    @SubscribeEvent
    public void onLivingAttacked(LivingAttackEvent event) {
        if (hitEntity != null && event.getEntityLiving() == hitEntity) {
            hitEntity.hurtResistantTime = 0;
            DamageSource dmgSrc = event.getSource().setDamageBypassesArmor().setDamageIsAbsolute();
            dmgSrc.damageType = "infinity";
            dmgSrc.setDamageAllowedInCreativeMode();
        }else if(event.getEntityLiving() != null){
            if((event.getSource().getTrueSource()) instanceof EntityPlayer){
                EntityPlayer player = (EntityPlayer)event.getSource().getTrueSource();
                ItemStack handItem;
                if(!player.getHeldItemMainhand().isEmpty())handItem = player.getHeldItemMainhand();
                else handItem = player.getHeldItemOffhand();
                NBTTagCompound omnipotence = TinkerUtil.getModifierTag(handItem, identifier);
                if(omnipotence.getSize() > 0){
                    afterHit(handItem, player, event.getEntityLiving(), event.getAmount(), true, true);
                }
            }
        }
    }
    @Override
    public void afterHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damageDealt, boolean wasCritical, boolean wasHit) {
        if (target.world.isRemote || !wasHit
                || (player instanceof EntityPlayer && ((EntityPlayer)player).getCooledAttackStrength(0.5F) < 0.95F)) {
            return;
        }
        float undealtDmg = Math.max(target.getMaxHealth() - damageDealt, 0F);
        if(undealtDmg > 0F){
            target.setHealth((target.getHealth() - undealtDmg)/2);
        }
    }

    @SubscribeEvent
    public void onTickEnd(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            hitEntity = null;
        }
    }

    @Override
    public int getPriority() {
        return -1;
    }
}
