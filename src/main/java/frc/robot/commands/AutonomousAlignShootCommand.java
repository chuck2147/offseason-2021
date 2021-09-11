package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.subsystems.IndexerSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.ShooterSubsystem.ShooterDistances;

/**
 * In parallel spin up the shooter wheels and vision align (timeout 1.5). Once
 * both are done, feeds balls into shooter for 3 seconds
 */
public class AutonomousAlignShootCommand extends SequentialCommandGroup {
  public AutonomousAlignShootCommand(SwerveDrivetrain drive, ShooterSubsystem shooter, IndexerSubsystem indexer) {

    addCommands(sequence(
      // Wait for shooter to be at speed
      new PrintCommand("Waiting for shooter wheels"), new WaitUntilCommand(shooter::isOnTarget),
      // Wait for vision to be aligned
      new PrintCommand("Waiting for vision align"), new WaitUntilCommand(VisionAlignCommand::isAligned),
      // Then feed balls into shooter for 2 seconds
      new PrintCommand("Feeding balls"), new RunCommand(indexer::feedToShooter).withTimeout(2))
        // DeadlineWith means AutonomousAlignShootCommand will end when the above
        // sequence ends,
        // And that it will run the below commands in parallel
        // But it won't wait for the below commands to finish before moving on
        .deadlineWith(
                // At the same time align
                new VisionAlignCommand(drive),
                // And spin up shooter
                new RunCommand(() -> shooter.shootFromBehindLine(), shooter)

        ));

  }
}
