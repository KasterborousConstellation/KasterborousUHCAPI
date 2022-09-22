package fr.supercomete.head.role.content.EchoEchoUHC;

import fr.supercomete.enums.Camps;
import fr.supercomete.head.GameUtils.GUI.MangaGui;
import fr.supercomete.head.role.Bonus.Bonus;
import fr.supercomete.head.role.Bonus.BonusType;
import fr.supercomete.head.role.CoolDown;
import fr.supercomete.head.role.EchoRole;
import fr.supercomete.head.role.RoleModifier.PreAnnouncementExecute;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Neo extends EchoRole implements PreAnnouncementExecute {
    public CoolDown coolDown = new CoolDown(0,30);
    public MangaGui gui;
    public Neo(UUID owner) {
        super(owner);
    }

    @Override
    public void PreAnnouncement() {
        this.gui=new MangaGui(this,Bukkit.getPlayer(getOwner()));
    }
    public Manga ChoseManga(){
        if(mangas.size()==Manga.values().length) {
            return null;
        }else{
            Random random=new Random();
            Manga manga=Manga.values()[random.nextInt(Manga.values().length)];
            while(mangas.contains(manga)){
                manga = Manga.values()[random.nextInt(Manga.values().length)];
            }
            return manga;
        }
    }
    //Neo 264
    //Flavio 135
    //Lucien 130
    //Paul 74
    //Lois 67
    //Baptiste 59
    //Alexis 65
    //Nicolas 30

    public ArrayList<Manga> mangas = new ArrayList<>();
    @Override
    public BranlusqueLevel getBranlusqueLevel() {
        return BranlusqueLevel.MageTeacher;
    }

    @Override
    public float getMangaProbability() {
        return 1;
    }

    @Override
    public String[] AskMoreInfo() {
        return new String[0];
    }

    @Override
    public String askName() {
        return "Neo";
    }

    @Override
    public Camps getDefaultCamp() {
        return Camps.Revolutionnaires;
    }

    @Override
    public List<String> askRoleInfo() {
        return Arrays.asList(
                "Vous devez gagner avec les "+Camps.Revolutionnaires.getColoredName(),
                "Votre admiration pour les mangas vous permet d'admirer vos manga avec la commande '/echo manga'",
                "Vous pouvez avec la commande '/echo fouille <Joueur>' fouiller les poches d'un joueur, se situant dans un rayon de 10blocs, afin de lui voler un manga. (Utilisable toutes les 30s)",
                "Cependant chaque membre d'Echo Echo ne possède pas le même nombre de manga. La fouille a un pourcentage different de réussite en fonction du rôle du joueur.",
                "La commande '/echo more' vous donne l'annexe des probabilités d'obtention d'un manga en fonction du rôle.",
                "Chaque manga vous donne un bonus supplémentaire entre (force, vitesse, et bonus de vie)."
        );
    }
    public void addManga(Manga manga){
        this.mangas.add(manga);
        addBonus(manga.getBonus());
    }
    //Komi
    //MHA
    //Goblin Slayer
    public static enum Manga{
        Komi_1("Komi cherche ses mots (Tome 1)",new Bonus(BonusType.Heart,1)),
        Komi_2("Komi cherche ses mots (Tome 2)",new Bonus(BonusType.Heart,1)),
        Komi_3("Komi cherche ses mots (Tome 3)",new Bonus(BonusType.Heart,2)),
        Goblin_Slayer_1("Goblin Slayer (Tome 1)",new Bonus(BonusType.Force,2)),
        Goblin_Slayer_2("Goblin Slayer (Tome 2)",new Bonus(BonusType.Force,2)),
        Goblin_Slayer_3("Goblin Slayer (Tome 3)",new Bonus(BonusType.Force,2)),
        Goblin_Slayer_4("Goblin Slayer (Tome 4)",new Bonus(BonusType.Force,2)),
        Goblin_Slayer_5("Goblin Slayer (Tome 5)",new Bonus(BonusType.Force,3)),
        Goblin_Slayer_6("Goblin Slayer (Tome 6)",new Bonus(BonusType.Force,3)),
        Goblin_Slayer_7("Goblin Slayer (Tome 7)",new Bonus(BonusType.Force,3)),
        Goblin_Slayer_8("Goblin Slayer (Tome 8)",new Bonus(BonusType.Force,3)),
        MHA_1("My Hero Academia (Tome 1)",new Bonus(BonusType.Speed,3)),
        MHA_2("My Hero Academia (Tome 2)",new Bonus(BonusType.Speed,3)),
        MHA_3("My Hero Academia (Tome 3)",new Bonus(BonusType.Speed,3)),
        MHA_4("My Hero Academia (Tome 4)",new Bonus(BonusType.Speed,5)),
        MHA_5("My Hero Academia (Tome 5)",new Bonus(BonusType.Speed,5)),
        MHA_6("My Hero Academia (Tome 6)",new Bonus(BonusType.Speed,6)),
        ;
        private final Bonus bonus;
        private final String name;
        Manga(final String name,Bonus bonus){
            this.bonus=bonus;
            this.name=name;
        }
        public Bonus getBonus() {
            return bonus;
        }
        public String getName(){
            return name;
        }
    }
    @Override
    public ItemStack[] askItemStackgiven() {
        return new ItemStack[0];
    }

    @Override
    public boolean AskIfUnique() {
        return false;
    }

    @Override
    public String AskHeadTag() {
        return null;
    }
}
