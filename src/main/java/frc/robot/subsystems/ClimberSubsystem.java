// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ClimberSubsystem extends SubsystemBase {
  private final TalonFX climberMotor = new TalonFX(Constants.CLIMB_MOTOR);
  private final DoubleSolenoid climberPiston = new DoubleSolenoid(Constants.CLIMBER_AIR_IN,Constants.CLIMBER_AIR_OUT);

  public ClimberSubsystem() {
  
    
    climberMotor.configFactoryDefault();
    climberMotor.setNeutralMode(NeutralMode.Brake);
    climberMotor.setInverted(TalonFXInvertType.CounterClockwise); 
  }


  public void stopClimber() {
    climberMotor.set(ControlMode.PercentOutput, 0);
  }

  public void runClimber() {
    climberMotor.set(ControlMode.PercentOutput, 1);
  }

  public void reverseClimber() {
    climberMotor.set(ControlMode.PercentOutput, -1);
  }

  public void climberPistonOn() {
    climberPiston.set(Value.kReverse);
  }

  public void climberPistonOff() {
    climberPiston.set(Value.kForward);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
