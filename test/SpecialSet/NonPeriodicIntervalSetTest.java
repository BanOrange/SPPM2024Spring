package SpecialSet;

import SpecialSet.inter.impl.NonPeriodicIntervalSetImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NonPeriodicIntervalSetTest {
    /**
     * Testing Strategy
     * Testing checkNoPeriod():
     *  Period:=-1;>0
     *
     *  测试策略：每种可能至少覆盖一次
     */

    @Test
    public void NoPeriodTest(){
        NonPeriodicIntervalSetImpl noPeriod = new NonPeriodicIntervalSetImpl();

        assertEquals(true,noPeriod.checkNoPeriodic(-1));
        assertEquals(false,noPeriod.checkNoPeriodic(1));

    }
}
