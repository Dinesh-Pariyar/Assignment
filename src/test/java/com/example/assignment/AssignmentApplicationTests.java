package com.example.assignment;

import io.jsonwebtoken.lang.Assert;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
class AssignmentApplicationTests {

    List<Integer> integerList = new ArrayList<>();

    Random random = new Random();


    @Test
    void contextLoads() {
    }

    public List<Integer> getDuplicateElementArrayNormal(List<Integer> a) {

        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < a.size(); i++) {
            int count = 0;
            for (int j = 0; j < a.size(); j++) {
                if (a.get(i).equals(a.get(j))) {
                    count++;
                }
            }
            if (count > 1) {
                result.add(a.get(i));
            }
        }

        return result;
    }


    public List<Integer> getDuplicateElementArray(List<Integer> a) {
        List<Integer> results = new ArrayList<>();

        Set<Integer> numSet = new HashSet<>();

        for (int i : a) {
            if (!numSet.contains(i)) {
                numSet.add(i);
            } else {
                results.add(i);
            }
        }
        return results;
    }


    @Test
    public void testNormalArrayExec() {
        int i = 0;
        while (i < 10000) {
            integerList.add(random.nextInt(1, 1000));
            i++;
        }

        Long normalExecStart = System.currentTimeMillis();
        getDuplicateElementArrayNormal(integerList);
        Long normalExecEnd = System.currentTimeMillis();
        long normalDiff = normalExecEnd-normalExecStart;



        Long impExecStart = System.currentTimeMillis();
        getDuplicateElementArray(integerList);
        Long impExecEnd = System.currentTimeMillis();
        long impDiff = impExecStart-impExecEnd;



        Assertions.assertTrue(impDiff < normalDiff);

    }

}
