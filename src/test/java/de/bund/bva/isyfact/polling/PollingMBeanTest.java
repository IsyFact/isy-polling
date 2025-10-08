package de.bund.bva.isyfact.polling;

import static de.bund.bva.isyfact.polling.common.konstanten.EreignisSchluessel.POLLING_CLUSTER_KONFIG_FEHLER;
import static de.bund.bva.isyfact.polling.common.konstanten.EreignisSchluessel.POLLING_CLUSTER_UNBEKANNT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import de.bund.bva.isyfact.polling.common.exception.PollingClusterKonfigurationException;
import de.bund.bva.isyfact.polling.common.exception.PollingClusterUnbekanntException;

class PollingMBeanTest {

    private static final String TEST_CLUSTER_NAME = "testCluster";

    private PollingVerwalter pollingVerwalter;
    private PollingMBean pollingMBean;

    @BeforeEach
    void setUp() {
        pollingVerwalter = mock(PollingVerwalter.class);
        pollingMBean = new PollingMBean();
        pollingMBean.setPollingVerwalter(pollingVerwalter);
        pollingMBean.setClusterId("testCluster");
    }

    @Nested
    class GetZeitpunktLetztePollingAktivitaet {
        @Test
        void test_normal() throws Exception {
            long timestamp = 123456789L;
            when(pollingVerwalter.getZeitpunktLetztePollingAktivitaet(TEST_CLUSTER_NAME))
                .thenReturn(timestamp);

            Date result = pollingMBean.getZeitpunktLetztePollingAktivitaet();
            assertThat(result).isEqualTo(new Date(timestamp));
        }

        @Test
        void test_konfigurationException() {
            when(pollingVerwalter.getZeitpunktLetztePollingAktivitaet(TEST_CLUSTER_NAME))
                .thenThrow(new PollingClusterKonfigurationException(POLLING_CLUSTER_KONFIG_FEHLER));

            Date result = pollingMBean.getZeitpunktLetztePollingAktivitaet();
            assertThat(result).isEqualTo(new Date(0));
        }

        @Test
        void test_clusterUnbekanntException() {
            when(pollingVerwalter.getZeitpunktLetztePollingAktivitaet(TEST_CLUSTER_NAME))
            .thenThrow(new PollingClusterUnbekanntException(POLLING_CLUSTER_UNBEKANNT));

            Date result = pollingMBean.getZeitpunktLetztePollingAktivitaet();
            assertThat(result).isEqualTo(new Date(0));
        }
    }

    @Nested
    class GetZeitraumLetztePollingAktivitaet {

        @Test
        void test_normal() throws Exception {
            long now = System.currentTimeMillis();
            long timestamp = now - 1000;
            when(pollingVerwalter.getZeitpunktLetztePollingAktivitaet("testCluster")).thenReturn(timestamp);

            long result = pollingMBean.getZeitraumLetztePollingAktivitaet();
            assertThat(result).isGreaterThan(1000);
        }

        @Test
        void test_neverPolled() throws Exception {
            when(pollingVerwalter.getZeitpunktLetztePollingAktivitaet("testCluster")).thenReturn(0L);

            long result = pollingMBean.getZeitraumLetztePollingAktivitaet();
            assertThat(result).isEqualTo(Long.MAX_VALUE);
        }


        @Test
        void test_konfigurationException() throws Exception {
            when(pollingVerwalter.getZeitpunktLetztePollingAktivitaet("testCluster"))
                .thenThrow(new PollingClusterKonfigurationException(POLLING_CLUSTER_KONFIG_FEHLER));

            long result = pollingMBean.getZeitraumLetztePollingAktivitaet();
            assertThat(result).isEqualTo(Long.MAX_VALUE);
        }

        @Test
        void test_clusterUnbekanntException() throws Exception {
            when(pollingVerwalter.getZeitpunktLetztePollingAktivitaet("testCluster"))
                .thenThrow(new PollingClusterUnbekanntException(POLLING_CLUSTER_UNBEKANNT));

            long result = pollingMBean.getZeitraumLetztePollingAktivitaet();
            assertThat(result).isEqualTo(Long.MAX_VALUE);
        }
    }
}