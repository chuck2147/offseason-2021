package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.Constants.ClimberState;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.IntakeSubsystem;

public class ClimberAllUpCommand extends CommandBase {
  private final ClimberSubsystem climber;
  private final IntakeSubsystem intake;
  private final Constants.ClimberState climberState;
  private PIDController pid = new PIDController(Constants.CLIMBER_ALL_UP_P, Constants.CLIMBER_ALL_UP_I, Constants.CLIMBER_ALL_UP_D, 0.01);
  private final TalonFX climberMotor = new TalonFX(Constants.CLIMB_MOTOR);

  public ClimberAllUpCommand(ClimberSubsystem climber, IntakeSubsystem intake, Constants.ClimberState climberState) {
    this.climber = climber;
    this.intake = intake;
    this.climberState = climberState;
    addRequirements(climber, intake);
  }  

  @Override
  public void initialize() {
    intake.extendIntake();
  }

  @Override
  public void execute(){
    double heightTarget = (Constants.climberPeakSensorVelocity - climberMotor.getSelectedSensorVelocity())/100;
    double pidUpVelocity = pid.calculate(0, heightTarget);

    if (climberState == ClimberState.UpAll) {
    climberMotor.set(ControlMode.PercentOutput, pidUpVelocity);
    }

    climber.climberPistonOff();
  }

  @Override
  public boolean isFinished() {
    double heightTarget = (Constants.climberPeakSensorVelocity - climberMotor.getSelectedSensorVelocity())/100;
    return Math.abs(heightTarget) <= 1;
  }
}
