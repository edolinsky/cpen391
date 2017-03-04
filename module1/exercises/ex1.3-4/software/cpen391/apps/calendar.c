/*
 * calendar.c
 *
 *  Created on: Feb 2, 2017
 *      Author: reidoliveira
 */

#include "calendar.h"

#define LINE_WIDTH 3
#define DAYS 7
#define ADJUST 10 // bit of a hack#define ASCII_ZERO 48
int dayHeight;
int displayedWeek;


void displayCalendar(int week) {

	initWiFi();

	usleep(1500000);

	putString(&WIFI_STATUS, &WIFI_TXDATA,
			"get_cal(CALENDAR_ID ,USER , PASSWORD, 0)\n");

	dayHeight = YRES / 6;
	displayedWeek = week;
// background
	screenFill(WHITE);

// division for month name
	int monthLine = dayHeight - 10;
	drawHLine(0, monthLine, XRES, BLACK);
	drawHLine(0, monthLine + 20, XRES, BLACK);

// days
	int dayDiv = XRES / DAYS;
	int numDivs = DAYS - 1;
	int i;
	for (i = 1; i <= numDivs; i++) {
		drawVLine((i * dayDiv) + ADJUST, monthLine, YRES - monthLine, BLACK);
	}

	drawNavigationButtons();
	parseData(listenToWifi());
	refresh();

}

void drawNavigationButtons() {
	Element * back = createElement(45, 20, BUTTON_WIDTH,
	BUTTON_HEIGHT, WHITE);
	setElementText(back, "back");
	back->textColour = BLACK;
	setElementAction(back, &backFromCalendar);
	addElementToList(back);
	Element * next = createElement(690, 20, BUTTON_WIDTH,
	BUTTON_HEIGHT, WHITE);
	setElementText(next, "next");
	next->textColour = BLACK;
	setElementAction(next, &nextWeek);
	addElementToList(next);
	Element * prev = createElement(600, 20, BUTTON_WIDTH,
	BUTTON_HEIGHT, WHITE);
	setElementText(prev, "prev");
	prev->textColour = BLACK;
	setElementAction(prev, &prevWeek);
	addElementToList(prev);
}

void nextWeek() {
	printf("Hello, show next week");
	displayCalendar(displayedWeek + 1);
}

void prevWeek() {
	printf("Hello, show previous week");
	displayCalendar(displayedWeek - 1);
}

void backFromCalendar(){

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

void writeMonth(char* month) {
	Element * monthText = createElement( MONTHTEXT_X, MONTHTEXT_Y,
			10 * FONT2_WIDTH, 50, -1);
	setElementText(monthText, month);
	addElementToList(monthText);
}

void writeDay(char* day, int pos) {

	char* dayText = malloc(sizeof day);
	strcpy(dayText, day);

	int dayDiv = XRES / DAYS;
	int start = dayDiv / 2;
	printf("HERE %d\n", start + (pos * dayDiv));
	Element * dayNum = createElement(start + (pos * dayDiv), dayHeight - 5,
			2 * FONT2_WIDTH, 30, -1);
	dayNum->textColour = BLACK;
	setElementText(dayNum, dayText);
	addElementToList(dayNum);
}

/*
 *
 February
 12    13    14    15    16    17    18
 2    17:0    18:0    Soccer Practice
 2    18:0    19:0    Dinner with Sam
 1    17:0    18:0    Soccer Practice
 0    13:0    14:0    Lunch with Brad
 3    7:30    11:30    ungh
 *
 */

char* listenToWifi() {
	putString(&WIFI_STATUS, &WIFI_TXDATA, "read_file(calendar_file)\n");
	int maxBuf = 1000;
	char *buf = malloc(maxBuf);
	int i = 0;
	while (i < maxBuf) {
		char x = getChar(&WIFI_STATUS, &WIFI_RXDATA);
		if (x == '`')
			break;
		buf[i++] = x;
	}
	return buf;
}

void parseData(char* buf) {

	int i = 0;
	while (buf[i] != '\n') {
		printf("%c", buf[i]);
		i++;
	}
	printf("Past first line\n");

	i++;

	while (buf[i] != '\n') {
		printf("%c", buf[i]);
		i++;
	}

	i++;

	// write month
	char month[50];
	int k = 0;
	while (buf[i] != '\n') {
		month[k] = buf[i];
		i++;
		k++;
	}
	month[k] = '\0';

	writeMonth(month);
	// refresh();
	i++; // \n
	// write days
	char day[3];
	int j = 0;
	int daynum = 0;
	while (1) {
		if (buf[i] == '\n') {
			day[j] = '\0';
			j = 0;
			writeDay(day, daynum);
			break;
		} else if (buf[i] != ' ') {
			day[j++] = buf[i];
		} else if (buf[i] == ' ' && buf[i + 1] == ' ') {

		} else {
			day[j] = '\0';
			j = 0;
			writeDay(day, daynum);
			daynum++;
		}
		i++;
	}

}
