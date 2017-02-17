#include "keyboard.h"

void drawKeyboard() {
	int x, y;
	for (x = 0; x < 800; x++) {
		for (y = 0; y < 480; y++) {
			WriteAPixel(x, y, WHITE);
		}
	}

	char array[27] = { 'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a',
			's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b',
			'n', 'm', ' ' };

	int xMargin = 10;
	int xPos = 20;
	int i;
	int c = 0;
	for (i = 0; i < 10; i++) {
		Button *btn3 = createButton(xMargin + xPos + 55, 230, 55, 55, GRAY);
		xPos += xMargin + 55;
		setButtonAction(btn3, &type);
		setButtonCharacter(btn3, array[c]);
		c++;
		drawButton(btn3);
	}

	xPos = 60;
	for (i = 0; i < 9; i++) {
		Button *btn3 = createButton(xMargin + xPos + 55, 290, 55, 55, GRAY);
		xPos += xMargin + 55;
		setButtonAction(btn3, &type);
		setButtonCharacter(btn3, array[c]);
		c++;
		drawButton(btn3);
	}

	xPos = 100;
	for (i = 0; i < 7; i++) {
		Button *btn3 = createButton(xMargin + xPos + 55, 350, 55, 55, GRAY);
		xPos += xMargin + 55;
		setButtonAction(btn3, &type);
		setButtonCharacter(btn3, array[c]);
		c++;
		drawButton(btn3);
	}

	Button *btn3 = createButton(300, 410, 200, 60, GRAY);
	xPos += xMargin + 70;
	setButtonAction(btn3, &type);
	setButtonCharacter(btn3, array[c]);
	c++;
	drawButton(btn3);

	listenToTouches();
}


void type(char letter) {
	printf("%c",letter);
}


