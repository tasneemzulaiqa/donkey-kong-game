import java.util.Properties;
import java.util.ArrayList;

/**
 * A utility class responsible for loading game objects from a properties file
 * and positioning them correctly in the game world.
 */
public class ObjectLoader {

    /**
     * Loads platform objects for a specific level.
     *
     * @param props Properties file
     * @param level The level number to load platforms for
     * @return An array of Platform objects.
     */
    public static Platform[] loadPlatforms(Properties props, int level) {
        String key = "platforms.level" + level;
        String[] platformData = props.getProperty(key).split(";");
        Platform[] platforms = new Platform[platformData.length];

        for (int i = 0; i < platformData.length; i++) {
            String[] coords = platformData[i].split(",");
            double x = Double.parseDouble(coords[0]);
            double y = Double.parseDouble(coords[1]);
            platforms[i] = new Platform(x, y);
        }
        return platforms;
    }

    /**
     * Loads ladders for a given level and aligns them if initial position overlaps any platform.
     *
     * @param props Properties file containing ladder definitions.
     * @param platforms Platforms used for alignment.
     * @param level The level number.
     * @return An array of Ladder objects.
     */
    public static Ladder[] loadLadders(Properties props, Platform[] platforms, int level) {
        int count = Integer.parseInt(props.getProperty("ladder.level" + level + ".count"));
        Ladder[] ladders = new Ladder[count];

        for (int i = 0; i < count; i++) {
            String[] coords = props.getProperty("ladder.level" + level + "." + (i + 1)).split(",");
            double x = Double.parseDouble(coords[0]);
            double y = Double.parseDouble(coords[1]);
            ladders[i] = new Ladder(x, y);
            reposition(ladders[i], platforms);
        }

        return ladders;
    }

    /**
     * Loads barrels for the specified level and repositions them if initial position overlaps any platform.
     *
     * @param props Properties containing barrel definitions.
     * @param platforms Platforms used for alignment.
     * @param level The level number.
     * @return An array of {@link Barrel} objects.
     */
    public static Barrel[] loadBarrels(Properties props, Platform[] platforms, int level) {
        int count = Integer.parseInt(props.getProperty("barrel.level" + level + ".count"));
        Barrel[] barrels = new Barrel[count];

        for (int i = 0; i < count; i++) {
            String[] coords = props.getProperty("barrel.level" + level + "." + (i + 1)).split(",");
            double x = Double.parseDouble(coords[0]);
            double y = Double.parseDouble(coords[1]);
            barrels[i] = new Barrel(x, y);
            reposition(barrels[i], platforms);
        }
        return barrels;
    }

    /**
     * Loads hammers (weapons) for the level and positions them on platforms.
     *
     * @param props Properties file containing hammer data.
     * @param platforms Platforms for placement reference.
     * @param level The level number.
     * @return A list of Weapon objects representing hammers.
     */
    public static ArrayList<Weapon> loadHammer(Properties props, Platform[] platforms, int level) {
        int count = Integer.parseInt(props.getProperty("hammer.level" + level + ".count"));
        ArrayList<Weapon> hammers = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            String[] coords = props.getProperty("hammer.level" + level + "." + (i + 1)).split(",");
            double x = Double.parseDouble(coords[0]);
            double y = Double.parseDouble(coords[1]);
            Hammer hammer = new Hammer(x, y);
            hammers.add(hammer);

            reposition(hammer, platforms);
        }

