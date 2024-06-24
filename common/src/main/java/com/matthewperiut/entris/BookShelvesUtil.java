package com.matthewperiut.entris;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class BookShelvesUtil {
    public static int countBookShelves(PlayerEntity player) {
        int ct = 0;
        Box box = Box.from(player.getPos());
        box = box.expand(8.f);
        for (double x = box.minX; x < box.maxX; x++) {
            for (double y = box.minY; y < box.maxY; y++) {
                for (double z = box.minZ; z < box.maxZ; z++) {
                    if (player.getWorld().getBlockState(BlockPos.ofFloored(x,y,z)).getBlock() == Blocks.BOOKSHELF)
                        ct++;
                }
            }
        }
        return ct;
    }
}
