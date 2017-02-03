/*
 * button.h
 *
 *  Created on: Feb 2, 2017
 *      Author: omar
 */

#ifndef BUTTON_H_
#define BUTTON_H_

#include <stdlib.h>
#include "../serial/colours.h"
#include "../serial/graphics.h"
#include "../serial/touch.h"

typedef struct button{
	int x,y;
	int width,height;
	int buttonColour;
	void (*action)();
}Button;

typedef struct buttonNode{
	Button *bt;
	Button *next;
}ButtonNode;

typedef struct buttonList{
	ButtonNode *head;
	ButtonNode *tail;
}ButtonList;

ButtonList *globalButtonList;

#endif /* BUTTON_H_ */
