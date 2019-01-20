/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

  public static final int PORT_DRIVE_JOYSTICK_RIGHT_Y_AXIS = 5;
  public static final int PORT_DRIVE_JOYSTICK_LEFT_Y_AXIS = 1;
  public static final int PORT_DRIVE_TALON_FRONT_LEFT = 3;
  public static final int PORT_DRIVE_TALON_BACK_LEFT = 4;
  public static final int PORT_DRIVE_TALON_FRONT_RIGHT = 1;
  public static final int PORT_DRIVE_TALON_BACK_RIGHT = 2;

  public static final int PORT_DRIVE_JOYSTICK = 0;

}
