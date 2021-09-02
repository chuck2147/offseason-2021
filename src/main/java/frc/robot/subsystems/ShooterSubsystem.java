package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.PIDNTValue;


public class ShooterSubsystem extends SubsystemBase {

  private final TalonFX upperMotor = new TalonFX(Constants.UPPER_SHOOTER_MOTOR);
  private final TalonFX lowerMotor = new TalonFX(Constants.LOWER_SHOOTER_MOTOR);

  ShuffleboardTab tab = Shuffleboard.getTab("NTValues");
  NetworkTableEntry upperVelocityGraphEntry = tab.add("Upper Current Velocity Graph", 0)
    .withSize(2, 1)
    .withWidget(BuiltInWidgets.kGraph)
    .getEntry();
    NetworkTableEntry upperVelocityEntry = tab.add("Upper Current Velocity", 0)
    .withSize(2, 1)
    .withWidget(BuiltInWidgets.kTextView)
    .getEntry();
  NetworkTableEntry lowerVelocityEntry = tab.add("Lower Current Velocity", 0)
    .withSize(2, 1)
    .withWidget(BuiltInWidgets.kTextView)
    .getEntry();

  private double targetVelocityLower = 0; 
  private int rollingAvg = 0;
  
  
  public ShooterSubsystem() {
    upperMotor.configFactoryDefault();
    lowerMotor.configFactoryDefault();
  
    upperMotor.setNeutralMode(NeutralMode.Coast);
    lowerMotor.setNeutralMode(NeutralMode.Coast);
  
    upperMotor.setInverted(TalonFXInvertType.CounterClockwise);
    lowerMotor.setInverted(TalonFXInvertType.Clockwise);

    // "full output" will now scale to 12 Volts for all control modes when enabled.
    upperMotor.configVoltageCompSaturation(12);
    lowerMotor.configVoltageCompSaturation(12); 
    lowerMotor.enableVoltageCompensation(true);
    upperMotor.enableVoltageCompensation(true);
    
		/* Config neutral deadband to be the smallest possible */
    upperMotor.configNeutralDeadband(0.001);
    lowerMotor.configNeutralDeadband(0.001);

    /* Config sensor used for Primary PID [Velocity] (Talon integrated encoder, PID_Slot, timeouts)*/
    upperMotor.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 0, 30);
    lowerMotor.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 0, 30);
   
    // PIDF
    new PIDNTValue(Constants.UPPER_SHOOTER_P, Constants.UPPER_SHOOTER_I, Constants.UPPER_SHOOTER_D, Constants.UPPER_SHOOTER_F, upperMotor, "Upper Shooter"); 
    new PIDNTValue(Constants.LOWER_SHOOTER_P, Constants.LOWER_SHOOTER_I, Constants.LOWER_SHOOTER_D, Constants.LOWER_SHOOTER_F, lowerMotor, "Lower Shooter"); 
  }


  public void stopShooter() {
    upperMotor.set(ControlMode.PercentOutput, 0);
    lowerMotor.set(ControlMode.PercentOutput, 0);
  }
  
  public double getVelocityUpper() {
    return upperMotor.getSelectedSensorVelocity();
  }

  public double getVelocityLower() {
    return lowerMotor.getSelectedSensorVelocity();
  } 
  
  public void setVelocity(double velocityUpper, double velocityLower) {
    targetVelocityLower = velocityLower;
    upperMotor.set(TalonFXControlMode.Velocity, velocityUpper);
    lowerMotor.set(TalonFXControlMode.Velocity, velocityLower);
  }

  public void shootFromBehindLine() {
    setVelocity(Constants.SHOOTER_BEHIND_LINE_UPPER.value, Constants.SHOOTER_BEHING_LINE_LOWER.value);
  }
  public void shootFromTriangle() {
    setVelocity(Constants.SHOOTER_TRIANGLE_UPPER.value, Constants.SHOOTER_TRIANGLE_LOWER.value);
  }
  public void shootFromFrontOfTrench(){
    setVelocity(Constants.SHOOTER_FRONT_OF_TRENCH_UPPER.value, Constants.SHOOTER_FRONT_OF_TRENCH_LOWER.value);
  }
  public void shootFromFar() {
    setVelocity(Constants.SHOOTER_FAR_UPPER.value, Constants.SHOOTER_FAR_LOWER.value);
  }
  // Lower_Motor Velocity will always take longer to get on target... so only needs lower velocity
  public boolean isOnTarget() {
    boolean lowerOnTarget = Math.abs(targetVelocityLower - getVelocityLower()) <= Constants.velocityPIDTolerance;
    //If statement needed because, will read true on startup.
    if (getVelocityLower() <50) {
      return false;
      } else {      
      return (lowerOnTarget);
      } 
  }
//Make sure velocity isOnTarget more than once
  public boolean isOnTargetAverage(int percent) {
    if(percent > 10) {
      percent = 10;
    } else if(percent < 0) {
      percent = 0;
    }

    if(rollingAvg >= percent) {
      return true;
    }
    return false;
  }

  public static double distanceToVelocity(double distance) {
    //TODO tune distance convertion .... use if camera distance to goal is known
    return 0.0;
  }

 
  @Override
  public void periodic() {
    if (isOnTarget()) {
      if(rollingAvg < 10) {
        rollingAvg++;
      }
    } else if(rollingAvg > 0) {
      if(rollingAvg > 0) {
        rollingAvg--;
      }
    }
    upperVelocityEntry.setValue(upperMotor.getSelectedSensorVelocity());
    upperVelocityGraphEntry.setValue(lowerMotor.getSelectedSensorVelocity());
    lowerVelocityEntry.setValue(lowerMotor.getSelectedSensorVelocity());

    // SmartDashboard.putNumber("Upper Velocity", upperMotor.getSelectedSensorVelocity());
    // SmartDashboard.putNumber("Lower Velocity", lowerMotor.getSelectedSensorVelocity());
    // SmartDashboard.putNumber("Target Velocity", targetVelocityLower);
    // SmartDashboard.putBoolean("Launcher On Target", isOnTarget());
    // SmartDashboard.putBoolean("Avg Launcher On Target", isOnTargetAverage(10));
  }
}