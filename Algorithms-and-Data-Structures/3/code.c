/*  yotkaz.asd.3, 24-04-2015 */

#include <stdio.h>
#include <stdlib.h>
#define GETC fgetc_unlocked(stdin)
#define PUTC(x) fputc_unlocked(x, stdout)
#define E0I -1
#define NEWLINE 10
#define SPACE 32
#define LEFT 76
#define RIGHT 82
#define NULLPTR 0

struct Node
{
    int ASCII;    
    struct Node* up;s
    struct Node* left;
    struct Node* right;
};

struct Node* postOrder(struct Node* node, struct Node* winner);

int main()
{
    struct Node* tab[26];
    struct Node* head = (struct Node*) malloc(sizeof(struct Node));
    struct Node* currentNode = head;
    struct Node* winner = (struct Node*) malloc(sizeof(struct Node));
    winner->ASCII = 0;
    
    int letterASCII = GETC;
    if(letterASCII == E0I) return 0;
    while(letterASCII != E0I)
    {
        currentNode = head;
        int ASCII = GETC;
        if(ASCII == SPACE)
        {
            ASCII = GETC;
            while(ASCII > 32)
            {
                if(ASCII == LEFT)
                {
                    if(currentNode->left != NULLPTR)
                    {
                        currentNode = currentNode->left;
                    }
                    else
                    {
                        struct Node* tmpNode = (struct Node*) malloc(sizeof(struct Node));
                        tmpNode->up = currentNode;
                        currentNode->left = tmpNode;
                        currentNode = tmpNode;
                    }
                    ASCII = GETC;
                }
                
                else
                {
                    if(currentNode->right != NULLPTR)
                    {
                        currentNode = currentNode->right;
                    }
                    else
                    {
                        struct Node* tmpNode = (struct Node*) malloc(sizeof(struct Node));
                        tmpNode->up = currentNode;
                        currentNode->right = tmpNode;
                        currentNode = tmpNode;
                    }
                    ASCII = GETC;
                }
            }
            
            currentNode->ASCII = letterASCII;
            if()
            
            if(ASCII == E0I)
            {
                goto end_input;
            }
        }
        else if(ASCII == NEWLINE)
        {
            head->ASCII = letterASCII;
        }
        else{
            head->ASCII = letterASCII;
            goto end_input;
        }
        letterASCII = GETC;
    }
    end_input:
      
    winner = postOrder(head, winner);
    
    PUTC(winner->ASCII);
    while(winner->up != NULLPTR)
    {      
        winner = winner->up;
        PUTC(winner->ASCII);
    }
    return 0;
}

struct Node* postOrder(struct Node* node, struct Node* winner)
{
    if(node->left != NULLPTR)
    {
        winner = postOrder(node->left, winner);
    }
    if(node->right != NULLPTR)
    {
        winner = postOrder(node->right, winner);
    }
    else if(node->left == NULLPTR)
    {

        if(node->ASCII > winner->ASCII)
        {
            winner = node;
        }
        else if(node->ASCII == winner->ASCII)
        {
            struct Node* tmpNode = node;
            struct Node* tmpWinnerNode = winner;
            while(tmpNode->ASCII == tmpWinnerNode->ASCII && tmpNode != tmpWinnerNode)
            {
                if(tmpNode->up == NULLPTR)
                {
                    goto upupend;
                }
                else if(tmpWinnerNode->up == NULLPTR)
                {
                    winner = node;
                    goto upupend;
                }
                tmpNode = tmpNode->up;
                tmpWinnerNode = tmpWinnerNode->up;
            }
            if(tmpNode != tmpWinnerNode)
            {
                if(tmpNode->ASCII > tmpWinnerNode->ASCII)
                {
                    winner = node;
                }
            }
        }
        upupend:;
    }
    return winner;
}