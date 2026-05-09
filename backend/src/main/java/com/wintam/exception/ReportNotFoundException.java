package com.wintam.exception;

public class ReportNotFoundException extends RuntimeException {
    public ReportNotFoundException() {
        super("Reporte no encontrado.");
    }
}
