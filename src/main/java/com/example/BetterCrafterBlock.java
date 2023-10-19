package com.example;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CrafterBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.CrafterBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BetterCrafterBlock extends CrafterBlock {
    public BetterCrafterBlock(BlockBehaviour.Properties properties){
        super(properties);
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        // FEATURE: Quasi-connectivity
        boolean bl2 = level.hasNeighborSignal(blockPos) || level.hasNeighborSignal(blockPos.above());
        boolean bl3 = blockState.getValue(TRIGGERED);
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (bl2 && !bl3) {
            level.scheduleTick(blockPos, this, 1);
            level.setBlock(blockPos, blockState.setValue(TRIGGERED, true), 2);
            this.setBlockEntityTriggered(blockEntity, true);
        } else if (!bl2 && bl3) {
            level.setBlock(blockPos, (blockState.setValue(TRIGGERED, false)).setValue(CRAFTING, false), 2);
            this.setBlockEntityTriggered(blockEntity, false);
        }
    }

    private void setBlockEntityTriggered(@Nullable BlockEntity blockEntity, boolean bl) {
        if (blockEntity instanceof CrafterBlockEntity crafterBlockEntity) {
            crafterBlockEntity.setTriggered(bl);
        }
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos blockPos) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof CrafterBlockEntity crafterBlockEntity) {
            int signal = crafterBlockEntity.getRedstoneSignal();
            // FEATURE: Full-strength signal
            return signal >= 9 ? 15 : signal;
        }
        return 0;
    }

}
