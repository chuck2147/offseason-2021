package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.IntakeSubsystem;

public class ClimberToTopCommand extends CommandBase {
  private final ClimberSubsystem climber;
  private final IntakeSubsystem intake;

  public ClimberToTopCommand(ClimberSubsystem climber, IntakeSubsystem intake) {
    this.climber = climber;
    this.intake = intake;
    addRequirements(climber, intake);
  }  

  @Override
  public void initialize() {
    intake.extendIntake();
  }

  @Override
  public void execute() {
    double pidError = (Constants.climberPeakSensorVelocity - this.climber.climberMotor.getSelectedSensorVelocity())/100;
    //System.out.println(pidError);
    this.climber.climberMotor.set(TalonFXControlMode.Position, Constants.CLIMBERTARGET);
    climber.climberPistonOff();
  }

  @Override
  public boolean isFinished() {
    double encodervalue = this.climber.climberMotor.getSelectedSensorPosition();
    return Math.abs(encodervalue) >= Constants.CLIMBERTARGET;
  }
}
