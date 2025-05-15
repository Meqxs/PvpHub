package github.meqxs.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import java.util.ArrayList;
import java.util.List;

public class ArmorHudRenderer {
    private static final int SLOT_SIZE = 16;
    private static final int DURABILITY_BAR_WIDTH = 14;
    private static final int DURABILITY_BAR_HEIGHT = 1;
    private static final float ITEM_SCALE = 0.75f;

    public static void render(DrawContext context, int screenWidth, int screenHeight) {
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerEntity player = client.player;
        
        if (player == null) return;

        // Position calculation - to the left of hotbar, moved further left
        int baseX = screenWidth / 2 - 91 - (SLOT_SIZE * 4) - 32; // Increased padding to 32
        int baseY = screenHeight - 19;

        // Convert Iterable<ItemStack> to List<ItemStack>
        List<ItemStack> armorList = new ArrayList<>();
        player.getArmorItems().forEach(armorList::add);
        ItemStack[] armorItems = armorList.toArray(new ItemStack[0]);
        
        for (int i = 0; i < 4; i++) {
            int x = baseX + (i * SLOT_SIZE);
            int y = baseY;
            
            // Draw slot background
            context.fill(x, y, x + SLOT_SIZE, y + SLOT_SIZE, 0x80000000);
            context.fill(x + 1, y + 1, x + SLOT_SIZE - 1, y + SLOT_SIZE - 1, 0x80808080);
            
            ItemStack armorPiece = armorItems[3 - i]; // Reverse order to match vanilla (3=helmet to 0=boots)
            
            // Calculate centered position for scaled item
            float scaledOffset = (SLOT_SIZE - (SLOT_SIZE * ITEM_SCALE)) / 2;
            
            // Push matrix for scaled item rendering
            context.getMatrices().push();
            context.getMatrices().translate(x + scaledOffset, y + scaledOffset, 0);
            context.getMatrices().scale(ITEM_SCALE, ITEM_SCALE, 1.0f);
            
            // Render item
            context.drawItem(armorPiece, 0, 0);
            
            // Pop matrix to restore original scale
            context.getMatrices().pop();
            
            // Render durability bar if item has durability
            if (!armorPiece.isEmpty() && armorPiece.isDamageable()) {
                int maxDurability = armorPiece.getMaxDamage();
                int currentDurability = maxDurability - armorPiece.getDamage();
                float durabilityPercentage = (float) currentDurability / maxDurability;

                // Draw durability bar background
                context.fill(
                    x + 1, y + 13,
                    x + DURABILITY_BAR_WIDTH, y + 13 + DURABILITY_BAR_HEIGHT,
                    0x44000000
                );

                // Draw durability bar
                context.fill(
                    x + 1, y + 13,
                    x + 1 + (int)((DURABILITY_BAR_WIDTH - 1) * durabilityPercentage), y + 13 + DURABILITY_BAR_HEIGHT,
                    getDurabilityColor(durabilityPercentage)
                );
            }
        }
    }

    private static int getDurabilityColor(float percentage) {
        if (percentage > 0.5f) {
            return 0xFF00FF00; // Green
        } else if (percentage > 0.25f) {
            return 0xFFFFFF00; // Yellow
        } else {
            return 0xFFFF0000; // Red
        }
    }
} 