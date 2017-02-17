#include "keyboard.h"

void drawKeyboard() {

	screenFill(RED);

	char array[27] = { 'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a',
			's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b',
			'n', 'm', ' ' };

	int xMargin = 10;
	int xPos = 20;
	int i;
	int c = 0;
	for (i = 0; i < 10; i++) {
		Element *bt = createElement(xMargin + xPos + 55, 230, 55, 55, GRAY);
		xPos += xMargin + 55;
		char c1[2] = {array[c], '\0'};
		setElementText(bt, c1);
		setElementAction(bt, &type);
		bt->hasArg = 1;
		bt->arg1 = (int) array[c];
		bt->action = &type;
		c++;
		addElementToList(bt);
	}

	xPos = 60;
	for (i = 0; i < 9; i++) {
		Element *bt = createElement(xMargin + xPos + 55, 290, 55, 55, GRAY);
		xPos += xMargin + 55;
		char c2[2] = {array[c], '\0'};
		setElementText(bt, c2);
		setElementAction(bt, &type);
		bt->hasArg = 1;
		bt->arg1 = (int) array[c];
		bt->action = &type;
		c++;
		addElementToList(bt);
	}

	xPos = 100;
	for (i = 0; i < 7; i++) {
		Element *bt = createElement(xMargin + xPos + 55, 350, 55, 55, GRAY);
		xPos += xMargin + 55;
		char c3[2] = {array[c], '\0'};
		setElementText(bt, c3);
		setElementAction(bt, &type);
		bt->hasArg = 1;
		bt->arg1 = (int) array[c];
		bt->action = &type;
		c++;
		addElementToList(bt);
	}

	Element *bt = createElement(300, 410, 200, 60, GRAY);
	xPos += xMargin + 70;
//	char c4[2] = {array[c], '\0'};
	setElementText(bt, "the quick brown fox jumped over the lazy dog");
	setElementAction(bt, &type);
	bt->hasArg = 1;
	bt->arg1 = (int) array[c];
	bt->action = &type;
	c++;
	addElementToList(bt);

	refresh();
	listenToTouches();
}

void type(int letter) {
	printf("%c", (char) letter);
}

