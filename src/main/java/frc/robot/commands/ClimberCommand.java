package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.ClimberState;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.Constants;

public class ClimberCommand extends CommandBase {
  private final ClimberSubsystem climber;
  private final IntakeSubsystem intake;
  private final Constants.ClimberState climberState;

  public ClimberCommand(ClimberSubsystem climber, IntakeSubsystem intake, Constants.ClimberState climberState) {
    this.intake = intake;
    this.climber = climber;
    this.climberState = climberState;
    addRequirements(climber, intake);

  }

  @Override
  public void initialize() {
    intake.extendIntake();
  }

  @Override
  public void execute(){
    if (climberState == ClimberState.Down) {
      climber.reverseClimber();
    } else if (climberState == ClimberState.Up) {
      climber.runClimber();
    } 
    climber.climberPistonOff();
  }

  @Override
  public void end(boolean interrupted) {
    climber.stopClimber();
    climber.climberPistonOn();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
