package telran.citizens.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import telran.citizens.dao.Citizens;
import telran.citizens.dao.CitizensImpl;
import telran.citizens.model.Person;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CitizensTest {
    private Citizens citizens;
    private static final LocalDate now = LocalDate.now();
    private Comparator<Person> lastNameComparator = (p1, p2) -> {
        int res = p1.getLastName().compareToIgnoreCase(p2.getLastName());
        return res != 0 ? res : Integer.compare(p1.getId(), p2.getId());
    };
    private Comparator<Person> ageComparator = (p1, p2) -> {
        int res = Integer.compare(p1.getAge(), p2.getAge());
        return res != 0 ? res : Integer.compare(p1.getId(), p2.getId());
    };

    @BeforeEach
    void setUp() {
        citizens = new CitizensImpl();
        citizens.add(new Person(1, "Alex", "Wolfson", now.minusYears(31)));
        citizens.add(new Person(2, "Emma", "Johnson", now.minusYears(25)));
        citizens.add(new Person(3, "Liam", "Johnson", now.minusYears(28)));
        citizens.add(new Person(4, "Sophia", "Brown", now.minusYears(22)));
        citizens.add(new Person(5, "Noah", "Davis", now.minusYears(35)));

    }

    @Test
    void testAdd() {
        assertFalse(citizens.add(null));
        assertFalse(citizens.add(new Person(3, "Liam", "Johnson", now.minusYears(25))));
        assertEquals(5, citizens.size());
        assertTrue(citizens.add(new Person(6, "Olivia", "Miller", now.minusYears(29))));
        assertEquals(6, citizens.size());
    }

    @Test
    void testRemove() {
        assertFalse(citizens.remove(6));
        assertEquals(5, citizens.size());
        assertTrue(citizens.remove(3));
        assertEquals(4, citizens.size());
    }

    @Test
    void testFindById() {
        assertNull(citizens.find(6));
        Person person = citizens.find(2);
        assertEquals("Emma", person.getFirstName());
        assertEquals("Johnson", person.getLastName());
        assertEquals(25, person.getAge());
        assertEquals(2, person.getId());
    }

    @Test
    void testFindByMinMaxAge() {
        Iterable<Person> res = citizens.find(20, 28);
        List<Person> actual = new ArrayList<>();

        for (Person person : res) {
            actual.add(person);
        }

        actual.sort(ageComparator);

        Iterable<Person> expected = List.of(
                new Person(4, "Sophia", "Brown", now.minusYears(22)),
                new Person(2, "Emma", "Johnson", now.minusYears(25)),
                new Person(3, "Liam", "Johnson", now.minusYears(28))
        );
        assertIterableEquals(expected, actual);
    }

    @Test
    void testFindByLastName() {
        Iterable<Person> res = citizens.find("Johnson");
        List<Person> actual = new ArrayList<>();

        for (Person person : res) {
            actual.add(person);
        }
        actual.sort(lastNameComparator);

        Iterable<Person> expected = List.of(
                new Person(2, "Emma", "Johnson", now.minusYears(25)),
                new Person(3, "Liam", "Johnson", now.minusYears(28))
        );
        assertIterableEquals(expected, actual);
    }

    @Test
    void testGetAllPersonsSortedById() {
        Iterable<Person> res = citizens.getAllPersonsSortedById();
        List<Person> actual = new ArrayList<>();

        for (Person person : res) {
            actual.add(person);
        }

        Iterable<Person> expected = List.of(
                new Person(1, "Alex", "Wolfson", now.minusYears(31)),
                new Person(2, "Emma", "Johnson", now.minusYears(25)),
                new Person(3, "Liam", "Johnson", now.minusYears(28)),
                new Person(4, "Sophia", "Brown", now.minusYears(22)),
                new Person(5, "Noah", "Davis", now.minusYears(35))
        );
        assertIterableEquals(expected, actual);

    }

    @Test
    void testGetAllPersonsSortedByAge() {
        Iterable<Person> res = citizens.getAllPersonsSortedByAge();
        List<Person> actual = new ArrayList<>();

        for (Person person : res) {
            actual.add(person);
        }

        Iterable<Person> expected = List.of(
                new Person(4, "Sophia", "Brown", now.minusYears(22)),
                new Person(2, "Emma", "Johnson", now.minusYears(25)),
                new Person(3, "Liam", "Johnson", now.minusYears(28)),
                new Person(1, "Alex", "Wolfson", now.minusYears(31)),
                new Person(5, "Noah", "Davis", now.minusYears(35))
        );
        assertIterableEquals(expected, actual);
    }

    @Test
    void testGetAllPersonsSortedByLastName() {
        Iterable<Person> res = citizens.getAllPersonsSortedByLastName();
        List<Person> actual = new ArrayList<>();

        for (Person person : res) {
            actual.add(person);
        }
        actual.forEach(p -> System.out.println(p));
        Iterable<Person> expected = List.of(
                new Person(4, "Sophia", "Brown", now.minusYears(22)),
                new Person(5, "Noah", "Davis", now.minusYears(35)),
                new Person(2, "Emma", "Johnson", now.minusYears(25)),
                new Person(3, "Liam", "Johnson", now.minusYears(28)),
                new Person(1, "Alex", "Wolfson", now.minusYears(31))
        );
        assertIterableEquals(expected, actual);
    }

    @Test
    void testSize() {
        assertEquals(5, citizens.size());
    }
}