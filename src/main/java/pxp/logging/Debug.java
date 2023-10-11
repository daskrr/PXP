package pxp.logging;

import java.util.Arrays;
import java.util.logging.Level;

public class Debug
{
    // LOG
    public static void log(Object message) {
        PXPLogger.getInstance().log(Level.INFO, message.toString());
    }
    public static void log(char message) {
        PXPLogger.getInstance().log(Level.INFO, String.valueOf(message));
    }
    public static void log(int message) {
        PXPLogger.getInstance().log(Level.INFO, String.valueOf(message));
    }
    public static void log(long message) {
        PXPLogger.getInstance().log(Level.INFO, String.valueOf(message));
    }
    public static void log(float message) {
        PXPLogger.getInstance().log(Level.INFO, String.valueOf(message));
    }
    public static void log(double message) {
        PXPLogger.getInstance().log(Level.INFO, String.valueOf(message));
    }
    public static void log(boolean message) {
        PXPLogger.getInstance().log(Level.INFO, String.valueOf(message));
    }
    public static void log(Object[] message) {
        PXPLogger.getInstance().log(Level.INFO, Arrays.toString(message));
    }
    public static void log(char[] message) {
        PXPLogger.getInstance().log(Level.INFO, Arrays.toString(message));
    }
    public static void log(int[] message) {
        PXPLogger.getInstance().log(Level.INFO, Arrays.toString(message));
    }
    public static void log(long[] message) {
        PXPLogger.getInstance().log(Level.INFO, Arrays.toString(message));
    }
    public static void log(float[] message) {
        PXPLogger.getInstance().log(Level.INFO, Arrays.toString(message));
    }
    public static void log(double[] message) {
        PXPLogger.getInstance().log(Level.INFO, Arrays.toString(message));
    }

    // WARNING
    public static void warn(Object message) {
        PXPLogger.getInstance().log(Level.WARNING, message.toString());
    }
    public static void warn(char message) {
        PXPLogger.getInstance().log(Level.WARNING, String.valueOf(message));
    }
    public static void warn(int message) {
        PXPLogger.getInstance().log(Level.WARNING, String.valueOf(message));
    }
    public static void warn(long message) {
        PXPLogger.getInstance().log(Level.WARNING, String.valueOf(message));
    }
    public static void warn(float message) {
        PXPLogger.getInstance().log(Level.WARNING, String.valueOf(message));
    }
    public static void warn(double message) {
        PXPLogger.getInstance().log(Level.WARNING, String.valueOf(message));
    }
    public static void warn(boolean message) {
        PXPLogger.getInstance().log(Level.WARNING, String.valueOf(message));
    }
    public static void warn(Object[] message) {
        PXPLogger.getInstance().log(Level.WARNING, Arrays.toString(message));
    }
    public static void warn(char[] message) {
        PXPLogger.getInstance().log(Level.WARNING, Arrays.toString(message));
    }
    public static void warn(int[] message) {
        PXPLogger.getInstance().log(Level.WARNING, Arrays.toString(message));
    }
    public static void warn(long[] message) {
        PXPLogger.getInstance().log(Level.WARNING, Arrays.toString(message));
    }
    public static void warn(float[] message) {
        PXPLogger.getInstance().log(Level.WARNING, Arrays.toString(message));
    }
    public static void warn(double[] message) {
        PXPLogger.getInstance().log(Level.WARNING, Arrays.toString(message));
    }

    // ERROR
    public static void err(Object message) {
        PXPLogger.getInstance().log(Level.SEVERE, message.toString());
    }
    public static void err(char message) {
        PXPLogger.getInstance().log(Level.SEVERE, String.valueOf(message));
    }
    public static void err(int message) {
        PXPLogger.getInstance().log(Level.SEVERE, String.valueOf(message));
    }
    public static void err(long message) {
        PXPLogger.getInstance().log(Level.SEVERE, String.valueOf(message));
    }
    public static void err(float message) {
        PXPLogger.getInstance().log(Level.SEVERE, String.valueOf(message));
    }
    public static void err(double message) {
        PXPLogger.getInstance().log(Level.SEVERE, String.valueOf(message));
    }
    public static void err(boolean message) {
        PXPLogger.getInstance().log(Level.SEVERE, String.valueOf(message));
    }
    public static void err(Object[] message) {
        PXPLogger.getInstance().log(Level.SEVERE, Arrays.toString(message));
    }
    public static void err(char[] message) {
        PXPLogger.getInstance().log(Level.SEVERE, Arrays.toString(message));
    }
    public static void err(int[] message) {
        PXPLogger.getInstance().log(Level.SEVERE, Arrays.toString(message));
    }
    public static void err(long[] message) {
        PXPLogger.getInstance().log(Level.SEVERE, Arrays.toString(message));
    }
    public static void err(float[] message) {
        PXPLogger.getInstance().log(Level.SEVERE, Arrays.toString(message));
    }
    public static void err(double[] message) {
        PXPLogger.getInstance().log(Level.SEVERE, Arrays.toString(message));
    }
}
