package net.gincat.demo.jpx;

import net.gincat.demo.jpx.entity.School;
import net.gincat.demo.jpx.query.SchoolListTO;
import net.gincat.demo.jpx.repository.ClazzRepository;
import net.gincat.demo.jpx.repository.SchoolRepository;
import net.gincat.demo.jpx.repository.StudentRepository;
import net.gincat.demo.jpx.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
class SpringDataJpaxDemoApplicationTests {
    @Autowired
    private ClazzRepository clazzRepository;
    @Autowired
    private SchoolRepository schoolRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;

    @Test
    @Rollback(false)
    void contextLoads() {
        School school = new School();
        school.setAddress("YunFly Road 889");
        school.setIdNo("1");
        school.setBuildTime(System.currentTimeMillis());
        school.setName("YUN LFY ");
        school.setDescription("Very Good");
        schoolRepository.save(school);
    }

    @Test
    void listSchool(){
        SchoolListTO params1 = new SchoolListTO();
        SchoolListTO params2 = new SchoolListTO();
        SchoolListTO params3 = new SchoolListTO();

        params1.setIdNo("1");
        params2.setName("YUN");
        params3.setName("C");
        System.out.println(schoolRepository.findAllNoPageWith(params1).size());
        System.out.println(schoolRepository.findAllNoPageWith(params2).size());
        System.out.println(schoolRepository.findAllNoPageWith(params3).size());


    }

}
