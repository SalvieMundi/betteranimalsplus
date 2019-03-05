package its_meow.betteranimalsplus.common.block;

import its_meow.betteranimalsplus.common.tileentity.TileEntityHandOfFate;
import its_meow.betteranimalsplus.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockHandOfFate extends BlockHorizontal {

	protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0D, 0.9375D, 0.875D, 0.9375D);
	protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 1.0D);
	protected static final AxisAlignedBB WES_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);
	protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 1.0D, 0.875D, 0.9375D);

	public BlockHandOfFate() {
		super(Properties.create(Material.IRON).hardnessAndResistance(3.0F, 2.0F));
		this.setRegistryName("handoffate");
		this.setDefaultState(this.getDefaultState().with(BlockHorizontal.HORIZONTAL_FACING, EnumFacing.NORTH));
	}
	
	@OnlyIn(Dist.CLIENT)
	public boolean hasCustomBreakingProgress(IBlockState state) {
		return true;
	}

	@Override
	public int getLightValue(IBlockState state, IWorldReader world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileEntityHandOfFate) {
			TileEntityHandOfFate tehof = (TileEntityHandOfFate) te;
			if(tehof.isOnFire()) {
				return 15;
			}
		}
		return super.getLightValue(state, world, pos);
	}

	@Override
	protected void fillStateContainer(Builder<Block, IBlockState> builder) {
		builder.add(BlockHorizontal.HORIZONTAL_FACING);
	}


	@Override
	public int getOpacity(IBlockState state, IBlockReader world, BlockPos pos) {
		return 1;
	}


	@Override
	public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack held = playerIn.getHeldItem(hand);
		if(held.getItem() == Items.FLINT_AND_STEEL) {
			if(!playerIn.isCreative()) {
				held.damageItem(1, playerIn);
			}
			worldIn.getPendingBlockTicks().scheduleTick(pos, state.getBlock(), 100);
			worldIn.notifyBlockUpdate(pos, worldIn.getBlockState(pos),
					worldIn.getBlockState(pos), 0);
			worldIn.markBlockRangeForRenderUpdate(pos.down(5).west(5).north(5),
					pos.up(5).east(5).south(5));
			TileEntity te = worldIn.getTileEntity(pos);
			if(te instanceof TileEntityHandOfFate) {
				TileEntityHandOfFate tehof = (TileEntityHandOfFate) te;
				tehof.setOnFire(true);
				return true;
			}
		} else if(held.getItem() == Blocks.SAND.asItem() || held.getItem() == Items.WATER_BUCKET) {
			TileEntity te = worldIn.getTileEntity(pos);
			if(te instanceof TileEntityHandOfFate) {
				TileEntityHandOfFate tehof = (TileEntityHandOfFate) te;
				tehof.setOnFire(false);
				return true;
			}
		} else if(held.getItem() == Items.NETHER_WART) {
			TileEntity te = worldIn.getTileEntity(pos);
			if(te instanceof TileEntityHandOfFate) {
				TileEntityHandOfFate tehof = (TileEntityHandOfFate) te;
				if(!tehof.hasNetherWart() && tehof.isOnFire()) {
					if(!playerIn.isCreative()) {
						held.shrink(1);
					}
					tehof.setHasNetherWart(true);
					return true;
				}
			}
		} else if(held.getItem() == ModItems.antler) {
			TileEntity te = worldIn.getTileEntity(pos);
			if(te instanceof TileEntityHandOfFate) {
				TileEntityHandOfFate tehof = (TileEntityHandOfFate) te;
				if(!tehof.hasAntler() && tehof.isOnFire()) {
					if(!playerIn.isCreative()) {
						held.shrink(1);
					}
					tehof.setHasAntler(true);
					return true;
				}
			}
		} else if(held.getItem() == ModItems.venisonRaw || held.getItem() == ModItems.venisonCooked) {
			TileEntity te = worldIn.getTileEntity(pos);
			if(te instanceof TileEntityHandOfFate) {
				TileEntityHandOfFate tehof = (TileEntityHandOfFate) te;
				if(!tehof.hasVenison() && tehof.isOnFire()) {
					if(!playerIn.isCreative()) {
						held.shrink(1);
					}
					tehof.setHasVenison(true);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public IBlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing());
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isTopSolid(IBlockState state) {
		return false;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public boolean isNormalCube(IBlockState state) {
		return false;
	}


	@Override
	public boolean isSolid(IBlockState state) {
		return false;
	}

	@Override
	public boolean hasTileEntity() {
		return true;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(IBlockState state, IBlockReader worldIn) {
		return new TileEntityHandOfFate();
	}

	public static ItemBlock getItemBlock() {
		return ModItems.itemHandOfFate;
	}

}
