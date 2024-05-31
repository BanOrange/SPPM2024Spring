package SpecialSet.inter.impl;

import SpecialSet.inter.NonPeriodicIntervalSet;

public class NonPeriodicIntervalSetImpl<L> implements NonPeriodicIntervalSet<L> {

    @Override
    public boolean checkNoPeriodic(long period) {
        return period<=0;
    }
}
