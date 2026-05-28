package de.bund.bva.isyfact.polling.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;

import org.junit.jupiter.api.Test;

import de.bund.bva.isyfact.polling.common.exception.PollingClusterKonfigurationException;

class JMXConnectionParameterTest {
    @Test
    void testValidConstructorWithCredentials() {
        String id = "id1";
        String host = "localhost";
        String port = "9999";
        String user = "admin";
        String pass = "secret";
        JMXConnectionParameter param = new JMXConnectionParameter(id, host, port, user, pass);
        assertThat(param.getId()).isEqualTo(id);
        assertThat(param.getIpAdressePort()).isEqualTo(host + ":" + port);
        assertThat(param.getJmxServiceUrl().toString()).contains(host + ":" + port);
        Map<String, ?> env = param.getEnvironment();
        assertThat(env).isNotNull();
        assertThat(env).containsKey("jmx.remote.credentials");
        assertThat((String[]) env.get("jmx.remote.credentials")).containsExactly(user, pass);
    }

    @Test
    void testValidConstructorWithoutCredentials() {
        String id = "id2";
        String host = "localhost";
        String port = "9999";
        JMXConnectionParameter param = new JMXConnectionParameter(id, host, port, null, null);
        assertThat(param.getId()).isEqualTo(id);
        assertThat(param.getIpAdressePort()).isEqualTo(host + ":" + port);
        assertThat(param.getJmxServiceUrl().toString()).contains(host + ":" + port);
        assertThat(param.getEnvironment()).isNull();
    }

    @Test
    void testConstructorMalformedUrlThrowsException() {
        String id = "id3";
        String host = "äöü"; // non ASCII characters for invalid host from JMXServiceURL
        String port = "9999";
        assertThatThrownBy(() ->
            new JMXConnectionParameter(id, host, port, "user", "pass")
        ).isInstanceOf(PollingClusterKonfigurationException.class);
    }
}
