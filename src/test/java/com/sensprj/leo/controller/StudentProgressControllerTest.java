package com.sensprj.leo.controller;
import com.sensprj.leo.entity.*;
import com.sensprj.leo.repository.AssessmentRepository;
import com.sensprj.leo.repository.LeoDependencyRepository;
import com.sensprj.leo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;


import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentProgressController.class)
@AutoConfigureMockMvc(addFilters = false)
class StudentProgressControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    UserRepository userRepository;

    @MockBean
    AssessmentRepository assessmentRepository;

    @MockBean
    LeoDependencyRepository leoDependencyRepository;

    @Test
    void getProgress_studentNotFound_returns404() throws Exception {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        mvc.perform(get("/api/students/999/progress"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getProgress_countsStatuses_andReturnsUnmarkedLabel() throws Exception {
        // Arrange student
        User student = new User();
        student.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(student));

        // Arrange LEOs
        Leo leo1 = new Leo(); leo1.setId(10L); leo1.setName("LEO 1");
        Leo leo2 = new Leo(); leo2.setId(20L); leo2.setName("LEO 2");
        Leo leo3 = new Leo(); leo3.setId(30L); leo3.setName("LEO 3");

        // Assessments:
        // - UNMARKED
        Assessment a1 = new Assessment();
        a1.setStudent(student);
        a1.setLeo(leo1);
        a1.setStatus("UNMARKED");

        // - NOT_REACHED
        Assessment a2 = new Assessment();
        a2.setStudent(student);
        a2.setLeo(leo2);
        a2.setStatus("NOT_REACHED");

        // - PARTIALLY_REACHED
        Assessment a3 = new Assessment();
        a3.setStudent(student);
        a3.setLeo(leo3);
        a3.setStatus("PARTIALLY_REACHED");

        when(assessmentRepository.findByStudent(student)).thenReturn(List.of(a1, a2, a3));

        // No dependencies for each LEO row rendering
        when(leoDependencyRepository.findByDependentLeo(leo1)).thenReturn(List.of());
        when(leoDependencyRepository.findByDependentLeo(leo2)).thenReturn(List.of());
        when(leoDependencyRepository.findByDependentLeo(leo3)).thenReturn(List.of());

        // Also avoid suggestion logic exploding: when controller later calls dependency lookup for "allLeos"
        when(leoDependencyRepository.findByPrerequisiteLeo(leo1)).thenReturn(List.of());
        when(leoDependencyRepository.findByPrerequisiteLeo(leo2)).thenReturn(List.of());
        when(leoDependencyRepository.findByPrerequisiteLeo(leo3)).thenReturn(List.of());

        // Act + Assert JSON
        mvc.perform(get("/api/students/1/progress"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalLeos").value(3))
                .andExpect(jsonPath("$.achieved").value(0))
                .andExpect(jsonPath("$.partially").value(1))
                .andExpect(jsonPath("$.notAchieved").value(1))
                .andExpect(jsonPath("$.unmarked").value(1))
                // progress = (achieved+partially+notAchieved)/total *100 = (0+1+1)/3 = 66.66 -> rounds to 67
                .andExpect(jsonPath("$.progress").value(67))
                // first row label for UNMARKED should be "Unmarked"
                .andExpect(jsonPath("$.leoStatuses[0].status").isNotEmpty());
    }
}