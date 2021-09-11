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

public class TrenchAuto extends SequentialCommandGroup {

    public TrenchAuto(SwerveDrivetrain drive, ShooterSubsystem shooter, IndexerSubsystem indexer,
            IntakeSubsystem intake) {
        addRequirements(shooter);

        addCommands(
                // Drive from the starting line to the aiming spot while spinning up the shooter
                new FollowPathCommand(drive, "Start line towards trench and shoot 3"),
                // Pew pew
                new AutonomousAlignShootCommand(drive, shooter, indexer));
    }

}