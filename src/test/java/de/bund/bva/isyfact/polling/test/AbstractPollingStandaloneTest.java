package de.bund.bva.isyfact.polling.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.bund.bva.isyfact.datetime.test.TestClock;
import de.bund.bva.isyfact.datetime.util.DateTimeUtil;
import de.bund.bva.isyfact.polling.PollingVerwalter;

/**
 * Tests for the polling manager in standalone mode.
 * 
 * To make the tests, this start parameters must be active for JMX:
 *
 * -Dcom.sun.management.jmxremote
 * -Dcom.sun.management.jmxremote.port=9010
 * -Dcom.sun.management.jmxremote.local.only=false
 * -Dcom.sun.management.jmxremote.ssl=false
 * -Dcom.sun.management.jmxremote.authenticate=false
 *
 */
public abstract class AbstractPollingStandaloneTest extends AbstractPollingTest {

    @Autowired
    protected PollingVerwalter pollingVerwalter;
    
    @Autowired
    protected PollingAktionAusfuehrer pollingAktionAusfuehrer;

    @Test
    public void startePollingTest() throws Exception {

        assertTrue(pruefeJMXStatus(), "JMX ist nicht gestartet.");

        TestClock testClock = TestClock.now();
        DateTimeUtil.setClock(testClock);
        
        // update Cluster 1. Polling may not be started because of "Modus Standalone".
        pollingVerwalter.aktualisiereZeitpunktLetztePollingAktivitaet("CLUSTER1"); 
        assertTrue(pollingVerwalter.startePolling("CLUSTER1"), "Polling darf nicht gestartet werden");

        // update Cluster 2. Polling may be started because of "Modus Standalone".
        assertTrue(pollingVerwalter.startePolling("CLUSTER2"), "Polling darf nicht gestartet werden");

        // pass a part of the waiting time.
        testClock.advanceBy(Duration.ofSeconds(5));

        // update Cluster 1. Polling may be started because of "Modus Standalone".
        assertTrue(pollingVerwalter.startePolling("CLUSTER1"), "Polling darf gestartet werden");
        
        // pass rest of the waiting time.
        testClock.advanceBy(Duration.ofSeconds(8));
        
        // check Cluster 1 again
        assertTrue(pollingVerwalter.startePolling("CLUSTER1"), "Polling darf gestartet werden");

        // For Cluster 3 is no MBean defined and a not existing port so the MBean is not available
        // polling may be executed.
        pollingVerwalter.aktualisiereZeitpunktLetztePollingAktivitaet("CLUSTER3"); 
        assertTrue(pollingVerwalter.startePolling("CLUSTER3"), "Polling darf gestartet werden");        
    }

    @Test
    public void annotationTest() {
        
        // save last execution time
        long ausfuehrungszeitpunkt1 = pollingVerwalter.getZeitpunktLetztePollingAktivitaet("CLUSTER1");
        assertEquals(0, ausfuehrungszeitpunkt1, "Zeitpunkt der letzen Ausführung");
        // execute polling
        pollingAktionAusfuehrer.doPollingAktionClusterKorrekt();
        // read time of last execution
        long ausfuehrungszeitpunkt2 = pollingVerwalter.getZeitpunktLetztePollingAktivitaet("CLUSTER1");
        assertEquals(0, ausfuehrungszeitpunkt2, "Zeitpunkt der letzen Ausführung");

        // perform actions for unknown cluster
        pollingAktionAusfuehrer.doPollingAktionClusterUnbekannt();
    }   
}
