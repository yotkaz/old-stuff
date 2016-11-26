/*  yotkaz.asd.6 */

#include <stdio.h>
#include <stdlib.h>
#define GETC fgetc_unlocked(stdin)
#define PUTC(x) fputc_unlocked(x, stdout)
#define NEW_LINE 10
#define SPACE 32
#define NULL_POINTER 0


//  I/O
inline long getNumber();
inline void outNumber(long x);

// MERGE SORT
void merge(long * array, long  start, long  middle, long  end);
void mergeSort(long * array, long  start, long  end);



int main()
{
    long i, n, m, k;
    n = getNumber();
    m = getNumber();
    k = getNumber();
    
    long* sizeArray = (long*) malloc(n * sizeof(long));
    
    long* resultArray = (long*) malloc(m * sizeof(long));
    long* newResultArray = (long*) malloc(m * sizeof(long));
    
    long resultSum = 0;
    
    i = 0;
    while(i < m)
    {
        resultArray[i] = -1;
        i++;
    }
    
    i = 0;
    while(i < m)
    {
        newResultArray[i] = -1;
        i++;
    }
    
    i = 0;
    long** array2d;
    array2d = (long**) malloc(n * sizeof(long*));
    while(i < n)
    {
        array2d[i] = (long*) malloc(n * sizeof(long));
        i++;
    }
        
    i = 0;
    while(i < n)
    {
        long currentSetSize = getNumber();
        array2d[i] = (long*) malloc(currentSetSize * sizeof(long));
        sizeArray[i] = currentSetSize;
        
        long j = 0;
        while(j < currentSetSize)
        {
            array2d[i][j] = getNumber();
            j++;
        }
        
        long* arrayToSort = array2d[i];
        mergeSort(arrayToSort, 0, currentSetSize - 1);
        array2d[i] = arrayToSort;
        
        i++;
    }
    
    i = 0;
    while(i < k)
    {
        long ki1 = getNumber();
        long ki2 = getNumber();
        long ki;
        
        if (resultSum % 2 == 0)
        {
            ki = ki1;
        }
        else
        {
            ki = ki2;
        }
        
        long rCounter = 0; // result array counter
        long aCounter = 0; // another array counter
        long nCounter = 0; // new result counter
        
        resultSum = 0;
        while(nCounter < m)
        {
            if(resultArray[rCounter] == -1)
            {
                if(aCounter < sizeArray[ki])
                {
                    newResultArray[nCounter] = array2d[ki][aCounter];
                    resultSum += array2d[ki][aCounter];
                    aCounter++;
                }
                else
                {
                    break;
                }
            }
            else if(aCounter >= sizeArray[ki])
            {
                newResultArray[nCounter] = resultArray[rCounter]; 
                resultSum += resultArray[rCounter];
                rCounter++;
            }
            else if(resultArray[rCounter] <= array2d[ki][aCounter])
            {
                newResultArray[nCounter] = resultArray[rCounter];
                resultSum += resultArray[rCounter];
                rCounter++;
            }
            else
            {
                newResultArray[nCounter] = array2d[ki][aCounter];
                resultSum += array2d[ki][aCounter];
                aCounter++;
            }
            nCounter++;
        }
        free(resultArray);
        resultArray = newResultArray;
        newResultArray = (long*) malloc(m * sizeof(long));
        
        long c = 0;
        while(c < m)
        {
            newResultArray[c] = -1;
            c++;
        }
        i++;
    }
    
    long diff = 0;
    long lastNumber = -1;
    
    i = 0;
    while (i < m)
    {
        if(resultArray[i] != lastNumber)
        {
            diff++;
            lastNumber = resultArray[i];
        }
        i++;
    }
    
    outNumber(resultSum);
    PUTC(SPACE);
    outNumber(diff);
            
    return 0;
}



// MERGE SORT
void merge(long * array, long  start, long  middle, long  end)
{
    long * tmpArray = (long *) malloc((end - start + 1) * sizeof(long ));
    
    long  index = 0;
    long  x = start;
    long  y = middle + 1;
    
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
    
    long  i;
    for(i = 0; i < (end - start + 1); i++)
    {
        array[start + i] = tmpArray[i];
    }   
}

void mergeSort(long * array, long  start, long  end)
{
    if(start != end)
    {
        long  middle = (start + end) / 2;
        mergeSort(array, start, middle);
        mergeSort(array, middle + 1, end);
        merge(array, start, middle, end);        
    }
}



//  INPUT
inline long getNumber()
{
	register long number = 0;
	int ASCII = GETC;
	while(ASCII > 32){
	    number = number * 10 + (ASCII - 48);
	    ASCII = GETC;
	}
	return number;
}

//  OUTPUT
inline void outNumber(long x)
{
    long divResult = x / 10;
	if(divResult != 0)
	{
	    outNumber(divResult);
	}
	PUTC((x % 10) + 48);
}
