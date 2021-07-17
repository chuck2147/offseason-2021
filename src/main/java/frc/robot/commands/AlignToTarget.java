// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.SwerveDrivetrain;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class AlignToTarget extends PIDCommand {
   /**
   * Turns to robot to the specified angle.
   *
   * @param targetAngleDegrees The angle to turn to
   * @param drive              The drive subsystem to use
   */
  
   public AlignToTarget(SwerveDrivetrain drive) {
    super(
        // The controller that the command will use
        new PIDController(.1, 0, 0),
        //This should return the measurment
        Limelight::getXOffset, 
        
        // This should return the setpoint (can also be a constant)
        0, 
        
        // This uses the output
        output -> drive.drive(0, 0, output, true), drive);
   
     
      // Set the controller to be continuous (because it is an angle controller)
      getController().enableContinuousInput(-180, 180);
      // Set the controller tolerance - the delta tolerance ensures the robot is stationary at the
      // setpoint before it is considered as having reached the reference
      getController()
          .setTolerance(1, 30); //turnTolerance= deg , turnRateTolerance = degrees per sec)

      SmartDashboard.putNumber("AlignToTargetX", Limelight.getXOffset());
}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    //if (Limelight.getXOffset() > .005 & Limelight.getXOffset() < -.005) {
    //  return true;
  //  } else {
    
    return false;
    
    

    //How to end command?  at setpoint or rotation < something slow?  !look at profiledPID command!
    //TODO: When finished set camera mode for driver and turn LED off
  
  }
}
