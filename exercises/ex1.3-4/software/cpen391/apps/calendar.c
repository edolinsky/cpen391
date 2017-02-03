/*
 * calendar.c
 *
 *  Created on: Feb 2, 2017
 *      Author: reidoliveira
 */

#include "../serial/graphics.h"
#include "../main.h"
#include "../fonts/fonts.h"
#include "calendar.h"

#define LINE_WIDTH 3
#define DAYS 7
#define ADJUST 10 // bit of a hack
#define ASCII_ZERO 48

char * months[] = {
		"January",
		"February",
		"March",
		"April",
		"May",
		"June",
		"July",
		"August",
		"September",
		"October"
		"November"
		"December"};

int dayHeight;

void displayCalendar() {
	dayHeight = YRES / 6;

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

	writeMonth(1);
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

void writeMonth(int month) {
	char *name = months[month-1];
	int length = 7; // hack
	int i;
	for(i = 0; i < length; i++) {
		OutGraphicsCharFont2a(i*15 + 100, dayHeight - 24, BLACK, WHITE, // magic nums are hacks
				name[i], FALSE);
	}
}

void fillDays(int startDay) {
	int dayDiv = XRES/DAYS;
	int start = dayDiv/2;
	int i;
	for (i = 0; i < DAYS; i++) {
		OutGraphicsCharFont2a(start + (i * dayDiv) + ADJUST, dayHeight - 5, BLACK, WHITE,
				ASCII_ZERO + startDay + i, FALSE);
	}
}
