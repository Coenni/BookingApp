package com.outfittery.booking.web.rest.errors;

import java.net.URI;

public final class ErrorConstants {

    public static final String PROBLEM_BASE_URL = "https://www.outfittery.de/api/problem";
    public static final URI DEFAULT_TYPE = URI.create(PROBLEM_BASE_URL + "/problem-with-message");
    public static final String NO_CONTENT = "nocontent";
    public static final String NOT_VALID_INPUT_DATA = "inputData.notValid";
    public static final String NO_CUSTOMER_FOUND = "customer.notexist";
    public static final String NO_TIME_SLOT_FOUND = "timeslot.notexist";
    public static final String ID_EXISTS = "id.exists";

    private ErrorConstants() {
    }
}
