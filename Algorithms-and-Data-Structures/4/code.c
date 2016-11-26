/*  yotkaz.asd.4 */

#include <stdio.h>
#include <stdlib.h>
#define GETC fgetc_unlocked(stdin)
#define PUTC(x) fputc_unlocked(x, stdout)
#define E0I -1
#define SPACE 32
#define MINUS 45


inline long getNumber();
inline void outNumber(long long x);

void mergeSort(int* array, int start, int end);
void merge(int* array, int start, int middle, int end);


int main(){
    int i = 0, j = 0, x = 0, y = 0;
    
    int n = getNumber();
    int k = getNumber();
    
    if(k == 0)
    {
        outNumber(0);
        PUTC(SPACE);
        outNumber(0);
        PUTC(SPACE);
        outNumber(0);
    }
    
    int** array2d;
    array2d = (int**) malloc(n * sizeof(int*));
    for(i = 0; i < n; i++)
    {
        array2d[i] = (int*) malloc(n * sizeof(int));
    }
    
    if(n > 0)
    {
        array2d[0][0] = getNumber();
    }
    
    for(j = 1; j < n; j++)
    {
        array2d[0][j] = getNumber() + array2d[0][j - 1];
    }
    
    for(i = 1; i < n; i++)
    {
        array2d[i][0] = getNumber() + array2d[i - 1][0];
        
        for(j = 1; j < n; j++)
        {
            array2d[i][j] = getNumber() + array2d[i][j - 1] + array2d[i - 1][j] - array2d[i - 1][j - 1];
        }
    }
    
    int* kSumsArray = (int*) malloc(k * sizeof(int));
        
    int arg1;
    int arg2;
    int arg3;
    int arg4;
    
    long long sumOfSums = 0;
        
    for(x = 0; x < k; x++)
    {
        arg1 = getNumber();
        arg2 = getNumber();
        arg3 = getNumber();
        arg4 = getNumber();
        
        /*
            arg1, arg2 - top left
            arg1, arg4 - top right
            arg3, arg2 - bottom left
            arg3, arg4 - bottom right
        */
    
        if(arg1 == 0)
        {
            if(arg2 == 0)
            {
                kSumsArray[x] = array2d[arg3][arg4];
            }
            else
            {
                kSumsArray[x] =  array2d[arg3][arg4] - array2d[arg3][arg2 - 1]; 
            } 
        }
        else if(arg2 == 0)
        {
            kSumsArray[x] =  array2d[arg3][arg4] - array2d[arg1 - 1][arg4]; 
        }
        else 
        {
            kSumsArray[x] =  array2d[arg3][arg4] - array2d[arg1 - 1][arg4] - array2d[arg3][arg2 - 1] + array2d[arg1 - 1][arg2 - 1]; 
        }
        
        sumOfSums += kSumsArray[x];
    }
    
    mergeSort(kSumsArray, 0, k -1);

    
    int differentSums = 1;
    int largestGroupSize = 0;
    int currentGroupSize = 1;
    int largestGroupCounter = 0;
    
    for(x = 1; x < k; x++)
    {
        if(kSumsArray[x] != kSumsArray[x - 1])
        {
            if(currentGroupSize > largestGroupSize)
            {
                largestGroupSize = currentGroupSize;
                largestGroupCounter = 1;
            }
            else if(currentGroupSize == largestGroupSize)
            {
                largestGroupCounter += 1;
            }
            currentGroupSize = 1;
            differentSums += 1;
        }
        else
        {
            currentGroupSize += 1;
        }
    }
    if(currentGroupSize > largestGroupSize)
    {
        largestGroupCounter = 1;
    }
    else if(currentGroupSize == largestGroupSize)
    {
        largestGroupCounter += 1;
    }
    
    int avarageSum = sumOfSums / k;
    
    outNumber(differentSums);
    PUTC(SPACE);
    outNumber(largestGroupCounter);
    PUTC(SPACE);
    outNumber(avarageSum);
   
    return 0;
}


void merge(int* array, int start, int middle, int end)
{
    int* tmpArray = (int*) malloc((end - start + 1) * sizeof(int));
    
    int index = 0;
    int x = start;
    int y = middle + 1;
    
    while((x <= middle) && (y <= end))
    {
        if(array[y] < array[x])
        {
            tmpArray[index] = array[y];
            y += 1;
        }
        else
        {
            tmpArray[index] = array[x];
            x += 1;
        }
        index += 1;
    }
    
    if(x <= middle)
    {
        while(x <= middle)
        {
            tmpArray[index] = array[x];
            x += 1;
            index += 1;
        }
    }
    else
    {
        while(y <= end)
        {
            tmpArray[index] = array[y];
            y += 1;
            index += 1;
        }
    }
    
    int i;
    for(i = 0; i < (end - start + 1); i++)
    {
        array[start + i] = tmpArray[i];
    }   
}

void mergeSort(int* array, int start, int end)
{
    if(start != end)
    {
        int middle = (start + end) / 2;
        mergeSort(array, start, middle);
        mergeSort(array, middle + 1, end);
        merge(array, start, middle, end);        
    }
}


inline long getNumber()
{   
	register long number = 0;
	int isMinus = 0;
	int ASCII = GETC;
	if(ASCII == MINUS)
	{
	    isMinus = 1;
	    ASCII = GETC;
	}
	while(ASCII > 32){
	    number = number * 10 + (ASCII - 48);
	    ASCII = GETC;
	}
	if(isMinus == 1)
	{
	    return -number;
	}
	return number;
}

inline void outNumber(long long x)
{
    if(x < 0)
    {
        PUTC(MINUS);
        outNumber(-x);
    }
    else
    {
        long long divResult = x / 10;
	    if(divResult != 0)
	    {
	        outNumber(divResult);
	    }
	    PUTC((x % 10) + 48);
    }
}
