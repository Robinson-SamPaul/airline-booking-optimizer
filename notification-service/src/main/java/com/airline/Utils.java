package com.airline;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

public class Utils {

    private Utils() {}

    private static final Config CONFIG = ConfigProvider.getConfig();
    public static final String SENDER_MAIL_ID = CONFIG.getValue("quarkus.mail.from", String.class);
    public static final String SENDER_USER_NAME = CONFIG.getValue("quarkus.mail.username", String.class);
    public static final String SENDER_PASSWORD = CONFIG.getValue("quarkus.mail.password", String.class);
    public static final String SENDER_HOST = CONFIG.getValue("quarkus.mail.host", String.class);
    public static final String SENDER_PORT = CONFIG.getValue("quarkus.mail.port", String.class);
    public static final String SENDER_START_TLS = CONFIG.getValue("quarkus.mail.start-tls", String.class);
    public static final String SENDER_AUTH = CONFIG.getValue("quarkus.mail.auth", String.class);
    public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    public static final String MAIL_SMTP_START_TLS = "mail.smtp.starttls.enable";
    public static final String MAIL_SMTP_HOST = "mail.smtp.host";
    public static final String MAIL_SMTP_PORT = "mail.smtp.port";
}
