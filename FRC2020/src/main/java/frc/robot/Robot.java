/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private DifferentialDrive drive;
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

//Drive
 private CANSparkMax leftMotor, left2motor;
  private CANSparkMax rightMotor, right2motor;
  //Falcon
  private WPI_TalonFX  talonFX;
  //Intake
  private CANSparkMax intake;
  //Secondary Intake
  private CANSparkMax secondaryIntake;
  //Carousel
  private CANSparkMax carousel;
  private CANSparkMax carouselUnload;
  //Shooter
  private CANSparkMax shooter1; 
  private CANSparkMax shooter2; 
  //Elevator
  private CANSparkMax elevator;


/// CHANGE CAN IDS
    //Drive
  private int leftdrCANID = 13; //CHANGE  ID
  private int left2drCANID = 8; //CHANGE CAN ID
  private int rightdrCANID = 14;  //CHANGE CAN ID
  private int right2drCANID = 9;  //CHANGE CAN ID
    //Intake
  private int intakeCANID = 7; //CHANGE CAN ID
  private int secondaryIntakeCANID = 10; //CHANGE CAN ID
    //Carousel
  private int carousel1CANID = 12; //CHANGE CAN ID
  private int carouselUnloadCANID = 16; //CHANGE CAN ID
    //Shooter
  private int shooter1CANID = 11; //CHANGE CAN ID
  private int shooter2CANID = 15; //CHANGE CAN ID
  //Falcon ID
  private int talonfxCANID = 0; //CHANGE CAN ID
  //Elevator
  private int elevetorCANID = 17;
////
  private XboxController xbox; 



  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

   leftMotor = new CANSparkMax(leftdrCANID, MotorType.kBrushless);
   left2motor = new CANSparkMax(left2drCANID, MotorType.kBrushless);
   rightMotor = new CANSparkMax(rightdrCANID, MotorType.kBrushless);
   right2motor = new CANSparkMax(right2drCANID, MotorType.kBrushless);

   left2motor.follow(leftMotor);
   right2motor.follow(rightMotor);

   drive = new DifferentialDrive(leftMotor, rightMotor);
   xbox = new XboxController(0); 

  //Falcon
   talonFX = new WPI_TalonFX(talonfxCANID);
  //Intake
   intake = new CANSparkMax(intakeCANID, MotorType.kBrushless);
  //Secondary Intake
    secondaryIntake = new CANSparkMax(secondaryIntakeCANID, MotorType.kBrushless);
  //Carousel
    carousel = new CANSparkMax(carousel1CANID, MotorType.kBrushless);
    carouselUnload = new CANSparkMax(carouselUnloadCANID, MotorType.kBrushless);
  //Shooter
    shooter1 = new CANSparkMax(shooter1CANID, MotorType.kBrushless); 
    shooter2 = new CANSparkMax(shooter2CANID, MotorType.kBrushless); 
    //Elevator
    elevator = new CANSparkMax(elevetorCANID, MotorType.kBrushless); 

  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {

      double XboxPosX = xbox.getX(Hand.kLeft); //was previsouly kRight
      double XboxPosY = xbox.getTriggerAxis(Hand.kLeft) - xbox.getTriggerAxis(Hand.kRight);
      drive.arcadeDrive(-XboxPosY,XboxPosX);

      //Intake
      if (xbox.getAButton()){
        intake.set(0.5);
      }else{intake.set(0); }

      if (xbox.getBButton()){
        secondaryIntake.set(0.5);
      }else{secondaryIntake.set(0);}

      //Carousel
      if (xbox.getBumper(Hand.kRight)){
        carousel.set(0.5);
      }else{ carousel.set(0);}
      if (xbox.getBButton()){
        carouselUnload.set(0.5);
      }
      else{
        carouselUnload.set(0);
      }

      //Falcon
      if (xbox.getXButton()&&xbox.getYButton()){
        talonFX.set(0.5);
      }else{ talonFX.set(0);}
      
      //Shooter
      if (xbox.getBackButton()){
        intake.set(0.5);
        secondaryIntake.set(0.5);
      }
      else if (xbox.getStartButton()){
        intake.set(-0.5);
        secondaryIntake.set(-0.5);
      }
      else{
        intake.set(0);
        secondaryIntake.set(0.0);
      }

      if (xbox.getYButton()){
        elevator.set(0.5);
      }
      else{
        elevator.set(0);
      }

  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
