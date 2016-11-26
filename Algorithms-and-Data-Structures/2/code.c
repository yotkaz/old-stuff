/*  yotkaz.asd.2 */
/* 
    TRYING TO IMPLEMENT MY VISION OF TREE LINKED LIST


                          1 0                               head proxy (with number of all targets)
                           |                                
                -----------------------                     
                5                     5                     proxies (with number of targets)
                |                     |                     
           -----------           -----------                
           2         3           3         2                
           |         |           |         |                
         -----   ---------   ---------   -----              
         |   |   |   |   |   |   |   |   |   |              
    .--- a - b - c - d - e - f - g - h - i - j ---.         targets (with values) 
    '--------------------<-<-<--------------------'   
        
*/

#include <stdio.h>
#include <stdlib.h>
#define GETC fgetc_unlocked(stdin)          // FAST INPUT CHAR
#define PUTC(x) fputc_unlocked(x, stdout)   // FAST OUTPUT CHAR
#define TARGET 1                            // NODES ON THE BOTTOM
#define PROXY 0                             // OTHER NODES
#define E0I -1                              // END OF INPUT ASCII - 10 FOR ENTER, -1 FOR END OF FILE
#define NULL_POINTER 0

//  I/O
inline long getNumber();
inline void outNumber(long x);

//  END OF INPUT - GLOBAL VARIABLE
int endOfInput = 0;


struct Node
{
    int type;                               // TARGET OR PROXY
    long size;                              // NUMBER OF SANS FOR PROXY
    long value;                             // VALUE 'C' FOR TARGET NODE
    
    struct Node* up;                        // NODE ABOVE THIS
    struct Node* branch1;                   // FOR TARGET: PREV
    struct Node* branch2;                   // FOR TARGET: NEXT
    struct Node* branch3;
    struct Node* branch4;                   
};

inline void printList(struct Node* currentNode, long numberOfNodes);
void recountNodes(struct Node* tmpPointer);
inline struct Node* createNode(int type, long size, long value, struct Node* up, struct Node* branch1, struct Node* branch2, struct Node* branch3, struct Node* branch4);
inline struct Node* addTargetNode(long value, struct Node* currentNode);
void addNodeToTree(struct Node* nodeToTree, struct Node* nodeOnTheLeft);
struct Node* deleteTargetNode(struct Node* currentNode);
void deleteNode(struct Node* currentNode);
struct Node* moveForward(long steps, struct Node* currentNode, struct Node* firstNode, long numberOfTargets);


int main()
{
    struct Node* firstNode;    
    struct Node* currentNode;
    long numberOfTargets = 0;
    long repeats = getNumber();
    
    if(endOfInput == 0)
    {
        firstNode = createNode(TARGET, 1, getNumber(), NULL_POINTER, NULL_POINTER, NULL_POINTER, NULL_POINTER, NULL_POINTER);
        firstNode->branch1 = firstNode;
        firstNode->branch2 = firstNode;
        currentNode = firstNode;
        numberOfTargets++;
    }
    else
    {
        printf("%i", -1);
        goto endProgram;
    }
    
    while(endOfInput == 0)
    {
        currentNode = addTargetNode(getNumber(), currentNode);
        numberOfTargets++;
    }
    
    currentNode = firstNode;
    
    while(repeats > 0)
    {
        if(currentNode->value % 2 == 0)
        {     
            if(numberOfTargets == 1)
            {
                printf("%i", -1);
                goto endProgram;
            }
            currentNode = currentNode->branch2;
            long stepsToGo = currentNode->value;
            if(firstNode == currentNode)
            {
                firstNode = currentNode->branch2;
            }
            currentNode = deleteTargetNode(currentNode);
            currentNode = currentNode->branch1;
            numberOfTargets--;
            currentNode = moveForward(stepsToGo, currentNode, firstNode, numberOfTargets);
        }
        else
        {
            long stepsToGo = currentNode->value;
            currentNode = addTargetNode(currentNode->value - 1, currentNode);
            currentNode = currentNode->branch1;
            numberOfTargets++;
            currentNode = moveForward(stepsToGo, currentNode, firstNode, numberOfTargets);
        }
        repeats--;
    }
    
    printList(currentNode, numberOfTargets);
    
    endProgram:
    return 0; 
}

