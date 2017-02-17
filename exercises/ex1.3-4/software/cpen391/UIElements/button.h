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
	char character;
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

// keep track of all the buttons for touch-button mapping
ButtonList *globalButtonList;

Button* createButton(int x, int y, int width, int height, int colour);
void* setButtonTitle(Button *bt, char* title);
void setButtonCharacter(Button *bt, char letter);
void* setButtonAction(Button *bt, void* action);
void drawButton(Button *bt);
void destroyButton(Button *bt);
void initButtons();
void buttonsCleanup();
void listenToTouches();

#endif /* BUTTON_H_ */
