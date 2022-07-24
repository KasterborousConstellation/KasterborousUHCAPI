package fr.supercomete.head.Inventory;

// Comment the line below in if you use compatre
//import com.github.johnnyjayjay.compatre.NmsDependent;

import net.minecraft.server.v1_8_R3.MojangsonParser;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.UUID;

/**
 * Class with a utility method to get custom heads as ItemStacks with Spigot.
 * The code below can easily be adjusted to work with any version 
 * (only the craftbukkit/nms package names and perhaps the material need to be changed).
 * 
 * If you want to compile it once and be compatible with any version, you only need to
 * find a way around the obfuscated packages. You can either use reflection for this 
 * (as is common practice), or <a href="https://github.com/johnnyjayjay/compatre">compatre</a>
 * (much simpler).
 * 
 * As it stands, the code is compiled against 1.8.8.
 * 
 * @author JohnnyJayJay (https://github.com/johnnyjayjay)
 */
// Comment the line below in if you use compatre
//@NmsDependent
public class CustomHeads {
  /* Since version 1.16.1, UUIDs in NBT tags are no longer serialised as strings,
     but as an int array of 4 x 32 bits, going from most significant to least significant
     bits of the UUID. */
  private static final boolean newStorageSystem;

  static {
    String versionString = Bukkit.getBukkitVersion();
    int[] version = Arrays.stream(versionString.substring(0, versionString.indexOf('-')).split("\\."))
        .mapToInt(Integer::parseInt)
        .toArray();
    newStorageSystem = version[0] > 1
        || (version[0] == 1 && version[1] > 16)
        || (version[0] == 1 && version[1] == 16 && version[2] >= 1);
  }
  /**
   * Creates a skull item stack that uses the given base64-encoded texture
   *
   * @param texture The texture value. Can be found on e.g. https://minecraft-heads.com/custom-heads/
   *                in the "Value" field.
   * @return an ItemStack with this texture.
   */
  public static ItemStack create(String texture) {
    // for non-legacy api versions, use PLAYER_HEAD or LEGACY_SKULL_ITEM instead of SKULL_ITEM.
    ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
    /* This code is compiled against version 1.8.8, but works on any version if you use
       the correct package name. */
    net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
    try {
      String nbtString = String.format(
          "{SkullOwner:{Id:%s,Properties:{textures:[{Value:\"%s\"}]}}}",
          serializeUuid(UUID.randomUUID()), texture
      );
      NBTTagCompound nbt = MojangsonParser.parse(nbtString);
      nmsItem.setTag(nbt);
    } catch (Exception e) { // Not catching a more specific exception here because that exception changes across versions
      throw new AssertionError("NBT Tag parsing failed - This should never happen.", e);
    }
    return CraftItemStack.asBukkitCopy(nmsItem);
  }
  private static String serializeUuid(UUID uuid) {
    if (newStorageSystem) {
      StringBuilder result = new StringBuilder();
      long msb = uuid.getMostSignificantBits();
      long lsb = uuid.getLeastSignificantBits();
      return result.append("[I;")
          .append(msb >> 32)
          .append(',')
          .append(msb & Integer.MAX_VALUE)
          .append(',')
          .append(lsb >> 32)
          .append(',')
          .append(lsb & Integer.MAX_VALUE)
          .append(']')
          .toString();
    } else {
      return '"' + uuid.toString() + '"';
    }
  }
}