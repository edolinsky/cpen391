/*
 * calendar.h
 *
 *  Created on: Feb 2, 2017
 *      Author: reidoliveira
 */

#ifndef CALENDAR_H_
#define CALENDAR_H_

void displayCalendar();
void drawHLine(int x, int y, int length, int colour);
void drawVLine(int x, int y, int length, int colour);
void writeMonth(int month);
void fillDays(int startDay);

#endif /* CALENDAR_H_ */
