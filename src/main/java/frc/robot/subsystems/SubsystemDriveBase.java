/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.RobotMap;
import frc.robot.commands.CommandJoystickDrive;

/**
 * Add your docs here.
 */
public class SubsystemDriveBase extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  WPI_TalonSRX driveTalonBackLeft;
  WPI_TalonSRX driveTalonBackRight;
  WPI_TalonSRX driveTalonFrontLeft;
  WPI_TalonSRX driveTalonFrontRight;
  DifferentialDrive driveMainDifferential;

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    setDefaultCommand(new CommandJoystickDrive());

  }

  public SubsystemDriveBase() {
     driveTalonBackLeft = new WPI_TalonSRX(RobotMap.PORT_DRIVE_TALON_BACK_LEFT);
     driveTalonBackRight = new WPI_TalonSRX(RobotMap.PORT_DRIVE_TALON_BACK_RIGHT);
     driveTalonFrontLeft= new WPI_TalonSRX(RobotMap.PORT_DRIVE_TALON_FRONT_LEFT);
     driveTalonFrontRight= new WPI_TalonSRX(RobotMap.PORT_DRIVE_TALON_FRONT_RIGHT);
  
     SpeedControllerGroup controllerGroupLeft = new SpeedControllerGroup(driveTalonBackLeft, driveTalonFrontLeft);
     SpeedControllerGroup controllerGroupRight = new SpeedControllerGroup(driveTalonBackRight, driveTalonFrontRight);

     driveMainDifferential = new DifferentialDrive(controllerGroupLeft, controllerGroupRight);

     driveMainDifferential.setExpiration(0.1);
  }

  public void drive(Joystick joystick) {
    drive(joystick.getRawAxis(RobotMap.PORT_DRIVE_JOYSTICK_LEFT_Y_AXIS), joystick.getRawAxis(RobotMap.PORT_DRIVE_JOYSTICK_RIGHT_Y_AXIS));
  }

  public void drive(double leftSpeed, double rightSpeed) {
    driveMainDifferential.tankDrive(leftSpeed, rightSpeed);
  }

  public void reset() {
    drive(0.0,0.0);
  }
  
}
