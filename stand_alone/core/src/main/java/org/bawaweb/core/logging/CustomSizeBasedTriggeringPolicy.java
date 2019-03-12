package org.bawaweb.core.logging;

/**
 * Created by medhoran on 1/6/14.
 * http://stackoverflow.com/questions/4705957/log4j-xml-configuration-with-rollingpolicy-and-triggeringpolicy
 * http://stackoverflow.com/questions/4705957/log4j-xml-configuration-with-rollingpolicy-and-triggeringpolicy
 */
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.Appender;
import org.apache.log4j.rolling.TriggeringPolicy;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.OptionHandler;

/**
 * Copy of org.apache.log4j.rolling.SizeBasedTriggeringPolicy but able to accept
 * a human-friendly value for maximumFileSize, eg. "10MB"
 *
 * Note that sub-classing SizeBasedTriggeringPolicy is not possible because that
 * class is final
 */
public class CustomSizeBasedTriggeringPolicy implements TriggeringPolicy, OptionHandler {

    /**
     * Rollover threshold size in bytes.
     */
    private long maximumFileSize = 10 * 1024 * 1024; // let 10 MB the default max size

    /**
     * Set the maximum size that the output file is allowed to reach before
     * being rolled over to backup files.
     *
     * <p>
     * In configuration files, the <b>MaxFileSize</b> option takes an long
     * integer in the range 0 - 2^63. You can specify the value with the
     * suffixes "KB", "MB" or "GB" so that the integer is interpreted being
     * expressed respectively in kilobytes, megabytes or gigabytes. For example,
     * the value "10KB" will be interpreted as 10240.
     *
     * @param value
     *            the maximum size that the output file is allowed to reach
     */
    public void setMaxFileSize(String value) {
        maximumFileSize = OptionConverter.toFileSize(value, maximumFileSize + 1);
    }

    public long getMaximumFileSize() {
        return maximumFileSize;
    }

    public void setMaximumFileSize(long maximumFileSize) {
        this.maximumFileSize = maximumFileSize;
    }

    public void activateOptions() {
    }

    public boolean isTriggeringEvent(Appender appender, LoggingEvent event,
                                     String filename, long fileLength) {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        boolean result = (fileLength >= maximumFileSize);

        return result;
    }

}

