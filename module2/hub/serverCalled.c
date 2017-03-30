#include "serverCalled.h"
#include "mainmenu.h"
#include "apps/notify.h"

void drawServerCalled(void){
	int i;
	int leftJust = 150;
	int instrStart = 150;
	int incr = 15;
	char title[] = "Your server has been called";
	char back[] = "Back to main screen";

	// Trigger notification to backend.
	call_attendant();

	for(i = 0; i < 480; i ++){
		//   x1,y1, len, Colour
		HLine(0, i, 800, GRAY);
	}

	// Write page title
	for(i = 0; i < strlen(title); i++){
		OutGraphicsCharFont2a((400 - strlen(title)*5)+i*10, 200, WHITE, WHITE, title[i], 0);
	}

	initElements();

	// Create three buttons
	//                                    x,   y, wdth, ht, colour
	Element *backButton = createElement(100, 350, 600, 100, DIM_GRAY);

	// Set actions
	setElementAction(backButton, &drawOrderPage);

	// Draw buttons
	addElementToList(backButton);

	refresh();


	// Write back button title
	for(i = 0; i < strlen(back); i++){
		OutGraphicsCharFont2a((400 - strlen(back)*5)+i*10, 400, WHITE, WHITE, back[i], 0);
	}

	listenToTouches();
}
