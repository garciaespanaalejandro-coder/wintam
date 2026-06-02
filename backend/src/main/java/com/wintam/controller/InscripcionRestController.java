package com.wintam.controller;

import com.wintam.dto.AttendeeResponse;
import com.wintam.dto.ConfirmAttendanceRequest;
import com.wintam.dto.MessageResponse;
import com.wintam.service.InscripcionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inscripcion")
public class InscripcionRestController {
    private final InscripcionService inscripcionService;

    public InscripcionRestController(InscripcionService inscripcionService) {
        this.inscripcionService = inscripcionService;
    }

    @PostMapping("/joinCata/{id}")
    public ResponseEntity<MessageResponse> joinCata(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(inscripcionService.joinCata(id));
    }

    @PatchMapping("/cancelCata/{id}")
    public ResponseEntity<MessageResponse> cancelCata(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(inscripcionService.cancelJoin(id));
    }

    @PatchMapping("/confirmAttendance")
    public ResponseEntity<MessageResponse> confirmAttendance(@Valid @RequestBody ConfirmAttendanceRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(inscripcionService.confirmAttendance(request));
    }

    @GetMapping("/getAttendees/{cataId}")
    public ResponseEntity<List<AttendeeResponse>> getAttendees(@PathVariable Long cataId){
        return ResponseEntity.ok(inscripcionService.getAttendees(cataId));
    }

    @GetMapping("/getRegistered/{cataId}")
    public ResponseEntity<List<AttendeeResponse>> getRegistered(@PathVariable Long cataId){
        return ResponseEntity.ok(inscripcionService.getRegistered(cataId));
    }

}
