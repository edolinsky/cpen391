#include "mainmenu.h"
#include <stdio.h>
#include "../serial/touch.h"
#include "../serial/graphics.h"
#include "../serial/colours.h"
#include "todo.h"
#include "shopping.h"
#include "../apps/calendar.h"

void drawMenu(void){
	int i;
	char title[] = "Whiteboard";
	char firstline[] = "Welcome to";
	char calendarName[] = "Calendar";
	char todoName[] = "Todo List";
	char shoppingName[] = "Shopping Lists";

	// Shade out the background
	for(i = 0; i < 480; i ++){
		//HLine(int x1, int y1, int length, int Colour)
		HLine(0, i, 800, GRAY);
	}

	// Write title
	for(i = 0; i < 10; i++){
		OutGraphicsCharFont2a(350+i*10, 75, BLACK, WHITE, title[i], 0);
	}

	// Write title
	for(i = 0; i < 10; i++){
		OutGraphicsCharFont2a(350+i*10, 50, BLACK, WHITE, firstline[i], 0);
	}

	initElements();

	// Create three buttons
	Element *calendarButton = 	createElement(100, 100, 600, 100, DIM_GRAY);
	Element *todoButton = 		createElement(100, 225, 600, 100, DIM_GRAY);
	Element *shoppingButton = 	createElement(100, 350, 600, 100, DIM_GRAY);

	// Set actions
	setElementAction(calendarButton, &displayCalendar);
	setElementAction(todoButton, &drawTodo);
	setElementAction(shoppingButton, &drawShopping);

	// Draw buttons
	addElementToList(calendarButton);
	addElementToList(todoButton);
	addElementToList(shoppingButton);

	refresh();

	// Write calendar title
	for(i = 0; i < 8; i++){
		OutGraphicsCharFont2a(360+i*10, 150, WHITE, WHITE, calendarName[i], 0);
	}

	// Write to do title
	for(i = 0; i < 9; i++){
		OutGraphicsCharFont2a(360+i*10, 275, WHITE, WHITE, todoName[i], 0);
	}

	// Write shopping title
	for(i = 0; i < 14; i++){
		OutGraphicsCharFont2a(330+i*10, 400, WHITE, WHITE, shoppingName[i], 0);
	}

	listenToTouches();
}
