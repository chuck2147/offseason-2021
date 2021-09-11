package frc.robot.commands.autonomous;


import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.AutonomousAlignShootCommand;
import frc.robot.commands.FollowPathCommand;
import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.subsystems.IndexerSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

public class TrenchAuto extends SequentialCommandGroup {

    public ShootandDriveForward(SwerveDrivetrain drive, ShooterSubsystem shooter, IndexerSubsystem indexer) {
        addRequirements(shooter);

        addCommands(
                //pew pew
                new AutonomousAlignShootCommand(drive, shooter, indexer).withTimeout(5),
                // moving
                new DriveForward(drive)
                );
                
                
    }

}