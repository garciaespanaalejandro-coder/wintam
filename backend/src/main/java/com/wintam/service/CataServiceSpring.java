package com.wintam.service;

import com.wintam.dto.*;
import com.wintam.exception.CataNotFoundException;
import com.wintam.exception.InvalidCataStatusException;
import com.wintam.exception.UnuathorizedException;
import com.wintam.model.Cata;
import com.wintam.model.CataStatus;
import com.wintam.model.User;
import com.wintam.repository.CataRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class CataServiceSpring implements CataService{
    private final CataRepository cataDAO;
    @PersistenceContext
    private EntityManager entityManager;

    public CataServiceSpring(CataRepository cataDAO) {
        this.cataDAO = cataDAO;
    }

    @Transactional
    @Override
    public MessageResponse createCata(CreateCataRequest request) {
        User usuario = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Cata cata1= Cata.builder()
                .title(request.getTitle())
                .wineType(request.getWineType())
                .experienceLevel(request.getExperienceLevel())
                .location(request.getLocation())
                .scheduleDate(request.getScheduleDate())
                .scheduledTime(request.getScheduledTime())
                .maxAttendees(request.getMaxAttendees())
                .host(usuario)
                .build();
        cataDAO.save(cata1);
        return new MessageResponse("Cata creada correctamente.");
    }

    @Override
    public List<CataResponse> searchCatas(SearchCatasRequest request) {
        List<CataResponse> catas= new ArrayList<>();
        Map<String, Object> parametros = new HashMap<>();

        StringBuilder jpql = new StringBuilder("SELECT c FROM Cata c WHERE 1=1 ");

        // APLICAR FILTROS DINÁMICOS
        if(request.getTitle()!= null && !request.getTitle().trim().isEmpty()){
            jpql.append("AND c.title = :title ");
            parametros.put("title",request.getTitle());
        }
        if(request.getWineType()!= null && !request.getWineType().trim().isEmpty()){
            jpql.append("AND c.wineType = :wineType ");
            parametros.put("wineType",request.getWineType());
        }
        if (request.getExperienceLevel() != null){
            jpql.append("AND c.experienceLevel = :experienceLevel ");
            parametros.put("experienceLevel",request.getExperienceLevel());
        }
        if (request.getLocation()!= null && !request.getLocation().trim().isEmpty()){
            jpql.append("AND c.location = :location ");
            parametros.put("location",request.getLocation());
        }
        if (request.getCataStatus()!=null){
            jpql.append("AND c.status = :cataStatus ");
            parametros.put("cataStatus",request.getCataStatus());
        }

        TypedQuery<Cata> query = entityManager.createQuery(jpql.toString(), Cata.class);
        for (Map.Entry<String, Object> entry : parametros.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        List<Cata> resultado= query.getResultList();

        for (Cata cata : resultado) {
            catas.add(cataToCataResponse(cata));
        }
        return catas;
    }

    @Transactional
    @Override
    public MessageResponse cancelCata(Long id) {
        Cata cata = cataDAO.findById(id)
                .orElseThrow(() -> new CataNotFoundException(id));
        User usuario = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!cata.getHost().getId().equals(usuario.getId())){
            throw new UnuathorizedException();
        }
        if (cata.getStatus() == CataStatus.CANCELLED || cata.getStatus() == CataStatus.COMPLETED) {
            throw new InvalidCataStatusException(cata.getStatus());
        }
        cata.setStatus(CataStatus.CANCELLED);
        cataDAO.save(cata);
        return new MessageResponse("Cata cancelada correctamente");
    }

    @Transactional
    @Override
    public AttendanceCodeResponse startCata(Long id) {
        Cata cata = cataDAO.findById(id)
                .orElseThrow(() -> new CataNotFoundException(id));
        User usuario = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!cata.getHost().getId().equals(usuario.getId())){
            throw new UnuathorizedException();
        }
        String code=getCode();
        cata.setAttendanceCode(code);
        cata.setCodeGeneratedAt(LocalDateTime.now());
        cataDAO.save(cata);
        return new AttendanceCodeResponse(code, cata.getCodeGeneratedAt());

    }

    @Override
    @Transactional
    public List<CataResponse> getAllCatas() {
        List<CataResponse> cataResponseList= new ArrayList<>();
        List<Cata> cataList= this.cataDAO.findAll();
        for (Cata cata : cataList) {
            cataResponseList.add(cataToCataResponse(cata));
        }
        return cataResponseList;
    }

    public CataResponse cataToCataResponse(Cata cata){
        CataResponse dto= new CataResponse();
        dto.setId(cata.getId());
        dto.setCataStatus(cata.getStatus());
        dto.setHostUsername(cata.getHost().getUsername());
        dto.setWineType(cata.getWineType());
        dto.setTitle(cata.getTitle());
        dto.setExperienceLevel(cata.getExperienceLevel());
        dto.setLocation(cata.getLocation());
        dto.setScheduleDate(cata.getScheduleDate());
        dto.setScheduledTime(cata.getScheduledTime());
        dto.setMaxAttendees(cata.getMaxAttendees());
        return dto;
    }

    private String getCode(){
        return String.valueOf((int)(Math.random() * 90000) + 10000);
    }
}
