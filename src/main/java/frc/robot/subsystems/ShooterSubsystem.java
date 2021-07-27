package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ShooterSubsystem extends SubsystemBase {

  private final TalonFX upperMotor = new TalonFX(Constants.UPPER_SHOOTER_MOTOR);
  private final TalonFX lowerMotor = new TalonFX(Constants.LOWER_SHOOTER_MOTOR);

// TODO: Tune these PID values for your robot
  private static double kF_upper = .05;
	private static double kP_upper = .05;
	private static double kI_upper = 0;
  private static double kD_upper = 0;
    
  private static double kF_lower = .05;
	private static double kP_lower = .05;
	private static double kI_lower = 0;
	private static double kD_lower = 0;  
  
  private double targetVelocityUpper = 0; 
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
   
    /* Config the Velocity closed loop gains in slot0 (PID_SLOT, PID_vALUE, timeouts)*/
    //TODO tune
		upperMotor.config_kF(0, kF_upper, 30);
    upperMotor.config_kP(0, kP_upper, 30);
    upperMotor.config_kI(0, kI_upper, 30);
    upperMotor.config_kD(0, kD_upper, 30);

    lowerMotor.config_kF(0, kF_lower, 30);
    lowerMotor.config_kP(0, kP_lower, 30);
    lowerMotor.config_kI(0, kI_lower, 30);
    lowerMotor.config_kD(0, kD_lower, 30);
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
    targetVelocityUpper = velocityUpper;
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


    SmartDashboard.putNumber("Upper Velocity", upperMotor.getSelectedSensorVelocity());
    SmartDashboard.putNumber("Lower Velocity", lowerMotor.getSelectedSensorVelocity());
    SmartDashboard.putNumber("Target Velocity", targetVelocityLower);
    SmartDashboard.putBoolean("Launcher On Target", isOnTarget());
    SmartDashboard.putBoolean("Avg Launcher On Target", isOnTargetAverage(10));
    
    
  }

}