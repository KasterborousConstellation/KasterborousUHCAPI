package fr.supercomete.head.role.content.DWUHC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import fr.supercomete.head.role.CommandUse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.supercomete.enums.Camps;
import fr.supercomete.head.role.CoolDown;
import fr.supercomete.head.role.DWRole;
import fr.supercomete.head.role.Status;
import fr.supercomete.head.role.Key.TardisHandler;
import fr.supercomete.head.role.RoleModifier.Companion;

public final class ClaraOswald extends DWRole implements Companion{
	public CoolDown statusCoolDown = new CoolDown(4, 0);
	public CommandUse commandUse = new CommandUse("/dw status");
	public ClaraOswald(UUID owner) {
		super(owner);
	}
    private final ArrayList<Location>locations= new ArrayList<>();
	@Override
	public String askName() {
		return "Clara Oswald";
	}

	@Override
	public Camps getDefaultCamp() {
		return Camps.DoctorCamp;
	}


	@Override
	public List<String> askRoleInfo() {
		return Arrays.asList(
				"§7Grace a votre grande connaisance de l'univers, vous pouvez connaitre le statut d'un joueur avec la commande '/dw status <Joueur>'. Si le joueur possède ne possède aucun status alors vous recevrez: '§cForme de vie inconnue§7', si le joueur possède plusieurs status alors vous en obtiendrez un aléatoirement."+statusCoolDown.formalizedUtilisation(),
				"Vous obtenez les coordonées du Tardis à son apparition, ainsi qu'a tout ses changements d'emplacement."
				
				);
	}

	@Override
	public ItemStack[] askItemStackgiven() {
		return new ItemStack[]{};
	}

	@Override
	public boolean AskIfUnique() {
		return true;
	}

	@Override
	public String AskHeadTag() {
		return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGM3NjA3MDY4YjMwNzA1MWQwYWRhYzk0MzZlZDI2OGZhOWU3M2Q4YTc5YTZiNWRiYzA4YjVkYzQwNDY3NzMxYiJ9fX0=";
	}
	public void NotifyTardis(final boolean first) {
		final Location tardis = TardisHandler.TardisLocation;
		locations.add(tardis);
		final String str = "§7Le Tardis est en §5"+tardis.getBlockX()+" §5"+tardis.getBlockY()+" §5"+tardis.getBlockZ();
		if(Bukkit.getPlayer(getOwner())==null) {
			return;
		}
		final Player player = Bukkit.getPlayer(getOwner());
		if(first) {
			player.sendMessage(ChatColor.WHITE+"[§b"+"Première apparition"+ChatColor.WHITE+"] "+str);
		}else {
			player.sendMessage(ChatColor.WHITE+"[§b"+"Changement d'emplacement"+ChatColor.WHITE+"] "+str);
		}
	}

	@Override
	public Status[] AskStatus() {
		return new Status[] {Status.Humain};
	}

    @Override
    public String[] AskMoreInfo() {
	    final ArrayList<String> array = new ArrayList<>();
	    array.add("[§b"+"Première apparition"+ChatColor.WHITE+"] ");
	    for(int a =0;a<(locations.size()-1);a++){
	        array.add("[§b"+"Changement d'emplacement"+ChatColor.WHITE+"] ");
        }
	    final String[] arr = new String[locations.size()+1];
	    arr[0]=commandUse.generate();
	    int e=1;
	    for(final Location location : locations){
	        arr[e]= array.get(e-1)+" "+"§5"+location.getBlockX()+" §5"+location.getBlockY()+" §5"+location.getBlockZ();
	        e++;
        }
        return arr;
    }


}