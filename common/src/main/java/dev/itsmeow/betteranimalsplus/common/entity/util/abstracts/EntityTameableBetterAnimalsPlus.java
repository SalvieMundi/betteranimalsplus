package dev.itsmeow.betteranimalsplus.common.entity.util.abstracts;

import dev.itsmeow.imdlib.entity.EntityTypeContainer;
import dev.itsmeow.imdlib.entity.interfaces.IContainerEntity;
import dev.itsmeow.betteranimalsplus.common.entity.util.EntityTypeContainerBAPTameable;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public abstract class EntityTameableBetterAnimalsPlus extends TamableAnimal implements IContainerEntity<EntityTameableBetterAnimalsPlus> {

    protected EntityTameableBetterAnimalsPlus(EntityType<? extends EntityTameableBetterAnimalsPlus> type, Level world) {
        super(type, world);
    }

    @Override
    public void tick() {
        super.tick();
        if(!level.isClientSide && this.isInSittingPose() != this.isOrderedToSit()) {
            this.setInSittingPose(this.isOrderedToSit());
        }
    }

    public boolean isTamingItem(Item item) {
        EntityTypeContainer<?> container = getContainer();
        if(container instanceof EntityTypeContainerBAPTameable<?>) {
            String[] items = ((EntityTypeContainerBAPTameable<?>) container).getTameItems();
            String id = item.getDescriptionId();
            for(String itemsId : items) {
                if(itemsId.startsWith("#")) {
                    // TODO test impl
                    if(ItemTags.getAllTags().getMatchingTags(item).contains(new ResourceLocation(itemsId.substring(1)))) {
                        return true;
                    }
                } else if(id.equals(itemsId)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean removeWhenFarAway(double range) {
        return despawn(range);
    }

    @Override
    public EntityTameableBetterAnimalsPlus getImplementation() {
        return this;
    }

}
