/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class IndexerSubsystem extends SubsystemBase {
  private CANSparkMax indexerMotor;
  private CANSparkMax hopperMotor;
  private AnalogInput irShooter;
  private AnalogInput irIndexer;
  private AnalogInput irHopper;

public IndexerSubsystem() {
  indexerMotor = new CANSparkMax(Constants.INDEXER_MOTOR, MotorType.kBrushless);
  hopperMotor = new CANSparkMax(Constants.HOPPER_MOTOR, MotorType.kBrushless);
  
  irShooter = new AnalogInput(Constants.IR_SHOOTER);
  irIndexer = new AnalogInput(Constants.IR_INDEXER);
  irHopper = new AnalogInput(Constants.IR_HOPPER);
 
  //set Neo Motors to Factory Defaults
  indexerMotor.restoreFactoryDefaults();
  hopperMotor.restoreFactoryDefaults();

  //set Neo motors to brakemode
  indexerMotor.setIdleMode(IdleMode.kBrake);
  hopperMotor.setIdleMode(IdleMode.kBrake);

  //set Neo motor direction
  indexerMotor.setInverted(false);
  hopperMotor.setInverted(true);

}
    public void stopIndexer() {
      indexerMotor.set(0);
    }

    public void stopHopper() {
      hopperMotor.set(0);
    }

    public void runIndexer() {
      indexerMotor.set(Constants.INDEXER_SPEED);
    }
    
    public void runHopper() {
      hopperMotor.set(Constants.HOPPER_SPEED);
    }

    public void feedToShooter() {
      indexerMotor.set(Constants.INDEXER_SPEED);
      hopperMotor.set(Constants.HOPPER_SPEED);
    }

    public void stopFeedToShooter() {
      indexerMotor.set(0);
      hopperMotor.set(0);
    }

  public boolean isShooterTriggered() {
    return irShooter.getVoltage() > Constants.IR_SHOOTER_VOLTAGE;
  }

  public boolean isIndexerTriggered() {
    return irIndexer.getVoltage() > Constants.IR_INDEXER_VOLTAGE;
  }

  public boolean isHopperTriggered() {
    return irHopper.getVoltage() > Constants.IR_HOPPER_VOLTAGE;
  }


  @Override
  public void periodic() {

  } 

}

