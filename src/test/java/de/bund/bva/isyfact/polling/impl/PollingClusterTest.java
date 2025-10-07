package de.bund.bva.isyfact.polling.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PollingClusterTest {
    private final String jmxDomain = "testDomain";
    private final String clusterId = "cluster1";
    private final String clusterName = "TestCluster";
    private final int wartezeit = 15;
    private List<JMXConnectionParameter> jmxList;

    @BeforeEach
    void setUp() {
        jmxList = Arrays.asList(
            new JMXConnectionParameter("host1", "port1", "user1", "pass1", "service1"),
            new JMXConnectionParameter("host2", "port2", "user2", "pass2", "service2")
        );
    }

    @Test
    void testConstructorValidArguments() {
        PollingCluster cluster = new PollingCluster(jmxDomain, clusterId, clusterName, wartezeit, jmxList);
        assertThat(cluster.getClusterId()).isEqualTo(clusterId);
        assertThat(cluster.getClusterName()).isEqualTo(clusterName);
        assertThat(cluster.getWartezeit()).isEqualTo(wartezeit);
        assertThat(cluster.getJmxConnectionParameter()).hasSize(jmxList.size());
        assertThat(cluster.getMBeanObjektName()).contains(clusterName);
    }

    @Test
    void testConstructorNullClusterId() {
        assertThatThrownBy(() ->
            new PollingCluster(jmxDomain, null, clusterName, wartezeit, jmxList)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testConstructorNullClusterName() {
        assertThatThrownBy(() ->
            new PollingCluster(jmxDomain, clusterId, null, wartezeit, jmxList)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testConstructorWartezeitTooSmall() {
        assertThatThrownBy(() ->
            new PollingCluster(jmxDomain, clusterId, clusterName, 5, jmxList)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testConstructorNullJmxList() {
        assertThatThrownBy(() ->
            new PollingCluster(jmxDomain, clusterId, clusterName, wartezeit, null)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testGetJmxConnectionParameterEmpty() {
        PollingCluster cluster = new PollingCluster(jmxDomain, clusterId, clusterName, wartezeit, Collections.emptyList());
        assertThat(cluster.getJmxConnectionParameter()).hasSize(0);
    }

    @Test
    void testGetZeitpunktLetztePollingAktivitaetAndUpdate() {
        PollingCluster cluster = new PollingCluster(jmxDomain, clusterId, clusterName, wartezeit, jmxList);
        long before = cluster.getZeitpunktLetztePollingAktivitaet();
        cluster.aktualisiereZeitpunktLetztePollingAktivitaet();
        long after = cluster.getZeitpunktLetztePollingAktivitaet();
        assertThat(after).isGreaterThanOrEqualTo(before);
    }
}
