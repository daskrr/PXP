package pxp.logging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public class PXPLogger
{
    private static Logger instance;
    public static Logger getInstance() {
        if (instance == null)
           instance = createLogger();

        return instance;
    }

    /**
     * The default logger for PXP
     */
    private static Logger createLogger() {
        Logger logger = Logger.getLogger(PXPLogger.class.getName());

        logger.setLevel(Level.FINE);

        DefaultHandler handler = new DefaultHandler();
        handler.setFormatter(new DefaultFormatter());
        logger.setUseParentHandlers(false);
        logger.addHandler(handler);

        return logger;
    }

    public static class DefaultHandler extends ConsoleHandler {
        @Override
        public void publish(LogRecord record) {
            super.publish(record);
        }
        @Override
        public void flush() {
            super.flush();
        }
        @Override
        public void close() throws SecurityException {
            super.close();
        }
    }

    public static class DefaultFormatter extends Formatter {
        @Override
        public String format(LogRecord record) {
            DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
            String date = dateFormat.format(new Date(record.getMillis()));

            return "[" + date + "] [" + record.getLevel().getName() + "]: "
                    + record.getMessage()+"\n";
        }
    }
}
