package frc.robot.commands;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.SlewRateLimiter;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Controller;
import frc.robot.subsystems.SwerveDrivetrain;

public class SwerveDriveCommand extends CommandBase {

  private final SwerveDrivetrain drivetrain;
  private final XboxController controller;

  // Slew rate limiters to make joystick inputs more gentle; 1/3 sec from 0 to 1.
  private final SlewRateLimiter xspeedLimiter = new SlewRateLimiter(3);
  private final SlewRateLimiter yspeedLimiter = new SlewRateLimiter(3);
  private final SlewRateLimiter rotLimiter = new SlewRateLimiter(3);

  public SwerveDriveCommand(SwerveDrivetrain drivetrain, Controller controller) {
    this.drivetrain = drivetrain;
    addRequirements(drivetrain);

    this.controller = controller;
  }
  
  final boolean robotCentric() {
    return controller.getStickButton(GenericHID.Hand.kLeft);
  } 

  @Override
  public void execute() {
    // GetX and GetY are swapped.
    final var xSpeed = -controller.getY(GenericHID.Hand.kLeft);
    final var ySpeed = -controller.getX(GenericHID.Hand.kLeft);
    final var rot = -controller.getRawAxis(4) * 3;

    if (robotCentric() == true) {
      drivetrain.drive(xSpeed, ySpeed, rot, false);
      } else {
        drivetrain.drive(xSpeed, ySpeed, rot, true);
      } 

  }
  
}
