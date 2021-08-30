// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean constants. This class should not be used for any other
 * purpose. All constants should be declared globally (i.e. public static). Do
 * not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the constants are needed, to reduce verbosity.
 */
public final class Constants {

	//SWERVE OFFSETS
	public static double frontLeftOffset = 65; 
	public static double frontRightOffset = -18;
	public static double backLeftOffset = 45; 
	public static double backRightOffset = 27;
	
    //SWERVE TIME-OUTs
    public static int kLongCANTimeoutMs = 0;  //use for constructors
    
    //SWERVE ANGLE MOTORS PID VALUES
    public static double kAngleP = 0.3; //.3;
	public static double kAngleI = 0;
	public static double kAngleD = 0;
    
    //PARAMETER USED FOR TURN TO DEGREES COMMAND
	public static double kTurnRateTolerance = 10;  //degpersecond? used in easy PIDcontroller
 
    public static double kMaxTurnRateDegPerS = 100;    
    public static double kMaxTurnAccelerationDegPerSSquared = 300;  

    public static double kTurnToleranceDeg = 5;   
    public static double kMaxTurnRateToleranceDegPerS = 10; // degrees per second

     
    //SHOOTER RPM (max=7000rpm min=100) (max enc units per 100ms = 18000) ***max changes with tuning...20000 now

    public static final NTValue SHOOTER_TRIANGLE_UPPER = new NTValue(-3000, "Shooter Triangle Upper");
    public static final NTValue SHOOTER_TRIANGLE_LOWER = new NTValue(13000, "Shooter Triangle Lower");

    public static final NTValue SHOOTER_BEHIND_LINE_UPPER = new NTValue(4500, "Shooter Behind Line Upper");
    public static final NTValue SHOOTER_BEHING_LINE_LOWER = new NTValue(10000, "Shooter Behind Line Lower");
    
    public static final NTValue SHOOTER_FRONT_OF_TRENCH_UPPER = new NTValue(4000, "Shooter Front Of Trench Upper"); 
    public static final NTValue SHOOTER_FRONT_OF_TRENCH_LOWER = new NTValue(12750, "Shooter Front OF Trench Lower"); 
   

    public static final NTValue SHOOTER_FAR_UPPER = new NTValue(14000, "Shooter Far Upper"); 
    public static final NTValue SHOOTER_FAR_LOWER = new NTValue(18700, "Shooter Far Lower"); 

    //returns- velocity on target with + or - tolerance
    public static final double velocityPIDTolerance = 1000;


    //MOTOR SPEEDS
    public static final double INDEXER_SPEED = .6;
    public static final double HOPPER_SPEED = .4;
    public static final double INTAKE_SPEED = .6;
	
    //IR SENSOR VOLTAGES... 0-3volts higher number=closer to sensor
    public static final double IR_SHOOTER_VOLTAGE = 1.75;
    public static final double IR_INDEXER_VOLTAGE = 1.75;
    public static final double IR_HOPPER_VOLTAGE = 1.2;

    //TIMEOUTS (secs)
    public static final double HOPPER_TIMEOUT = 1;
    public static final double INTAKE_REVERSE_TIMEOUT = 1;

    //MOTOR PORTS
    public static final int UPPER_SHOOTER_MOTOR = 9;
	public static final int LOWER_SHOOTER_MOTOR = 10;
	public static final int INTAKE_MOTOR = 11;
	public static final int HOPPER_MOTOR = 12;
    public static final int INDEXER_MOTOR = 13;
    public static final int CLIMB_MOTOR = 14;
    
    //IR SENSORs 0-3volts higher number=closer to sensor
	public static final int IR_SHOOTER = 0;
	public static final int IR_INDEXER = 1;
    public static final int IR_HOPPER = 2;
    
    //CYLINDERS
    public static final int CLIMBER_AIR_IN = 0;
    public static final int CLIMBER_AIR_OUT = 1;
    public static final int INTAKE_RIGHT_AIR_IN = 3;
    public static final int INTAKE_RIGHT_AIR_OUT = 2;
    public static final int INTAKE_LEFT_AIR_IN = 5;
    public static final int INTAKE_LEFT_AIR_OUT = 4;
    public static final int SPINNER_AIR_IN = 6;
    public static final int SPINNER_AIR_OUT = 7;
	public static final double kTurnRateToleranceDegPerS = 0;

    //MOTOR PIDs
    public static final double UPPER_SHOOTER_P = 0.3;
    public static final double UPPER_SHOOTER_I = 0;
    public static final double UPPER_SHOOTER_D = 4.5;
    public static final double UPPER_SHOOTER_F = 0.0487; 
    public static final double LOWER_SHOOTER_P = 0.3;
    public static final double LOWER_SHOOTER_I = 0;
    public static final double LOWER_SHOOTER_D = 4.5;
    public static final double LOWER_SHOOTER_F = 0.0487;

    //LIMELIGHT PIDs 
    public static final double VISION_ALIGN_P = 0.185;
    public static final double VISION_ALIGN_I = 0;
    public static final double VISION_ALIGN_D = 0.005;
    public static final double VISION_ALIGN_F = 0;

    //CLIMBER PIDs
    public static final double CLIMBER_ALL_UP_P = 0.185;
    public static final double CLIMBER_ALL_UP_I = 0;
    public static final double CLIMBER_ALL_UP_D = 0;
    public static final double CLIMBER_ALL_UP_F = 0;


    //CLIMBER ALL UP ENCODER CONSTANT
    //TODO figure out the setpoint for the To Top command.
    //TODO measure the climber target distance and plug that into varibale.
    public static final double CLIMBERTOTOPSETPOINT = 0;
    public static final double CLIMBERTARGET = 0;
    public static double climberSensorUnitsPerRotation = 2048;
    public static double climberGearRatio = 1; 
    public static double climberMaxRPM = 6380;
    //Amount of sensor units per 100ms
    public static double climberPeakSensorVelocity = (climberMaxRPM / 600) * (climberSensorUnitsPerRotation / climberGearRatio);

    public enum ClimberState {
        Down, Up, UpAll
    }
	
}
