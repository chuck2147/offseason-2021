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
import edu.wpi.first.wpilibj2.command.PerpetualCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.IndexerTriggeredCommand;
import frc.robot.commands.SwerveDriveCommand;
import frc.robot.commands.VisionAlign;
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


  private final XboxController driverController = new XboxController(0);
  private final XboxController operatorController = new XboxController(1);
  
  //shooter buttons
  private final JoystickButton shooterTriangleButton = new JoystickButton(operatorController, 3); // X button
  private final JoystickButton shooterBehindLineButton = new JoystickButton(operatorController, 1); // A button
  private final JoystickButton shooterFarButton = new JoystickButton(operatorController, 2); // B button
  private final JoystickButton newButton = new JoystickButton(operatorController, 4);
  private final JoystickButton faceFront = new JoystickButton(driverController, 5);
  private final JoystickButton intakeButton = new JoystickButton(operatorController, 6); 
  private final JoystickButton climberUpButton = new JoystickButton(driverController, 7);
  private final JoystickButton climberDownButton = new JoystickButton(driverController, 8);
  //button 9 (left stick button) is for Robot-Centric Drive
  private final AxisTrigger runIndexerButton = new AxisTrigger(driverController, 3);
 
  private final SendableChooser<Command> autoChooser = new SendableChooser<Command>();

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    drivetrain.setDefaultCommand(new SwerveDriveCommand(drivetrain, driverController));

    configureButtonBindings();   
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

    shooterFarButton.whileHeld(shooter::shootFromFar, shooter);
    shooterFarButton.whenReleased(shooter::stopShooter, shooter);

      //turn to 0 degrees...slow rotation with kD?...turn 180deg off?
      //faceFront.whenPressed (new TurnToAngleCommand(0, drivetrain).withTimeout(5));

      faceFront.whileHeld(new VisionAlign(drivetrain));
      faceFront.whenReleased(new SwerveDriveCommand(drivetrain, driverController));
    
      runIndexerButton.whileHeld(indexer::feedToShooter, indexer);
      runIndexerButton.whenReleased(indexer::stopFeedToShooter, indexer);
    
    intakeButton.whileHeld(new WaitCommand(0.1).andThen(new InstantCommand(intake::runIntake, intake).perpetually()));
    intakeButton.whenPressed(intake::extendIntake, intake);
    intakeButton.whenReleased(intake::retractIntake, intake); 
    intakeButton.whenReleased(intake::stopIntake, intake);

    climberUpButton.whileHeld(climber::runClimber, climber);
      climberUpButton.whenPressed(climber::climberPistonOff, climber);
      climberUpButton.whenReleased(climber::stopClimber, climber);
      climberUpButton.whenReleased(climber::climberPistonOn, climber);

    climberDownButton.whileHeld(climber::reverseClimber, climber);
      climberDownButton.whenPressed(climber::climberPistonOff, climber);
      climberDownButton.whenReleased(climber::stopClimber, climber);
      climberDownButton.whenReleased(climber::climberPistonOn, climber);

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
