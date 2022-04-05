import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.jupiter.api.*;

import java.io.*;
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
    void testUpdate(){
        Student student = enrolmentSystem.isStudentExisted("S101312");

        ByteArrayInputStream in = new ByteArrayInputStream("COSC3321".getBytes());
        System.setIn(in);

        enrolmentSystem.update(student,"2021A","1");
        assertEquals(16,enrolmentSystem.getStudentEnrolmentList().size());

        in = new ByteArrayInputStream("COSC4030".getBytes());
        System.setIn(in);

        enrolmentSystem.update(student,"2020C","2");
        assertEquals(15,enrolmentSystem.getStudentEnrolmentList().size());

        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    void testCourseForStudentReport(){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        ByteArrayInputStream in = new ByteArrayInputStream("S101312\n2020C".getBytes());
        System.setIn(in);

        String expect = "COSC4030: Theory of Computation (Credit: 5)\n" +
                "BUS2232: Business Law (Credit: 3)";

        Report allCourseForStudent = new allCourseForStudent(enrolmentSystem);
        allCourseForStudent.report(enrolmentSystem);

        //Only get element having the index from 2 of this to ignore the user input asking message
        String[] implement = out.toString().trim().split("\n");
        StringBuilder actual = new StringBuilder();
        for (int i = 2; i < implement.length; i++){
            actual.append(implement[i]);
            if (i != (implement.length-1))
                actual.append("\n");
        }
        assertEquals(expect, actual.toString());

        //Check the exporting file
        in = new ByteArrayInputStream("S101312_2020C.csv".getBytes());
        System.setIn(in);

        allCourseForStudent.reportFile(enrolmentSystem);
        assertTrue(new File("S101312_2020C.csv").exists());
        new File("S101312_2020C.csv").delete();

        try {
            in.close();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testStudentOfCourseReport() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        ByteArrayInputStream in = new ByteArrayInputStream("COSC3321\n2021A".getBytes());
        System.setIn(in);

        String expect = """
                S101153: Jang Min Seon (Birthdate: 9/25/2000)
                S103817: Thuy Thu Nguyen (Birthdate: 3/4/2000)
                S101163: Joseph Fergile (Birthdate: 10/29/1998)
                S102732: Mark Duong (Birthdate: 8/28/2001)""";

        Report allStudentOfCourse = new allStudentOfCourse(enrolmentSystem);
        allStudentOfCourse.report(enrolmentSystem);

        //Only get element having the index from 1 of this to ignore the user input asking message
        String[] implement = out.toString().trim().split("\n");
        StringBuilder actual = new StringBuilder();
        for (int i = 1; i < implement.length; i++){
            actual.append(implement[i]);
            if (i != (implement.length-1))
                actual.append("\n");
        }
        assertEquals(expect, actual.toString());

        //Check the exporting file
        in = new ByteArrayInputStream("COSC3321_2021A.csv".getBytes());
        System.setIn(in);

        allStudentOfCourse.reportFile(enrolmentSystem);
        assertTrue(new File("COSC3321_2021A.csv").exists());
        new File("COSC3321_2021A.csv").delete();

        try {
            in.close();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCourseInSemReport() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        ByteArrayInputStream in = new ByteArrayInputStream("2020C".getBytes());
        System.setIn(in);

        String expect = """
                COSC4030: Theory of Computation (Credit: 5)
                BUS2232: Business Law (Credit: 3)""";

        Report allCourseInSem = new allCourseInSem(enrolmentSystem);
        allCourseInSem.report(enrolmentSystem);

        //Only get element having the index from 1 of this to ignore the user input asking message
        String[] implement = out.toString().trim().split("\n");
        StringBuilder actual = new StringBuilder();
        for (int i = 1; i < implement.length; i++){
            actual.append(implement[i]);
            if (i != (implement.length-1))
                actual.append("\n");
        }
        assertEquals(expect, actual.toString());

        //Check the exporting file
        in = new ByteArrayInputStream("Courses_2020C.csv".getBytes());
        System.setIn(in);

        allCourseInSem.reportFile(enrolmentSystem);
        assertTrue(new File("Courses_2020C.csv").exists());
        new File("Courses_2020C.csv").delete();

        try {
            in.close();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}