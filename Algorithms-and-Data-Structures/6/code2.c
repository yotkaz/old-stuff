/*  yotkaz.asd.6 */

#include <stdio.h>
#include <stdlib.h>
#define GETC fgetc_unlocked(stdin)
#define PUTC(x) fputc_unlocked(x, stdout)
#define NEW_LINE 10
#define SPACE 32
#define NULL_POINTER 0


//  I/O
inline int getNumber();
inline void outNumber(int x);

// MERGE SORT
void merge(int * array, int  start, int  middle, int  end);
void mergeSort(int * array, int  start, int  end);


// QUICK SORT
inline void swap(int *x,int *y);
inline int choose_pivot(int i,int j );
void quicksort(int list[],int m,int n);


struct List
{
    int value;
    struct List* next;
};


int main()
{
    int i, n, m, k;
    n = getNumber();
    m = getNumber();
    k = getNumber();
	
	struct List* head = (struct List*)malloc(sizeof(struct List));
	struct List* last = head;
	struct List* current = head;
	head->value = -1;
	head->next = NULL_POINTER;
    
    int* sizeArray = (int*) malloc(n * sizeof(int));
    int resultSum = 0;
    
    int** array2d;
    array2d = (int**) malloc(n * sizeof(int*));
        
    i = 0;
    while(i < n)
    {
        int currentSetSize = getNumber();
        array2d[i] = (int*) malloc(currentSetSize * sizeof(int));
        sizeArray[i] = currentSetSize;
        
        int j = 0;
        while(j < currentSetSize)
        {
            array2d[i][j] = getNumber();
            j++;
        }
        
        int* arrayToSort = array2d[i];
        quicksort(arrayToSort, 0, currentSetSize - 1);
        array2d[i] = arrayToSort;
        
        i++;
    }
    
    i = 0;
    while(i < k)
    {
        int ki1 = getNumber();
        int ki2 = getNumber();
        int ki;
        
        if (resultSum % 2 == 0)
        {
            ki = ki1;
        }
        else
        {
            ki = ki2;
        }
        
        int aCounter = 0; // another array counter
        int nCounter = 0; // new result counter
        
        resultSum = 0;
        while(nCounter < m)
        {
            if(current->next == NULL_POINTER)
            {
                if(aCounter < sizeArray[ki])
                {
					struct List* newElement = (struct List*)malloc(sizeof(struct List));
					newElement->value = array2d[ki][aCounter];
                    current->next = newElement;
					current = newElement;
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
				resultSum += current->next->value;
                current = current->next;
            }
            else if(current->next->value > array2d[ki][aCounter])
            {
				struct List* newElement = (struct List*)malloc(sizeof(struct List));
				newElement->value = array2d[ki][aCounter];
				newElement->next = current->next;
				current->next = newElement;
				current = newElement;
				resultSum += array2d[ki][aCounter];
				aCounter++;
            }
            else
            {
				resultSum += current->next->value;
				current = current->next;
            }
            nCounter++;
        }
		current = head;
        i++;
    }
    
    int diff = 0;
    int lastNumber = -1;
    
    i = 0;
	current = head->next;
    while (i < m)
    {
        if(current->value != lastNumber)
        {
            diff++;
            lastNumber = current->value;
        }
        i++;
		current = current->next;
    }
    
    outNumber(resultSum);
    PUTC(SPACE);
    outNumber(diff);
            
    return 0;
}



// MERGE SORT
void merge(int * array, int  start, int  middle, int  end)
{
    int * tmpArray = (int *) malloc((end - start + 1) * sizeof(int ));
    
    int  index = 0;
    int  x = start;
    int  y = middle + 1;
    
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
    
    int  i;
    for(i = 0; i < (end - start + 1); i++)
    {
        array[start + i] = tmpArray[i];
    }   
}

void mergeSort(int * array, int  start, int  end)
{
    if(start != end)
    {
        int  middle = (start + end) / 2;
        mergeSort(array, start, middle);
        mergeSort(array, middle + 1, end);
        merge(array, start, middle, end);        
    }
}



// QUICK SORT
inline void swap(int *x,int *y)
{
    int temp;
    temp = *x;
    *x = *y;
    *y = temp;
}
 
inline int choose_pivot(int i,int j )
{
    return((i+j) /2);
}
 
void quicksort(int list[],int m,int n)
{
    int key,i,j,k;
    if( m < n)
    {
        k = choose_pivot(m,n);
        swap(&list[m],&list[k]);
        key = list[m];
        i = m+1;
        j = n;
        while(i <= j)
        {
            while((i <= n) && (list[i] <= key))
                i++;
            while((j >= m) && (list[j] > key))
                j--;
            if( i < j)
                swap(&list[i],&list[j]);
        }
        /* swap two elements */
        swap(&list[m],&list[j]);
 
        /* recursively sort the lesser list */
        quicksort(list,m,j-1);
        quicksort(list,j+1,n);
    }
}




//  INPUT
inline int getNumber()
{
	register int number = 0;
	int ASCII = GETC;
	while(ASCII > 32){
	    number = number * 10 + (ASCII - 48);
	    ASCII = GETC;
	}
	return number;
}

//  OUTPUT
inline void outNumber(int x)
{
    int divResult = x / 10;
	if(divResult != 0)
	{
	    outNumber(divResult);
	}
	PUTC((x % 10) + 48);
}
