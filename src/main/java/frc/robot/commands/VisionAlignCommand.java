package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Limelight;
import frc.robot.subsystems.SwerveDrivetrain;

public class VisionAlignCommand extends CommandBase {
  SwerveDrivetrain drivetrain;
  public VisionAlignCommand(SwerveDrivetrain drivetrain) {
    this.drivetrain = drivetrain;
    addRequirements(drivetrain);
    
  }

  @Override
  public void execute() {
    double xTarget = -Limelight.getTargetX() * 0.1;
    drivetrain.drive(0, 0, xTarget, true);
  } 
}