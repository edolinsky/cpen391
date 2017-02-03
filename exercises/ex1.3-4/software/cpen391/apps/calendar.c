/*
 * calendar.c
 *
 *  Created on: Feb 2, 2017
 *      Author: reidoliveira
 */

#include "../serial/graphics.h"
#include "../main.h"

#define LINE_WIDTH 3
#define DAYS 7
#define ADJUST 10

int dayHeight;

void displayCalendar() {
	dayHeight = YRES / 4;

	// background
	screenFill(WHITE);

	// division for month name
	int monthLine = dayHeight - 10;
	drawHLine(0, monthLine, XRES, BLACK);
	drawHLine(0, monthLine + 20, XRES, BLACK);

	// days
	int dayDiv = XRES/DAYS;
	int numDivs = DAYS-1;
	int i;
	for (i = 1; i <= numDivs; i++) {
		drawVLine((i * dayDiv) + ADJUST, monthLine, YRES - monthLine, BLACK);
	}

	fillDays(1);

}

/*
 * draws multiple horizontal lines centered on y to equal a certain thickness
 */
void drawHLine(int x, int y, int length, int colour) {

	int i;
	for (i = y - (LINE_WIDTH / 2); i < y + (LINE_WIDTH / 2); i++) {
		HLine(x, i, length, colour);
	}
}

/*
 * draws multiple vertical lines centered on x to equal a certain thickness
 */
void drawVLine(int x, int y, int length, int colour) {
	int i;
	for (i = x - (LINE_WIDTH / 2); i < x + (i < LINE_WIDTH); i++) {
		VLine(i, y, length, colour);
	}
}

void fillDays(int startDay) {
//	int dayDiv = XRES/DAYS/2;
//	int i;
//	for (i = 1; i <= DAYS - 1; i++) {
//		OutGraphicsCharFont2a((i * dayDiv) + ADJUST, dayHeight, BLACK, WHITE,
//				(int)'A', FALSE);
//	}

	OutGraphicsCharFont2a(50, 50, BLACK, WHITE,
			65, FALSE);
}
