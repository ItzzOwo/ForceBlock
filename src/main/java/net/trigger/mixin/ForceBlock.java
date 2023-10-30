package net.trigger.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.lwjgl.glfw.GLFW;
@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public class ForceBlock {
    private boolean toggleForceBlock = false;
    private long lastKeyPressTime = 0L;
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo info) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player != null) {
            if (GLFW.glfwGetKey(mc.getWindow().getHandle(), GLFW.GLFW_KEY_P) == GLFW.GLFW_PRESS) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastKeyPressTime >= 300) {
                    toggleForceBlock = !toggleForceBlock;
                    if (toggleForceBlock) {
                        BlockPos playerPos = mc.player.getBlockPos();
                        BlockPos ghostBlockPos = new BlockPos(playerPos.getX(), playerPos.getY() - 1, playerPos.getZ());
                        BlockState ghostBlockState = Blocks.BARRIER.getDefaultState();
                        ((ClientWorld) mc.world).setBlockState(ghostBlockPos, ghostBlockState, 19);
                        //pretty cool fly/speed (can be used as head-hitter lmao)
                    } else {
                        toggleForceBlock = false;
                    }
                }
            }
        }
    }
}
