package com.wintam.service;

import com.wintam.dto.AttendeeResponse;
import com.wintam.dto.ConfirmAttendanceRequest;
import com.wintam.dto.MessageResponse;

import java.util.List;

public interface InscripcionService {
    List<AttendeeResponse> getAttendees(Long id);
    MessageResponse joinCata(Long id);
    MessageResponse cancelJoin(Long id);
    MessageResponse confirmAttendance(ConfirmAttendanceRequest request);
}
