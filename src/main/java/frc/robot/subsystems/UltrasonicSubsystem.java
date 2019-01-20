package frc.robot.subsystems;

import frc.robot.commands.ReporterCommand;
import frc.util.SampleRate;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Class for obtaining data from a MaxBotix MB1013 or MB1200 Range Finder
 */
public class UltrasonicSubsystem extends ReportableSubsystem implements Runnable {
  // Supported models:
  public enum Model {
    MB1013, MB1200
  }

  private Model model;

  private SerialPort serialPort;
  private double serialRangeCm = -1;
  private double serialRangeFactorCm;
  private SampleRate serialSampleRate;

  private AnalogPotentiometer analogPotentiometer;

  private final double cmPerInch = 2.54;

  /**
   * Constructs an instance using a serial port and/or analog input to get data
   * from the sonar.
   */
  public UltrasonicSubsystem(Model model, SerialPort serialPort, AnalogInput analogInput)
	{
		this.model = model;
		this.serialPort = serialPort;
		if (null != serialPort)
		{
			serialSampleRate = new SampleRate();
			serialSampleRate.start();
			serialRangeFactorCm = (Model.MB1013 == model)
				? 10		// 10 mm/cm
				: 1;		// 1 cm/cm
			new Thread(this).start();
		}
		
		if (null != analogInput)
		{
			// Construct analog potentiometer to measure full range of sensor in cm:
			double analogRangeCm = (Model.MB1013 == model)
				? 512		// 5120 mm = 512 cm
				: 1024;		// 1024 cm
			analogPotentiometer = new AnalogPotentiometer(analogInput, analogRangeCm, 0.0);
		}
	}

	/**
	 * Reads and returns the current range (in cm) from the analog port.
	 * -1 is returned if range is not being obtained form the analog port.
	 */
	public double getAnalogRangeCm()
	{
		return null == analogPotentiometer ? -1 : analogPotentiometer.get();
	}

	/**
	 * Reads and returns the current range (in inches) from the analog port.
	 * -1 is returned if range is not being obtained form the analog port.
	 */
	public double getAnalogRangeInches()
	{
		return null == analogPotentiometer ? -1 : analogPotentiometer.get() / cmPerInch;
	}

	/**
	 * Returns the current range (in cm) from the serial port.
	 * -1 is returned if range is not being obtained form the serial port.
	 */
	public double getSerialRangeCm()
	{
		return serialRangeCm;
	}
	
	/**
	 * Returns the current range (in inches) from the serial port.
	 * -1 is returned if range is not being obtained form the serial port.
	 */
	public double getSerialRangeInches()
	{
		return -1 == serialRangeCm ? -1 : serialRangeCm / cmPerInch;
	}
	
	public void initDefaultCommand()
	{
		// Set the default command for a subsystem here.
		setDefaultCommand(new ReporterCommand(this));
	}

	@Override
	public void report()
	{
		if (null != serialPort)
		{
			SmartDashboard.putNumber(model + " Serial (cm)", getSerialRangeCm());
			SmartDashboard.putNumber(model + " Serial (inch)", getSerialRangeInches());
			SmartDashboard.putNumber(model + " Serial Rate", serialSampleRate.getSampleRate());
		}
		
		if (null != analogPotentiometer)
		{
			SmartDashboard.putNumber(model + " Analog (cm)", getAnalogRangeCm());
			SmartDashboard.putNumber(model + " Analog (inch)", getAnalogRangeInches());
		}
	}

	/**
	 * Code for thread that reads range values from the serial port.
	 * 
	 * Values are in the form RxxxxCR where:
	 * 	R is character 'R'
	 * 	xxxx is 3 or 4 decimal digits
	 *	CR is a carriage return character
	 */
	@Override
	public void run()
	{
		// Reset serial port to empty buffers:
		serialPort.reset();
		
		// Start with an empty character buffer:
		byte[] data = new byte[0];
		int index = 0;
		
		int value = 0;
		boolean startCharacterFound = false;
		
		// Loop forever reading and processing characters from the serial port.
		while (true)
		{
			// If all previously read characters have been processed, read more characters:
			if (index >= data.length)
			{
				data = serialPort.read(serialPort.getBytesReceived());
				if (0 == data.length)
				{
					continue;
				}
				index = 0;
			}
			
			// Process a single character:
			byte c = data[index++];
			if (startCharacterFound)
			{
				// If we have seen a 'R', look for a CR or digit:
				if ('\r' == c)
				{
					serialRangeCm = value / serialRangeFactorCm;
					value = 0;
					startCharacterFound = false;
					serialSampleRate.addSample();
				}
				else
				{
					// Add current digit to value being accumulated:
					value = value * 10 + (c - '0');
				}
			}
			else
			{
				// See if character is the 'R' indicating the start of a value:
				startCharacterFound = 'R' == c;
			}
		}
	}
}