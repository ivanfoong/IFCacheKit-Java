package com.ivanfoong.cache.disk;

import java.io.Serializable;

/**
 * Created by ivanfoong on 8/10/15.
 */
public class ComplexObject implements Serializable {
    int variable1;
    int variable2;
    int variable3;
    int variable4;
    int variable5;
    int variable6;
    int variable7;
    int variable8;
    int variable9;
    int variable10;
    int variables[];
    String stringVariable1;
    String stringVariable2;
    String stringVariable3;
    String stringVariable4;
    String stringVariable5;
    String stringVariable6;
    String stringVariable7;
    String stringVariable8;
    String stringVariable9;
    String stringVariable10;
    String stringVariables[];

    public ComplexObject(final int aSeed) {
        variable1 = aSeed;
        variable2 = variable1 + 1;
        variable3 = variable2 + 1;
        variable4 = variable3 + 1;
        variable5 = variable4 + 1;
        variable6 = variable5 + 1;
        variable7 = variable6 + 1;
        variable8 = variable7 + 1;
        variable9 = variable8 + 1;
        variable10 = variable9 + 1;
        variables = new int[] {variable1, variable2, variable3, variable4, variable5, variable6, variable7, variable8, variable9, variable10};
        stringVariable1 = String.valueOf(variable1);
        stringVariable2 = String.valueOf(variable2);
        stringVariable3 = String.valueOf(variable3);
        stringVariable4 = String.valueOf(variable4);
        stringVariable5 = String.valueOf(variable5);
        stringVariable6 = String.valueOf(variable6);
        stringVariable7 = String.valueOf(variable7);
        stringVariable8 = String.valueOf(variable8);
        stringVariable9 = String.valueOf(variable9);
        stringVariable10 = String.valueOf(variable10);
        stringVariables = new String[] {stringVariable1, stringVariable2, stringVariable3, stringVariable4, stringVariable5, stringVariable6, stringVariable7, stringVariable8, stringVariable9, stringVariable10};
    }
}