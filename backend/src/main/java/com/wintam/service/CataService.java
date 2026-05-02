package com.wintam.service;

import com.wintam.dto.CreateCataRequest;
import com.wintam.dto.MessageResponse;

public interface CataService {
    public MessageResponse createCata(CreateCataRequest createCataRequest);
}
