import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class UnitTest {
    EnrolmentSystem enrolmentSystem;

    @BeforeEach
    void setUp() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        enrolmentSystem = new EnrolmentSystem();
        enrolmentSystem.readData("default.csv");
    }

    @Test
    void testIsStudentExisted() {
        Student student = enrolmentSystem.isStudentExisted("S101312");
        assertEquals("S101312",student.getId());
        assertEquals("Alex Mike",student.getName());
        assertEquals("10/13/1998",student.getBirthDate());

        assertNull(enrolmentSystem.isCourseExisted("s3891724"));
        assertNull(enrolmentSystem.isCourseExisted("abcd"));
    }

    @Test
    void testIsCourseExisted() {
        Course course = enrolmentSystem.isCourseExisted("BUS2232");
        assertEquals("BUS2232",course.getId());
        assertEquals("Business Law",course.getName());
        assertEquals(3,course.getNumberOfCredits());

        assertNull(enrolmentSystem.isCourseExisted("COSC2440"));
        assertNull(enrolmentSystem.isCourseExisted("abcd"));
    }

    @Test
    void testIsSemValid() {
        assertEquals("2021C", enrolmentSystem.isSemValid("2021C"));
        assertEquals("2020A", enrolmentSystem.isSemValid("2020A"));
        assertNull(enrolmentSystem.isSemValid("2020D"));
        assertNull(enrolmentSystem.isSemValid("2020"));
        assertNull(enrolmentSystem.isSemValid("A2020"));
    }

    @Test
    void testAdd() {
        assertTrue(enrolmentSystem.add("S101312", "COSC3321", "2021A"));
        assertEquals(16,enrolmentSystem.getStudentEnrolmentList().size());

        assertTrue(enrolmentSystem.add("S103912","PHYS1230","2021A"));
        assertEquals(17,enrolmentSystem.getStudentEnrolmentList().size());

        assertFalse(enrolmentSystem.add("S101312","COSC4030","2020C"));
    }

    @Test
    void testDelete() {
        assertTrue(enrolmentSystem.delete("S101312","COSC4030","2020C"));
        assertEquals(14,enrolmentSystem.getStudentEnrolmentList().size());

        assertTrue(enrolmentSystem.delete("S103723","BUS2232","2020B"));
        assertEquals(13,enrolmentSystem.getStudentEnrolmentList().size());

        assertFalse(enrolmentSystem.delete("S101312","COSC4030","2021A"));
    }

    @Test
    void testUpdate() {
        Student student = enrolmentSystem.isStudentExisted("S101312");

        ByteArrayInputStream in = new ByteArrayInputStream("COSC3321".getBytes());
        System.setIn(in);

        enrolmentSystem.update(student,"2021A","1");
        assertEquals(16,enrolmentSystem.getStudentEnrolmentList().size());

        in = new ByteArrayInputStream("COSC4030".getBytes());
        System.setIn(in);

        enrolmentSystem.update(student,"2020C","2");
        assertEquals(15,enrolmentSystem.getStudentEnrolmentList().size());
    }

    @Test
    void testGetOne() {
        Student student1 = enrolmentSystem.isStudentExisted("S101163");
        Student student2 = enrolmentSystem.isStudentExisted("S103817");
        Course course1 = enrolmentSystem.isCourseExisted("BUS2232");
        Course course2 = enrolmentSystem.isCourseExisted("COSC4030");

        assertEquals(student1,enrolmentSystem.getOne("S101163","BUS2232","2020C").getStudent());
        assertEquals(course1,enrolmentSystem.getOne("S101163","BUS2232","2020C").getCourse());
        assertEquals("2020C",enrolmentSystem.getOne("S101163","BUS2232","2020C").getSemester());

        assertEquals(student2,enrolmentSystem.getOne("S103817","COSC4030","2020C").getStudent());
        assertEquals(course2,enrolmentSystem.getOne("S103817","COSC4030","2020C").getCourse());
        assertEquals("2020C",enrolmentSystem.getOne("S103817","COSC4030","2020C").getSemester());
    }

    @Test
    void testGetAll() {
        assertEquals(15, enrolmentSystem.getAll().size());
    }

    @Test
    void testReadData(){
        EnrolmentSystem enrolmentSystem1 = new EnrolmentSystem();
        assertFalse(enrolmentSystem1.readData("abc.csv"));
        assertTrue(enrolmentSystem1.readData("default.csv"));
        assertEquals(15,enrolmentSystem1.getStudentEnrolmentList().size());
    }

    @Test
    void testReport(){

    }

}