package com.dqs.eventdrivensearch.queryDistribution.config;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import de.flapdoodle.embed.mongo.config.Net;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmbeddedMongoConfig {

    @Bean(destroyMethod = "stop")
    public MongodExecutable embeddedMongoServer() throws Exception {
        MongodConfig mongodConfig = MongodConfig.builder()
                .version(Version.Main.V6_0)
                .net(new Net(27017, Network.localhostIsIPv6()))
                .build();

        MongodStarter starter = MongodStarter.getDefaultInstance();
        MongodExecutable mongodExecutable = starter.prepare(mongodConfig);
        mongodExecutable.start();

        return mongodExecutable;
    }
}
