package frc.robot.commands;

import com.ctre.phoenix.sensors.PigeonIMU;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.Limelight;
import frc.robot.PIDNTValue;
import frc.robot.subsystems.SwerveDrivetrain;

public class VisionAlignCommand extends CommandBase {
  SwerveDrivetrain drivetrain;
  private PIDController pid = new PIDController(Constants.VISION_ALIGN_P, Constants.VISION_ALIGN_I, Constants.VISION_ALIGN_D, 0.01);
  public VisionAlignCommand(SwerveDrivetrain drivetrain) {
    addRequirements(drivetrain);
    new PIDNTValue(Constants.VISION_ALIGN_P, Constants.VISION_ALIGN_I, Constants.VISION_ALIGN_D, pid, "Vision Align");
    this.drivetrain = drivetrain;
  }
  
  @Override
  public boolean isFinished() {
    double xTarget = Limelight.getTargetX();
    return Math.abs(xTarget) <= 1;
  }

  private static double getError() {
    return Limelight.getTargetX();
  }

  @Override
  public void execute() {
    double xTarget = Limelight.getTargetX();
    System.out.println(xTarget);
    double pidAngularVelocity = pid.calculate(0, -xTarget);
    drivetrain.drive(0, 0, pidAngularVelocity, true);
  }

  public static boolean isAligned() {
    final var error = getError();
    // If it is facing the goal and done rotating
    return error < 0.1 && error != 0 && SwerveDrivetrain.getInstance().getAngularVelocityRotation2d() < 0.5;
  }
}