package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Limelight {
    private static NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    private static NetworkTableEntry ledMode = table.getEntry("ledMode");

    public static void enableTracking() {
        // camMode.setNumber(0);
        ledMode.setNumber(3);
    }

    public static void disableTracking() {
        // camMode.setNumber(1);
        ledMode.setNumber(1);
    }

    public static boolean hasTarget() {
        return table.getEntry("tv").getBoolean(true);
    }

    public static double getTargetX() {
        return table.getEntry("tx").getNumber(0).doubleValue();
    } 
    
    public static double getTargetY() {
        return table.getEntry("ty").getNumber(0).doubleValue();
    }
}