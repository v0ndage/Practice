//this program counts the number of inversions &
//compares them with the number of bubblesort passes
//for all permutations of a given int[]

import java.util.*;

public class count {

    //lists of all permutations in string format
    static ArrayList<String> all = new ArrayList<>();
    //lists inversions per permutation
    static List<Integer> inversions = new ArrayList<>();
    //lists bubblesort pass count per permutation
    static List<Integer> passcount = new ArrayList<>();
    //factorial, to determine permutation count
    static int fact = 0;
    //counts total inversions as we go
    static int inv = 0;

    public static void main(String[] args) {

        Integer[] array = {1, 2, 3, 4};
        print("Input Array: " + Arrays.toString(array));

        //factorial is number of expected permutations for UNIQUE int[]
        fact = factorial(array.length);

        //inversions will be counted during permutation
        //basically we just shuffle randomly until all permutations are found
        permute(array, fact);

        print("Permutations:\n" + all.toString());
        print("Inversions (by permutation index):\n" + inversions.toString());
        print("Total inversion count: " + inv);
        print("Bubblesort pass count (by permutation index):\n" + passcount.toString());
    }

    public static int factorial(int n) {
        int f = n;
        while (n!=1) {
            f*=n-1;
            n--;
        } return f;
    }

    public static void permute(Integer[] shuffle, int f){

        //randomly shuffle array until all permutations are found
        while (all.size()!=f) {

            //shuffle int[] as list
            Collections.shuffle(Arrays.asList(shuffle));

            //convert int[] to string for contains()
            String str = "";
            for (int i : shuffle) str += (char)(i + '0');

            //only add unique permutations, count inversions & bubble passcount
            if (!all.contains(str)) {
                all.add(str);
                inversions.add(inversion(shuffle));
                passcount.add(bubblesort(shuffle));
            }
        }
    }

    public static int inversion(Integer[] arr) {

        //count inversions per permutation
        int count = 0;
        for (int i=0; i<arr.length-1; i++) {
            for (int j=i+1; j<arr.length; j++) {
                if (arr[i] > arr[j]) {
                    count++;
                }
            }
        } inv += count;
        return count;
    }

    public static int bubblesort(Integer arr[]) {
        int l = arr.length-1, temp = 0, pass = 0;
        for (int i=0; i < l; i++) {
            for (int j=0; j < l-i; j++) {
                if (arr[j] > arr[j+1]) {
                    pass++;
                    temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        } return pass;
    }

    //brevity function
    public static void print(String s){ System.out.println("\n" + s); }
}

/*
a(n) = n!n(n-1)/4 gives the total number of inversions in all the permutations of [n].
[Stern, Terquem] Proof: For fixed i,j and for fixed I,J (i<j, I>J, 1 <= i,j,I,J <= n),
we have (n-2)! permutations p of [n] for which p(i)=I and p(j)=J
(permute {1,2,...,n} \ {I,J} in the positions (1,2,...,n) \ {i,j}).
There are n(n-1)/2 choices for the pair (i,j) with i<j and n(n-1)/2 choices for the pair (I,J) with I>J.
Consequently, the total number of inversions in all the permutations of [n] is (n-2)![n(n-1)/2]^2 = n!n(n-1)/4.
*/

