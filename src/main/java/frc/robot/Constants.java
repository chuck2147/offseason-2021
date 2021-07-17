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
	public static double frontLeftOffset = 72; 
	public static double frontRightOffset = -5;
	public static double backLeftOffset = 60; 
	public static double backRightOffset = 40;
	
    //SWERVE TIME-OUTs
    public static int kLongCANTimeoutMs = 0;  //use for constructors
    
    //SWERVE ANGLE MOTORS PID VALUES
    public static double kAngleP = .3;
	public static double kAngleI = 0;
	public static double kAngleD = 0;
    
    //PARAMETER USED FOR TURN TO DEGREES COMMAND
	public static double kTurnRateTolerance = 10;  //degpersecond? used in easy PIDcontroller
 
    public static double kMaxTurnRateDegPerS = 100;    
    public static double kMaxTurnAccelerationDegPerSSquared = 300;  

    public static double kTurnToleranceDeg = 5;   
    public static double kMaxTurnRateToleranceDegPerS = 10; // degrees per second

     
    //SHOOTER RPM (max=7000rpm min=100) (max enc units per 100ms = 18000) ***max changes with tuning...20000 now
    
    public static final int TEST_UPPER = 10000;
    public static final int TEST_LOWER = 0;

    public static final int BEHIND_LINE_UPPER = -500; 
    public static final int BEHIND_LINE_LOWER = 10000; 

//    public static final int BEHIND_LINE_UPPER = -1300; 
   // public static final int BEHIND_LINE_LOWER = 11700; 
    
    public static final int FRONT_OF_TRENCH_UPPER = 300; 
    public static final int FRONT_OF_TRENCH_LOWER = 10400; 
   
    public static final int BEHIND_TRENCH_UPPER = 300; 
    public static final int BEHIND_TRENCH_LOWER = 11700; 

    public static final int NEW_UPPER = 16000; 
    public static final int NEW_LOWER = 19000; 

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


	
    
        

}
