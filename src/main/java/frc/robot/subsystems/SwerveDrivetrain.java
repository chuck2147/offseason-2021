// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.sensors.PigeonIMU;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.kinematics.SwerveDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.SwerveDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.util.Units;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.math.Rotation2;
import frc.robot.math.Vector2;

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
  private Pose2d pose = new Pose2d();
  private final double SCALE = 100 / 2.54;
  private final NetworkTableInstance nt = NetworkTableInstance.getDefault();
  private final NetworkTable currentPoseTable = nt.getTable("/pathFollowing/current");
  private final NetworkTableEntry currentXEntry = currentPoseTable.getEntry("x");
  private final NetworkTableEntry currentYEntry = currentPoseTable.getEntry("y");
  private final NetworkTableEntry currentAngleEntry = currentPoseTable.getEntry("angle");
  private static final SwerveDrivetrain instance;
  
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

  SwerveDriveOdometry odometry = new SwerveDriveOdometry(kinematics,
    getYaw(), new Pose2d(5.0, 13.5, new Rotation2d()));
  // TODO: Update these CAN device IDs to match your TalonFX + CANCoder device IDs
  // TODO: Update module offsets to match your CANCoder offsets
  private SwerveModuleMK3[] modules = new SwerveModuleMK3[] {
    new SwerveModuleMK3(new TalonFX(1), new TalonFX(2), new CANCoder(1), Rotation2d.fromDegrees(Constants.frontLeftOffset)), // Front Left
    new SwerveModuleMK3(new TalonFX(3), new TalonFX(4), new CANCoder(2), Rotation2d.fromDegrees(Constants.frontRightOffset)), // Front Right
    new SwerveModuleMK3(new TalonFX(5), new TalonFX(6), new CANCoder(3), Rotation2d.fromDegrees(Constants.backLeftOffset)), // Back Left
    new SwerveModuleMK3(new TalonFX(7), new TalonFX(8), new CANCoder(4), Rotation2d.fromDegrees(Constants.backRightOffset))  // Back Right
  };

  SwerveModuleState[] states;

  static {
    instance = new SwerveDrivetrain();
  }

  public SwerveDrivetrain() {
   pigeon.setYaw(0, 100);  //gyro reset (angle deg, timeoutsMs)   
  }
  
  public Rotation2d getYaw() {
    double[] ypr = new double[3];
    pigeon.getYawPitchRoll(ypr);
    return Rotation2d.fromDegrees(ypr[0]);
  }

  public static SwerveDrivetrain getInstance() {
    return instance;
  }

  public double getAngularVelocity() {
    double[] angularVelocities = new double[3];
    pigeon.getRawGyro(angularVelocities); 
    return angularVelocities[2];
  }

  private Pose2d getPose() {
    return pose;
  }
  
  public Pose2d getScaledPose() {
    final var pose = getPose();
    final var translation = pose.getTranslation().times(SCALE);
    final var rotation = pose.getRotation().rotateBy(new Rotation2d(Math.PI / 2));

    return new Pose2d(-translation.getY(), translation.getX(), rotation.times(-1));
  }

  public void resetPose(Vector2 translation, Rotation2 angle) {
    System.out.println("Reset Pose");
    resetGyroAngle(angle);
    odometry.resetPosition(
      new Pose2d(
        //coordinates switched x is forward, y is left and right.
        // Converting to unit system of path following which uses x for right and left
        new Translation2d(translation.y / SCALE, -translation.x / SCALE),
        new Rotation2d(angle.toRadians())
      ),
      getYaw()
    );
    pose = odometry.getPoseMeters();
    updatePoseNT();
  }
  private void updatePoseNT() {
    final var pose = getScaledPose();
    // System.out.println(pose);

    currentAngleEntry.setDouble(pose.getRotation().getRadians());
    currentXEntry.setDouble(pose.getX());
    currentYEntry.setDouble(pose.getY());

  }

  public void resetGyroAngle(Rotation2 angle) {
    System.out.println("Reset Gyro Angle");
    pigeon.setYaw(angle.toDegrees(), 100);  //gyro reset (angle deg, timeoutsMs)   
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
    states =
      kinematics.toSwerveModuleStates(
        fieldRelative
          ? ChassisSpeeds.fromFieldRelativeSpeeds(xSpeed, ySpeed, rot, getYaw().times(-1)) 
          : new ChassisSpeeds(xSpeed, ySpeed, rot));
    SwerveDriveKinematics.normalizeWheelSpeeds(states, kMaxSpeed);
    for (int i = 0; i < states.length; i++) {
      SwerveModuleMK3 module = modules[i];
      SwerveModuleState state = states[i];
      module.setDesiredState(state);
    }
  }

  @Override
  public void periodic() {
    if (states != null) {
      pose = odometry.update(getYaw(), states);
      updatePoseNT();
    }
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
