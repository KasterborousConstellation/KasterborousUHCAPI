package fr.supercomete.head.GameUtils.GameConfigurable;
import java.util.ArrayList;

import fr.supercomete.head.GameUtils.GameMode.Modes.DWUHC;
import fr.supercomete.head.GameUtils.GameMode.Modes.EchoEchoUHC;
import fr.supercomete.head.GameUtils.Time.TimeUtility;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.supercomete.head.Inventory.InventoryHandler;
import fr.supercomete.head.core.Main;
public  class Configurable {
    public static boolean ExtractBool(int i) {
        return i % 2 == 0;
    }
    public static boolean ExtractBool(LIST config) {
        return Main.currentGame.getDataFrom(config) % 2 == 0;
    }
    private int data;
    private LIST id;
    private ConfigurableType type;
    public Configurable(LIST id, int data, ConfigurableType type) {
        this.setId(id);
        this.setData(data);
        this.setType(type);
    }

    public ItemStack Representation() {
        ItemStack it = new ItemStack(id.getMat());
        ItemMeta im = it.getItemMeta();
        im.setDisplayName("§b" + id.getName() + " §b" + getRepresented());
        ArrayList<String> lore = Main.SplitCorrectlyString(id.getDescription(), 50, "§7");
        switch (type) {
            case Percentage:
                lore.add(InventoryHandler.ClickTypoAdd + id.getRule().getAdd() + "%");
                if (id.getRule().isMass()) {
                    lore.add(InventoryHandler.ClickTypoMassAdd + id.getRule().getMassadd() + "%");
                }
                lore.add(InventoryHandler.ClickTypoRemove + id.getRule().getAdd() + "%");
                if (id.getRule().isMass()) {
                    lore.add(InventoryHandler.ClickTypoMassRemove + id.getRule().getMassadd() + "%");
                }
                break;
            case Bool:
                lore.add("§aClick pour activer/désactiver");
                break;
            case Integer:
                lore.add(InventoryHandler.ClickTypoAdd + id.getRule().getAdd());
                if (id.getRule().isMass()) {
                    lore.add(InventoryHandler.ClickTypoMassAdd + id.getRule().getMassadd());
                }
                lore.add(InventoryHandler.ClickTypoRemove + id.getRule().getAdd());
                if (id.getRule().isMass()) {
                    lore.add(InventoryHandler.ClickTypoMassRemove + id.getRule().getMassadd());
                }
                break;
            case Timer:
                lore.add(InventoryHandler.ClickTypoAdd + TimeUtility.transform(id.getRule().getAdd(), "§a", "§a", "§a"));
                if (id.getRule().isMass()) {
                    lore.add(InventoryHandler.ClickTypoMassAdd + TimeUtility.transform(id.getRule().getMassadd(), "§a", "§a", "§a"));
                }
                lore.add(InventoryHandler.ClickTypoRemove + TimeUtility.transform(id.getRule().getAdd(), "§c", "§c", "§c"));
                if (id.getRule().isMass()) {
                    lore.add(InventoryHandler.ClickTypoMassRemove + TimeUtility.transform(id.getRule().getMassadd(), "§c", "§c", "§c"));
                }
                break;
            default:
                break;
        }
        im.setLore(lore);
        it.setItemMeta(im);
        return it;
    }

    private String getRepresented() {
        switch (type) {
            case Bool:
                return Main.TranslateBoolean(data % 2 == 0);
            case Timer:
                return TimeUtility.transform(data, "§a", "§a", "§a");
            case Integer:
                return "§a" + data;
            case Percentage:
                return "§a" + data + "%";
            default:
                return "PAUL TU AS OUBLIER";
        }
    }

