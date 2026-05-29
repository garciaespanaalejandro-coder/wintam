package com.wintam.service;

import com.wintam.dto.*;

import java.util.List;

public interface CataService {
    MessageResponse createCata(CreateCataRequest createCataRequest);
    List<CataResponse> searchCatas(SearchCatasRequest searchCatasRequest);
    MessageResponse cancelCata(Long id);
    AttendanceCodeResponse startCata(Long id);
    List<CataResponse> getAllCatas();
}
