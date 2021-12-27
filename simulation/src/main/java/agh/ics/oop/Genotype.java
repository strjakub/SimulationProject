package agh.ics.oop;

import java.util.Arrays;
import java.util.Random;

public class Genotype {
    private final Integer[] gens = new Integer[32];

    public Genotype(){
        Random generator = new Random();
        for (int i = 0; i < 32; ++i) {
            gens[i] = generator.nextInt(8);
        }
        Arrays.sort(gens);
    }

    public Genotype(Genotype strong, Genotype weak, int x){
        Random generator = new Random();
        int sideSwitch = generator.nextInt(2);
        Integer[] strongGenotype = strong.getGens();
        Integer[] weakGenotype = weak.getGens();

        if(sideSwitch == 0){
            if (x + 1 >= 0) System.arraycopy(strongGenotype, 0, gens, 0, x + 1);
            if (32 - (x + 1) >= 0) System.arraycopy(weakGenotype, x + 1, gens, x + 1, 32 - (x + 1));
        }
        else{
            if (32 - x >= 0) System.arraycopy(weakGenotype, 0, gens, 0, 32 - x);
            if (32 - (32 - x) >= 0) System.arraycopy(strongGenotype, 32 - x, gens, 32 - x, 32 - (32 - x));
        }
        Arrays.sort(gens);
    }

    @Override
    public String toString() {
        return Arrays.toString(gens);
    }

    public Integer[] getGens() {
        return gens;
    }
}
