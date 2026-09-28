package com.example.examcraft_api.service;

import com.example.examcraft_api.model.Unit;

import java.util.List;

public interface UnitService {

    Unit addUnit(Unit unit);

    List<Unit> getAllUnits();
}