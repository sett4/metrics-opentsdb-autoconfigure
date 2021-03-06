/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.sett4.metrics.opentsdb;

import io.micrometer.core.instrument.Clock;
import io.micrometer.opentsdb.OpenTsdbConfig;
import io.micrometer.opentsdb.OpenTsdbMeterRegistry;

import org.springframework.boot.actuate.autoconfigure.metrics.CompositeMeterRegistryAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.export.simple.SimpleMetricsExportAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for exporting metrics to OpenTSDB.
 *
 * @author Jon Schneider
 * @since 2.0.0
 */
@Configuration
@AutoConfigureBefore({ CompositeMeterRegistryAutoConfiguration.class,
                       SimpleMetricsExportAutoConfiguration.class })
@AutoConfigureAfter(MetricsAutoConfiguration.class)
//@ConditionalOnBean(Clock.class)
@ConditionalOnClass(OpenTsdbMeterRegistry.class)
@ConditionalOnProperty(prefix = "management.metrics.export.opentsdb", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(OpenTsdbProperties.class)
public class OpenTsdbMetricsExportAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OpenTsdbConfig openTsdbConfig(OpenTsdbProperties openTsdbProperties) {
        return new OpenTsdbPropertiesConfigAdapter(openTsdbProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenTsdbMeterRegistry openTsdbMeterRegistry(OpenTsdbConfig openTsdbConfig,
                                                       Clock clock) {
        return new OpenTsdbMeterRegistry(openTsdbConfig, clock);
    }

}
