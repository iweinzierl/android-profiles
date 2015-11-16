package de.iweinzierl.easyprofiles.util.geo;

import com.google.common.base.Strings;

public class GeofenceRequestIdGenerator {

    private static final char SPLIT_CHARACTER = '#';

    private static final String REQUEST_ID_TEMPLATE = "location-based-trigger" + SPLIT_CHARACTER + "%s";

    public String generateRequestId(long triggerId) {
        return String.format(REQUEST_ID_TEMPLATE, triggerId);
    }

    public long extractTriggerId(String requestId) throws IllegalArgumentException {
        if (Strings.isNullOrEmpty(requestId)) {
            throw new IllegalArgumentException("RequestId is null or empty: " + requestId);
        }

        String[] parts = requestId.split("#");

        if (parts.length >= 2) {
            return Long.valueOf(parts[parts.length - 1]);
        }

        throw new IllegalArgumentException("RequestId has wrong format - no trigger id could be extracted: " + requestId);
    }
}
