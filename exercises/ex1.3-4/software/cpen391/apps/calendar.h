/*
 * calendar.h
 *
 *  Created on: Feb 2, 2017
 *      Author: reidoliveira
 */

#ifndef CALENDAR_H_
#define CALENDAR_H_

#include "../serial/graphics.h"
#include "../main.h"
#include "../serial/colours.h"
#include "../fonts/fonts.h"
#include "../UIElements/element.h"
#include "../serial/wifi.h"
#include <string.h>

#define MONTHTEXT_X 360
#define MONTHTEXT_Y 20
#define BUTTON_WIDTH 80
#define BUTTON_HEIGHT 40

void displayCalendar(int week);
void drawHLine(int x, int y, int length, int colour);
void drawVLine(int x, int y, int length, int colour);
void writeMonth(char* month);
void writeDay(char* day, int pos);
void drawNavigationButtons();
void nextWeek();
void prevWeek();
char* listenToWifi();
void backFromCalendar();

#endif /* CALENDAR_H_ */
