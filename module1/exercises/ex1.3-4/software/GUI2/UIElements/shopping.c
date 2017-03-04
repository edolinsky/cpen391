#include "todo.h"
#include <stdio.h>
#include "../serial/touch.h"
#include "../serial/graphics.h"
#include "../serial/colours.h"
#include "todo.h"
#include "element.h"

#include "shopping.h"

char *shoppinglist[30];
int head;


void drawShopping(void){
	char title[] = "Shopping List";
	char add[] = "add note";
	char back[] = "back";
	char next[] = "next";
	int listStart = 100;
	int listLeft = 50;
	int i;
	//printf("%s", shoppinglist[0]);

	elementsCleanup();
	// Shade out the background
	for(i = 0; i < 480; i ++){
		HLine(0, i, 800, GRAY);
	}

	// Write title
	for(i = 0; i < 14; i++){
		OutGraphicsCharFont2a(350+i*10, 25, BLACK, WHITE, title[i], 0);
	}

	initElements();

	// Create back button
	Element *backButton = createElement(0, 0, 100, 50, DIM_GRAY);
	setElementAction(backButton, &drawMenu);
	addElementToList(backButton);

	// Create add button
	Element *addButton = createElement(700, 0, 100, 50, DIM_GRAY);
	setElementAction(addButton, &newItem); // have to make an add to list feature
	addElementToList(addButton);

//	// Page down button
//	Element *nextPage = createElement(750, 430, 50, 50, DIM_GRAY);
//	setElementAction(nextPage, &nextPage); // have to make a next page function
//	addElementToList(nextPage);
//
//	// Previous page button
//	Element *prevPage = createElement(750, 110, 50, 50, DIM_GRAY);
//	setElementAction(prevPage, &previousPage); // have to make a previous page function
//	addElementToList(prevPage);

	Element *TEST = createElement(600, 0, 50, 50, BLACK);
	setElementAction(TEST, &testAdd); // have to make a previous page function
	addElementToList(TEST);

	refresh();

	//drawArrows();


	// Write the name of the add button
	for(i = 0; i < 9; i++){
		OutGraphicsCharFont2a(710+i*10, 25, WHITE, RED, add[i], 0);
	}

	// Write name of the back button
	for(i = 0; i < 4; i++){
		OutGraphicsCharFont2a(25+i*10, 25, WHITE, RED, back[i], 0);
	}


	// No line checks rn
	for (i = 0; i < 6; i++){

		if(shoppinglist[i]!=NULL){

			Element *item = createElement(listLeft, listStart + i*55, 600, 50, GRAY);
			setElementText(item, shoppinglist[i]);
			addElementToList(item);
			refresh();


		}

	}


	// TODO: add next page button so we can see all the list

	// Listen to touch screen
	listenToTouches();
}


void newItem(void){
	char *new = keyboard();
	addToShoppingList(new);

}

void testAdd(){
	char *test = "test";
	addToShoppingList(test);
	//printf("%s", shoppinglist[0]);
	drawShopping();
}

void removeFromShoppingList(int j){
	shoppinglist[j] = NULL;
	drawShopping();
}

void addToShoppingList(char *string){
	int i;

	// put the string in first available spot in the array
	for(i = 0; i < 30; i++){
		if(shoppinglist[i] == NULL){
			shoppinglist[i] = string;
			break;
		}
	}

}

void drawArrows(){
	int i;
	for(i = 0; i < 6; i++){
		Line(750+i*1, 430, 775+i*1, 480, WHITE);
	}

	for(i = 0; i < 6; i++){
		Line(800+i*1, 430, 775+i*1, 480, WHITE);
	}

	for(i = 0; i < 6; i++){
		Line(750+i*1, 160, 775+i*1, 110, WHITE);
	}

	for(i = 0; i < 6; i++){
		Line(800+i*1, 160, 775+i*1, 110, WHITE);
	}

	for(i = 160; i < 430; i++){
		HLine(750, i, 50, SILVER);
	}
}

void drawCheckbox(int x, int y){
	int side = 15;

	VLine(x, y, side, BLACK);
	VLine(x + side, y, side, BLACK);
	HLine(x, y, side, BLACK);
	HLine(x, y+side, side, BLACK);
}
