/*
 * Class By M4TH1EU
 */

package fr.rpcore.thirstmod.common.thirstlogic;

import fr.rpcore.thirstmod.network.NetworkManager;
import fr.rpcore.thirstmod.network.PacketThirstStats;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.biome.BiomeDesert;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Field;
import java.util.Random;

public class ThirstStats {
    public int thirstLevel;
    public float saturation;
    public float exhaustion;
    public int thirstTimer;
    public boolean poisoned;
    public int poisonTimer;
    public int movementSpeed;
    public transient int lastThirstLevel;
    public transient float lastSaturation;
    public transient boolean lastPoisoned;
    public transient Random random = new Random();
    public transient DamageSource thirstDmgSource = new DamageThirst();
    public transient Field foodTimer;

    public ThirstStats() {
        this.lastThirstLevel = -1;
        resetStats();
    }

    public void update(EntityPlayer player) {
        if (this.exhaustion > 5.0F) {
            this.exhaustion -= 5.0F;
            if (this.saturation > 0.0F) {
                this.saturation = Math.max(this.saturation - 1.0F, 0.0F);
            } else if (player.world.getDifficulty() != EnumDifficulty.PEACEFUL) {
                this.thirstLevel = Math.max(this.thirstLevel - 1, 0);
            }
        }
        if (this.thirstLevel <= 6) {
            player.setSprinting(false);
            if ((this.thirstLevel == 0) &&
                    (this.thirstTimer++ > 200) && (
                    (player.getHealth() > 10.0F) || (player.world.getDifficulty() == EnumDifficulty.HARD) || (
                            (player.world.getDifficulty() == EnumDifficulty.NORMAL) && (player.getHealth() > 1.0F)))) {
                this.thirstTimer = 0;
                player.attackEntityFrom(this.thirstDmgSource, 1.0F);
            }
        }
        int ms = player.isRiding() ? 10 : this.movementSpeed;
        float exhaustMultiplier = player.world.isDaytime() ? 1.0F : 0.9F;
        exhaustMultiplier *= ((player.world.getBiomeForCoordsBody(player.getPosition()) instanceof BiomeDesert) ? 2.0F : 1.0F);
        if ((player.isInsideOfMaterial(Material.WATER)) || (player.isInWater())) {
            addExhaustion(0.03F * ms * 0.003F * exhaustMultiplier);
        } else if (player.onGround) {
            if (player.isSprinting()) {
                addExhaustion(0.06F * ms * 0.018F * exhaustMultiplier);
            } else {
                addExhaustion(0.01F * ms * 0.018F * exhaustMultiplier);
            }
        } else if (!player.isRiding()) {
            if (player.isSprinting()) {
                addExhaustion(0.06F * ms * 0.025F * exhaustMultiplier);
            } else {
                addExhaustion(0.01F * ms * 0.025F * exhaustMultiplier);
            }
        }
        if ((this.poisoned) && (this.thirstLevel > 0)) {
            if (this.poisonTimer++ < 1200) {
                if ((player.getHealth() > 1.0F) && (player.world.getDifficulty() != EnumDifficulty.PEACEFUL) && (this.thirstTimer++ > 200)) {
                    this.thirstTimer = 0;
                    player.attackEntityFrom(this.thirstDmgSource, 1.0F);
                }
            } else {
                this.poisoned = false;
                this.poisonTimer = 0;
            }
        }
        if (this.foodTimer == null) {
            try {
                this.foodTimer = player.getFoodStats().getClass().getDeclaredField("foodTimer");
                this.foodTimer.setAccessible(true);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        if ((this.thirstLevel < 16) || (this.poisoned)) {
            try {
                this.foodTimer.setInt(player.getFoodStats(), 0);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if ((this.lastThirstLevel != this.thirstLevel) || (this.lastSaturation != this.saturation) || (this.lastPoisoned != this.poisoned)) {
            NetworkManager.getNetworkWrapper().sendTo(new PacketThirstStats(this), (EntityPlayerMP) player);
            this.lastThirstLevel = this.thirstLevel;
            this.lastSaturation = this.saturation;
            this.lastPoisoned = this.poisoned;
        }
        if (Keyboard.isKeyDown(36)) {
            this.thirstLevel = Math.max(this.thirstLevel - 1, 0);
        } else if (Keyboard.isKeyDown(37)) {
            this.thirstLevel = Math.min(this.thirstLevel + 1, 20);
        }
    }

    public void addStats(int heal, float sat) {
        this.thirstLevel = Math.min(this.thirstLevel + heal, 20);
        this.saturation = Math.min(this.saturation + heal * sat * 2.0F, this.thirstLevel);
    }

    public void addExhaustion(float exhaustion) {
        this.exhaustion = Math.min(this.exhaustion + exhaustion, 40.0F);
    }

    public void attemptToPoison(float chance) {
        if (this.random.nextFloat() < chance) {
            this.poisoned = true;
        }
    }

    public boolean canDrink() {
        return this.thirstLevel < 20;
    }

    public int getMovementSpeed(EntityPlayer player) {
        double x = player.posX - player.prevPosX;
        double y = player.posY - player.prevPosY;
        double z = player.posZ - player.prevPosZ;
        return (int) Math.round(100.0D * Math.sqrt(x * x + y * y + z * z));
    }

    public void resetStats() {
        this.thirstLevel = 20;
        this.saturation = 5.0F;
        this.exhaustion = 0.0F;
        this.poisoned = false;
        this.poisonTimer = 0;
    }

    public static class DamageThirst
            extends DamageSource {
        public DamageThirst() {
            super("thirst");
            setDamageBypassesArmor();
            setDamageIsAbsolute();
        }

        public ITextComponent getDeathMessage(EntityLivingBase entity) {
            if ((entity instanceof EntityPlayer)) {
                EntityPlayer player = (EntityPlayer) entity;
                return new TextComponentString(player.getDisplayName() + "'s body is now made up of 0% water!");
            }
            return super.getDeathMessage(entity);
        }
    }
}
