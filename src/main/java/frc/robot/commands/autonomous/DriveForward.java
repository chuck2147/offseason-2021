package frc.robot.commands.autonomous;

import frc.robot.math.Vector2;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.SwerveDrivetrain;

public class DriveForward extends CommandBase {
    private final SwerveDrivetrain m_driveSubsystem;
    private double endTime;

    public DriveForward(SwerveDrivetrain driveSubsystem) {
        m_driveSubsystem = driveSubsystem;
        addRequirements(m_driveSubsystem);
    }

    @Override
    public void initialize() {
        endTime = Timer.getFPGATimestamp() + 1;
    }

    @Override
    public void execute() {
        m_driveSubsystem.drive(0, 0.5,  0, false);
    }

    @Override
    public boolean isFinished() {
        return Timer.getFPGATimestamp() > endTime;
    }
}