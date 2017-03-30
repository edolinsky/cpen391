#include "mainmenu.h"
#include <stdio.h>
#include <unistd.h>
#include "serial/touch.h"
#include "serial/graphics.h"
#include "serial/colours.h"
#include "successScreen.h"
#include "serverCalled.h"
#include "apps/authenticate.h"

#define TIME_REQUEST_DELAY 2000000 // two seconds.

void drawMenu(void){
	int i;
	int leftJust = 150;
	int instrStart = 150;
	int incr = 15;
	char title[] = "Welcome to Koerners Pub!";
	//char firstline[] = "Welcome to";
	char getStarted[] = "Lets get started!";
	char company[] = "Powered by Resty";
	char instructions1[] = "Resty is a self ordering system that connects with your Android phone.";
	char instructions2[] = "To get started follow these steps:";
	char instructions3[] = "1. Download the Resty app from the Google Play Store";
	char instructions4[] = "2. Turn on your phones Bluetooth";
	char instructions5[] = "3. Connect to RESTYTABLE5";
	char instructions6[] = "4. Launch the Resty app";
	char instructions7[] = "5. Enter the following PIN when prompted";
	char instructions8[] = "6. Press the 'Lets get started!' button";
	char PIN[] = "Your table pin:";

	get_time();
	usleep(TIME_REQUEST_DELAY);
	int time = read_time();

	char* randomPin = generate_random_pin(time);

	// Shade out the background
	for(i = 0; i < 480; i ++){
		//HLine(int x1, int y1, int length, int Colour)
		HLine(0, i, 800, GRAY);
	}

	// Write company name in lower right corner
	for(i = 0; i < strlen(company); i++){
		OutGraphicsCharFont1((750-(strlen(company)*7))+i*7, 465, BLACK, WHITE, company[i], 0);
	}

	// Write title
	for(i = 0; i < strlen(title); i++){
		OutGraphicsCharFont2a((400-(strlen(title)*6))+i*12, 75, BLACK, WHITE, title[i], 0);
	}

	// write the instructions
	for(i = 0; i < strlen(instructions1); i++){
		OutGraphicsCharFont1(leftJust+i*7, instrStart, BLACK, WHITE, instructions1[i], 0);

	}
	for(i = 0; i < strlen(instructions2); i++){
		OutGraphicsCharFont1(leftJust+i*7, instrStart + incr, BLACK, WHITE, instructions2[i], 0);
	}
	for(i = 0; i < strlen(instructions3); i++){
		OutGraphicsCharFont1(leftJust+i*7, instrStart + incr*2, BLACK, WHITE, instructions3[i], 0);
	}
	for(i = 0; i < strlen(instructions4); i++){
		OutGraphicsCharFont1(leftJust+i*7, instrStart + incr*3, BLACK, WHITE, instructions4[i], 0);
	}
	for(i = 0; i < strlen(instructions5); i++){
		OutGraphicsCharFont1(leftJust+i*7, instrStart+ incr*4, BLACK, WHITE, instructions5[i], 0);
	}
	for(i = 0; i < strlen(instructions6); i++){
		OutGraphicsCharFont1(leftJust+i*7, instrStart+ incr*5, BLACK, WHITE, instructions6[i], 0);
	}
	for(i = 0; i < strlen(instructions7); i++){
		OutGraphicsCharFont1(leftJust+i*7, instrStart+ incr*6, BLACK, WHITE, instructions7[i], 0);
	}
	for(i = 0; i < strlen(instructions8); i++){
		OutGraphicsCharFont1(leftJust+i*7, instrStart+ incr*7, BLACK, WHITE, instructions8[i], 0);
	}

	initElements();

	//createElement(int x, int y, int width, int height, int colour);

	// Create three buttons
	Element *launchButton = createElement(100, 350, 600, 100, DIM_GRAY);

	// Set actions
	setElementAction(launchButton, &drawSuccess);

	// Draw buttons
	addElementToList(launchButton);

	refresh();

	// Write get started button title
	for(i = 0; i < 17; i++){
		OutGraphicsCharFont2a(310+i*10, 400, WHITE, WHITE, getStarted[i], 0);
	}

	for(i = 0; i < strlen(randomPin); i++){
		OutGraphicsCharFont2a((400 - strlen(randomPin)*5) + i*10, 300, BLACK, BLACK, randomPin[i], 0);
	}

	for(i = 0; i < strlen(PIN); i++){
		OutGraphicsCharFont2a((400 - strlen(PIN)*5) + i*10, 275, BLACK, BLACK, PIN[i], 0);
	}

	free(randomPin);


	listenToTouches();
}
