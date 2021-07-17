// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IndexerSubsystem;

public class IndexerTriggeredCommand extends CommandBase {
  public final IndexerSubsystem indexer;
  
  public IndexerTriggeredCommand(IndexerSubsystem indexer) {
    this.indexer = indexer;
    addRequirements(indexer);  
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (indexer.isShooterTriggered()) {
      indexer.stopIndexer();
      } else if (indexer.isIndexerTriggered()) {
          indexer.runIndexer();
          indexer.runHopper();
      } 
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    indexer.stopIndexer();
    indexer.stopHopper();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
