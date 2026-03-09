package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.impl.render.Render3DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.util.render.RenderUtil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.math.AxisAlignedBB;

import java.awt.*;

import org.lwjgl.opengl.GL11;

public class ESPModule extends Module {

    public ESPModule() {
        super("ESP", "Highlights players and mobs.", Category.RENDER, true, false, false);
    }

    @Override
    public void onRender3D(Render3DEvent event) {

        if (mc.world == null || mc.player == null) return;

        // Set up OpenGL state
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDepthMask(false);

        for (Entity entity : mc.world.loadedEntityList) {

            if (entity == mc.player) continue;

            Color color;

            // FRIENDS
            if (entity instanceof EntityPlayer &&
                OyVey.friendManager.isFriend(entity.getName())) {

                color = Color.GREEN;
            }

            // OTHER PLAYERS (health-based)
            else if (entity instanceof EntityPlayer) {

                EntityPlayer player = (EntityPlayer) entity;

                float health = player.getHealth();
                float max = player.getMaxHealth();
                float percent = health / max;

                color = new Color(1f - percent, percent, 0f);
            }

            // MOBS / ANIMALS
            else if (entity instanceof EntityMob || entity instanceof EntityAnimal) {
                color = Color.WHITE;
            }

            else continue;

            double x = entity.lastTickPosX +
                (entity.posX - entity.lastTickPosX) * mc.getRenderPartialTicks()
                - mc.getRenderManager().viewerPosX;

            double y = entity.lastTickPosY +
                (entity.posY - entity.lastTickPosY) * mc.getRenderPartialTicks()
                - mc.getRenderManager().viewerPosY;

            double z = entity.lastTickPosZ +
                (entity.posZ - entity.lastTickPosZ) * mc.getRenderPartialTicks()
                - mc.getRenderManager().viewerPosZ;

            AxisAlignedBB bb = entity.getEntityBoundingBox()
                .offset(-entity.posX, -entity.posY, -entity.posZ)
                .offset(x, y, z);

            // FILLED BOX (semi-transparent)
            RenderUtil.drawBox(bb, new Color(color.getRed(), color.getGreen(), color.getBlue(), 60));

            // OUTLINE
            RenderUtil.drawBlockOutline(bb, color, 2.0f);

            // TRACER
            RenderUtil.drawTracerLine(
                x + (entity.width / 2),
                y,
                z + (entity.width / 2),
                color,
                1.5f
            );
        }

        // Restore OpenGL state
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }
}
