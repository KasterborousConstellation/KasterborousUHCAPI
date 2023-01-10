package fr.supercomete.head.GameUtils.Time;

public class TimeUtility {
    public static String transform(int v,String color){
        return transform(v,color,color,color);
    }
    public static String transform(int v, String MinutesColor, String SecondsColor, String NumberColor) {
        int[] e = { v / 60, v % 60 };
        return NumberColor + e[0] + MinutesColor + "m" + NumberColor + e[1] + SecondsColor + "s";
    }
}