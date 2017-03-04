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
	screenFill(GRAY);

	// Write title
	for(i = 0; i < 14; i++){
		OutGraphicsCharFont2a(350+i*10, 25, BLACK, WHITE, title[i], 0);
	}

	initElements();

	// Create back button
	Element *backButton = createElement(0, 0, 100, 50, DIM_GRAY);
	setElementAction(backButton, &drawMenu);
	setElementText(backButton, back);
	addElementToList(backButton);

	// Create add button
	Element *addButton = createElement(700, 0, 100, 50, DIM_GRAY);
	setElementAction(addButton, &newItem); // have to make an add to list feature
	setElementText(addButton, add);
	addElementToList(addButton);

	// No line checks rn
	for (i = 0; i < 6; i++){

		if(shoppinglist[i]!=NULL){

			Element *item = createElement(listLeft, listStart + i*55, 700, 50, GRAY);
			setElementText(item, shoppinglist[i]);
			item->hasArg = 1;
			item->arg1 = (int) i;
			item->action = &removeFromShoppingList;
			addElementToList(item);
		}
	}


	// TODO: add next page button so we can see all the list

	// Listen to touch screen
	refresh();
	listenToTouches();
}


void newItem(void){
	char *typed = keyboard();
	char *val = strcpy(val, typed);
	addToShoppingList(val);
	drawShopping();
}

void removeFromShoppingList(int j){
	char* name = shoppinglist[j];
	shoppinglist[j] = NULL;
	free(name);
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
