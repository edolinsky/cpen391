#include "keyboard.h"

Point pos;
char text[MAX_CHARACTERS];
int charCount;

char* keyboard() {
	// init typing space
	pos.x = MARGIN;
	pos.y = MARGIN;
	charCount = 0;
	drawUi();
	drawKeys();
	refresh();
	listenToTouches();
	text[charCount] = '\0';
	return text;
}

void drawUi() {
	screenFill(BLACK);
	filledRectangle(MARGIN, MARGIN, XRES - 1 - MARGIN, TEXTAREA - MARGIN, GRAY);
}

void drawKeys() {

	char array[27] = { 'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a',
			's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b',
			'n', 'm', ' ' };

	int i;
	int c = 0;
	for (i = 0; i < 10; i++) {
		Element *bt = createElement(MARGIN + i * KEYSIZE, TEXTAREA, KEYSIZE,
				KEYSIZE, GRAY);
		char c1[2] = {array[c], '\0'};
		setElementText(bt, c1);
		setElementAction(bt, &type);
		bt->hasArg = 1;
		bt->arg1 = (int) array[c];
		bt->action = &type;
		c++;
		addElementToList(bt);
	}

	int offset = 40;
	for (i = 0; i < 9; i++) {
		Element *bt = createElement(offset + MARGIN + i * KEYSIZE, TEXTAREA + KEYSIZE,
				KEYSIZE, KEYSIZE, GRAY);
		char c2[2] = {array[c], '\0'};
		setElementText(bt, c2);
		setElementAction(bt, &type);
		bt->hasArg = 1;
		bt->arg1 = (int) array[c];
		bt->action = &type;
		c++;
		addElementToList(bt);
	}

	offset = 100;
	for (i = 0; i < 7; i++) {
		Element *bt = createElement(offset + MARGIN + i * KEYSIZE, TEXTAREA + 2*KEYSIZE,
				KEYSIZE, KEYSIZE, GRAY);
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
	char c4[2] = {array[c], '\0'};
	setElementAction(bt, &type);
	bt->hasArg = 1;
	bt->arg1 = (int) array[c];
	bt->action = &type;
	c++;
	addElementToList(bt);
}

void type(int letter) {

	if(charCount >= MAX_CHARACTERS) {
		return;
	}

	printf("%c", (char) letter);
	// print char
	OutGraphicsCharFont2a(pos.x, pos.y, BLACK, GRAY, letter, FALSE);

	// add to text
	text[charCount++] = letter;

	// update pos
	// room for another character on this line
	if(pos.x <= XRES - 1 - MARGIN - FONT2_WIDTH) {
		pos.x += FONT2_WIDTH;
	} else {
		// next line
		pos.x = MARGIN;
		pos.y += FONT2_HEIGHT;
	}
}
