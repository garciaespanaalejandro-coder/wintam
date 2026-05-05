package com.wintam.service;

import com.wintam.dto.ConfirmAttendanceRequest;
import com.wintam.dto.MessageResponse;

public interface InscripcionService {
    MessageResponse joinCata(Long id);
    MessageResponse cancelJoin(Long id);
    MessageResponse confirmAttendance(ConfirmAttendanceRequest request);
}
