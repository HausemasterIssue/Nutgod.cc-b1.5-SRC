package me.zeroeightsix.kami.util.other;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class EventRenderBlockModel extends EventCancellable {

    private IBlockAccess blockAccess;
    private IBakedModel bakedModel;
    private IBlockState blockState;
    private BlockPos blockPos;
    private BufferBuilder bufferBuilder;
    private boolean checkSides;
    private long rand;
    private boolean renderable;

    public EventRenderBlockModel(IBlockAccess blockAccess, IBakedModel bakedModel, IBlockState blockState, BlockPos blockPos, BufferBuilder bufferBuilder, boolean checkSides, long rand) {
        this.blockAccess = blockAccess;
        this.bakedModel = bakedModel;
        this.blockState = blockState;
        this.blockPos = blockPos;
        this.bufferBuilder = bufferBuilder;
        this.checkSides = checkSides;
        this.rand = rand;
    }

    public IBlockAccess getBlockAccess() {
        return this.blockAccess;
    }

    public void setBlockAccess(IBlockAccess blockAccess) {
        this.blockAccess = blockAccess;
    }

    public IBakedModel getBakedModel() {
        return this.bakedModel;
    }

    public void setBakedModel(IBakedModel bakedModel) {
        this.bakedModel = bakedModel;
    }

    public IBlockState getBlockState() {
        return this.blockState;
    }

    public void setBlockState(IBlockState blockState) {
        this.blockState = blockState;
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    public void setBlockPos(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public BufferBuilder getBufferBuilder() {
        return this.bufferBuilder;
    }

    public void setBufferBuilder(BufferBuilder bufferBuilder) {
        this.bufferBuilder = bufferBuilder;
    }

    public boolean isCheckSides() {
        return this.checkSides;
    }

    public void setCheckSides(boolean checkSides) {
        this.checkSides = checkSides;
    }

    public long getRand() {
        return this.rand;
    }

    public void setRand(long rand) {
        this.rand = rand;
    }

    public boolean isRenderable() {
        return this.renderable;
    }

    public void setRenderable(boolean renderable) {
        this.renderable = renderable;
    }
}
