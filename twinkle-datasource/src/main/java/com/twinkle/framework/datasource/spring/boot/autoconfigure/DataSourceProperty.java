package com.twinkle.framework.datasource.spring.boot.autoconfigure;

import com.twinkle.framework.datasource.spring.boot.autoconfigure.druid.DruidConfig;
import com.twinkle.framework.datasource.spring.boot.autoconfigure.hikari.HikariCpConfig;
import com.twinkle.framework.datasource.utils.CryptoUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Function: Yaml source. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/12/19 6:31 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
@Data
@Accessors(chain = true)
public class DataSourceProperty {
    /**
     * Encrypt regex.
     */
    private static final Pattern ENC_PATTERN = Pattern.compile("^ENC\\((.*)\\)$");

    /**
     * DataSource Name.
     */
    private String pollName;
    /**
     * DataSource Poll Type.
     * Druid > HikariCp
     * Default will use Druid.
     */
    private Class<? extends DataSource> type;
    /**
     * JDBC driver
     */
    private String driverClassName;
    /**
     * JDBC url
     */
    private String url;
    /**
     * JDBC UserName
     */
    private String username;
    /**
     * JDBC Password
     */
    private String password;
    /**
     * jndi DataSource Name.
     */
    private String jndiName;
    /**
     * The schema to run the initial scripts.
     */
    private String schema;
    /**
     * The initial scripts.
     */
    private String data;
    /**
     * If continue once failed,
     * default: true
     */
    private boolean continueOnError = true;
    /**
     * Separator
     * By default ;
     */
    private String separator = ";";
    /**
     * Druid Configurations.
     */
    @NestedConfigurationProperty
    private DruidConfig druid = new DruidConfig();
    /**
     * HikariCp Configurations.
     */
    @NestedConfigurationProperty
    private HikariCpConfig hikari = new HikariCpConfig();

    /**
     * Public Key to do decryption.
     * If not set, will use default Public Key.
     */
    private String publicKey;

    public String getUrl() {
        return decrypt(this.url);
    }

    public String getUsername() {
        return decrypt(this.username);
    }

    public String getPassword() {
        return decrypt(this.password);
    }

    /**
     * Decrypt the text.
     */
    private String decrypt(String _text) {
        if (StringUtils.hasText(_text)) {
            Matcher matcher = ENC_PATTERN.matcher(_text);
            if (matcher.find()) {
                try {
                    return CryptoUtil.decrypt(publicKey, matcher.group(1));
                } catch (Exception e) {
                    log.error("DynamicDataSourceProperties.decrypt error, Exception: {}", e);
                }
            }
        }
        return _text;
    }
}
