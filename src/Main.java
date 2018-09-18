package manipulatorchallenge.src;

import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static java.lang.Math.*;

/**
 * Created by Bulat Murtazin on 25.06.2018 -> 14:09
 * KPFU ITIS 11-601
 **/


public class Main {

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {

        //TODO Input color here
        int neededColor = 6;

        //Temporary variable for keeping colorId
//        int colorId = -1;

        //Servodrives and sensors initiallization
        RegulatedMotor motor1 = new EV3LargeRegulatedMotor(MotorPort.B);
        RegulatedMotor motor2 = new EV3LargeRegulatedMotor(MotorPort.C);
        RegulatedMotor motor3 = new EV3MediumRegulatedMotor(MotorPort.D);
        EV3ColorSensor sensor = new EV3ColorSensor(LocalEV3.get().getPort("S4"));

        //Configuring servodrives' rotating speed
        motor1.setSpeed(20);
        motor2.setSpeed(20);
        motor3.setSpeed(50);

        //Constants
        final int LINK1 = 24;
        final int LINK2 = 16;

        //Rotating
//        double c2 = (x * x + y * y - LINK1 * LINK1 - LINK2 * LINK2) / (2 * LINK1 * LINK2);
//        double s2 = sqrt(1 - c2 * c2);
//        double th = 180 - (atan2(s2, c2) * 180 / Math.PI);
//        double k1 = (x * x + y * y + LINK1 * LINK1 - LINK2 * LINK2) / (2 * LINK1 * (sqrt(x * x + y * y)));
//        double k2 = sqrt(1 - k1 * k1);
//        double g = atan2(k2, k1) * 180 / Math.PI;
//        double d1 = atan2(y, x) * 180 / Math.PI;
//        double b = 180 - (d1 + g);
//        motor2.rotate((int)th);
//        motor1.rotate((int)b);

        //Calculating angles
//        int alpha = (int) toDegrees(acos((LINK1 * LINK1 + c * c - LINK2 * LINK2) / (2 * LINK1 * c)));
//        int beta = (int) toDegrees(acos((LINK1 * LINK1 + LINK2 * LINK2 - c * c) / (2 * LINK1 * LINK2)));
//        int gamma = (int) toDegrees(acos((LINK2 * LINK2 + c * c - LINK1 * LINK1) / (2 * LINK2 * c)));



        //Rotating



//        motor2.rotate(180 - 90 + 33 + 33);
//        motor1.rotate(90 - 33);
//        Delay.msDelay(2000);
//        motor1.rotate(-57);
//        motor2.rotate(-156);

        //        List of colors from Color.class
//        int black = 7;
//        int blue = 2;
//        int green = 1;
//        int red = 0;
//        int yellow = 3;
//        int white = 6;
//        int brown = 13;

        //        List of colors from task's text
//        0 – black
//        1 – blue
//        2 – green
//        3 – yellow
//        4 – red
//        5 – white
//        6 – brown

        int angle1Current = 0;
        int angle2Current = 0;

        int angle1Needed = 0;
        int angle2Needed = 0;

        int nextElementY = 1;
        int nextElementX = 1;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                int[] anglesToRotate = defineNextRotation(nextElementX, nextElementY);
                angle1Needed = anglesToRotate[0];
                angle2Needed = anglesToRotate[1];
                motor2.rotate(angle2Needed - angle2Current);
                motor1.rotate(angle1Needed - angle1Current);
                angle1Current = angle1Needed;
                angle2Current = angle2Needed;
                if (nextElementX == 1 && nextElementY == 1) {
                    Sound.beep();
                }
                Delay.msDelay(500);
                int colorId = getBoxColor(sensor);
                LCD.drawString(colorId + "", 5, 5);
                Delay.msDelay(2000);
                if (colorId == neededColor) {
                    drop(motor3, motor2, motor1, nextElementX, nextElementY);
                    if (colorId < 4) {
                        nextElementY = colorId + 1;
                    }
                    else nextElementY = colorId - 3;
                    nextElementX++;
                    break;
                }
                else nextElementY++;
            }
        }
        int[] anglesToReturnTo11 = moveTo11();
        motor1.rotate(anglesToReturnTo11[0] - angle1Current);
        motor2.rotate(anglesToReturnTo11[1] - angle2Current);
        Sound.beep();

    }

    private static int decodeColor(int colorFromLejos) {
        if (colorFromLejos == 7) {
            return 0;
        }
        else if (colorFromLejos == 2) {
            return 1;
        }
        else if (colorFromLejos == 1) {
            return 2;
        }
        else if (colorFromLejos == 3) {
            return 3;
        }
        else if (colorFromLejos == 0) {
            return 4;
        }
        else if (colorFromLejos == 6) {
            return 5;
        }
        else if (colorFromLejos == 13) {
            return 6;
        }
        else return 404;
    }

    private static int getBoxColor(EV3ColorSensor sensor) {
        int colorId = sensor.getColorID();
        return decodeColor(colorId);
    }

    private static void drop(RegulatedMotor motor3, RegulatedMotor motor2, RegulatedMotor motor1, int x, int y) {
        if (x == 4) {
            motor2.rotate(5);
            motor3.rotate(180);
            motor3.rotate(-180);
            motor2.rotate(-5);
        }
        else if (x == 1) {
            if (y == 1) {
                motor3.rotate(180);
                motor3.rotate(-180);
            }
            if (y == 2) {
                motor2.rotate(5);
                motor3.rotate(180);
                motor1.rotate(10);
                motor1.rotate(-10);
                motor3.rotate(-180);
                motor2.rotate(-5);
            }
            if (y == 3) {
                motor1.rotate(-5);
                motor3.rotate(180);
                motor1.rotate(10);
                motor1.rotate(-5);
                motor3.rotate(-180);
            }
            else {
                motor1.rotate(-5);
                motor3.rotate(180);
                motor1.rotate(10);
                motor3.rotate(-180);
                motor1.rotate(-5);
            }
        }
        else {
            motor3.rotate(180);
            motor3.rotate(-180);
        }
    }

    //Movings
    private static int [] moveTo11() {
        int angle2 = 156;
        int angle1 = 54;
        return new int[]{angle1, angle2};
    }

    private static int [] moveTo12() {
        int angle2 = 130;
        int angle1 = 45;
        return new int[]{angle1, angle2};
    }

    private static int [] moveTo13() {
        int angle2 = 115;
        int angle1 = 45;
        return new int[]{angle1, angle2};
    }

    private static int [] moveTo14() {
        int angle2 = 95;
        int angle1 = 50;
        return new int[]{angle1, angle2};
    }

    private static int [] moveTo21() {
        int angle2 = 132;
        int angle1 = 14;
        return new int[]{angle1, angle2};
    }

    private static int [] moveTo22() {
        int angle2 = 125;
        int angle1 = 16;
        return new int[]{angle1, angle2};
    }

    private static int [] moveTo23() {
        int angle2 = 110;
        int angle1 = 26;
        return new int[]{angle1, angle2};
    }

    private static int [] moveTo24() {
        int angle2 = 95;
        int angle1 = 34;
        return new int[]{angle1, angle2};
    }

    private static int [] moveTo31() {
        int angle2 = 127;
        int angle1 = -5;
        return new int[]{angle1, angle2};
    }

    private static int [] moveTo32() {
        int angle2 = 115;
        int angle1 = 6;
        return new int[]{angle1, angle2};
    }

    private static int [] moveTo33() {
        int angle2 = 92;
        int angle1 = 18;
        return new int[]{angle1, angle2};
    }

    private static int [] moveTo34() {
        int angle2 = 88;
        int angle1 = 26;
        return new int[]{angle1, angle2};
    }

    private static int [] moveTo41() {
        int angle2 = 105;
        int angle1 = -20;
        return new int[]{angle1, angle2};
    }

    private static int [] moveTo42() {
        int angle2 = 95;
        int angle1 = -5;
        return new int[]{angle1, angle2};
    }

    private static int [] moveTo43() {
        int angle2 = 90;
        int angle1 = 10;
        return new int[]{angle1, angle2};
    }

    private static int [] moveTo44() {
        int angle2 = 74;
        int angle1 = 23;
        return new int[]{angle1, angle2};
    }

    private static int [] moveTo51() {
        int angle2 = 87;
        int angle1 = -19;
        return new int[]{angle1, angle2};
    }

    private static int [] moveTo52() {
        int angle2 = 78;
        int angle1 = -2;
        return new int[]{angle1, angle2};
    }

    private static int [] moveTo53() {
        int angle2 = 58;
        int angle1 = 7;
        return new int[]{angle1, angle2};
    }

    private static int [] moveTo54() {
        int angle2 = 38;
        int angle1 = 24;
        return new int[]{angle1, angle2};
    }

    private static int[] defineNextRotation(int x, int y) {
        if (x == 1) {
            if (y == 1) {
                return moveTo11();
            }
            else if (y == 2) {
                return moveTo12();
            }
            else if (y == 3) {
                return moveTo13();
            }
            else return moveTo14();
        }
        else if (x == 2) {
            if (y == 1) {
                return moveTo21();
            }
            else if (y == 2) {
                return moveTo22();
            }
            else if (y == 3) {
                return moveTo23();
            }
            else return moveTo24();
        }
        else if (x == 3) {
            if (y == 1) {
                return moveTo31();
            }
            else if (y == 2) {
                return moveTo32();
            }
            else if (y == 3) {
                return moveTo33();
            }
            else return moveTo34();
        }
        else if (x == 4) {
            if (y == 1) {
                return moveTo41();
            }
            else if (y == 2) {
                return moveTo42();
            }
            else if (y == 3) {
                return moveTo43();
            }
            else return moveTo44();
        }
        else {
            if (y == 1) {
                return moveTo51();
            }
            else if (y == 2) {
                return moveTo52();
            }
            else if (y == 3) {
                return moveTo53();
            }
            else return moveTo54();
        }
    }
}
