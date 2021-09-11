package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.subsystems.IndexerSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

/**
 * In parallel spin up the shooter wheels and vision align (timeout 1.5). Once
 * both are done, feeds balls into shooter for 3 seconds
 */
public class AutonomousAlignShootCommand extends SequentialCommandGroup {
  public AutonomousAlignShootCommand(SwerveDrivetrain drive, ShooterSubsystem shooter, IndexerSubsystem indexer) {

    addCommands(
      new PrintCommand("Waiting for vision align"),
      new VisionAlignCommand(drive).withTimeout(2),
      new InstantCommand(() -> drive.drive(0, 0, 0, false), drive),
      new PrintCommand("Shooting"),
      new RunCommand(shooter::shootFromBehindLine, shooter).withTimeout(5),
      new PrintCommand("Done Shooting"),
      new InstantCommand(shooter::stopShooter, shooter)
    );

  }
}