//  CREATE NODE AND RETURN POINTER
inline struct Node* createNode(int type, long size, long value, struct Node* up, struct Node* branch1, struct Node* branch2, struct Node* branch3, struct Node* branch4)
{
    struct Node* returnNode = (struct Node*)malloc(sizeof(struct Node));
    returnNode->type = type;
    returnNode->size = size;
    returnNode->value = value;
    returnNode->up = up;
    returnNode->branch1 = branch1;
    returnNode->branch2 = branch2;
    returnNode->branch3 = branch3;
    returnNode->branch4 = branch4;
    return returnNode;
}

//  ADD NEW TARGET NODE AND RETURN POINTER
inline struct Node* addTargetNode(long value, struct Node* currentNode)
{
    struct Node* newNode = createNode(TARGET, 1, value, currentNode->up, currentNode, currentNode->branch2, NULL_POINTER, NULL_POINTER);
    addNodeToTree(newNode, currentNode);
    currentNode->branch2 = newNode;
    newNode->branch2->branch1 = newNode;
    return newNode;
}

//  FIND A PLACE FOR EXISTING NODE ON THE TREE
void addNodeToTree(struct Node* nodeToTree, struct Node* nodeOnTheLeft) 
{
    if(nodeToTree->up == NULL_POINTER) // node->up is head
    {
        struct Node* newHeadNode = createNode(PROXY, nodeToTree->size + nodeOnTheLeft->size, 0, NULL_POINTER, nodeOnTheLeft, nodeToTree, NULL_POINTER, NULL_POINTER);
        nodeOnTheLeft->up = newHeadNode;
        nodeToTree->up = newHeadNode;
    }
    else if(nodeToTree->up->branch3 != NULL_POINTER) // only one branch is free
    {
        if(nodeOnTheLeft == nodeToTree->up->branch1)
        {
            nodeToTree->up->branch4 = nodeToTree->up->branch3;
            nodeToTree->up->branch3 = nodeToTree->up->branch2;
            nodeToTree->up->branch2 = nodeToTree;
        }
        else if(nodeOnTheLeft == nodeToTree->up->branch2)
        {
            nodeToTree->up->branch4 = nodeToTree->up->branch3;
            nodeToTree->up->branch3 = nodeToTree;
        }
        else
        {
            nodeToTree->up->branch4 = nodeToTree;
        } 
        struct Node* leftUpNode = nodeToTree->up;
        struct Node* newRightUpNode = createNode(PROXY, leftUpNode->branch3->size + leftUpNode->branch4->size, 0, leftUpNode->up, leftUpNode->branch3, leftUpNode->branch4, NULL_POINTER, NULL_POINTER);
        leftUpNode->branch3 = NULL_POINTER;
        leftUpNode->branch4 = NULL_POINTER;
        leftUpNode->size = leftUpNode->branch1->size + leftUpNode->branch2->size;
        newRightUpNode->branch1->up = newRightUpNode;
        newRightUpNode->branch2->up = newRightUpNode;
        
        recountNodes(newRightUpNode);
        addNodeToTree(newRightUpNode, leftUpNode);   
    }
    else if(nodeToTree->up->branch2 != NULL_POINTER) // two branches are free
    {
        nodeToTree->up->size = nodeToTree->up->size + nodeToTree->size;
        if(nodeOnTheLeft == nodeToTree->up->branch1)
        {
            nodeToTree->up->branch3 = nodeToTree->up->branch2;
            nodeToTree->up->branch2 = nodeToTree;
        }
        else
        {
            nodeToTree->up->branch3 = nodeToTree;
        }
        recountNodes(nodeToTree->up);
        
    }
    else // three branches are free
    {
        nodeToTree->up->size = nodeToTree->up->size + nodeToTree->size;
        nodeToTree->up->branch2 = nodeToTree;
        recountNodes(nodeToTree->up);      
    }
}

//  DELETE TARGET NODE FROM LINKED LIST AND RETURN CURRENT NODE
struct Node* deleteTargetNode(struct Node* currentNode)
{
    struct Node* prevNode = currentNode->branch1;
    struct Node* nextNode = currentNode->branch2;
    prevNode->branch2 = nextNode;
    nextNode->branch1 = prevNode;
    deleteNode(currentNode);
    return nextNode;
}

