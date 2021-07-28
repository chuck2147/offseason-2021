// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.sensors.PigeonIMU;

import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.kinematics.SwerveDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Units;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.NTValue;

public class SwerveDrivetrain extends SubsystemBase {

  public static final double kMaxSpeed = Units.feetToMeters(16.2); // 16.2 feet per second
  public static final double kMaxAngularSpeed = Math.PI; // 1/2 rotation per second

  /**
   * TODO: These are example values and will need to be adjusted for your robot!
   * Modules are in the order of -
   * Front Left
   * Front Right
   * Back Left
   * Back Right
   * 
   * Positive x values represent moving toward the front of the robot whereas
   * positive y values represent moving toward the left of the robot
   * https://docs.wpilib.org/en/stable/docs/software/kinematics-and-odometry/swerve-drive-kinematics.html#constructing-the-kinematics-object
   */

  PigeonIMU pigeon = new PigeonIMU(20);  //CAN Id for gyro
  int _loopCount = 0;

  // width and length are switched, we are too lazy to figure out which way it should be
  double length = 18;
  double width = 19.75;
  
  private SwerveDriveKinematics kinematics = new SwerveDriveKinematics(
    // Locations for the swerve drive modules relative to the robot center. 
    //Positive x values represent moving toward the front of the robot whereas positive y values represent moving toward the left of the robot.
    //old value 12.75
    new Translation2d(
      Units.inchesToMeters(width/2),
      Units.inchesToMeters(length/2)
    ),
    new Translation2d(
      Units.inchesToMeters(width/2),
      Units.inchesToMeters(-length/2)
    ),
    new Translation2d(
      Units.inchesToMeters(-width/2),
      Units.inchesToMeters(length/2)
    ),
    new Translation2d(
      Units.inchesToMeters(-width/2),
      Units.inchesToMeters(-length/2)
    )
  );

 
  // TODO: Update these CAN device IDs to match your TalonFX + CANCoder device IDs
  // TODO: Update module offsets to match your CANCoder offsets
  private SwerveModuleMK3[] modules = new SwerveModuleMK3[] {
    new SwerveModuleMK3(new TalonFX(1), new TalonFX(2), new CANCoder(1), Rotation2d.fromDegrees(Constants.frontLeftOffset)), // Front Left
    new SwerveModuleMK3(new TalonFX(3), new TalonFX(4), new CANCoder(2), Rotation2d.fromDegrees(Constants.frontRightOffset)), // Front Right
    new SwerveModuleMK3(new TalonFX(5), new TalonFX(6), new CANCoder(3), Rotation2d.fromDegrees(Constants.backLeftOffset)), // Back Left
    new SwerveModuleMK3(new TalonFX(7), new TalonFX(8), new CANCoder(4), Rotation2d.fromDegrees(Constants.backRightOffset))  // Back Right
  };

  public SwerveDrivetrain() {
   pigeon.setYaw(0, 100);  //gyro reset (angle deg, timeoutsMs)   
  }
  
  public double getYaw(){
    double[] ypr = new double[3];
    pigeon.getYawPitchRoll(ypr);
    return ypr[0];
  }


  /**
   * Method to drive the robot using joystick info.
   *
   * @param xSpeed Speed of the robot in the x direction (forward).
   * @param ySpeed Speed of the robot in the y direction (sideways).
   * @param rot Angular rate of the robot.
   * @param fieldRelative Whether the provided x and y speeds are relative to the field.
   */
  
  public void drive(double xSpeed, double ySpeed, double rot, boolean fieldRelative) {

    // var modules = new SwerveModuleMK3[] {
    //   new SwerveModuleMK3(new TalonFX(1), new TalonFX(2), new CANCoder(1), Rotation2d.fromDegrees(Constants.frontLeftOffset.value)), // Front Left
    //   new SwerveModuleMK3(new TalonFX(3), new TalonFX(4), new CANCoder(2), Rotation2d.fromDegrees(Constants.frontRightOffset.value)), // Front Right
    //   new SwerveModuleMK3(new TalonFX(5), new TalonFX(6), new CANCoder(3), Rotation2d.fromDegrees(Constants.backLeftOffset.value)), // Back Left
    //   new SwerveModuleMK3(new TalonFX(7), new TalonFX(8), new CANCoder(4), Rotation2d.fromDegrees(Constants.backRightOffset.value))  // Back Right
    // };

    SwerveModuleState[] states =
      kinematics.toSwerveModuleStates(
        fieldRelative
          ? ChassisSpeeds.fromFieldRelativeSpeeds(xSpeed, ySpeed, rot, Rotation2d.fromDegrees(-getYaw())) 
          : new ChassisSpeeds(xSpeed, ySpeed, rot));
    SwerveDriveKinematics.normalizeWheelSpeeds(states, kMaxSpeed);
    for (int i = 0; i < states.length; i++) {
      SwerveModuleMK3 module = modules[i];
      SwerveModuleState state = states[i];
      module.setDesiredState(state);
  
        
      
      SmartDashboard.putNumber("gyro Angle", getYaw());
      
    }
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
