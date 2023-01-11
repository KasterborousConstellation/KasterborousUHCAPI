package fr.supercomete.head.GameUtils.Scenarios;

import fr.supercomete.enums.Gstate;



import fr.supercomete.head.GameUtils.GameMode.ModeHandler.KtbsAPI;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.TeamMode;
import fr.supercomete.head.GameUtils.Team;
import fr.supercomete.head.GameUtils.TeamManager;
import fr.supercomete.head.GameUtils.Time.Timer;
import fr.supercomete.head.core.KasterborousRunnable;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public enum Scenarios implements KasterborousScenario {
	CutClean("CutClean", Compatibility.allModes,Material.IRON_INGOT),
	CatEyes("CatEyes", Compatibility.allModes,Material.EYE_OF_ENDER),
	HasteyBoys("Hastey Boys", Compatibility.allModes,Material.IRON_PICKAXE),
	Timber("Timber", Compatibility.allModes,Material.LOG),
	NoFire("No Fire", Compatibility.allModes,Material.FLINT_AND_STEEL),
	NoSnowBall("No SnowBall", Compatibility.allModes,Material.SNOW_BALL),
	NoHorse("No Horse", Compatibility.allModes,Material.SADDLE),
	NoRod("No Rod", Compatibility.allModes,Material.FISHING_ROD),
	FastSmelting("FastSmelting", Compatibility.allModes,Material.FURNACE),
	FireEnchantLess("FireEnchantLess", Compatibility.allModes,Material.BLAZE_POWDER),
	NoFall("NoFall", Compatibility.allModes,Material.IRON_BOOTS),
	FinalHeal("Final Heal", Compatibility.allModes,Material.APPLE),
	MasterLevel("Master Level", Compatibility.allModes,Material.EXP_BOTTLE),
	NoBow("NoBow", Compatibility.allModes,Material.BOW),
	NoSword("NoSword", Compatibility.allModes,Material.IRON_SWORD),
	KillSwitch("KillSwitch", Compatibility.allModes,Material.ENDER_PEARL),
	GoldenHead("GoldenHead", Compatibility.allModes,Material.GOLDEN_APPLE),
	BetaZombie("BetaZombie", Compatibility.allModes,Material.ROTTEN_FLESH),
	BloodDiamond("BloodDiamond", Compatibility.allModes,Material.REDSTONE),
	NoFood("NoFood", Compatibility.allModes,Material.GOLDEN_CARROT),
    TeamInventory("Team-Inventory",new Compatibility(CompatibilityType.WhiteList,new Class[]{TeamMode.class}),Material.CHEST,Arrays.asList(new KasterborousRunnable(){

        @Override
        public String name() {
            return "Team-Inventory-Runnables";
        }

        @Override
        public void onAPILaunch(KtbsAPI api) {

        }

        @Override
        public void onAPIStop(KtbsAPI api) {

        }

        @Override
        public void onGameLaunch(KtbsAPI api) {
            if(!api.getScenariosProvider().IsScenarioActivated(TeamInventory.getName())){
                return;
            }
            fr.supercomete.commands.TeamInventory.inventoryHashMap.clear();
            for(Team team : api.getTeamProvider().getTeams()){
                fr.supercomete.commands.TeamInventory.inventoryHashMap.put(
                        team,
                        Bukkit.createInventory(null,54,""+TeamManager.getColorOfShortColor(team.getColor())+"Inventaire de l'équipe "+team.getTeamName())
                );
            }
        }

        @Override
        public void onGameEnd(KtbsAPI api) {
            fr.supercomete.commands.TeamInventory.inventoryHashMap.clear();
        }

        @Override
        public void onTick(Gstate gstate, KtbsAPI api) {

        }

        @Override
        public void onTimer(Timer timer) {

        }
    }))
    ;
	private final String name;
	private final Compatibility compatiblity;
	private final Material mat;
    private List<KasterborousRunnable> attachedRunnables;
    Scenarios(String name, Compatibility compatibilty, Material mat){
        this.name=name;
        this.compatiblity=compatibilty;
        this.mat=mat;
        attachedRunnables=null;
    }
	Scenarios(String name, Compatibility compatibilty, Material mat,List<KasterborousRunnable>attachedRunnables){
		this.name=name;
		this.compatiblity=compatibilty;
		this.mat=mat;
        this.attachedRunnables=attachedRunnables;
	}
	@Override
	public String getName(){
	    return this.name;
	}
    @Override
	public Compatibility getCompatiblity() {
		return compatiblity;
	}
    @Override
	public Material getMat() {
		return mat;
	}

    @Override
    public List<KasterborousRunnable> getAttachedRunnable() {
        return attachedRunnables;
    }
}