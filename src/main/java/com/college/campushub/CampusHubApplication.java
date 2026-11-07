package com.college.campushub;

import java.util.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
@RequestMapping("/api")
@CrossOrigin
public class CampusHubApplication {
  private final JdbcTemplate db;
  public CampusHubApplication(JdbcTemplate db) { this.db = db; }
  public static void main(String[] args) { SpringApplication.run(CampusHubApplication.class, args); }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody Map<String,String> body) {
    String id = body.getOrDefault("studentId","").trim();
    String name = body.getOrDefault("studentName","").trim();
    if (id.isBlank() || name.isBlank()) return ResponseEntity.badRequest().body(Map.of("message","Enter your student name and ID."));
    List<Map<String,Object>> rows = db.queryForList("SELECT student_id, full_name, department, year_of_study, email FROM students WHERE student_id=? AND LOWER(full_name)=LOWER(?)", id, name);
    if (rows.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message","Student details were not found. Check your name and ID or contact the administrator."));
    return ResponseEntity.ok(rows.get(0));
  }

  @GetMapping("/dashboard/{id}")
  public Map<String,Object> dashboard(@PathVariable String id) {
    return Map.of("registered",count("SELECT COUNT(*) FROM registrations WHERE student_id=?",id),
      "attended",count("SELECT COUNT(*) FROM attendance WHERE student_id=? AND status='PRESENT'",id),
      "clubs",count("SELECT COUNT(*) FROM memberships WHERE student_id=?",id),
      "upcoming",count("SELECT COUNT(*) FROM registrations r JOIN events e ON e.event_id=r.event_id WHERE r.student_id=? AND CONCAT(e.event_date,' ',e.start_time)>=NOW()",id));
  }
  private int count(String sql,String id) { Integer n=db.queryForObject(sql,Integer.class,id); return n==null?0:n; }

  @GetMapping("/events")
  public List<Map<String,Object>> events(@RequestParam(defaultValue="") String category) {
    if(category.isBlank()) return db.queryForList("SELECT e.*, (SELECT COUNT(*) FROM registrations r WHERE r.event_id=e.event_id) registered_count FROM events e ORDER BY event_date,start_time");
    return db.queryForList("SELECT e.*, (SELECT COUNT(*) FROM registrations r WHERE r.event_id=e.event_id) registered_count FROM events e WHERE category=? ORDER BY event_date,start_time",category);
  }
  @PostMapping("/events/{eventId}/register")
  public ResponseEntity<?> register(@PathVariable int eventId,@RequestBody Map<String,String> body) {
    String id=body.getOrDefault("studentId","");
    if(db.queryForObject("SELECT COUNT(*) FROM registrations WHERE student_id=? AND event_id=?",Integer.class,id,eventId)>0)
      return ResponseEntity.status(409).body(Map.of("message","You are already registered for this event."));
    Integer seats=db.queryForObject("SELECT capacity-(SELECT COUNT(*) FROM registrations WHERE event_id=?) FROM events WHERE event_id=?",Integer.class,eventId,eventId);
    if(seats==null) return ResponseEntity.notFound().build();
    if(seats<=0) return ResponseEntity.badRequest().body(Map.of("message","This event is full."));
    db.update("INSERT INTO registrations(student_id,event_id) VALUES(?,?)",id,eventId);
    return ResponseEntity.ok(Map.of("message","Registration successful."));
  }
  @GetMapping("/clubs")
  public List<Map<String,Object>> clubs() { return db.queryForList("SELECT c.*, (SELECT COUNT(*) FROM memberships m WHERE m.club_id=c.club_id) member_count FROM clubs c ORDER BY club_name"); }
  @PostMapping("/clubs/{clubId}/join")
  public ResponseEntity<?> join(@PathVariable int clubId,@RequestBody Map<String,String> body) {
    String id=body.getOrDefault("studentId","");
    if(db.queryForObject("SELECT COUNT(*) FROM memberships WHERE student_id=? AND club_id=?",Integer.class,id,clubId)>0)
      return ResponseEntity.status(409).body(Map.of("message","You already belong to this club."));
    db.update("INSERT INTO memberships(student_id,club_id) VALUES(?,?)",id,clubId);
    return ResponseEntity.ok(Map.of("message","Club membership added."));
  }
  @GetMapping("/schedules")
  public List<Map<String,Object>> schedules() { return db.queryForList("SELECT event_id,event_name,category,event_date,start_time,end_time,venue,description FROM events ORDER BY event_date,start_time"); }
  @GetMapping("/attendance/{id}")
  public List<Map<String,Object>> attendance(@PathVariable String id) {
    return db.queryForList("SELECT e.event_name,e.event_date,e.venue,a.status,a.marked_at FROM attendance a JOIN events e ON e.event_id=a.event_id WHERE a.student_id=? ORDER BY e.event_date DESC",id);
  }
  @GetMapping("/registrations/{id}")
  public List<Map<String,Object>> registrations(@PathVariable String id) {
    return db.queryForList("SELECT e.event_id,e.event_name,e.category,e.event_date,e.start_time,e.venue,r.registered_at FROM registrations r JOIN events e ON e.event_id=r.event_id WHERE r.student_id=? ORDER BY e.event_date",id);
  }
  @GetMapping("/profile/{id}")
  public ResponseEntity<?> profile(@PathVariable String id) {
    List<Map<String,Object>> rows=db.queryForList("SELECT student_id,full_name,department,year_of_study,email FROM students WHERE student_id=?",id);
    return rows.isEmpty()?ResponseEntity.notFound().build():ResponseEntity.ok(rows.get(0));
  }
  @GetMapping("/health")
  public Map<String,String> health() { return Map.of("status","ok","application","Campus Club Hub"); }
}