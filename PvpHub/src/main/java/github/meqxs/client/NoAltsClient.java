package github.meqxs.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class NoAltsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Register our HUD renderers
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            // Render armor display
            ArmorHudRenderer.render(
                drawContext,
                drawContext.getScaledWindowWidth(),
                drawContext.getScaledWindowHeight()
            );
            
            // Render stats display
            StatsHudRenderer.render(
                drawContext,
                drawContext.getScaledWindowWidth(),
                drawContext.getScaledWindowHeight()
            );
        });
    }
} 