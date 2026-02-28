package hithere.dhrd;

import com.seibel.distanthorizons.api.DhApi;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DHPerDimensionRenderDistanceClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("dhrd");
    public static DHRDConfig config;
    
    private String lastDimension = "";

    @Override
    public void onInitializeClient() {
        // Load the config file on startup
        config = DHRDConfig.load();

        // Register a client tick event to detect dimension changes
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.level != null && client.player != null) {
                // Get the current dimension identifier (e.g., "minecraft:the_nether")
                String currentDimension = client.level.dimension().location().toString();

                // If the dimension has changed since the last tick...
                if (!currentDimension.equals(lastDimension)) {
                    lastDimension = currentDimension;
                    onDimensionChanged(currentDimension);
                }
            } else {
                // Reset when leaving the level or disconnecting
                lastDimension = ""; 
            }
        });
    }

    private void onDimensionChanged(String newDimension) {        
        // Look up the new dimension in our config map. If it doesn't exist, use the default.
        int newRenderDistance = config.dimensionRenderDistances.getOrDefault(newDimension, config.defaultRenderDistance);

        applyDHRenderDistance(newRenderDistance);
    }

    private void applyDHRenderDistance(int distance) {        
        try {
			DhApi.Delayed.configs.graphics().chunkRenderDistance().setValue(distance);
        } catch (Exception e) {
            LOGGER.error("Failed to set DH render distance! Is Distant Horizons installed?", e);
        }
    }
}