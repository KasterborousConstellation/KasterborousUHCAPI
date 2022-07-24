package fr.supercomete.head.GameUtils.Scenarios;
import org.bukkit.Material;
public enum Scenarios implements KasterborousScenario {
	CutClean("CutClean",ScenarioCompatibility.allModes,Material.IRON_INGOT),
	CatEyes("CatEyes",ScenarioCompatibility.allModes,Material.EYE_OF_ENDER),
	HasteyBoys("Hastey Boys",ScenarioCompatibility.allModes,Material.IRON_PICKAXE),
	Timber("Timber",ScenarioCompatibility.allModes,Material.LOG),
	NoFire("No Fire",ScenarioCompatibility.allModes,Material.FLINT_AND_STEEL),
	NoSnowBall("No SnowBall",ScenarioCompatibility.allModes,Material.SNOW_BALL),
	NoHorse("No Horse",ScenarioCompatibility.allModes,Material.SADDLE),
	NoRod("No Rod",ScenarioCompatibility.allModes,Material.FISHING_ROD),
	FastSmelting("FastSmelting",ScenarioCompatibility.allModes,Material.FURNACE),
	FireEnchantLess("FireEnchantLess",ScenarioCompatibility.allModes,Material.BLAZE_POWDER),
	NoFall("NoFall",ScenarioCompatibility.allModes,Material.IRON_BOOTS),
	FinalHeal("Final Heal",ScenarioCompatibility.allModes,Material.APPLE),
	MasterLevel("Master Level",ScenarioCompatibility.allModes,Material.EXP_BOTTLE),
	NoBow("NoBow",ScenarioCompatibility.allModes,Material.BOW),
	NoSword("NoSword",ScenarioCompatibility.allModes,Material.IRON_SWORD),
	KillSwitch("KillSwitch",ScenarioCompatibility.allModes,Material.ENDER_PEARL),
	GoldenHead("GoldenHead",ScenarioCompatibility.allModes,Material.GOLDEN_APPLE),
	BetaZombie("BetaZombie",ScenarioCompatibility.allModes,Material.ROTTEN_FLESH),
	DiamondLimit("DiamondLimit",ScenarioCompatibility.allModes,Material.DIAMOND),
	BloodDiamond("BloodDiamond",ScenarioCompatibility.allModes,Material.REDSTONE),
	NoFood("NoFood",ScenarioCompatibility.allModes,Material.GOLDEN_CARROT)
    ;
	private final String name;
	private final ScenarioCompatibility compatiblity;
	private final Material mat;
	Scenarios(String name,ScenarioCompatibility compatibilty,Material mat){
		this.name=name;
		this.compatiblity=compatibilty;
		this.mat=mat;
	}
	@Override
	public String getName(){
	    return this.name;
	}
    @Override
	public ScenarioCompatibility getCompatiblity() {
		return compatiblity;
	}
    @Override
	public Material getMat() {
		return mat;
	}
}