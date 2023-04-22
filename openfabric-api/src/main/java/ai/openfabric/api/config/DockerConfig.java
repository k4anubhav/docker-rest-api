package ai.openfabric.api.config;


import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DockerConfig {

    static public DockerClientConfig dockerClientConfig() {
        return DefaultDockerClientConfig.createDefaultConfigBuilder().build();
    }

    static public DockerHttpClient dockerHttpClient(DockerClientConfig config) {
        return new ApacheDockerHttpClient.Builder().dockerHost(config.getDockerHost()).sslConfig(config.getSSLConfig()).build();
    }

    static public DockerClient dockerClient(DockerClientConfig config, DockerHttpClient httpClient) {
        return DockerClientImpl.getInstance(config, httpClient);
    }

    @Bean
    public DockerClient dockerClientBean() {
        return dockerClient(dockerClientConfig(), dockerHttpClient(dockerClientConfig()));
    }

}
