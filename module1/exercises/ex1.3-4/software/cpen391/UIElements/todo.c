#include <stdio.h>
#include "../serial/touch.h"
#include "../serial/graphics.h"
#include "../serial/colours.h"
#include "shopping.h"
#include "element.h"

#include "todo.h"

char *notes[30];
int colours[] = {5,6,7,8,5,6,7,8};

void drawTodo(void){
	char title[] = "Notes";
	char add[] = "add note";
	char back[] = "back";
	char next[] = "next";
	int listStart = 100;
	int listLeft = 100;
	int i;

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
	setElementAction(addButton, &newNote); // have to make an add to list feature
	setElementText(addButton, add);
	addElementToList(addButton);

	// No line checks rn
	for (i = 0; i < 4; i++){

		if(notes[i]!=NULL){

			Element *item = createElement(listLeft+25*i+130*i, listStart, 130, 130, colours[i]);
			setElementText(item, notes[i]);
			item->hasArg = 1;
			item->arg1 = (int) i;
			item->action = &removeFromNotes;
			addElementToList(item);
		}
	}

	for (i = 4; i < 8; i++){

			if(notes[i]!=NULL){

				Element *item = createElement(listLeft+25*(i-4)+130*(i-4), listStart+150, 130, 130, colours[i]);
				setElementText(item, notes[i]);
				item->hasArg = 1;
				item->arg1 = (int) i;
				item->action = &removeFromNotes;
				addElementToList(item);
			}
		}


	// TODO: add next page button so we can see all the list

	// Listen to touch screen
	refresh();
	listenToTouches();
}


void newNote(void){
	char *typed = keyboard();
	char *val = strcpy(val, typed);
	addToNotes(val);
	drawTodo();
}

void removeFromNotes(int j){
	char* name = notes[j];
	notes[j] = NULL;
	free(name);
	drawTodo();
}

void addToNotes(char *string){
	int i;

	// put the string in first available spot in the array
	for(i = 0; i < 30; i++){
		if(notes[i] == NULL){
			notes[i] = string;
			break;
		}
	}

}
