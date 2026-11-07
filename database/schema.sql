CREATE DATABASE IF NOT EXISTS college_club_management;
USE college_club_management;

CREATE TABLE IF NOT EXISTS students (
  student_id VARCHAR(30) PRIMARY KEY,
  full_name VARCHAR(120) NOT NULL,
  department VARCHAR(80) NOT NULL,
  year_of_study TINYINT NOT NULL,
  email VARCHAR(140) UNIQUE
);
CREATE TABLE IF NOT EXISTS clubs (
  club_id INT AUTO_INCREMENT PRIMARY KEY,
  club_name VARCHAR(100) NOT NULL UNIQUE,
  category VARCHAR(40) NOT NULL,
  description TEXT,
  coordinator VARCHAR(120)
);
CREATE TABLE IF NOT EXISTS memberships (
  student_id VARCHAR(30) NOT NULL,
  club_id INT NOT NULL,
  joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY(student_id,club_id),
  FOREIGN KEY(student_id) REFERENCES students(student_id),
  FOREIGN KEY(club_id) REFERENCES clubs(club_id)
);
CREATE TABLE IF NOT EXISTS events (
  event_id INT AUTO_INCREMENT PRIMARY KEY,
  event_name VARCHAR(140) NOT NULL,
  category VARCHAR(40) NOT NULL,
  description TEXT,
  event_date DATE NOT NULL,
  start_time TIME NOT NULL,
  end_time TIME NOT NULL,
  venue VARCHAR(140) NOT NULL,
  capacity INT NOT NULL DEFAULT 100
);
CREATE TABLE IF NOT EXISTS registrations (
  registration_id INT AUTO_INCREMENT PRIMARY KEY,
  student_id VARCHAR(30) NOT NULL,
  event_id INT NOT NULL,
  registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uq_student_event(student_id,event_id),
  FOREIGN KEY(student_id) REFERENCES students(student_id),
  FOREIGN KEY(event_id) REFERENCES events(event_id)
);
CREATE TABLE IF NOT EXISTS attendance (
  attendance_id INT AUTO_INCREMENT PRIMARY KEY,
  student_id VARCHAR(30) NOT NULL,
  event_id INT NOT NULL,
  status ENUM('PRESENT','ABSENT') NOT NULL,
  marked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uq_attendance_student_event(student_id,event_id),
  FOREIGN KEY(student_id) REFERENCES students(student_id),
  FOREIGN KEY(event_id) REFERENCES events(event_id)
);
CREATE TABLE IF NOT EXISTS scores (
  score_id INT AUTO_INCREMENT PRIMARY KEY,
  student_id VARCHAR(30) NOT NULL,
  event_id INT NOT NULL,
  score DECIMAL(7,2) NOT NULL DEFAULT 0,
  UNIQUE KEY uq_score_student_event(student_id,event_id),
  FOREIGN KEY(student_id) REFERENCES students(student_id),
  FOREIGN KEY(event_id) REFERENCES events(event_id)
);

INSERT IGNORE INTO students(student_id,full_name,department,year_of_study,email) VALUES
('23CSE001','Abhinav Reddy','Computer Science',2,'abhinav@example.edu'),
('23ECE014','Kiran Kumar','Electronics',2,'kiran@example.edu');
INSERT IGNORE INTO clubs(club_id,club_name,category,description,coordinator) VALUES
(1,'CodeCraft','Technical','Programming, coding contests and developer meetups.','Faculty Coordinator'),
(2,'RoboSphere','Technical','Robotics, automation and hardware projects.','Faculty Coordinator'),
(3,'Rhythm','Cultural','Music, dance and stage performances.','Cultural Coordinator'),
(4,'Pixel Studio','Creative','Photography, design and visual storytelling.','Creative Coordinator'),
(5,'Arena','Sports','Cricket, football, badminton and athletics.','Sports Coordinator');
INSERT IGNORE INTO events(event_id,event_name,category,description,event_date,start_time,end_time,venue,capacity) VALUES
(1,'Campus Hackathon','Technical','Build a useful prototype with a team in this 8-hour hackathon.','2026-11-18','09:00:00','17:00:00','Innovation Lab',120),
(2,'Code Sprint','Technical','Competitive problem solving and algorithm challenges.','2026-11-20','10:00:00','13:00:00','Computer Lab 2',80),
(3,'Battle of Bands','Cultural','A live music competition for college bands.','2026-11-22','16:00:00','19:00:00','Open Air Theatre',200),
(4,'Street Dance','Cultural','Solo and group dance performances.','2026-11-23','14:00:00','17:00:00','Main Auditorium',150),
(5,'Inter-College Cricket','Sports','Team-based cricket tournament.','2026-11-25','08:00:00','16:00:00','Sports Ground',180),
(6,'Photography Walk','Creative','A guided campus photography challenge.','2026-11-27','07:00:00','10:00:00','Main Gate',60);