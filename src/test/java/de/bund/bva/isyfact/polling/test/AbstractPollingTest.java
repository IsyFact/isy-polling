package de.bund.bva.isyfact.polling.test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public abstract class AbstractPollingTest {

    /**
     * Tests the JMX status and prints information on the console.
     * @return true, if JMX is active, otherwise false.
     */
    public boolean pruefeJMXStatus() throws Exception {

        if (System.getProperty("com.sun.management.jmxremote") == null) {
            IO.println("JMX remote ist disabled");
            return false;
        } else {
            IO.println("JMX remote is enabled");
            if (System.getProperty("com.sun.management.jmxremote.port") != null) {
                IO.println("JMX running on port "
                        + Integer.parseInt(
                        System.getProperty("com.sun.management.jmxremote.port")));
            }
        }

        return true;
    }
}
