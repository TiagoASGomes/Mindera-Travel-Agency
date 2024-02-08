package org.mindswap.academy.mindera_travel_agency.util;

public class Messages {
    public static final String INVALID_DATE = "Invalid date";
    public static final String INVALID_HOTEL_INFO = "Hotel info cannot be null";
    public static final String INVALID_PAYMENT_STATUS = "Invalid payment status, payment status can only contain upper case letters and underscores";
    public static final String INVALID_FARE_CLASS = "Invalid fare class, fare class can only contain upper case letters and spaces";
    public static final String CANNOT_DELETE_INVOICE = "Cannot delete paid or pending invoice";
    public static final String CANNOT_ALTER_HOTEL_RESERVATION = "Cannot alter hotel reservation with paid or pending invoice";
    public static final String INVALID_NAME = "Invalid name, name must be 2 words or longer and can only contain upper and lower case letters and spaces";
    public static final String INVALID_EMAIL = "Invalid email";
    public static final String INVALID_PHONE_NUMBER = "Invalid phone number";
    public static final String INVALID_PRICE = "Invalid price, price must be greater than 0";
    public static final String INVALID_LUGGAGE_WEIGHT = "Invalid luggage weight, luggage weight must be between 0 and 100 kg";
    public static final String INVALID_CARRY_ON_LUGGAGE = "Invalid carry on luggage";
    public static final String INVALID_INVOICE_ID = "Invalid invoice id, invoice id must be greater than 0";
    public static final String EMAIL_NOT_FOUND = "No user found with email: ";
    public static final String INVALID_PASSWORD = "Invalid Password";
    public static final String INVALID_USER_NAME = "Invalid user name";
    public static final String INVALID_DATE_OF_BIRTH = "Invalid date of birth";
    public static final String CANNOT_ALTER_PLANE_TICKET = "Cannot alter plane ticket with paid or pending invoice";
    public static final String PENDING_PAYMENT = "PENDING";
    public static final String PAID_PAYMENT = "PAID";
    public static final String NOT_REQUESTED_PAYMENT = "NOT_REQUESTED";
    public static final String INVALID_CHECK_IN_OUT_DATE = "Check in date must be before check out date";
    public static final String ROOM_NOT_FOUND = "No room found with id: ";
    public static final String CANNOT_UPDATE_INVOICE = "Cannot update paid invoice.";
    public static final String PAYMENT_STATUS_DUPLICATE = "Payment status name already exists";
    public static final String PAYMENT_STATUS_IN_USE = "Cannot delete payment status in use";
    public static final String DUPLICATE_EMAIL = "Email already exists";
    public static final String PASSWORDS_DID_NOT_MATCH = "Old password does not match password in database";
    public static final String INVOICE_NOT_COMPLETE = "Please finish planning your trip";
    public static final String INVALID_FLIGHT_ID = "Invalid flight id, flight id must greater than 0";
    public static final String INVALID_PRICE_ID = "Invalid price id, price id must be greater than 0";
    public static final String INVOICE_FLIGHT_LIMIT_REACHED = "Max number of flights per invoice reached at 50 flights";
    public static final String FLIGHT_TICKET_ID_NOT_FOUND = "No flight ticket found with id: ";
    public static final String HOTEL_RESERVATION_ID_NOT_FOUND = "No hotel reservation found with id: ";
    public static final String CANNOT_CHANGE_INVOICE = "Cannot change reservation to a different invoice";
    public static final String INVALID_ARRIVAL_DATE = "Invalid arrival date, arrival date must be in the future or present";
    public static final String INVALID_LEAVE_DATE = "Invalid leave date, leave date must be in the future";
    public static final String INVOICE_ID_NOT_FOUND = "No invoice found with id: ";
    public static final String INVALID_USER_ID = "Invalid user id, user id must be greater than 0";
    public static final String INVALID_PAYMENT_DATE = "Invalid payment date, payment date must be in the future or present";
    public static final String STATUS_NAME_NOT_FOUND = "No payment status found with name: ";
    public static final String STATUS_ID_NOT_FOUND = "No payment status found with id: ";
    public static final String USER_ID_NOT_FOUND = "No user found with id: ";
    public static final String INVALID_DURATION = "Invalid duration, duration must be between 0 and 24 hours";
    public static final String INVALID_VAT = "Invalid vat";
    public static final String HOTEL_RESERVATION_ALREADY_EXISTS = "Hotel reservation already exists, delete or update existing reservation";

    private Messages() {
    }

}
