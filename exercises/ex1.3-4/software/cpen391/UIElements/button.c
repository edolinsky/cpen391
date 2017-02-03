/*
 * button.c
 *
 *  Created on: Feb 2, 2017
 *      Author: omar
 */
#include "button.h"

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

void* setButtonAction(Button *bt, void* action) {
	bt->action = action;
}

void drawButton(Button *bt) {
	int x, y;
	for (x = bt->x; x <= bt->x + bt->width; x++) {
		for (y = bt->y; y <= bt->y + bt->height; y++) {
			WriteAPixel(x, y, bt->buttonColour);
		}
	}

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
	// do something to un-draw the button
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
listen:
	p = getRelease();
	ButtonNode *node = globalButtonList->head;
	printf("%d, %d\n", p.x, p.y);

	while (node != NULL) {
		Button *bt = node->bt;
		// check if the touch is within this buttons bounds
		if (p.x > bt->x && p.x < (bt->x + bt->width) && p.y > bt->y
				&& p.y < (bt->y + bt->height)) {
			(*(bt->action))();
			printf("Ha!");
			break;
		}
		node = node->next;
		printf("Hello?");

	}

	goto listen;
}
