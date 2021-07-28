package frc.robot;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class NTValue {
  public NTValue (double initialValue, String key) {
    ShuffleboardTab tab = Shuffleboard.getTab("NTValues");
    NetworkTableEntry entry = tab.add(key, initialValue)
      .withSize(2, 1)
      .withWidget(BuiltInWidgets.kTextView) // specify the widget here
      .getEntry();
    entry.addListener(event -> {
      value = entry.getValue().getDouble();
    }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
    value = initialValue;
  }
  public double value;
}
