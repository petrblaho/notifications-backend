package com.redhat.cloud.notifications.connector.email;

import com.redhat.cloud.notifications.connector.ExceptionProcessor;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.Exchange;
import org.apache.camel.http.base.HttpOperationFailedException;

@ApplicationScoped
public class EmailExceptionProcessor extends ExceptionProcessor {

    private static final String HTTP_LOG_MSG = "Message sending failed on %s: [orgId=%s, historyId=%s] " +
            "with status code [%d] and body [%s]";

    @Override
    protected void process(Throwable t, Exchange exchange) {
        if (t instanceof HttpOperationFailedException e) {
            Log.errorf(
                    HTTP_LOG_MSG,
                    getRouteId(exchange),
                    getOrgId(exchange),
                    getExchangeId(exchange),
                    e.getStatusCode(),
                    e.getResponseBody()
            );
        } else {
            logDefault(t, exchange);
        }
    }
}
