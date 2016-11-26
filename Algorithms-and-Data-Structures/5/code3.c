/*  yotkaz.asd.5 */

#include <stdio.h>
#include <stdlib.h>
#define GETC fgetc_unlocked(stdin)
#define PUTC(x) fputc_unlocked(x, stdout)
#define E0I -1
#define NEW_LINE 10
#define SPACE 32
#define MINUS 45
#define NULL_POINTER 0

//  I/O
inline int getNumber();
inline void outNumber(int x);


struct List
{
    int index;
    struct List* prev;
};

struct List2
{
    int index;
    struct List2* prev;
    struct List2* next;
};

int main()
{
    int n = getNumber();
    int wolne = n;
    
    /*
        -1: wolne
        -2: komitety
        0, 1, 2, 3 etc: sztaby
    */
    
    struct List** sasiedziTab = malloc(n * sizeof(struct List*));
    int x = 0;
    while (x < n) {
        sasiedziTab[x] = malloc(sizeof(struct List));
        x++;
    }
    
    struct List2* wolneList = malloc(sizeof(struct List2));
    wolneList->index = -1;
    wolneList->prev = NULL_POINTER;
    x = 0;
    while (x < n) {
        struct List2* tmp = malloc(sizeof(struct List2));
        tmp->index = x;
        tmp->prev = wolneList;
        wolneList->next = tmp;
        wolneList = tmp;
        x++;
    }

   
    register int* sztabyTabA = malloc(n * sizeof(int));
    register int* sztabyTabB = malloc(n * sizeof(int));
    register int* tmpSztabyTabA = malloc(n * sizeof(int));
    register int* tmpSztabyTabB = malloc(n * sizeof(int));
    int counter = 0;     
    while(counter < n)
    {
        sztabyTabA[counter] = -1;
        sztabyTabB[counter] = -1;
        sasiedziTab[counter]->index = -1;
        counter++;
    }
    
    int number = getNumber();
    while(number != -1)
    {
        int sasiad1 = number;
        int sasiad2 = getNumber();
        number = getNumber();
        
        struct List* nowySasiad1 = (struct List*)malloc(sizeof(struct List));
        nowySasiad1->index = sasiad1;
        nowySasiad1->prev = sasiedziTab[sasiad2];
        sasiedziTab[sasiad2] = nowySasiad1;
        
        struct List* nowySasiad2 = (struct List*)malloc(sizeof(struct List));
        nowySasiad2->index = sasiad2;
        nowySasiad2->prev = sasiedziTab[sasiad1];
        sasiedziTab[sasiad1] = nowySasiad2;
        
    }
    
    int k = getNumber();
    int* kosztyTabA = malloc(k * sizeof(int));
    int* kosztyTabB = malloc(k * sizeof(int));
    
    counter = 0;
    while(counter < k)
    {
        number = getNumber();
		wolne--;
        
		
		sztabyTabA[number] = counter;
        sztabyTabB[number] = counter;
        kosztyTabA[counter] = 1;
        kosztyTabB[counter] = 1;
        struct List* prevSasiad = sasiedziTab[number];
        while(prevSasiad->index != -1)
        {   
            if(sztabyTabA[prevSasiad->index] < 0)
            {
                sztabyTabA[prevSasiad->index] = -2;
            }
            if(sztabyTabB[prevSasiad->index] < 0)
            {
                sztabyTabB[prevSasiad->index] = -2;
            }
            prevSasiad = prevSasiad->prev;
        }
		
        counter++;
    }
   
    while(wolne > 0)
    {
		int boolInt = 0;
        struct List2* prevWolna = wolneList;
        while (prevWolna->index != -1)
        {
            if(sztabyTabA[prevWolna->index] == -2)
            {
                int* kandydaciTMP = malloc(k * sizeof(int));
                int i = 0;
                while(i < k)
                {
                    kandydaciTMP[i] = 0;
                    i++;
                }
                struct List* prevSasiad = sasiedziTab[prevWolna->index];
                while(prevSasiad->index != -1)
                {
                    if(sztabyTabA[prevSasiad->index] >= 0)
                    {
						kandydaciTMP[sztabyTabA[prevSasiad->index]]++;    
                    }        
                    prevSasiad = prevSasiad->prev;
                }
                
				i = 0;
                int aMax = 0;
                while(i < k)
                {
					if(aMax == 0 && kandydaciTMP[i] != 0)
					{
						aMax = kandydaciTMP[i];
                        tmpSztabyTabA[prevWolna->index] = i;
					}
					else if(aMax == kandydaciTMP[i])
                    {
                        tmpSztabyTabA[prevWolna->index] = i;
                    }
                    else if(aMax > kandydaciTMP[i] && kandydaciTMP[i] != 0)
                    {
                        aMax = kandydaciTMP[i];
                        tmpSztabyTabA[prevWolna->index] = i;
                    } 
                    i++;
                }
                free(kandydaciTMP);
            }    
            if(sztabyTabB[prevWolna->index] == -2)
            {
                int* kandydaciTMP = malloc(k * sizeof(int));
                int i = 0;
                int bMax = 0;
                while(i < k)
                {
                    kandydaciTMP[i] = 0;
                    i++;
                }
                struct List* prevSasiad = sasiedziTab[prevWolna->index];
                while(prevSasiad->index != -1)
                {
                    int tmpInt = sztabyTabB[prevSasiad->index];
                    if(tmpInt >= 0)
                    {
                        kandydaciTMP[tmpInt]++; 
                        if(bMax < kandydaciTMP[tmpInt])
                        {
                            bMax = kandydaciTMP[tmpInt];
                            tmpSztabyTabB[prevWolna->index] = tmpInt;
                        }  
                        else if(bMax == kandydaciTMP[tmpInt] && tmpInt < tmpSztabyTabB[prevWolna->index])
                        {
                            tmpSztabyTabB[prevWolna->index] = tmpInt;
                        }
                    }    
                    prevSasiad = prevSasiad->prev;
                }
                free(kandydaciTMP);
            }   
            
            prevWolna = prevWolna->prev;
			
            
        }
        
        prevWolna = wolneList;
        while (prevWolna->index != -1)
        {
            
            if(sztabyTabA[prevWolna->index] == -2)
            {
            
                sztabyTabA[prevWolna->index] = tmpSztabyTabA[prevWolna->index];
                kosztyTabA[sztabyTabA[prevWolna->index]]++;
                struct List* prevSasiad = sasiedziTab[prevWolna->index];
                while(prevSasiad->index != -1)
                {
                    if(sztabyTabA[prevSasiad->index] == -1 )
                    {   
                        sztabyTabA[prevSasiad->index] = -3;
                    }
                    prevSasiad = prevSasiad->prev;
                }
                
            }
            if(sztabyTabB[prevWolna->index] == -2)
            {
                sztabyTabB[prevWolna->index] = tmpSztabyTabB[prevWolna->index];
                kosztyTabB[sztabyTabB[prevWolna->index]]++;
                struct List* prevSasiad = sasiedziTab[prevWolna->index];
                while(prevSasiad->index != -1)
                {
                    if(sztabyTabB[prevSasiad->index] == -1)
                    {
                        sztabyTabB[prevSasiad->index] = -3;
                    }
                    prevSasiad = prevSasiad->prev;
                }
                wolne--;
                if(prevWolna->next == NULL_POINTER)
                {
                    wolneList = prevWolna->prev;
                    prevWolna->prev->next = NULL_POINTER;
                }
                else
                {
                    prevWolna->prev->next = prevWolna->next;
                    prevWolna->next->prev = prevWolna->prev;
                }
               
				boolInt = 1;
            }
            
            prevWolna = prevWolna->prev;
        }
        
        prevWolna = wolneList;
        while (prevWolna->index != -1)
        {
            if(sztabyTabA[prevWolna->index] == -3) sztabyTabA[prevWolna->index] = -2;
            if(sztabyTabB[prevWolna->index] == -3) sztabyTabB[prevWolna->index] = -2;
            prevWolna = prevWolna->prev;
        }
        
		if(boolInt == 0) break;
    }
    
    counter = 0;
    while(counter < k)
    {
        outNumber(kosztyTabA[counter]);
        PUTC(SPACE);
        outNumber(kosztyTabB[counter]);
        PUTC(NEW_LINE);
        counter++;
    }
    
    return 0;
}


//  INPUT
inline int getNumber()
{
	register int number = 0;
	int ASCII = GETC;
	if(ASCII == MINUS) 
	{
	    GETC;
	    GETC;
	    GETC;
	    GETC;
	    GETC;
	    return -1;
	}
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
