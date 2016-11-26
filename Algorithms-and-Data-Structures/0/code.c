/*  yotkaz.asd.0 */

#include <stdio.h>
#define G fgetc_unlocked(stdin)
#define P(x) fputc_unlocked(x, stdout)

inline int getNumber()
{
	register int number = 0;
	int ASCII = G;
	while(ASCII > 32){
	    number = number * 10 + (ASCII - 48);
	    ASCII = G;
	}
	return number;
}

inline void outNumber(int x)
{
    int divResult = x / 10;
	if(divResult != 0)
	{
	    outNumber(divResult);
	}
	P((x % 10) + 48);
}

inline int countChars(register long long int x, int d)
{
	if (d == 1)
	{
		return x;
	}

	if (x == 0)
	{
		return 1;
	}

	register int counter = 0;
	while (x != 0)
	{
		counter ++;
		x = x / d;
	}
	return counter;
}

int main()
{
	register long long int sum = 0;
	int n = getNumber();
	int d = getNumber();
    
	while (n >= 0)
	{
		sum += getNumber();
		n--;
	}
    
	outNumber(countChars(sum, d));
	return 0;
}
