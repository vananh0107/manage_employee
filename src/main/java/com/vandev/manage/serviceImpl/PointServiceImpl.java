package com.vandev.manage.serviceImpl;

import com.vandev.manage.pojo.Employee;
import com.vandev.manage.pojo.Point;
import com.vandev.manage.repository.PointRepository;
import com.vandev.manage.service.PointService;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PointServiceImpl implements PointService {

    private final PointRepository pointRepository;

    @Autowired
    public PointServiceImpl(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    @Override
    public Point createPoint(Point point) {
        if (point.getReason() == null || point.getReason().trim().isEmpty()) {
            throw new ValidationException("Reason is required.");
        }
        if (point.getRecordedDate() == null) {
            throw new ValidationException("Recorded date is required.");
        }
        return pointRepository.save(point);
    }

    @Override
    public Point updatePoint(Integer pointId, Point point) {
        Optional<Point> existingPoint = pointRepository.findById(pointId);
        if (existingPoint.isPresent()) {
            Point updatedPoint = existingPoint.get();
            updatedPoint.setType(point.isType());
            updatedPoint.setReason(point.getReason());
            updatedPoint.setRecordedDate(point.getRecordedDate());
            updatedPoint.setEmployee(point.getEmployee());
            return pointRepository.save(updatedPoint);
        } else {
            throw new IllegalArgumentException("Point record not found.");
        }
    }

    @Override
    public void deletePoint(Integer pointId) {
        if (!pointRepository.existsById(pointId)) {
            throw new IllegalArgumentException("Point record not found.");
        }
        pointRepository.deleteById(pointId);
    }

    @Override
    public Point getPointById(Integer pointId) {
        return pointRepository.findById(pointId)
                .orElseThrow(() -> new IllegalArgumentException("Point record not found."));
    }

    @Override
    public List<Point> getAllPoints() {
        return pointRepository.findAll();
    }


    @Override
    public List<Point> getPointsByEmployeeId(Integer employeeId) {
        return pointRepository.findByEmployee_Id(employeeId);
    }
}