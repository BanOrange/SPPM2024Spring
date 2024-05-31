package IntervalSet;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class IntervalSetStaticTest {
    /**
     * Testing Strategy
     *  empty()
     *     no inputs, only output is empty IntervalSet
     *      observe with labels()
     *
     */

    //测试asser能否正常工作
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    //测试IntervalSet的静态方法，即返回一个空集合
    @Test
    public void testEmptyVerticesEmpty() {
        assertEquals("expected empty() IntervalSet to have no labels",
                Collections.emptySet(), IntervalSet.empty().labels());
    }
}
