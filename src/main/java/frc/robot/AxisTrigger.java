package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.button.Button;

public class AxisTrigger extends Button {
    private final GenericHID controller;
    private final int axisNumber;
    private double threshold = 0.75;

    AxisTrigger(GenericHID controller, int axisNumber, double threshold) {
        this.controller = controller;
        this.axisNumber = axisNumber;
        this.threshold = threshold;
    }

    AxisTrigger(GenericHID controller, int axisNumber) {
        this.controller = controller;
        this.axisNumber = axisNumber;
    }

    @Override
    public boolean get() {
       if (threshold <= 0) {
        return controller.getRawAxis(axisNumber) < threshold;
       } 
       return controller.getRawAxis(axisNumber) > threshold;
    }
}