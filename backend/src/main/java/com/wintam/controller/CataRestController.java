package com.wintam.controller;

import com.wintam.dto.*;
import com.wintam.model.CataStatus;
import com.wintam.model.ExperienceLevel;
import com.wintam.service.CataService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cata")
public class CataRestController {
    private final CataService cataService;


    public CataRestController(CataService cataService) {
        this.cataService = cataService;
    }

    @PostMapping("/createCata")
    public ResponseEntity<MessageResponse> createCata(@Valid @RequestBody CreateCataRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(cataService.createCata(request));
    }

    @GetMapping("/searchCata")
    public ResponseEntity<List<CataResponse>> searchCata(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String wineType,
            @RequestParam(required = false) ExperienceLevel experienceLevel,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) CataStatus cataStatus){
        SearchCatasRequest request= new SearchCatasRequest();
        request.setTitle(title);
        request.setWineType(wineType);
        request.setExperienceLevel(experienceLevel);
        request.setLocation(location);
        request.setCataStatus(cataStatus);

        return ResponseEntity.ok(cataService.searchCatas(request));
    }

    @PatchMapping("/cancel/{id}")
    public ResponseEntity<MessageResponse> cancelCata(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(cataService.cancelCata(id));
    }

    @PatchMapping("/startCata/{id}")
    public ResponseEntity<AttendanceCodeResponse> startCata(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(cataService.startCata(id));
    }

    @GetMapping("/getAllCatas")
    public ResponseEntity <List<CataResponse>> getAllCatas(){
        return ResponseEntity.status(HttpStatus.OK).body(cataService.getAllCatas());
    }


}
