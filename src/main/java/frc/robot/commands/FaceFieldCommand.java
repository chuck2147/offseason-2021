package frc.robot.commands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.Constants.FaceFieldState;
import frc.robot.subsystems.SwerveDrivetrain;

public class FaceFieldCommand extends CommandBase {
  private final SwerveDrivetrain drivetrain;
  private final Constants.FaceFieldState faceFieldState;
  private PIDController pid = new PIDController(Constants.VISION_ALIGN_P, Constants.VISION_ALIGN_I, Constants.VISION_ALIGN_D, 0.01);
  
  public FaceFieldCommand(SwerveDrivetrain drivetrain, Constants.FaceFieldState faceFieldState) {
    this.drivetrain = drivetrain;
    this.faceFieldState = faceFieldState;
    addRequirements(drivetrain);
  }

  @Override
  public void initialize() {
    
  }

  @Override
  public void execute() {
    double endpoint = 0;
    double startpoint = drivetrain.getYaw().getDegrees();
    double pidAngularVelocity = pid.calculate(startpoint, endpoint);
    
    if (faceFieldState == FaceFieldState.Front) {
      endpoint = 0;
      startpoint = drivetrain.getYaw().getDegrees();
      pidAngularVelocity = pid.calculate(startpoint, endpoint);
      drivetrain.drive(0, 0, pidAngularVelocity, true);
    } else if (faceFieldState == FaceFieldState.Back) {
      endpoint = 180;
      startpoint = drivetrain.getYaw().getDegrees();
      pidAngularVelocity = pid.calculate(startpoint, endpoint);
      drivetrain.drive(0, 0, pidAngularVelocity, true);
    } else if (faceFieldState == FaceFieldState.Left) {
      endpoint = 270;
      startpoint = drivetrain.getYaw().getDegrees();
      pidAngularVelocity = pid.calculate(startpoint, endpoint);
      drivetrain.drive(0, 0, pidAngularVelocity, true);
    }else if (faceFieldState == FaceFieldState.Right) {
      endpoint = 90;
      startpoint = drivetrain.getYaw().getDegrees();
      pidAngularVelocity = pid.calculate(startpoint, endpoint);
      drivetrain.drive(0, 0, pidAngularVelocity, true);
    }
  }

  public boolean isFinished() {
    double endpoint = 0;
    double startpoint = drivetrain.getYaw().getDegrees();

    if (faceFieldState == FaceFieldState.Front) {
      endpoint = 0;
    } else if (faceFieldState == FaceFieldState.Back) {
      endpoint = 180;
    } else if (faceFieldState == FaceFieldState.Left) {
      endpoint = 270;
    }else if (faceFieldState == FaceFieldState.Right) {
      endpoint = 90;
    }
    return endpoint == startpoint;
  }
}
