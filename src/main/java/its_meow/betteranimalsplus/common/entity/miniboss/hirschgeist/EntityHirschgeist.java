package its_meow.betteranimalsplus.common.entity.miniboss.hirschgeist;

import its_meow.betteranimalsplus.common.entity.ITimeOfDay;
import its_meow.betteranimalsplus.common.entity.miniboss.hirschgeist.ai.HirschgeistAIAttackMelee;
import its_meow.betteranimalsplus.common.entity.miniboss.hirschgeist.ai.HirschgeistAIFlameAttack;
import its_meow.betteranimalsplus.init.ModLootTables;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class EntityHirschgeist extends EntityLiving implements IMob, ITimeOfDay {

    public EntityHirschgeist(World worldIn) {
        super(worldIn);
        // this.setSize(3, 4);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new HirschgeistAIAttackMelee(this, 0.7D));
        this.tasks.addTask(2, new HirschgeistAIFlameAttack(this));
        this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 15F));
        this.targetTasks.addTask(1, new EntityAIFindEntityNearestPlayer(this));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(150.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(50.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.65D);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.isDaytime(this.world)) {
            this.setSize(1, 2);
        } else {
            this.setSize(3, 4);
        }
    }

    @Override
    public boolean getCanSpawnHere() {
        if (this.world.getEntitiesWithinAABB(EntityHirschgeist.class, this.getEntityBoundingBox().grow(150)).size() == 1) {
            return false;
        } else {
            return false;
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SKELETON_HORSE_AMBIENT;
    }

    @Override
    protected float getSoundPitch() {
        return 0.3F; // Lower pitch of skeleton horse sound
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound(SoundEvents.ENTITY_SHEEP_STEP, 0.5F, 0.6F);
    }

    @Override
    protected ResourceLocation getLootTable() {
        return ModLootTables.hirschgeist;
    }

    @Override
    public boolean attackable() {
        return !this.isDaytime(this.world);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isDaytime(this.world) && FMLCommonHandler.instance().getSide() == Side.CLIENT && FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            if (source.getTrueSource() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) source.getTrueSource();
                player.sendMessage(new TextComponentString("The " + I18n.format("entity.betteranimalsplus.Hirschgeist.name") + " is immortal in the daytime. Try fighting it later."));
            }
        }
        return this.isDaytime(this.world) ? false : super.attackEntityFrom(source, amount);
    }

    @Override
    public void setAttackTarget(EntityLivingBase entityIn) {
        super.setAttackTarget(this.isDaytime(this.world) ? null : entityIn);
    }

    public Vec3d getHeadLookVec(float p_184665_1_) {
        Vec3d vec3d;
        if (this.getAttackTarget() != null) {
            BlockPos blockpos = this.getAttackTarget().getPosition();
            float f = Math.max(MathHelper.sqrt(this.getDistanceSqToCenter(blockpos)) / 4.0F, 1.0F);
            float f1 = 6.0F / f;
            float f2 = this.rotationPitch;
            this.rotationPitch = -f1 * 1.5F * 5.0F;
            vec3d = this.getLook(p_184665_1_);
            this.rotationPitch = f2;

            return vec3d;
        }
        return null;
    }

}
