package github.meqxs.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;

public class StatsHudRenderer {
    private static final int PADDING = 4;
    private static final int TEXT_COLOR = 0xFFFFFFFF;
    private static final int SHADOW_COLOR = 0x4D000000;

    public static void render(DrawContext context, int screenWidth, int screenHeight) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        // Get FPS
        String fps = client.fpsDebugString.split(" ")[0];

        // Get ping
        int ping = -1;
        PlayerListEntry playerListEntry = client.player.networkHandler.getPlayerListEntry(client.player.getUuid());
        if (playerListEntry != null) {
            ping = playerListEntry.getLatency();
        }

        // Prepare text
        String fpsText = "FPS: " + fps;
        String pingText = "Ping: " + (ping != -1 ? ping : "---") + "ms";

        // Draw background shadows for better visibility
        context.fill(PADDING - 1, PADDING - 1, 
                    PADDING + client.textRenderer.getWidth(fpsText) + 1, 
                    PADDING + client.textRenderer.fontHeight + 1, 
                    SHADOW_COLOR);
        
        context.fill(PADDING - 1, PADDING + client.textRenderer.fontHeight + PADDING - 1,
                    PADDING + client.textRenderer.getWidth(pingText) + 1,
                    PADDING + client.textRenderer.fontHeight * 2 + PADDING + 1,
                    SHADOW_COLOR);

        // Draw text
        context.drawTextWithShadow(client.textRenderer, Text.literal(fpsText), 
                                 PADDING, PADDING, TEXT_COLOR);
        context.drawTextWithShadow(client.textRenderer, Text.literal(pingText),
                                 PADDING, PADDING + client.textRenderer.fontHeight + PADDING, 
                                 TEXT_COLOR);
    }
} 