//  DELETE NODE (PROXY OR TARGET)
void deleteNode(struct Node* currentNode)
{
    struct Node* tmpPointer = currentNode;
    if(tmpPointer->up->branch2 != NULL_POINTER && tmpPointer->up->branch3 != NULL_POINTER)
    {
        if(tmpPointer->up->branch1 == tmpPointer)
        {
            tmpPointer->up->branch1 = tmpPointer->up->branch2;
            tmpPointer->up->branch2 = tmpPointer->up->branch3;
            tmpPointer->up->branch3 = NULL_POINTER;
            tmpPointer->up->size -= tmpPointer->size;
            recountNodes(tmpPointer->up);
            free(tmpPointer);
        }
        else if(tmpPointer->up->branch2 == tmpPointer)
        {
            tmpPointer->up->branch2 = tmpPointer->up->branch3;
            tmpPointer->up->branch3 = NULL_POINTER;
            tmpPointer->up->size -= tmpPointer->size;
            recountNodes(tmpPointer->up);
            free(tmpPointer);
        }
        else
        {
            tmpPointer->up->branch3 = NULL_POINTER;
            tmpPointer->up->size -= tmpPointer->size;
            recountNodes(tmpPointer->up);
            free(tmpPointer);
        }
    } 
    else if(tmpPointer->up->branch1 != NULL_POINTER && tmpPointer->up->branch2 != NULL_POINTER)
    {
        if(tmpPointer->up->up == NULL_POINTER)
        {
            if(tmpPointer->up->branch1 == tmpPointer)
            {
                tmpPointer->up->branch2->up = NULL_POINTER;
                free(tmpPointer->up);
                free(tmpPointer);
            }
            else
            {
                tmpPointer->up->branch1->up = NULL_POINTER;
                free(tmpPointer->up);
                free(tmpPointer);
            }
        }
        else
        {
            if(tmpPointer->up->branch1 == tmpPointer)
            {
                struct Node* nodeToMove = tmpPointer->up->branch2;
                if(nodeToMove->up->up->branch1 == nodeToMove->up)
                {
                    nodeToMove->up->up->branch1 = nodeToMove;
                }
                else if(nodeToMove->up->up->branch2 == nodeToMove->up)
                {
                    nodeToMove->up->up->branch2 = nodeToMove;
                }
                else if(nodeToMove->up->up->branch3 == nodeToMove->up)
                {
                    nodeToMove->up->up->branch3 = nodeToMove;
                }
                struct Node* upNodeToDelete = nodeToMove->up;
                free(upNodeToDelete);
                nodeToMove->up = tmpPointer->up->up;
                free(tmpPointer);                
                recountNodes(nodeToMove);    
            }
            else if(tmpPointer->up->branch2 == tmpPointer)
            {
                struct Node* nodeToMove = tmpPointer->up->branch1;
                if(nodeToMove->up->up->branch1 == nodeToMove->up)
                {
                    nodeToMove->up->up->branch1 = nodeToMove;
                }
                else if(nodeToMove->up->up->branch2 == nodeToMove->up)
                {
                    nodeToMove->up->up->branch2 = nodeToMove;
                }
                else if(nodeToMove->up->up->branch3 == nodeToMove->up)
                {
                    nodeToMove->up->up->branch3 = nodeToMove;
                }
                struct Node* upNodeToDelete = nodeToMove->up;
                free(upNodeToDelete);
                nodeToMove->up = tmpPointer->up->up;
                free(tmpPointer); 
                recountNodes(nodeToMove);        
            }
        }
    }
}

