package de.bund.bva.isyfact.polling.impl;

import de.bund.bva.isyfact.datetime.test.TestClock;
import de.bund.bva.isyfact.polling.PollingMBean;
import de.bund.bva.isyfact.polling.PollingVerwalter;
import de.bund.bva.isyfact.polling.autoconfigure.IsyPollingAutoConfiguration;
import de.bund.bva.isyfact.polling.config.IsyPollingProperties;
import de.bund.bva.isyfact.polling.test.AbstractPollingTest;
import de.bund.bva.isyfact.polling.test.TestConfig;
import de.bund.bva.isyfact.util.datetime.DateTimeUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.export.annotation.AnnotationJmxAttributeSource;
import org.springframework.jmx.export.assembler.MetadataMBeanInfoAssembler;
import org.springframework.jmx.support.RegistrationPolicy;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests for the polling manager.
 * <p>
 * For the tests to work, JMX must be enabled using the following VM startup parameters:
 * <p>
 * -Dcom.sun.management.jmxremote
 * -Dcom.sun.management.jmxremote.port=9010
 * -Dcom.sun.management.jmxremote.local.only=false
 * -Dcom.sun.management.jmxremote.ssl=false
 * -Dcom.sun.management.jmxremote.authenticate=false
 */
@SpringBootTest(classes = {
        TestConfig.class, PollingVerwalterDefaultJmxConnTest.TestConfig.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE, properties = {
        "isy.logging.anwendung.name = test",
        "isy.logging.anwendung.typ = test",
        "isy.logging.anwendung.version = test",

        "isy.polling.jmx.verbindungen.SERVER1.host = localhost",
        "isy.polling.jmx.verbindungen.SERVER1.port = 9010",
        "isy.polling.jmx.verbindungen.SERVER1.benutzer = server1",
        "isy.polling.jmx.verbindungen.SERVER1.passwort = server1",

        "isy.polling.jmx.verbindungen.SERVER2.host = localhost",
        "isy.polling.jmx.verbindungen.SERVER2.port = 9010",
        "isy.polling.jmx.verbindungen.SERVER2.benutzer = SERVER2",
        "isy.polling.jmx.verbindungen.SERVER2.passwort = SERVER2",

        "isy.polling.cluster.CLUSTER1.name = Name-Cluster1", "isy.polling.cluster.CLUSTER1.wartezeit = 12"})
@ImportAutoConfiguration(IsyPollingAutoConfiguration.class)
public class PollingVerwalterDefaultJmxConnTest extends AbstractPollingTest {

    @Autowired
    private PollingVerwalter pollingVerwalter;

    /**
     * Testing method "startePolling".
     */
    @Test
    public void startePollingTest() throws Exception {

        Assertions.assertTrue(pruefeJMXStatus(), "JMX ist nicht gestartet.");

        TestClock testClock = TestClock.now();
        DateTimeUtil.setClock(testClock);

        // update Cluster 1
        // polling is not allowed to start, because the test is local
        pollingVerwalter.aktualisiereZeitpunktLetztePollingAktivitaet("CLUSTER1");
        Assertions.assertFalse(pollingVerwalter.startePolling("CLUSTER1"), "Polling darf nicht gestartet werden");

        // pass a part of the waiting time.
        testClock.advanceBy(Duration.ofSeconds(5));

        // polling for Cluster1 is still not allowed to be started
        Assertions.assertFalse(pollingVerwalter.startePolling("CLUSTER1"), "Polling darf nicht gestartet werden");

        // pass rest of the waiting time
        testClock.advanceBy(Duration.ofSeconds(8));

        // check Cluster 1 again
        Assertions.assertTrue(pollingVerwalter.startePolling("CLUSTER1"), "Polling darf gestartet werden");
    }

    @Configuration
    static class TestConfig {
        @Bean
        public MBeanExporter mBeanExporter(@Qualifier("cluster1Monitor") PollingMBean cluster1Monitor,
                                           IsyPollingProperties isyPollingProperties) {
            MBeanExporter mBeanExporter = new MBeanExporter();
            mBeanExporter.setRegistrationPolicy(RegistrationPolicy.REPLACE_EXISTING);
            mBeanExporter.setAssembler(new MetadataMBeanInfoAssembler(new AnnotationJmxAttributeSource()));
            mBeanExporter.setAutodetect(false);

            Map<String, Object> mBeans = new HashMap<>();
            String key = "de.bund.bva.isyfact.polling:type=PollingStatus,name=\"Polling-Aktivitaet-"
                    + isyPollingProperties.getCluster().get("CLUSTER1").getName() + "\"";

            mBeans.put(key, cluster1Monitor);

            mBeanExporter.setBeans(mBeans);

            return mBeanExporter;
        }
    }
}
