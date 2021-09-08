/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.ClimberState;
import frc.robot.commands.ClimberToTopCommand;
import frc.robot.commands.ClimberCommand;
import frc.robot.commands.IndexerTriggeredCommand;
import frc.robot.commands.SwerveDriveCommand;
import frc.robot.commands.VisionAlignCommand;
import frc.robot.commands.autonomous.DriveForward;
import frc.robot.commands.autonomous.Steal2Auto;
import frc.robot.commands.autonomous.TestPathAuto;
import frc.robot.commands.autonomous.TrenchAuto;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.IndexerSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.SwerveDrivetrain;


/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  private final SwerveDrivetrain drivetrain = new SwerveDrivetrain();
  private final ShooterSubsystem shooter = new ShooterSubsystem();
  private final IndexerSubsystem indexer = new IndexerSubsystem();
  private final IntakeSubsystem intake = new IntakeSubsystem();
  private final ClimberSubsystem climber = new ClimberSubsystem();


  private final Controller driverController = new Controller(0, 0.05);
  private final Controller operatorController = new Controller(1, 0.05);
  
  //shooter buttons
  private final JoystickButton shooterTriangleButton = operatorController.getButton(Controller.Button.X);
  private final JoystickButton shooterBehindLineButton = operatorController.getButton(Controller.Button.A);
  private final JoystickButton shooterFarButton = operatorController.getButton(Controller.Button.B);
  private final JoystickButton shooterFrontOfTrench = operatorController.getButton(Controller.Button.Y);
  private final JoystickButton intakeButton = operatorController.getButton(Controller.Button.RightBumper);
  private final AxisTrigger runOperatorIndexerButton = new AxisTrigger(operatorController, 3);
  private final AxisTrigger runOperatorIndexerReverseButton = new AxisTrigger(operatorController, 2);

  //private final JoystickButton climberUpButton = driverController.getButton(Controller.Button.Back);
  //private final JoystickButton climberDownButton = driverController.getButton(Controller.Button.Start);  
  private final JoystickButton faceTargetButton = driverController.getButton(Controller.Button.A);
  private final JoystickButton shooterDriverButton = driverController.getButton(Controller.Button.X);
  private final AxisTrigger runDriverIndexerButton = new AxisTrigger(driverController, 3);
  private final AxisTrigger runDriverIndexerReverseButton = new AxisTrigger(driverController, 2);
  //button 9 (left stick button) is for Robot-Centric Drive

  private final SendableChooser<Command> autoChooser = new SendableChooser<Command>();

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    drivetrain.setDefaultCommand(new SwerveDriveCommand(drivetrain, driverController));
    configureButtonBindings();   

    autoChooser.addOption("Drive Forward Only", new DriveForward(drivetrain));
    autoChooser.addOption("Test Path (not for matches)", new TestPathAuto(drivetrain));
    autoChooser.setDefaultOption("Trench", new TrenchAuto(drivetrain, shooter, indexer, intake));
    autoChooser.addOption("Steal 2", new Steal2Auto(drivetrain, shooter, indexer, intake));
    
    SmartDashboard.putData("Auto Selector", autoChooser); 
  }

  /**
   * 
   * Use this method to define your button->command mappings. Buttons can be
   * created by instantiating a {@link GenericHID} or one of its subclasses
   * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
   * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    
    driverController.getButton(Controller.Button.Back)
    .whileHeld(new ClimberCommand(climber, intake, ClimberState.Up));
    driverController.getButton(Controller.Button.Start)
    .whileHeld(new ClimberCommand(climber, intake, ClimberState.Down));
    driverController.getButton(Controller.Button.Y)
    .whileHeld(new ClimberToTopCommand(climber, intake));
    //autoChooser.addOption("Shot Drive Back", new AutoShotDriveBack(drivetrain, shooter, indexer));

    // <<<INTERNAL Triggers are actions that happen without a joystick action needed and are always running when enabled.>>>

    //INTERNAL Robot Triggers
      Trigger isHopperTriggered = new Trigger(() -> indexer.isHopperTriggered());
      Trigger isIndexerTriggered = new Trigger(() -> indexer.isIndexerTriggered());
      Trigger isOnTarget = new Trigger(() -> shooter.isOnTargetAverage(7));
    
    //INTERNAL Triggered Commands
      isIndexerTriggered.whileActiveContinuous(new IndexerTriggeredCommand(indexer));
      isOnTarget.whileActiveContinuous(indexer::feedToShooter, indexer);
      isOnTarget.whenInactive(indexer::stopFeedToShooter, indexer);
     
      //If Hopper IRsensor is trigger then run Hopper motor for x seconds
      isHopperTriggered.whenActive(new RunCommand (() -> {
          indexer.runHopper();
        }).withTimeout(Constants.HOPPER_TIMEOUT)
        .andThen(() -> {
          indexer.stopHopper();
        }));
      
    //  <<<<<DRIVER CONTROLLER>>>>>
    
    shooterBehindLineButton.whileHeld(shooter::shootFromBehindLine, shooter);
    shooterBehindLineButton.whenReleased(shooter::stopShooter, shooter);
    
    shooterTriangleButton.whileHeld(shooter::shootFromTriangle, shooter);
    shooterTriangleButton.whenReleased(shooter::stopShooter, shooter);

    shooterFrontOfTrench.whileHeld(shooter::shootFromFrontOfTrench, shooter);
    shooterFrontOfTrench.whenReleased(shooter::stopShooter, shooter);
    shooterFarButton.whileHeld(shooter::shootFromFar, shooter);
    shooterFarButton.whenReleased(shooter::stopShooter, shooter);

    shooterDriverButton.whileHeld(shooter::shootFromBehindLine, shooter);
    shooterDriverButton.whenReleased(shooter::stopShooter, shooter);
  
    faceTargetButton.whileHeld(new VisionAlignCommand(drivetrain));
    
    runOperatorIndexerButton.whileHeld(indexer::feedToShooter, indexer);
    runOperatorIndexerButton.whenReleased(indexer::stopFeedToShooter, indexer);
  
    runOperatorIndexerReverseButton.whileHeld(indexer::runReverse, indexer);
    runOperatorIndexerReverseButton.whenReleased(indexer::stopFeedToShooter, indexer);

    runDriverIndexerButton.whileHeld(indexer::feedToShooter, indexer);
    runDriverIndexerButton.whenReleased(indexer::stopFeedToShooter, indexer);
  
    runDriverIndexerReverseButton.whileHeld(indexer::runReverse, indexer);
    runDriverIndexerReverseButton.whenReleased(indexer::stopFeedToShooter, indexer);
     
    intakeButton.whileHeld(new WaitCommand(0.1).andThen(new InstantCommand(intake::runIntake, intake).perpetually()));
    intakeButton.whenPressed(intake::extendIntake, intake);
    intakeButton.whenReleased(intake::retractIntake, intake); 
    intakeButton.whenReleased(intake::stopIntake, intake);

    // climberUpButton.whenPressed(intake::extendIntake, intake);
    // climberUpButton.whileHeld(climber::runClimber, climber);
    //   climberUpButton.whenPressed(climber::climberPistonOff, climber);
    //   climberUpButton.whenReleased(climber::stopClimber, climber);
    //   climberUpButton.whenReleased(climber::climberPistonOn, climber);

    //   climberDownButton.whileHeld(climber::reverseClimber, climber);
    //   climberDownButton.whenPressed(climber::climberPistonOff, climber);
    //   climberDownButton.whenReleased(climber::stopClimber, climber);
    //   climberDownButton.whenReleased(climber::climberPistonOn, climber);

  // <<<OPERATOR CONTROLLER>>> (If driver-controller automatic functions go wrong... 
  //Operator can overide driver-controller and run mechanisms manually...in/out up/down)

  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }
}
