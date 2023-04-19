package fr.supercomete.head.API;


import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Helper implements CoordinateHelper{
    @Override
    public double Distance(Player player1, Player player2) {
        return player1.getWorld().equals(player2.getWorld())?player1.getLocation().distance(player2.getLocation()):Double.MAX_VALUE;
    }

    @Override
    public String getDirectionArrow(Location locationn, Location Toward){
        if(Toward==null|| !locationn.getWorld().equals(Toward.getWorld())){
            return "§c???";
        }
        final double pi = Math.PI;
        final Vector vector = locationn.getDirection();
        vector.setY(0);
        final Vector direction = new Vector(Toward.getX()-locationn.getX(),0,Toward.getZ()-locationn.getZ());
        final Vector direction_90 = new Vector(direction.getZ(),0,-direction.getX());
        final double discriminant = vector.angle(direction_90);
        double angle = direction.angle(vector);
        if(discriminant>pi/2.0){
            angle = 2*pi -angle;
        }
        if(angle< pi/8.0) {
            return "§b⬆";
        }else if(angle<(3.0*pi)/8.0){
            return "§b⬈";
        }else if(angle<(5.0*pi)/8.0){
            return "§b➡";
        }else if(angle< (7.0*pi)/8.0) {
            return "§b⬊";
        }else if(angle<(9.0*pi)/8.0) {
            return "§b⬇";
        }else if(angle<(11.0*pi)/8.0){
            return "§b⬋";
        }else if(angle<(13.0*pi)/8.0){
            return "§b⬅";
        }else if(angle<(15.0*pi)/8.0){
            return "§b⬉";
        }else{
            return "§b⬆";
        }
    }
}