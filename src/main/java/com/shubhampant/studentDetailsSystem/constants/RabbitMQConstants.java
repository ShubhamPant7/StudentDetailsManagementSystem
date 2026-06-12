package com.shubhampant.studentDetailsSystem.constants;

public final class RabbitMQConstants {

    private RabbitMQConstants() {}

    public static final String STUDENT_EXCHANGE = "student.exchange";

    public static final String STUDENT_CREATED = "student.created";

    public static final String STUDENT_UPDATED = "student.updated";

    public static final String STUDENT_DELETED = "student.deleted";

    public static final String STUDENT_EXCEL_UPLOADED = "student.excel.uploaded";
}
