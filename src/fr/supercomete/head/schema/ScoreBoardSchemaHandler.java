package fr.supercomete.head.schema;
import fr.supercomete.head.GameUtils.GameMode.ModeHandler.KtbsAPI;
import fr.supercomete.head.GameUtils.GameMode.ModeModifier.Groupable;
import fr.supercomete.head.GameUtils.Lag;
import fr.supercomete.head.core.KasterborousRunnable;
import fr.supercomete.head.core.Main;
import fr.supercomete.head.Exception.BadExtensionException;
import fr.supercomete.head.Exception.MalformedSchemaException;
import fr.supercomete.head.schema.schema.Schema;
import fr.supercomete.head.schema.utility.*;
import fr.supercomete.head.world.Scoreboard.SimpleScoreboard;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import java.io.*;
import java.lang.reflect.Field;
import java.util.*;
public final class ScoreBoardSchemaHandler {
    private static HashMap<UUID, SimpleScoreboard> boards = new HashMap<>();
    public static SimpleScoreboard get(Player player){
        check_valid(player);
        return  boards.get(player.getUniqueId());
    }
    private static void check_valid(Player player){
        if(!boards.containsKey(player.getUniqueId())){
            boards.put(player.getUniqueId(), new SimpleScoreboard(" "));
        }
    }
    public static void update(Schema schema,Schema header,Schema footer,Player player) {
        check_valid(player);
        final SimpleScoreboard ss = boards.get(player.getUniqueId());
        String[] main_evaluated = schema.evaluate(32,player);
        for(int i =1;i<15;i++){
            if(i<main_evaluated.length){
                ss.add(main_evaluated[i],15-i);
            }else{
                ss.fix(15-i);
            }

        }
        ss.setTitle(main_evaluated[0]);
        ss.update();
        ss.send(player);
        final StringBuilder builder_header= new StringBuilder();
        for(final String str:header.evaluate(32,player)){
            builder_header.append(str).append("\n");
        }
        builder_header.deleteCharAt(builder_header.length()-1);
        final StringBuilder builder_footer= new StringBuilder();
        for(final String str:footer.evaluate(32,player)){
            builder_footer.append(str).append("\n");
        }
        builder_footer.deleteCharAt(builder_footer.length()-1);
        sendHeadAndFooter(player,builder_header.toString(),builder_footer.toString());
    }


    public static void init() throws IOException, BadExtensionException, MalformedSchemaException {
        final SchemaEnvironment env = Main.scoreboardEnvironment;

        //Load every condition and variables

        //VARIABLES
        env.register(
                "tps",
                (SchemaVariable) player -> getTPS()+""
        );
        env.register(
                "gamemode",
                (SchemaVariable) player -> Main.currentGame.getMode().getName()
        );
        env.register(
                "gamename",
                (SchemaVariable) player -> Main.currentGame.getGameName()
        );
        env.register(
                "ping",
                (SchemaVariable) player -> getPing(player)+""
        );
        env.register(
                "host",
                (SchemaVariable) player -> getHost()+""
        );
        env.register(
                "serverid",
                (SchemaVariable) player -> getServerId()+""
        );
        env.register(
                "discordlink",
                (SchemaVariable) player -> getDiscordLink()+""
        );
        env.register(
                "playercount",
                (SchemaVariable) player -> Main.countnumberofplayer()+""
        );
        env.register(
                "slots",
                (SchemaVariable) player -> Main.currentGame.getMaxNumberOfplayer()+""
        );
        env.register(
                "kills",
                (SchemaVariable) player -> (Main.currentGame.getKillList().get(player.getUniqueId())==null)?"Aucun":Main.currentGame.getKillList().get(player.getUniqueId())+""
        );
        env.register(
                "groupsize",
                (SchemaVariable) player -> (Main.currentGame.getMode()instanceof Groupable)?Main.currentGame.getGroupe()+"":"0"
        );
        env.register(
                "time",
                (SchemaVariable) player -> Main.transformScoreBoardType(Main.currentGame.getTime(),"","")+""
        );
        env.register(
                "bordersize",
                (SchemaVariable) player -> ((int) (Main.currentGame.getFirstBorder()/2))+""
        );
        env.register(
                "servername",
                (SchemaVariable) player -> Main.INSTANCE.getConfig().getString("serverapi.serverconfig.servername")
        );
        //CONDITIONS
        env.register(
                "hasgroups",
                (SchemaCondition) player -> Main.currentGame.getMode()instanceof Groupable
        );

        //Load if possible
        final Schema tab_header_schema = SchemaFileHandler.load("tab-scoreboard-header.shma");
        final Schema tab_footer_schema = SchemaFileHandler.load("tab-scoreboard-footer.shma");
        final Schema main_scoreboard = SchemaFileHandler.load("main-scoreboard.shma");
        final KasterborousRunnable Scoreboardtask = new KasterborousRunnable(){
            @Override
            public String name() {
                return "KTBS-ScoreboardTask";
            }
            @Override
            public void onScoreBoardUpdate(KtbsAPI api) {
                for(final Player player : Bukkit.getOnlinePlayers()){
                    update(main_scoreboard,tab_header_schema,tab_footer_schema,player);
                }
            }
        };
        Bukkit.getServicesManager().load(KtbsAPI.class).getKTBSRunnableProvider().RegisterRunnable(new ArrayList<>(Collections.singletonList(Scoreboardtask)));
    }

    private static void sendHeadAndFooter(Player player, String head, String foot) {
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        try {
            Field header = packet.getClass().getDeclaredField("a");
            Field footer = packet.getClass().getDeclaredField("b");
            header.setAccessible(true);footer.setAccessible(true);
            header.set(packet, IChatBaseComponent.ChatSerializer.a("\"" + head + "\""));
            footer.set(packet, IChatBaseComponent.ChatSerializer.a("\"" + foot + "\""));
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
    private static String getTPS(){
        double tps = Lag.getTPS() * 100;
        tps = Math.round(tps);
        tps = tps / 100;
        String to_tps=(""+Math.min(tps+0.1,20));
        return to_tps.substring(0,Math.min(5,to_tps.length()));
    }
    private static int getPing(Player player) {
        return ((CraftPlayer) player).getHandle().ping;
    }
    private static String getServerId() {
        return Main.INSTANCE.ServerId;
    }
    private static String getDiscordLink(){
        return Main.INSTANCE.DiscordLink;
    }
    private static String getHost(){
        return((Main.host!=null&& Bukkit.getPlayer(Main.host)!=null)?Bukkit.getPlayer(Main.host).getName():"§cUnknown");
    }
}