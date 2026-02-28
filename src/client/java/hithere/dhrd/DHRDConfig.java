package hithere.dhrd;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
//import com.seibel.distanthorizons.api.DhApi;

public class DHRDConfig {
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "DH-perdim-renderdist.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    // Stores our dimensions and their target render distances
    public Map<String, Integer> dimensionRenderDistances = new HashMap<>();
    
    // Fallback render distance if a dimension isn't listed
    public int defaultRenderDistance = 256;// DhApi.Delayed.configs.graphics().chunkRenderDistance().getValue(); 

    public DHRDConfig() {
        // Set up some default values so the user has an example to look at
        dimensionRenderDistances.put("minecraft:overworld", 256);
        dimensionRenderDistances.put("minecraft:the_nether", 32);
        dimensionRenderDistances.put("minecraft:the_end", 1024);
    }

    /*
     Loads the config from disk, or creates a new one if it doesn't exist.
     */
    public static DHRDConfig load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                return GSON.fromJson(reader, DHRDConfig.class);
            } catch (IOException e) {
                System.err.println("Failed to load DHRD config! Using defaults.");
                e.printStackTrace();
            }
        }
        
        // If file doesn't exist or failed to load, create a new one and save it
        DHRDConfig config = new DHRDConfig();
        config.save();
        return config;
    }

    /*
     Saves the current config to disk.
     */
    public void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            System.err.println("Failed to save DHRD config!");
            e.printStackTrace();
        }
    }

}
