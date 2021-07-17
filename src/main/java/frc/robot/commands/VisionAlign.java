// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.controller.ProfiledPIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.ProfiledPIDCommand;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.SwerveDrivetrain;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class VisionAlign extends ProfiledPIDCommand {
  /** Creates a new VisionAlign. */
  public VisionAlign(SwerveDrivetrain drive) {
    super(
        // The ProfiledPIDController used by the command
        new ProfiledPIDController(
            // The PID gains
            .01,
            0,
            0,
            // The motion profile constraints
            new TrapezoidProfile.Constraints(100,300)),  //kMaxTurnRateDegPerS , kMaxTurnAccelerationDegPerSSquared
        // This should return the measurement
        Limelight::getXOffset,
        // This should return the goal (can also be a constant)
        () -> new TrapezoidProfile.State(),
        // This uses the output
        (output, setpoint) -> drive.drive(0, 0, output, true), drive 
          // Use the output (and setpoint, if desired) here
        );

        // Set the controller to be continuous (because it is an angle controller)
        getController().enableContinuousInput(-180, 180);
       // Set the controller tolerance - the delta tolerance ensures the robot is stationary at the
       // setpoint before it is considered as having reached the reference
       getController()
        .setTolerance(1, 30); //kTurnToleranceDeg, kTurnRateToleranceDegPerS

        
    // Use addRequirements() here to declare subsystem dependencies.
    // Configure additional PID options by calling `getController` here.
    SmartDashboard.putBoolean("Vision Aligned", getController().atGoal());
    SmartDashboard.putNumber("Vision AlignX", Limelight.getXOffset());
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