    public void update(ClickType click) {
        if (click.isRightClick()) {
            if (click.isShiftClick()) {
                if (id.getBound().Inbound(data + id.getRule().getMassadd())) {
                    data = data + id.getRule().getMassadd();
                }
            } else {
                if (id.getBound().Inbound(data + id.getRule().getAdd())) {
                    data = data + id.getRule().getAdd();
                }
            }
        } else {
            if (click.isShiftClick()) {
                if (id.getBound().Inbound(data - id.getRule().getMassadd())) {
                    data = data - id.getRule().getMassadd();
                }
            } else {
                if (id.getBound().Inbound(data - id.getRule().getAdd())) {
                    data = data - id.getRule().getAdd();
                }
            }
        }
        if (type.equals(ConfigurableType.Bool)) {
            if (data == 2) data = 0;
            if (data == -1) data = 1;
        }
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }


    public LIST getId() {
        return id;
    }

    public void setId(LIST id) {
        this.id = id;
    }

    public ConfigurableType getType() {
        return type;
    }

    public void setType(ConfigurableType type) {
        this.type = type;
    }

    public enum LIST {
        DigDown(
                Material.GOLD_SPADE,
                "DigDown",
                "Defini si la partie autorise le digdown",
                false,
                ConfigurableType.Bool,
                Bound.BooleanBound,
                AddingRule.BooleanAddingRule,
                new TypeBinding(BindingType.Player)
        ),
        BlockTransform(
                Material.GOLD_SPADE,
                "Transformation Andésite/Granite/Diorite",
                "Transforme l'andésite, le granite, la diorite en cobblestone",
                true,
                ConfigurableType.Bool,
                Bound.BooleanBound,
                AddingRule.BooleanAddingRule,
                new TypeBinding(BindingType.Block)
        ),
        CrossTeam(
                Material.BANNER,
                "CrossTeam",
                "Autorise le crossteam",
                false,
                ConfigurableType.Bool,
                Bound.BooleanBound,
                AddingRule.BooleanAddingRule,
                new TypeBinding(BindingType.Player)
        ),
        DiamondLeggings(
                Material.DIAMOND_LEGGINGS,
                "Pantalon en diamant",
                "Autorise le craft du pantalon en diamant",
                true,
                ConfigurableType.Bool,
                Bound.BooleanBound,
                AddingRule.BooleanAddingRule,
                new TypeBinding(BindingType.Object)
        ),
        ReviveBeforePvp(
                Material.SOUL_SAND,
                "Revive avant PVP",
                "Fait respawn les joueurs avant le PVP ou les rôles si le mode de jeu est un mode de jeu avec des rôles",
                true,
                ConfigurableType.Bool,
                Bound.BooleanBound,
                AddingRule.BooleanAddingRule,
                new TypeBinding(BindingType.Player)
        ),
        EnderPearl(
                Material.ENDER_PEARL,
                "Ender Pearl",
                "Autorise les enders pearls",
                false,
                ConfigurableType.Bool,
                Bound.BooleanBound,
                AddingRule.BooleanAddingRule,
                new TypeBinding(BindingType.Object)
        ),
        LavaBucket(
                Material.LAVA_BUCKET,
                "Seau de lave",
                "Autorise le seau de lave",
                true,
                ConfigurableType.Bool,
                Bound.BooleanBound,
                AddingRule.BooleanAddingRule,
                new TypeBinding(BindingType.Object)
        ),
        Trap(
                Material.CHEST,
                "Pièges",
                "Autorise les pièges",
                false,
                ConfigurableType.Bool,
                Bound.BooleanBound,
                AddingRule.BooleanAddingRule,
                new TypeBinding(BindingType.Player)
        ),
        End(
                Material.ENDER_STONE,
                "Accès à l'end",
                "Autorise l'end",
                false,
                ConfigurableType.Bool,
                Bound.BooleanBound,
                AddingRule.BooleanAddingRule,
                new TypeBinding(BindingType.World)
        ),
        Nether(
                Material.NETHERRACK,
                "Accès au Nether",
                "Autorise au nether",
                false,
                ConfigurableType.Bool,
                Bound.BooleanBound,
                AddingRule.BooleanAddingRule,
                new TypeBinding(BindingType.World)
        ),
        Killcave(
                Material.TRIPWIRE_HOOK,
                "KillCave",
                "Autorise le Kill en Cave",
                false,
                ConfigurableType.Bool,
                Bound.BooleanBound,
                AddingRule.BooleanAddingRule,
                new TypeBinding(BindingType.Player)
        ),
        Chat(
                Material.BOOK,
                "ToggleChat",
                "Défini si le chat général est autorisé pendant la partie.",
                true,
                ConfigurableType.Bool,
                Bound.BooleanBound,
                AddingRule.BooleanAddingRule,
                new TypeBinding(BindingType.Chat)
        ),
        AutoChatDisable(
                Material.BARRIER,
                "Désactivation du Chat automatique",
                "Désactive le Chat au moment du PVP",
                true,
                ConfigurableType.Bool,
                Bound.BooleanBound,
                AddingRule.BooleanAddingRule,
                new TypeBinding(BindingType.Chat)
        ),
        DeathChat(
                Material.SOUL_SAND,
                "Chat des morts",
                "Autorise le Chat des morts et des spectateurs",
                false,
                ConfigurableType.Bool,
                Bound.BooleanBound,
                AddingRule.BooleanAddingRule,
                new TypeBinding(BindingType.Chat)
        ),
        DeathChatVisible(
                Material.GLASS,
                "Visibilité du Chat des morts",
                "Defini la visibilité du Chat des morts par les joueurs encore en vie",
                false,
                ConfigurableType.Bool,
                Bound.BooleanBound,
                AddingRule.BooleanAddingRule,
                new TypeBinding(BindingType.Chat)
        ),
        //Percentage
        Gravel(
                Material.FLINT,
                "Drop Flint",
                "Defini le taux de drop de flint.",
                10,
                ConfigurableType.Percentage,
                new Bound(1, 100),
                new AddingRule(1, 5),
                new TypeBinding(BindingType.Block)
        ),
        ExpBoost(
                Material.EXP_BOTTLE,
                "Exp Boost",
                "Defini le pourcentage d'exp gagné par le joueur par rapport aux valeurs de base",
                90,
                ConfigurableType.Percentage,
                new Bound(50, 200),
                new AddingRule(1, 10),
                new TypeBinding(BindingType.Player)
        ),
        Apple(
                Material.APPLE,
                "Drop Pomme",
                "Defini le taux de drop des pommes.",
                10,
                ConfigurableType.Percentage,
                new Bound(1, 100),
                new AddingRule(1, 5),
                new TypeBinding(BindingType.Block)
        ), ForcePercent(
                Material.DIAMOND_SWORD,
                "Pourcentage de force",
                "Defini le pourcentage de force bonus par niveau de l'effet force ",
                20,
                ConfigurableType.Percentage,
                new Bound(0, 100),
                new AddingRule(1, 5),
                new TypeBinding(BindingType.Effect)
        ),
        ResistancePercent(
                Material.IRON_BLOCK,
                "Pourcentage de résistance",
                "Defini le pourcentage de résistance par niveau de l'effet résistance ",
                10,
                ConfigurableType.Percentage,
                new Bound(0, 100),
                new AddingRule(1, 5),
                new TypeBinding(BindingType.Effect)
        ),
        DiamondLimit(
                Material.DIAMOND,
                "DiamondLimit",
                "Configuration de la limite de diamants. Quand la limite est atteinte les mineraix de dimants donnent de l'or",
                22,
                ConfigurableType.Integer,
                new Bound(1, 200),
                new AddingRule(1),
                new TypeBinding(BindingType.Block)
        ),
        AmountDiamondArmor(
                Material.DIAMOND,
                "Pièces Diams",
                "Défini le nombre de pièces en diamant que les joueurs peuvent porter.",
                3,
                ConfigurableType.Integer,
                new Bound(1, 4),
                new AddingRule(1),
                new TypeBinding(BindingType.Object)
        ),
        DecTime(
                Material.BARRIER,
                "Déco-Kill",
                "Défini le nombre de minutes avant la mise à mort d'un joueur pour cause de déconnexion trop longue",
                60 * 15,
                ConfigurableType.Timer,
                new Bound(10, 10000),
                new AddingRule(10, 60),
                new TypeBinding(BindingType.Player)
        ),
        StewartUpdate(
                Material.WATCH,
                "Temps de Stewarts",
                "Defini le nombre de secondes nécessaires pour faire avancer d'un % la recherche d'indice de Lethbridge-Stewart",
                1,
                ConfigurableType.Timer,
                new Bound(1, 10),
                new AddingRule(1, 3),
                new ModeBinding(new DWUHC())
        ),
        TardisKey(
                Material.TRIPWIRE_HOOK,
                "Nombre de clef du Tardis",
                "Defini le nombre de clef que le tardis va distribuer. La première clef va toujours au Docteur si il est dans la composition. Le reste des clefs, est distribué entre les role ayant le status: Companion",
                5,
                ConfigurableType.Integer,
                new Bound(0, 5),
                new AddingRule(1),
                new ModeBinding(new DWUHC())
        ),
        TardisEjectionTime(
                Material.SLIME_BLOCK,
                "Temps avant ejection du Tardis",
                "Defini le temps avant lequel un joueur est ejecté du Tardis.",
                120,
                ConfigurableType.Timer,
                new Bound(60, 3600),
                new AddingRule(1, 10),
                new ModeBinding(new DWUHC())
        ),
        SoldierSpyTime(
                Material.WATCH,
                "Durée de l'enquête du Soldat Enquêteur",
                "Defini le temps que prend une enquête du Soldat Enquêteur",
                60 * 10,
                ConfigurableType.Timer,
                new Bound(60, 3600),
                new AddingRule(1, 10),
                new ModeBinding(new DWUHC())
        ),
        KarvanistaTime(
                Material.HOPPER,
                "Durée de la persuasion de Karvanista",
                "Défini le temps durant lequel Karvanista doit persuader son allié.",
                60*7,
                ConfigurableType.Timer,
                new Bound(60,20*60),
                new AddingRule(1,10),
                new ModeBinding(new DWUHC())
        ),
        LoisFlagTime(
                Material.HOPPER,
                "Temps drapeaux",
                "Défini le temps entre chaque apparation de drapeaux de Loïs",
                60*7,
                ConfigurableType.Timer,
                new Bound(60,10*60),
                new AddingRule(1,10),
                new ModeBinding(new EchoEchoUHC())
        )
        ;
        private final String name;
        private final String description;
        private final int data;
        private final Material mat;
        private final ConfigurableType type;
        private final Bound bound;
        private final AddingRule rule;
        private final Binding bind;

        LIST(Material mat, String name, String description, int basedata, ConfigurableType type, Bound bound, AddingRule rule, Binding bind) {
            this.name = name;
            this.description = description;
            this.data = basedata;
            this.mat = mat;
            this.type = type;
            this.bound = bound;
            this.rule = rule;
            this.bind = bind;
        }

        LIST(Material mat, String name, String description, boolean basedata, ConfigurableType type, Bound bound, AddingRule rule, Binding bind) {
            this.name = name;
            this.description = description;
            this.data = basedata ? 0 : 1;
            this.mat = mat;
            this.type = type;
            this.bound = bound;
            this.rule = rule;
            this.bind = bind;
        }

        public Binding getBind() {
            return bind;
        }

        public Bound getBound() {
            return bound;
        }

        public AddingRule getRule() {
            return rule;
        }

        public ConfigurableType getType() {
            return type;
        }

        public int getBaseData() {
            return data;
        }

        public String getDescription() {
            return description;
        }

        public String getName() {
            return name;
        }

        public Material getMat() {
            return mat;
        }

    }
}