package telran.citizens.dao;

import telran.citizens.model.Person;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CitizensImpl implements Citizens {
    private List<Person> idList;
    private List<Person> lastNameList;
    private List<Person> ageList;
    private Comparator<Person> lastNameComparator = (p1, p2) -> {
        int res = p1.getLastName().compareToIgnoreCase(p2.getLastName());
        return res != 0 ? res : Integer.compare(p1.getId(), p2.getId());
    };
    private Comparator<Person> ageComparator = (p1, p2) -> {
        int res = Integer.compare(p1.getAge(), p2.getAge());
        return res != 0 ? res : Integer.compare(p1.getId(), p2.getId());
    };

    public CitizensImpl() {
        idList = new ArrayList<>();
        lastNameList = new ArrayList<>();
        ageList = new ArrayList<>();
    }

    public CitizensImpl(List<Person> citizens) {
        this();
        idList.addAll(citizens);
        lastNameList.addAll(citizens);
        ageList.addAll(citizens);

        Collections.sort(idList);
        lastNameList.sort(lastNameComparator);
        ageList.sort(ageComparator);
    }


    @Override
    public boolean add(Person person) {
        if (person == null) {
            return false;
        }
        int index = Collections.binarySearch(idList, person);
        if (index >= 0) {
            return false;
        }
        index = -index - 1;
        idList.add(index, person);

        index = Collections.binarySearch(lastNameList, person, lastNameComparator);
        index = index >= 0 ? index : -index - 1;
        lastNameList.add(index, person);

        index = Collections.binarySearch(ageList, person, ageComparator);
        index = index >= 0 ? index : -index - 1;
        ageList.add(index, person);
        return true;
    }

    @Override
    public boolean remove(int id) {
        Person tmp = find(id);
        if (tmp == null) {
            return false;
        }
        idList.remove(tmp);
        ageList.remove(tmp);
        lastNameList.remove(tmp);
        return true;
    }

    @Override
    public Person find(int id) {
        Person pattern = new Person(id, null, null, null);
        int index = Collections.binarySearch(idList, pattern);
        return index < 0 ? null : idList.get(index);
    }

    @Override
    public Iterable<Person> find(int minAge, int maxAge) {
        LocalDate now = LocalDate.now();
        Person pattern = new Person(Integer.MIN_VALUE, null, null, now.minusYears(minAge));
        int fromIndex = -Collections.binarySearch(ageList, pattern, ageComparator) - 1;

        pattern = new Person(Integer.MAX_VALUE, null, null, now.minusYears(maxAge + 1));
        int toIndex = -Collections.binarySearch(ageList, pattern, ageComparator) - 1;

        return ageList.subList(fromIndex, toIndex);
    }


    @Override
    public Iterable<Person> find(String lastName) {

        Person pattern = new Person(0, null, lastName, null);
        int fromIndex = -Collections.binarySearch(lastNameList, pattern, lastNameComparator) - 1;

        pattern = new Person(Integer.MAX_VALUE, null, lastName, null);
        int toIndex = -Collections.binarySearch(lastNameList, pattern, lastNameComparator) - 1;

        return lastNameList.subList(fromIndex, toIndex);
    }

    @Override
    public Iterable<Person> getAllPersonsSortedById() {
        return idList;
    }

    @Override
    public Iterable<Person> getAllPersonsSortedByAge() {
        return ageList;
    }

    @Override
    public Iterable<Person> getAllPersonsSortedByLastName() {
        return lastNameList;
    }

    @Override
    public int size() {
        return idList.size();
    }
}