        return hammers;
    }

    /**
     * Loads Mario's starting position and repositions him onto a platform.
     *
     * @param props Properties containing Mario's coordinates.
     * @param platforms Platforms for alignment.
     * @param level The level number.
     * @return A mario object
     */
    public static Mario loadMario(Properties props, Platform[] platforms, int level) {
        String[] coords = props.getProperty("mario.level" + level).split(",");
        double x = Double.parseDouble(coords[0]);
        double y = Double.parseDouble(coords[1]);
        Mario mario = new Mario(x, y);
        reposition(mario, platforms);
        return mario;
    }

    /**
     * Loads Donkey Kong's starting position and repositions him.
     *
     * @param props Properties containing Donkey Kong's coordinates.
     * @param platforms Platforms for alignment.
     * @param level The level number.
     * @return A DonkeyKong object.
     */
    public static DonkeyKong loadDonkeyKong(Properties props, Platform[] platforms, int level) {
        String[] coords = props.getProperty("donkey.level" + level).split(",");
        double x = Double.parseDouble(coords[0]);
        double y = Double.parseDouble(coords[1]);
        DonkeyKong dk = new DonkeyKong(x, y);
        reposition(dk, platforms);
        return dk;
    }

    /**
     * Loads all monkey enemies (normal and intelligent) for the level and repositions them.
     *
     * @param props Properties containing monkey definitions.
     * @param platforms Platforms for positioning reference.
     * @param level The level number.
     * @return A list of monkey objects containing both normal and smart monkey.
     */
    public static ArrayList<Monkey> loadMonkeys(Properties props, Platform[] platforms, int level) {
        ArrayList<Monkey> allMonkey = new ArrayList<>();

        // Load normal monkeys
        int normMonkCount = Integer.parseInt(props.getProperty("normalMonkey.level" + level + ".count"));
        for (int i = 0; i < normMonkCount; i++) {
            String[] parts = props.getProperty("normalMonkey.level" + level + "." + (i + 1)).split(";");
            String[] coords = parts[0].split(",");
            double x = Double.parseDouble(coords[0]);
            double y = Double.parseDouble(coords[1]);
            boolean facingLeft = parts[1].equals("left");

            String[] routeStr = parts[2].split(",");
            int[] route = new int[routeStr.length];
            for (int j = 0; j < routeStr.length; j++) {
                route[j] = Integer.parseInt(routeStr[j]);
            }

            NormalMonkey normal = new NormalMonkey(x, y, facingLeft, route, platforms);
            reposition(normal, platforms);
            allMonkey.add(normal);
        }

        // Load smart monkeys
        int smartMonkCount = Integer.parseInt(props.getProperty("intelligentMonkey.level" + level + ".count"));
        for (int i = 0; i < smartMonkCount; i++) {
            String[] parts = props.getProperty("intelligentMonkey.level" + level + "." + (i + 1)).split(";");
            String[] coords = parts[0].split(",");
            double x = Double.parseDouble(coords[0]);
            double y = Double.parseDouble(coords[1]);
            boolean facingLeft = parts[1].equals("left");

            String[] routeStr = parts[2].split(",");
            int[] route = new int[routeStr.length];
            for (int j = 0; j < routeStr.length; j++) {
                route[j] = Integer.parseInt(routeStr[j]);
            }
            SmartMonkey smart = new SmartMonkey(x, y, facingLeft, route, platforms);
            reposition(smart, platforms);
            allMonkey.add(smart);
        }
        return allMonkey;
    }

    /**
     * Loads blasters for the level and repositions them.
     *
     * @param props Properties file containing blaster data.
     * @param platforms Platforms used for placement.
     * @param level The level number.
     * @return A list of Weapon objects representing blasters.
     */
    public static ArrayList<Weapon> loadBlaster(Properties props, Platform[] platforms, int level) {
        int count = Integer.parseInt(props.getProperty("blaster.level" + level + ".count"));
        ArrayList<Weapon> blasters = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            String[] coords = props.getProperty("blaster.level" + level + "." + (i + 1)).split(",");
            double x = Double.parseDouble(coords[0]);
            double y = Double.parseDouble(coords[1]);
            Blaster blaster = new Blaster(x, y);
            blasters.add(blaster);
            reposition(blaster, platforms);
        }
        return blasters;
    }

    /**
     * Combines multiple arrays of GameObject into one.
     *
     * @param arrays A 2D array containing multiple GameObject arrays.
     * @return A single combined array of GameObject.
     */
    public static GameObject[] combineObjects(GameObject[][] arrays) {
        int totalLength = 0;
        for (GameObject[] array : arrays) {
            totalLength += array.length;
        }
        GameObject[] combined = new GameObject[totalLength];
        int index = 0;
        for (GameObject[] array : arrays) {
            for (GameObject object : array) {
                combined[index++] = object;
            }
        }
        return combined;
    }

    /**
     * Repositions a game object vertically so that it rests on the top of a platform
     * if it is intersecting within a small vertical margin.
     *
     * @param obj The GameObject to reposition.
     * @param platforms Array of platforms to check against.
     */
    private static void reposition(GameObject obj, Platform[] platforms) {
        for (Platform plat : platforms) {

            // Check if bottom of object is intersecting top of platform
            double objBottomY = obj.getY() + obj.getImage().getHeight() / 2.0;
            double platformTopY = plat.getY() - plat.getImage().getHeight() / 2.0;

            // Use a small margin of error to determine intersection
            if (Math.abs(objBottomY - platformTopY) <= 5 && obj.getX() >= plat.getX() - plat.getImage().getWidth() / 2.0 &&
                    obj.getX() <= plat.getX() + plat.getImage().getWidth() / 2.0) {

                // Reposition the ladder so its bottom sits just on the platform
                double newY = platformTopY - obj.getImage().getHeight() / 2.0;
                obj.setY(newY);
                break;
            }
        }
    }
}

