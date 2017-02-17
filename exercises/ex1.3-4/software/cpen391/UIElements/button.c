/*
 * button.c
 *
 *  Created on: Feb 2, 2017
 *      Author: omar
 */
#include "button.h"
#include "../main.h"
#include "../serial/graphics.h"
#include "../fonts/fonts.h"
#include "../serial/colours.h"
#include <math.h>

Button* createButton(int x, int y, int width, int height, int colour) {
	Button *bt = malloc(sizeof(Button));
	bt->x = x;
	bt->y = y;
	bt->width = width;
	bt->height = height;
	bt->buttonColour = colour;
	return bt;
}

void* setButtonTitle(Button *bt, char* title) {

}

void setButtonCharacter(Button *bt, char letter) {
	bt->character = letter;
}

void* setButtonAction(Button *bt, void* action) {
	bt->action = action;
}

void drawButton(Button *bt) {
	int x, y;
	for (y = bt->y; y <= bt->y + bt->height; y++) {
		HLine(bt->x, y, bt->width, bt->buttonColour);
	}
	OutGraphicsCharFont2a(bt->x + bt->width / 2 - 5, bt->y + bt->height / 2 - 5,
			WHITE, WHITE, bt->character, FALSE);

	// add the button to the global list so it can respond to touches
	ButtonNode *newNode = malloc(sizeof(ButtonNode));
	newNode->bt = bt;
	newNode->next = NULL;
	if (globalButtonList->tail == NULL) {
		globalButtonList->head = newNode;
		globalButtonList->tail = newNode;
	} else {
		globalButtonList->tail->next = newNode;
		globalButtonList->tail = newNode;
	}
}

void destroyButton(Button *bt) {
	// do something to un-draw the button?
	free(bt);
}

/**
 * need to run this function before creating any buttons
 */
void initButtons() {
	globalButtonList = malloc(sizeof(ButtonList));
	globalButtonList->head = NULL;
	globalButtonList->tail = NULL;
}

/**
 * neet to call this function before exiting
 */
void buttonsCleanup() {
	ButtonNode *node = globalButtonList->head;
	while (node != NULL) {
		ButtonNode *next = node->next;
		free(node);
		node = next;
	}
}

void listenToTouches() {

	Point p;
	listen: getPress();
	p = getRelease();
	ButtonNode *node = globalButtonList->head;

	while (node != NULL) {
		Button *bt = node->bt;
		// check if the touch is within this buttons bounds
		if (p.x > bt->x && p.x < (bt->x + bt->width) && p.y > bt->y
				&& p.y < (bt->y + bt->height)) {
			(*(bt->action))(bt->character);
			break;
		}
		node = node->next;
	}

	goto listen;
}
