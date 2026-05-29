package com.booking.controller;

import com.booking.dto.request.AddSessionsRequest;
import com.booking.dto.request.CreateOfferingRequest;
import com.booking.dto.request.CreateUserRequest;
import com.booking.entity.Offering;
import com.booking.entity.Session;
import com.booking.entity.Teacher;
import com.booking.repository.TeacherRepository;
import com.booking.service.OfferingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {
    private final OfferingService offeringService;
    private final TeacherRepository teacherRepository;

    public TeacherController(OfferingService offeringService, TeacherRepository teacherRepository) {
        this.offeringService = offeringService;
        this.teacherRepository = teacherRepository;
    }

    @PostMapping
    public ResponseEntity<Teacher> createTeacher(@RequestBody CreateUserRequest req) {
        Teacher t = new Teacher();
        t.setName(req.getName());
        Teacher saved = teacherRepository.save(t);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/{teacherId}/offerings")
    public ResponseEntity<Offering> createOffering(@PathVariable Long teacherId, @RequestBody CreateOfferingRequest req) {
        Offering o = offeringService.createOffering(teacherId, req);
        return ResponseEntity.ok(o);
    }

    @PostMapping("/{teacherId}/offerings/{offeringId}/sessions")
    public ResponseEntity<List<Session>> addSessions(@PathVariable Long teacherId, @PathVariable Long offeringId, @RequestBody AddSessionsRequest req) {
        List<Session> sessions = offeringService.addSessions(teacherId, offeringId, req);
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/{teacherId}/offerings")
    public ResponseEntity<List<Offering>> listOfferings(@PathVariable Long teacherId) {
        return ResponseEntity.ok(offeringService.listOfferingsByTeacher(teacherId));
    }
}
