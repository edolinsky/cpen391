/*
 * touch.c
 *
 *  Created on: Feb 1, 2017
 *      Author: reidoliveira
 */

#include <stdio.h>
#include "touch.h"
#include "../main.h"

/*****************************************************************************
 ** Initialise touch screen controller
 *****************************************************************************/
void initTouch(void) {
	// Program 6850 and baud rate generator to communicate with touchscreen
	// send touchscreen controller an "enable touch" command

	// setting up the 6850
	TOUCH_CONTROL = 0x95;
	TOUCH_BAUD = 0x07;

	// now that it is set up, we talk to the touch controller through rx & tx
	putChar(&TOUCH_STATUS, &TOUCH_TXDATA, 0x55);
	putChar(&TOUCH_STATUS, &TOUCH_TXDATA, 0x01);
	putChar(&TOUCH_STATUS, &TOUCH_TXDATA, 0x12);

	while (getChar(&TOUCH_STATUS, &TOUCH_RXDATA) != 0)
		; // wait for OK

	printf("touch screen initialized\n");
}

/*****************************************************************************
 ** test if screen touched
 *****************************************************************************/
int ScreenTouched(void) {
	// return TRUE if any data received from 6850 connected to touchscreen
	// or FALSE otherwise
	return getChar(&TOUCH_STATUS, &TOUCH_RXDATA) == 129;
}

/*****************************************************************************
 ** wait for screen to be touched
 *****************************************************************************/
void waitForTouch() {
	while (!ScreenTouched())
		;
}

/*****************************************************************************
 * This function waits for a touch screen press event and returns X,Y coord
 *****************************************************************************/
Point getPress(void) {

	Point p1;
	char bytes[4];
	while (getChar(&TOUCH_STATUS, &TOUCH_RXDATA) != 129)
		;
	int i;
	for (i = 0; i < 4; i++)
		bytes[i] = getChar(&TOUCH_STATUS, &TOUCH_RXDATA);
	p1.x = 480 * ((int) bytes[0] + (int) (bytes[1] << 7)) / 4096.0;
	p1.y = 800 * ((int) bytes[2] + (int) (bytes[3] << 7)) / 4096.0;
	return p1;
}

/*****************************************************************************
 * This function waits for a touch screen release event and returns X,Y coord
 *****************************************************************************/
Point getRelease(void) {

	Point p1;
	char bytes[4];
	while (getChar(&TOUCH_STATUS, &TOUCH_RXDATA) != 128)
		;
	int i;
	for (i = 0; i < 4; i++)
		bytes[i] = getChar(&TOUCH_STATUS, &TOUCH_RXDATA);
	p1.x = 480 * ((int) bytes[0] + (int) (bytes[1] << 7)) / 4096.0;
	p1.y = 800 * ((int) bytes[2] + (int) (bytes[3] << 7)) / 4096.0;
	return p1;
}
