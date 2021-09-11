package frc.robot.commands.autonomous;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.AutonomousAlignShootCommand;
import frc.robot.commands.FollowPathCommand;
import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.subsystems.IndexerSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.ShooterSubsystem.ShooterDistances;

public class TrenchAuto extends SequentialCommandGroup {

    public TrenchAuto(SwerveDrivetrain drive, ShooterSubsystem shooter, IndexerSubsystem indexer,
            IntakeSubsystem intake) {
        addRequirements(shooter);

        addCommands(
                // Drive from the starting line to the aiming spot while spinning up the shooter
                new FollowPathCommand(drive, "Start line towards trench and shoot 3")
                        .deadlineWith(new RunCommand(() -> shooter.shootFromBehindLine(), shooter)),
                // Fold down intake
                new InstantCommand(intake::extendIntake),
                // Pew pew
                new AutonomousAlignShootCommand(drive, shooter, indexer),
                // Run the intake continuously while driving and grabbing 3 trench balls
                new FollowPathCommand(drive, "Aim to 3 from trench").deadlineWith(new RunCommand(intake::runIntake)),
                // Drive back to aiming spot while spinning up the shooter again
                new FollowPathCommand(drive, "3 from trench to aim")
                        .deadlineWith(new RunCommand(() -> shooter.shootFromBehindLine(), shooter)),
                // Pew pew
                new AutonomousAlignShootCommand(drive, shooter, indexer),
                // Fold up intake
                new InstantCommand(intake::retractIntake),
                // Stop intake wheels
                new InstantCommand(intake::stopIntake));
    }

}