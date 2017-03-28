#include "successScreen.h"

void drawSuccess(void){
	int i;
	int leftJust = 150;
	int instrStart = 150;
	int incr = 15;
	char title[] = "Orders will appear here after you order on your phone";
	char call[] = "Call your server";
	char test[] = "test";

	for(i = 0; i < 480; i ++){
		//HLine(int x1, int y1, int length, int Colour)
		HLine(0, i, 800, GRAY);
	}

	// Write page title
	for(i = 0; i < strlen(title); i++){
		OutGraphicsCharFont2a((400 - strlen(title)*5)+i*10, 50, WHITE, WHITE, title[i], 0);
	}

	initElements();

	//createElement(int x, int y, int width, int height, int colour);

	// Create three buttons
	Element *callButton = createElement(575, 425, 225, 75, DIM_GRAY);
	Element *testButton = createElement(200, 200, 400, 100, DIM_GRAY);

	// Set actions
	setElementAction(callButton, &drawServerCalled);
	setElementAction(testButton, &drawOrderPage);

	// Draw buttons
	addElementToList(callButton);
	addElementToList(testButton);

	refresh();


	// Write back button title
	for(i = 0; i < strlen(call); i++){
		OutGraphicsCharFont2a((775 - strlen(call)*10)+i*10, 450, WHITE, WHITE, call[i], 0);
	}

	// Write back button title
	for(i = 0; i < strlen(test); i++){
		OutGraphicsCharFont2a((400 - strlen(test)*5)+i*10, 250, WHITE, WHITE, test[i], 0);
	}

	listenToTouches();
}
