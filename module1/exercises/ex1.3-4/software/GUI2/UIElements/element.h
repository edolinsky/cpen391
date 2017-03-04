/*
 * element.h
 *
 *  Created on: Feb 2, 2017
 *      Author: omar
 */

#ifndef BUTTON_H_
#define BUTTON_H_

#include <stdlib.h>
#include "../serial/graphics.h"
#include "../serial/touch.h"
#include "../main.h"
#include "../fonts/fonts.h"
#include "../serial/colours.h"
#include <math.h>
#include <string.h>

#define BREAK_KEY -1

typedef enum{
	TEXT_LARGE,
	TEXT_SMALL
}TextSize;

typedef struct element{
	int x,y;
	int width,height;
	int elementColour;
	int textColour;
	char *text;
	TextSize textSize;

	// clickable?
	void (*action)();
	int hasArg;
	int arg1;

}Element;

typedef struct elementNode{
	Element *e;
	Element *next;
}ElementNode;

typedef struct elementList{
	ElementNode *head;
	ElementNode *tail;
}ElementList;

// keep track of all the elements for touch-element mapping
ElementList *globalElementList;

Element* createElement(int x, int y, int width, int height, int colour);
void setElementText(Element *e, char* title);
void setElementAction(Element *e, void* action);
void addElementToList(Element *e);
void initElements();
void elementsCleanup();
void listenToTouches();
void refresh();

#endif /* BUTTON_H_ */
