//This simple program illuminates the 300 year old Goldbach Congjecture by picturing the pattern of sums of Primes (it is just for fun)
//See example: https://imgur.com/AjWIkn7

#include <stdlib.h>
#include <stdio.h>

#define size 100

int prime(int x){

    if (x%2 == 0) return 0;

    for (int i = 3; i < size; i+=2)
    {
        if((x%i == 0) && (x != i))return 0;
    }

    return 1;
    
}

int main(){

    for (int i = 0; i < size; i++)
    {
        printf("%.2d ", i);
           
        for (int j = 1; j < size; j++)
        {
            if (i == 0) printf("%.2d ", j);
            else if ((prime(i) == 1) && (prime(j) == 1) && (i==j && i%2 == 1)) printf("e@ ");
            else if ((prime(i) == 1) && (prime(j) == 1)) printf("@@ ");
            else if (i==j && i%2 == 1) printf("ev ");
            else printf("   ");
        }

        printf("\n");
    }

    return 0;
}