//  MOVE FORWARD AND RETURN CURRENT NODE
struct Node* moveForward(long steps, struct Node* currentNode, struct Node* firstNode, long numberOfTargets)
{
    if(numberOfTargets < steps)
    {
        steps = steps % numberOfTargets;
    }
    if(steps == 0)
    {
        return currentNode;
    }
    if((currentNode->type == TARGET) && (currentNode->up == NULL_POINTER))
    {
        while(steps > 0)
        {
            currentNode = currentNode->branch2;
            steps--;
        }
    }
    else
    {
        struct Node* tempPointer = currentNode;
        
        moveUp:
        while(tempPointer->up != NULL_POINTER)
        {
            if(tempPointer->up->branch1 == tempPointer)
            {
                long tmpSize = tempPointer->up->branch2->size;
                if(steps > tmpSize)
                {
                    steps -= tmpSize;
                }
                else
                {               
                    tempPointer = tempPointer->up->branch2;
                    if(tempPointer->type == TARGET && steps == 1)
                    {
                        currentNode = tempPointer;
                        goto moved;
                    }
                    else
                    {
                        goto moveDown;
                    }
                }                
                if(tempPointer->up->branch3 != NULL_POINTER)
                {
                    long tmpSize = tempPointer->up->branch3->size;
                    if(steps > tmpSize)
                    {
                        steps -= tmpSize;
                    }
                    else
                    {               
                        tempPointer = tempPointer->up->branch3;
                        if(tempPointer->type == TARGET && steps == 1)
                        {
                            currentNode = tempPointer;
                            goto moved;
                        }
                        else
                        {
                            goto moveDown;
                        }
                    }
                }
            }
            else if(tempPointer->up->branch2 == tempPointer)
            {
                if(tempPointer->up->branch3 != NULL_POINTER)
                {
                    long tmpSize = tempPointer->up->branch3->size;
                    if(steps > tmpSize)
                    {
                        steps -= tmpSize;
                    }
                    else
                    {               
                        tempPointer = tempPointer->up->branch3;
                        if(tempPointer->type == TARGET && steps == 1)
                        {
                            currentNode = tempPointer;
                            goto moved;
                        }
                        else
                        {
                            goto moveDown;
                        }
                    }
                }
            }         
            tempPointer = tempPointer->up;            
        }
     
        steps--;
        if(steps == 0)
        {
            currentNode = firstNode;
            goto moved;
        } 
        else
        {
            tempPointer = firstNode;
            goto moveUp;
        }
        
        
        moveDown:
        if(steps > tempPointer->branch1->size)
        {
            steps -= tempPointer->branch1->size;
        }
        else
        {
            if(steps == 1 && tempPointer->branch1->type == TARGET)
            {
                currentNode = tempPointer->branch1;
                goto moved;
            }
            else
            {
                tempPointer = tempPointer->branch1;
                goto moveDown;
            }      
        }
        if(tempPointer->branch2 != NULL_POINTER)
        {
            if(steps > tempPointer->branch2->size)
            {
                steps -= tempPointer->branch2->size;
            }
            else
            {
                if(steps == 1 && tempPointer->branch2->type == TARGET)
                {
                    currentNode = tempPointer->branch2;
                    goto moved;
                }
                else
                {
                    tempPointer = tempPointer->branch2;
                    goto moveDown;
                }   
            }
        }
        if(tempPointer->branch3 != NULL_POINTER)
        {
            if(steps > tempPointer->branch3->size)
            {
                steps -= tempPointer->branch3->size;
            }
            else
            {
                if(steps == 1 && tempPointer->branch3->type == TARGET)
                {
                    currentNode = tempPointer->branch3;
                    goto moved;
                }
                else
                {
                    tempPointer = tempPointer->branch3;
                    goto moveDown;
                }                
            }
        }
    }
        
    moved:
    return currentNode;
}

//  RECOUNT PROXIES NODES
void recountNodes(struct Node* tmpPointer)
{
    long size;
    
    struct Node* tmpUp = tmpPointer->up;
    while(tmpPointer->up != NULL_POINTER)
    { 
        tmpUp = tmpPointer->up;
        size = tmpUp->branch1->size;      
        if(tmpUp->branch2 != NULL_POINTER)
        {
            size += tmpUp->branch2->size; 
        }       
        if(tmpUp->branch3 != NULL_POINTER)
        {
            size += tmpUp->branch3->size; 
        }    
        tmpUp->size = size;
        tmpPointer = tmpUp;
    }
}

//  PRINT LIST
inline void printList(struct Node* currentNode, long numberOfTargets)
{
    struct Node* tmpPointer = currentNode;
    while(numberOfTargets > 1)
    {
        outNumber(tmpPointer->value);
        tmpPointer = tmpPointer->branch2;
        numberOfTargets--;
        PUTC(32);
    }
    outNumber(tmpPointer->value);
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
	if(ASCII == E0I) endOfInput = 1;
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
