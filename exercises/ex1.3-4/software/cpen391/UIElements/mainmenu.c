#include "mainmenu.h"

void drawMenu(void){
	int i;
	char title[] = "Whiteboard";
	char calendarName[] = "Calendar";
	char todoName[] = "Todo";
	char shoppingName[] = "Shopping";
	// 800-480

	// Shade out the background
	for(i = 0; i < 479; i ++){
		//HLine(int x1, int y1, int length, int Colour)
		HLine(0, i, 799, WHITE);
	}

	// Write title
	for(i = 0; i < 10; i++){
		//OutGraphicsCharFont2a(int x, int y, int colour, int backgroundcolour, int c, int Erase);
		// This is not right spacing or placement (using 10x14)
		OutGraphicsCharFont2a(360+i*8, 75, BLACK, WHITE, title[i], 0);
	}

	initElements();

	// Create three buttons
	//createButton(int x, int y, int width, int height, int colour)
	// Not in the right place
	//Button *calendarButton = createButton(300, 200, 200, 100, BLUE);
	Element *todoButton = createElement(300, 300, 200, 100, BLUE);
	//Button *shoppingButton = createButton(300, 400, 200, 100, BLUE);

	// Set actions
	//setButtonAction(calendarButton, void* action); 	// draw calendar gui
//	setElementAction(todoButton, &drawTodo); 		// draw todo gui
	//setButtonAction(shoppingButton, void* action);	// draw shopping gui

	// Draw buttons
	refresh();

	listenToTouches();
}
