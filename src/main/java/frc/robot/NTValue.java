package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class NTValue {
  public NTValue (int initialValue, String key) {
    // NetworkTable table = NetworkTableInstance.getDefault().getTable("/");
    // NetworkTableEntry entry = table.getEntry(key);
    // SmartDashboard.putNumber(key, initialValue);
    ShuffleboardTab tab = Shuffleboard.getTab("Subsystems");
    NetworkTableEntry entry = tab.add(key, initialValue).getEntry();
    // entry.setValue(initialValue);
    value = initialValue;
  }
  public int value;
}
