package Classes.Utils;


/**
 * Created by SM David on 30/09/2018.
 */
public class Utils {

    public static String typeToRegex(String type)
    {
        if(type == null || type.isEmpty())
            return "";

        String[] types = type.split(";");
        String ans = "";
        for(int i=0;i<types.length;i++)
        {
            if(isValidStr(types[i]))
                ans += types[i] + (i<types.length-1 ? "|": "");

        }
        return ans;

    }

    public static boolean isValidStr(String str) {
        return str.matches("[0-9a-zA-Z]+");
    }

    public static boolean isAlpha(String str) {
        return str.matches("[a-zA-Z]+");
    }


}
