#include "todo.h"

char *todoList[30];

void drawTodo(void){
	char title[] = "Shopping List";
	char add[] = "add event";
	char back[] = "back";
	int listStart = 100;
	int listLeft = 50;
	// Shade out the background
	int i;
	for(i = 0; i < 479; i ++){
		//HLine(int x1, int y1, int length, int Colour)
		HLine(0, i, 799, WHITE);
	}

	// Write title
	for(i = 0; i < 10; i++){
		//OutGraphicsCharFont2a(int x, int y, int colour, int backgroundcolour, int c, int Erase);
		// This is not right spacing or placement (using 10x14)
		OutGraphicsCharFont2a(344+i*8, 75, BLACK, WHITE, title[i], 0);
	}

	initElements();

	// Create back button
	Element *backButton = createElement(0, 0, 100, 50, RED);
	setElementAction(backButton, &drawMenu);

	// Create add button
	Element *addButton = createElement(700, 0, 100, 50, RED);
	setElementAction(addButton, &newEntry); // have to make an add to list feature

	refresh();

	// Write the name of the add button
	for(i = 0; i < 10; i++){
		//OutGraphicsCharFont2a(int x, int y, int colour, int backgroundcolour, int c, int Erase);
		// This is not right spacing or placement (using 10x14)
		OutGraphicsCharFont2a(720+i*8, 25, BLACK, RED, add[i], 0);
	}

	// Write name of the back button
	for(i = 0; i < 10; i++){
		//OutGraphicsCharFont2a(int x, int y, int colour, int backgroundcolour, int c, int Erase);
		// This is not right spacing or placement (using 10x14)
		OutGraphicsCharFont2a(25+i*8, 25, BLACK, RED, back[i], 0);
	}

	// TODO: CODE TO WRITE OUT LIST GOES HERE
	// each item needs a button, printed + wrapped string,
	int lines = 0;
	for(i = 0; i < 30 && lines < 26; i++){
		if(todoList[i] != NULL){
			// print that shit boi
			int j;

			// TODO: add button at the start of the first line

			// find null element?
			while(todoList[i][j] != '\0'){
				OutGraphicsCharFont2a(listLeft+j*8, listStart+14*lines, BLACK, WHITE, todoList[i][j], 0);
				j++;

				// check if we're at the end of the screen
				// TODO: prevent word splits
				if(j > 75){
					lines++;
				}
			}
			// New line
			lines++;
		}
	}

	// TODO: add next page button so we can see the

	// Listen to touch screen
	listenToTouches();
}

// This function is def broken
void newEntry(void){
	char *input;

	initElements();

	// need to update the keyboard to output the typed string
	// have to limit to 150 chars
	input = keyboard();

	//assuming I can just draw over the drawn keyboard
	Element *backButton = createElement(0, 0, 100, 50, RED);
	setElementAction(backButton, &drawMenu);

	//assuming I can just draw over the drawn keyboard
	Element *submitButton = createElement(7000, 0, 100, 50, RED);
	setElementAction(submitButton, &addToList); //need to pass argument, idk how

	refresh();

	listenToTouches();
}

// Remove element from the array, reprint the screen
void removeFromList(int j){
	todoList[j] = NULL;
	drawTodo();
}

void addToList(char *string){
	int i;

	// put the string in first available spot in the array
	for(i = 0; i < 30; i++){
		if(todoList[i] == NULL){
			todoList[i] = string;
			break;
		}
	}

	// if we got here, don't add anything
}
