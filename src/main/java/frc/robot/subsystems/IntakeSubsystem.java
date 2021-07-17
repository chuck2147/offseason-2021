// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class IntakeSubsystem extends SubsystemBase {
  private CANSparkMax intakeMotor; 
  
  private final DoubleSolenoid intakeLeftPiston = new DoubleSolenoid(Constants.INTAKE_LEFT_AIR_IN, Constants.INTAKE_LEFT_AIR_OUT);
  private final DoubleSolenoid intakeRightPiston = new DoubleSolenoid(Constants.INTAKE_RIGHT_AIR_IN, Constants.INTAKE_RIGHT_AIR_OUT);
  
public IntakeSubsystem() {
    intakeMotor = new CANSparkMax(Constants.INTAKE_MOTOR, MotorType.kBrushless);
    intakeMotor.restoreFactoryDefaults();
    intakeMotor.setIdleMode(IdleMode.kCoast);
    intakeMotor.setInverted(true);
}

  public void stopIntake() {
   intakeMotor.set(0);
  }

  public void runIntake() {
    intakeMotor.set(Constants.INTAKE_SPEED);
   }

  public void reverseIntake() {
    intakeMotor.set(-.8);
   }

  public void extendIntake() {
    intakeRightPiston.set(Value.kReverse);
    intakeLeftPiston.set(Value.kReverse);
  }

  public void retractIntake() {
    intakeRightPiston.set(Value.kForward);
    intakeLeftPiston.set(Value.kForward);
  }

    @Override
  public void periodic() {
    // This method will be called once per scheduler run
  
  }
}
