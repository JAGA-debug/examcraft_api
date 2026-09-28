package com.example.examcraft_api.service;

import com.example.examcraft_api.model.Unit;
import com.example.examcraft_api.repository.UnitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnitServiceImplementation implements UnitService {

    private final UnitRepository unitRepository;

    public UnitServiceImplementation(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    @Override
    public Unit addUnit(Unit unit) {
        return unitRepository.save(unit);
    }

    @Override
    public List<Unit> getAllUnits() {
        return unitRepository.findAll();
    }
}