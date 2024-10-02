package com.vandev.manage.service;

import com.vandev.manage.pojo.Point;

import java.util.List;

public interface PointService {
    Point createPoint(Point point);
    Point updatePoint(Integer pointId, Point point);
    void deletePoint(Integer pointId);
    Point getPointById(Integer pointId);
    List<Point> getAllPoints();
    List<Point> getPointsByEmployeeId(Integer employeeId);
}
