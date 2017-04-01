#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include "apps/order.h"
#include "serial/wifi.h"
#include "orderPage.h"
#include "serverCalled.h"
#include "main.h"

#define ORDER_REQUEST_DELAY 2000000 // two seconds.

void drawOrderPage(void){
	int i = 0;
	int vspace = 25;
	int itemwidth = 400;
	int statuswidth = 150;
	int namewidth = 150;
	int margin = 50;
	int tablestart = 75;
	char call[] = "Call your server";
	char reload[] = "Refresh";
	char item[] = "Item";
	char name[] = "Customer";
	char status[] = "Status";

	// Set app context so that we can return to this screen.
	app_context = ORDER_CONTEXT;

	// Get order information from server via WiFi chip.
	get_order("test_user", "26a00ff96d");
	usleep(ORDER_REQUEST_DELAY);
	char* orders = read_order();
	printf(orders);

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

	// Order parsing:
	i = 0;
	int lines = 0;
	int comma = 0;
	int cursorx = margin + 10;
	int cursory = tablestart + vspace/2;
	int word = 0;
	while(orders[i] != '\0'){
		if(orders[i] == '\n'){
			lines++;
			cursory = tablestart*1.5 + (lines - 1)*vspace;
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

	free(orders);

	// Buttons:
	initElements();

	// Create server call and reload buttons
	//                                    x,   y, wid, ht, colour
	Element *callButton = createElement(575, 425, 225, 75, DIM_GRAY);
	Element *reloadButton = createElement(0, 425, 225, 75, DIM_GRAY);

	// Set actions
	setElementAction(callButton, &drawServerCalled);
	setElementAction(reloadButton, &drawOrderPage);

	// Draw buttons
	addElementToList(callButton);
	addElementToList(reloadButton);

	refresh();

	// Write call server button label.
	for(i = 0; i < strlen(call); i++){
		OutGraphicsCharFont2a((775 - strlen(call)*10)+i*10, 450, WHITE, WHITE, call[i], 0);
	}

	// Write refresh button label.
	for(i = 0; i < strlen(reload); i++){
		OutGraphicsCharFont2a(75 + i*10, 450, WHITE, WHITE, reload[i], 0);
	}
}
