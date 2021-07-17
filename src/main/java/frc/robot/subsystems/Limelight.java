// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Limelight extends SubsystemBase {
 
  private NetworkTable m_limelightTable;
  private static double tx;

  public void Vision() {
    m_limelightTable = NetworkTableInstance.getDefault().getTable("limelight");
  }

  @Override
  public void periodic() {
    // read values periodically
    tx = m_limelightTable.getEntry("tx").getDouble(0.0);
  }

  public static double getXOffset() {
    return tx;
  }
}
