package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.IntakeSubsystem;

public class ClimberAllUpCommand extends CommandBase {
  private final ClimberSubsystem climber;
  private final IntakeSubsystem intake;
  private PIDController pid = new PIDController(Constants.CLIMBER_ALL_UP_P, Constants.CLIMBER_ALL_UP_I, Constants.CLIMBER_ALL_UP_D, 0.01);
  private TalonFX climberMotor;

  public ClimberAllUpCommand(ClimberSubsystem climber, IntakeSubsystem intake) {
    this.climber = climber;
    this.intake = intake;
    this.climber.climberMotor = climberMotor;
    addRequirements(climber, intake);
  }  

  @Override
  public void initialize() {
    intake.extendIntake();
  }

  @Override
  public void execute(){
    double pidError = (Constants.climberPeakSensorVelocity - climberMotor.getSelectedSensorVelocity())/100;
    double encodervalue = climberMotor.getSelectedSensorPosition();
    double heightTarget = Constants.climberDistance - encodervalue;
    double pidUpVelocity = pid.calculate(0, heightTarget);
    System.out.println(pidError);
    climberMotor.set(ControlMode.PercentOutput, pidUpVelocity);
    climber.climberPistonOff();
  }

  @Override
  public boolean isFinished() {
    double encodervalue = climberMotor.getSelectedSensorPosition();
    double heightTarget = Constants.climberDistance - encodervalue;
    return Math.abs(heightTarget) <= 1;
  }
}
