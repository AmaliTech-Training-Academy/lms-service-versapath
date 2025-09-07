package com.capstone.lms_service.service;

import org.common.event.CreateSkillEvent;

public interface CourseService {
    String createCourse(CreateSkillEvent skillEvent);
}
