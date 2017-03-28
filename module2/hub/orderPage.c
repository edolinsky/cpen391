#include "orderPage.h"
#include "serverCalled.h"

void drawOrderPage(void){
	int i = 0;
	int vspace = 25;
	int itemwidth = 400;
	int statuswidth = 150;
	int namewidth = 150;
	int margin = 50;
	int tablestart = 75;
	char call[] = "Call your server";
	char item[] = "Item";
	char name[] = "Customer";
	char status[] = "Status";
	char orders[] = "Burger and fries,Erik,Cooking;Butter Chicken,Annalies,On the Way;Pho Nachos,Reid,Ordered;Sushi,Omar,Delivered;";

	char title[] = "Your orders so far:";
	// Shade out the background
	for(i = 0; i < 480; i ++){
		//HLine(int x1, int y1, int length, int Colour)
		HLine(0, i, 800, GRAY);
	}

	// Write title
	for(i = 0; i < strlen(title); i++){
		OutGraphicsCharFont2a((400-(strlen(title)*6))+i*12, 25, BLACK, WHITE, title[i], 0);
	}

	// Draw the table
	VLine(margin, tablestart, 325, BLACK);
	VLine(margin + itemwidth, tablestart, 325, BLACK);
	VLine(margin + namewidth + itemwidth, tablestart, 325, BLACK);
	VLine(margin + statuswidth + namewidth + itemwidth, tablestart, 325, BLACK);

	// Draw horizontal lines of table
	for(i = 0; i < 14; i++){
		HLine(margin, tablestart + i*vspace, 800 - 2*margin, BLACK);
	}

	// Print Item column title
	for(i = 0; i < strlen(item); i++){
		OutGraphicsCharFont2a(margin + 10 +i*12, tablestart - vspace/2, BLACK, WHITE, item[i], 0);
	}

	// Print customer name column title
	for(i = 0; i < strlen(name); i++){
		OutGraphicsCharFont2a(margin + 10 + itemwidth +i*12, tablestart - vspace/2, BLACK, WHITE, name[i], 0);
	}

	// Print status column title
	for(i = 0; i < strlen(status); i++){
		OutGraphicsCharFont2a( margin + 10 + itemwidth + statuswidth+i*12, tablestart - vspace/2, BLACK, WHITE, status[i], 0);
	}

	// Parsing BS:
	i = 0;
	int semicolon = 0;
	int comma = 0;
	int cursorx = margin + 10;
	int cursory = tablestart + vspace/2;
	int word = 0;
	while(orders[i] != NULL){
		if(orders[i] == ';'){
			semicolon++;
			cursory = tablestart*1.5 + (semicolon - 1)*vspace;
			cursorx = margin + 10;
			comma = 0;
			word = 0;
		} else if(orders[i] == ','){
			comma++;
			word = 0;
			if(comma == 1){
				cursorx = cursorx + itemwidth;
			} else if(comma == 2){
				cursorx = cursorx + statuswidth;
			} else if(comma == 3){
				cursorx = cursorx + statuswidth;
			}
		} else{
			OutGraphicsCharFont2a(cursorx + word*10, cursory, BLACK, WHITE, orders[i], 0);
			word++;
		}
		i++;
	}

	// Button:
	initElements();

	//createElement(int x, int y, int width, int height, int colour);

	// Create server call button
	Element *callButton = createElement(575, 425, 225, 75, DIM_GRAY);

	// Set actions
	setElementAction(callButton, &drawServerCalled);

	// Draw buttons
	addElementToList(callButton);

	refresh();

	// Write call server button title
	for(i = 0; i < strlen(call); i++){
		OutGraphicsCharFont2a((775 - strlen(call)*10)+i*10, 450, WHITE, WHITE, call[i], 0);
	}
}
