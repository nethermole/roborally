package com.nethermole.roborally.utils;

import java.util.ArrayList;
import java.util.Collections;

public class NonNullArrayList<T> extends ArrayList<T> {
    @Override
    public boolean add(T t){
        if(t == null){
            return false;
        }
        return super.add(t);
    }

    @Override
    public void add(int index, T t){
        if(t != null){
            super.add(t);
        }
    }
}
