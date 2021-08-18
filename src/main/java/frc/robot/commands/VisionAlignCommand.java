package frc.robot.commands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.Limelight;
import frc.robot.PIDNTValue;
import frc.robot.subsystems.SwerveDrivetrain;

public class VisionAlignCommand extends CommandBase {
  SwerveDrivetrain drivetrain;
  private PIDController pid = new PIDController(Constants.VISION_ALIGN_P, Constants.VISION_ALIGN_I, Constants.VISION_ALIGN_D);
  public VisionAlignCommand(SwerveDrivetrain drivetrain) {
    addRequirements(drivetrain);
    new PIDNTValue(Constants.VISION_ALIGN_P, Constants.VISION_ALIGN_I, Constants.VISION_ALIGN_D, pid, "Vision Align");
  }
  
  @Override
  public boolean isFinished() {
    double xTarget = Limelight.getTargetX();
    return Math.abs(xTarget) <= 1;
  }

  @Override
  public void execute() {
    double xTarget = Limelight.getTargetX();
    double pidAngularVelocity = pid.calculate(0, xTarget);
    drivetrain.drive(0, 0, pidAngularVelocity, true);
    pid.setPID(Constants.VISION_ALIGN_P, Constants.VISION_ALIGN_I, Constants.VISION_ALIGN_D);
  }
}