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
inline long getNumber();
inline void outNumber(long x);


struct List
{
    long index;
    struct List* prev;
};


int main()
{
    long n = getNumber();
    long wolne = n;
    
    /*
        -1: wolne
        -2: komitety
        0, 1, 2, 3 etc: sztaby
    */
    
    struct List** sasiedziTab = malloc(n * sizeof(struct List*));
    long x = 0;
    while (x < n) {
        sasiedziTab[x] = malloc(sizeof(struct List));
        x++;
    }
   
    long* sztabyTabA = malloc(n * sizeof(long));
    long* sztabyTabB = malloc(n * sizeof(long));
    long* tmpSztabyTabA = malloc(n * sizeof(long));
    long* tmpSztabyTabB = malloc(n * sizeof(long));
    long counter = 0;     
    while(counter < n)
    {
        sztabyTabA[counter] = -1;
        sztabyTabB[counter] = -1;
        sasiedziTab[counter]->index = -1;
        sasiedziTab[counter]->prev = NULL_POINTER;
        counter++;
    }
    
    long number = getNumber();
    while(number != -1)
    {
        long sasiad1 = number;
        long sasiad2 = getNumber();
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
    
    long k = getNumber();
    long* kosztyTabA = malloc(k * sizeof(long));
    long* kosztyTabB = malloc(k * sizeof(long));
    
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
		int bool = 0;
        counter = 0;
        while (counter < n)
        {
            if(sztabyTabA[counter] == -2)
            {
                long* kandydaciTMP = malloc(k * sizeof(long));
                long i = 0;
                while(i < k)
                {
                    kandydaciTMP[i] = 0;
                    i++;
                }
                struct List* prevSasiad = sasiedziTab[counter];
                while(prevSasiad->index != -1)
                {
                    if(sztabyTabA[prevSasiad->index] >= 0)
                    {
						kandydaciTMP[sztabyTabA[prevSasiad->index]]++;    
                    }        
                    prevSasiad = prevSasiad->prev;
                }
                
				i = 0;
                long aMax = 0;
                while(i < k)
                {
					if(aMax == 0 && kandydaciTMP[i] != 0)
					{
						aMax = kandydaciTMP[i];
                        tmpSztabyTabA[counter] = i;
					}
					else if(aMax == kandydaciTMP[i])
                    {
                        tmpSztabyTabA[counter] = i;
                    }
                    else if(aMax > kandydaciTMP[i] && kandydaciTMP[i] != 0)
                    {
                        aMax = kandydaciTMP[i];
                        tmpSztabyTabA[counter] = i;
                    } 
                    i++;
                }
                free(kandydaciTMP);
            }    
            if(sztabyTabB[counter] == -2)
            {
                long* kandydaciTMP = malloc(k * sizeof(long));
                long i = 0;
                while(i < k)
                {
                    kandydaciTMP[i] = 0;
                    i++;
                }
                struct List* prevSasiad = sasiedziTab[counter];
                while(prevSasiad->index != -1)
                {
                    if(sztabyTabB[prevSasiad->index] >= 0)
                    {
                        kandydaciTMP[sztabyTabB[prevSasiad->index]]++;   
                    }    
                    prevSasiad = prevSasiad->prev;
                }
                i = 0;
                
                long bMax = 0;
                
                while(i < k)
                {
                    if(bMax < kandydaciTMP[i])
                    {
                        bMax = kandydaciTMP[i];
                        tmpSztabyTabB[counter] = i;
                    }
                    i++;
                }
                free(kandydaciTMP);
            }   
            
            counter++;
			
            
        }
        
        counter = 0;
        while (counter < n)
        {
            
            if(sztabyTabA[counter] == -2)
            {
            
                sztabyTabA[counter] = tmpSztabyTabA[counter];
                kosztyTabA[sztabyTabA[counter]]++;
                struct List* prevSasiad = sasiedziTab[counter];
                while(prevSasiad->index != -1)
                {
                    if(sztabyTabA[prevSasiad->index] == -1 )
                    {   
                        sztabyTabA[prevSasiad->index] = -3;
                    }
                    prevSasiad = prevSasiad->prev;
                }
                
            }
            if(sztabyTabB[counter] == -2)
            {
                sztabyTabB[counter] = tmpSztabyTabB[counter];
                kosztyTabB[sztabyTabB[counter]]++;
                struct List* prevSasiad = sasiedziTab[counter];
                while(prevSasiad->index != -1)
                {
                    if(sztabyTabB[prevSasiad->index] == -1)
                    {
                        sztabyTabB[prevSasiad->index] = -3;
                    }
                    prevSasiad = prevSasiad->prev;
                }
                wolne--;
				bool = 1;
            }
            
            counter++;
        }
        
        counter = 0;
        while (counter < n)
        {
            if(sztabyTabA[counter] == -3) sztabyTabA[counter] = -2;
            if(sztabyTabB[counter] == -3) sztabyTabB[counter] = -2;
            counter++;
        }
        
		if(bool == 0) break;
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
inline long getNumber()
{
	register long number = 0;
	int ASCII = GETC;
	if(ASCII < 32 && ASCII != NEW_LINE && ASCII != SPACE && ASCII != MINUS)
	{
	    ASCII = GETC;
	}
	if(ASCII == NEW_LINE || ASCII == SPACE)
	{
	    ASCII = GETC;
	}
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
inline void outNumber(long x)
{
    long divResult = x / 10;
	if(divResult != 0)
	{
	    outNumber(divResult);
	}
	PUTC((x % 10) + 48);
}
