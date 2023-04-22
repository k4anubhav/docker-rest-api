package ai.openfabric.api.config;


import com.yahoo.elide.Elide;
import com.yahoo.elide.ElideSettings;
import com.yahoo.elide.ElideSettingsBuilder;
import com.yahoo.elide.audit.Slf4jLogger;
import com.yahoo.elide.core.DataStore;
import com.yahoo.elide.core.EntityDictionary;
import com.yahoo.elide.core.filter.dialect.RSQLFilterDialect;
import com.yahoo.elide.security.checks.Check;
import com.yahoo.elide.security.checks.prefab.Role;
import com.yahoo.elide.spring.config.ElideConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@Configuration
@Slf4j
public class ElideConfig {

    @Bean
    public EntityDictionary buildDictionary(AutowireCapableBeanFactory beanFactory) {

        Map<String, Class<? extends Check>> checks = new HashMap<>();
        checks.put("allow all", Role.ALL.class);
        checks.put("deny all", Role.NONE.class);
        return new EntityDictionary(checks, beanFactory::autowireBean);
    }

    @Bean
    @Primary
    public Elide elide(EntityDictionary dictionary, DataStore dataStore, ElideConfigProperties elideConfigProperties) {
        ElideSettings settings = new ElideSettingsBuilder(dataStore)
                .withUpdate200Status()
                .withEntityDictionary(dictionary)
                .withDefaultMaxPageSize(elideConfigProperties.getMaxPageSize())
                .withDefaultPageSize(elideConfigProperties.getPageSize())
                .withUseFilterExpressions(true)
                .withJoinFilterDialect(new RSQLFilterDialect(dictionary))
                .withSubqueryFilterDialect(new RSQLFilterDialect(dictionary))
                .withAuditLogger(new Slf4jLogger())
                .withEncodeErrorResponses(true)
                .withISO8601Dates("yyyy-MM-dd'T'HH:mm'Z'", TimeZone.getTimeZone("UTC"))
                .build();
        return new Elide(settings);
    }

}
